diff --strip-trailing-cr -urN Original/Paper-Server/src Paper-Server/src > server.patch
diff --strip-trailing-cr -urN Original/Paper-API/src Paper-API/src > api.patch
diff --strip-trailing-cr -urN Original/Paper-MojangAPI/src Paper-MojangAPI/src > mojang.patch
mkdir -p patches
mv -f server.patch patches
mv -f api.patch patches
mv -f mojang.patch patches