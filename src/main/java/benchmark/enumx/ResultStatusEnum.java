package benchmark.enumx;

/**
 * the status string for test result display
 * @author ryang
 *
 */
public enum ResultStatusEnum {
	TEST_PASS("PASS"), 
	TEST_FAIL("FAIL"),
	HTTP_ERROR("HTTP ERROR"),
	TEST_EXCEPTION("EXCEPTION"),
	TEST_FAIR("ERROR IN RANGE")
	;

	private final String text;

	/**
	 * @param text
	 */
	private ResultStatusEnum(final String text) {
		this.text = text;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString() {
		return text;
	}
	
	public String getValue() {
		return text;
	}
}
