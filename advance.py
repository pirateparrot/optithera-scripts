import psycopg2
import utils

output_dir = "./output/"

conn = psycopg2.connect("dbname={0} host={1} user={0} password={2}".format("postgres", "127.0.0.1", "testtest"))
cursor = conn.cursor()

### Individuals_#####_Visits ###
cursor.execute("""
	SELECT V.person_id, V.id, V.visit_date, V.form_id, V.fasting, V.descr
	FROM "public"."visit" AS V
	ORDER BY V.person_id ASC, V.id ASC
	;
""")
f = cursor.fetchone()
current_id = -1
while f:
	if f[0] != current_id:
		try:
			file.close() # Ugly, but Python doesn't let me create ad hoc mocks like JS
		except:
			pass
		current_id = f[0]
		file = open(output_dir + "Individuals_" + str(current_id) + "_Visits.tsv", "a")
		file.write("visitId,visitDate,studyFormId,fasting,description\n")
	li = utils.to_prepared_list(f)
	file.write("""{1},{2},{3},{4},{5}\n""".format(*li))
	f = cursor.fetchone()


### Individuals_#####_Phenotypes ###
cursor.execute("""
	SELECT V.person_id, V.id, P.name, M.value, P.um, P.descr, V.visit_date
	FROM "public"."phenotype" AS P
	LEFT JOIN (
		SELECT * FROM "public"."measure" M
	) AS M ON P.id = M.phenotype_id
	LEFT JOIN "public"."visit" V ON M.visit_id = V.id
	ORDER BY V.person_id ASC
	;
""")
f = cursor.fetchone()
current_id = -1
while f:
	if f[0] != current_id:
		try:
			file.close() # Ugly, but Python doesn't let me create ad hoc mocks like JS
		except:
			pass
		current_id = f[0]
		file = open(output_dir + "Individuals_" + str(current_id) + "_Phenotypes.tsv", "a")
		file.write("visitId,phenotypeType,phenotypeGroup,name,measureDataType,measure,units,description,diagnosisDate\n")
	li = utils.to_prepared_list(f)
	file.write("""{1},,,{2},,{3},{4},{5},{6}\n""".format(*li))
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
