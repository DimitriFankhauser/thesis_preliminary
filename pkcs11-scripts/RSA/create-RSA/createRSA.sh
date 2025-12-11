pkcs11-tool --module /usr/lib64/pkcs11/libsofthsm2.so \
  --login --token-label "MyFirstToken" \
  --keypairgen \
  --key-type RSA:2048 \
  --id 2222 \
  --label "MyFirstRSAKey" \
  --pin 123456789