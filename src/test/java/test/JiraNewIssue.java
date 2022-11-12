package test;

import builder.IssueContentBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import model.RequestCapability;
import utils.AuthenticationHandler;
import utils.ProjectInfo;

import static io.restassured.RestAssured.given;

public class JiraNewIssue implements RequestCapability {

    public static void main(String[] args) {

        // Api info
        String baseUri = "https://sdetpro-tutorial.atlassian.net";
        String projectKey = "SDETPRO1";
        String email = System.getenv("email");
        String apiToken = System.getenv("token");
        String encodedCredStr = AuthenticationHandler.encodedCredStr(email, apiToken);
        String path = "/rest/api/3/issue";

        // Request Object
        RequestSpecification request = given();
        request.baseUri(baseUri);
        request.header(defautHeader);
        request.header(acceptJSONHeader);
        request.header(getAuthenticatedHeader.apply(encodedCredStr));

        // Define body data
        String summary = "Summary | IssueContentBuilder";
        ProjectInfo projectInfo = new ProjectInfo(baseUri, projectKey);
        String taskTypeId = projectInfo.getIssueTypeId("task");
        String issueFieldsContent = IssueContentBuilder.build(projectKey, taskTypeId, summary);

        // Send request
        Response response = request.body(issueFieldsContent).post(path);
        response.prettyPrint();
    }
}
