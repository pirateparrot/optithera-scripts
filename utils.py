import re

NEWLINE_REPLACEMENT = " " # Default
SUBARRAY_SEPARATOR = ";" # Default
newline_regex = re.compile(r"[\n\r]")

def prepare_fields(li):
	for i, x in enumerate(li):
		updated = str(x) if x is not None else ""
		updated = newline_regex.sub(NEWLINE_REPLACEMENT, updated)
		li[i] = updated
	return li

def to_prepared_list(li):
	return prepare_fields(list(li))

def to_csv(li):
	return ",".join(to_prepared_list(li))

def drain_cursor_to_csv(cursor):
	result = []
	f = cursor.fetchone()
	while f:
		result.append(to_csv(f))
		f = cursor.fetchone()
	return "\n".join(result)

def size_of_subarray(cursor, pos):
	size = 0
	cursor.scroll(0, "absolute")
	f = cursor.fetchone()
	while f:
		size = max(size, len(f[pos].split(SUBARRAY_SEPARATOR)))
		f = cursor.fetchone()
	cursor.scroll(0, "absolute")
	return size

def break_subarray(li, pos, size):
	sub = li[pos].split(SUBARRAY_SEPARATOR)
	padding = [None for i in xrange(0, size - len(sub))]
	return li[:pos] + sub + li[pos+1:] + padding

def make_headers(name, pos, nb):
	return ",".join([name+str(i) for i in xrange(pos, nb)])

def make_placeholders(pos, nb):
	return ",".join(["{"+str(i)+"}" for i in xrange(pos, pos+nb)])
