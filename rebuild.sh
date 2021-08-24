#!/bin/bash
echo 
echo Building...
echo 

cd Chiyogami/

mvn clean install

cd ../

cp -f Chiyogami/Paper-Server/target/paper-1.16.5.jar Chiyogami-1.16.5.jar