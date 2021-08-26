#!/bin/bash
echo 
echo Building...
echo 

cd Chiyogami/

./gradlew reobfJar

cd ../

cp -f Chiyogami/Paper-Server/build/libs/Paper-Server-reobf.jar Chiyogami-1.17.1.jar