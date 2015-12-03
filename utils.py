def prepare_fields(li):
	for i, x in enumerate(li):
		updated = str(x)
		li[i] = updated if "\n" not in updated else "\"" + updated + "\""
	return li

def to_csv(li):
	return ",".join(prepare_fields(list(li)))

def drain_cursor_to_csv(cursor):
	result = []
	f = cursor.fetchone()
	while f:
		result.append(to_csv(f))
		f = cursor.fetchone()
	return "\n".join(result)
