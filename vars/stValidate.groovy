#!/usr/bin/env groovy​

def call(config, tasks = []) {
	if (config.st.validate) {
		stage('Validate') {
			if (config.verbose) echo "Validation for ${config.name} begins here"
			node(label: config.lb.docker) {
				staticCodeValidation()
			}
			if (config.verbose) echo "Validation for ${config.name} ends here"
		}
	} else {
		echo "Validation will be skipped (config.st.validate = ${config.st.validate})"
	}
}