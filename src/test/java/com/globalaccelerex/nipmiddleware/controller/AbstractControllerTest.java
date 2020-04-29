package com.globalaccelerex.nipmiddleware.controller;

import com.globalaccelerex.nipmiddleware.Application;
import com.globalaccelerex.nipmiddleware.config.AppConfig;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

@Slf4j
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@Sql(scripts = "classpath:PopulateDb.sql",  executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@SpringBootTest(classes = Application.class , webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class AbstractControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private AppConfig appConfig;

    @Autowired
    public TestRestTemplate testRestTemplate;

    @Value("${spring.profiles.active:}")
    private String activeProfile;

    protected String createUrlWithPort(String uri){
        final val stringBuilder = new StringBuilder();
        if(StringUtils.equalsIgnoreCase(activeProfile, "test")){
            return stringBuilder.append(appConfig.getBaseUrl())
                    .append(":")
                    .append(port)
                    .append(uri)
                    .toString();
        }else{
            return stringBuilder.append(appConfig.getBaseUrl())
                    .append(uri)
                    .toString();
        }
    }



}
