package test;

import io.restassured.specification.RequestSpecification;
import model.RequestCapability;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import utils.AuthenticationHandler;

import static io.restassured.RestAssured.given;

public class BaseTest implements RequestCapability {

    protected String baseUri;
    protected String projectKey;
    protected String encodedCredStr;
    protected RequestSpecification request;

    @BeforeSuite
    public void beforeSuite() {
        baseUri = "https://sdetpro-tutorial.atlassian.net";
        projectKey = "SDETPRO1";
        encodedCredStr = AuthenticationHandler.encodedCredStr(email, token);
    }

    @BeforeTest
    public void beforeTest() {
        request = given();
        request.baseUri(baseUri);
        request.header(defautHeader);
        request.header(acceptJSONHeader);
        request.header(getAuthenticatedHeader.apply(encodedCredStr));
    }
}
