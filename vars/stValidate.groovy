#!/usr/bin/env groovy​

/*
 * This work is protected under copyright law in the Kingdom of
 * The Netherlands. The rules of the Berne Convention for the
 * Protection of Literary and Artistic Works apply.
 * Digital Me B.V. is the copyright owner.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package nl.digitalme.indy

// Validation step for Linux distribution on Docker
def valDocker(config, String dist) {
	if (config.verbose) echo "Static code validation on ${dist}"
	// Checkout the source
	checkout scm
	try {
		// Extract Dockerfile from shared lib to root workspace folder
		writeFile(
			file: 'Dockerfile',
			text: libraryResource('dist/' + dist + '/validate.dockerfile')
		)
		// Build docker image from 'ci' folder and use flake to validate to whole workspace
		docker.build(config.name + '-validate-' + dist + ':' + config.branch, '.').inside { sh "python3.5 -m flake8" }
	} finally {
		if (config.verbose) echo "Static code validation on ${dist}: Cleanup"
		step([$class: 'WsCleanup'])
	}
}

// Generates a Map of nodes to execute validation steps per distribution
Map distNode(config) {
	Map mDists = [:]
	
	config.dists.each { dist ->
		if (dist =~ /^(win|mac)/) {
			if (config.verbose) echo "Validation on ${dist} not (yet) implemented"
		} else {
			if (config.verbose) echo "Validation on ${dist} will be done via docker (label = ${config.labels.docker})"
			mDists += [
				(dist): {
					node(label: config.labels.docker) {
						valDocker(config, dist)
					}
				}
			]
		}
	}
	return mDists
}

// Default method
def call(config, tasks = []) {
	if (config.stages.contains('validate')) {
		stage('Validate') {
			if (config.verbose) echo "Validation for ${config.name} begins here"
			parallel(distNode(config))
		}
		if (config.verbose) echo "Validation for ${config.name} ends here"
	} else {
		echo "Validation will be skipped (config.stages.validate = ${config.stages.validate})"
	}
}