package ca.mcgill.ecse.wareflow.features;

import org.junit.runner.RunWith;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(plugin = "pretty", features = "src/test/resources",
    glue = "ca.mcgill.ecse.wareflow.features")
public class CucumberFeaturesTestRunner {

}
