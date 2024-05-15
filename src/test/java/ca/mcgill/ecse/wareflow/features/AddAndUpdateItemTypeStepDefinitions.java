package ca.mcgill.ecse.wareflow.features;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import java.util.List;
import java.util.Map;
import ca.mcgill.ecse.wareflow.application.WareFlowApplication;
import ca.mcgill.ecse.wareflow.controller.ItemTypeController;
import ca.mcgill.ecse.wareflow.model.ItemType;
import ca.mcgill.ecse.wareflow.model.WareFlow;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class AddAndUpdateItemTypeStepDefinitions {
  String error;
  private WareFlow wareFlow = WareFlowApplication.getWareFlow();

  /**
   * @param dataTable represents itemTypes that should exist in system
   * @author Namir Habib ; Mahmoud Amin ; Thibaut Chan Teck Su
   */
  @Given("the following item types exist in the system \\(f6)")
  public void the_following_item_types_exist_in_the_system_f6(
      io.cucumber.datatable.DataTable dataTable) {
    List<Map<String, String>> rows = dataTable.asMaps();
    for (var row : rows) {
      String name = row.get("name");
      int expectedLifeSpanInDays = Integer.parseInt(row.get("expectedLifeSpanInDays"));
      new ItemType(name, expectedLifeSpanInDays, wareFlow);
    }
  }

  /**
   * @param string represents name of the itemType
   * @param string2 represents lifespan of the itemType Defines steps so :that manager can
   *        successfully add an ItemType
   * @author Namir Habib ; Mahmoud Amin ; Thibaut Chan Teck Su
   */
  @When("the manager attempts to add a new item type to the system with name {string} and expected life span of {string} days \\(f6)")
  public void the_manager_attempts_to_add_a_new_item_type_to_the_system_with_name_and_expected_life_span_of_days_f6(
      String name, String expectedLifeSpanInDays) {
    error = ItemTypeController.addItemType(name, Integer.parseInt(expectedLifeSpanInDays));
  }

  /**
   * @param string itemType to be updated
   * @param string2 new name of itemType
   * @param string3 new lifespan of itemType Defines steps so : manager can successfully update an
   *        ItemType
   * @author Namir Habib ; Mahmoud Amin ; Thibaut Chan Teck Su
   */
  @When("the manager attempts to update an item type in the system with name {string} to have name {string} and expected life span of {string} days \\(f6)")
  public void the_manager_attempts_to_update_an_item_type_in_the_system_with_name_to_have_name_and_expected_life_span_of_days_f6(
      String oldName, String newName, String expectedLifeSpanInDays) {
    error = ItemTypeController.updateItemType(oldName, newName,
        Integer.parseInt(expectedLifeSpanInDays));
  }

  /**
   * @param string represents name of the itemType Verifies that number of itemTypes in system is
   *        correct
   * @author Sophia Carbone ; Mathias Pacheco Lemina
   */
  @Then("the number of item types in the system shall be {string} \\(f6)")
  public void the_number_of_item_types_in_the_system_shall_be_f6(String number) {
    assertEquals(wareFlow.numberOfItemTypes(), Integer.parseInt(number));
  }

  /**
   * @param dataTable represents itemTypes that should exist in system Verifies that number of
   *        itemTypes in datatable exist in itemPLus application
   * @author Sophia Carbone
   */
  @Then("the following item types shall exist in the system \\(f6)")
  public void the_following_item_types_shall_exist_in_the_system_f6(
      io.cucumber.datatable.DataTable dataTable) {
    List<Map<String, String>> rows = dataTable.asMaps();
    for (var row : rows) {
      String name = row.get("name");
      int expectedLifeSpanInDays = Integer.parseInt(row.get("expectedLifeSpanInDays"));
      ItemType itemType = ItemType.getWithName(name);

      assertNotNull(itemType);
      assertEquals(itemType.getExpectedLifeSpanInDays(), expectedLifeSpanInDays);
    }
  }

  /**
   * @param string Represents name of itemType supposed to exist in the system
   * @param string2 Represents expectedLifespan (if string CORRECTLY exist, then associated object
   *        should have that corresponding lifespan
   * @author Sophia Carbone ; Mathias Pacheco Lemina ; Anslean Albert Jeyaras
   */
  @Then("the item type with name {string} and expected life span of {string} days shall exist in the system \\(f6)")
  public void the_item_type_with_name_and_expected_life_span_of_days_shall_exist_in_the_system_f6(
      String name, String expectedLifeSpanInDays) {
    ItemType itemType = ItemType.getWithName(name); // Get the new element to test
    assertNotNull(itemType);
    assertEquals(Integer.parseInt(expectedLifeSpanInDays), itemType.getExpectedLifeSpanInDays());
  }

  /**
   * @param string Represents name of itemType supposed to NOT exist in system
   * @param string2 Represents expectedLifespan ( if string DOES exist, then associated string2
   *        should not) Verifies itemType ItemType(string, string2, itemPLus) does not exist
   * @author Sophia Carbone ; Mathias Pacheco Lemina ; Anslean Albert Jeyaras
   */
  @Then("the item type with name {string} and expected life span of {string} days shall not exist in the system \\(f6)")
  public void the_item_type_with_name_and_expected_life_span_of_days_shall_not_exist_in_the_system_f6(
      String name, String expectedLifeSpanInDays) {
    ItemType itemType = ItemType.getWithName(name); // Get the old or new element to test
    if (itemType != null) { // if not null, old and new name are the same
      assertNotEquals(Integer.parseInt(expectedLifeSpanInDays),
          itemType.getExpectedLifeSpanInDays());
    }
  }

  /**
   * @param string : The error message that should be raised
   * @author Mathias Pacheco Lemina
   */
  @Then("the system shall raise the error {string} \\(f6)")
  public void the_system_shall_raise_the_error_f6(String expectedError) {
    assertEquals(error, expectedError);
  }
}
