package benchmark.config;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.jdbc.core.JdbcTemplate;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

/**
 * Selenium requires us to have some system properties set before it'll function right.
 * This class will take a list of properties in the Spring environment and push them into
 * Java system properties.
 */
@Configuration
//@PropertySource(value = "classpath:db-${env}.properties") //will use the same property
public class DatabaseConfig {

	private static final Logger log = LoggerFactory.getLogger(DatabaseConfig.class);

	@Autowired
	ConfigurableEnvironment env;

	@Autowired
	DataSource dataSource;

	/**
	 * Fetch a JDBC template for this database.
	 *
	 * @return the JDBC template to set the data.
	 */
	@Bean
	JdbcTemplate getJdbcTemplate() {
		return new JdbcTemplate(dataSource);
	} 

	@Bean
	DataSource getDataSource() {
		String product = env.getProperty("product");
		if (product == null || product.trim().equals("")) {
			throw new IllegalArgumentException("\n\n Properties file does not specify 'product'!"
					+ "\n Please add DB connection to your properties such as:"
					+ "\n\n ########### database ###########"
					+ "\n product=example"
					+ "\n example.driverClassName=oracle.jdbc.driver.OracleDriver"
					+ "\n example.url=jdbc:oracle:thin:@10.10.10.10:1521:example"
					+ "\n example.username=example"
					+ "\n example.password=example"
					+ "\n ########################################\n");
		}
		HikariConfig config = new HikariConfig();
		config.setMaximumPoolSize(( env.getProperty(product + ".maxPoolSize") == null ) ? 15 : env.getProperty(product + ".maxPoolSize", Integer.class));
		config.setDriverClassName(env.getRequiredProperty(product + ".driverClassName"));
		config.setJdbcUrl(env.getRequiredProperty(product + ".url"));
		config.setMaxLifetime(60000); //1min
		config.addDataSourceProperty("user", env.getRequiredProperty(product + ".username"));
		config.addDataSourceProperty("password", env.getRequiredProperty(product + ".password"));
		log.info("Creating new DataSource with config={}", config);
		HikariDataSource ds = new HikariDataSource(config);
		return ds;
	}

}
