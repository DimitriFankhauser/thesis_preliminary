# Genesis Script
This is my large setup script for RSA. 
This script will do the following steps: 
1. create a Token-Slot
2. create a self-signed certificate
3. create an RSA Keypair in Softhsm
4. Create a CSR (Certificate Signing Request) for your Keypair
5. Create a new, SIGNED Certificate based on CSR and load it into the HSM with correct Key_ID and Label