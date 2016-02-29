import sys
from time import sleep
import psycopg2
import utils

# Constants
# utils.NEWLINE_REPLACEMENT = " " # Can be uncommented and edited
# utils.SUBARRAY_SEPARATOR = ";" # Can be uncommented and edited
# utils.FIELD_SEPARATOR = "," # Can be uncommented and edited
NB_VERSIONS = 2

if len(sys.argv) != 6:
	print "Passed arguments were: " + str(sys.argv)
	print "Arguments must be: advance.py outputDir dbHost dbName dbUser dbPassword"
	exit(0)

output_dir = "./" + sys.argv[1]
connection_string = "dbname={1} host={0} user={2} password={3}".format(*sys.argv[2:])

print "Connecting to " + connection_string
conn = psycopg2.connect(connection_string)
cursor = conn.cursor()
print "Connected\n"
print "Number of genotype versions: " + str(NB_VERSIONS)
sleep(2)


#########################
### Individuals_##### ###
#########################
print "Exporting Individuals"
cursor.execute("""
	SELECT I.id, I.dob, I.sex, I.ethnic_code, I.centre_name, I.region_name, I.country_name, I.comments, B.batches
	FROM "public"."person" I
	INNER JOIN (
		SELECT B.person_id, array_to_string(array_agg(B.batch_id::text), '{0}') AS batches
		FROM "public"."batches" B
		GROUP BY B.person_id
		ORDER BY B.person_id ASC
	) B ON I.id = B.person_id
	ORDER BY I.id ASC
	;
""".format(utils.SUBARRAY_SEPARATOR))
print "Writing " + str(cursor.rowcount) + " files\n"
# 8 is where the batches subarray is located
subarrayPos = 8
nbBatches = utils.size_of_subarray(cursor, subarrayPos)
batchPlaceholders = utils.make_placeholders(subarrayPos, nbBatches)
headerPlaceholders = utils.make_headers("batch", 0, nbBatches)
f = cursor.fetchone()
while f:
	with open(output_dir + "Individuals_" + str(f[0]) + ".tsv", "w") as file:
		file.write("individualId,familyId,paternalId,maternalId,dateOfBirth,gender,ethnicCode,centreName,region,country,notes,{0}\n".format(headerPlaceholders))
		li = utils.to_prepared_list(f)
		li = utils.break_subarray(li, subarrayPos, nbBatches)
		file.write(("""{0},,,,{1},{2},{3},{4},{5},{6},{7},"""+batchPlaceholders+"""\n""").format(*li))
		f = cursor.fetchone()


################################
### Individuals_#####_Visits ###
################################
print "Exporting Individuals Visits"
cursor.execute("""
	SELECT V.person_id, V.id, V.visit_date, V.form_id, V.fasting, V.descr
	FROM "public"."visit" AS V
	ORDER BY V.person_id ASC, V.id ASC
	;
""")
print "Writing " + str(cursor.rowcount) + " files\n"
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


####################################
### Individuals_#####_Phenotypes ###
####################################
print "Exportung Individuals Phenotypes"
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
print "Writing " + str(cursor.rowcount) + " files\n"
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


###################################
### Individuals_#####_Genotypes ###
###################################
print "Exporting Individuals Genotypes"

mapping = {
	"1": "AA",
	"2": "BB",
	"3": "AB",
	"4": "00",
	"5": "A0",
	"6": "B0",
	"X": "00",
	"x": "00" # Saves a call to lower()
}

cursor1 = conn.cursor()
cursor2 = conn.cursor()
print "Executing SQL query 1/2..."
cursor1.execute("""
	SELECT G.person_id, G.genotype_version, G.genotype
	FROM "public"."snp_genotype" AS G
	ORDER BY person_id ASC, genotype_version ASC LIMIT 3;
""")

print "Executing SQL query 2/2..."
cursor2.execute("""
	SELECT A.genotype_index, A.annotation_version, A.rs, A.marshfield
	FROM "public"."snp_annotation" A
	ORDER BY A.annotation_version ASC, A.genotype_index ASC;
""")
print "Writing " + str(cursor1.rowcount) + " files\n"
f1 = cursor1.fetchone()
f2 = cursor2.fetchone()
while f1:
	file = open(output_dir + "Individuals_" + str(f1[0]) + "_v" + str(f1[1]) + "_Genotypes.tsv", "a")
	file.write("personId,genotypeVersion,letter,rs,marshfield\n")
	current_version = f1[1]
	buffer = []
	while f2 and f2[1] == current_version:
		li1 = utils.to_prepared_list(f1[:2])
		li2 = utils.to_prepared_list(f2[2:])
		letter = mapping[f1[2][f2[0]-1]]
		buffer.append("""{0},{1},{2},{3},{4}\n""".format(*(li1+[letter]+li2)))
		f2 = cursor2.fetchone()

	file.write("".join(buffer))
	if f2 is None: # rewind
		cursor2.scroll(0, "absolute")
		f2 = cursor2.fetchone()
	file.close()
	f1 = cursor1.fetchone()


###############
### Batches ###
###############
print "Exporting Batches"
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









