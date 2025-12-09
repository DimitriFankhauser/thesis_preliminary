pkcs11-tool \
  --module /usr/lib64/pkcs11/libsofthsm2.so \
  --login \
  --pin 98765432 \
  --read-object \
  --type pubkey \
  --label MyFirstRSAKey \
  --output-file rsa_public.der;

openssl rsa -inform DER -in rsa_public.der -pubin -outform PEM -out rsa_public.pem