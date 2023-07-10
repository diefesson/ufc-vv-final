package com.diefesson.flightmanager.test.acceptance;

import org.junit.runner.RunWith;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;;

@RunWith(Cucumber.class)
@CucumberOptions(features = "classpath:features", glue = { "com.diefesson.flightmanager.test.acceptance" })
public class CucumberTest {
}