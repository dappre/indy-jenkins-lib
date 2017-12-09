#!/usr/bin/env groovy

def call() {
	if (options.compile ) {
		stage('Compilation') {
			echo 'Compilation goes here'
		}
	} else {
		echo "Compilation will be skipped (option.compile = ${options.compile})"
	}
}