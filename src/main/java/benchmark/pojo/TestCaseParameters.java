package benchmark.pojo;

import java.io.File;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

/**
 * use jaxb to read xml file
 * https://www.mkyong.com/java/jaxb-hello-world-example/
 * http://theopentutorials.com/tutorials/java/jaxb/jaxb-marshalling-and-unmarshalling-list-of-objects/
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "testCaseParameters")
public class TestCaseParameters {

	@XmlElement(name = "testCaseParameter", type = TestCaseParameter.class)
	private List<TestCaseParameter> testCaseParameter = null;

	public List<TestCaseParameter> getTestCaseParameter() {
		return testCaseParameter;
	}

	public void setTestCaseParameter(List<TestCaseParameter> testCaseParameter) {
		this.testCaseParameter = testCaseParameter;
	}

	public static void main(String[] args) throws JAXBException {
		JAXBContext jaxbContext = JAXBContext.newInstance(TestCaseParameters.class);
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		TestCaseParameters tcp = (TestCaseParameters) jaxbUnmarshaller
				.unmarshal(new File("C:/git/performance_benchmark/src/test/resources/testcaseconfig.xml"));

		for (TestCaseParameter parameter : tcp.getTestCaseParameter()) {
			System.out.println(parameter.toString());
		}

//		System.out.println(tcp.toString());
	}
}
