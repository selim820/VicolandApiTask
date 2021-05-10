package com.VicolandApiTask.step_definitions;

import com.VicolandApiTask.utilities.ConfigurationReader;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;

import static io.restassured.RestAssured.*;

import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.Assert;

import javax.swing.text.AbstractDocument;
import java.util.HashMap;
import java.util.Map;

public class UserFlow  {

    Response response;
    String BearerToken = "Bearer " + ConfigurationReader.get("AccessToken");
    int PostedUserID;


    Map<String, String> postMap;
    Map<String, String> GetResultMap;
    Map<String, String> patchMap;


    @Given("I create new user with given data: name as {string},gender as {string},email as {string},status as {string}")
    public void I_create_new_user_with_given_data_name_as_gender_as_email_as_status_as(String name, String gender, String email, String status) {

        postMap = new HashMap<>();
        postMap.put("name", name);
        postMap.put("gender", gender);
        postMap.put("email", email);
        postMap.put("status", status);

         response=given().accept("application/json")
                .contentType("application/json")
                .and().header("Authorization", BearerToken)
                .and().body(postMap)
                .when().post(ConfigurationReader.get("PostUrl"));

         response.prettyPrint();

        JsonPath jsonPath=response.jsonPath();
        System.out.println("jsonPath = " + jsonPath.getString("data.name"));




        try {
            PostedUserID = jsonPath.getInt("data.id");

        } catch (NumberFormatException n) {
            n.printStackTrace();
            throw new NumberFormatException("Previously created mail. Please create new mail !");
        }




    }


    @Then("Status code should be {int}")
    public void status_should_be(int statusCode) {
        // Write code here that turns the phrase above into concrete actions

        int ActualStatusCode=(int) response.path("code");
        Assert.assertEquals("Status codes doesnt match", statusCode, ActualStatusCode);
    }

    @Then("Content-type should be {string}")
    public void content_type_should_be(String ContentType) {
        Assert.assertTrue(response.contentType().contains(ContentType));

    }

    @Then("Response should match with following inputs: name with {string},gender with {string},email with {string},status with {string}")
    public void response_should_match_with_following_inputs_name_with_gender_with_email_with_status_with(String ExpectedName, String ExpectedGender, String ExpectedMail, String ExpectedStatus) {

        System.out.println(ConfigurationReader.get("GetUrl"));
        System.out.println(PostedUserID);
        response = given().accept(ContentType.JSON)
                .and().pathParam("id", PostedUserID)
                .when().get(ConfigurationReader.get("GetUrl") + "{id}");

        response.prettyPrint();
        GetResultMap = (Map<String, String>) response.path("data");
        String ActualName = GetResultMap.get("name");
        String ActualMail = GetResultMap.get("email");
        String ActualGender = GetResultMap.get("gender");
        String ActualStatus = GetResultMap.get("status");

        Assert.assertEquals("Names do not match", ExpectedName, ActualName);
        Assert.assertEquals("Genders do not match", ExpectedGender, ActualGender);
        Assert.assertEquals("Mails do not match", ExpectedMail, ActualMail);
        Assert.assertEquals("Status do not match", ExpectedStatus, ActualStatus);


    }

    @Then("I rename the user as {string}")
    public void I_rename_the_user_as(String PatchName) {
        // Write code here that turns the phrase above into concrete actions
        Map<String, String> patchMap = new HashMap<>();
        patchMap.put("name", PatchName);
        patchMap.put("email", postMap.get("email"));
        patchMap.put("status", postMap.get("status"));


        response = given().contentType(ContentType.JSON)
                .and().accept(ContentType.JSON)
                .and().header("Authorization", BearerToken)
                .and().pathParam("id", PostedUserID)

                .and().body(patchMap)
                .when().patch(ConfigurationReader.get("GetUrl") + "{id}");
        System.out.println("patch---");
        response.prettyPrint();
    }

    @Then("Response Name should be {string}")
    public void response_Name_should_be(String ExpectedName) {
        // Write code here that turns the phrase above into concrete actions
        given().accept(ContentType.JSON)
                .and().pathParam("id", PostedUserID)
                .when().get(ConfigurationReader.get("GetUrl") + "{id}")
                .then().assertThat().statusCode(200)
                .and().contentType(Matchers.equalTo("application/json; charset=utf-8"))
                .and().body("data.name", Matchers.equalTo(ExpectedName));


    }

