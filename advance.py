import psycopg2
import utils

output_dir = "./output/"

conn = psycopg2.connect("dbname={0} host={1} user={0} password={2}".format("postgres", "127.0.0.1", "testtest"))
cursor = conn.cursor()

# Individuals_#####_Phenotypes
cursor.execute("""SELECT * FROM "public"."phenotype";""")
f = cursor.fetchone()
while f:
	print f
	with open(output_dir + "Individuals_" + str(f[0]) + "_Phenotypes.tsv", "w") as file:
		file.write(utils.to_csv(f))
	f = cursor.fetchone()


# Batches
cursor.execute("""SELECT * FROM "public"."batch";""")
with open(output_dir + "Batches.tsv", "w") as file:
	file.write(utils.drain_cursor_to_csv(cursor))


print "Done"
