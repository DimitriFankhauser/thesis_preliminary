pkcs11-tool --module /usr/lib64/pkcs11/libsofthsm2.so \
  --login --token-label "MyFirstToken" \
  --keygen \
  --key-type AES:32 \
  --id 3333 \
  --label "MyFirstAESKey" \
  --usage-sign --usage-decrypt --usage-wrap \
  --pin 123456789
