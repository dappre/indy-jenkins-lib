#!groovy

stPackage indyjenkinslib

def call(String name) {
	// Define parameters and their default values
	properties([
		parameters([
			booleanParam(name: 'stValidate', defaultValue: true, description: 'Enable validation stage'),
			booleanParam(name: 'stCompile', defaultValue: true, description: 'Enable compilation stage'),
			booleanParam(name: 'stTest', defaultValue: true, description: 'Enable testing stage'),
			booleanParam(name: 'stPackage', defaultValue: true, description: 'Enable packaging stage'),
			booleanParam(name: 'stRelease', defaultValue: false, description: 'Enable release stage (if relevant)'),
			booleanParam(name: 'stDeliver', defaultValue: false, description: 'Enable delivery stage (if relevant)'),
			booleanParam(name: 'notify', defaultValue: true, description: 'Enable notification stage (if possible)'),
			booleanParam(name: 'verbose', defaultValue: false, description: 'Enable verbose mode'),
			booleanParam(name: 'dry', defaultValue: false, description: 'Enable dry mode (no external changes, only show what should be done)'),
			booleanParam(name: 'fast', defaultValue: false, description: 'Enable fail fast option'),
			string(name: 'libExtRemote', defaultValue: 'https://code.digital-me.nl/git/DEVops/IndyJenkinsLibExt.git', description: 'Git URL of the shared library'),
			string(name: 'libExtBranch', defaultValue: 'master', description: 'Git branch for the Extended shared library'),
			string(name: 'libExtCredId', defaultValue: 'inexistent', description: 'Credentials to access the Extended shared library'),
		])
	])

	// Define the configuration options based on the parameters
	def config = [
		st = [
			validate:	params.stValidate,
			compile:	params.stCompile,
			test:		params.stTest,
			package:	params.stPackage,
			release:	params.stRelease,
			deliver:	params.stDeliver,
			notify:		params.stNotify,
		],
		op = [
			verbose:	params.verbose,
			dry:		params.dry,
			fast:		params.fast,
		],
		extended: false,
		name: name,
	]

	// Load Extended library if available
	echo 'Trying to load Extended library...'
	try {
		library identifier: "libExt@${params.libExtBranch}", retriever: modernSCM(
		[$class: 'GitSCMSource',
			remote: params.libExtRemote,
			credentialsId: params.libExtCredId]
		)
		echo 'Extended shared library loaded: extended feature are supported'
		config.extended = true
	} catch (error) {
		echo 'Extended shared library could NOT be loaded: extended feature are disabled'
		if(config.verbose) {
			echo "Warning message:\n${error.message}"
		}
		config.extended = false
	}

	return config
}