    @Then("Response Gender should be {string}")
    public void response_Gender_should_be(String ExpectedGender) {
        // Write code here that turns the phrase above into concrete actions
        given().accept(ContentType.JSON)
                .and().pathParam("id", PostedUserID)
                .when().get(ConfigurationReader.get("GetUrl") + "{id}")
                .then().assertThat().statusCode(200)
                .and().contentType(Matchers.equalTo("application/json; charset=utf-8"))
                .and().body("data.gender", Matchers.equalTo(ExpectedGender));


    }

    @Then("Response Email should be {string}")
    public void response_Email_should_be(String ExpectedMail) {
        // Write code here that turns the phrase above into concrete actions
        given().accept(ContentType.JSON)
                .and().pathParam("id", PostedUserID)
                .when().get(ConfigurationReader.get("GetUrl") + "{id}")
                .then().assertThat().statusCode(200)
                .and().contentType(Matchers.equalTo("application/json; charset=utf-8"))
                .and().body("data.email", Matchers.equalTo(ExpectedMail));


    }

    @Then("Response Status should be {string}")
    public void response_Status_should_be(String ExpectedStatus) {
        // Write code here that turns the phrase above into concrete actions
        given().accept(ContentType.JSON)
                .and().pathParam("id", PostedUserID)
                .when().get(ConfigurationReader.get("GetUrl") + "{id}")
                .then().assertThat().statusCode(200)
                .and().contentType(Matchers.equalTo("application/json; charset=utf-8"))
                .and().body("data.status", Matchers.equalTo(ExpectedStatus));

    }

    @Then("I delete the new user")
    public void I_delete_new_user() {
        // Write code here that turns the phrase above into concrete actions
       response=given().pathParam("id", PostedUserID)
                .and().header("Authorization", BearerToken)
                .when().delete(ConfigurationReader.get("GetUrl") + "{id}");

response.prettyPrint();


    }

    @Then("I create a post comment for current user with following data: title as {string}, body as {string}")
    public void I_create_a_post_comment_for_current_user_with_following_data_title_as_body_as(String title, String body) {
        Map<String, String> commentMap = new HashMap<>();
        commentMap.put("title", title);
        commentMap.put("body", body);

        Response response = given().contentType(ContentType.JSON)
                .and().accept(ContentType.JSON)
                .and().header("Authorization",BearerToken)
                .and().body(commentMap)
                .and().pathParam("id", PostedUserID)
                .when().post(ConfigurationReader.get("GetUrl")+"{id}"+"/posts");

        System.out.println("comment---");
        response.prettyPrint();

    }

    @Then("Comment Response should match with following data: title as {string}, body as {string}")
    public void comment_Response_should_match_with_following_data_title_as_body_as(String ExpectedTitle, String ExpectedBody) {
        given().accept(ContentType.JSON)
                .pathParam("id",PostedUserID)
                .when().get(ConfigurationReader.get("GetUrl")+"{id}"+"/posts")
                .then().assertThat().statusCode(200)
                .and().body("data.title[0]",Matchers.equalTo(ExpectedTitle),
                "data.body[0]",Matchers.equalTo(ExpectedBody));


    }
    @Then("Comment Response id and User id should match")
    public void comment_Response_id_and_User_id_should_match() {

        given().accept(ContentType.JSON)
                .pathParam("id",PostedUserID)
                .when().get(ConfigurationReader.get("GetUrl")+"{id}"+"/posts")
                .then().assertThat().statusCode(200)
                .and().body("data.user_id[0]",Matchers.equalTo(PostedUserID));
    }


    @Then("Deleted data should not exist")
    public void deleted_data_should_not_exist() {
       response=given().accept(ContentType.JSON)
                .pathParam("id",PostedUserID)
                .when().get(ConfigurationReader.get("GetUrl")+"{id}");

        int ExpectedStatusCode=response.path("code");
       Assert.assertEquals(ExpectedStatusCode,404);



    }


}
