package com.vvkozlov.emerchantpay.merchant.unit.bdd.runners;

import io.cucumber.junit.CucumberOptions;
import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;
import static io.cucumber.core.options.Constants.GLUE_PROPERTY_NAME;

@Suite
@SelectClasspathResource("features")
@ConfigurationParameter(
        key = GLUE_PROPERTY_NAME,
        value = "com.vvkozlov.emerchantpay.merchant.unit.bdd.stepdefinitions"
)
@CucumberOptions(
        plugin = {"pretty", "html:target/cucumber-reports"}
)
public class RunCucumberTest {}
