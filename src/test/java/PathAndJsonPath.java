
    import io.restassured.response.Response;
    import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;

    public class PathAndJsonPath {

        @Test
        public void extractingPath(){
            // "post code": "90210",

            int postCode=
                    given()

                            .when()
                            .get("http://api.zippopotam.us/us/90210")

                            .then()
                            .log().body()
                            .extract().path("'post code'");
            ;

            System.out.println("postCode = " + postCode);
        }

        @Test
        public void extractingJosnPath(){
            // "post code": "90210",
            int postCode=
                    given()

                            .when()
                            .get("http://api.zippopotam.us/us/90210")

                            .then()
                            .log().body()
                            .extract().jsonPath().getInt("'post code'")
                    // tip dönüşümü otomatik, uygun tip verilmeli
                    ;

            System.out.println("postCode = " + postCode);
        }

        @Test
        public void getUsers(){

      Response response=
            given()
                    .when()
                    .get("https://gorest.co.in/public/v2/users")
                    .then()
                    .log().body()
                    .extract().response();

            int idPath=response.path("[2].id");
        }
}
