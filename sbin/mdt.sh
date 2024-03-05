#!	/bin/bash

java -Dlog4j.configurationFile=%MDT_HOME%/log4j2.xml \
-cp $MDT_HOME/sbin/mdt.core.jar \
mdt.tool.MDTCommandsMain "$@"
