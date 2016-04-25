import sys
import os
from time import sleep
import psycopg2

if len(sys.argv) != 6:
  print "Passed arguments were: " + str(sys.argv)
  print "Arguments must be: spreadsheets.py output dbHost dbName dbUser dbPassword"
  exit(0)

output_dir = "./" + sys.argv[1]
output_dir = output_dir if output_dir[-1] == "/" else output_dir + "/"
print "Reading data from Postgres and writing to {0}".format(output_dir)

connection_string = "dbname={1} host={0} user={2} password={3}".format(*sys.argv[2:])

print "Connecting to " + connection_string
conn = psycopg2.connect(connection_string)
cursor = conn.cursor()
print "Connected\n"

cursor.execute("""
  SELECT DISTINCT A.chromosome
  FROM "public"."snp_annotation" A
  ORDER BY A.chromosome ASC
""")

NULL_CHR = "_"
chromosomes = [(row[0] if row[0] is not None else NULL_CHR) for row in cursor.fetchall()]
print "{0} chromosomes found: {1}".format(len(chromosomes), ", ".join(chromosomes))

for chromosome in chromosomes:
  if chromosome is NULL_CHR:
    continue
  filename = "{0}mismatches_{1}.csv".format(output_dir, chromosome)
  with open(filename, "w") as file:
    cursor.execute("""
      SELECT
      A.rs, V.chromosome AS "VCF_chr", A.chromosome AS "SNP_chr",
      V.position AS "VCF_pos", A.position AS "SNP_pos", ABS(V.position - A.position) AS "pos_spread",
      (100 * CAST((ABS(V.position - A.position)) AS DOUBLE PRECISION) / CAST(((V.position + A.position) / 2) AS DOUBLE PRECISION)) AS "pos_pct_diff",
      V.ref, V.alt, A.allele_a, A.allele_b
      FROM "public"."snp_annotation" A
      LEFT JOIN "public"."vcf" V ON A.rs = V.rs
      WHERE A.chromosome = '{0}' AND (A.position <> V.position OR A.chromosome <> V.chromosome)
    """.format(chromosome))
    print "Writing {0} rows to {1}".format(cursor.rowcount, filename)
    file.write("rs,VCF_chr,SNP_chr,VCF_pos,SNP_pos,pos_spread,pos_pct_diff,ref,alt,allele_a,allele_b\n")
    buffer = []
    f = cursor.fetchone()
    while f:
      buffer.append("""{0},{1},{2},{3},{4},{5},{6},{7},"{8}",{9},{10}\n""".format(*list(f)))
      f = cursor.fetchone()
    file.write("".join(buffer))
  # exit(0)















