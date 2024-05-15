package ca.mcgill.ecse.wareflow.features;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import java.sql.Date;
import java.util.List;
import java.util.Map;
import ca.mcgill.ecse.wareflow.application.WareFlowApplication;
import ca.mcgill.ecse.wareflow.controller.ShipmentNoteController;
import ca.mcgill.ecse.wareflow.model.Employee;
import ca.mcgill.ecse.wareflow.model.ItemContainer;
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

public class DeleteShipmentNoteStepDefinitions {
  WareFlow wareFlow = WareFlowApplication.getWareFlow();
  public static ShipmentNote deletedNote;

  /**
   * Loads the input dataTable, instantiates and adds the given employees
   *
   * @param dataTable a data table with each employees information (username, password, name,
   *        phoneNumber)
   */
  @Given("the following employees exist in the system \\(f11)")
  public void the_following_employees_exist_in_the_system_f11(
      io.cucumber.datatable.DataTable dataTable) {
    // Turns the dataTable into a list of lists (each row becomes a list)
    List<Map<String, Object>> tableList = dataTable.asMaps(String.class, Object.class);

    // Iterates through each list to create the specified employees and add them to the WareFlow
    // application.
    for (Map<String, Object> row : tableList) {
      String username = (row.get("username")).toString();
      String password = (row.get("password")).toString();
      String name = (row.get("name")).toString();
      String phoneNumber = (row.get("phoneNumber")).toString();

      // Instantiate and add the specified employees to the WareFlow application
      new Employee(username, password, name, phoneNumber, wareFlow);


    }
  }

  /**
   * Loads the input dataTable and updates the manager
   *
   * @param dataTable a data table with each managers information (username, password)
   */
  @Given("the following manager exists in the system \\(f11)")
  public void the_following_manager_exists_in_the_system_f11(
      io.cucumber.datatable.DataTable dataTable) {

    // Turns the dataTable into a list of lists (each row becomes a list).
    List<Map<String, Object>> tableList = dataTable.asMaps(String.class, Object.class);

    // Iterates through each list to create the specified employees and add them to the WareFlow
    // application.
    for (Map<String, Object> row : tableList) {
      String username = (row.get("username")).toString(); // Not used since the username shouldn't
                                                          // change
      String password = (row.get("password")).toString();

      // Instantiate and add the manager to the WareFlow application
      new Manager(username, password, "", "", wareFlow);


    }
  }

  /**
   * Loads the input dataTable, instantiates and adds the given item types
   *
   * @param dataTable a data table with each item type in the system (with its name and
   *        expectedLifeSpanInDays)
   */
  @Given("the following item types exist in the system \\(f11)")
  public void the_following_container_types_exist_in_the_system_f11(
      io.cucumber.datatable.DataTable dataTable) {

    // Turns the dataTable into a list of lists (each row becomes a list).
    List<Map<String, Object>> tableList = dataTable.asMaps(String.class, Object.class);

    // Iterates through each list to create the specified item types and add them to the
    // WareFlow
    // application.
    for (Map<String, Object> row : tableList) {
      String name = (row.get("name")).toString();
      int expectedLifeSpanInDays = Integer.parseInt((row.get("expectedLifeSpanInDays")).toString());

      // Instantiate and add the specified item types to the WareFlow application
      new ItemType(name, expectedLifeSpanInDays, wareFlow);


    }
  }

  /**
   * Loads the input dataTable, instantiates and adds the given containers
   *
   * @param dataTable a data table with each container (and its corresponding specific information)
   */
  @Given("the following containers exist in the system \\(f11)")
  public void the_following_containers_exist_in_the_system_f11(
      io.cucumber.datatable.DataTable dataTable) {

    // Turns the dataTable into a list of lists (each row becomes a list).
    List<Map<String, Object>> tableList = dataTable.asMaps(String.class, Object.class);

    // Iterates through each list to create the specified container and add them to the WareFlow
    // application.
    for (Map<String, Object> row : tableList) {
      int containerNumber = Integer.parseInt(row.get("containerNumber").toString());
      String itemTypeName = (row.get("type")).toString();
      int areaNumber = Integer.parseInt(row.get("areaNumber").toString());
      int slotNumber = Integer.parseInt(row.get("slotNumber").toString());
      Date addedOnDate = Date.valueOf(row.get("addedOnDate").toString());

      // Instantiate and add the specified containers to the WareFlow application.
      ItemType itemType = ItemType.getWithName(itemTypeName);
      new ItemContainer(containerNumber, areaNumber, slotNumber, addedOnDate, wareFlow, itemType);


    }
  }

