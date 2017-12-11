#!/usr/bin/env groovy​

def call(config) {
	if (config.compilation) {
		stage('Compilation') {
			echo 'Compilation goes here'
		}
	} else {
		echo "Compilation will be skipped (config.compile = ${config.compile})"
	}
}