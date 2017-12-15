#!/usr/bin/env groovy​

package nl.digitalme.indy

def call(config, tasks = []) {
	if (config.st.validate) {
		stage('Validate') {
			if (config.verbose) echo "Validation for ${config.name} begins here"
			node(label: config.lb.docker) {
				try {
					echo 'Static code validation'
					// Checkout the source
					checkout scm
					config.dists.each { dist ->
						// Extract Dockerfile from shared lib to 'ci' folder
						writeFile(
								file: 'ci/Dockerfile',
								text: libraryResource('dist/' + dist + '/validate.dockerfile')
								)
						// Build docker image from 'ci' folder and use flake to validate
						docker.build(config.name + '-validate-' + dist + ':' + config.branch, 'ci').inside { sh "python3.5 -m flake8" }
					}
				}
				finally {
					echo 'Static code validation: Cleanup'
					step([$class: 'WsCleanup'])
				}
			}
			if (config.verbose) echo "Validation for ${config.name} ends here"
		}
	} else {
		echo "Validation will be skipped (config.st.validate = ${config.st.validate})"
	}
}