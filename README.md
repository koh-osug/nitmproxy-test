# Summary

This is an Android test app for [nitmproxy](https://github.com/chhsiao90/nitmproxy).

# Setup

The MITM CA must be installed. Since Android 7 no application is trusting user defined CAs anymore by default. The Google Chrome browser does.

Install the certificate `app/src/main/res/raw/server.pem` as user certificate in Android. Security -> Encryption & credentials -> Install a certificate / Install from SD card 

# CA Certificate

The CA for web proxy unser `app/src/main/res/raw` was created with:

~~~shell script
openssl req -x509 -nodes -newkey rsa:2048 -keyout key.pem -out server.pem -days 7300 -subj '/CN=nitmproxy/OU=PKI/O=Netty in the Middle' -addext "keyUsage = digitalSignature, keyEncipherment, dataEncipherment, cRLSign, keyCertSign" -addext "extendedKeyUsage = serverAuth, clientAuth"
openssl rsa -in key.pem -out key.pem
~~~~

