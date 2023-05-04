
import Model.Location;
import Model.Place;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class ZippoTest {

    @Test
    public void test() {
        given()
                // hazirlik islemleri: (token, send body, parametreler)
                .when()
                // endpoint (url), metodu  post mu gidecek delete mi
                .then()
        // assertion, test, data islemleri  , test kismi
        ;
    }

    @Test
    public void statusCodeTest() {
        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .log().body()   // donen body json datasi,  log.all dersen butun bilgileri yazar
                .statusCode(200)  // donus kodu 200 mu
        ;
    }

    @Test
    public void contentTypeTest() {
        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .log().body()
                .statusCode(200)
                .contentType(ContentType.JSON)  // donen sonuc JSON mi
        ;
    }

    @Test
    public void checkCountryInResponseTest() {
        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .log().body()
                .statusCode(200)
                .body("country", equalTo("United States")) // body nin country degiskeni United Sate mi
        // bilgiyi disari almadan kontrol etme sekli
        // pm.response.json().id  ->  body.id
        ;
    }

    @Test
    public void checkStateInResponseTest() {
        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .log().body()
                .statusCode(200)
                .body("places[0].state", equalTo("California"))

        ;
    }

    @Test
    public void checkHasItemy() {
        given()
                .when()
                .get("http://api.zippopotam.us/tr/01000")

                .then()
                .log().body()
                .statusCode(200)
                .body("places.'place name'", hasItem("Dörtağaç Köyü"))  // butun place namelerin herhangi birinde Dörtağaç Köyü var mi

        ;
    }

    @Test
    public void bodyArrayHasSizeTest() {
        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .log().body()
                .statusCode(200)
                .body("places", hasSize(1)) // places arraylistin uzunlugunu dogruluyor

        ;
    }

    @Test
    public void combiningTest() {
        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                // .log().body()
                .statusCode(200)
                .body("places", hasSize(1)) // places arraylistin uzunlugunu dogruluyor
                .body("places.state", hasItem("California")) // verilen pathdeki list bu tem var mi
                .body("places[0].'place name'", equalTo("Beverly Hills"))  // esit mi

        ;
    }

    @Test
    public void pathParamTest() {
        given()
                .pathParam("ulke", "us")
                .pathParam("postaKod", 90210)
                .log().uri()  // request link olusan linki gosteriyor

                .when()
                .get("http://api.zippopotam.us/{ulke}/{postaKod}")

                .then()
                // .log().body()
                .statusCode(200)
        ;
    }
    @Test
    public void queryParamTest() {
      //  https://gorest.co.in/public/v1/users?page=2

        given()
                .param("page",1)  // ?page=1 seklinde linke ekleniyor
                .log().uri()

                .when()
                .get("https://gorest.co.in/public/v1/users")// ?page=1

                .then()
                // .log().body()
                .statusCode(200)
                .log().body()
        ;
    }

    @Test
    public void queryParamTest2() {
        // https://gorest.co.in/public/v1/users?page=3
        // bu linkteki 1 den 10 kadar sayfaları çağırdığınızda response daki donen page degerlerinin
        // çağrılan page nosu ile aynı olup olmadığını kontrol ediniz.

        for (int i = 1; i < 10; i++) {

        given()
                .param("page",i)  // ?page=1 seklinde linke ekleniyor
                .log().uri()

                .when()
                .get("https://gorest.co.in/public/v1/users")// ?page=1

                .then()
                // .log().body()
                .statusCode(200)
                .log().body()
                .body("meta.pagination.page",equalTo(i))
        ;
    }
  }

  RequestSpecification requestSpec;
    ResponseSpecification responseSpec;

  @BeforeClass
  public void Setup(){

      baseURI = "https://gorest.co.in/public/v1";

      requestSpec=new RequestSpecBuilder()
              .log(LogDetail.URI)
              .setContentType(ContentType.JSON)
              .build();
      responseSpec=new ResponseSpecBuilder()
              .expectContentType(ContentType.JSON)
              .expectStatusCode(200)
              .log(LogDetail.BODY)
              .build();
  }

    @Test
    public void requestResponseSpecificationn() {
        //  https://gorest.co.in/public/v1/users?page=2

        given()
                .param("page",1)  // ?page=1 seklinde linke ekleniyor
                .spec(requestSpec) // isteklerin ozellikleri

                .when()
                .get("/users")// baseUri de tanimladik basta http gormezse otomatik ekliyor

                .then()
                .spec(responseSpec) // donusun ozellikleri
        ;
    }
    @Test
    public void extractingJsonPath() {
      String countryName=
        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .log().body()
                .extract().path("country")
              ;
        System.out.println("countryName = " + countryName);
        Assert.assertEquals(countryName,"United States");
    }

    @Test
    public void extractingJsonPath2() {
      // placename
        String placeName=
                given()
                        .when()
                        .get("http://api.zippopotam.us/us/90210")

                        .then()
                        .log().body()
                        .extract().path("places[0].'place name'")
                ;
        System.out.println("placeName = " + placeName);
        Assert.assertEquals(placeName,"Beverly Hills");

    }
    @Test
    public void extractingJsonPath3() {
// https://gorest.co.in/public/v1/users  dönen değerdeki limit bilgisini yazdırınız.
       int limit=
        given()
                .when()
                .get("https://gorest.co.in/public/v1/users")

                .then()
                //.log().body()
                .extract().path("meta.pagination.limit");
        System.out.println("limit = " + limit);
    }
    @Test
    public void extractingJsonPath4() {
// https://gorest.co.in/public/v1/users  dönen değerdeki butun idleri yazdırınız.
        List<Integer> idler =
        given()
                .when()
                .get("https://gorest.co.in/public/v1/users")

                .then()
                .log().body()
                .statusCode(200)
                .extract().path("data.id");
        System.out.println("idler = " + idler);
    }
    @Test
    public void extractingJsonPath5() {
        // https://gorest.co.in/public/v1/users  dönen değerdeki bütün name lei yazdırınız.

        List<String> names=
                given()

                        .when()
                        .get("https://gorest.co.in/public/v1/users")

                        .then()
                        .statusCode(200)
                        .extract().path("data.name"); // bütün id leri ver        ;

        System.out.println("names = " + names);
    }
    @Test
    public void extractingJsonPathResponsAll() {
        // https://gorest.co.in/public/v1/users  dönen değerdeki bütün name lei yazdırınız.

        Response donenData =
                given()

                        .when()
                        .get("https://gorest.co.in/public/v1/users")

                        .then()
                        .statusCode(200)
                        .extract().response();  // donen butun datayi verir

        List<Integer> idler = donenData.path("data.id");
        List<String> names = donenData.path("data.name");
        int limit = donenData.path("meta.pagination.limit");

        System.out.println("idler = " + idler);
        System.out.println("names = " + names);
        System.out.println("limit = " + limit);

        Assert.assertTrue(idler.contains(1203767));
        Assert.assertTrue(names.contains("Dakshayani Pandey"));
        Assert.assertEquals(limit, 10, "test sonucu hatali");
    }

    @Test
    public void extractJsonAll_POJO(){ // POJO = JSON nesnesi = locationNesnesi

      Location locationnesnesi=
      given()

              .when()
              .get("http://api.zippopotam.us/us/90210")

              .then()
              //.log().body()
              .extract().body().as(Location.class)  // location sablonuna
       ;
        System.out.println("locationnesnesi.getCountry() = " + locationnesnesi.getCountry());

        for(Place p : locationnesnesi.getPlaces())
            System.out.println("p = " + p);

        System.out.println("locationnesnesi.getPlaces().get(0).getPlacename() = " + locationnesnesi.getPlaces().get(0).getPlacename());

    }

    @Test
    public void extractJsonAll_POJO_Soru() {

        // aşağıdaki endpointte(link)  Dörtağaç Köyü ait diğer bilgileri yazdırınız

        Location adana=
                given()
                        .when()
                        .get("http://api.zippopotam.us/tr/01000")

                        .then()
                        //.log().body()
                        .statusCode(200)
                        .extract().body().as(Location.class)
                ;
        for (Place p: adana.getPlaces())
            if(p.getPlacename().equalsIgnoreCase("Dörtağaç Köyü")) {
                System.out.println("p = " + p);
            }

    }














    }