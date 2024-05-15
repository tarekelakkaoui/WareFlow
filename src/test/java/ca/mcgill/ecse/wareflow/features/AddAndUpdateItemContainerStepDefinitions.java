package ca.mcgill.ecse.wareflow.features;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import java.sql.Date;
import java.util.List;
import java.util.Map;
import ca.mcgill.ecse.wareflow.application.WareFlowApplication;
import ca.mcgill.ecse.wareflow.controller.ItemContainerController;
import ca.mcgill.ecse.wareflow.model.ItemContainer;
import ca.mcgill.ecse.wareflow.model.ItemType;
import ca.mcgill.ecse.wareflow.model.WareFlow;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class AddAndUpdateItemContainerStepDefinitions {
  private WareFlow wareflow = WareFlowApplication.getWareFlow();
  private String error;

  /**
   * @author Jules Delabrousse (@JulesDelab)
   * @author Deniz Emre (@denizemre03)
   * @param dataTable representing table of existing item types (see
   *        ../resources/AddAndUpdateContainer.feature)
   */
  @Given("the following item types exist in the system \\(f8)")
  public void the_following_container_types_exist_in_the_system_f8(
      io.cucumber.datatable.DataTable dataTable) {

    List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

    for (Map<String, String> columns : rows) {
      new ItemType(columns.get("name"), Integer.parseInt(columns.get("expectedLifeSpanInDays")),
          wareflow);
    }
  }

  /**
   * @author Kaitlyn Pereira (@kaitlynp18)
   * @author Deniz Emre (@denizemre03)
   * @param dataTable representing table of existing containers (see
   *        ../resources/AddAndUpdateContainer.feature)
   */
  @Given("the following containers exist in the system \\(f8)")
  public void the_following_containers_exist_in_the_system_f8(
      io.cucumber.datatable.DataTable dataTable) {

    List<Map<String, String>> rows = dataTable.asMaps();

    for (var row : rows) {
      int aContainerNumber = Integer.parseInt(row.get("containerNumber"));
      ItemType aItemType = ItemType.getWithName(row.get("type"));
      Date aAddedOnDate = Date.valueOf(row.get("addedOnDate"));
      int aAreaNumber = Integer.parseInt(row.get("areaNumber"));
      int aSlotNumber = Integer.parseInt(row.get("slotNumber"));
      new ItemContainer(aContainerNumber, aAreaNumber, aSlotNumber, aAddedOnDate, wareflow,
          aItemType);
    }
  }

  /**
   * @author Viviane-Laura Tain (@vivianeltain)
   * @author Deniz Emre (@denizemre03)
   * @param containerNum represents container number to update
   * @param itemType represents item type
   * @param addedOnDateString represents added date
   * @param areaNum represents area number
   * @param slotNum represents slot number
   */
  @When("the manager attempts to add a new item container to the system with container number {string}, type {string}, added date {string}, area number {string}, and slot number {string} \\(f8)")
  public void the_manager_attempts_to_add_a_new_container_to_the_system_with_container_number_type_purchase_date_area_number_and_slot_number_f8(
      String containerNum, String itemType, String addedOnDateString, String areaNum, String slotNum) {

    int containerNumber = Integer.parseInt(containerNum);
    int areaNumber = Integer.parseInt(areaNum);
    int slotNumber = Integer.parseInt(slotNum);
    Date addedOnDate = Date.valueOf(addedOnDateString);

    error = ItemContainerController.addItemContainer(containerNumber, areaNumber, slotNumber,
        addedOnDate, itemType);
  }

  /**
   * @author Kaitlyn Pereira (@kaitlynp18)
   * @author Ana Floarea (@anafloarea)
   * @param containerNum represents container number to update
   * @param itemType represents item type
   * @param addedOnDate represents added date
   * @param areaNum represents area number
   * @param slotNum represents slot number
   */
  @When("the manager attempts to update container number {string} in the system with type {string}, addedOnDate {string}, areaNumber {string}, and slotNumber {string} \\(f8)")
  public void the_manager_attempts_to_update_container_number_in_the_system_with_type_purchase_date_area_number_and_slot_number_f8(
      String containerNum, String itemType, String addedOnDate, String areaNum, String slotNum) {
    // get all the new parameters
    int containerNumber = Integer.parseInt(containerNum);
    int newAreaNumber = Integer.parseInt(areaNum);
    int newSlotNumber = Integer.parseInt(slotNum);
    Date newAddedOnDate = Date.valueOf(addedOnDate);
    String newItemTypeName = itemType;
    // update the container with new parameters and track possible error
    error = ItemContainerController.updateItemContainer(containerNumber, newAreaNumber,
        newSlotNumber, newAddedOnDate, newItemTypeName);
  }

  /**
   * @author Kaitlyn Pereira (@kaitlynp18)
   * @author Caroline Thom (@carolinethom02)
   * @param expectedNumOfContainers represents the expected number of containers in the system
   */
  @Then("the number of containers in the system shall be {string} \\(f8)")
  public void the_number_of_containers_in_the_system_shall_be_f8(String expectedNumOfContainers) {

    int numOfContainers = wareflow.numberOfItemContainers();
    assertEquals(Integer.parseInt(expectedNumOfContainers), numOfContainers);
  }

  /**
   * @author Jules Delabrousse (@JulesDelab)
   * @author Caroline Thom (@carolinethom02)
   * @param dataTable representing table of supposedly existing containers (see
   *        ../resources/AddAndUpdateContainer.feature)
   */
  @Then("the following containers shall exist in the system \\(f8)")
  public void the_following_containers_shall_exist_in_the_system_f8(
      io.cucumber.datatable.DataTable dataTable) {

    List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
    for (Map<String, String> columns : rows) {
      ItemContainer currentContainer =
          ItemContainer.getWithContainerNumber(Integer.parseInt(columns.get("containerNumber")));
      assertNotNull(currentContainer);
      assertEquals(ItemType.getWithName(columns.get("type")), currentContainer.getItemType());
      assertEquals(Date.valueOf(columns.get("addedOnDate")), currentContainer.getAddedOnDate());
      assertEquals(Integer.parseInt(columns.get("areaNumber")), currentContainer.getAreaNumber());
      assertEquals(Integer.parseInt(columns.get("slotNumber")), currentContainer.getSlotNumber());
    }
  }

  /**
   * @author Ana Floarea (@anafloarea)
   * @author Viviane-Laura Tain (@vivianeltain)
   * @param dataTable
   */
  @Then("the following item types shall exist in the system \\(f8)")
  public void the_following_containers_types_shall_exist_in_the_system_f8(
      io.cucumber.datatable.DataTable dataTable) {

    List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
    for (Map<String, String> columns : rows) {
      ItemType actualItemType = ItemContainer
          .getWithContainerNumber(Integer.parseInt(columns.get("containerNumber"))).getItemType();
      ItemType expectedItemType = ItemType.getWithName(columns.get("type"));
      assertEquals(expectedItemType.getName(), actualItemType.getName());
      assertEquals(expectedItemType.getExpectedLifeSpanInDays(),
          actualItemType.getExpectedLifeSpanInDays());
    }
  }

  /**
   * @author Viviane-Laura Tain (@vivianeltain)
   * @author Caroline Thom (@carolinethom02)
   * @param expectedError
   */
  @Then("the error {string} shall be raised \\(f8)")
  public void the_error_shall_be_raised_f8(String expectedError) {

    assertEquals(expectedError, error);
  }

  /**
   * @author Jules Delabrousse (@JulesDelab)
   * @author Ana Floarea (@anafloarea)
   * @param itemType Item type of the compared container
   * @param containerNumber Container number of the compared container
   * @param addedOnDate Added date of the compared container
   * @param areaNumber Area Number of the compared container
   * @param slotNumber Slot Number of the compared container
   */
  @Then("the item container {string} with container number {string}, added date {string}, area number {string}, and slot number {string} shall exist in the system \\(f8)")
  public void the_container_with_container_number_purchase_date_area_number_and_slot_number_shall_exist_in_the_system_f8(
      String itemType, String containerNumber, String addedOnDate, String areaNumber,
      String slotNumber) {

    ItemContainer containerInSystem =
        ItemContainer.getWithContainerNumber(Integer.parseInt(containerNumber));
    assertNotNull(containerInSystem);
    assertEquals(containerInSystem.getItemType(), ItemType.getWithName(itemType));
    assertEquals(containerInSystem.getAddedOnDate(), Date.valueOf(addedOnDate));
    assertEquals(containerInSystem.getAreaNumber(), Integer.parseInt(areaNumber));
    assertEquals(containerInSystem.getSlotNumber(), Integer.parseInt(slotNumber));
  }
}
