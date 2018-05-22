package benchmark.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;

import benchmark.enumx.TestCategoryEnum;
import benchmark.pojo.PerfBenchmarkDetAtt;
import benchmark.pojo.PerfBenchmarkDetAttRs;
import benchmark.pojo.PerfBenchmarkDetail;
import benchmark.pojo.PerfBenchmarkSummary;
import benchmark.pojo.TimeMachineUnitPojo;
import benchmark.report.google.timeline.TimeLineModal;
import benchmark.report.single.SingleTestResult;
import benchmark.report.single.SingleTestResultBusiness;
import benchmark.report.single.SingleTestResultNetwork;
import benchmark.report.summary.BusinessSummaryReport;
import benchmark.report.summary.NetworkSummaryReport;
import benchmark.report.summary.SummaryReportFactory;
import benchmark.tools.HibernateUtil;
import benchmark.tools.PropertiesUtil;
import benchmark.tools.StatisticFormulaUtil;

/**
 * persist data into data base
 * quick and ugly
 *
 */
public class BenchmarkPersistImp implements BenchmarkPersistDao{
	
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Override
	public Double getAverageHistoryScore(String methodName, Environment env) {
		Double standardScore = Double.valueOf(env.getRequiredProperty("STANDARD_SCROE").trim());
		Double score = -1D;
		
		/* initial the Hibernate connection */
//		Session session = new HibernateUtil(env).getSession();
		Session session = HibernateUtil.getSessionFactory().openSession();
	
		Query query = session.createQuery("from PerfBenchmarkDetail where test_name "
				+ "= :testName order by perf_benchmark_detail_id desc"); //must use java model class..oh hibernate
		query.setParameter("testName", methodName.trim());
		
		@SuppressWarnings("unchecked")
		List<PerfBenchmarkDetail> list = query.list();
		if(list == null||list.isEmpty()){
			return standardScore;
		}
		int i=0;
		for(;i<list.size();i++){
			score+=list.get(i).getAdjustScore();
		}
		log.debug("getAverageHistoryScore temp:{} i:{} methodName: {}", methodName, score, i, methodName);
		
		/* recycle */
		session.close();
		
		if(i==0){
			return standardScore;
		} else if(score/i>100){
			return 100D;
		} else {
			return score/i;
		}
	}

	@Override
	public Double getAverageHistoryFirstAttScore(String methodName, Environment env) {
		Double standardScore = Double.valueOf(env.getRequiredProperty("STANDARD_SCROE").trim());
		Double score = -1D;
		
		/* initial the Hibernate connection */
//		Session session = new HibernateUtil(env).getSession();
		Session session = HibernateUtil.getSessionFactory().openSession();
		
		Query query = session.createQuery("from PerfBenchmarkDetail where test_name "
				+ "=:testName order by perf_benchmark_detail_id desc"); //must use java model class..oh hibernate
		query.setParameter("testName", methodName.trim());
		@SuppressWarnings("unchecked")
		List<PerfBenchmarkDetail> list = query.list();
		if(list == null||list.isEmpty()){
			return standardScore;
		}
		int i=0;
		for(;i<list.size();i++){
			score+=list.get(i).getFirstAttemptScore();
		}
		log.debug("getAverageHistoryFirstAttScore temp:{} i:{} methodName: {}", methodName, score, i, methodName);
		
		/* recycle */
		session.close();
		
		if(i==0){
			return standardScore;
		} else if(score/i>100){
			return 100D;
		} else {
			return score/i;
		}
	}

	@Override
	public Double getLastAttScroe(String methodName, Environment env) {
		Double standardScore = Double.valueOf(env.getRequiredProperty("STANDARD_SCROE").trim());
		Double score = -1D;
		
		/* initial the Hibernate connection */
//		Session session = new HibernateUtil(env).getSession();
		Session session = HibernateUtil.getSessionFactory().openSession();
		
		Query query = session.createQuery("from PerfBenchmarkDetail where test_name "
				+ "=:testName order by perf_benchmark_detail_id desc"); //must use java model class..oh hibernate
		query.setParameter("testName", methodName.trim());
		@SuppressWarnings("unchecked")
		List<PerfBenchmarkDetail> list = query.list();
		if(list == null||list.isEmpty()){
			return standardScore; //no record, return standard
		} else {
			score = list.get(0).getAdjustScore();
		}
		
		/* recycle */
		session.close();
	
		return score;
	}

