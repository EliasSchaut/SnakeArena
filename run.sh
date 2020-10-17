#!/usr/bin/env bash
top=$(git rev-parse --show-toplevel 2>/dev/null)
if [ $? -ne 0 ]; then
  echo "Not in a git repo!"
  exit 1
fi

cd $top/src
javac $(find . -name \*.java) && jar -cvfm SnakeArena.jar mainfest.txt $(find . -name \*.class -o -name \*.properties) && java -jar SnakeArena.jar
