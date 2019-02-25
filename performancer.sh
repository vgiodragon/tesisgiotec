#! /bin/bash
tiempo=0.5 #segundos

mhora=$(date +%H:%M:%S.%N)
#echo "$mhora"


#echo "------------------------"
#ene=$(sudo perf stat -e power/energy-cores/,power/energy-ram/ -ag sleep $tiempo 2>&1)
#echo "$ene"
#ene=$(echo ${ene:25:200})
#e_core=$(echo "$ene" | cut -d "J" -f1 | cut -d ":" -f2 | sed 's/ //g' | sed 's/,/./g')
#e_ram=$(echo "$ene" | cut -d "J" -f2 | cut -d "/" -f3 | sed 's/ //g' | sed 's/,/./g')
#echo "core $e_core"
#echo "ram $e_ram"

mcpu=$(top -bn 2 -d 0.7 | grep '^%Cpu' | tail -n 1 | gawk '{print $2+$4+$6}')
mem_used=$(top -bn 2 -d 0.3 | grep '^KiB Mem' | tail -n 1 | gawk '{print $6}')
echo "$mhora,$mcpu,$mem_used" >> perfs/$1.csv

