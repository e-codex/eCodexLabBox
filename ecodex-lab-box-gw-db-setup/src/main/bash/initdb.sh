#!/bin/sh

java -Dloader.path=./lib -Dloader.main=eu.ecodex.labbox.gw.setup.SetupGwDatabase -cp ./bin/* org.springframework.boot.loader.PropertiesLauncher

