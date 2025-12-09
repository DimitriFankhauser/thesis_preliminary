curl -X POST \
  http://localhost:8080/crypto/encryptRSA \
  -H 'Content-Type: application/json' \
  -d '{"id": 1, "message": "This message will be encrypted through the backend"}'
