#!/bin/bash
echo "Ingresa # de alarmas:"
read varNtest
namefile="$varNtest"
python3 suscribeandSaveCSV.py $namefile &
suscriber=$!
sleep 3
echo "$suscriber"

python3 testgiotec.py $namefile &
publisher=$!
echo "$publisher"

mhora=$(date +%H:%M:%S)
echo "inicio perf $mhora en file perfs/$namefile.csv"
contador=0
while [  $contador -lt 700 ]; do
./performancer.sh $namefile;
#sleep 0.25;
let "contador += 1"
done
mhora=$(date +%H:%M:%S)
echo "fin perf $mhora en file perfs/$namefile.csv"

kill $suscriber
kill $publisher
