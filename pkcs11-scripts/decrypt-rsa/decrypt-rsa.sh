pkcs11-tool \
  --module /usr/lib64/pkcs11/libsofthsm2.so \
  --login \
  --decrypt \
  --id 2222 \
  --mechanism RSA-PKCS \
  --input-file ./ciphertext.enc \
  --output-file decrypted.txt