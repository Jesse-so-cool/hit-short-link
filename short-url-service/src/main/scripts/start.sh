#!/bin/sh
export JAVA_HOME=$JAVA_HOME
export PATH=$JAVA_HOME/bin:$PATH
export CLASSPATH=.:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar
export SERVICE_NAME=${PWD##*/}
PIDFILE=service.pid
ROOT_DIR="$(cd $(dirname $0) && pwd)"
# 获取当前服务名 skywalking
AGENTDIR="/data/agent/skywalking-agent.jar"
AGENTOPT=" -javaagent:$AGENTDIR -Dskywalking.agent.service_name=$SERVICE_NAME"

CLASSPATH=$ROOT_DIR/conf/:./*:$ROOT_DIR/lib/*
JAVA_OPTS="-Xms256m -Xmx512m -XX:+UseParallelGC"
MAIN_CLASS=cn.com.bluemoon.shorturl.ShortUrlServiceApplication
# 判断是否有APM监控路径报信息
if [ -f "$AGENTDIR" ]; then
   JAVA_OPTS="$JAVA_OPTS $AGENTOPT"
fi

if [ ! -d "logs" ]; then
   mkdir logs
fi
if [ -f "$PIDFILE" ]; then
    echo "Service is already start ..."
else
    echo "Service  start ..."
    nohup java $JAVA_OPTS -cp $CLASSPATH $MAIN_CLASS 1> logs/log.out 2>&1  &
    printf '%d' $! > $PIDFILE
    echo "Service  start SUCCESS "
fi