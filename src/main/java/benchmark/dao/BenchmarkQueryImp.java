package benchmark.dao;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

public class BenchmarkQueryImp implements BenchmarkQueryDao{
	
	private JdbcTemplate jdbcTemplate;
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	public BenchmarkQueryImp(JdbcTemplate template) {
		jdbcTemplate = template;
	}
	
	@Override
	public void benchmarkQuery() {
		log.debug("** benchmarkQuery: a benchmark test simple query");
    	String query = " select count(*) from ( "
    			+ " select div_date, div, market_data.price_adj(sd.cusip, div_date, 'match_on_or_before') "
    			+ " div_price, d.*, dt.dividendtypename, dst.supplementaltypename  "
    			+ " from stock_div sd "
    			+ " join ciqdividend d on sd.div_date=d.exdate and sd.ciq_tradingitemid=d.tradingitemid "
    			+ " join ciqdividendtype dt on dt.dividendtypeid=d.dividendtypeid "
    			+ " join ciqdividendsupplementaltype dst on dst.supplementaltypeid=d.supplementaltypeid)"; 
    			
    	List<Map<String, Object>> results = jdbcTemplate.queryForList(
    			query,
    			new Object[] {}
    	);
    	if(results == null||results.isEmpty()){
    		throw new IllegalArgumentException("** benchmarkQuery: no result got from query.\n\n"+query+"\n"); 
    	}
    	log.debug("** benchmarkQuery: done");
	}

}
