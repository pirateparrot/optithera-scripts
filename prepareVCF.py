import sys
from time import sleep

if len(sys.argv) != 2:
  print "Passed arguments were: " + str(sys.argv)
  print "Arguments must be: prepareVCF.py vcfFile"
  exit(0)

VCF_INPUT = sys.argv[1]
CSV_OUTPUT = VCF_INPUT + ".csv"
BATCH_SIZE = 1000

print "Reading data from {0} and writing to {1}".format(VCF_INPUT, CSV_OUTPUT)

sleep(3)

############################################################################

def nullify(row):
  return "" if row == "." else row

batch = []
i = 0
totalSize = 0
counter = 0

def flush_batch(batch, handle):
  if len(batch) == 0:
    return
  if counter % 100000 == 0:
    print "{0} rows written, {1} Mb".format(counter, (totalSize / 1048576))
  handle.write("".join(batch))

with open("./{0}".format(CSV_OUTPUT), 'w') as output:
  with open("./{0}".format(VCF_INPUT,'r')) as file:
    for line in file:
      if line[0] != "#": # Skip comments and headers
        row = line.split(chr(9)) # Split on tabs
        row[5] = nullify(row[5]) # quality
        row[6] = nullify(row[6]) # filter
        # row[7] = nullify(row[7]) # info
        row[7] = "" # Saving space
        formatted = "{0}${1}${2}${3}${4}${5}${6}${7}${8}\n".format(counter, *row)
        totalSize += len(formatted)
        batch.append(formatted)
        i += 1
        counter += 1
        if i % BATCH_SIZE == 0:
          flush_batch(batch, output)
          batch = []
          i = 0

    flush_batch(batch, output) # Flush the rest
    print "Final count: {0} rows written, {1} Mb".format(counter, (totalSize / 1048576))
