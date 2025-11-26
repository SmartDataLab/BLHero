::基础模块目录
set gamingspace=D:\Git\gaming\
set gamingprospace=D:\Git\gaming-project-branch\

cd %gamingspace%gaming-agent
call mvn clean install

cd %gamingspace%gaming-tool
call mvn clean install

cd %gamingspace%gaming-db
call mvn clean install

cd %gamingspace%gaming-fakecmd
call mvn clean install

cd %gamingspace%gaming-design
call mvn clean install

cd %gamingspace%gaming-ruler
call mvn clean install

cd %gamingprospace%gaming-prefab
call mvn clean install

cd %gamingprospace%gaming-backstage
call mvn clean install

cd %~dp0
call mvn clean install

::call ./copydeploy.bat
@pause
::此bat文件需要GBK的编码格式