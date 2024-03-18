@echo off

java -Dlog4j.configurationFile=%MDT_HOME%/log4j2.xml ^
-cp %MDT_HOME%/build/libs/mdt.core-24.03.05-all.jar ^
mdt.tool.MDTCommandsMain %*
