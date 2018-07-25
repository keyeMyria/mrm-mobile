#!/usr/bin/env bash

# Install JDK8 on circleCI so as to run flank on circleCI
installJava() {
  apt-get install -y python-software-properties debconf-utils
  echo "deb http://ppa.launchpad.net/webupd8team/java/ubuntu trusty main" >> /etc/apt/sources.list.d/webupd8team-java.list
  echo "deb-src http://ppa.launchpad.net/webupd8team/java/ubuntu trusty main" >> /etc/apt/sources.list.d/webupd8team-java.list
  apt-key adv --keyserver keyserver.ubuntu.com --recv-keys EEA14886
  apt-get update
  echo "oracle-java8-installer shared/accepted-oracle-license-v1-1 select true" | debconf-set-selections
  apt-get install -y oracle-java8-installer
  apt install oracle-java8-set-default
}

main() {
  installJava
}

main