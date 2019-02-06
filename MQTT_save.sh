#!/bin/bash

python3 suscribeAndSave.py &
sleep 2
suscriber=$!

nalarms=$1
ntest=$2
echo "PiD suscriber $suscriber"
sleep 705
mongoexport --db tesisgiotec --collection alarmaRaspberry --type=csv --out Rasp/${nalarms}_${ntest}.csv --fields timestamp1,Hora,timestamp2,Hpaso21,Hpaso22
mongoexport --db tesisgiotec --collection alarmaOdroid --type=csv --out Odroid/${nalarms}_${ntest}.csv --fields timestamp1,Hora,timestamp2,Hpaso21,Hpaso22

mhora=$(date +%H:%M:%S)
echo "Fin $mhora "
python3 dropDatabase.py

kill $suscriber

