package benchmark.tools;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * the algorithm to calculation score in statistic
 * @author ryang
 *
 */
public class StatisticFormulaUtil {
	
	/**
	 * the average calculation formula
	 * @param singleTestList
	 * @return
	 */
	public static Double avgAlgorithm(List<Double> list){
		Double sum = 0D;
		int cnt = 0;
		for(;cnt<list.size();cnt++){
			sum += list.get(cnt);
		}
		Double avgFirstAttScore = BigDecimalUtil.roundDecimalDouble(sum/cnt, 1);
		return avgFirstAttScore;
	}
	
	/**
	 * the weight calculation formula
	 * @param singleTestList
	 * @return
	 */
	public static Double weightAlgorithm(List<Double> list) {
		double weightScore = 0D;
		double weightCnt = 0D;
		for (int i = 0; i < list.size(); i++) {
			double score = list.get(i);
			if(score>=90 && score<=100){
				weightScore += score * 1;
				weightCnt += 1D;
			} else if(score>=80 && score<90){
				weightScore += score * 2;
				weightCnt += 2D;
			} else if(score>=70 && score<80){
				weightScore += score * 3;
				weightCnt += 3D;
			} else if(score>=60 && score<70){
				weightScore += score * 4;
				weightCnt += 4D;
			} else if(score>=50 && score<60){
				weightScore += score * 5;
				weightCnt += 5D;
			} else if(score>=40 && score<50){
				weightScore += score * 6;
				weightCnt += 6D;
			} else if(score>=30 && score<40){
				weightScore += score * 7;
				weightCnt += 7D;
			} else if(score>=20 && score<30){
				weightScore += score * 8;
				weightCnt += 8D;
			} else if(score>=10 && score<20){
				weightScore += score * 9;
				weightCnt += 9D;
			} else if(score>=0 && score<10){
				weightScore += score * 10;
				weightCnt += 10D;
			} else {
				weightScore += score * 1;
				weightCnt += 1D;
			}
		}
		Double score = new BigDecimal(String.valueOf(weightScore)).divide(
				new BigDecimal(String.valueOf(weightCnt)), 1,
				RoundingMode.HALF_UP).doubleValue(); //keep 1 decimal
		return score;
	}
}
