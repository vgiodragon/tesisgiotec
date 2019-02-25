#!/bin/bash
./removelog.sh
./compile.sh
./stop-start-flink.sh

echo "Ejecutando..."
sleep 11
mhora=$(date +%H:%M:%S)
echo "Inicio $mhora "
./run.sh
