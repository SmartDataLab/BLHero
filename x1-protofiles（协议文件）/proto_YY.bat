::此文件需要GBK编码
::协议脚本文件所在的项目路径
::protoc.exe文件所在的项目路径
set protoHome=%~dp0

set script=%protoHome%src

set javaoutput=D:\Git\fengwei-x1-server\x1-project\x1-protobuf

cd %protoHome%
::protoc工具是不会进行子目录递归的，需要逐个给出
::以protofiles目录为基础创建java文件，输出在本工程的目录下

::rd /s/q %project%\src\main\java\com\

protoc3.11.4.exe -I=%script% --java_out=%javaoutput%/src/main/java/ %script%/*.proto
::生成GRPC代码
::protoc3.11.4.exe -I=%script% --plugin=protoc-gen-grpc-java=%protoHome%protoc-gen-grpc-java-1.51.0-windows-x86_64.exe --grpc-java_out=%javaoutput%/src/main/java/ %script%/*.proto


::protoc3.11.4.exe --plugin=protoc-gen-go=%GOPATH%\bin\protoc-gen-go.exe -I=%script%/gosrc --go_out=. %script%/gosrc/*.proto

::protoc3.11.4.exe -I=%script% --csharp_out=%csoutput% %script%/*.proto

echo. & pause

::grpc相关下载包
::https://repo1.maven.org/maven2/io/grpc/protoc-gen-grpc-java/