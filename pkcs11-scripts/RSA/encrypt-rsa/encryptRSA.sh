pkcs11-tool \
  --module /usr/lib64/pkcs11/libsofthsm2.so \
  --login \
  --encrypt \
  --id 2222 \
  --mechanism RSA-PKCS \
  --input-file ./plaintext.txt \
  --output-file decrypted.enc