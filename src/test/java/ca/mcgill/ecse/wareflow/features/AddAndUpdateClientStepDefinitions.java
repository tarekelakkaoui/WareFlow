package ca.mcgill.ecse.wareflow.features;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.List;
import java.util.Map;
import ca.mcgill.ecse.wareflow.application.WareFlowApplication;
import ca.mcgill.ecse.wareflow.controller.UserController;
import ca.mcgill.ecse.wareflow.model.Client;
import ca.mcgill.ecse.wareflow.model.Manager;
import ca.mcgill.ecse.wareflow.model.User;
import ca.mcgill.ecse.wareflow.model.WareFlow;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class AddAndUpdateClientStepDefinitions {
  private String error;

  /**
   * @author Eric Zhu
   */
  @Given("the following clients exist in the system \\(f3)")
  public void the_following_clients_exist_in_the_system_f3(
      io.cucumber.datatable.DataTable dataTable) {
    // Converts the dataTable into a list of maps
    List<Map<String, String>> rows = dataTable.asMaps();

    // Iterate through each row of the dataTable and creates a new Client for each row
    for (var row : rows) {
      String username = row.get("username");
      String password = row.get("password");
      String name = row.get("name");
      String phoneNumber = row.get("phoneNumber");
      String address = row.get("address");
      new Client(username, name, password, phoneNumber, address, WareFlowApplication.getWareFlow());
    }
  }

  /**
   * 
   * @author Muhammad Hamamd
   */
  @Given("the following manager exists in the system \\(f3)")
  public void the_following_manager_exists_in_the_system_f3(
      io.cucumber.datatable.DataTable dataTable) {
    // Converts the dataTable into a list of maps
    List<Map<String, String>> rows = dataTable.asMaps();

    // Iterate through each row of the dataTable and creates a new Manager for each row
    for (var row : rows) {
      String username = row.get("username");
      String password = row.get("password");
      String name = row.get("name");
      String phoneNumber = row.get("phoneNumber");
      new Manager(username, name, password, phoneNumber, WareFlowApplication.getWareFlow());
    }
  }

  /**
   * @author Qasim Li
   */
  @When("a new client attempts to register with {string}, {string}, {string}, {string}, and {string} \\(f3)")
  public void a_new_client_attempts_to_register_with_and_f3(String username, String password,
      String name, String phoneNumber, String address) {
    // Calls the controller to add a client to the WareFlow object and stores the error code
    error = UserController.addEmployeeOrClient(username, password, name, phoneNumber,
        false, address);
  }

  /**
   * @author Steve Chen
   */
  @When("the client with {string} attempts to update their account information to {string}, {string}, {string}, and {string} \\(f3)")
  public void the_client_with_attempts_to_update_their_account_information_to_and_f3(String username,
      String newPassword, String newName, String newPhoneNumber, String newAddress) {
    // Updates a client to the WareFlow object and stores the error code
    error = UserController.updateEmployeeOrClient(username, newPassword, newName,
        newPhoneNumber, newAddress);
  }

  /**
   * @author Eric Zhu
   */
  @Then("the following {string} shall be raised \\(f3)")
  public void the_following_shall_be_raised_f3(String errorString) {
    // Checks if the error code contains the expected error code as a substring
    assertTrue(error.contains(errorString));
  }

  /**
   * @author Qasim Li, Bohan Wang
   */
  @Then("the number of clients in the system shall be {string} \\(f3)")
  public void the_number_of_clients_in_the_system_shall_be_f3(String expectedClientNumber) {
    // Retrieves the number of clients in the WareFlow object and typecasts it into a String
    Integer numberOfClients = WareFlowApplication.getWareFlow().getClients().size();
    Integer expectedSize = Integer.parseInt(expectedClientNumber);

    // Checks if the number of clients in WareFlow matches the expected number
    assertEquals(expectedSize, numberOfClients);
  }

  /**
   * @author Christopher
   */
  @Then("a new client account shall exist with {string}, {string}, {string}, {string}, and {string} \\(f3)")
  public void a_new_client_account_shall_exist_with_and_f3(String username, String password,
      String name, String phoneNumber, String address) {
    WareFlow wareFlow = WareFlowApplication.getWareFlow();

    // Retrieves the list of clients currently in the WareFlow object
    List<Client> clients = wareFlow.getClients();
    Client newClient = null;

    // Iterates through all clients, stores the client with the specified username into newClient
    for (Client client : clients) {
      if (client.getUsername().equals(username)) {
        newClient = client;
      }
    }

    // Checks if newClient is null, i.e. if the username matches a client in the system
    assertNotNull(newClient);

    // Checks if the client attributes match the expected values
    assertEquals(username, newClient.getUsername());
    assertEquals(password, newClient.getPassword());
    assertEquals(name, newClient.getName());
    assertEquals(phoneNumber, newClient.getPhoneNumber());
    assertEquals(address, newClient.getAddress());
  }

  /**
   * @author Connor Tate
   */
  @Then("the following clients shall exist in the system \\(f3)")
  public void the_following_clients_shall_exist_in_the_system_f3(
      io.cucumber.datatable.DataTable dataTable) {
    // Converts the dataTable into a list of maps
    List<Map<String, String>> rows = dataTable.asMaps();

    // Iterates through all rows in dataTable to verify if a client exists with each username
    for (var row : rows) {
      String username = row.get("username");
      // Verifies that the user with that username exists
      assertTrue(User.getWithUsername(username) != null);
      Client client = (Client) User.getWithUsername(username);
      // Verifies that the client's attributes match the expected values
      assertEquals(row.get("password"), client.getPassword());
      assertEquals(row.get("name"), client.getName());
      assertEquals(row.get("phoneNumber"), client.getPhoneNumber());
      assertEquals(row.get("address"), client.getAddress());
    }
  }

  /**
   * @author Steve Chen
   */
  @Then("their client account information will be updated and is now {string}, {string}, {string}, {string}, and {string} \\(f3)")
  public void their_client_account_information_will_be_updated_and_is_now_and_f3(String username,
      String newPasssword, String newName, String newPhoneNumber, String newAddress) {
    WareFlow wareFlow = WareFlowApplication.getWareFlow();
    Client currentClient = null;

    // Iterates through all clients, stores the client with the specified username into newClient
    for (Client client : wareFlow.getClients()) {
      if (username.equals(client.getUsername())) {
        currentClient = client;
      }
    }

    // Checks if newClient is null, i.e. if the username matches a client in the system
    assertNotNull(currentClient);
    // Checks if the client attributes match the expected values
    assertEquals(username, currentClient.getUsername());
    assertEquals(newPasssword, currentClient.getPassword());
    assertEquals(newName, currentClient.getName());
    assertEquals(newPhoneNumber, currentClient.getPhoneNumber());
    assertEquals(newAddress, currentClient.getAddress());
  }
}
