@echo off
title Hotel Reservation System
color 0A
echo.
echo  ================================================
echo    HOTEL RESERVATION SYSTEM - Starting...
echo  ================================================
echo.

REM Check if java exists
java -version >nul 2>&1
if errorlevel 1 (
    echo  ERROR: Java not found!
    echo  Please install Java JDK 11 or higher from:
    echo  https://adoptium.net/
    echo.
    pause
    exit /b 1
)

REM Create output folder
if not exist "out" mkdir out

echo  Compiling... Please wait...
echo.

REM Compile all java files
javac -encoding UTF-8 -d out -sourcepath src src\hotel\ui\Main.java 2>compile_errors.txt

if errorlevel 1 (
    echo  COMPILE ERROR - Check compile_errors.txt
    type compile_errors.txt
    pause
    exit /b 1
)

del compile_errors.txt 2>nul
echo  Compiled successfully!
echo  Launching GUI...
echo.

REM Run the application
java -Dfile.encoding=UTF-8 -Dawt.useSystemAAFontSettings=on -Dswing.aatext=true -cp out hotel.ui.Main

pause
