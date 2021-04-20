# Summary

This is an Android test app for [nitmproxy](https://github.com/chhsiao90/nitmproxy).

# Phones

For testing TLSv1.3 and ALPN a new Android version is needed, e.g. Android 11.
For testing TLSv1.2 without ALPN ANdroid 8 can be used.

# Setup

The MITM CA must be installed. Since Android 7 no application is trusting user defined CAs anymore by default. The Google Chrome browser does.

Install the certificate `app/src/main/assets/server.pem` as user certificate in Android. Security -> Encryption & credentials -> Install a certificate / Install from SD card 

# Build

The module is using nitmproy as submodule for convenience.

~~~shell
git pull recurse-submodules
~~~

Add your own remote to the submodule to be able to push and pull from your own repo:

~~~shell
cd externals/nitmproxy
git remote add mine <your mitmproxy repo>
~~~ 
