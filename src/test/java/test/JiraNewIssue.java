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

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

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

        IssueFields issueFields = issueContentBuilder.getIssueFields();  // Hoặc sử dụng: issueFields.toString();
        System.out.println(issueFields.getFields().getSummary());
        System.out.println(issueFields.getFields().getProject().getKey());
        System.out.println(issueFields.getFields().getIssueType().getId());

        String expectedSummary = issueFields.getFields().getSummary();
        String expectedStatus = "To Do";

        // Check created task details
        Map<String, String> responseBody = JsonPath.from(response.asString()).get();
        final String CREATED_ISSUE_KEY = responseBody.get("key");

        // VERIFY
        // Functional Interface
        Function<String, Map<String, String>> getIssueInfo = issueKey -> {
            String getIssuePath = "/rest/api/3/issue/" + issueKey;

            // READ CREATED JIRA TASK INFO
            Response response_ = request.get(getIssuePath);

            Map<String, Object> fields = JsonPath.from(response_.getBody().asString()).get("fields");
            String actualSummary = fields.get("summary").toString();
            Map<String, Object> status = (Map<String, Object>) fields.get("status");
            Map<String, Object> statusCategory = (Map<String, Object>) status.get("statusCategory");
            String actualStatus = statusCategory.get("name").toString();

            Map<String, String> issueInfo = new HashMap<>();
            issueInfo.put("summary", actualSummary);
            issueInfo.put("status", actualStatus);
            return issueInfo;
        };

        Map<String, String> issueInfo = getIssueInfo.apply(CREATED_ISSUE_KEY);

        System.out.println("expectedSummary: " + expectedSummary);
        System.out.println("actualSummary: " + issueInfo.get("summary"));

        System.out.println("expectedStatus: " + expectedStatus);
        System.out.println("actualStatus: " + issueInfo.get("status"));

        // UPDATE CREATED JIRA TASK
        // 1 - Get all JIRA Id transitions

        // GET /rest/api/3/issue/{issueIdOrKey}/transitions
        String getIssueTransitions = "/rest/api/3/issue/" + CREATED_ISSUE_KEY + "/transitions";
        request.get(getIssueTransitions);

        // Change status To Do -> Done
        final String DONE_STATUS_ID = "41";
        String transitionId = "{\n" +
                "  \"transition\": {\n" +
                "    \"id\": \"41\"\n" +
                "  }\n" +
                "}";

        request.body(transitionId).post(getIssueTransitions).then().statusCode(204);

        // Re-use Functional Interface
        issueInfo = getIssueInfo.apply(CREATED_ISSUE_KEY);
        String latestIssueStatus = issueInfo.get("status");
        System.out.println("latestIssueStatus: "+ latestIssueStatus);
    }
}
