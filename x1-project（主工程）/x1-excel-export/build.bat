::基础模块目录
set gamingspace=E:\git\gaming\

cd %gamingspace%gaming-tool
call mvn clean install

cd %gamingspace%gaming-design
call mvn clean install

cd %~dp0
call mvn clean install

@pause