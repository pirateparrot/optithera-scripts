/**
 * Autogenerated by Avro
 * 
 * DO NOT EDIT DIRECTLY
 */
package org.bdgenomics.formats.avro;  
@SuppressWarnings("all")
/** Enumeration for DNA/RNA bases. For codes outside of ACTGU, see the IUPAC
 resolution codes (http://www.bioinformatics.org/sms/iupac.html). */
@org.apache.avro.specific.AvroGenerated
public enum Base { 
  A, C, T, G, U, N, X, K, M, R, Y, S, W, B, V, H, D  ;
  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"enum\",\"name\":\"Base\",\"namespace\":\"org.bdgenomics.formats.avro\",\"doc\":\"Enumeration for DNA/RNA bases. For codes outside of ACTGU, see the IUPAC\\n resolution codes (http://www.bioinformatics.org/sms/iupac.html).\",\"symbols\":[\"A\",\"C\",\"T\",\"G\",\"U\",\"N\",\"X\",\"K\",\"M\",\"R\",\"Y\",\"S\",\"W\",\"B\",\"V\",\"H\",\"D\"]}");
  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }
}