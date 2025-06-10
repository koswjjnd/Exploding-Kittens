package explodingkittens.integration;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

import static io.cucumber.junit.platform.engine.Constants.GLUE_PROPERTY_NAME;
import static io.cucumber.junit.platform.engine.Constants.PLUGIN_PROPERTY_NAME;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features/GameSetup.feature")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, 
    value = "explodingkittens.integration")
@ConfigurationParameter(key = PLUGIN_PROPERTY_NAME, 
    value = "pretty, html:target/cucumber-reports/GameSetup.html, "
        + "json:target/cucumber-reports/GameSetup.json")
public class GameSetupIntegrationTest {
    // Test class configuration
} 