#!/bin/bash
echo 
echo Building...
echo 

cd Chiyogami/

./tuinity build

cd ../

cp -f Chiyogami/Tuinity-Server/target/tuinity-1.16.5.jar Chiyogami-1.16.5.jar