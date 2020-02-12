package com.pivotal.api.runner;

import cucumber.api.CucumberOptions;
import cucumber.api.testng.AbstractTestNGCucumberTests;

/**
 * Created by pivotal-test Team.
 */
@CucumberOptions(
        monochrome = true,
        format = {"pretty",
                "html:target/test-report",
                "json:target/test-report.json",
                "junit:target/test-report.xml"},
        features = {"src/test/resources/features"},
        glue = {"com.pivotal.api"})
public class Runner extends AbstractTestNGCucumberTests {

}
