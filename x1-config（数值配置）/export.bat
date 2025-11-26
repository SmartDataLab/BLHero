::配置文件的目录
set configHome=%~dp0

::Excel配置文件所在的路径
set excelHome=%configHome%excel
set exportHome=%configHome%export

::配置完整导出路径，策划对比用
set docOutput=doc-csv
set docOutputType=csv

::配置服务器文件导出路径
set serverOutput=server-csv
set serverOutputType=csv

::配置客户端文件导出路径
set clientOutput=client-json
set clientOutputType=json

::语言表相关
set langFileExcelName=YLanguageAuto.xlsx
set langFileClassName=LanguageAuto

java -jar x1-excel-export-0.0.1-jar-with-dependencies.jar 1 %excelHome% %langFileExcelName% %langFileClassName%
::java -jar x1-excel-export-0.0.1-jar-with-dependencies.jar 2 %excelHome% %exportHome% %docOutput% %docOutputType% "*" "false"
java -jar x1-excel-export-0.0.1-jar-with-dependencies.jar 2 %excelHome% %exportHome% %serverOutput% %serverOutputType% "S" "false"
java -jar x1-excel-export-0.0.1-jar-with-dependencies.jar 2 %excelHome% %exportHome% %clientOutput% %clientOutputType% "C" "false"

::复制打包文件到前端工程

::set clientGamecfgPath=E:\Workspace\Svn\x1\client\x1\assets\bundles\gamecfg\gamecfg.bin
::xcopy exportHome\%clientOutput%\%clientOutput%.zip %clientGamecfgPath% /e/i/f/y

echo. & pause