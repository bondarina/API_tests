package api;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.restassured.http.ContentType;

import java.time.Clock;
import java.util.List;
import static io.restassured.RestAssured.given;
import lombok.Getter;
import lombok.Setter;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import api.UserData;
import org.junit.jupiter.api.Test;
import java.util.*;
import java.util.stream.Collectors;

public class ReqresTest {
private final static String URL = "https://reqres.in/";

    @Test
   // @JsonCreator
  //  @JsonProperty
    public void checkAvatarAndIdTest(){

        Specifications.installSpecification(Specifications.requestSpec(URL),
                Specifications.responseSpecOK200());

        List<UserData> users = given()
        .when()
        .get("api/users?page=2")
        .then().log().all()
        .extract().body().jsonPath().getList("data", UserData.class);

//тест 1.2
/*users.forEach(x-> Assertions.assertTrue(x.getAvatar().contains(x.getId().
        toString())));

//тест 1.3
Assert.assertTrue(users.stream().allMatch(x->x.getEmail().endsWith("@reqres.in")));*/

        //тест 1.3 способ 2
        List<String> avatars = users.stream().map(UserData::getAvatar).collect(Collectors.toList());
        List<String> ids = users.stream().map(x->x.getId().toString()).collect(Collectors.toList());

        for(int i = 0; i<avatars.size(); i++) {
            Assert.assertTrue(avatars.get(i).contains(ids.get(i)));
        }
    }

@Test
    public void successRegTest(){
    Specifications.installSpecification(Specifications.requestSpec(URL),
            Specifications.responseSpecOK200());
    Integer id = 4;
    String token = "QpwL5tke4Pnpja7X4";
    Register user = new Register("eve.holt@reqres.in", "pistol");
    SuccessReg successReg = given()
            .body(user)
            .when()
            .post("api/register")
            .then().log().all()
            .extract().as(SuccessReg.class);

    Assert.assertNotNull(successReg.getId());
    Assert.assertNotNull(successReg.getToken());
            Assert.assertEquals(id, successReg.getId());
            Assert.assertEquals(token, successReg.getToken());
}

@Test
    public void unSuccessregTest() {
        Specifications.installSpecification(Specifications.requestSpec(URL),
                Specifications.responseSpecError400());
    Register user = new Register("sydney@fife", "");
    UnSuccessReg unSuccessReg = given()
            .body(user)
            .post("api/register")
            .then().log().all()
            .extract().as(UnSuccessReg.class);
    Assert.assertEquals("Missing password", unSuccessReg.getError());

}

@Test
public void sortedYearsTest() {
    Specifications.installSpecification(Specifications.requestSpec(URL),
            Specifications.responseSpecOK200());

    List<ColorsData> colors = given()
            .when()
            .get("api/unknown")
            .then().log().all()
            .extract().body().jsonPath().getList("data", ColorsData.class);

    List<Integer> years = colors.stream().map(ColorsData::getYear).collect(Collectors.toList());
    List<Integer> sortedYears = years.stream().sorted().collect(Collectors.toList());
    Assert.assertEquals(sortedYears, years);
    System.out.println(years);
    System.out.println(sortedYears);
}

@Test
    public void deleteUserTest(){
    Specifications.installSpecification(Specifications.requestSpec(URL),
            Specifications.responseSpecUnique(204));
    given()
            .when()
            .delete("api/users/2")
            .then().log().all();
}

@Test
    public void timeTest(){
    Specifications.installSpecification(Specifications.requestSpec(URL),
            Specifications.responseSpecOK200());
    UserTime user = new UserTime("morpheus", "zion resident");
    UserTimeRs response = given()
            .body(user)
            .when()
            .put("api/users/2")
            .then().log().all()
            .extract().as(UserTimeRs.class);

  /* String regex = "(.{5})$";
   String currentTime = Clock.systemUTC().instant().toString().replaceAll(regex,
           "");
    System.out.println(currentTime);
    Assert.assertEquals(currentTime, response.getUpdatedAt().replaceAll(regex,
            ""));
    System.out.println(response.getUpdatedAt().replaceAll(regex, ""));*/
}

}
