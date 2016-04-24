import sys
import os
from time import sleep
import psycopg2

if len(sys.argv) != 6:
  print "Passed arguments were: " + str(sys.argv)
  print "Arguments must be: loadVCF.py .vcf.csv_File dbHost dbName dbUser dbPassword"
  exit(0)

CSV_INPUT = sys.argv[1]
CSV_INPUT_ABSOLUTE = os.path.abspath("./{0}".format(CSV_INPUT))
print "Reading data from {0} and writing to Postgres".format(CSV_INPUT)

connection_string = "dbname={1} host={0} user={2} password={3}".format(*sys.argv[2:])

print "Connecting to " + connection_string
conn = psycopg2.connect(connection_string)
cursor = conn.cursor()
print "Connected\n"

#################################################
print "Create or replace table vcf"
cursor.execute("""DROP TABLE IF EXISTS vcf""")
conn.commit()
cursor.execute("""
  CREATE TABLE vcf
  (
    id integer NOT NULL,
    chromosome text NOT NULL,
    "position" integer NOT NULL,
    rs character varying(32) NOT NULL,
    ref text NOT NULL,
    alt text NOT NULL,
    quality text,
    filter text,
    info text,
    CONSTRAINT vcf_pk PRIMARY KEY (id)
  )
  ;
""")
conn.commit()
print "Done."

#################################################
print "Loading the file, this could take a while"
cursor.execute("""
  COPY vcf (id, chromosome, "position", rs, ref, alt, quality, filter, info)
  FROM '{0}' CSV DELIMITER '$'
""".format(CSV_INPUT_ABSOLUTE))
conn.commit()
print "Done."

##########################################################
print "Setting indexes and constraints, takes a long time"
sleep(2)
# Index might already exist. CREATE INDEX IF NOT EXISTS is only available in Postgres 9.5
try:
  cursor.execute("""
    CREATE UNIQUE INDEX vcf_index_rs
    ON vcf
    USING btree
    (rs COLLATE pg_catalog."default");
  """)
except:
  pass
conn.commit()

cursor.execute("""SELECT COUNT(*) FROM vcf""")
print "{0} rows written".format(cursor.fetchone()[0])
