import psycopg2
import utils

output_dir = "./output/"

conn = psycopg2.connect("dbname={0} host={1} user={0} password={2}".format("postgres", "127.0.0.1", "testtest"))
cursor = conn.cursor()

### Individuals_#####_Phenotypes ###
cursor.execute("""
	SELECT V.person_id, V.id, P.name, M.value, P.um, P.descr, V.visit_date
	FROM "public"."phenotype" AS P
	LEFT JOIN (
		SELECT DISTINCT ON (phenotype_id) * FROM "public"."measure" M
	) AS M ON P.id = M.phenotype_id
	LEFT JOIN "public"."visit" V ON M.visit_id = V.id
	;
""")
f = cursor.fetchone()
while f:
	with open(output_dir + "Individuals_" + str(f[0]) + "_Phenotypes.tsv", "w") as file:
		file.write("visitId,phenotypeType,phenotypeGroup,name,measureDataType,measure,units,description,diagnosisDate\n")
		li = utils.to_prepared_list(f)
		file.write("""{1},,,{2},,{3},{4},{5},{6}""".format(*li))
	f = cursor.fetchone()


### Batches ###
with open(output_dir + "Batches.tsv", "w") as file:
	file.write("batchId,batchDate,batchType,description,studyName\n")

	# First do the batch table
	cursor.execute("""SELECT B.id, B.batch_date, B.description FROM "public"."batch" AS B;""")
	f = cursor.fetchone()
	while f:
		li = utils.to_prepared_list(f)
		file.write("""{0},{1},ADVANCE-ON,{2},Genotyping Batch\n""".format(*li))
		f = cursor.fetchone()

	# Then do the samples table
	cursor.execute("""SELECT B.id, B.sample_date, B.description FROM "public"."samples" AS B;""")
	f = cursor.fetchone()
	while f:
		li = utils.to_prepared_list(f)
		file.write("""{0},{1},ADVANCE-ON,{2},Enrollment Batch\n""".format(*li))
		f = cursor.fetchone()



print "Done"
