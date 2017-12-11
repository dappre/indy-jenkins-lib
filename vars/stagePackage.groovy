#!/usr/bin/env groovy​

def call(config) {
	if (config.packaging) {
		stage('Packaging') {
			echo "Packaging for ${config.name} goes here"
		}
	} else {
		echo "Packaging will be skipped (config.packaging = ${config.packaging})"
	}
}