	@Override
	public Double getLastFirstAttScore(String methodName, Environment env) {
		Double standardScore = Double.valueOf(env.getRequiredProperty("STANDARD_SCROE").trim());
		Double score = -1D;
		
		/* initial the Hibernate connection */
//		Session session = new HibernateUtil(env).getSession();
		Session session = HibernateUtil.getSessionFactory().openSession();		
		
		Query query = session.createQuery("from PerfBenchmarkDetail where test_name "
				+ "=:testName order by perf_benchmark_detail_id desc"); //must use java model class..oh hibernate
		query.setParameter("testName", methodName.trim());
		@SuppressWarnings("unchecked")
		List<PerfBenchmarkDetail> list = query.list();
		if(list==null||list.isEmpty()){
			return standardScore; //no record, return standard
		} else {
			score=list.get(0).getFirstAttemptScore();
		}
		
		/* recycle */
		session.close();
		
		return score;
		
	}

	@Override
	public List<PerfBenchmarkSummary> getRecentSummaryList(int limit, String env, TestCategoryEnum category) {
		if(limit<=0){
			return null;
		}
		/* initial the Hibernate connection */
//		Session session = new HibernateUtil(env).getSession();
		Session session = HibernateUtil.getSessionFactory().openSession();
		
		Query query = session.createQuery("from PerfBenchmarkSummary where "
				+ "category_name =:categoryName "
				+ "and environment =:environment "
				+ "order by perf_benchmark_summary_id desc");
		query.setParameter("categoryName", category.getValue())
			.setParameter("environment", env)
			.setMaxResults(limit);
		
		@SuppressWarnings("unchecked")
		List<PerfBenchmarkSummary> list = query.list();
		
		//reorder by build # asc
		Collections.sort(list, new Comparator<PerfBenchmarkSummary>(){
            public int compare(PerfBenchmarkSummary arg0, PerfBenchmarkSummary arg1) {
            	if(arg0.getPerfBenchmarkSummaryID()>arg1.getPerfBenchmarkSummaryID()){
            		return 1;
				} else if(arg0.getPerfBenchmarkSummaryID()<arg1.getPerfBenchmarkSummaryID()){
					return -1;
				} else {
					return 0;
				}
            }
        });
		/* recycle */
		session.close();
		
		if(list==null||list.isEmpty()){
			return null;
		} else {
			return list;
		}
	}

	@Override
	public Double getAvgFirstAttScoreBySummaryID(Long summaryID, String env) {
		Double standardScore = Double.valueOf(PropertiesUtil.getPropertyConfig("STANDARD_SCROE").trim());
		//Double standardScore = Double.valueOf(env.getRequiredProperty("STANDARD_SCROE").trim());
		
		/* initial the Hibernate connection */
//		Session session = new HibernateUtil(env).getSession();
		Session session = HibernateUtil.getSessionFactory().openSession();
		Query query = session.createQuery("from PerfBenchmarkDetail where PERF_BENCHMARK_SUMMARY_ID =:summaryID");
		query.setParameter("summaryID", summaryID);
		
		@SuppressWarnings("unchecked")
		List<PerfBenchmarkDetail> list = query.list();
		
		/* recycle */
		session.close();
		
		if(list==null||list.isEmpty()){
			return standardScore;
		} else {
			List<Double> firstAttList = new ArrayList<Double>();
			for(int i=0;i<list.size();i++){
				firstAttList.add(list.get(i).getFirstAttemptScore());
			}
			Double avgFirstAttScore = StatisticFormulaUtil.weightAlgorithm(firstAttList); //weighted algorithm
			return avgFirstAttScore;
		}
	}
	
	@Override
	public void persistAllTestResult(String env) {
		persistBusinessTestResult(env);
		persistNetworkTestResult(env);
	}
	
