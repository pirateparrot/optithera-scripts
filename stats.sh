#! /bin/bash

if [ $# -ne 2 ]
  then
    echo "First argument must be the target directory"
    echo "Second argument must be the pattern of the files to select, e.g. '*'"
    exit -1
fi

wc $1/$2 | sed '1d;$d' | awk '{print $3}' | sort -n | awk '
  BEGIN {
    c = 0;
    sum = 0;
  }
  $1 ~ /^[0-9]*(\.[0-9]*)?$/ {
    a[c++] = $1;
    sum += $1;
  }
  END {
    ave = sum / c;
    if( (c % 2) == 1 ) {
      median = a[ int(c/2) ];
    } else {
      median = ( a[c/2] + a[c/2-1] ) / 2;
    }
    OFS="\t";
    print "sum, count, average, median, min, max"
    print sum, c, ave, median, a[0], a[c-1];
  }
'
