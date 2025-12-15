CIPHERTEXT=${ curl -X POST \
  http://localhost:8080/crypto/encryptRSA \
  -H 'Content-Type: application/json' \
  -d '{"id": 1, "message": "This message will be encrypted through the backend"}'
  }

echo CIPHERTEXT
echo $CIPHERTEXT

CLEARTEXT=${ curl -X POST \
  http://localhost:8080/crypto/decryptRSA \
  -H 'Content-Type: application/json' \
  -d "{\"id\": 1, \"message\": \"$CIPHERTEXT\"}"
}

echo CLEARTEXT
echo $CLEARTEXT