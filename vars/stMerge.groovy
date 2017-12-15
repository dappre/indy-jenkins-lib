#!/usr/bin/env groovy​

def call(config, tasks = []) {
	if (config.st.merge) {
		stage('Merge') {
			if (config.verbose) echo "Merging for ${config.name} begins here"
			if (config.verbose) echo "Merging for ${config.name} ends here"
		}
	} else {
		echo "Merging will be skipped (config.st.merge = ${config.st.merge})"
	}
}