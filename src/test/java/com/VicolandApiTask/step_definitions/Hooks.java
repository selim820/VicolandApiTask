package com.VicolandApiTask.step_definitions;

import com.VicolandApiTask.utilities.ConfigurationReader;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.restassured.RestAssured;

public class Hooks {



    @Before
    public void setUp() {
        RestAssured.baseURI = ConfigurationReader.get("GoRestUrl");

    }


    @After
    public void close() {

    }
}
