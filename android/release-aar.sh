#!/bin/sh
bash gradlew :sdk:build # build new aar
cp sdk/build/outputs/aar/sdk-* app/libs/ # copy all flavors to app/libs/
