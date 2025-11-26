
@echo off  
  
set "config=.\config.ini"  
  
for /f "tokens=1,2 delims==" %%a in (%config%) do (  
    set "%%a=%%b"  
)

if not %SYNC_TO_WORKBENCH%==true exit 1
  
cd  %~dp0
call start /wait  %~dp0\package_json.py

echo f| xcopy %~dp0\map.bin %~dp0%GAME_CFG%map.bin /S /E /F /Y

del %~dp0\map.bin
