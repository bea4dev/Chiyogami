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
git clone https://github.com/Be4rJP/Paper.git
cd Paper/
./gradlew applyPatches
./gradlew reobfJar
cd ../
mv -f Paper/ Original/

echo 
echo Copying...
echo 
cp -rf Original/ Chiyogami/

echo 
echo Applying patch...
echo 
cp -f patches/api.patch Chiyogami/Paper-API/
cp -f patches/mojang.patch Chiyogami/Paper-MojangAPI/
cp -f patches/server.patch Chiyogami/Paper-Server/

cd Chiyogami/

patch -Np1 < Paper-API/api.patch
patch -Np1 < Paper-MojangAPI/mojang.patch
patch -Np1 < Paper-Server/server.patch

cd ../

echo 
echo Starting build...
echo 
./rebuild.sh