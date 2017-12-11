#!/usr/bin/env groovy​

def call(config, codes = []) {
	if (config.st.compile) {
		stage('Compile') {
			echo "Compilation for ${config.name} goes here"
		}
	} else {
		echo "Compilation will be skipped (config.st.compile = ${config.st.compile})"
	}
}