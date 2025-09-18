package listeners;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class ExtentReportListener implements ITestListener {

        private ExtentReports extent;
        private ExtentTest test;

        @Override
        public void onStart(ITestContext context) {
            // Se execută o singură dată, la începutul suitei
            ExtentSparkReporter spark = new
                    ExtentSparkReporter("reports/ExecutionReport.html");

            extent = new ExtentReports();
            extent.attachReporter(spark);
        }

        @Override
        public void onTestStart(ITestResult result) {
            // Se execută la începutul fiecărui test @Test
            test = extent.createTest(result.getMethod().getMethodName());
        }

        @Override
        public void onTestSuccess(ITestResult result) {
            test.pass("Testul a trecut cu succes!");
        }

        @Override
        public void onTestFailure(ITestResult result) {
            test.fail("Testul a eșuat!");
            test.fail(result.getThrowable()); // Adaugă eroarea/excepția în raport
        }

        @Override
        public void onTestSkipped(ITestResult result) {
            test.skip("Testul a fost omis.");
        }

        @Override
        public void onFinish(ITestContext context) {
            // Se execută o singură dată, la finalul suitei
            extent.flush(); // Scrie toate datele în fișierul HTML
        }
    }

