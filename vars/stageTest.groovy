#!/usr/bin/env groovy​

def call(config) {
	if (config.testing) {
		stage('Testing') {
			echo "Testing for ${config.name} goes here"
		}
	} else {
		echo "Testing will be skipped (config.testing = ${config.testing})"
	}
}