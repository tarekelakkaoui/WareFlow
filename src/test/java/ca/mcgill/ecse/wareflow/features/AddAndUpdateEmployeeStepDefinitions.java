package ca.mcgill.ecse.wareflow.features;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.List;
import java.util.Map;
import ca.mcgill.ecse.wareflow.application.WareFlowApplication;
import ca.mcgill.ecse.wareflow.controller.UserController;
import ca.mcgill.ecse.wareflow.model.Employee;
import ca.mcgill.ecse.wareflow.model.Manager;
import ca.mcgill.ecse.wareflow.model.User;
import ca.mcgill.ecse.wareflow.model.WareFlow;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class AddAndUpdateEmployeeStepDefinitions {
  private static WareFlow assetPlus = WareFlowApplication.getWareFlow();
  private String error = "";


  /**
   * @author Jatin Patel and Anastasiia Nemyrovska
   */
  @Given("the following employees exist in the system \\(f2)")
  public void the_following_employees_exist_in_the_system_f2(
      io.cucumber.datatable.DataTable dataTable) {

    List<Map<String, String>> employeesToAdd = dataTable.asMaps();
    for (Map<String, String> employee : employeesToAdd) {
      String username = employee.get("username");
      String name = employee.get("name");
      String password = employee.get("password");
      String phoneNumber = employee.get("phoneNumber");
      assetPlus.addEmployee(username, name, password, phoneNumber);
    }
  }

  /**
   * @author Jatin Patel and Anastasiia Nemyrovska
   */
  @Given("the following manager exists in the system \\(f2)")
  public void the_following_manager_exists_in_the_system_f2(
      io.cucumber.datatable.DataTable dataTable) {

    List<Map<String, String>> managerToAdd = dataTable.asMaps();
    for (Map<String, String> manager : managerToAdd) {
      String username = manager.get("username");
      String name = manager.get("name");
      String password = manager.get("password");
      String phoneNumber = manager.get("phoneNumber");
      new Manager(username, name, password, phoneNumber, assetPlus);
    }
  }

  /**
   * @author Jatin Patel and Anastasiia Nemyrovska
   */
  @When("a new employee attempts to register with {string}, {string}, {string}, and {string} \\(f2)")
  public void a_new_employee_attempts_to_register_with_and_f2(String username, String password,
      String name, String phoneNumber) {

    callController(UserController.addEmployeeOrClient(username, password, name, phoneNumber, true, ""));
  }

  /**
   * @author Pei Yan Geng, Dmytro Martyniuk and Laurence Perreault
   */
  @When("the employee with {string} attempts to update their account information to {string}, {string}, and {string} \\(f2)")
  public void the_employee_with_attempts_to_update_their_account_information_to_and_f2(
      String username, String newPassword, String newName, String newPhoneNumber) {
    callController(UserController.updateEmployeeOrClient(username, newPassword, newName, newPhoneNumber, ""));
  }

  /**
   * @author Pei Yan Geng, Dmytro Martyniuk and Laurence Perreault
   */
  @Then("the following {string} shall be raised \\(f2)")
  public void the_following_shall_be_raised_f2(String errorString) {
    assertTrue(error.contains(errorString));
  }

  /**
   * @author Pei Yan Geng, Dmytro Martyniuk and Laurence Perreault
   */
  @Then("the number of employees in the system shall be {string} \\(f2)")
  public void the_number_of_employees_in_the_system_shall_be_f2(String expectedNumberOfEmployees) {

    List<Employee> employees = assetPlus.getEmployees();

    assertEquals((Integer) employees.size(), Integer.parseInt(expectedNumberOfEmployees));
  }

  /**
   * @author Marc-Antoine Nadeau & Behrad Rezaie
   */
  @Then("a new employee account shall exist with {string}, {string}, {string}, and {string} \\(f2)")
  public void a_new_employee_account_shall_exist_with_and_f2(String username, String password,
      String name, String phoneNumber) {

    // Checks an employee with given username exists
    assertTrue(Employee.hasWithUsername(username));
    // Checks other employee attributes
    Employee existingEmployee = (Employee) User.getWithUsername(username);
    assertEquals(password, existingEmployee.getPassword());
    assertEquals(name, existingEmployee.getName());
    assertEquals(phoneNumber, existingEmployee.getPhoneNumber());

  }

  /**
   * @author Marc-Antoine Nadeau & Behrad Rezaie
   */
  @Then("their employee account information will be updated and is now {string}, {string}, {string}, and {string} \\(f2)")
  public void their_employee_account_information_will_be_updated_and_is_now_and_f2(String username,
      String newPassword, String newName, String newPhoneNumber) {

    assertTrue(Employee.hasWithUsername(username));
    Employee updatedEmployeeWithKnownUsernameAddress = (Employee) User.getWithUsername(username);
    assertEquals(newPassword, updatedEmployeeWithKnownUsernameAddress.getPassword());
    assertEquals(newName, updatedEmployeeWithKnownUsernameAddress.getName());
    assertEquals(newPhoneNumber, updatedEmployeeWithKnownUsernameAddress.getPhoneNumber());
  }

  /**
   * @author Marc-Antoine Nadeau & Behrad Rezaie
   */
  @Then("the following employees shall exist in the system \\(f2)")
  public void the_following_employees_shall_exist_in_the_system_f2(
      io.cucumber.datatable.DataTable dataTable) {
    List<Map<String, String>> employeeData = dataTable.asMaps();
    // dataTable should be converted to this format:
    // [
    // { "username"="jeff@ap.com", "password"="pass1", "name"="Jeff", "phoneNumber"="(555)555-5555" },
    // { "username"="john@ap.com", "password"="pass2", "name"="John", "phoneNumber"="(444)444-4444" }
    // ]
    for (Map<String, String> data : employeeData) {
      // Get info from each sub-directory
      String username = data.get("username");
      String name = data.get("name");
      String password = data.get("password");
      String phoneNumber = data.get("phoneNumber");

      // Compares it to what we have in the system
      Employee employee = (Employee) User.getWithUsername(username);
      assertNotNull(employee, "Employee with" + username + " was not found.");
      assertEquals(name, employee.getName());
      assertEquals(password, employee.getPassword());
      assertEquals(phoneNumber, employee.getPhoneNumber());
    }
  }

  private void callController(String result) {
    if (!result.isEmpty()) {
      error += result;
    }
  }
}
