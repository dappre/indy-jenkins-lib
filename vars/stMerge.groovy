#!/usr/bin/env groovy​

def call(config, tasks = []) {
	if (config.stages.contains('merge')) {
		stage('Merge') {
			if (config.verbose) echo "Merging for ${config.name} begins here"
			if (config.verbose) echo "Merging for ${config.name} ends here"
		}
	} else {
		echo "Merging will be skipped (config.stages.merge = ${config.stages.merge})"
	}
}