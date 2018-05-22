package benchmark.tools;

import java.io.File;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * http://www.mkyong.com/hibernate/maven-3-hibernate-3-6-oracle-11g-example-xml-mapping/
 * https://www.mkyong.com/hibernate/maven-3-hibernate-3-6-oracle-11g-example-annotation/
 */
public class HibernateUtil {
	
//	private final SessionFactory sessionFactory;
	private Session session;
	private static final SessionFactory sessionFactory = buildSessionFactory();

	private static SessionFactory buildSessionFactory() {
		
		try {
			String directory = "hibernatecfg";
			Configuration cfg = new Configuration();
			cfg.configure(directory + File.separator + "hibernate-" + System.getProperty("env") + ".cfg.xml"); 
			// Create the SessionFactory from hibernate.cfg.xml
//			return new Configuration().configure().buildSessionFactory();
			return cfg.buildSessionFactory();
		} catch (Throwable ex) {
			// Make sure you log the exception, as it might be swallowed
			System.err.println("Initial SessionFactory creation failed." + ex);
			throw new ExceptionInInitializerError(ex);
		}
	}

	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public static void shutdown() {
		// Close caches and connection pools
		getSessionFactory().close();
	}

//	public HibernateUtil(Environment env) {
////		ClassLoader classLoader = getClass().getClassLoader();
////		File xmlFile = new File(classLoader.getResource("testcaseconfig.xml").getFile());
//		String directory = "hibernatecfg";
//		Configuration cfg = new Configuration();
//		cfg.configure(directory + File.separator + "hibernate-" + env.getRequiredProperty("env") + ".cfg.xml"); 
//		
////		Configuration cfg = new Configuration();
////		cfg.configure("hibernate-" + env.getRequiredProperty("env") + ".cfg.xml"); 
//		sessionFactory = cfg.buildSessionFactory();
//	}
	
//	public HibernateUtil(String env) {
//		String directory = "hibernatecfg";
//		Configuration cfg = new Configuration();
//		cfg.configure(directory + File.separator + "hibernate-" + env + ".cfg.xml"); 
////		Configuration cfg = new Configuration();
////		cfg.configure("hibernate-" + env + ".cfg.xml"); 
//		sessionFactory = cfg.buildSessionFactory();
//	}
	
	public Session getSession(){
		session = sessionFactory.openSession();
		return session;
	}
	
	public void closeSession(){
		session.close();
		sessionFactory.close();
	}
	
}
