import psycopg2
import utils

output_dir = "./output/"

conn = psycopg2.connect("dbname={0} host={1} user={0} password={2}".format("postgres", "127.0.0.1", "testtest"))
cursor = conn.cursor()

### Individuals_#####_Phenotypes ###
cursor.execute("""SELECT * FROM "public"."phenotype";""")
f = cursor.fetchone()
while f:
	with open(output_dir + "Individuals_" + str(f[0]) + "_Phenotypes.tsv", "w") as file:
		file.write(utils.to_csv(f))
	f = cursor.fetchone()


### Batches ###
with open(output_dir + "Batches.tsv", "w") as file:
	file.write("batchId,batchDate,description,studyName\n")

	# First do the batch table
	cursor.execute("""SELECT * FROM "public"."batch";""")
	f = cursor.fetchone()
	while f:
		file.write(utils.to_csv(f) + ",Genotyping Batch\n")
		f = cursor.fetchone()

	# Then do the samples table
	cursor.execute("""SELECT * FROM "public"."samples";""")
	f = cursor.fetchone()
	while f:
		file.write(utils.to_csv(f) + ",Enrollment Batch\n")
		f = cursor.fetchone()



print "Done"
