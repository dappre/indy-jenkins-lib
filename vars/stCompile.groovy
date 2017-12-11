#!/usr/bin/env groovy​

def call(config, tasks = []) {
	if (config.st.compile) {
		stage('Compile') {
			if (config.verbose) echo "Compilation for ${config.name} begins here"
			if (config.verbose) echo "Compilation for ${config.name} ends here"
		}
	} else {
		echo "Compilation will be skipped (config.st.compile = ${config.st.compile})"
	}
}