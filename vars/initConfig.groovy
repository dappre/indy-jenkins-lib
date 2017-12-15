#!/usr/bin/env groovy

//package nl.digitalme.indy

// Return ArrayList of defined distributions
def listDists() {
	return [
		'ubuntu-16',
		'centos-7',
//		'macosx-10',
//		'windows-10',
	]
}

// Return ArrayList of defined stages
def	listStages() {
	return [
		'merge',
		'validate',
		'test',
		'package',
		'approve',
		'release',
		'deliver',
		'notify',
	]
}

// Default method
def call(String name) {
	// Define parameters and their default values
	properties([
		parameters([
			// Allow to run extended stages when triggered manually
			booleanParam(name: 'extended', defaultValue: false, description: 'Enable extended stages (requires extended lib)'),
			// Distribution to build on
			textParam(name: 'listDists', defaultValue: listDists().join("\n"), description: 'List of distribution to use'),
			// Label names required to run stages
			string(name: 'labelDocker', defaultValue: 'docker', description: 'Node label to run docker commands'),
			//			string(name: 'lbMacOS10', defaultValue: 'mac', description: 'Node label for Mac OS X 10'),
			//			string(name: 'lbWindows10', defaultValue: 'windows', description: 'Node label for Windows 10'),
			// Options to tune the above stages
			choice(name: 'verbose', choices: ['1', '2', '0'].join("\n"), defaultValue: '1', description: 'Control verbosity'),
			booleanParam(name: 'dryRun', defaultValue: false, description: 'Enable dryRun mode (no external changes, only show what should be done)'),
			booleanParam(name: 'failFast', defaultValue: false, description: 'Enable failFast option'),
			booleanParam(name: 'pkgDeps', defaultValue: false, description: 'Enable deps packaging'),
			// Allow to skip some stages for testing purpose
			textParam(name: 'listStages', defaultValue: listStages().join("\n"), description: 'List of stages to go through (when relevant)'),
			// Parameters to load the extended library
			string(name: 'libExtRemote', defaultValue: 'https://code.digital-me.nl/git/DEVops/IndyJenkinsLibExt.git', description: 'Git URL of the shared library'),
			string(name: 'libExtBranch', defaultValue: 'master', description: 'Git branch for the Extended shared library'),
			string(name: 'libExtCredId', defaultValue: 'inexistent', description: 'Credentials to access the Extended shared library'),
		])
	])

	// Instanciate a configuration object based on the parameters
	def config = new plConfig(name, params, env.BRANCH_NAME)

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