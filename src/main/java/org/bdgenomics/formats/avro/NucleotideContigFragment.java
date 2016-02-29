/**
 * Autogenerated by Avro
 * 
 * DO NOT EDIT DIRECTLY
 */
package org.bdgenomics.formats.avro;  
@SuppressWarnings("all")
/** Stores a contig of nucleotides; this may be a reference chromosome, may be an
 assembly, may be a BAC. Very long contigs (>1Mbp) need to be split into fragments.
 It seems that they are too long to load in a single go. For best performance,
 it seems like 10kbp is a good point at which to start splitting contigs into
 fragments. */
@org.apache.avro.specific.AvroGenerated
public class NucleotideContigFragment extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
  private static final long serialVersionUID = -7675446384174868707L;
  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"NucleotideContigFragment\",\"namespace\":\"org.bdgenomics.formats.avro\",\"doc\":\"Stores a contig of nucleotides; this may be a reference chromosome, may be an\\n assembly, may be a BAC. Very long contigs (>1Mbp) need to be split into fragments.\\n It seems that they are too long to load in a single go. For best performance,\\n it seems like 10kbp is a good point at which to start splitting contigs into\\n fragments.\",\"fields\":[{\"name\":\"contig\",\"type\":[\"null\",{\"type\":\"record\",\"name\":\"Contig\",\"doc\":\"Record for describing a reference assembly. Not used for storing the contents\\n of said assembly.\\n\\n @see NucleotideContigFragment\",\"fields\":[{\"name\":\"contigName\",\"type\":[\"null\",\"string\"],\"doc\":\"The name of this contig in the assembly (e.g., \\\"chr1\\\").\",\"default\":null},{\"name\":\"contigLength\",\"type\":[\"null\",\"long\"],\"doc\":\"The length of this contig.\",\"default\":null},{\"name\":\"contigMD5\",\"type\":[\"null\",\"string\"],\"doc\":\"The MD5 checksum of the assembly for this contig.\",\"default\":null},{\"name\":\"referenceURL\",\"type\":[\"null\",\"string\"],\"doc\":\"The URL at which this reference assembly can be found.\",\"default\":null},{\"name\":\"assembly\",\"type\":[\"null\",\"string\"],\"doc\":\"The name of this assembly (e.g., \\\"hg19\\\").\",\"default\":null},{\"name\":\"species\",\"type\":[\"null\",\"string\"],\"doc\":\"The species that this assembly is for.\",\"default\":null},{\"name\":\"referenceIndex\",\"type\":[\"null\",\"int\"],\"doc\":\"Optional 0-based index of this contig in a SAM file header that it was read\\n   from; helps output SAMs/BAMs with headers in the same order as they started\\n   with, before a conversion to ADAM.\",\"default\":null}]}],\"doc\":\"The contig identification descriptor for this contig.\",\"default\":null},{\"name\":\"description\",\"type\":[\"null\",\"string\"],\"doc\":\"A description for this contig. When importing from FASTA, the FASTA header\\n   description line should be stored here.\",\"default\":null},{\"name\":\"fragmentSequence\",\"type\":[\"null\",\"string\"],\"doc\":\"The sequence of bases in this fragment.\",\"default\":null},{\"name\":\"fragmentNumber\",\"type\":[\"null\",\"int\"],\"doc\":\"In a fragmented contig, the position of this fragment in the set of fragments.\\n   Can be null if the contig is not fragmented.\",\"default\":null},{\"name\":\"fragmentStartPosition\",\"type\":[\"null\",\"long\"],\"doc\":\"The position of the first base of this fragment in the overall contig. E.g.,\\n   if all fragments are 10kbp and this is the third fragment in the contig,\\n   the start position would be 20000L.\",\"default\":null},{\"name\":\"fragmentLength\",\"type\":[\"null\",\"long\"],\"doc\":\"The length of this fragment.\",\"default\":null},{\"name\":\"numberOfFragmentsInContig\",\"type\":[\"null\",\"int\"],\"doc\":\"The total count of fragments that this contig has been broken into. Can be\\n   null if the contig is not fragmented.\",\"default\":null}]}");
  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }
  /** The contig identification descriptor for this contig. */
  @Deprecated public org.bdgenomics.formats.avro.Contig contig;
  /** A description for this contig. When importing from FASTA, the FASTA header
   description line should be stored here. */
  @Deprecated public java.lang.CharSequence description;
  /** The sequence of bases in this fragment. */
  @Deprecated public java.lang.CharSequence fragmentSequence;
  /** In a fragmented contig, the position of this fragment in the set of fragments.
   Can be null if the contig is not fragmented. */
  @Deprecated public java.lang.Integer fragmentNumber;
  /** The position of the first base of this fragment in the overall contig. E.g.,
   if all fragments are 10kbp and this is the third fragment in the contig,
   the start position would be 20000L. */
  @Deprecated public java.lang.Long fragmentStartPosition;
  /** The length of this fragment. */
  @Deprecated public java.lang.Long fragmentLength;
  /** The total count of fragments that this contig has been broken into. Can be
   null if the contig is not fragmented. */
  @Deprecated public java.lang.Integer numberOfFragmentsInContig;

  /**
   * Default constructor.  Note that this does not initialize fields
   * to their default values from the schema.  If that is desired then
   * one should use <code>newBuilder()</code>. 
   */
  public NucleotideContigFragment() {}

  /**
   * All-args constructor.
   * @param contig The contig identification descriptor for this contig.
   * @param description A description for this contig. When importing from FASTA, the FASTA header
   description line should be stored here.
   * @param fragmentSequence The sequence of bases in this fragment.
   * @param fragmentNumber In a fragmented contig, the position of this fragment in the set of fragments.
   Can be null if the contig is not fragmented.
   * @param fragmentStartPosition The position of the first base of this fragment in the overall contig. E.g.,
   if all fragments are 10kbp and this is the third fragment in the contig,
   the start position would be 20000L.
   * @param fragmentLength The length of this fragment.
   * @param numberOfFragmentsInContig The total count of fragments that this contig has been broken into. Can be
   null if the contig is not fragmented.
   */
  public NucleotideContigFragment(org.bdgenomics.formats.avro.Contig contig, java.lang.CharSequence description, java.lang.CharSequence fragmentSequence, java.lang.Integer fragmentNumber, java.lang.Long fragmentStartPosition, java.lang.Long fragmentLength, java.lang.Integer numberOfFragmentsInContig) {
    this.contig = contig;
    this.description = description;
    this.fragmentSequence = fragmentSequence;
    this.fragmentNumber = fragmentNumber;
    this.fragmentStartPosition = fragmentStartPosition;
    this.fragmentLength = fragmentLength;
    this.numberOfFragmentsInContig = numberOfFragmentsInContig;
  }

  public org.apache.avro.Schema getSchema() { return SCHEMA$; }
  // Used by DatumWriter.  Applications should not call. 
  public java.lang.Object get(int field$) {
    switch (field$) {
    case 0: return contig;
    case 1: return description;
    case 2: return fragmentSequence;
    case 3: return fragmentNumber;
    case 4: return fragmentStartPosition;
    case 5: return fragmentLength;
    case 6: return numberOfFragmentsInContig;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }
  // Used by DatumReader.  Applications should not call. 
  @SuppressWarnings(value="unchecked")
  public void put(int field$, java.lang.Object value$) {
    switch (field$) {
    case 0: contig = (org.bdgenomics.formats.avro.Contig)value$; break;
    case 1: description = (java.lang.CharSequence)value$; break;
    case 2: fragmentSequence = (java.lang.CharSequence)value$; break;
    case 3: fragmentNumber = (java.lang.Integer)value$; break;
    case 4: fragmentStartPosition = (java.lang.Long)value$; break;
    case 5: fragmentLength = (java.lang.Long)value$; break;
    case 6: numberOfFragmentsInContig = (java.lang.Integer)value$; break;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }

  /**
   * Gets the value of the 'contig' field.
   * @return The contig identification descriptor for this contig.
   */
  public org.bdgenomics.formats.avro.Contig getContig() {
    return contig;
  }

  /**
   * Sets the value of the 'contig' field.
   * The contig identification descriptor for this contig.
   * @param value the value to set.
   */
  public void setContig(org.bdgenomics.formats.avro.Contig value) {
    this.contig = value;
  }

  /**
   * Gets the value of the 'description' field.
   * @return A description for this contig. When importing from FASTA, the FASTA header
   description line should be stored here.
   */
  public java.lang.CharSequence getDescription() {
    return description;
  }

  /**
   * Sets the value of the 'description' field.
   * A description for this contig. When importing from FASTA, the FASTA header
   description line should be stored here.
   * @param value the value to set.
   */
  public void setDescription(java.lang.CharSequence value) {
    this.description = value;
  }

  /**
   * Gets the value of the 'fragmentSequence' field.
   * @return The sequence of bases in this fragment.
   */
  public java.lang.CharSequence getFragmentSequence() {
    return fragmentSequence;
  }

  /**
   * Sets the value of the 'fragmentSequence' field.
   * The sequence of bases in this fragment.
   * @param value the value to set.
   */
  public void setFragmentSequence(java.lang.CharSequence value) {
    this.fragmentSequence = value;
  }

  /**
   * Gets the value of the 'fragmentNumber' field.
   * @return In a fragmented contig, the position of this fragment in the set of fragments.
   Can be null if the contig is not fragmented.
   */
  public java.lang.Integer getFragmentNumber() {
    return fragmentNumber;
  }

  /**
   * Sets the value of the 'fragmentNumber' field.
   * In a fragmented contig, the position of this fragment in the set of fragments.
   Can be null if the contig is not fragmented.
   * @param value the value to set.
   */
  public void setFragmentNumber(java.lang.Integer value) {
    this.fragmentNumber = value;
  }

  /**
   * Gets the value of the 'fragmentStartPosition' field.
   * @return The position of the first base of this fragment in the overall contig. E.g.,
   if all fragments are 10kbp and this is the third fragment in the contig,
   the start position would be 20000L.
   */
  public java.lang.Long getFragmentStartPosition() {
    return fragmentStartPosition;
  }

  /**
   * Sets the value of the 'fragmentStartPosition' field.
   * The position of the first base of this fragment in the overall contig. E.g.,
   if all fragments are 10kbp and this is the third fragment in the contig,
   the start position would be 20000L.
   * @param value the value to set.
   */
  public void setFragmentStartPosition(java.lang.Long value) {
    this.fragmentStartPosition = value;
  }

  /**
   * Gets the value of the 'fragmentLength' field.
   * @return The length of this fragment.
   */
  public java.lang.Long getFragmentLength() {
    return fragmentLength;
  }

  /**
   * Sets the value of the 'fragmentLength' field.
   * The length of this fragment.
   * @param value the value to set.
   */
  public void setFragmentLength(java.lang.Long value) {
    this.fragmentLength = value;
  }

  /**
   * Gets the value of the 'numberOfFragmentsInContig' field.
   * @return The total count of fragments that this contig has been broken into. Can be
   null if the contig is not fragmented.
   */
  public java.lang.Integer getNumberOfFragmentsInContig() {
    return numberOfFragmentsInContig;
  }

  /**
   * Sets the value of the 'numberOfFragmentsInContig' field.
   * The total count of fragments that this contig has been broken into. Can be
   null if the contig is not fragmented.
   * @param value the value to set.
   */
  public void setNumberOfFragmentsInContig(java.lang.Integer value) {
    this.numberOfFragmentsInContig = value;
  }

  /**
   * Creates a new NucleotideContigFragment RecordBuilder.
   * @return A new NucleotideContigFragment RecordBuilder
   */
  public static org.bdgenomics.formats.avro.NucleotideContigFragment.Builder newBuilder() {
    return new org.bdgenomics.formats.avro.NucleotideContigFragment.Builder();
  }
  
  /**
   * Creates a new NucleotideContigFragment RecordBuilder by copying an existing Builder.
   * @param other The existing builder to copy.
   * @return A new NucleotideContigFragment RecordBuilder
   */
  public static org.bdgenomics.formats.avro.NucleotideContigFragment.Builder newBuilder(org.bdgenomics.formats.avro.NucleotideContigFragment.Builder other) {
    return new org.bdgenomics.formats.avro.NucleotideContigFragment.Builder(other);
  }
  
  /**
   * Creates a new NucleotideContigFragment RecordBuilder by copying an existing NucleotideContigFragment instance.
   * @param other The existing instance to copy.
   * @return A new NucleotideContigFragment RecordBuilder
   */
  public static org.bdgenomics.formats.avro.NucleotideContigFragment.Builder newBuilder(org.bdgenomics.formats.avro.NucleotideContigFragment other) {
    return new org.bdgenomics.formats.avro.NucleotideContigFragment.Builder(other);
  }
  
  /**
   * RecordBuilder for NucleotideContigFragment instances.
   */
  public static class Builder extends org.apache.avro.specific.SpecificRecordBuilderBase<NucleotideContigFragment>
    implements org.apache.avro.data.RecordBuilder<NucleotideContigFragment> {

    /** The contig identification descriptor for this contig. */
    private org.bdgenomics.formats.avro.Contig contig;
    private org.bdgenomics.formats.avro.Contig.Builder contigBuilder;
    /** A description for this contig. When importing from FASTA, the FASTA header
   description line should be stored here. */
    private java.lang.CharSequence description;
    /** The sequence of bases in this fragment. */
    private java.lang.CharSequence fragmentSequence;
    /** In a fragmented contig, the position of this fragment in the set of fragments.
   Can be null if the contig is not fragmented. */
    private java.lang.Integer fragmentNumber;
    /** The position of the first base of this fragment in the overall contig. E.g.,
   if all fragments are 10kbp and this is the third fragment in the contig,
   the start position would be 20000L. */
    private java.lang.Long fragmentStartPosition;
    /** The length of this fragment. */
    private java.lang.Long fragmentLength;
    /** The total count of fragments that this contig has been broken into. Can be
   null if the contig is not fragmented. */
    private java.lang.Integer numberOfFragmentsInContig;

    /** Creates a new Builder */
    private Builder() {
      super(org.bdgenomics.formats.avro.NucleotideContigFragment.SCHEMA$);
    }
    
    /**
     * Creates a Builder by copying an existing Builder.
     * @param other The existing Builder to copy.
     */
    private Builder(org.bdgenomics.formats.avro.NucleotideContigFragment.Builder other) {
      super(other);
      if (isValidValue(fields()[0], other.contig)) {
        this.contig = data().deepCopy(fields()[0].schema(), other.contig);
        fieldSetFlags()[0] = true;
      }
      if (other.hasContigBuilder()) {
        this.contigBuilder = org.bdgenomics.formats.avro.Contig.newBuilder(other.getContigBuilder());
      }
      if (isValidValue(fields()[1], other.description)) {
        this.description = data().deepCopy(fields()[1].schema(), other.description);
        fieldSetFlags()[1] = true;
      }
      if (isValidValue(fields()[2], other.fragmentSequence)) {
        this.fragmentSequence = data().deepCopy(fields()[2].schema(), other.fragmentSequence);
        fieldSetFlags()[2] = true;
      }
      if (isValidValue(fields()[3], other.fragmentNumber)) {
        this.fragmentNumber = data().deepCopy(fields()[3].schema(), other.fragmentNumber);
        fieldSetFlags()[3] = true;
      }
      if (isValidValue(fields()[4], other.fragmentStartPosition)) {
        this.fragmentStartPosition = data().deepCopy(fields()[4].schema(), other.fragmentStartPosition);
        fieldSetFlags()[4] = true;
      }
      if (isValidValue(fields()[5], other.fragmentLength)) {
        this.fragmentLength = data().deepCopy(fields()[5].schema(), other.fragmentLength);
        fieldSetFlags()[5] = true;
      }
      if (isValidValue(fields()[6], other.numberOfFragmentsInContig)) {
        this.numberOfFragmentsInContig = data().deepCopy(fields()[6].schema(), other.numberOfFragmentsInContig);
        fieldSetFlags()[6] = true;
      }
    }
    
    /**
     * Creates a Builder by copying an existing NucleotideContigFragment instance
     * @param other The existing instance to copy.
     */
    private Builder(org.bdgenomics.formats.avro.NucleotideContigFragment other) {
            super(org.bdgenomics.formats.avro.NucleotideContigFragment.SCHEMA$);
      if (isValidValue(fields()[0], other.contig)) {
        this.contig = data().deepCopy(fields()[0].schema(), other.contig);
        fieldSetFlags()[0] = true;
      }
      this.contigBuilder = null;
      if (isValidValue(fields()[1], other.description)) {
        this.description = data().deepCopy(fields()[1].schema(), other.description);
        fieldSetFlags()[1] = true;
      }
      if (isValidValue(fields()[2], other.fragmentSequence)) {
        this.fragmentSequence = data().deepCopy(fields()[2].schema(), other.fragmentSequence);
        fieldSetFlags()[2] = true;
      }
      if (isValidValue(fields()[3], other.fragmentNumber)) {
        this.fragmentNumber = data().deepCopy(fields()[3].schema(), other.fragmentNumber);
        fieldSetFlags()[3] = true;
      }
      if (isValidValue(fields()[4], other.fragmentStartPosition)) {
        this.fragmentStartPosition = data().deepCopy(fields()[4].schema(), other.fragmentStartPosition);
        fieldSetFlags()[4] = true;
      }
      if (isValidValue(fields()[5], other.fragmentLength)) {
        this.fragmentLength = data().deepCopy(fields()[5].schema(), other.fragmentLength);
        fieldSetFlags()[5] = true;
      }
      if (isValidValue(fields()[6], other.numberOfFragmentsInContig)) {
        this.numberOfFragmentsInContig = data().deepCopy(fields()[6].schema(), other.numberOfFragmentsInContig);
        fieldSetFlags()[6] = true;
      }
    }

    /**
      * Gets the value of the 'contig' field.
      * The contig identification descriptor for this contig.
      * @return The value.
      */
    public org.bdgenomics.formats.avro.Contig getContig() {
      return contig;
    }

    /**
      * Sets the value of the 'contig' field.
      * The contig identification descriptor for this contig.
      * @param value The value of 'contig'.
      * @return This builder.
      */
    public org.bdgenomics.formats.avro.NucleotideContigFragment.Builder setContig(org.bdgenomics.formats.avro.Contig value) {
      validate(fields()[0], value);
      this.contigBuilder = null;
      this.contig = value;
      fieldSetFlags()[0] = true;
      return this; 
    }

    /**
      * Checks whether the 'contig' field has been set.
      * The contig identification descriptor for this contig.
      * @return True if the 'contig' field has been set, false otherwise.
      */
    public boolean hasContig() {
      return fieldSetFlags()[0];
    }

    /**
     * Gets the Builder instance for the 'contig' field and creates one if it doesn't exist yet.
     * The contig identification descriptor for this contig.
     * @return This builder.
     */
    public org.bdgenomics.formats.avro.Contig.Builder getContigBuilder() {
      if (contigBuilder == null) {
        if (hasContig()) {
          setContigBuilder(org.bdgenomics.formats.avro.Contig.newBuilder(contig));
        } else {
          setContigBuilder(org.bdgenomics.formats.avro.Contig.newBuilder());
        }
      }
      return contigBuilder;
    }

    /**
     * Sets the Builder instance for the 'contig' field
     * The contig identification descriptor for this contig.
     * @return This builder.
     */
    public org.bdgenomics.formats.avro.NucleotideContigFragment.Builder setContigBuilder(org.bdgenomics.formats.avro.Contig.Builder value) {
      clearContig();
      contigBuilder = value;
      return this;
    }

    /**
     * Checks whether the 'contig' field has an active Builder instance
     * The contig identification descriptor for this contig.
     * @return True if the 'contig' field has an active Builder instance
     */
    public boolean hasContigBuilder() {
      return contigBuilder != null;
    }

    /**
      * Clears the value of the 'contig' field.
      * The contig identification descriptor for this contig.
      * @return This builder.
      */
    public org.bdgenomics.formats.avro.NucleotideContigFragment.Builder clearContig() {
      contig = null;
      contigBuilder = null;
      fieldSetFlags()[0] = false;
      return this;
    }

    /**
      * Gets the value of the 'description' field.
      * A description for this contig. When importing from FASTA, the FASTA header
   description line should be stored here.
      * @return The value.
      */
    public java.lang.CharSequence getDescription() {
      return description;
    }

    /**
      * Sets the value of the 'description' field.
      * A description for this contig. When importing from FASTA, the FASTA header
   description line should be stored here.
      * @param value The value of 'description'.
      * @return This builder.
      */
    public org.bdgenomics.formats.avro.NucleotideContigFragment.Builder setDescription(java.lang.CharSequence value) {
      validate(fields()[1], value);
      this.description = value;
      fieldSetFlags()[1] = true;
      return this; 
    }

    /**
      * Checks whether the 'description' field has been set.
      * A description for this contig. When importing from FASTA, the FASTA header
   description line should be stored here.
      * @return True if the 'description' field has been set, false otherwise.
      */
    public boolean hasDescription() {
      return fieldSetFlags()[1];
    }


    /**
      * Clears the value of the 'description' field.
      * A description for this contig. When importing from FASTA, the FASTA header
   description line should be stored here.
      * @return This builder.
      */
    public org.bdgenomics.formats.avro.NucleotideContigFragment.Builder clearDescription() {
      description = null;
      fieldSetFlags()[1] = false;
      return this;
    }

    /**
      * Gets the value of the 'fragmentSequence' field.
      * The sequence of bases in this fragment.
      * @return The value.
      */
    public java.lang.CharSequence getFragmentSequence() {
      return fragmentSequence;
    }

    /**
      * Sets the value of the 'fragmentSequence' field.
      * The sequence of bases in this fragment.
      * @param value The value of 'fragmentSequence'.
      * @return This builder.
      */
    public org.bdgenomics.formats.avro.NucleotideContigFragment.Builder setFragmentSequence(java.lang.CharSequence value) {
      validate(fields()[2], value);
      this.fragmentSequence = value;
      fieldSetFlags()[2] = true;
      return this; 
    }

    /**
      * Checks whether the 'fragmentSequence' field has been set.
      * The sequence of bases in this fragment.
      * @return True if the 'fragmentSequence' field has been set, false otherwise.
      */
    public boolean hasFragmentSequence() {
      return fieldSetFlags()[2];
    }


    /**
      * Clears the value of the 'fragmentSequence' field.
      * The sequence of bases in this fragment.
      * @return This builder.
      */
    public org.bdgenomics.formats.avro.NucleotideContigFragment.Builder clearFragmentSequence() {
      fragmentSequence = null;
      fieldSetFlags()[2] = false;
      return this;
    }

    /**
      * Gets the value of the 'fragmentNumber' field.
      * In a fragmented contig, the position of this fragment in the set of fragments.
   Can be null if the contig is not fragmented.
      * @return The value.
      */
    public java.lang.Integer getFragmentNumber() {
      return fragmentNumber;
    }

    /**
      * Sets the value of the 'fragmentNumber' field.
      * In a fragmented contig, the position of this fragment in the set of fragments.
   Can be null if the contig is not fragmented.
      * @param value The value of 'fragmentNumber'.
      * @return This builder.
      */
    public org.bdgenomics.formats.avro.NucleotideContigFragment.Builder setFragmentNumber(java.lang.Integer value) {
      validate(fields()[3], value);
      this.fragmentNumber = value;
      fieldSetFlags()[3] = true;
      return this; 
    }

    /**
      * Checks whether the 'fragmentNumber' field has been set.
      * In a fragmented contig, the position of this fragment in the set of fragments.
   Can be null if the contig is not fragmented.
      * @return True if the 'fragmentNumber' field has been set, false otherwise.
      */
    public boolean hasFragmentNumber() {
      return fieldSetFlags()[3];
    }


    /**
      * Clears the value of the 'fragmentNumber' field.
      * In a fragmented contig, the position of this fragment in the set of fragments.
   Can be null if the contig is not fragmented.
      * @return This builder.
      */
    public org.bdgenomics.formats.avro.NucleotideContigFragment.Builder clearFragmentNumber() {
      fragmentNumber = null;
      fieldSetFlags()[3] = false;
      return this;
    }

    /**
      * Gets the value of the 'fragmentStartPosition' field.
      * The position of the first base of this fragment in the overall contig. E.g.,
   if all fragments are 10kbp and this is the third fragment in the contig,
   the start position would be 20000L.
      * @return The value.
      */
    public java.lang.Long getFragmentStartPosition() {
      return fragmentStartPosition;
    }

    /**
      * Sets the value of the 'fragmentStartPosition' field.
      * The position of the first base of this fragment in the overall contig. E.g.,
   if all fragments are 10kbp and this is the third fragment in the contig,
   the start position would be 20000L.
      * @param value The value of 'fragmentStartPosition'.
      * @return This builder.
      */
    public org.bdgenomics.formats.avro.NucleotideContigFragment.Builder setFragmentStartPosition(java.lang.Long value) {
      validate(fields()[4], value);
      this.fragmentStartPosition = value;
      fieldSetFlags()[4] = true;
      return this; 
    }

    /**
      * Checks whether the 'fragmentStartPosition' field has been set.
      * The position of the first base of this fragment in the overall contig. E.g.,
   if all fragments are 10kbp and this is the third fragment in the contig,
   the start position would be 20000L.
      * @return True if the 'fragmentStartPosition' field has been set, false otherwise.
      */
    public boolean hasFragmentStartPosition() {
      return fieldSetFlags()[4];
    }


    /**
      * Clears the value of the 'fragmentStartPosition' field.
      * The position of the first base of this fragment in the overall contig. E.g.,
   if all fragments are 10kbp and this is the third fragment in the contig,
   the start position would be 20000L.
      * @return This builder.
      */
    public org.bdgenomics.formats.avro.NucleotideContigFragment.Builder clearFragmentStartPosition() {
      fragmentStartPosition = null;
      fieldSetFlags()[4] = false;
      return this;
    }

    /**
      * Gets the value of the 'fragmentLength' field.
      * The length of this fragment.
      * @return The value.
      */
    public java.lang.Long getFragmentLength() {
      return fragmentLength;
    }

    /**
      * Sets the value of the 'fragmentLength' field.
      * The length of this fragment.
      * @param value The value of 'fragmentLength'.
      * @return This builder.
      */
    public org.bdgenomics.formats.avro.NucleotideContigFragment.Builder setFragmentLength(java.lang.Long value) {
      validate(fields()[5], value);
      this.fragmentLength = value;
      fieldSetFlags()[5] = true;
      return this; 
    }

    /**
      * Checks whether the 'fragmentLength' field has been set.
      * The length of this fragment.
      * @return True if the 'fragmentLength' field has been set, false otherwise.
      */
    public boolean hasFragmentLength() {
      return fieldSetFlags()[5];
    }


    /**
      * Clears the value of the 'fragmentLength' field.
      * The length of this fragment.
      * @return This builder.
      */
    public org.bdgenomics.formats.avro.NucleotideContigFragment.Builder clearFragmentLength() {
      fragmentLength = null;
      fieldSetFlags()[5] = false;
      return this;
    }

    /**
      * Gets the value of the 'numberOfFragmentsInContig' field.
      * The total count of fragments that this contig has been broken into. Can be
   null if the contig is not fragmented.
      * @return The value.
      */
    public java.lang.Integer getNumberOfFragmentsInContig() {
      return numberOfFragmentsInContig;
    }

    /**
      * Sets the value of the 'numberOfFragmentsInContig' field.
      * The total count of fragments that this contig has been broken into. Can be
   null if the contig is not fragmented.
      * @param value The value of 'numberOfFragmentsInContig'.
      * @return This builder.
      */
    public org.bdgenomics.formats.avro.NucleotideContigFragment.Builder setNumberOfFragmentsInContig(java.lang.Integer value) {
      validate(fields()[6], value);
      this.numberOfFragmentsInContig = value;
      fieldSetFlags()[6] = true;
      return this; 
    }

    /**
      * Checks whether the 'numberOfFragmentsInContig' field has been set.
      * The total count of fragments that this contig has been broken into. Can be
   null if the contig is not fragmented.
      * @return True if the 'numberOfFragmentsInContig' field has been set, false otherwise.
      */
    public boolean hasNumberOfFragmentsInContig() {
      return fieldSetFlags()[6];
    }


    /**
      * Clears the value of the 'numberOfFragmentsInContig' field.
      * The total count of fragments that this contig has been broken into. Can be
   null if the contig is not fragmented.
      * @return This builder.
      */
    public org.bdgenomics.formats.avro.NucleotideContigFragment.Builder clearNumberOfFragmentsInContig() {
      numberOfFragmentsInContig = null;
      fieldSetFlags()[6] = false;
      return this;
    }

    @Override
    public NucleotideContigFragment build() {
      try {
        NucleotideContigFragment record = new NucleotideContigFragment();
        if (contigBuilder != null) {
          record.contig = this.contigBuilder.build();
        } else {
          record.contig = fieldSetFlags()[0] ? this.contig : (org.bdgenomics.formats.avro.Contig) defaultValue(fields()[0]);
        }
        record.description = fieldSetFlags()[1] ? this.description : (java.lang.CharSequence) defaultValue(fields()[1]);
        record.fragmentSequence = fieldSetFlags()[2] ? this.fragmentSequence : (java.lang.CharSequence) defaultValue(fields()[2]);
        record.fragmentNumber = fieldSetFlags()[3] ? this.fragmentNumber : (java.lang.Integer) defaultValue(fields()[3]);
        record.fragmentStartPosition = fieldSetFlags()[4] ? this.fragmentStartPosition : (java.lang.Long) defaultValue(fields()[4]);
        record.fragmentLength = fieldSetFlags()[5] ? this.fragmentLength : (java.lang.Long) defaultValue(fields()[5]);
        record.numberOfFragmentsInContig = fieldSetFlags()[6] ? this.numberOfFragmentsInContig : (java.lang.Integer) defaultValue(fields()[6]);
        return record;
      } catch (Exception e) {
        throw new org.apache.avro.AvroRuntimeException(e);
      }
    }
  }

  private static final org.apache.avro.io.DatumWriter
    WRITER$ = new org.apache.avro.specific.SpecificDatumWriter(SCHEMA$);  

  @Override public void writeExternal(java.io.ObjectOutput out)
    throws java.io.IOException {
    WRITER$.write(this, org.apache.avro.specific.SpecificData.getEncoder(out));
  }

  private static final org.apache.avro.io.DatumReader
    READER$ = new org.apache.avro.specific.SpecificDatumReader(SCHEMA$);  

  @Override public void readExternal(java.io.ObjectInput in)
    throws java.io.IOException {
    READER$.read(this, org.apache.avro.specific.SpecificData.getDecoder(in));
  }

}