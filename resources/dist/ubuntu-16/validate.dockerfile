#
#  This work is protected under copyright law in the Kingdom of
#  The Netherlands. The rules of the Berne Convention for the
#  Protection of Literary and Artistic Works apply.
#  Digital Me B.V. is the copyright owner.
#
#  Licensed under the Apache License, Version 2.0 (the "License");
#  you may not use this file except in compliance with the License.
#  You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
#  Unless required by applicable law or agreed to in writing, software
#  distributed under the License is distributed on an "AS IS" BASIS,
#  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#  See the License for the specific language governing permissions and
#  limitations under the License.
#

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
	python3-setuptools

# Install some Python dev tools
RUN apt-get install -y \
	python-setuptools \
	python3.5-dev \
	python3-nacl

# Install and update modules for validation
RUN pip3.5 install -U \ 
	pip \ 
	setuptools \
	pep8 \
	pep8-naming \
	flake8
