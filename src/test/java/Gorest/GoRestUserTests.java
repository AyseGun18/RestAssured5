package Gorest;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class GoRestUserTests {

    Faker randomUretici=new Faker();
    int userID;

    RequestSpecification reqSpec;

    @BeforeClass
    public void setup() {

        baseURI = "https://gorest.co.in/public/v2/users";  // baseUri RequestSpecification den önce tanımlanmalı.
        //baseURI ="https://test.gorest.co.in/public/v2/users/";

        reqSpec = new RequestSpecBuilder()
                .addHeader("Authorization", "Bearer e4b22047188da067d3bd95431d94259f63896347f9864894a0a7013ee5f9c703")
                .setContentType(ContentType.JSON)
               // .setBaseUri(baseURI)
                .build();

    }

    @Test(enabled = false)
    public void createUserJson() {
        // POST https://gorest.co.in/public/v2/users
        // "Authorization: Bearer 523891d26e103bab0089022d20f1820be2999a7ad693304f560132559a2a152d"
        // {"name":"{{$randomFullName}}", "gender":"male", "email":"{{$randomEmail}}", "status":"active"}
        String rndFullname = randomUretici.name().fullName();
        String rndEmail = randomUretici.internet().emailAddress();

        userID =
                given()
                        //.header("Authorization", "Bearer 206f7d10800d71dd5a4fd6e847ecd10a69375e6227e2e6f6d9f8d28386c09aeb")
                        .spec(reqSpec)
                      //  .contentType(ContentType.JSON) // gönderilecek data JSON
                        .body("{\"name\":\""+rndFullname+"\", \"gender\":\"male\", \"email\":\""+rndEmail+"\", \"status\":\"active\"}")
                        //.log().uri()
                        //.log().body()

                        .when()
                        .post("")

                        .then()
                        .log().body()
                        .statusCode(201)
                        .contentType(ContentType.JSON)
                        .extract().path("id");
    }

    @Test
    public void createUserMap() {

        System.out.println("baseURI = " + baseURI);
        String rndFullname = randomUretici.name().fullName();
        String rndEmail = randomUretici.internet().emailAddress();

        Map<String,String> newUser=new HashMap<>();
        newUser.put("name",rndFullname);
        newUser.put("gender","male");
        newUser.put("email",rndEmail);
        newUser.put("status","active");

        userID =
                given()
                        .header("Authorization", "Bearer 206f7d10800d71dd5a4fd6e847ecd10a69375e6227e2e6f6d9f8d28386c09aeb")
                        .contentType(ContentType.JSON) // gönderilecek data JSON
                        .body(newUser)
                        .log().uri()
                        //.log().body()

                        .when()
                        .post("")

                        .then()
                        .log().body()
                        .statusCode(201)
                        .contentType(ContentType.JSON)
                        .extract().path("id");
    }

    @Test(enabled = false)
    public void createUserClass() {
        String rndFullname = randomUretici.name().fullName();
        String rndEmail = randomUretici.internet().emailAddress();

        User newUser=new User();
        newUser.name=rndFullname;
        newUser.gender="male";
        newUser.email=rndEmail;
        newUser.status="active";

        userID =
                given()
                        .header("Authorization", "Bearer 206f7d10800d71dd5a4fd6e847ecd10a69375e6227e2e6f6d9f8d28386c09aeb")
                        .contentType(ContentType.JSON) // gönderilecek data JSON
                        .body(newUser)
                        //.log().uri()
                        //.log().body()

                        .when()
                        .post("https://gorest.co.in/public/v2/users")

                        .then()
                        .log().body()
                        .statusCode(201)
                        .contentType(ContentType.JSON)
                        .extract().path("id");
    }

    @Test(dependsOnMethods = "createUserMap")
    public void getUserByID() {

        given()
                .spec(reqSpec)

                .when()
                .get(""+userID)

                .then()
                .log().body()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("id",equalTo(userID))
        ;

    }

    @Test(dependsOnMethods = "getUserByID")
    public void updateUser() {
//        given()
//                .header("Authorization", "Bearer 206f7d10800d71dd5a4fd6e847ecd10a69375e6227e2e6f6d9f8d28386c09aeb")
//
//                .when()
//                .get("https://gorest.co.in/public/v2/users/"+userID)
//
//                .then()
//                .log().body()
//                .statusCode(200)
//                .contentType(ContentType.JSON)

        Map<String, String> updateUser = new HashMap<>();
        updateUser.put("name", "ismet temur");

        given()
                .spec(reqSpec)
                .body(updateUser)
                .log().uri()

                .when()
                .put("" + userID)

                .then()
                .log().body()
                .statusCode(200)
                .body("id", equalTo(userID))
                .body("name", equalTo("ismet temur"))
        ;
    }



    @Test(dependsOnMethods = "updateUser")
    public void deleteUser() {

        given()
                .spec(reqSpec)
                .when()
                .delete(""+userID)
                .then()
                .log().all()
                .statusCode(204)
                ;

    }

    @Test(dependsOnMethods = "deleteUser")
    public void deleteUserNegative() {

        given()
                .spec(reqSpec)
                .when()
                .delete(""+userID)
                .then()
                .log().all()
                .statusCode(404)
        ;
    }
}
