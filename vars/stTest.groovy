#!/usr/bin/env groovy​

def call(config, tasks = []) {
	if (config.stages.test) {
		stage('Test') {
			if (config.verbose) echo "Testing for ${config.name} begins here"
			if (config.verbose) echo "Testing for ${config.name} ends here"
		}
	} else {
		echo "Testing will be skipped (config.stages.test = ${config.stages.test})"
	}
}