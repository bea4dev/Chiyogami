diff --strip-trailing-cr -urN Original/Paper-Server/src Chiyogami/Paper-Server/src > server.patch
diff --strip-trailing-cr -urN Original/Paper-API/src Chiyogami/Paper-API/src > api.patch
diff --strip-trailing-cr -urN Original/Paper-MojangAPI/src Chiyogami/Paper-MojangAPI/src > mojang.patch
mkdir -p patches
mv -f server.patch patches
mv -f api.patch patches
mv -f mojang.patch patches