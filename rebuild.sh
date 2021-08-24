#!/bin/bash
echo 
echo Building...
echo 

cd Chiyogami/

./gradlew reobfJar

cd ../

cp -f Chiyogami/Paper-Server/build/libs/Paper-Server-1.17.1-R0.1-SNAPSHOT-mojang-mapped.jar Chiyogami-1.17.1.jar