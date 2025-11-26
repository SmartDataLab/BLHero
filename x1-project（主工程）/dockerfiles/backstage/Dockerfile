#构建镜像初始的依赖镜像（镜像名:版本号，缺省版本号时为latest）
FROM yy-jdk8

#指定镜像创建者信息
Maintainer YY

#设置语言
ENV LANG en_US.UTF-8
ENV LC_ALL en_US.UTF-8

#设置时区
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

#在容器中创建目录
RUN mkdir /game
RUN mkdir /game/x1
RUN mkdir /game/x1/common-libs
RUN mkdir /game/x1/jar
RUN mkdir /game/x1/config
RUN mkdir /game/x1/logs

#COPY 当前目录下的文件 目标文件夹
COPY ./common-libs/* /game/x1/common-libs
RUN rm -rf /game/x1/common-libs/jdk.tools-1.8.jar
COPY ./jar/* /game/x1/jar
RUN cp /usr/local/java/jdk1.8.0_231/lib/tools.jar /game/x1/common-libs

#设置进行容器时的工作目录，也是程序启动的目录，如果不进行此设置，会影响到springboot无法正常加载config配置
WORKDIR /game/x1

ENTRYPOINT ["java", "-server", "-XX:+HeapDumpOnOutOfMemoryError", "-Xms1024M", "-Xmx2048M", "-classpath", "./config/*:./common-libs/*:./jar/*:", "com.xiugou.x1.backstage.X1BackStage", ">", "./log_console.log", "&"]

#使用如下命令进行构建
#docker build -t x1-backstage:202408011000 .
