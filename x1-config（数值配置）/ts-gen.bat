::配置文件的目录
set configHome=%~dp0

::Excel配置文件所在的路径
set excelHome=%configHome%excel
set exportHome=%configHome%export

::配置客户端文件导出路径，目前需要导出成csv才能生成对应的ts类
set clientOutput=client-csv
set clientOutputType=csv

::配置客户端文件导出路径
set genOutputPath=%configHome%export\ts-gen
set configInputPath=%configHome%export\client-csv
set tsGenFilePath=%configHome%ts-gen.txt

java -jar x1-excel-export-0.0.1-jar-with-dependencies.jar 2 %excelHome% %exportHome% %clientOutput% %clientOutputType% "C" false
java -jar x1-excel-export-0.0.1-jar-with-dependencies.jar 3 %genOutputPath% %configInputPath% %tsGenFilePath%

echo. & pause