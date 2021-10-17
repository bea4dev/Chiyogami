diff --strip-trailing-cr -urN Original/Tuinity-Server/src Chiyogami/Chiyogami-Server/src > server.patch
diff --strip-trailing-cr -urN Original/Tuinity-Server/pom.xml Chiyogami/Chiyogami-Server/pom.xml > server.pom.xml.patch
diff --strip-trailing-cr -urN Original/Tuinity-API/src Chiyogami/Chiyogami-API/src > api.patch
diff --strip-trailing-cr -urN Original/Tuinity-API/pom.xml Chiyogami/Chiyogami-API/pom.xml > api.pom.xml.patch
diff --strip-trailing-cr -urN Original/pom.xml Chiyogami/pom.xml > pom.xml.patch
mkdir -p patches
mv -f server.patch patches
mv -f server.pom.xml.patch patches
mv -f api.patch patches
mv -f api.pom.xml.patch patches
mv -f pom.xml.patch patches