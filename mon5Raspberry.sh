#! /bin/bash
tiempo=0.25 #segundos

mhora=$(date +%H:%M:%S.%N)
#echo "$mhora"

#Cambiar aqui el comando a medir ej: sleep

L1_1=$(sudo perf_4.9 stat -e L1-dcache-load-misses,L1-dcache-loads -ag sleep $tiempo 2>&1)
#echo "$L1_1"
L1_1=$(echo ${L1_1:25:200})
dcache_load_misses=$(echo "$L1_1" | cut -d "L" -f1 | cut -d ":" -f2 | sed 's/ //g' | sed 's/,/./g'| sed 's/,/./g')
dcache_loads=$(echo "$L1_1" | cut -d "L" -f3 | cut -d "s" -f2 | sed 's/ //g' | sed 's/,/./g'| sed 's/,/./g')

#echo "L1_dcache_load_misses $dcache_load_misses"
#echo "dcache_loads $dcache_loads"

#echo "------------------------"
L1_2=$(sudo perf_4.9 stat -e L1-dcache-stores,LLC-loads -ag sleep $tiempo 2>&1)
#echo "resultado $L1_2"

L1_2=$(echo ${L1_2:25:200})
L1_dcache_stores=$(echo "$L1_2" | cut -d "L" -f1 | cut -d ":" -f2 | sed 's/ //g' | sed 's/,/./g'| sed 's/,/./g')
LLC_loads=$(echo "$L1_2" | cut -d "L" -f2 | cut -d "s" -f3 | sed 's/ //g' | sed 's/,/./g'| sed 's/,/./g')
#echo "L1_dcache_stores $L1_dcache_stores"
#echo "LLC_loads $LLC_loads"


#echo "------------------------"
stat=$(sudo perf_4.9 stat -e cycles,instructions,cache-misses,cache-references,branches -ag sleep $tiempo 2>&1)
#echo "$stat"
stat=$(echo ${stat:40:600})
cycles=$(echo "$stat" | cut -d "c" -f1 | cut -d ":" -f2 | sed 's/ //g' | sed 's/,/./g'| sed 's/,/./g')
instructions=$(echo "$stat" | cut -d "i" -f2 | cut -d "s" -f2 | sed 's/ //g' | sed 's/,/./g'| sed 's/,/./g')
cache_misses=$(echo "$stat" | cut -d "c" -f6 | cut -d "e" -f2 | sed 's/ //g' | sed 's/,/./g'| sed 's/,/./g')
all_per_cache=$(echo "$stat" | cut -d "%" -f1 | cut -d "#" -f3 | sed 's/ //g' | sed 's/,/./g')
cache_references=$(echo "$stat" | cut -d "c" -f10 | cut -d "s" -f2 | sed 's/ //g' | sed 's/,/./g'| sed 's/,/./g')
branches=$(echo "$stat" | cut -d "b" -f1 | cut -d "s" -f10 | sed 's/ //g' | sed 's/,/./g'| sed 's/,/./g')
#echo "$stat"
#echo "cycles $cycles"
#echo "instructions $instructions"
#echo "cache_misses $cache_misses"
#echo "all_per_cache $all_per_cache"
#echo "cache_references $cache_references"
#echo "branches $branches"

sudo perf_4.9 record -a sleep $tiempo > aux.txt 2>&1
recorded=$(sudo perf_4.9 report --sort comm --stdio 2>&1)

mjava=$(echo "$recorded" | awk '{print $0} ' | grep java | cut -d '%' -f1 | sed 's/ //g')
msleep=$(echo "$recorded" | awk '{print $0} ' | grep sleep | cut -d '%' -f1 | sed 's/ //g')
mperf=$(echo "$recorded" | awk '{print $0} ' | grep "%  perf" | cut -d '%' -f1 | sed 's/ //g')
mmosquitto=$(echo "$recorded" | awk '{print $0} ' | grep mosquitto | cut -d '%' -f1 | sed 's/ //g')
mswapper=$(echo "$recorded" | awk '{print $0} ' | grep swapper | cut -d '%' -f1 | sed 's/ //g')

echo "$mhora,$dcache_load_misses,$dcache_loads,$L1_dcache_stores,$LLC_loads,$cycles,$instructions,$cache_misses,$all_per_cache,$cache_references,$branches,$mjava,$msleep,$mmosquitto,$mperf,$mswapper" >> perfs/$1.csv
