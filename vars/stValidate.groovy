#!/usr/bin/env groovy​

def call(config, codes = []) {
	if (config.st.validate) {
		stage('Validate') {
			echo "Validation for ${config.name} goes here"
		}
	} else {
		echo "Validation will be skipped (config.st.validate = ${config.st.validate})"
	}
}