package benchmark.config;

import java.io.File;
import java.util.Hashtable;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import benchmark.pojo.TestCaseParameter;
import benchmark.pojo.TestCaseParameters;

@Configuration
public class TestParametersConfig {
	
	private static final Logger log = LoggerFactory.getLogger(TestParametersConfig.class);

	@Bean(name = "read-testcase-parameters")
	Hashtable<String, TestCaseParameter> getTestParametersMaps() {
		Hashtable<String, TestCaseParameter> hash = new Hashtable<String, TestCaseParameter>();
		try {
			
			/* read from /test/resources 
			 * https://www.mkyong.com/java/java-read-a-file-from-resources-folder/
			 */
			ClassLoader classLoader = getClass().getClassLoader();
			File xmlFile = new File(classLoader.getResource("testcaseconfig.xml").getFile());
			
			JAXBContext jaxbContext = JAXBContext.newInstance(TestCaseParameters.class);

			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			TestCaseParameters tcps = (TestCaseParameters) jaxbUnmarshaller.unmarshal(xmlFile);
//			TestCaseParameters tcps = (TestCaseParameters) jaxbUnmarshaller.unmarshal(new File("C:/git/performance_benchmark/src/test/resources/testcaseconfig.xml"));
					

			for (TestCaseParameter parameter : tcps.getTestCaseParameter()) {
				hash.put(parameter.getFullMethodName(), parameter);
				log.debug(parameter.toString());
			}
		} catch (JAXBException e) {
			e.printStackTrace();
		}

		return hash;
	}

}
