#!groovy

package nl.digitalme.indy

public static final Map stages = [
	'merge',
	'validate',
	'test',
	'package',
	'approve',
	'release',
	'deliver',
	'notify',
]

public static final Map dists = [
	'ubuntu-16.x86_64',
	'centos-7.x86_64',
	'win-10.x86_64',
]

def call(String name) {
	// Define parameters and their default values
	properties([
		parameters([
			// Allow to run extended stages when triggered manually
			booleanParam(name: 'extended', defaultValue: false, description: 'Enable extended stages (requires extended lib)'),
			// Distribution to build on
			textParam(name: 'distList', defaultValue: dists.join("\n"), description: 'List of distribution to use'),
//			textParam(name: 'distList', defaultValue: ['ubuntu16.x86_64', 'centos7.x86_64', 'win10.x86_64'].join("\n")),
//			textParam(name: 'distList', defaultValue: libraryResource('dist/*')),
			// Label names required to run stages
			string(name: 'lbDocker', defaultValue: 'docker', description: 'Node label to run docker commands'),
//			string(name: 'lbMacOS10', defaultValue: 'mac', description: 'Node label for Mac OS X'),
//			string(name: 'lbWin10', defaultValue: 'windows', description: 'Node label for Windows 10'),
			// Options to tune the above stages 
			choice(name: 'verbose', choices: ['0', '1', '2'].join("\n"), defaultValue: '1', description: 'Control verbosity'),
			booleanParam(name: 'dryRun', defaultValue: false, description: 'Enable dryRun mode (no external changes, only show what should be done)'),
			booleanParam(name: 'failFast', defaultValue: false, description: 'Enable failFast option'),
			booleanParam(name: 'pkgDeps', defaultValue: false, description: 'Enable deps packaging'),
			// Allow to skip some stages for testing purpose
			booleanParam(name: 'skipMerge', defaultValue: false, description: 'Skip merging stage'),
			booleanParam(name: 'skipValidate', defaultValue: false, description: 'Skip validation stage'),
			booleanParam(name: 'skipTest', defaultValue: false, description: 'Skip testing stage'),
			booleanParam(name: 'skipPackage', defaultValue: false, description: 'Skip packaging stage'),
			booleanParam(name: 'skipApprove', defaultValue: false, description: 'Skip approval stage (requires extended lib)'),
			booleanParam(name: 'skipRelease', defaultValue: false, description: 'Skip release stage (requires extended lib)'),
			booleanParam(name: 'skipDeliver', defaultValue: false, description: 'Skip delivery stage (requires extended lib)'),
			booleanParam(name: 'skipNotify', defaultValue: false, description: 'Skip notification stage (requires extended lib)'),
			// Parameters to load the extended library
			string(name: 'libExtRemote', defaultValue: 'https://code.digital-me.nl/git/DEVops/IndyJenkinsLibExt.git', description: 'Git URL of the shared library'),
			string(name: 'libExtBranch', defaultValue: 'master', description: 'Git branch for the Extended shared library'),
			string(name: 'libExtCredId', defaultValue: 'inexistent', description: 'Credentials to access the Extended shared library'),
		])
	])

	// Instanciate a configuration object based on the parameters
	def config = new plConfig(name, params)

	// Load Extended library if available and update configuration accordingly
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
		echo 'Extended shared library loaded: extended features are supported'
	} catch (error) {
		echo 'Extended shared library could NOT be loaded: extended features are disabled'
		if(config.verbose) {
			echo "Warning message:\n${error.message}"
		}
		config.extended = false
	}

	return config
}