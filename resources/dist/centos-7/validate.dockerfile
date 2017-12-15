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
