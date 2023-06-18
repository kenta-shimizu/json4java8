#!/bin/sh

package="com.shimizukenta.jsonhub"
path_src="./src/main/java/"
path_docs="./docs"

# remove files and mkdir
rm -Rf ${path_docs}
mkdir ${path_docs}

javadoc -locale en_US \
--show-members public \
-d ${path_docs} \
--source-path ${path_src} \
${package}
