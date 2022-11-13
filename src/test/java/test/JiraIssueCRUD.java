package test;

import api_flow.IssueFlow;
import io.restassured.specification.RequestSpecification;
import model.RequestCapability;
import utils.AuthenticationHandler;

import static io.restassured.RestAssured.given;

public class JiraIssueCRUD implements RequestCapability {

    public static void main(String[] args) {

        // Api info
        String baseUri = "https://sdetpro-tutorial.atlassian.net";
        String projectKey = "SDETPRO1";
        String email = System.getenv("email");
        String apiToken = System.getenv("token");
        String encodedCredStr = AuthenticationHandler.encodedCredStr(email, apiToken);

        // Request Object
        RequestSpecification request = given();
        request.baseUri(baseUri);
        request.header(defautHeader);
        request.header(acceptJSONHeader);
        request.header(getAuthenticatedHeader.apply(encodedCredStr));

        // CRUD
        IssueFlow issueFlow = new IssueFlow(baseUri, projectKey, request, "task");

        System.out.println("------>CREATE");
        issueFlow.createIssue();

        System.out.println("------>READ");
        issueFlow.verifyIssueDetails();

        System.out.println("------>UPDATE");
        issueFlow.updateIssue("Done");
        issueFlow.verifyIssueDetails();

        System.out.println("------>DELETE");
        issueFlow.deleteIssue();
    }
}
