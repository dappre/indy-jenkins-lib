#!/usr/bin/env groovy​

def call(config) {
	if (config.test) {
		echo 'Testing goes here'
	} else {
		echo "Testing will be skipped (config.test = ${config.test})"
	}
}