package benchmark.enumx;

/**
 * @author ryang
 *
 */
public enum TestCategoryEnum {
	
	//BUSINESS_SUMMARY("businessSummary"),
	API("api"),
	BUSINESS("business"),
	DATABASE("database"),
	HARDWARE("hardware"),
	HIGHLEVEL("highlevel"),
	HTTP("http"),
	JAVASCRIPT("javascript"),
	NETWORK("network"),
	PRESSURE("pressure");
	
	private final String value;
	
	/**
	 * @param value
	 */
	private TestCategoryEnum(final String value) {
		this.value = value;
	}
	
	public String getValue() { 
		return value; 
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString() {
		return String.valueOf(value);
	}
}
