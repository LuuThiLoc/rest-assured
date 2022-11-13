package test;

import builder.IssueContentBuilder;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import model.IssueFields;
import model.RequestCapability;
import org.apache.commons.lang3.RandomStringUtils;
import utils.AuthenticationHandler;
import utils.ProjectInfo;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class JiraNewIssue implements RequestCapability {

    public static void main(String[] args) {

        // Api info
        String baseUri = "https://sdetpro-tutorial.atlassian.net";
        String projectKey = "SDETPRO1";
        String email = System.getenv("email");
        String apiToken = System.getenv("token");
        String encodedCredStr = AuthenticationHandler.encodedCredStr(email, apiToken);
        String path = "/rest/api/3/issue/";

        // Request Object
        RequestSpecification request = given();
        request.baseUri(baseUri);
        request.header(defautHeader);
        request.header(acceptJSONHeader);
        request.header(getAuthenticatedHeader.apply(encodedCredStr));

        // Define body data
        int desiredLength = 20;
        boolean hasLetters = true;
        boolean hasNumbers = true;

        String randomSummary = RandomStringUtils.random(desiredLength, hasLetters, hasNumbers);
        ProjectInfo projectInfo = new ProjectInfo(baseUri, projectKey);
        String taskTypeId = projectInfo.getIssueTypeId("task");
        IssueContentBuilder issueContentBuilder = new IssueContentBuilder();

        // input đầu vào của issueFields gồm (projectKey, taskTypeId, randomSummary)
        String issueFieldsContent = issueContentBuilder.build(projectKey, taskTypeId, randomSummary);

        // CREATE JIRA TASK
        // Send 1 cái request lên để get issueFields này về
        Response response = request.body(issueFieldsContent).post(path);

        // Sau khi send request thì tạo ra trên JIRA: id của IssueType + key của nó
        // Khi verify thì chỉ cần: Summary + Status của IssueType đc tạo ra trên JIRA "To Do"
        // response.prettyPrint();

        // Check created task details
        Map<String, String> responseBody = JsonPath.from(response.asString()).get();
        String getIssuePath = "/rest/api/3/issue/" + responseBody.get("key");

        // READ CREATED JIRA TASK INFO
        response = request.get(getIssuePath);

        // VERIFY
        IssueFields issueFields = issueContentBuilder.getIssueFields();  // Hoặc sử dụng: issueFields.toString();
        System.out.println(issueFields.getFields().getSummary());
        System.out.println(issueFields.getFields().getProject().getKey());
        System.out.println(issueFields.getFields().getIssueType().getId());

        String expectedSummary = issueFields.getFields().getSummary();
        String expectedStatus = "To Do";

        Map<String, Object> fields = JsonPath.from(response.getBody().asString()).get("fields");
        String actualSummary = fields.get("summary").toString();
        Map<String, Object> status = (Map<String, Object>) fields.get("status");
        Map<String, Object> statusCategory = (Map<String, Object>) status.get("statusCategory");
        String actualStatus = statusCategory.get("name").toString();

        System.out.println("expectedSummary: " + expectedSummary);
        System.out.println("actualSummary: " + actualSummary);

        System.out.println("expectedStatus: " + expectedStatus);
        System.out.println("actualStatus: " + actualStatus);
    }
}
