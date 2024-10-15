package utils;//package utils.javautils;
//
//import org.testng.ITestResult;
//import org.testng.annotations.*;
//import utils.baseutils.BrowserManager;
//
//import java.lang.reflect.Method;
//
//public class BaseTest extends BrowserManager
//{
//
//    @BeforeClass(alwaysRun = true)
//    public void setUpClass() {
////        Reporter.setupReport(this.getClass().getSimpleName());
//    }
//
//    @BeforeMethod(alwaysRun = true)
//    @Parameters("localOrRemote")
//    public void setUpLog(Method method, @Optional("local") String localOrRemote) throws Exception {
//        LoggerUtil.setLogFileName(method.getName());
////        Reporter.setupReport(method.getName());
//        Reporter.createTest(method.getName());
//        browserRun();
//    }
//
//    @AfterMethod(alwaysRun = true)
//    public void tearDown(ITestResult result) throws Exception {
//        logResultStatus(result);
////        Reporter.reportonTestResult(result, driver);
//        waitForUi(2);
////        quitBrowser();
//    }
//
//    private void logResultStatus(ITestResult result) throws Exception {
//        switch (result.getStatus()) {
//            case ITestResult.SUCCESS:
//                Reporter.logPass("**** " + result.getName() + " has PASSED ****");
//                break;
//            case ITestResult.FAILURE:
//                fail("**** " + result.getName() + " has FAILED ****");
//                Reporter.logFail("**** " + result.getName() + " has FAILED ****", driver);
//                break;
//            case ITestResult.SKIP:
//                logStep("**** " + result.getName() + " has been SKIPPED ****");
//                Reporter.logStep("**** " + result.getName() + " has been SKIPPED ****");
//                break;
//        }
//    }
//
//    @AfterClass(alwaysRun = true)
//    public void tearDownClass() {
//        Reporter.flushReport();
//    }
//
//
//}
