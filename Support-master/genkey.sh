#!/bin/bash
#Made by John Humphrys for ProductChain
#One parameter to generate required keys
echo Generating keys for $1
#Generate private key
openssl genrsa -out $1.private.pem 2048
#Generate DER format so Bluetooth beacon can read it
openssl pkcs8 -topk8 -inform PEM -outform DER -in $1.private.pem -out $1.private.der -nocrypt
#Generate standard public key
openssl rsa -in $1.private.pem -pubout -out $1.public.pem
#Generate DER format so Bluetooth beacon can read
openssl rsa -in $1.private.pem  -pubout -outform DER -out $1.public.der
echo Finished generating keys for $1
exit 0