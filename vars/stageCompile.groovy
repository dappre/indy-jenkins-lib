#!/usr/bin/env groovy

def call() {
	if (config.compile ) {
		stage('Compilation') {
			echo 'Compilation goes here'
		}
	} else {
		echo "Compilation will be skipped (config.compile = ${config.compile})"
	}
}