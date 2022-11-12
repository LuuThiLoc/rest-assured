package test;

import com.google.gson.Gson;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import model.IssueFields;
import model.RequestCapability;
import utils.AuthenticationHandler;
import utils.ProjectInfo;

import static io.restassured.RestAssured.given;

public class JiraNewIssue implements RequestCapability {

    public static void main(String[] args) {

        // Api info
        String baseUri = "https://sdetpro-tutorial.atlassian.net";
        String projectKey = "SDETPRO";
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
//        String fieldsStr = "{\n" +
//                "  \"fields\": {\n" +
//                "    \"summary\": \"Summary | From Jira API\",\n" +
//                "    \"project\": {\n" +
//                "      \"key\": \"SDETPRO\"\n" +
//                "    },\n" +
//                "    \"issuetype\": {\n" +
//                "      \"id\": \"10001\"\n" +
//                "    }\n" +
//                "  }\n" +
//                "}";

        String summary = "This is my summary";
        ProjectInfo projectInfo = new ProjectInfo(baseUri, projectKey);
        String taskTypeId = projectInfo.getIssueTypeId("task");
        IssueFields.IssueType issueType = new IssueFields.IssueType(taskTypeId);
        IssueFields.Project project = new IssueFields.Project(projectKey);
        IssueFields.Fields fields = new IssueFields.Fields(summary, project, issueType);
        IssueFields issueFields = new IssueFields(fields);

        // Send request
        Response response = request.body(new Gson().toJson(issueFields)).post(path);
        response.prettyPrint();
    }
}
