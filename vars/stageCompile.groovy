#!/usr/bin/env groovy​

def call(config) {
	if (config.compilation) {
		stage('Compilation') {
			echo "Compilation for ${config.name} goes here"
		}
	} else {
		echo "Compilation will be skipped (config.compilation = ${config.compilation})"
	}
}