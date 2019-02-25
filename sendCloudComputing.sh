#!/bin/bash
#echo "$mhora"
alarmas=1200
while [  $alarmas -lt 1400 ]; do
sleep 35
python3 suscribeandSaveCSV.py $alarmas &
suscriber=$!
echo "$suscriber"
./runperformancer.sh Cc4g$alarmas &
runperf=$!
echo "$runperf"

sleep 10
python3 sendtoCloud.py $alarmas &
publisher=$!
echo "$publisher"
sleep 700
mhora=$(date +%H:%M:%S)
echo "fin loop! $alarmas $mhora "
kill $runperf
kill $suscriber
kill $publisher
let "alarmas += 200"
done

