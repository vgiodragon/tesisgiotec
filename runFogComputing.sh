#!/bin/bash
#echo "$mhora"
alarmas=1200

while [  $alarmas -lt 1400 ]; do
./paso1.sh > aux2.txt &
paso1=$!
echo "Loop $alarmas"
sleep 60

./runperformancer.sh fc4g$alarmas &
runperf=$!
echo "$runperf"
python3 suscribeandSaveCSV.py $alarmas &
suscriber=$!
echo "$suscriber"

python3 sendasFog.py $alarmas &
echo "$!"
sleep 700
kill $runperf
kill $paso1
kill $suscriber
let "alarmas += 200"
done
