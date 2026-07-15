#!/bin/bash
echo ""
echo " ================================================"
echo "   HOTEL RESERVATION SYSTEM - Starting..."
echo " ================================================"
echo ""

if ! command -v java &> /dev/null; then
    echo " ERROR: Java not found! Install JDK 11+"
    exit 1
fi

mkdir -p out

echo " Compiling..."
find src -name "*.java" > sources.txt
javac -encoding UTF-8 -d out @sources.txt
rm sources.txt

if [ $? -ne 0 ]; then
    echo " Compile failed!"
    exit 1
fi

echo " Launching..."
java -Dfile.encoding=UTF-8 -Dawt.useSystemAAFontSettings=on -Dswing.aatext=true -cp out hotel.ui.Main
