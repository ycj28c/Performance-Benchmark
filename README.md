Performance Benchmark Project
=====================================

Introduction
------------
This project include framework and report, the purpose is to benchmark the performance of the system, also it persist the history data in the database, allow user to compare with the history running result very easily. Currently is mainly about business benchmark (UI, Selenium), is planning to add api/database/http/javascript/network and other kinds of test. currently program will generate two layer report (single test report and category summary report), in future we will also have super index which display all category benchmark summary.

+ Git address: 

	https://github.com/ycj28c/Performance-Benchmark.git
	
Terms
-----
General:
+ Score: this is the benchmark score for each result, standard is set to 100, the higher the better, the lower the worse

Summary report
+ Current Score: the Average score of each tests in this category
+ TestName: it is also the method name in project
+ Pass: means the benchmark test without exception(such as timeout) or error
+ Fail: the benchmark test has exception
+ Total: total runs, should be the sum of pass and fail

Single test report:
+ Current Test Adjust Score: the average score of each attempts of current test
+ History Average First Attempt Score: the average of "all" the first attempts score in history of current test
+ History Average Score: average adjust score of current test
+ Last Test First Attempt Score: last build first attempt score of current test
+ Last Test Score: last build adjust score of current test

How To Run
----------
make sure you has install MAVEN, and JDK 1.8
	
	mvn -Denv=prod -Dbrowser=firefox clean test

env parameter: 
+ qa: run in qa environment
+ stage: run in stage environment
+ demo: run in demo environment
+ prod: run in prod environment

browser parameter:
+ chrome: run in chrome browser test
+ firefox: run in firefox browser test(not support yet)
+ ie: run in IE browser test(not support yet)

Attention: for Http test, may using the javascript performance API, in that case, can only use chrome

Test Case Configure
-------------------
for each test cases, we can inject the TestCaseParameter parameter by use the dataProvider, for example 

	@Test(dataProvider="test-case-parameter", enabled = true, priority = 1)
	public void p4pDashBoardChartAndTablePerformance(TestCaseParameter tcp){
	
	}

The TestCaseParameter setting locate at /src/test/resources/testcaseconfig.xml, you are able to configure parameter for each test case:

	<testCaseParameters>
	<!-- ProxyProposalResultsTest -->
		<testCaseParameter>
			<fullMethodName>benchmark.business.ProxyProposalResultsTest.ProxyProposalResultsPerformance10Ticker</fullMethodName>
			<simpleMethodName>ProxyProposalResultsPerformance10Ticker</simpleMethodName>
			<retryTimes>1</retryTimes>
			<standardTime>50</standardTime>
			<timeoutInSeconds>500</timeoutInSeconds>
			<sleepInMillis>100</sleepInMillis>
			<description>check the Proxy Proposals and Results work flow performance with 10 ticker</description>
		</testCaseParameter>
		...
	</testCaseParameters>

Then in your test, you can directly call those parameter, such as :

	int sleepInMillis = tcp.getSleepInMillis();
	int retryTimes = tcp.getRetryTimes();
	Double standard = tcp.getStandardTime();
	int timeOutInSeconds = tcp.getTimeoutInSeconds();
	String methodName = tcp.getSimpleMethodName();
	String description = tcp.getDescription();




	
	