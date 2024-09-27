package cc.remer.finance.config;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

//public class PostgresInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
//
//  @Override
//  public void initialize(ConfigurableApplicationContext applicationContext) {
//
//    var postgreSQLContainer = new PostgreSQLContainer<>(
//        DockerImageName.parse("postgres:17.0"));
//
//    postgreSQLContainer
//        .withEnv("POSTGRES_USER", "test")
//        .withEnv("POSTGRES_PASSWORD", "test")
//        .start();
//
//    TestPropertyValues values = TestPropertyValues.of(
//        "spring.datasource.url", postgreSQLContainer.getJdbcUrl(),
//        "spring.datasource.username", postgreSQLContainer.getUsername(),
//        "spring.datasource.password", postgreSQLContainer.getPassword()
//    );
//    values.applyTo(applicationContext);
//  }
//}
