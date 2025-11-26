@echo off  
  
set "config=.\config.ini"  
  
for /f "tokens=1,2 delims==" %%a in (%config%) do (  
    set "%%a=%%b"  
)

call python export_client.py
call python export_server.py
call pacakge_json.bat

pause
