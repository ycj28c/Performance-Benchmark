package benchmark.dao;

import java.io.IOException;
import java.util.Properties;

import org.springframework.jdbc.core.JdbcTemplate;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

/* use sprint JdbcTemplate instead, this class just for test */
public class DBConnectionExample {
	
	private final String env;
	
	public DBConnectionExample(String env){
		this.env = env;
	}
	/**
	 * get the jdbcTemplate for test
	 * @param env : qa/stage/prod/demo
	 * @throws IOException
	 */
	public JdbcTemplate getJdbcTemplate(){
		Properties prod = new Properties();
		try {
			prod.load(getClass().getClassLoader().getResourceAsStream("env-"+env+".properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		String product = prod.getProperty("product");
		HikariConfig config = new HikariConfig();
		config.setMaximumPoolSize(( prod.getProperty(product + ".maxPoolSize") == null ) ? 
				100 : Integer.parseInt(prod.getProperty(product + ".maxPoolSize")));
		config.setDriverClassName(prod.getProperty(product + ".driverClassName"));
		config.setJdbcUrl(prod.getProperty(product + ".url"));
		config.addDataSourceProperty("user", prod.getProperty(product + ".username"));
		config.addDataSourceProperty("password", prod.getProperty(product + ".password"));
		HikariDataSource ds = new HikariDataSource(config);
		return new JdbcTemplate(ds);
	}
}
