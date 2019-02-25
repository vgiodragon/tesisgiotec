#!/bin/bash
echo "Ingresa # de alarmas:"
read varNtest
namefile="$varNtest"
python3 suscribeandSaveCSV.py $namefile &
suscriber=$!
sleep 3
echo "$suscriber"

./runperformancer.sh $namefile &
runperf=$!
echo "$runperf"

python3 testgiotec.py $namefile
sleep 45
kill $suscriber
kill $runperf
mhora=$(date +%H:%M:%S)
echo "fin! $mhora "

