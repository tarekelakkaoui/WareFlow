package ca.mcgill.ecse.wareflow.features;

import ca.mcgill.ecse.wareflow.application.WareFlowApplication;
import io.cucumber.java.After;
import io.cucumber.java.Before;

public class CommonStepDefinitions {
  /**
   * Method used to delete the current getWareFlow system instance before the next test. This is
   * effective for all scenerios in all feature files
   */
  @Before
  public void setup() {
    WareFlowApplication.getWareFlow().delete();
  }

  @After
  public void tearDown() {
    WareFlowApplication.getWareFlow().delete();
  }
}
