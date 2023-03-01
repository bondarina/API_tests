package api;

import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import io.restassured.path.json.JsonPath;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class RegresNPjTest {
private final static String URL = "https://reqres.in/";

@Test
    public void checkAvatarsNPjTest(){
    Specifications.installSpecification(Specifications.requestSpec(URL),
            Specifications.responseSpecOK200());

    Response response = given()
            .when()
            .get("api/users?page=2")
            .then().log().all()
            .body("page", equalTo(2))
            .body("data.id", notNullValue())
            .body("data.email", notNullValue())
            .body("data.first_name", notNullValue())
            .body("data.last_name", notNullValue())
            .body("data.avatar", notNullValue())
            .extract().response();

    JsonPath jsonPath = response.jsonPath();
List<String> emails = jsonPath.get("data.email");
List<Integer> ids = jsonPath.get("data.id");
List<String> avatars = jsonPath.get("data.avatar");

for (int i = 0; i < avatars.size(); i++){
    Assertions.assertTrue(avatars.get(i).contains(ids.get(i).toString()));
}

Assertions.assertTrue(emails.stream().allMatch(x->x.endsWith("@reqres.in")));
}

@Test
// пример проверки без Response
    public void successUserRegTestNPj(){
    Specifications.installSpecification(Specifications.requestSpec(URL),
            Specifications.responseSpecOK200());
    Map<String, String> user = new HashMap<>();
    user.put("email", "eve.holt@reqres.in");
    user.put("password", "pistol");
    given()
            .body(user)
            .when()
            .post("api/register")
            .then().log().all()
            .body("id", equalTo(4))
    .body("token", equalTo("QpwL5tke4Pnpja7X4"));
}

@Test
    // пример проверки c Response (как метод выше, только способом с Response)
public void successUserRegTestNPjWithRs(){
    Specifications.installSpecification(Specifications.requestSpec(URL),
            Specifications.responseSpecOK200());
    Map<String, String> user = new HashMap<>();
    user.put("email", "eve.holt@reqres.in");
    user.put("password", "pistol");
    // пошло отличие от метода предыдущего (добавляем использование Response)
    Response response = given()
            .body(user)
            .when()
            .post("api/register")
            .then().log().all()
            // здесь удалили строчки --  .body("id", equalTo(4))
            //            .body("token", equalTo("QpwL5tke4Pnpja7X4")) --, т.к. они лишние (дублируют 2 последние новые строчки с Assert)

            // пошло отличие от метода предыдущего
            .extract().response();
    JsonPath jsonPath = response.jsonPath();
    int id = jsonPath.get("id");
    String token = jsonPath.get("token");
    Assert.assertEquals(4, id);
    Assert.assertEquals("QpwL5tke4Pnpja7X4", token);
}

@Test
    public void unsuccessUserRegNPj(){
    Specifications.installSpecification(Specifications.requestSpec(URL),
            Specifications.responseSpecError400());
    Map<String, String> user = new HashMap<>();
    user.put("email", "sydney@fife");
    given()
            .body(user)
            .when()
            .post("api/register")
            .then().log().all()
            .body("error", equalTo("Missing password"));
}

@Test
    // пример проверки c Response (как метод выше, только способом с Response)
public void unsuccessUserRegNPjWithRs(){
    Specifications.installSpecification(Specifications.requestSpec(URL),
            Specifications.responseSpecError400());
    Map<String, String> user = new HashMap<>();
    user.put("email", "sydney@fife");
   Response response =  given()
            .body(user)
            .when()
            .post("api/register")
            .then().log().all()
           // строчку --- .body("error", equalTo("Missing password")); --- убираем,
    // т.к. с Response она теперь дублирует
           .extract().response();
   JsonPath jsonPath = response.jsonPath();
   String error = jsonPath.get("error");
   Assert.assertEquals("Missing password", error);
}

}
