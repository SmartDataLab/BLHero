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

nohup java -server -javaagent:$server_path/jar/gaming-agent-0.0.1.jar -XX:+HeapDumpOnOutOfMemoryError -Xms1024M -Xmx2048M -classpath $server_path/config/*:/game/x1/common-libs/*:$server_path/jar/*: com.xiugou.x1.game.server.X1GameServer > ./log_console.log &

