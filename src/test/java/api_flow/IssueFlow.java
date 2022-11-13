package api_flow;

import builder.BodyJSONBuilder;
import builder.IssueContentBuilder;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import model.IssueFields;
import model.IssueTransition;
import org.apache.commons.lang3.RandomStringUtils;
import utils.ProjectInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IssueFlow {
    private static final String issuePathPrefix = "/rest/api/3/issue/";
    private String baseUri;
    private String projectKey;
    private RequestSpecification request;
    private Response response;
    private String createdIssueKey;
    private String issueTypeStr;
    private IssueFields issueFields; // Cả summary & status đều nằm trong content của fields -> Expected
    private String status;
    private static Map<String, String> transitionTypeMap = new HashMap<>();

    // Create a Block
    static {
        transitionTypeMap.put("41", "Done");
        transitionTypeMap.put("31", "In Progress");
        transitionTypeMap.put("21", "To Do");
    }

    // Cần đưa vào issueType và request để tạo flow
    public IssueFlow(String baseUri, String projectKey, RequestSpecification request, String issueTypeStr) {
        this.baseUri = baseUri;
        this.projectKey = projectKey;
        this.issueTypeStr = issueTypeStr;
        this.request = request;
        this.status = "To Do";
    }

    public void createIssue() {

        ProjectInfo projectInfo = new ProjectInfo(baseUri, projectKey);
        String taskTypeId = projectInfo.getIssueTypeId("task");
        IssueContentBuilder issueContentBuilder = new IssueContentBuilder();

        int desiredLength = 20;
        boolean hasLetters = true;
        boolean hasNumbers = true;
        String randomSummary = RandomStringUtils.random(desiredLength, hasLetters, hasNumbers);

        // input đầu vào của issueFields gồm (projectKey, taskTypeId, randomSummary)
        String issueFieldsContent = issueContentBuilder.build(projectKey, taskTypeId, randomSummary);
        issueFields = issueContentBuilder.getIssueFields();  // Expected
        this.response = request.body(issueFieldsContent).post(issuePathPrefix);  // Actual

        Map<String, String> responseBody = JsonPath.from(response.asString()).get();
        createdIssueKey = responseBody.get("key");
    }

    public void updateIssue(String issueStatusStr) {

        // handle status target nằm trong Block transitionTypeMap
        String targetTransitionId = null;
        for (String transitionId : transitionTypeMap.keySet()) {
            if (transitionTypeMap.get(transitionId).equalsIgnoreCase(issueStatusStr)) {
                targetTransitionId = transitionId;
                break;
            }
        }

        if (targetTransitionId == null) {
            throw new RuntimeException("[ERR] Issue status string is not supported!");
        }

        // UPDATE CREATED JIRA TASK
        String getIssueTransitions = issuePathPrefix + createdIssueKey + "/transitions";
        request.get(getIssueTransitions);

        IssueTransition.Transition transition = new IssueTransition.Transition(targetTransitionId);
        IssueTransition issueTransition = new IssueTransition(transition);
        String transitionBody = BodyJSONBuilder.getJSONContent(issueTransition);

        request.body(transitionBody).post(getIssueTransitions).then().statusCode(204);

        // Re-use Functional Interface
        Map<String, String> issueInfo = getIssueInfo();
        String actualIssueStatus = issueInfo.get("status");
        String expectedIssueStatus = transitionTypeMap.get(targetTransitionId);
        System.out.println("Actual Issue Status is updated: " + actualIssueStatus);
        System.out.println("Expected Issue Status is updated: " + expectedIssueStatus);
    }

    public void deleteIssue() {
        String path = issuePathPrefix + createdIssueKey;
        request.delete(path);

        // Verify Issue is not existing
        response = request.get(path);
        Map<String, List<String>> notExistingIssueRes = JsonPath.from(response.body().asString()).get();
        List<String> errorMessages = notExistingIssueRes.get("errorMessages");
        System.out.println("Returned msg: " + errorMessages.get(0));
    }

    // Expected & Actual, getIssueInfo
    public void verifyIssueDetails() {
        Map<String, String> issueInfo = getIssueInfo();
        String expectedSummary = issueFields.getFields().getSummary();
        String expectedStatus = status;
        String actualSummary = issueInfo.get("summary");
        String actualStatus = issueInfo.get("status");
        System.out.println("expectedSummary: " + expectedSummary);
        System.out.println("actualSummary: " + actualSummary);
        System.out.println("expectedStatus: " + expectedStatus);
        System.out.println("actualStatus: " + actualStatus);
    }

    private Map<String, String> getIssueInfo() {
        String getIssuePath = issuePathPrefix + createdIssueKey;

        // 2 - READ CREATED JIRA TASK INFO
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
    }
}
