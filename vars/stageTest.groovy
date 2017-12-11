#!/usr/bin/env groovy​

def call(config) {
	if (config.testing) {
		stage('Testing') {
			echo 'Testing goes here'
		}
	} else {
		echo "Testing will be skipped (config.test = ${config.test})"
	}
}