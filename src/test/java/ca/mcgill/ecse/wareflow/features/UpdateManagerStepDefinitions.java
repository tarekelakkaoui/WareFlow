package ca.mcgill.ecse.wareflow.features;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Assertions;
import ca.mcgill.ecse.wareflow.application.WareFlowApplication;
import ca.mcgill.ecse.wareflow.controller.UserController;
import ca.mcgill.ecse.wareflow.model.Manager;
import ca.mcgill.ecse.wareflow.model.WareFlow;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class UpdateManagerStepDefinitions {
    private static WareFlow assetPlus = WareFlowApplication.getWareFlow();

  private String error = "";
  private String username = "";
  private String password = "";

  /**
   * @author Omar Moussa
   */
  @Given("the following manager exists in the system \\(f1)")
  public void the_following_manager_exists_in_the_system_f1(
      io.cucumber.datatable.DataTable dataTable) {
    Manager manager =
        new Manager(dataTable.row(1).get(0), null, dataTable.row(1).get(1), null, assetPlus);
    if (!assetPlus.hasManager())
      assetPlus.setManager(manager);

    Assertions.assertTrue(assetPlus.hasManager());
    Assertions.assertEquals("manager", assetPlus.getManager().getUsername());
    Assertions.assertEquals("manager", assetPlus.getManager().getPassword());

  }

  /**
   * @author Omar Moussa
   */
  @When("a manager with {string} attempts to update their password to {string} \\(f1)")
  public void a_manager_with_attempts_to_update_their_password_to_f1(String username,
      String password) {
    this.error = UserController.updateManager(password);
    this.username = assetPlus.getManager().getUsername();
    this.password = assetPlus.getManager().getPassword();
  }

  /**
   * @author Achraf Ghellach
   */
  @Then("the following {string} shall be raised \\(f1)")
  public void the_following_shall_be_raised_f1(String errorExpected) {
    Assertions.assertTrue(error.contains(errorExpected));
    error = "";
  }

  /**
   * @author Achraf Ghellach
   */
  @Then("the manager account information will not be updated and will keep {string} and {string} \\(f1)")

  public void the_manager_account_information_will_not_be_updated_and_will_keep_and_f1(
          String username, String password) {
    Assertions.assertEquals(username, this.username);
    Assertions.assertEquals(password, this.password);
  }

  /**
   * @author Teddy El-Husseini
   */
  @Then("the manager account information will be updated and is now {string} and {string} \\(f1)")
  public void the_manager_account_information_will_be_updated_and_is_now_and_f1(String username,
                                                                                String password) {
    Assertions.assertEquals(username, this.username);
    Assertions.assertEquals(password, this.password);
  }

  /**
   * @author Teddy El-Husseini
   */
  @Then("the number of managers in the system shall be {string} \\(f1)")
  public void the_number_of_managers_in_the_system_shall_be_f1(String numberOfManagersExpected) {
    String managerNumber = "0";
    boolean managerExists = WareFlowApplication.getWareFlow().hasManager();
    if (managerExists) {
      managerNumber = "1";
    }
    assertEquals(numberOfManagersExpected, managerNumber);
  }
}
