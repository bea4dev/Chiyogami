#!/bin/bash
echo 
echo Building...
echo 

cd Chiyogami/

mvn clean install

cd ../

cp -f Chiyogami/Chiyogami-Server/target/tuinity-1.15.2.jar Chiyogami-1.15.2.jar