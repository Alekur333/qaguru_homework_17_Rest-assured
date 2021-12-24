package kur.alex.tests;

import com.github.fge.jsonschema.cfg.ValidationConfiguration;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import org.junit.jupiter.api.Test;

import static com.github.fge.jsonschema.SchemaVersion.DRAFTV4;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.core.Is.is;

public class ApiTests {

    JsonSchemaFactory jsonSchemaFactory = JsonSchemaFactory.newBuilder()
                    .setValidationConfiguration(ValidationConfiguration.newBuilder()
                    .setDefaultVersion(DRAFTV4).freeze()).freeze();

    @Test
    void getListUsersStatusCodeTest() {

        given()
                .contentType(JSON)
                .when()
                .get("https://reqres.in/api/users?page=2")
                .then()
                .statusCode(200);
    }

    @Test
    void getListUsersSchemaValidationTest() {

        given()
                .contentType(JSON)
                .when()
                .get("https://reqres.in/api/users?page=1")
                .then()
                .assertThat()
                .body(matchesJsonSchemaInClasspath("schemas/list-users.json").using(jsonSchemaFactory));
    }

    @Test
    void getListUsersSupportUrlValueTest() {

        given()
                .contentType(JSON)
                .when()
                .get("https://reqres.in/api/users?page=2")
                .then()
                .body("support.url", is("https://reqres.in/#support-heading"));
    }

    @Test
    void getListUsersSupportTextValueTest() {

        given()
                .contentType(JSON)
                .when()
                .get("https://reqres.in/api/users?page=2")
                .then()
                .body("support.text",
                        is("To keep ReqRes free, contributions towards server costs are appreciated!"));
    }


    @Test
    void getListUsersPageEqualsRequestTest() {

        int page = 1;
        String request = "https://reqres.in/api/users?page=" + page;

        given()
                .contentType(JSON)
                .when()
                .get(request)
                .then()
                .body("page", is(page));
    }

}
