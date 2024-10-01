package cc.remer.finance.imports;

import cc.remer.finance.bank.Bank;
import cc.remer.finance.bank.BankRepository;
import cc.remer.finance.config.IntegrationTest;
import cc.remer.finance.transaction.TransactionRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;

import static cc.remer.finance.TestFixture.bank;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;

class ImportControllerTest extends IntegrationTest {

  @Autowired
  private BankRepository bankRepository;

  @Autowired
  private TransactionRepository transactionRepository;

  @BeforeEach
  void setUp() {
    bankRepository.deleteAll();
    transactionRepository.deleteAll();
  }

  @Test
  void importFileWithInvalidFile() {
    given()
      .auth().basic("user", "password")
      .contentType(ContentType.MULTIPART)
      .multiPart("file", "invalid", "text/csv")
      .when()
      .post("/api/v1/import")
      .then()
      .statusCode(400);
  }

  @Test
  void importFile() throws IOException {
    Bank bank = bankRepository.save(bank());

    Resource csvFile = new ClassPathResource("dkb-test.csv");

    given()
      .auth().basic("user", "password")
      .contentType(ContentType.MULTIPART)
      .multiPart("file", csvFile.getFile(), "text/csv")
      .when()
      .post("/api/v1/import")
      .then()
      .statusCode(200);
  }

  @Test
  void importFileCreatesTransactions() throws IOException {
    Bank bank = bankRepository.save(bank());

    Resource csvFile = new ClassPathResource("dkb-test.csv");

    Response response = given()
      .auth().basic("user", "password")
      .contentType(ContentType.MULTIPART)
      .multiPart("file", csvFile.getFile(), "text/csv")
      .when()
      .post("/api/v1/import")
      .then()
      .statusCode(200)
      .extract().response();

    assertThat(response.getBody().asString()).isEqualTo("6");
    assertThat(transactionRepository.count()).isEqualTo(6);

  }

}