package test;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import model.BuildModelJSON;
import model.PostBody;
import model.RequestCapability;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

public class PATCHMethod implements RequestCapability{

    public static void main(String[] args) {

        String baseUri = "https://jsonplaceholder.typicode.com";

        // Form up a request instance, baseUri, header
        RequestSpecification request = given();
        request.baseUri(baseUri);
        request.header(defautHeader);

        // Form up body
        PostBody postBody = new PostBody();
        postBody.setTitle("New Title");
        String postBodyStr = BuildModelJSON.parseJSONString(postBody);
        final String TARGET_POST_ID = "1";
        Response response = request.body(postBodyStr).patch("/posts/".concat(TARGET_POST_ID));
        response.then().body("title", equalTo(postBody.getTitle()));
        response.prettyPrint();
    }
}
