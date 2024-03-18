package mdt.aas.model;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 *
 * @author Kang-Woo Lee (ETRI)
 */
public enum DataTypeDefXsd {
	XS_ANY_URI("xs:anyURI"),
	XS_BASE64_BINARY("xs:base64Binary"),
	XS_BOOLEAN("xs:boolean"),
	XS_DATE("xs:date"),
	XS_DATE_TIME("xs:dateTime"),
	XS_DATE_TIMESTAMP("xs:dateTimeStamp"),
	XS_DECIMAL("xs:decimal"),
	XS_DOUBLE("xs:double"),
	XS_DURATION("xs:duration"),
	XS_FLOAT("xs:float"),
	XS_DAY("xs:Day"),
	XS_MONTH("xs:Month"),
	XS_MONTH_DAY("xs:MonthDay"),
	XS_YEAR("xs:Year"),
	XS_YEAR_MONTH("xs:YearMonth"),
	XS_HEX_BINARY("xs:hexBinary"),
	XS_STRING("xs:string"),
	XS_TIME("xs:time"),

	XS_INTEGER("xs:integer"),
	XS_LONG("xs:long"),
	XS_INT("xs:int"),
	XS_SHORT("xs:short"),
	XS_BYTE("xs:byte"),
	XS_NONNEGATIVE_INTEGER("xs:nonNegativeInteger"),
	XS_POSITIVE_INTEGER("xs:positiveInteger"),
	XS_UNSIGNED_LONG("xs:unsignedLong"),
	XS_UNSIGNED_INT("xs:unsignedInt"),
	XS_UNSIGNED_SHORT("xs:unsignedShort"),
	XS_UNSIGNED_BYTE("xs:unsignedByte"),
	XS_NONPOSITIVE_INTEGER("xs:nonPositiveInteger"),
	XS_NEGATIVE_INTEGER("xs:negativeInteger"),

	XS_DAY_TIME_DURATION("xs:dayTimeDuration"),
	XS_YEAR_MONTH_DURATION("xs:yearMonthDuration"),
	;
	private String m_name;
	
	DataTypeDefXsd(String name) {
		m_name = name;
	}

	@JsonValue
	public String getName() {
		return m_name;
	}
	
//	@JsonCreator
//	public static DataTypeDefXsd fromJson(@JsonProperty("name") String name) {
//		return FStream.of(DataTypeDefXsd.values())
//						.findFirst(xsd -> xsd.m_name.equals(name))
//						.getOrNull();
//	}
}