	private void persistBusinessTestResult(String env){
		if(SummaryReportFactory.getBusinessSummary().getSingleTestList()==null
				||SummaryReportFactory.getBusinessSummary().getSingleTestList().isEmpty()){
			return;
		}
		
		BusinessSummaryReport bsrData = SummaryReportFactory.getBusinessSummary();

		/* initial the Hibernate connection */
//		Session session = new HibernateUtil(env).getSession();
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();
		
		/* persist performance benchmark summary */
		PerfBenchmarkSummary pbs = new PerfBenchmarkSummary();
		//pbs.setPerfBenchmarkSummaryID(5L); 
		pbs.setEnvironment(env);
		pbs.setAdjustScore(bsrData.calculateSummaryScore());
		pbs.setCategoryName(TestCategoryEnum.BUSINESS.getValue());
		pbs.setCreateDate(Calendar.getInstance().getTime());
		
		session.save(pbs);
		SummaryReportFactory.getBusinessSummary().setBuildNumber(String.valueOf(pbs.getPerfBenchmarkSummaryID())); //set builder number
//		SummaryReportFactory.getBusinessSummary().setBuildDate(String.valueOf(pbs.getCreateDate())); //set builder date
		
		/* persist performance benchmark detail */
		for(SingleTestResult temp:bsrData.getSingleTestList()){
			SingleTestResultBusiness str = (SingleTestResultBusiness)temp;
			PerfBenchmarkDetail pbd = new PerfBenchmarkDetail();
			//pbd.setPerfBenchmarkDetailID(perfBenchmarkDetailID);
			pbd.setTestName(str.getName());
			pbd.setPerfBenchmarkSummaryID(pbs.getPerfBenchmarkSummaryID());
			pbd.setDescription(str.getDescription());
			pbd.setPass(str.getPass());
			pbd.setFail(str.getFail());
			pbd.setTotal(str.getTotal());
			pbd.setAdjustScore(str.getAdjustScore());
			pbd.setFirstAttemptScore(str.getTimeLineModalList().get(0).getScore());
			pbd.setStartTime(str.getBeginTime());
			pbd.setEndTime(str.getEndTime());
			pbd.setCreateDate(Calendar.getInstance().getTime());
			session.save(pbd);
			
			/* persist performance benchmark detail attempts */
			for(int i=0;i<str.getTimeLineModalList().size();i++){
				TimeLineModal tlm =  str.getTimeLineModalList().get(i);
				PerfBenchmarkDetAtt pbda = new PerfBenchmarkDetAtt();
				//pbda.setPerfBenchmarkDetAttID(perfBenchmarkDetAttID);
				pbda.setPerfBenchmarkDetailID(pbd.getPerfBenchmarkDetailID());
				pbda.setAttemptOrder(i+1);
				pbda.setScore(tlm.getScore());
				pbda.setStatus(tlm.getStatus());
				pbda.setStartTime(Calendar.getInstance().getTime()); //pending to modify for later use
				pbda.setEndTime(Calendar.getInstance().getTime()); //pending to modify for later use
				pbda.setCreateDate(Calendar.getInstance().getTime());
				session.save(pbda);
				
				/* persist performance benchmark detail attempts result */
				if(tlm.getDuring()!=null&&!tlm.getDuring().isEmpty()){
					for(Entry<String, Double> rsItem: tlm.getDuring().entrySet()){
						PerfBenchmarkDetAttRs pbdar = new PerfBenchmarkDetAttRs();
						//pbdar.setPerfBenchmarkDetAttRsID(perfBenchmarkDetAttRsID);
						pbdar.setPerfBenchmarkDetAttID(pbda.getPerfBenchmarkDetAttID());
						pbdar.setGoal(tlm.getStandardTimeCost());
						pbdar.setResultKey(rsItem.getKey());
						pbdar.setResultValueSecond(rsItem.getValue());
						pbdar.setCreateDate(Calendar.getInstance().getTime());
						session.save(pbdar);
					}
				}
			}
		}
		
		/* commit and close */
		tx.commit();
		session.close();
	}
	
