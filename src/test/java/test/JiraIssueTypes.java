package test;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import model.RequestCapability;
import org.apache.commons.codec.binary.Base64;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class JiraIssueTypes implements RequestCapability {

    public static void main(String[] args) {
        String baseUri = "https://sdetpro-tutorial.atlassian.net";
        String path = "/rest/api/3/project/SDETPRO";

        String email = System.getenv("lhtk7pc@gmail.com");
        String apiToken = System.getenv("yFNowU36gYyigvch9hn60B66");
        String cred = email + ":" + apiToken;
        byte[] encodedCred = Base64.encodeBase64(cred.getBytes());
        String encodedCredStr = new String(encodedCred);

        RequestSpecification request = given();
        request.baseUri(baseUri);
        request.header(defautHeader);
//      request.header(RequestCapability.getAuthenticatedHeader(encodedCredStr));
        request.header(getAuthenticatedHeader.apply(encodedCredStr));

        Response response = request.get(path);
//        response.prettyPrint();

        // Verification "issueTypes"
        // Kieu du lieu tra ve response ở dạng {} là Map
        // String: issueTypes"
        // Object: List của ~ cái Map [{},{},{}]
        // Map<String, Object> projectInfo = JsonPath.from(response.asString()).get();  //  <T> T get() --> <T>: Map<String, Object>
        Map<String, List<Map<String, String>>> projectInfo = JsonPath.from(response.asString()).get();

        // Object
        List<Map<String, String>> issueTypes = projectInfo.get("issueTypes");  // get value of Key "issueTypes" = object
        for (Map<String, String> issueType : issueTypes) {
            System.out.println(issueType.get("id"));
            System.out.println(issueType.get("name"));
            System.out.println("---------------");
        }
    }
}
