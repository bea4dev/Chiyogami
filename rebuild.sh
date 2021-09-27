#!/bin/bash
echo 
echo Building...
echo 

cd Chiyogami/

./tuinity build

cd ../

cp -f Chiyogami/Tuinity-Server/target/tuinity-1.15.2.jar Chiyogami-1.15.2.jar