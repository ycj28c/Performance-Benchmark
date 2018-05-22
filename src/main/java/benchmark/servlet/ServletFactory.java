package benchmark.servlet;

import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.client.RestTemplate;

public class ServletFactory {
	
	public RestTemplate restTemplate;
	public Environment env;
	public JdbcTemplate jdbcTemplate;
	
	public ServletFactory(RestTemplate restTemplate, Environment env, JdbcTemplate jdbcTemplate){
		this.restTemplate = restTemplate;
		this.env = env;
		this.jdbcTemplate = jdbcTemplate;
	}
	
}
