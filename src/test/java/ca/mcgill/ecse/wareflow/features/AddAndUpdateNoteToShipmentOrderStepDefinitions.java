package ca.mcgill.ecse.wareflow.features;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import ca.mcgill.ecse.wareflow.application.WareFlowApplication;
import ca.mcgill.ecse.wareflow.controller.ShipmentNoteController;
import ca.mcgill.ecse.wareflow.model.ItemType;
import ca.mcgill.ecse.wareflow.model.Manager;
import ca.mcgill.ecse.wareflow.model.ShipmentNote;
import ca.mcgill.ecse.wareflow.model.ShipmentOrder;
import ca.mcgill.ecse.wareflow.model.User;
import ca.mcgill.ecse.wareflow.model.WareFlow;
import ca.mcgill.ecse.wareflow.model.WarehouseStaff;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class AddAndUpdateNoteToShipmentOrderStepDefinitions {
  private WareFlow wareFlow = WareFlowApplication.getWareFlow();
  private String error;

  /**
   * @author Sebastian Reinhardt
   */
  @Given("the following employees exist in the system \\(f10)")
  public void the_following_employees_exist_in_the_system_f10(
      io.cucumber.datatable.DataTable dataTable) {
    List<Map<String, String>> rows = dataTable.asMaps(); // Retrieving the data from the feature
                                                         // file is a usable format

    for (Map<String, String> row : rows) { // iterating through the rows of the data from the
                                           // feature file
      wareFlow.addEmployee(row.get("username"), row.get("name"), row.get("password"),
          row.get("phoneNumber")); // adding employees with the given information from the feature
                                   // file to the wareFlow
    }
  }

  /**
   * @author Alice Godbout
   */
  @Given("the following manager exists in the system \\(f10)")
  public void the_following_manager_exists_in_the_system_f10(
      io.cucumber.datatable.DataTable dataTable) {
    List<Map<String, String>> rows = dataTable.asMaps(); // Retrieving the data from the feature
                                                         // file is a usable format

    for (Map<String, String> row : rows) { // iterating through the rows of the data from the
                                           // feature file
      Manager manager = new Manager(row.get("username"), "", row.get("password"), "", wareFlow);
      wareFlow.setManager(manager); // setting the manager
    }
  }

  /**
   * @author Ray Liu
   */
  @Given("the following asset types exist in the system \\(f10)")
  public void the_following_asset_types_exist_in_the_system_f10(
      io.cucumber.datatable.DataTable dataTable) {
    List<Map<String, String>> rows = dataTable.asMaps(); // Retrieving the data from the feature
                                                         // file is a usable format

    for (Map<String, String> row : rows) { // iterating through the rows of the data from the
                                           // feature file
      wareFlow.addItemType(row.get("name"), Integer.parseInt(row.get("expectedLifeSpanInDays")));
    }
  }

  /**
   * @author Colin Xiong
   */
  @Given("the following assets exist in the system \\(f10)")
  public void the_following_assets_exist_in_the_system_f10(
      io.cucumber.datatable.DataTable dataTable) {
    List<Map<String, String>> rows = dataTable.asMaps(); // Retrieving the data from the feature
                                                         // file is a usable format

    for (Map<String, String> row : rows) { // iterating through the rows of the data from the
                                           // feature file
      wareFlow.addItemContainer(Integer.parseInt(row.get("containerNumber")),
          Integer.parseInt(row.get("areaNumber")), Integer.parseInt(row.get("slotNumber")),
          Date.valueOf(row.get("addedOnDate")), ItemType.getWithName(row.get("type")));
    }
  }

  /**
   * @author Aurelia Bouliane
   */
  @Given("the following orders exist in the system \\(f10)")
  public void the_following_orders_exist_in_the_system_f10(
      io.cucumber.datatable.DataTable dataTable) {
    List<Map<String, String>> rows = dataTable.asMaps(); // Retrieving the data from the feature
                                                         // file is a usable format

    for (Map<String, String> row : rows) { // iterating through the rows of the data from the
                                           // feature file
      wareFlow.addOrder(Integer.parseInt(row.get("id")), Date.valueOf(row.get("placedOnDate")),
          row.get("description"), Integer.parseInt(row.get("quantity")),
          User.getWithUsername(row.get("orderPlacer"))); // adding the shipmentOrders with the
      // given information from the feature file
    }
  }

  /**
   * @author Alexander Liu
   */
  @Given("the following notes exist in the system \\(f10)")
  public void the_following_notes_exist_in_the_system_f10(
      io.cucumber.datatable.DataTable dataTable) {
    List<Map<String, String>> rows = dataTable.asMaps(); // Retrieving the data from the feature
                                                         // file is a usable format

    for (Map<String, String> row : rows) { // iterating through the rows of the data from the
                                           // feature file
      ShipmentOrder shipmentOrder = ShipmentOrder.getWithId(Integer.parseInt(row.get("orderId"))); // finding
                                                                                                   // the
                                                                                                   // correct
                                                                                                   // shipmentOrder
      shipmentOrder.addShipmentNote(Date.valueOf(row.get("date")), row.get("description"),
          (WarehouseStaff) WarehouseStaff.getWithUsername(row.get("noteTaker"))); // adding the
      // shipmentNotes with the
      // given information from the
      // feature file
    }
  }

  /**
   * @author Houman Azari
   */
  @When("staff with username {string} attempts to add a new note with date {string} and description {string} to an existing shipment order {string} \\(f10)")
  public void staff_with_username_attempts_to_add_a_new_note_with_date_and_description_to_an_existing_maintenance_order_f10(
      String userUsername, String date, String noteDescription, String orderId) {
    error = ShipmentNoteController.addShipmentNote(Date.valueOf(date), noteDescription,
        Integer.parseInt(orderId), userUsername); // calling the controller method
  }

  /**
   * @author Alice Godbout
   */
  @When("the manger attempts to update note number {string} for shipment order {string} with note taker {string}, date {string}, and description {string} \\(f10)")
  public void the_manger_attempts_to_update_note_number_for_maintenance_order_with_note_taker_date_and_description_f10(
      String noteIndex, String orderId, String noteTaker, String dateAdded,
      String noteDescription) {
    error = ShipmentNoteController.updateShipmentNote(Integer.parseInt(orderId),
        Integer.parseInt(noteIndex), Date.valueOf(dateAdded.trim()), noteDescription, noteTaker);
  }

  /**
   * @author Ray Liu
   */
  @Then("the number of notes in the system shall be {string} \\(f10)")
  public void the_number_of_notes_in_the_system_shall_be_f10(String expectedAmountOfNotes) {
    int amountOfNotes = 0;
    for (ShipmentOrder shipmentOrder : wareFlow.getOrders()) {
      amountOfNotes += shipmentOrder.numberOfShipmentNotes();
      // order
    }
    assertEquals(Integer.parseInt(expectedAmountOfNotes), amountOfNotes); // asserting the numbers
                                                                          // match
  }

  /**
   * @author Aurelia Bouliane
   */
  @Then("the number of notes for order {string} in the system shall be {string} \\(f10)")
  public void the_number_of_notes_for_order_in_the_system_shall_be_f10(String orderId,
      String expectedAmountOfNotes) {
    ShipmentOrder shipmentOrder = ShipmentOrder.getWithId(Integer.parseInt(orderId));
    assertEquals(Integer.parseInt(expectedAmountOfNotes), shipmentOrder.numberOfShipmentNotes());
  }

  /**
   * @author Sebastian Reinhardt
   */
  @Then("the note number {string} for order {int} with noteTaker {string}, date {string}, and description {string} shall exist in the system \\(f10)")
  public void the_note_number_for_order_with_note_taker_date_and_description_shall_exist_in_the_system_f10(
      String noteIndex, Integer orderId, String noteTaker, String dateAdded,
      String noteDescription) {
    ShipmentOrder shipmentOrder = ShipmentOrder.getWithId(orderId); // getting the
                                                                    // correct order
    ShipmentNote shipmentNote = shipmentOrder.getShipmentNote(Integer.parseInt(noteIndex)); // getting
    // the
    // correct
    // order
    assertNotNull(shipmentNote); // making sure it exists
    assertEquals(noteTaker, shipmentNote.getNoteTaker().getUsername());
    // are the same
    assertEquals(dateAdded, shipmentNote.getDate().toString());
    assertEquals(noteDescription, shipmentNote.getDescription());
  }

  /**
   * @author Sebastian Reinhardt
   */
  @Then("the following notes shall exist in the system \\(f10)")
  public void the_following_notes_shall_exist_in_the_system_f10(
      io.cucumber.datatable.DataTable dataTable) {
    List<List<String>> rows = dataTable.asLists();

    List<ShipmentOrder> orders = wareFlow.getOrders(); // Gettting all the orders
    List<ShipmentNote> notes = new ArrayList<ShipmentNote>();
    for (ShipmentOrder order : orders) {
      notes.addAll(order.getShipmentNotes());
    }

    for (ShipmentNote note : notes) { // iterating through the notes
      List<String> noteAsList = new ArrayList<String>();
      noteAsList.add(note.getNoteTaker().getUsername());
      // note
      noteAsList.add("" + note.getOrder().getId());
      noteAsList.add(note.getDate().toString());
      noteAsList.add(note.getDescription());
      assertTrue(rows.contains(noteAsList));
    }
  }

  /**
   * @author Alexander Liu
   */
  @Then("the system shall raise the error {string} \\(f10)")
  public void the_system_shall_raise_the_error_f10(String errorMessage) {
    assertEquals(errorMessage, error); // asserting the errorMessages are identical
  }
}
