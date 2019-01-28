package de.terrestris.shogun2.integration;

import com.zaxxer.hikari.HikariDataSource;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.GenericContainer;

import java.util.Properties;

import static org.junit.Assert.assertTrue;

/**
 * Integration test
 *
 * @author Andre Henn
 */
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional(value = "transactionManagerTest")
@ContextConfiguration(locations = {"classpath*:META-INF/spring/test-hibernate-config.xml"})
public class SimpleTest {

    @ClassRule
    public static GenericContainer existingSHOGun2 = new GenericContainer<>("shogun-test-image:0.0.1")
        .withExposedPorts(5432);

    // docker run -it --rm -v "$PWD":/usr/src/mymaven -v "$HOME/.m2":/root/.m2 -v "$PWD/target:/usr/src/mymaven/target" -w /usr/src/mymaven maven:3.6.0-jdk-11 mvn install test

    @Autowired
    @Qualifier("testDataSource")
    private static HikariDataSource testDataSource;

    @BeforeClass
    public static void setUp() {
        String address = existingSHOGun2.getContainerIpAddress();
        Integer port = existingSHOGun2.getFirstMappedPort();

        Properties peter = testDataSource.getDataSourceProperties();

        String url = "jdbc:postgresql://"+address+":"+port+"/shogun2webapp?currentSchema=shogun";

        peter.setProperty("url", url);
        testDataSource.setDataSourceProperties(peter);
        existingSHOGun2.start();
    }

    @AfterClass
    public static void onDestroy() {
        existingSHOGun2.stop();
    }

    @Test
    public void test() {
        assertTrue(false);
    }

}
