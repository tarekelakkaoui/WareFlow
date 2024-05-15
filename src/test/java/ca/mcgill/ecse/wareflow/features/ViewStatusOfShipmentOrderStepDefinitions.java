package ca.mcgill.ecse.wareflow.features;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import java.sql.Date;
import java.util.List;
import java.util.Map;
import ca.mcgill.ecse.wareflow.application.WareFlowApplication;
import ca.mcgill.ecse.wareflow.controller.ShipmentOrderController;
import ca.mcgill.ecse.wareflow.controller.TOShipmentNote;
import ca.mcgill.ecse.wareflow.controller.TOShipmentOrder;
import ca.mcgill.ecse.wareflow.model.ItemContainer;
import ca.mcgill.ecse.wareflow.model.ItemType;
import ca.mcgill.ecse.wareflow.model.Manager;
import ca.mcgill.ecse.wareflow.model.ShipmentOrder;
import ca.mcgill.ecse.wareflow.model.User;
import ca.mcgill.ecse.wareflow.model.WareFlow;
import ca.mcgill.ecse.wareflow.model.WarehouseStaff;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class ViewStatusOfShipmentOrderStepDefinitions {
  private WareFlow wareFlow = WareFlowApplication.getWareFlow();
  private List<TOShipmentOrder> orders;

  /**
   * Gherkin step definition method to create and add employees to the WareFlow application.
   *
   * @author Mohamed Abdelrahman
   * @author Anders Woodruff
   * @author Philippe Aprahamian
   * @author David Marji
   * @author Ming Xuan Yue
   * @author Manuel Hanna
   * @param dataTable Cucumber DataTable containing the username, password, name and phoneNumber of
   *        the employees that must exist in the system.
   */
  @Given("the following employees exist in the system \\(f14)")
  public void the_following_employees_exist_in_the_system_f14(
      io.cucumber.datatable.DataTable dataTable) {
    List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
    for (Map<String, String> row : rows) {
      wareFlow.addEmployee(row.get("username"), row.get("name"), row.get("password"),
          row.get("phoneNumber"));
    }
  }

  /**
   * Gherkin step definition method to create and add a manager to the WareFlow application.
   * 
   * @author Manuel Hanna
   * @author Philippe Aprahamian
   * @author Ming Xuan Yue
   * @param dataTable Cucumber DataTable containing the username and password of the manager that
   *        must exist in the system.
   */
  @Given("the following manager exists in the system \\(f14)")
  public void the_following_manager_exists_in_the_system_f14(
      io.cucumber.datatable.DataTable dataTable) {
    List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
    Manager manager =
        new Manager(rows.get(0).get("username"), null, rows.get(0).get("password"), "", wareFlow);
    wareFlow.setManager(manager);
  }

  /**
   * Gherkin step definition method to create and add item types to the WareFlow application.
   *
   * @author Anders Woodruff
   * @author Philippe Aprahamian
   * @author Ming Xuan Yue
   * @param dataTable Cucumber DataTable containing the name and expectedLifeSpanInDays of the item
   *        types that must exist in the system.
   */
  @Given("the following item types exist in the system \\(f14)")
  public void the_following_container_types_exist_in_the_system_f14(
      io.cucumber.datatable.DataTable dataTable) {
    List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
    for (Map<String, String> row : rows) {
      wareFlow.addItemType(row.get("name"), Integer.parseInt(row.get("expectedLifeSpanInDays")));
    }
  }

  /**
   * Gherkin step definition method to create and add containers to the WareFlow application.
   * 
   * @author Ming Xuan Yue
   * @author David Marji
   * @author Philippe Aprahamian
   * @param dataTable Cucumber DataTable containing the containerNumber, type, addedOnDate,
   *        areaNumber and slotNumber of the containers that must exist in the system
   */
  @Given("the following containers exist in the system \\(f14)")
  public void the_following_containers_exist_in_the_system_f14(
      io.cucumber.datatable.DataTable dataTable) {
    List<Map<String, String>> rows = dataTable.asMaps();
    for (var row : rows) {
      int containerNumber = Integer.parseInt(row.get("containerNumber"));
      Date addedOnDate = Date.valueOf(row.get("addedOnDate"));
      int areaNumber = Integer.parseInt(row.get("areaNumber"));
      int slotNumber = Integer.parseInt(row.get("slotNumber"));
      ItemType type = ItemType.getWithName(row.get("type"));
      wareFlow.addItemContainer(containerNumber, areaNumber, slotNumber, addedOnDate, type);
    }
  }

  /**
   * Gherkin step definition method to create and add orders to the WareFlow application.
   * 
   * @author Ming Xuan Yue
   * @author Philippe Aprahamian
   * @author Mohamed Abdelrahman
   * @param dataTable Cucumber DataTable containing the id, orderPlacer, addedOnDate and description
   *        of the orders that must exist in the system.
   */
  @Given("the following orders exist in the system \\(f14)")
  public void the_following_orders_exist_in_the_system_f14(
      io.cucumber.datatable.DataTable dataTable) {
    List<Map<String, String>> rows = dataTable.asMaps();
    for (var row : rows) {
      int id = Integer.parseInt(row.get("id"));
      Date placedOnDate = Date.valueOf(row.get("placedOnDate"));
      String description = row.get("description");
      String orderPlacerUsername = row.get("orderPlacer");
      String containerNumberStr = row.get("containerNumber");
      int quantity = Integer.parseInt(row.get("quantity"));

      ShipmentOrder newOrder = wareFlow.addOrder(id, placedOnDate, description, quantity,
          User.getWithUsername(orderPlacerUsername));
      if (containerNumberStr != null) {
        int containerNumber = Integer.parseInt(containerNumberStr);
        newOrder.setContainer(ItemContainer.getWithContainerNumber(containerNumber));
      }
    }
  }

  /**
   * Gherkin step definition method to create and add order notes to the WareFlow application.
   * 
   * @author Ming Xuan Yue
   * @author Philippe Aprahamian
   * @param dataTable Cucumber DataTable containing the noteTaker, orderId, date and description of
   *        the notes that must exist in the system.
   */
  @Given("the following notes exist in the system \\(f14)")
  public void the_following_notes_exist_in_the_system_f14(
      io.cucumber.datatable.DataTable dataTable) {
    List<Map<String, String>> rows = dataTable.asMaps();
    for (var row : rows) {
      String noteTaker = row.get("noteTaker");
      int orderID = Integer.parseInt(row.get("orderId"));
      Date date = Date.valueOf(row.get("date"));
      String description = row.get("description");
      ShipmentOrder order = ShipmentOrder.getWithId(orderID);
      order.addShipmentNote(date, description,
          (WarehouseStaff) WarehouseStaff.getWithUsername(noteTaker));
    }
  }

  /**
   * Gherkin step definition method to test controller 6 by getting orders that exist in the system.
   * 
   * @author David Marji
   * @author Manuel Hanna
   * @author Mohamed Abdelrahman
   */
  @When("the manager attempts to view all shipment orders in the system \\(f14)")
  public void the_manager_attempts_to_view_all_shipment_orders_in_the_system_f14() {
    orders = ShipmentOrderController.getOrders();
  }

  /**
   * Gherkin step definition method to ensure the information of the orders obtained by the
   * controller method 6 is the same as the information in the datatable.
   * 
   * @author David Marji
   * @author Philippe Aprahamian
   * @author Mohamed Abdelrahman
   * @author Ming Xuan Yue
   * @param dataTable Cucumber DataTable containing the id, orderPlacer, placedOnDate, description,
   *        containerName, expectedLifeSpanInDays, addedOnDate, areaNumber and slotNumber of the
   *        orders shown.
   * 
   */
  @Then("the following shipment orders shall be presented \\(f14)")
  public void the_following_shipment_orders_shall_be_presented_f14(
      io.cucumber.datatable.DataTable dataTable) {
    List<Map<String, String>> rows = dataTable.asMaps();
    int i = 0;
    for (var row : rows) {
      TOShipmentOrder currOrder = orders.get(i);
      int id = Integer.parseInt(row.get("id"));
      assertEquals(id, currOrder.getId());
      String orderPlacerUsername = row.get("orderPlacer");
      assertEquals(orderPlacerUsername, currOrder.getOrderPlacer());
      Date placedOnDate = Date.valueOf(row.get("placedOnDate"));
      assertEquals(placedOnDate, currOrder.getPlacedOnDate());
      String description = row.get("description");
      assertEquals(description, currOrder.getDescription());
      String itemName = row.get("itemName");
      assertEquals(itemName, currOrder.getItemName());
      String expectLifeSpanStr = row.get("expectedLifeSpanInDays");
      int expectLifeSpan = -1;
      if (expectLifeSpanStr != null) {
        expectLifeSpan = Integer.parseInt(expectLifeSpanStr);
      }
      assertEquals(expectLifeSpan, currOrder.getExpectedLifeSpanInDays());
      String addedOnDateStr = row.get("addedOnDate");
      Date addedOnDate = null;
      if (addedOnDateStr != null) {
        addedOnDate = Date.valueOf(addedOnDateStr);
      }
      assertEquals(addedOnDate, currOrder.getAddedOnDate());
      String areaNumberStr = row.get("areaNumber");
      int areaNumber = -1;
      if (areaNumberStr != null) {
        areaNumber = Integer.parseInt(areaNumberStr);
      }
      assertEquals(areaNumber, currOrder.getAreaNumber());
      String slotNumberStr = row.get("slotNumber");
      int slotNumber = -1;
      if (slotNumberStr != null) {
        slotNumber = Integer.parseInt(slotNumberStr);
      }
      assertEquals(slotNumber, currOrder.getSlotNumber());
      i++;
    }
  }

  /**
   * Gherkin step definition method to ensure that information of the notes of the order obtained by
   * the controller method 6 is the same as the information in the datatable.
   * 
   * @author David Marji
   * @author Erik Cupsa
   * @author Philippe Aprahamian
   * @author Mohamed Abdelrahman
   * @author Ming Xuan Yue
   * @param string the orderId of a specific order in the system.
   * @param dataTable Cucumber DataTable containing the noteTaker, addedOnDate and description of
   *        the notes of the order with the provided orderId.
   */
  @Then("the order with id {string} shall have the following notes \\(f14)")
  public void the_order_with_id_shall_have_the_following_notes_f14(String string,
      io.cucumber.datatable.DataTable dataTable) {
    int orderID = Integer.parseInt(string);
    TOShipmentOrder currOrder = null;
    for (var order : orders) {
      if (order.getId() == orderID) {
        currOrder = order;
      }
    }

    assertNotNull(currOrder);

    List<TOShipmentNote> currShipmentNotes = currOrder.getNotes();
    List<Map<String, String>> rows = dataTable.asMaps();
    int i = 0;
    for (var row : rows) {
      TOShipmentNote currNote = currShipmentNotes.get(i);
      String noteTaker = row.get("noteTaker");
      assertEquals(noteTaker, currNote.getNoteTakerUsername());
      Date date = Date.valueOf(row.get("date"));
      assertEquals(date, currNote.getDate());
      String description = row.get("description");
      assertEquals(description, currNote.getDescription());
      i++;
    }
  }

  /**
   * Gherkin step definition method to ensure that the order obtained by controller 6 has no notes
   * in the system.
   * 
   * @author Erik Cupsa
   * @author Philippe Aprahamian
   * @param string the orderId of a specific order in the system.
   */
  @Then("the order with id {string} shall have no notes \\(f14)")
  public void the_order_with_id_shall_have_no_notes_f14(String string) {
    int orderID = Integer.parseInt(string);
    TOShipmentOrder currOrder = null;
    for (var order : orders) {
      if (order.getId() == orderID) {
        currOrder = order;
      }
    }

    assertNotNull(currOrder);
    assertEquals(currOrder.hasNotes(), false);
  }
}
