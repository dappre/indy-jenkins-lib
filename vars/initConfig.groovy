#!groovy

package indyjenkinslib

def call(String name) {
	// Define parameters and their default values
	properties([
		parameters([
			// Stages to run
			booleanParam(name: 'stValidate', defaultValue: true, description: 'Enable validation stage'),
			booleanParam(name: 'stCompile', defaultValue: true, description: 'Enable compilation stage'),
			booleanParam(name: 'stTest', defaultValue: true, description: 'Enable testing stage'),
			booleanParam(name: 'stPackage', defaultValue: true, description: 'Enable packaging stage'),
			booleanParam(name: 'stApprove', defaultValue: false, description: 'Enable approval stage (if relevant)'),
			booleanParam(name: 'stRelease', defaultValue: false, description: 'Enable release stage (if relevant)'),
			booleanParam(name: 'stDeliver', defaultValue: false, description: 'Enable delivery stage (if relevant)'),
			booleanParam(name: 'stNotify', defaultValue: true, description: 'Enable notification stage (if possible)'),
			// Options to tune the above stages 
			booleanParam(name: 'verbose', defaultValue: false, description: 'Enable verbose mode'),
			booleanParam(name: 'dryRun', defaultValue: false, description: 'Enable dryRun mode (no external changes, only show what should be done)'),
			booleanParam(name: 'failFast', defaultValue: false, description: 'Enable failFast option'),
			booleanParam(name: 'pkgDeps', defaultValue: false, description: 'Enable deps packaging'),
			// Parameters to load the extended library
			string(name: 'libExtRemote', defaultValue: 'https://code.digital-me.nl/git/DEVops/IndyJenkinsLibExt.git', description: 'Git URL of the shared library'),
			string(name: 'libExtBranch', defaultValue: 'master', description: 'Git branch for the Extended shared library'),
			string(name: 'libExtCredId', defaultValue: 'inexistent', description: 'Credentials to access the Extended shared library'),
		])
	])

	// Define the configuration stages and options based on the parameters
	def config = [
		// Project name
		name: 			name,
		// Stages
		st: [
			validate:	params.stValidate,
			compile:	params.stCompile,
			test:		params.stTest,
			package:	params.stPackage,
			approve:	params.stApprove,
			release:	params.stRelease,
			deliver:	params.stDeliver,
			notify:		params.stNotify,
		],
		// Other options
		verbose:		params.verbose,
		dryRun:			params.dryRun,
		failfast:		params.failFast,
		pkgDeps:		params.pkgDeps,
		// Flag for the extend support
		extended: 		false,
	]

	// Load Extended library if available
	echo 'Trying to load Extended library...'
	try {
		library(
			identifier: "libExt@${params.libExtBranch}",
			retriever: modernSCM([
				$class: 'GitSCMSource',
				remote: params.libExtRemote,
				credentialsId: params.libExtCredId
			])
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