#!/bin/bash

echo "horaEjecucion,cpu,mem_used" > perfs/$1.csv
mhora=$(date +%H:%M:%S)
echo "inicio perf $mhora en file perfs/$1.csv"
contador=0
while [  $contador -lt 700 ]; do
./performancer.sh $1;
#sleep 0.25;
let "contador += 1"
done
mhora=$(date +%H:%M:%S)
echo "fin perf $mhora en file perfs/$1.csv"