  /**
   * Loads the input dataTable, instantiates and adds the given shipment orders
   *
   * @param dataTable a data table for each order in the system (and its corresponding specific
   *        information)
   */
  @Given("the following orders exist in the system \\(f11)")
  public void the_following_orders_exist_in_the_system_f11(
      io.cucumber.datatable.DataTable dataTable) {

    // Turns the dataTable into a list of lists (each row becomes a list).
    List<Map<String, Object>> tableList = dataTable.asMaps(String.class, Object.class);

    // Iterates through each list to create the specified orders and add them to the WareFlow
    // application.
    for (Map<String, Object> row : tableList) {
      int orderID = Integer.parseInt(row.get("id").toString());
      String orderPlacer = (row.get("orderPlacer").toString());
      Date placedOnDate = Date.valueOf(row.get("placedOnDate").toString());
      String description = (row.get("description").toString());
      int containerNumber = Integer.parseInt(row.get("containerNumber").toString());
      int quantity = Integer.parseInt(row.get("quantity").toString());

      // Instantiate and add the specified shipment orders to the WareFlow application.

      User aUser = User.getWithUsername(orderPlacer);
      ItemContainer container = ItemContainer.getWithContainerNumber(containerNumber);
      ShipmentOrder order =
          new ShipmentOrder(orderID, placedOnDate, description, quantity, wareFlow, aUser);
      order.setContainer(container);



    }
  }

  /**
   * Loads the input dataTable, instantiates and adds the given shipment order notes
   *
   * @param dataTable a data table for each note in the system (and its corresponding specific
   *        information)
   */
  @Given("the following notes exist in the system \\(f11)")
  public void the_following_notes_exist_in_the_system_f11(
      io.cucumber.datatable.DataTable dataTable) {

    // Turns the dataTable into a list of lists (each row becomes a list).
    List<Map<String, Object>> tableList = dataTable.asMaps(String.class, Object.class);

    // Iterates through each list to create the specified orders and add them to the WareFlow
    // application.
    for (Map<String, Object> row : tableList) {
      String noteTaker = (row.get("noteTaker").toString());
      int orderID = Integer.parseInt(row.get("orderId").toString());
      Date addedOnDate = Date.valueOf(row.get("addedOnDate").toString());
      String description = (row.get("description").toString());

      // Instantiate and add the specified shipment order notes to the appropriate shipment
      // order.
      ShipmentOrder order = ShipmentOrder.getWithId(orderID);
      WarehouseStaff staff = (WarehouseStaff) WarehouseStaff.getWithUsername(noteTaker); // check
                                                                                         // this
      ShipmentNote note = new ShipmentNote(addedOnDate, description, order, staff);
      order.addShipmentNote(note);

    }
  }

  /**
   * Deletes the specified shipment note
   *
   * @param noteNumberInput the note number
   * @param orderNumberInput the shipment order
   */
  @When("the manger attempts to delete note number {string} for shipment order {string} \\(f11)")
  public void the_manger_attempts_to_delete_note_number_for_shipment_order_f11(
      String noteNumberInput, String orderNumberInput) {
    int noteNumber = Integer.parseInt(noteNumberInput);
    int orderNumber = Integer.parseInt(orderNumberInput);

    if (orderNumber < wareFlow.getOrders().size()) {
      ShipmentOrder order = ShipmentOrder.getWithId(orderNumber);
      if (noteNumber < order.getShipmentNotes().size()) {
        deletedNote = ShipmentOrder.getWithId(orderNumber).getShipmentNote(noteNumber);
      }
    }
    ShipmentNoteController.deleteShipmentNote(orderNumber, noteNumber);

  }

  /**
   * Checks that the number of shipment notes in the WareFlow application has updated
   *
   * @param expectedNumberInput number of notes in the system
   */
  @Then("the number of notes in the system shall be {string} \\(f11)")
  public void the_number_of_notes_in_the_system_shall_be_f11(String expectedNumberInput) {
    int expectedNumber = Integer.parseInt(expectedNumberInput);
    int numberOfShipmentNotes = 0;

    for (ShipmentOrder order : wareFlow.getOrders()) {
      numberOfShipmentNotes += order.numberOfShipmentNotes();
    }

    assertEquals(expectedNumber, numberOfShipmentNotes);
  }

  /**
   * Checks that the number of shipment notes for the specified shipment order has updated
   *
   * @param orderNumberInput order number
   * @param expectedNumberInput expected number of notes
   */
  @Then("the number of notes for order {string} in the system shall be {string} \\(f11)")
  public void the_number_of_notes_for_order_in_the_system_shall_be_f11(String orderNumberInput,
      String expectedNumberInput) {
    int orderNumber = Integer.parseInt(orderNumberInput);
    int expectedNumber = Integer.parseInt(expectedNumberInput);

    ShipmentOrder order = ShipmentOrder.getWithId(orderNumber);

    assertEquals(expectedNumber, order.numberOfShipmentNotes());

  }

  /**
   * Checks that the specified shipment note has been deleted from the ContainerPllus application
   *
   * @param noteNumberInput note number
   * @param orderNumberInput order number
   */
  @Then("the note number {string} for order {int} shall not exist in the system \\(f11)")
  public void the_note_number_for_order_shall_not_exist_in_the_system_f11(String noteNumberInput,
      Integer orderNumberInput) {

    int noteNumber = Integer.parseInt(noteNumberInput);
    int orderNumber = orderNumberInput;

    ShipmentOrder order = ShipmentOrder.getWithId(orderNumber);

    if (noteNumber < order.getShipmentNotes().size()) {
      assertFalse(deletedNote.equals(order.getShipmentNote(noteNumber)));
    }
  }
}
