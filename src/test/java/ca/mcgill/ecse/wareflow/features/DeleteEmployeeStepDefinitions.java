package ca.mcgill.ecse.wareflow.features;

import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import ca.mcgill.ecse.wareflow.controller.UserController;
import ca.mcgill.ecse.wareflow.model.Employee;
import ca.mcgill.ecse.wareflow.model.Manager;
import ca.mcgill.ecse.wareflow.model.User;
import ca.mcgill.ecse.wareflow.model.WareFlow;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class DeleteEmployeeStepDefinitions {
  private WareFlow wareFlow =
      ca.mcgill.ecse.wareflow.application.WareFlowApplication.getWareFlow();

  /**
   * This step checks if the following employees exist in the system.
   * 
   * @author Kevin Li
   * @param dataTable This is a data table containing all the employees that exists in the system.
   */
  @Given("the following employees exist in the system \\(f4)")
  public void the_following_employees_exist_in_the_system_f4(
      io.cucumber.datatable.DataTable dataTable) {
    List<Map<String, String>> rows = dataTable.asMaps();
    for (var row : rows) {
      String username = row.get("username");
      String name = row.get("name");
      String password = row.get("password");
      String phoneNumber = row.get("phoneNumber");
      new Employee(username, name, password, phoneNumber, wareFlow);
    }
  }

  /**
   * This step checks if the following managers exist in the system.
   * 
   * @author Mathieu Allaire
   * @param dataTable This is a data table containing all the managers that exists in the system.
   */
  @Given("the following manager exists in the system \\(f4)")
  public void the_following_manager_exists_in_the_system_f4(
      io.cucumber.datatable.DataTable dataTable) {
    List<Map<String, String>> rows = dataTable.asMaps();
    for (var row : rows) {
      String username = row.get("username");
      String password = row.get("password");
      new Manager(username, "", password, "", wareFlow);
    }
  }

  /**
   * This step deletes an employee account linked to a particular username string.
   * 
   * @author Luis Jarquin
   * @param employeeUsername This is a string containing the username address linked ot an employee
   *        account.
   */
  @When("the employee attempts to delete their own account linked to the {string} \\(f4)")
  public void the_employee_attempts_to_delete_their_own_account_linked_to_the_f4(
      String employeeUsername) {
      UserController.deleteEmployeeOrClient(employeeUsername);
  }

  /**
   * This step checks that once the employee with username string attempts to delete their own account,
   * their account does not exist in the system.
   * 
   * @author Jerome Desrosiers
   * @param employeeUsername This is a string containing the username address that was linked to the now
   *        deleted employee account.
   */
  @Then("the employee account linked to {string} shall not exist in the system \\(f4)")
  public void the_employee_account_linked_to_shall_not_exist_in_the_system_f4(
      String employeeUsername) {
    User actualEmployee = Employee.getWithUsername(employeeUsername);
    if (actualEmployee != null) {
      Assertions.assertFalse(actualEmployee instanceof Employee);
    }
  }

  /**
   * This step checks that after the employee with username string attempts to delete their own
   * account, the manager still exists in the system.
   * 
   * @author Yuri Sorice
   * @param managerUsername This is a string containing the username address of the manager.
   */
  @Then("the manager account linked to {string} shall exist in the system \\(f4)")
  public void the_manager_account_linked_to_shall_exist_in_the_system_f4(String managerUsername) {
    User expectedManager = Manager.getWithUsername(managerUsername);
    User actualManager = wareFlow.getManager();
    Assertions.assertEquals(expectedManager, actualManager);
  }

  /**
   * This step checks that the number of employees in the system is correct after deletion of an
   * employee with username string.
   * 
   * @author Tessa Hason
   * @param numOfEmployees This is a string containing the expected number of employees.
   */
  @Then("the number of employees in the system shall be {string} \\(f4)")
  public void the_number_of_employees_in_the_system_shall_be_f4(String numOfEmployees) {
    int expectedNumOfEmployees = Integer.parseInt(numOfEmployees);
    int actualNumOfEmployees = wareFlow.numberOfEmployees();
    Assertions.assertEquals(expectedNumOfEmployees, actualNumOfEmployees);
  }
}
