package test;

import com.google.gson.Gson;
import io.restassured.http.Header;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import model.PostBody;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsStringIgnoringCase;
import static org.hamcrest.CoreMatchers.equalTo;

public class POSTMethodBk {

    public static void main(String[] args) {

        // RequestSpecification Object
        String baseUri = "https://jsonplaceholder.typicode.com";
        RequestSpecification request = given();
        request.baseUri(baseUri);

        // Content-type -> Header
        request.header(new Header("Content-type", "application/json; charset=UTF-8"));

        // Gson
        Gson gson = new Gson();

        // Bulk Action
        PostBody postBody1 = new PostBody(1, 1, "title 1", "body 1");
        PostBody postBody2 = new PostBody(1, 1, "title 2", "body 2");
        PostBody postBody3 = new PostBody(1, 1, "title 3", "body 3");

        List<PostBody> postBodyList = Arrays.asList(postBody1, postBody2, postBody3);

        for (PostBody postBody : postBodyList) {
            System.out.println(postBody);

            // Send POST request
            Response response = request.body(gson.toJson(postBody)).post("/posts");
            response.prettyPrint();

            // Verification
            response.then().statusCode(equalTo(201));
            response.then().statusLine(containsStringIgnoringCase("201 Created"));
            response.then().body("userId", equalTo(postBody.getUserId()));
            response.then().body("title", equalTo(postBody.getTitle()));
            response.then().body("body", equalTo(postBody.getBody()));
        }
    }
}

