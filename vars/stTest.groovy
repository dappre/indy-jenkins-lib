#!/usr/bin/env groovy​

def call(config, tasks = []) {
	if (config.st.test) {
		stage('Test') {
			if (config.verbose) echo "Testing for ${config.name} begins here"
			if (config.verbose) echo "Testing for ${config.name} ends here"
		}
	} else {
		echo "Testing will be skipped (config.st.test = ${config.st.test})"
	}
}