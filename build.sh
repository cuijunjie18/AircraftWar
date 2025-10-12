#!/bin/bash

# 进入项目根目录（AircraftWar-base所在目录）
javac \
-encoding UTF-8 \
-d out/production/AircraftWar-base \
-cp "lib/commons-lang3-3.8.1.jar" \
src/edu/hitsz/application/*.java \
src/edu/hitsz/aircraft/*.java \
src/edu/hitsz/basic/*.java \
src/edu/hitsz/bullet/*.java \
src/edu/hitsz/prop/*.java \
src/edu/hitsz/factory/*.java \
src/edu/hitsz/strategy/*.java \
src/edu/hitsz/utils/*.java \
src/edu/hitsz/data/*.java \