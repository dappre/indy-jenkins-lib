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
FROM centos:centos7.4.1708

# Install all current updates
RUN yum -y upgrade

# Install common requirements
RUN yum -y install \
	git \
	wget \
	unzip

# Install Python 3.5 from PIUS repo
RUN yum -y install https://centos7.iuscommunity.org/ius-release.rpm && yum -y install \
	python35u-pip \
	python35u-tools \
	python35u-setuptools \
	python35u-libs

# Install and update modules for validation
RUN pip3.5 install -U \ 
	pip \ 
	setuptools \
	pep8 \
	pep8-naming \
	flake8
