# Pull base image from official repo
FROM centos:centos7.3.1611

# Install all current update
RUN yum -y upgrade

# Install common requirements
RUN yum -y install \
	git \
	wget \
	unzip

# Install Python 3.5 from PIUS repo
RUN yum -y install https://centos7.iuscommunity.org/ius-release.rpm \
 && yum -y install python35u-pip python35u-tools python35u-setuptools python35u-libs

# Install Python modules for validation
RUN pip3 install -U \ 
	pip \ 
	setuptools \
	pep8 \
	pep8-naming \
	flake8
