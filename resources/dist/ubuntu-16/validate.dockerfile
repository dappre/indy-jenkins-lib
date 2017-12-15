# Pull base image from official repo
FROM ubuntu:16.04

# Install all current updates
RUN apt-get update -y

# Install common requirements
RUN apt-get install -y \
	git \
	wget \
	unzip

# Install Python 3.5 
RUN apt-get install -y \
	python3.5 \
	python3-pip \
	python-setuptools \
	python3-nacl

# Install and update modules for validation
RUN pip3 install -U \ 
	pip \ 
	setuptools \
	pep8 \
	pep8-naming \
	flake8
