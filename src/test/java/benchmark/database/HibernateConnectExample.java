package benchmark.database;

import java.util.Calendar;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.testng.annotations.Test;

import benchmark.config.AbstractTest;
import benchmark.pojo.PerfBenchmarkDetAtt;
import benchmark.pojo.PerfBenchmarkDetAttRs;
import benchmark.pojo.PerfBenchmarkDetail;
import benchmark.pojo.PerfBenchmarkSummary;

/** 
 * the test to test the connect to performance benchmark table with hibernate technology, ugly ugly and ugly,
 * just a test, don't be mad
 * 
 * The code below work hibernate 4
 * 
 */
public class HibernateConnectExample extends AbstractTest{
	
	@Test(enabled = false)
	public void PerfBenchmarkSummaryTest(){		
		System.out.println("Object saved start.....!!");
		Configuration cfg = new Configuration();
		cfg.configure("hibernate.cfg.xml"); 
 
		SessionFactory factory = cfg.buildSessionFactory();
		Session session = factory.openSession();
		PerfBenchmarkSummary p=new PerfBenchmarkSummary();
		p.setPerfBenchmarkSummaryID(3L);
		p.setCategoryName("aaaaaa");
		p.setAdjustScore(22L);
		p.setEnvironment("qa");
		p.setCreateDate(Calendar.getInstance().getTime());
		
		Transaction tx = session.beginTransaction();
		session.save(p);
		System.out.println("Object saved successfully.....!!");
		tx.commit();
		session.close();
		factory.close();
	}
	
	@Test(enabled = false)
	public void PerfBenchmarkDetailTest(){		
		System.out.println("Object saved start.....!!");
		Configuration cfg = new Configuration();
		cfg.configure("hibernate.cfg.xml"); 
 
		SessionFactory factory = cfg.buildSessionFactory();
		Session session = factory.openSession();

		PerfBenchmarkDetail p=new PerfBenchmarkDetail();
		p.setPerfBenchmarkDetailID(2L);
		p.setTestName("aaaaa");
		p.setPerfBenchmarkSummaryID(1L);
		p.setAdjustScore(333D);
		p.setFirstAttemptScore(354.44D);
		p.setPass(1);
		p.setFail(2);
		p.setTotal(3);
		p.setDescription("dsfgafgadfga");
		p.setStartTime(Calendar.getInstance().getTime());
		p.setEndTime(Calendar.getInstance().getTime());
		p.setCreateDate(Calendar.getInstance().getTime());
 
		Transaction tx = session.beginTransaction();
		session.save(p);
		System.out.println("Object saved successfully.....!!");
		tx.commit();
		session.close();
		factory.close();
	}
	
	@Test(enabled = false)
	public void PerfBenchmarkDetAttTest(){		
		System.out.println("Object saved start.....!!");
		Configuration cfg = new Configuration();
		cfg.configure("hibernate.cfg.xml"); 
 
		SessionFactory factory = cfg.buildSessionFactory();
		Session session = factory.openSession();

		PerfBenchmarkDetAtt p = new PerfBenchmarkDetAtt();
		p.setPerfBenchmarkDetAttID(1);
		p.setAttemptOrder(1);
		p.setPerfBenchmarkDetailID(1);
		p.setScore(43545D);
		p.setCreateDate(Calendar.getInstance().getTime());
 
		Transaction tx = session.beginTransaction();
		session.save(p);
		System.out.println("Object saved successfully.....!!");
		tx.commit();
		session.close();
		factory.close();
	}
	
	@Test(enabled = false)
	public void PerfBenchmarkDetAttRsTest(){		
		System.out.println("Object saved start.....!!");
		Configuration cfg = new Configuration();
		cfg.configure("hibernate.cfg.xml"); 
 
		SessionFactory factory = cfg.buildSessionFactory();
		Session session = factory.openSession();

		PerfBenchmarkDetAttRs p = new PerfBenchmarkDetAttRs();
		p.setPerfBenchmarkDetAttRsID(2);
		p.setPerfBenchmarkDetAttID(1);
		p.setResultKey("dfgdfgdfg");
		p.setResultValueSecond(44D);
		p.setGoal(234D);
		p.setCreateDate(Calendar.getInstance().getTime());
 
		Transaction tx = session.beginTransaction();
		session.save(p);
		System.out.println("Object saved successfully.....!!");
		tx.commit();
		session.close();
		factory.close();
	}

}
