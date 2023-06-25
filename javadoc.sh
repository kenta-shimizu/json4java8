#!/bin/sh

package="com.shimizukenta.jsonhub"
path_src="./src/main/java/"
path_docs="./docs"

# remove files and mkdir
rm -Rf ${path_docs}
mkdir ${path_docs}

javadoc \
-locale en_US \
-d ${path_docs} \
--show-members public \
--source-path ${path_src} \
${package}
