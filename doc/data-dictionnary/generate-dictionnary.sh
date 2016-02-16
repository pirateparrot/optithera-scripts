#!/bin/bash
#
# e.g.: java -jar ~/lib/schemaSpy_5.0.0.jar -t mssql-jtds -host db.dev.ocmss.com -por/jtds-1.3.1-jdbc/jtds-1.3.1.jar -o dbdoc -p icms-ws -s ICMS
# see also: http://schemaspy.sourceforge.net/  and  https://github.com/l0b0/schemaspy2svg



###########################################################
# generate_svg_diagrams
###########################################################
# Generate SVG diagrams to replace the default PNG diagrams
#
# SYNOPSIS: generate_svg_diagrams "myhtmldoc/"
#
generate_svg_diagrams() {
	DIR=$1

	echo -e "*** Converting diagrams to SVG ..."
	# Converts all dot files to SVG
	find "$DIR" -type f -name "*.dot"  -exec dot -O -Tsvg {} +
	# Replaces all hyperlinks from PNG to SVG, making backup with .bak
	find "$DIR" -type f -name "*.html" -exec sed -i.bak s/png/dot.svg/g {} +
	# Delets backup files with extension .bak
	find "$DIR" -type f -name "*.bak"  -exec rm -f {} +
	# Deletes all PNG files
	find "$DIR" -type f -name "*.png"  -delete
}



###########################################################
# Prognomix Database (used for ADVANCE / MONICA)
###########################################################
prognomix() {
	OUTPUT_DIR="prognomix"
	PASSWORD="prognomix"

	mkdir -p $OUTPUT_DIR
	java -jar ~/lib/schemaSpy_5.0.0.jar \
		-t pgsql \
		-dp "$HOME/lib/postgresql-jdbc/postgresql-9.4-1201.jdbc41.jar" \
		-host 'localhost' \
		-port $PORT \
		-db 'prognomix' \
		-u 'prognomix' \
		-p "$PASSWORD" \
		-schemas 'prognomix,public' \
		-o "$OUTPUT_DIR" \
		-X ''

	generate_svg_diagrams $OUTPUT_DIR
}



###########################################################
# Optithera Hits Database
###########################################################
optithera() {
	OUTPUT_DIR="optitherahits2"
	PASSWORD=""

	mkdir -p $OUTPUT_DIR
	java -jar "$HOME/lib/schemaSpy_5.0.0.jar" \
		-t mysql \
		-dp \"$HOME/lib/mysql-connector-j/mysql-connector-java-5.1.35/mysql-connector-java-5.1.35-bin.jar\" \
		-host 'localhost' \
		-port $PORT \
		-db 'optitherahits2' \
		-u "root" \
		-p "$PASSWORD" \
		-schemas '' \
		-o "$OUTPUT_DIR" \
		-X ''

	generate_svg_diagrams $OUTPUT_DIR
}



###########################################################
# Biomarqueurs Database
###########################################################
biomarqueurs() {
	OUTPUT_DIR="biomarqueurs"
	PASSWORD=""

	mkdir -p $OUTPUT_DIR
	java -jar "$HOME/lib/schemaSpy_5.0.0.jar" \
		-t mysql \
		-dp \"$HOME/lib/mysql-connector-j/mysql-connector-java-5.1.35/mysql-connector-java-5.1.35-bin.jar\" \
		-host 'localhost' \
		-port $PORT \
		-db 'biomarqueurs' \
		-u "root" \
		-p "$PASSWORD" \
		-schemas '' \
		-o "$OUTPUT_DIR" \
		-X ''

	generate_svg_diagrams $OUTPUT_DIR
}



###########################################################
# MAIN
###########################################################
prognomix
optithera
biomarqueurs

