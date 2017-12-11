#!/usr/bin/env groovy​

def call(config, codes = []) {
	if (config.st.package) {
		stage('Package') {
			echo "Packaging for ${config.name} goes here"
		}
	} else {
		echo "Packaging will be skipped (st.package = ${st.package})"
	}
}