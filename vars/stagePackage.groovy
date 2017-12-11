#!/usr/bin/env groovy​

def call(config) {
	if (config.package) {
		echo 'Packaging goes here'
	} else {
		echo "Packaging will be skipped (config.package = ${config.package})"
	}
}