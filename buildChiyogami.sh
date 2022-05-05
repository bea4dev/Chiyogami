#!/bin/bash
echo 
echo Hello World!
echo 
echo This is the script for the initial build of Chiyogami.
echo 
echo If you want to modify the code and build it after running this script, please run "rebuild.sh".
echo 
echo Build task will start in 10 seconds.
echo 
sleep 10s

echo 
echo Build paper.
echo 
git clone -b ver/1.15.2 https://github.com/Be4rJP/Tuinity.git
./Tuinity/tuinity patch
./Tuinity/tuinity build
mv -f Tuinity/ Original/

echo 
echo Copying...
echo 
mkdir -p Chiyogami
cp -rf Original/* Chiyogami/

mv -f Chiyogami/Tuinity-Server/ Chiyogami/Chiyogami-Server/
mv -f Chiyogami/Tuinity-API/ Chiyogami/Chiyogami-API/

echo 
echo Applying patch...
echo 
cp -f patches/server.patch Chiyogami/Chiyogami-Server/
cp -f patches/server.pom.xml.patch Chiyogami/Chiyogami-Server/
cp -f patches/api.patch Chiyogami/Chiyogami-API/
cp -f patches/api.pom.xml.patch Chiyogami/Chiyogami-API/
cp -f patches/pom.xml.patch Chiyogami/

cd Chiyogami/

patch -Np1 < Chiyogami-Server/server.patch
patch -Np1 < Chiyogami-Server/server.pom.xml.patch
patch -Np1 < Chiyogami-API/api.patch
patch -Np1 < Chiyogami-API/api.pom.xml.patch
patch -Np1 < pom.xml.patch

cd ../

echo 
echo Starting build...
echo 
./rebuild.sh