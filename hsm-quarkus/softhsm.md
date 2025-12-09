## relevante Links
- https://docs.oneidentity.com/bundle/safeguard-for-privileged-sessions_administration-guide_7.5/page/guides/shared/procedure-configuring-softhsm.htm
- Beispiel in Java: https://github.com/miladhub/pkcs11-example
- https://dearzubi.medium.com/integrating-softhsm-with-openssl-using-opensc-pkcs11-184c3f92e397


relevant und genaue Verwendung noch unklar:
- pkcs11-tool (Command)

## Verzeichnisse
**Tokens**: `/var/lib/softhsm`

**Config**: `/etc/softhsm2.conf`

**Library-Files**
`/usr/lib64/libsofthsm2.so`
`/usr/lib64/pkcs11/libsofthsm2.so`



## Installation Softhsm2
`sudo yum install softhsm`

Setup, das nötig war, damit PKCS11-Tool auf Tokens zugreifen kann:
    
    USER_NAME=$(whoami)
    USER_GROUP=$(id -gn $USER_NAME);
    sudo chown -R $USER_NAME:$USER_GROUP /var/lib/softhsm/;
    sudo chmod -R 700 /var/lib/softhsm/;




`

**Token initialisieren**

    softhsm2-util --init-token --slot 0 --label "MyFirstToken" --so-pin 1234 --pin 98765432


=> so-pin (Security Officer Pin)
=> pin (Applikationsseitiger Pin)


**Key in Softhsm speichern**

pkcs11-tool --module /usr/lib64/pkcs11/libsofthsm2.so --login --token-label "MyFirstToken" --keypairgen --key-type RSA:2048 --id 2222 --label "MyFirstRSAKey"





**Versuch 7.12.25**

- RSA Key mit Label "MyFirstRSAKey" im Token "MyFirstToken" generiert [script](/src/main/pkcs11-scripts/create-RSA)
- public Key extrahiert [script](src/main/pkcs11-scripts/extract-public-key)
- Message verschlüsselt [script](src/main/pkcs11-scripts/encrypt-rsa)
- Message entschlüsselt [script](src/main/pkcs11-scripts/decrypt-rsa)