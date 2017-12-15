#!/usr/bin/env groovy​

package nl.digitalme.indy

// Validation step for Linux distribution on Docker
def valDocker(config, String dist) {
	echo "Static code validation on ${dist}"
	// Checkout the source
	checkout scm
	try {
		// Extract Dockerfile from shared lib to 'ci' folder
		writeFile(
			file: 'ci/Dockerfile',
			text: libraryResource('dist/' + dist + '/validate.dockerfile')
		)
		// Build docker image from 'ci' folder and use flake to validate
		docker.build(config.name + '-validate-' + dist + ':' + config.branch, 'ci').inside { sh "python3.5 -m flake8" }
	} finally {
		echo "Static code validation on ${dist}: Cleanup"
		step([$class: 'WsCleanup'])
	}
}

// Generates a Map of node to execute validation steps per distribution
Map distNode(config) {
	Map mDists = [:]
	
	config.dists.each { dist ->
		if (dist =~ /^(win|mac)/) {
			echo "Validation on ${dist} not (yet) implemented"
		} else {
			echo "Validation on ${dist} will be done via docker"
			mDists += [
				(dist): {
					node(label: config.lb.docker) {
						valDocker(config, config.dists[0])
					}
				}
			]
		}
	}
	return mDists
}

// Default method
def call(config, tasks = []) {
	if (config.st.validate) {
		stage('Validate') {
			if (config.verbose) echo "Validation for ${config.name} begins here"

			parallel(
				distNode(config)
//				(config.dists[0]): {
//					node(label: config.lb.docker) {
//						valDocker(config, config.dists[0])
//					}
//				},
//				(config.dists[1]): {
//					node(label: config.lb.docker) {
//						valDocker(config, config.dists[1])
//					}
//				}
			)
		}
		if (config.verbose) echo "Validation for ${config.name} ends here"
	} else {
		echo "Validation will be skipped (config.st.validate = ${config.st.validate})"
	}
}