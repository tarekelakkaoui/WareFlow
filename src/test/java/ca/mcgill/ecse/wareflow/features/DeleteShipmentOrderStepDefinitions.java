package ca.mcgill.ecse.wareflow.features;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.sql.Date;
import java.util.List;
import java.util.Map;
import ca.mcgill.ecse.wareflow.application.WareFlowApplication;
import ca.mcgill.ecse.wareflow.controller.ShipmentOrderController;
import ca.mcgill.ecse.wareflow.model.ItemContainer;
import ca.mcgill.ecse.wareflow.model.ItemType;
import ca.mcgill.ecse.wareflow.model.Manager;
import ca.mcgill.ecse.wareflow.model.ShipmentOrder;
import ca.mcgill.ecse.wareflow.model.User;
import ca.mcgill.ecse.wareflow.model.WareFlow;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class DeleteShipmentOrderStepDefinitions {
  WareFlow wareFlow = WareFlowApplication.getWareFlow();

  /**
   * Adding employees given in the first @Given clause in Gherkin feature file.
   *
   * @author Isbat-ul Islam
   * @param employees DataTable containing info on the employees we wish to test on
   */
  @Given("the following employees exist in the system \\(f13)")
  public void the_following_employees_exist_in_the_system_f13(
      io.cucumber.datatable.DataTable employees) {
    List<Map<String, String>> rows = employees.asMaps();
    for (var row : rows) {
      String username = row.get("username");
      String password = row.get("password");
      String name = row.get("name");
      String phoneNumber = row.get("phoneNumber");
      wareFlow.addEmployee(username, name, password, phoneNumber);
    }
  }

  /**
   * Adding Manager from the feature file.
   *
   * @author Isbat-ul Islam
   * @param manager DataTable containing info on the Manager we wish to test on
   */
  @Given("the following manager exists in the system \\(f13)")
  public void the_following_manager_exists_in_the_system_f13(
      io.cucumber.datatable.DataTable manager) {

    List<Map<String, String>> rows = manager.asMaps();
    for (var row : rows) {
      String aUsername = row.get("username");
      String aPassword = row.get("password");
      String aName = "";
      String aPhoneNumber = "";
      // No need of setManager since Manager constructor associates Manager to WareFlow
      new Manager(aUsername, aName, aPassword, aPhoneNumber, wareFlow);
    }
  }

  /**
   * Adds item types specified in the Gherkin Feature file.
   *
   * @author Isbat-ul Islam
   * @param itemTypes DataTable containing info on the ItemTypes we wish to test on
   */
  @Given("the following item types exist in the system \\(f13)")
  public void the_following_container_types_exist_in_the_system_f13(
      io.cucumber.datatable.DataTable itemTypes) {

    List<Map<String, String>> rows = itemTypes.asMaps();
    for (var row : rows) {
      String aName = row.get("name");
      Integer aExpectedLifeSpanInDays = Integer.parseInt(row.get("expectedLifeSpanInDays"));
      wareFlow.addItemType(aName, aExpectedLifeSpanInDays);
    }
  }

  /**
   * Adds specific containers defined in Gherkin feature file.
   *
   * @author Isbat-ul Islam
   * @param itemContainers DataTable containing info on the ItemContainers we wish to test on
   */
  @Given("the following containers exist in the system \\(f13)")
  public void the_following_containers_exist_in_the_system_f13(
      io.cucumber.datatable.DataTable itemContainers) {

    List<Map<String, String>> rows = itemContainers.asMaps(String.class, String.class);
    for (var row : rows) {
      Integer aContainerNumber = Integer.parseInt(row.get("containerNumber"));
      ItemType aItemType = ItemType.getWithName(row.get("type"));
      Date aDate = Date.valueOf(row.get("addedOnDate"));
      Integer aAreaNumber = Integer.parseInt(row.get("areaNumber"));
      Integer aSlotNumber = Integer.parseInt(row.get("slotNumber"));
      wareFlow.addItemContainer(aContainerNumber, aAreaNumber, aSlotNumber, aDate, aItemType);
    }
  }

  /**
   * Add specified Orders defined in feature file.
   *
   * @author Isbat-ul Islam
   * @param shipmentOrders DataTable containing info on the ShipmentOrders we wish to test on
   */
  @Given("the following orders exist in the system \\(f13)")
  public void the_following_orders_exist_in_the_system_f13(
      io.cucumber.datatable.DataTable shipmentOrders) {
    List<Map<String, String>> rows = shipmentOrders.asMaps();
    for (var row : rows) {
      Integer aID = Integer.parseInt(row.get("id"));
      String aOrderPlacerUsername = row.get("orderPlacer");
      Date dateRaised = Date.valueOf(row.get("placedOnDate"));
      String description = row.get("description");
      Integer containerID = Integer.parseInt(row.get("containerNumber"));
      Integer quantity = Integer.parseInt(row.get("quantity"));

      ShipmentOrder order = new ShipmentOrder(aID, dateRaised, description, quantity, wareFlow,
          User.getWithUsername(aOrderPlacerUsername));
      order.setContainer(ItemContainer.getWithContainerNumber(containerID));
    }
  }

  /**
   * Delete order with orderID "string".
   *
   * @author Neil Joe George
   * @param orderId The ID of the order we are trying to delete
   */
  @When("the manager attempts to delete the shipment order with id {string} \\(f13)")
  public void the_manager_attempts_to_delete_the_shipment_order_with_id_f13(String orderId) {
    int aID = Integer.parseInt(orderId);
    ShipmentOrderController.deleteShipmentOrder(aID);
  }

  /**
   * Check if the shipment orders saved in the system are correct
   *
   * @author Mathieu Pestel
   * @author Neil Joe George
   * @param shipmentOrders DataTable containing info on the expected ShipmentOrders in the system
   */
  @Then("the following orders shall exist in the system \\(f13)")
  public void the_following_orders_shall_exist_in_the_system_f13(
      io.cucumber.datatable.DataTable shipmentOrders) {
    List<Map<String, String>> orderTable = shipmentOrders.asMaps(String.class, String.class);
    for (Map<String, String> order : orderTable) {
      int orderID = Integer.parseInt(order.get("id"));
      ShipmentOrder currentOrder = ShipmentOrder.getWithId(orderID);
      Date currentDate = Date.valueOf(order.get("placedOnDate"));
      String currentDescription = currentOrder.getDescription();
      int currentContainerNumber = currentOrder.getContainer().getContainerNumber();
      int currentQuantity = currentOrder.getQuantity();

      assertEquals(currentDate, currentOrder.getPlacedOnDate());
      assertEquals(currentOrder.getId(), Integer.parseInt(order.get("id")));
      assertEquals(order.get("description"), currentDescription);
      assertEquals(Integer.parseInt(order.get("quantity")), currentQuantity);
      assertEquals(Integer.parseInt(order.get("containerNumber")), currentContainerNumber);
    }
  }

  /**
   * Check if the number of shipment orders in the system is correct
   *
   * @author Mathieu Pestel
   * @param numOrders The amount of orders we expect to be in the system.
   */
  @Then("the number of shipment orders in the system shall be {string} \\(f13)")
  public void the_number_of_shipment_orders_in_the_system_shall_be_f13(String numOrders) {
    assertEquals(Integer.parseInt(numOrders), wareFlow.getOrders().size());
  }
}
