#!/bin/bash
echo 
echo Hello World!
echo 
echo This is the script for the initial build of Chiyoami.
echo 
echo If you want to modify the code and build it after running this script, please run "rebuild.sh".
echo 
echo Build task will start in 10 seconds.
echo 
sleep 10s

echo 
echo Build paper.
echo 
git clone -b ver/1.16.5 https://github.com/Be4rJP/Tuinity.git
./Tuinity/tuinity patch
./Tuinity/tuinity build
mv -f Tuinity/ Original/

echo 
echo Copying...
echo 
mkdir -p Chiyogami
cp -rf Original/* Chiyogami/

echo 
echo Applying patch...
echo 
cp -f patches/server.patch Chiyogami/Tuinity-Server/
cp -f patches/server.pom.xml.patch Chiyogami/Tuinity-Server/
cp -f patches/api.patch Chiyogami/Tuinity-API/
cp -f patches/api.pom.xml.patch Chiyogami/Tuinity-API/

cd Chiyogami/

patch -Np1 < Tuinity-Server/server.patch
patch -Np1 < Tuinity-Server/server.pom.xml.patch
patch -Np1 < Tuinity-API/api.patch
patch -Np1 < Tuinity-API/api.pom.xml.patch

cd ../

echo 
echo Starting build...
echo 
./rebuild.sh