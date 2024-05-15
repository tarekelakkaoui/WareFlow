package ca.mcgill.ecse.wareflow.features;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import java.util.List;
import java.util.Map;
import ca.mcgill.ecse.wareflow.application.WareFlowApplication;
import ca.mcgill.ecse.wareflow.controller.UserController;
import ca.mcgill.ecse.wareflow.model.Client;
import ca.mcgill.ecse.wareflow.model.Manager;
import ca.mcgill.ecse.wareflow.model.WareFlow;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class DeleteClientStepDefinitions {
  // "global" variables to be used in step definitions
  private WareFlow wareFlow = WareFlowApplication.getWareFlow();

  /**
   * Initiates the scenario by creating a couple test Clients.
   * 
   * @author William Wang
   * @param clientsDataTable
   */
  @Given("the following clients exist in the system \\(f5)")
  public void the_following_clients_exist_in_the_system_f5(
      io.cucumber.datatable.DataTable clientsDataTable) {
    List<Map<String, String>> rows = clientsDataTable.asMaps();
    for (var row : rows) {
      String username = row.get("username");
      String password = row.get("password");
      String name = row.get("name");
      String phoneNumber = row.get("phoneNumber");
      String address = row.get("address");
      wareFlow.addClient(username, name, password, phoneNumber, address);
    }
  }

  /**
   * Initiates the scenario by creating a manager with specific username and password.
   * 
   * @author Krasimir Kirov
   * @param managerDataTable The Manager's username and password
   */
  @Given("the following manager exists in the system \\(f5)")
  public void the_following_manager_exists_in_the_system_f5(
      io.cucumber.datatable.DataTable managerDataTable) {
    String username = "";
    String password = "";
    List<Map<String, String>> rows = managerDataTable.asMaps();
    for (var row : rows) {
      username = row.get("username");
      password = row.get("password");
    }

    if (wareFlow.hasManager()) {
      Manager existingManager = wareFlow.getManager();
      existingManager.setUsername(username);
      existingManager.setPassword(password);
    } else {
      new Manager(username, "", password, "", wareFlow);
    }
  }

  /**
   * Using an username address, attempts to delete an employee or client's account.
   * 
   * @author Michael Rafferty
   * @param clientUsername
   */
  @When("the client attempts to delete their own account linked to the {string} \\(f5)")
  public void the_client_attempts_to_delete_their_own_account_linked_to_the_f5(
      String clientUsername) {
    UserController.deleteEmployeeOrClient(clientUsername);
  }

  /**
   * Verifies that the provided username is not associated with any client in the system. This is
   * achieved by iterating through the list of clients and ensuring the provided username does not
   * match any client's username.
   *
   * @author Vlad Arama
   * @param expectedClientUsername The username address to verify against the list of clients.
   */
  @Then("the client account linked to {string} shall not exist in the system \\(f5)")
  public void the_client_account_linked_to_shall_not_exist_in_the_system_f5(
      String expectedClientUsername) {
    List<Client> clientsList = wareFlow.getClients();
    for (Client client : clientsList) {
      assertNotEquals("Client with the same username has been found in the system.",
          expectedClientUsername, client.getUsername());
    }
  }

  /**
   * Verifies that a manager exists and that its username matches the provided username
   * 
   * @author Li Yang Lei
   * @param expectedManagerUsername
   */
  @Then("the manager account linked to {string} shall exist in the system \\(f5)")
  public void the_manager_account_linked_to_shall_exist_in_the_system_f5(
      String expectedManagerUsername) {
    Manager manager = wareFlow.getManager();
    assertEquals(expectedManagerUsername, manager.getUsername());
  }

  /**
   * @author Tim Pham
   * @param expectedNumberOfClients
   */
  @Then("the number of clients in the system shall be {string} \\(f5)")
  public void the_number_of_clients_in_the_system_shall_be_f5(String expectedNumberOfClients) {
    // Write code here that turns the phrase above into concrete actions
    assertEquals(Integer.parseInt(expectedNumberOfClients), wareFlow.getClients().size());
  }
}
