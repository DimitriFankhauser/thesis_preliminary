#TODO: make this follow user input
SOFTHSM_LABEL="Genesis"
SOFTHSM_ID=7777
SO_PIN=2222
PIN=123456789
MODULE_PATH=/usr/lib64/pkcs11/libsofthsm2.so
KEY_LABEL="rsaGenesis"


# INITIALIZE TOKEN
softhsm2-util --init-token --free --label "$SOFTHSM_LABEL" --so-pin "$SO_PIN" --pin "$PIN"

# CREATE INITIAL CERTIFICATE
openssl req -x509 -out localhost.crt -keyout localhost.der \
  -newkey rsa:2048 -nodes -sha256 \
  -subj '/CN=localhost' -extensions EXT -config <( \
   printf "[dn]\nCN=localhost\n[req]\ndistinguished_name = dn\n[EXT]\nsubjectAltName=DNS:localhost\nkeyUsage=digitalSignature\nextendedKeyUsage=serverAuth");

# CREATE RSA KEYPAIR IN YOUR SOFTHSM TOKEN
pkcs11-tool --module $MODULE_PATH \
  --token-label $SOFTHSM_LABEL \
  --pin $PIN \
  --keypairgen \
  --key-type RSA:2048 \
  --id $SOFTHSM_ID \
  --label $KEY_LABEL;

# CREATE A CSR (CERTIFICATE SIGNING REQUEST)
openssl req -provider pkcs11 -new \
  -key "pkcs11:token=${SOFTHSM_LABEL};object=${KEY_LABEL};type=private;pin-value=123456789;module-path=${MODULE_PATH}" \
  -subj "/CN=MyNewService" \
  -out request.csr;

# SIGN CERTIFICATE
openssl x509 -provider pkcs11 -req \
  -in request.csr \
  -CAkey ./localhost.der \
  -CA ./localhost.crt \
  -CAcreateserial \
  -out newly_signed_cert.crt \
  -days 365;

# IMPORT SIGNED CERTIFICATE INTO SOFTHSM
pkcs11-tool --module $MODULE_PATH \
  --token $SOFTHSM_LABEL \
  --pin $PIN \
  --write-object newly_signed_cert.crt --type cert \
  --label $KEY_LABEL\
  --id $SOFTHSM_ID;





