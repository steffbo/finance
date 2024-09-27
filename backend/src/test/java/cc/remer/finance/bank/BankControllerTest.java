package cc.remer.finance.bank;

import static cc.remer.finance.TestFixture.TEST_BANK_IBAN;
import static cc.remer.finance.TestFixture.TEST_BANK_NAME;
import static cc.remer.finance.TestFixture.bank;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

import cc.remer.finance.config.IntegrationTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class BankControllerTest extends IntegrationTest {

  @Autowired
  private BankRepository bankRepository;

  @BeforeEach
  void setUp() {
    RestAssured.baseURI = "http://localhost:" + port;
    bankRepository.deleteAll();
  }

  @Test
  void missingAuth() {
    given()
        .contentType(ContentType.JSON)
        .when()
        .get("/api/bank")
        .then()
        .statusCode(401);
  }

  @Test
  void wrongPassword() {
    given()
        .contentType(ContentType.JSON)
        .auth().basic("user", "wrong")
        .when()
        .get("/api/bank")
        .then()
        .statusCode(401);
  }

  @Test
  void get() {
    Bank bank = bankRepository.save(bank());

    given()
        .contentType(ContentType.JSON)
        .auth().basic("user", "password")
        .when()
        .get("/api/bank/{id}", bank.getId())
        .then()
        .statusCode(200)
        .body("name", equalTo(TEST_BANK_NAME))
        .body("iban", equalTo(TEST_BANK_IBAN));
  }

  @Test
  void list() {
    bankRepository.save(bank());

    given()
        .contentType(ContentType.JSON)
        .auth().basic("user", "password")
        .when()
        .get("/api/bank")
        .then()
        .statusCode(200)
        .body(".", hasSize(1))
        .body("[0].name", equalTo(TEST_BANK_NAME))
        .body("[0].iban", equalTo(TEST_BANK_IBAN));
  }

  @Test
  void add() {
    given()
        .contentType(ContentType.JSON)
        .auth().basic("user", "password")
        .body(bank())
        .when()
        .post("/api/bank")
        .then().body("name", equalTo(TEST_BANK_NAME))
        .statusCode(200);
  }

  @Test
  void update() {
    Bank bank = bankRepository.save(bank());
    bank.setName("Updated Bank");

    given()
        .contentType(ContentType.JSON)
        .auth().basic("user", "password")
        .body(bank)
        .when()
        .put("/api/bank")
        .then()
        .statusCode(200)
        .body("name", equalTo("Updated Bank"));
  }

  @Test
  void delete() {
    Bank bank = bankRepository.save(bank());

    given()
        .contentType(ContentType.JSON)
        .auth().basic("user", "password")
        .param("id", bank.getId())
        .when()
        .delete("/api/bank")
        .then()
        .statusCode(200);

    assertThat(bankRepository.findAll()).isEmpty();
  }

}
