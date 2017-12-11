#!groovy

package indyjenkinslib

def call() {
	// Define parameters and their default values
	properties([
		parameters([
			booleanParam(name: 'compile', defaultValue: true, description: 'Enable compilation'),
			booleanParam(name: 'test', defaultValue: true, description: 'Enable tests'),
			booleanParam(name: 'package', defaultValue: true, description: 'Enable packaging'),
			booleanParam(name: 'release', defaultValue: false, description: 'Enable release (if relevant)'),
			booleanParam(name: 'delivery', defaultValue: false, description: 'Enable delivery (if relevant)'),
			booleanParam(name: 'notify', defaultValue: true, description: 'Enable notification (if possible)')
			booleanParam(name: 'dry', defaultValue: false, description: 'Enable dry mode (don\'t release/deliver, only show what should be done)'),
			booleanParam(name: 'fast', defaultValue: false, description: 'Enable fast option'),
			string(name: 'libExtRemote', defaultValue: 'https://code.digital-me.nl/git/DEVops/IndyJenkinsLibExt.git', description: 'Git URL of the shared library'),
			string(name: 'libExtBranch', defaultValue: 'master', description: 'Git branch for the Extended shared library'),
			string(name: 'libExtCredId', defaultValue: 'inexistent', description: 'Credentials to access the Extended shared library'),
		])
	])

	// Define the configuration options based on the parameters
	def config = [
		dry: params.dry,
		fast: params.fast,
		compile: params.compile,
		test: params.test,
		package: params.package,
		release: params.release,
		delivery: params.delivery,
		notify: params.notify,
		extended: false,
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
		echo "Extended shared library NOT loaded: extended feature are disabled (${error.message})"
		config.extended = false
	}

	return config
}