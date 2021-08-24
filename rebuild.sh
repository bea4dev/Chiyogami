#!/bin/bash
echo 
echo Building...
echo 

cd Chiyogami/

mvn clean install

cd ../

cp -f Chiyogami/Paper-Server/target/paper-1.15.2.jar Chiyogami-1.15.2.jar