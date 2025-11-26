
@set svn_work=export\client-json
@set gamecfg=gamecfg\src

echo f| xcopy %~dp0%svn_work% %~dp0%gamecfg% /S /E /F /Y

cd  %~dp0
call start /wait  %~dp0\package_json.exe 
echo f| xcopy %~dp0\gamecfg\src\gamecfg.bin %~dp0\gamecfg\client-json\gamecfg.bin /S /E /F /Y

del %~dp0\gamecfg\src\gamecfg.bin
pause