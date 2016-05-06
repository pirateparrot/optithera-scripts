import re

NEWLINE_REPLACEMENT = " " # Default
SUBARRAY_SEPARATOR = ";" # Default
FIELD_SEPARATOR = "," # Default
newline_regex = re.compile(r"[\n\r]")

# Sanitizes all the elements in a list
def prepare_fields(li):
	for i, x in enumerate(li):
		updated = str(x) if x is not None else ""
		updated = newline_regex.sub(NEWLINE_REPLACEMENT, updated)
		li[i] = updated
	return li

# Turns a cursor into a list ready to write
def to_prepared_list(li):
	return prepare_fields(list(li))

# Turns a cursor into a csv string
def to_csv(li):
	return FIELD_SEPARATOR.join(to_prepared_list(li))

# Completely drains a cursor to return a csv string
def drain_cursor_to_csv(cursor):
	result = []
	f = cursor.fetchone()
	while f:
		result.append(to_csv(f))
		f = cursor.fetchone()
	return "\n".join(result)

# Returns the length of the longest element at a position.
# Rewinds the cursor before returning
def size_of_subarray(cursor, pos):
	size = 0
	cursor.scroll(0, "absolute")
	f = cursor.fetchone()
	while f:
		size = max(size, len(f[pos].split(SUBARRAY_SEPARATOR)))
		f = cursor.fetchone()
	cursor.scroll(0, "absolute")
	return size

# Turns the subarray created by array_to_string(array_agg(...)) into a python list
def break_subarray(li, pos, size):
	sub = li[pos].split(SUBARRAY_SEPARATOR)
	padding = ["" for i in xrange(0, size - len(sub))]
	return li[:pos] + sub + li[pos+1:] + padding

# Prepares headers dynamically based on the number of columns
def make_headers(name, pos, nb):
	return FIELD_SEPARATOR.join([name+str(i) for i in xrange(pos, nb)])

# Prepares placeholders (ex: {0}) dynamically to fill in with format()
def make_placeholders(pos, nb):
	return FIELD_SEPARATOR.join(["{"+str(i)+"}" for i in xrange(pos, pos+nb)])
