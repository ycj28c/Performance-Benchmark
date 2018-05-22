package benchmark.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.client.RestTemplate;

import benchmark.servlet.ServletFactory;

@Configuration
@PropertySource(value = "classpath:env-${env:qa}.properties")
public class ServletConfig {
	
	@Autowired
	protected Environment env;
	
	@Autowired
	protected JdbcTemplate jdbcTemplate;
	
	@Bean
	RestTemplate getRestTemplate() {
		return new RestTemplate();
	}
	
    @Bean
    public ServletFactory servletFactory() {
        return new ServletFactory(getRestTemplate(), env, jdbcTemplate);
    }
	
}
