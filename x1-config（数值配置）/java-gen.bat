::配置文件的目录
set configHome=%~dp0

::配置客户端文件导出路径
set genOutputPath=D:\Git\fengwei-x1-server\x1-project\x1-design\src\main\java\com\xiugou\x1\design\module
set configInputPath=%configHome%export\server-csv
set genFilePath=%configHome%java-gen.txt

java -jar x1-excel-export-0.0.1-jar-with-dependencies.jar 5 %genOutputPath% %configInputPath% %genFilePath%

echo. & pause