#!/usr/bin/env groovy​

def call(config, codes = []) {
	if (config.st.test) {
		stage('Test') {
			echo "Testing for ${config.name} goes here"
		}
	} else {
		echo "Testing will be skipped (config.st.test = ${config.st.test})"
	}
}