#!groovy

package nl.digitalme.indy

def call(String name) {
	// Define parameters and their default values
	properties([
		parameters([
			// Allow to run extended stages when triggered manually
			booleanParam(name: 'extended', defaultValue: false, description: 'Enable extanded stages (requires extended lib)'),
			// Allow to skip some stages for testing purpose
			booleanParam(name: 'skipValidate', defaultValue: false, description: 'Skip validation stage'),
			booleanParam(name: 'skipCompile', defaultValue: false, description: 'Skip compilation stage'),
			booleanParam(name: 'skipTest', defaultValue: false, description: 'Skip testing stage'),
			booleanParam(name: 'skipPackage', defaultValue: false, description: 'Skip packaging stage'),
			booleanParam(name: 'skipApprove', defaultValue: false, description: 'Skip approval stage (requires extended lib)'),
			booleanParam(name: 'skipRelease', defaultValue: false, description: 'Skip release stage (requires extended lib)'),
			booleanParam(name: 'skipDeliver', defaultValue: false, description: 'Skip delivery stage (requires extended lib)'),
			booleanParam(name: 'skipNotify', defaultValue: false, description: 'Skip notification stage (requires extended lib)'),
			// Label names required to run stages
			string(name: 'lbDocker', defaultValue: 'docker', description: 'Node label to run docker commands'),
			string(name: 'lbMacOS10', defaultValue: 'mac', description: 'Node label for Mac OS X'),
			string(name: 'lbWin10', defaultValue: 'windows', description: 'Node label for Windows 10'),
			// Distribution to build on
			textParam(name: 'osList', defaultValue: ['ubuntu', 'centos', 'windows'],join("\n")),
			choice(
				name: 'distribution',
				choices: ['all', 'linux', 'unix', 'ubuntu16', 'centos7', 'macos10', 'win10'].join("\n"),
				defaultValue: 'linux',
				description: 'Choose on which distribution(s) to build on'
			),
			// Options to tune the above stages 
			choice(name: 'verbose', choices: ["0", "1", "2"].join("\n"), defaultValue: "1, description: 'Enable verbose mode'),
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
	def config = new plConfig(name, params)
/*	def config = [
		// Project name
		name: 			name,
		// Flag for the extended lib support
		extended: 		params.extended,
		// Stages
		st: [
			validate:	!params.skipValidate,
			compile:	!params.skipCompile,
			test:		!params.skipTest,
			package:	!params.skipPackage,
			approve:	!params.skipApprove,
			release:	!params.skipRelease,
			deliver:	!params.skipDeliver,
			notify:		!params.skipNotify,
		],
		lb: [
			docker:		params.lbDocker,
		],
		// Other options
		verbose:		params.verbose,
		dryRun:			params.dryRun,
		failfast:		params.failFast,
		pkgDeps:		params.pkgDeps,
	]
*/
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