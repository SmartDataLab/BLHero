#!/bin/bash

server_path=$(cd "$(dirname "$0")"/; pwd)

echo -e "\nstop $server_path "

for (( i=1; i<=100; i++))
do
        process=`ps -ef |grep -v 'grep' | grep "java" | grep $server_path/ | awk '{print $2}'`
        if [[ $process -gt 0 ]];then
            kill -15 $process
            echo "process lenght:${#process[*]} kill -15 $process "
        else 
            echo "not java process"
            break
        fi
        sleep 2
done

echo "stop $server_path end"