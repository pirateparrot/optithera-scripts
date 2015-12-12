import re

NEWLINE_REPLACEMENT = " " # Default
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
