#!/usr/bin/env bash

function write_csv() {
  FILE_NAME="input1_big.csv"
  echo "id,name,is mobile,score" > $FILE_NAME
  for ((i=1; i<=$ROWS_NUM; i++));
  do
     echo "$i,example.com/csv$i,true,454.23" >> $FILE_NAME
  done
}

function write_json() {
  FILE_NAME="input1_big.json"
  echo "[" > $FILE_NAME
  for ((i=1; i<=($ROWS_NUM - 1); i++));
  do
     echo "{\"site_id\": \"$i\", \"name\": \"example.com/json$i\", \"mobile\": 1, \"score\": 21.003 }," >> $FILE_NAME
  done
  echo "{\"site_id\": \"$ROWS_NUM\", \"name\": \"example.com/json$ROWS_NUM\", \"mobile\": 1, \"score\": 21.003 }" >> $FILE_NAME
  echo "]" >> $FILE_NAME
}

function write_files() {
  echo "id,name,is mobile,score" > $FILE_CSV
  echo "[" > $FILE_JSON
  for ((i=1; i<=($ROWS_NUM - 1); i++));
  do
    echo "$i,example.com/csv$i,true,454.23" >> $FILE_CSV
    echo "{\"site_id\": \"$i\", \"name\": \"example.com/json$i\", \"mobile\": 1, \"score\": 21.003 }," >> $FILE_JSON
  done
  echo "$i,example.com/csv$i,true,454.23" >> $FILE_CSV
  echo "{\"site_id\": \"$ROWS_NUM\", \"name\": \"example.com/json$ROWS_NUM\", \"mobile\": 1, \"score\": 21.003 }" >> $FILE_JSON
  echo "]" >> $FILE_JSON
}

function run_app() {
  if [ ! -f build/libs/geomotive-test-task-0.1.0.jar ]; then
    echo "geomotive-test-task-0.1.0.jar not found!"
    gradle build -x test
  fi
  java -Dinput.files="$FILE_CSV,$FILE_JSON" -Dproducer.consumer.unit.size=1000 -jar build/libs/geomotive-test-task-0.1.0.jar $TDIR $TDIR/$FILE_OUT
}

FILE_CSV="input1_big.csv"
FILE_JSON="input2_big.json"
FILE_OUT="output_big.json"
ROWS_NUM=10000000
_PWD=`pwd`
RAND=$RANDOM
TDIR="t_input$RAND"
#TDIR="t_input13002"
mkdir $TDIR
if [ "$1" != "s" ]
then
  trap "{ cd $_PWD; rm -rf $TDIR; exit 255; }" SIGINT SIGKILL
fi

cd $TDIR

echo "write input files"
SECONDS=0
#write_csv
#write_json
write_files
echo "Writing has finished in $SECONDS s."

echo "start the application"
cd $_PWD
SECONDS=0
run_app
echo "The application has finished in $SECONDS s."

if [ "$1" != "s" ]
then
  rm -r "$TDIR"
fi
