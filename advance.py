import psycopg2
import utils

conn = psycopg2.connect("dbname={0} host={1} user={0} password={2}".format("postgres", "127.0.0.1", "testtest"))
cursor = conn.cursor()

cursor.execute("""SELECT * FROM "public"."batch";""")
print utils.drain_cursor_to_csv(cursor)


print "hello world"
