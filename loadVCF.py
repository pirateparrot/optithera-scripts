import sys
from time import sleep
import psycopg2

VCF_FILE = "00-All.vcf"
BATCH_SIZE = 5000

if len(sys.argv) != 5:
  print "Passed arguments were: " + str(sys.argv)
  print "Arguments must be: loadVCF.py dbHost dbName dbUser dbPassword"
  exit(0)

connection_string = "dbname={1} host={0} user={2} password={3}".format(*sys.argv[1:])

print "Connecting to " + connection_string
conn = psycopg2.connect(connection_string)
cursor = conn.cursor()
print "Connected\n"

print "Creating vcf table if it doesn't exist"
cursor.execute("""
  CREATE TABLE IF NOT EXISTS vcf
  (
    id integer NOT NULL,
    chromosome integer NOT NULL,
    "position" integer NOT NULL,
    rs character varying(32) NOT NULL,
    ref text NOT NULL,
    alt text NOT NULL,
    quality text,
    filter text,
    info text,
    CONSTRAINT vcf_pk PRIMARY KEY (id),
    CONSTRAINT vcf_unique_rs UNIQUE (rs)
  )
  ;
""")
conn.commit()

# Index might already exist. CREATE INDEX IF NOT EXISTS is only available in Postgres 9.5
try:
  cursor.execute("""
    CREATE INDEX vcf_index_rs
    ON vcf
    USING btree
    (rs COLLATE pg_catalog."default");
  """)
except:
  pass
conn.commit()


##############################################
print "Loading data from {0}".format(VCF_FILE)

def nullify(row):
  return "NULL" if (row == " " or row == ".") else ("'" + row.strip() + "'")

batch = []
i = 0
totalSize = 0
counter = 0

def flush_batch(batch):
  if len(batch) == 0:
    return
  base = "INSERT INTO vcf VALUES {0};"
  rows = ["(" + row + ")" for row in batch]
  statement = base.format(",".join(rows))
  print "Writing {0} rows ({1}) {2} bytes".format(len(batch), counter, totalSize)
  cursor.execute(statement)
  conn.commit()
  sleep(0.1)

with open("./{0}".format(VCF_FILE,'r')) as file:
  for line in file:
    if line[0] != "#": # Skip comments and headers
      row = line.split(chr(9)) # Split on tabs
      row[5] = nullify(row[5]) # quality
      row[6] = nullify(row[6]) # filter
      # row[7] = nullify(row[7]) # info
      row[7] = "NULL" # Saving space
      formatted = "{0},{1},{2},'{3}','{4}','{5}',{6},{7},{8}".format(counter, *row)
      totalSize += len(formatted)
      batch.append(formatted)
      i += 1
      counter += 1
      if i % BATCH_SIZE == 0:
        flush_batch(batch)
        batch = []
        i = 0

file.close()
