	private void persistNetworkTestResult(String env){
		if(SummaryReportFactory.getNetworkSummary().getSingleTestList()==null
				||SummaryReportFactory.getNetworkSummary().getSingleTestList().isEmpty()){
			return;
		}
		
		NetworkSummaryReport nsrData = SummaryReportFactory.getNetworkSummary();

		/* initial the Hibernate connection */
//		Session session = new HibernateUtil(env).getSession();
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();
		
		/* persist performance benchmark summary */
		PerfBenchmarkSummary pbs = new PerfBenchmarkSummary();
		pbs.setEnvironment(env);
		pbs.setAdjustScore(nsrData.calculateSummaryScore());
		pbs.setCategoryName(TestCategoryEnum.NETWORK.getValue());
		pbs.setCreateDate(Calendar.getInstance().getTime());
		
		session.save(pbs);
		SummaryReportFactory.getNetworkSummary().setBuildNumber(String.valueOf(pbs.getPerfBenchmarkSummaryID())); //set builder number
		
		/* persist performance benchmark detail */
		for(SingleTestResult temp:nsrData.getSingleTestList()){
			SingleTestResultNetwork str = (SingleTestResultNetwork)temp;
			PerfBenchmarkDetail pbd = new PerfBenchmarkDetail();
			//pbd.setPerfBenchmarkDetailID(perfBenchmarkDetailID);
			pbd.setTestName(str.getName());
			pbd.setPerfBenchmarkSummaryID(pbs.getPerfBenchmarkSummaryID());
			pbd.setDescription(str.getDescription());
			pbd.setPass(str.getPass());
			pbd.setFail(str.getFail());
			pbd.setTotal(str.getTotal());
			pbd.setAdjustScore(str.getAdjustScore());
			pbd.setFirstAttemptScore(-1); //no first attemp for network test, set to -1
			pbd.setStartTime(str.getBeginTime());
			pbd.setEndTime(str.getEndTime());
			pbd.setCreateDate(Calendar.getInstance().getTime());
			session.save(pbd);
		}
		
		/* commint and close */
		tx.commit();
		session.close();
	}

	@Override
	public List<PerfBenchmarkDetail> getRecentDetailList(int limit, String testName) {
		if(limit<=0){
			return null;
		}
		Session session = HibernateUtil.getSessionFactory().openSession();
		
		Query query = session.createQuery("from PerfBenchmarkDetail where "
				+ "test_name =:testName "
				+ "order by perf_benchmark_detail_id desc");
		query.setParameter("testName", testName.trim())
			.setMaxResults(limit);
		
		@SuppressWarnings("unchecked")
		List<PerfBenchmarkDetail> list = query.list();
		
		//reorder by build # asc, seems hibernate is not clever enough to order result
		Collections.sort(list, new Comparator<PerfBenchmarkDetail>(){
            public int compare(PerfBenchmarkDetail arg0, PerfBenchmarkDetail arg1) {
            	if(arg0.getPerfBenchmarkDetailID()>arg1.getPerfBenchmarkDetailID()){
            		return 1;
				} else if(arg0.getPerfBenchmarkDetailID()<arg1.getPerfBenchmarkDetailID()){
					return -1;
				} else {
					return 0;
				}
            }
        });
		/* recycle */
		session.close();
		
		if(list==null||list.isEmpty()){
			return null;
		} else {
			return list;
		}
	}

	@Override
	public List<TimeMachineUnitPojo> getTimeMachineDetailBySummaryId(Long summaryId) {
		Session session = HibernateUtil.getSessionFactory().openSession();

		Query query = session.createSQLQuery(
				" select test_name, result_key, median(result_value_second), max(result_value_second) from ( "
				+ " select rs.perf_benchmark_det_att_rs_id, rs.result_key, rs.result_value_second, detail.test_name "
				+ " from perf_benchmark_det_att_rs rs "
				+ " join perf_benchmark_det_att att on rs.perf_benchmark_det_att_id = att.perf_benchmark_det_att_id "
				+ " join perf_benchmark_detail detail on att.perf_benchmark_detail_id = detail.perf_benchmark_detail_id "
				+ " join perf_benchmark_summary summary on detail.perf_benchmark_summary_id = summary.perf_benchmark_summary_id "
				+ " where summary.perf_benchmark_summary_id = ? ) tm group by tm.test_name, tm.result_key");
		query.setLong(0, summaryId);
		
		/* stupid hibernate, remember to convert to object then to your type
		 * http://stackoverflow.com/questions/20486641/ljava-lang-object-cannot-be-cast-to
		 */
		@SuppressWarnings("unchecked")
		List<Object> result = (List<Object>)query.list();
		@SuppressWarnings("rawtypes")
		Iterator itr = result.iterator();
		List<TimeMachineUnitPojo> list = new ArrayList<TimeMachineUnitPojo>();
		while(itr.hasNext()){
		   Object[] obj = (Object[]) itr.next();
		   
		   String test_name = String.valueOf(obj[0]); 
		   String result_key = String.valueOf(obj[1]); 
		   Double median = Double.parseDouble(String.valueOf(obj[2])); 
		   Double max = Double.parseDouble(String.valueOf(obj[3])); 
		   
		   TimeMachineUnitPojo tmup = new TimeMachineUnitPojo(test_name, result_key, median, max);
		   list.add(tmup);
		}
		
		/* recycle */
		session.close();
		
		return list;
	}


}
