package utils;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import model.RequestCapability;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class ProjectInfo implements RequestCapability {
    private String baseUri;
    private String projectKey;
    private List<Map<String, String>> issueTypes;
    private Map<String, List<Map<String, String>>> projectInfo;

    public ProjectInfo(String baseUri, String projectKey) {
        this.baseUri = baseUri;
        this.projectKey = projectKey;
        getProjectInfo();
    }

    // TARGET: get issueTypeId by input = issueTypeStr
    public String getIssueTypeId(String issueTypeStr) {
        getIssueTypes();

        String issueTypeId = null;

        for (Map<String, String> issueType : issueTypes) {
            if (issueType.get("name").equalsIgnoreCase(issueTypeStr)) {
                issueTypeId = issueType.get("id");
                break;
            }
        }

        if (issueTypeId == null) {
            throw new RuntimeException("[ERR] Could not find id for " + issueTypeStr);
        }

        return issueTypeId;
    }

    // TODO: getTransitionTypeId(){} // same as: getIssueTypeId()  // transitionType --> Id

    // Support method - private: update value for issueTypes
    private void getIssueTypes() {
        issueTypes = projectInfo.get("issueTypes");
    }

    // Support method - private: update value for projectInfo via constructor
    private void getProjectInfo() {
        String path = "/rest/api/3/project/".concat(projectKey);

//        Set environment variables such as email, apiToken in Configuration | Set directly
        String email = System.getenv("email");
        String apiToken = System.getenv("token");

        RequestSpecification request = given();
        request.baseUri(baseUri);
        request.header(defautHeader);
        request.header(getAuthenticatedHeader.apply(AuthenticationHandler.encodedCredStr(email,apiToken)));

        Response response = request.get(path);
        projectInfo = JsonPath.from(response.asString()).get();
//        response.prettyPrint();  // always print response
    }
}
