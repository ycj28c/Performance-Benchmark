package benchmark.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.client.RestTemplate;

public abstract class AbstractServlet{
	protected RestTemplate restTemplate;
	protected Environment env;
	protected JdbcTemplate jdbcTemplate;
	
	protected final Logger log = LoggerFactory.getLogger(this.getClass());
	
	protected AbstractServlet(RestTemplate restTemplate, Environment env, JdbcTemplate jdbcTemplate){
		this.restTemplate = restTemplate;
		this.env = env;
		this.jdbcTemplate = jdbcTemplate;
	}
}
