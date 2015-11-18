#!/bin/bash
echo $1 $2 $3
workdir=$1
infile=$2
outfile=$3
cd $workdir
#tesseract -l chi $infile $outfile
/home/zhangshuang/ocr/tesseract-master/tesscv  -l chi $infile $outfile

