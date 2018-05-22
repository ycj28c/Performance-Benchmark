package benchmark.enumx;

/**
 * 
 * @author ryang
 *
 */
public enum EvaluationPeriodEnum {
	/*
	 * Evaluation Period :
	 *		1-Year: p4pPeriodCode.p4pPeriodId:1
	 *		3-Year: p4pPeriodCode.p4pPeriodId:2
	 *		5-Year: p4pPeriodCode.p4pPeriodId:3
	 */
	One_Year(1), 
	Three_Year(2),
	Five_Year(3)
	;
	
	private final int value;

	/**
	 * @param value
	 */
	private EvaluationPeriodEnum(final int value) {
		this.value = value;
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
