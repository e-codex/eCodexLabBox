This folder contains a maven project which downloads the labbox. Just type

mvn -De-codex.repo.username=<username> -De-codex.repo.password=<password>

install to download all the e-codex components.

If you do not have maven installed you can run

mvnw.bat -De-codex.repo.username=<username> -De-codex.repo.password=<password>

install which will download maven into this folder and use this installation to download the e-codex components.

Anyway you have to setup the passwords for the e-codex repository. Username and password are available via the e-codex project: https://www.e-codex.eu

NOTE: if you are behind a proxy you should configure maven according to this guide:
https://maven.apache.org/guides/mini/guide-proxies.html




