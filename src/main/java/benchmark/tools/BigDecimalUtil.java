package benchmark.tools;

import java.math.BigDecimal;
import java.math.MathContext;

public class BigDecimalUtil {
	/**
	 * round the double with required decimal digit
	 * @param during
	 * @return
	 */
	public static Double roundDecimalDouble(Double during, int digit){
		return new BigDecimal(during).setScale(digit, BigDecimal.ROUND_UP).doubleValue();
	}
	
	/**
	 * round the double with required context digit
	 * @param during
	 * @return
	 */
	public static Double roundDoubleContext(Double during, int digit){
		return new BigDecimal(during).round(new MathContext(digit)).doubleValue();
	}
	
}
