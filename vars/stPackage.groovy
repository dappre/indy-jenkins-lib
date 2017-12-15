#!/usr/bin/env groovy​

def call(config, tasks = []) {
	if (config.stages.contains('package')) {
		stage('Package') {
			if (config.verbose) echo "Packaging for ${config.name} goes here"
			if (config.verbose) echo "Packaging for ${config.name} goes here"
		}
	} else {
		echo "Packaging will be skipped (st.package = ${st.package})"
	}
}