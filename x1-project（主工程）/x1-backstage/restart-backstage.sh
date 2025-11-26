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

echo "start $server_path"

nohup java -server -XX:+HeapDumpOnOutOfMemoryError -Xms512M -Xmx1024M -classpath $server_path/config/*:/game/x1/common-libs/*:$server_path/jar/*: com.xiugou.x1.backstage.X1BackStage > ./log_console.log &

