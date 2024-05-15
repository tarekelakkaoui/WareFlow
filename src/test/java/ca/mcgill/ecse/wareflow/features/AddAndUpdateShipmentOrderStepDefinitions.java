package ca.mcgill.ecse.wareflow.features;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
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

public class AddAndUpdateShipmentOrderStepDefinitions {
  private WareFlow wareFlow = WareFlowApplication.getWareFlow();
  private String error;

  /**
   * @author Tiffany Miller
   * @param dataTable The data table of the employees that must exist in the system.
   */
  @Given("the following employees exist in the system \\(f12)")
  public void the_following_employees_exist_in_the_system_f12(
      io.cucumber.datatable.DataTable dataTable) {
    List<List<String>> rows = dataTable.asLists(String.class);
    for (int i = 1; i < rows.size(); i++) {
      List<String> row = rows.get(i);
      String username = row.get(0);
      String password = row.get(1);
      String name = row.get(2);
      String phoneNumber = row.get(3);
      wareFlow.addEmployee(username, name, password, phoneNumber);
    }
    error = "";

  }

  /**
   * @author Tiffany Miller
   * @param dataTable The data table of the manager that must exist in the system.
   */
  @Given("the following manager exists in the system \\(f12)")
  public void the_following_manager_exists_in_the_system_f12(
      io.cucumber.datatable.DataTable dataTable) {
    List<Map<String, String>> rows = dataTable.asMaps();
    for (int i = 0; i < rows.size(); i++) {
      Map<String, String> row = rows.get(i);
      String username = row.get("username");
      String password = row.get("password");
      Manager manager = new Manager(username, "manager", password, "", wareFlow);
      wareFlow.setManager(manager);
    }
    error = "";

  }

  /**
   * @author Tiffany Miller
   * @param dataTable The data table of the clients that must exist in the system.
   */
  @Given("the following clients exist in the system \\(f12)")
  public void the_following_clients_exist_in_the_system_f12(
      io.cucumber.datatable.DataTable dataTable) {
    List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
    for (int i = 0; i < rows.size(); i++) {
      Map<String, String> row = rows.get(i);
      String username = row.get("username");
      String password = row.get("password");
      String name = row.get("name");
      String phoneNumber = row.get("phoneNumber");
      String address = row.get("address");
      wareFlow.addClient(username, name, password, phoneNumber, address);
    }
    error = "";

  }

  /**
   * @author Tiffany Miller
   * @param dataTable The data table of the item types that must exist in the system.
   */
  @Given("the following item types exist in the system \\(f12)")
  public void the_following_item_types_exist_in_the_system_f12(
      io.cucumber.datatable.DataTable dataTable) {
    List<Map<String, String>> rows = dataTable.asMaps();
    for (int i = 0; i < rows.size(); i++) {
      Map<String, String> row = rows.get(i);
      String name = row.get("name");
      int lifeSpan = Integer.parseInt(row.get("expectedLifeSpanInDays"));
      wareFlow.addItemType(name, lifeSpan);
    }
    error = "";

  }

  /**
   * @author Tiffany Miller
   * @param dataTable The data table of the containers that must exist in the system.
   */
  @Given("the following containers exist in the system \\(f12)")
  public void the_following_containers_exist_in_the_system_f12(
      io.cucumber.datatable.DataTable dataTable) {
    List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
    for (int i = 0; i < rows.size(); i++) {
      Map<String, String> row = rows.get(i);
      int containerNumber = Integer.parseInt(row.get("containerNumber"));
      int areaNumber = Integer.parseInt(row.get("areaNumber"));
      int slotNumber = Integer.parseInt(row.get("slotNumber"));
      Date addedOnDate = Date.valueOf(row.get("addedOnDate"));
      ItemType type = ItemType.getWithName(row.get("type"));
      wareFlow.addItemContainer(containerNumber, areaNumber, slotNumber, addedOnDate, type);
    }
    error = "";
  }

  /**
   * @author Tiffany Miller
   * @param dataTable The data table of the orders that must exist in the system.
   */
  @Given("the following orders exist in the system \\(f12)")
  public void the_following_orders_exist_in_the_system_f12(
      io.cucumber.datatable.DataTable dataTable) {
    List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
    for (int i = 0; i < rows.size(); i++) {
      Map<String, String> row = rows.get(i);
      int id = Integer.parseInt(row.get("id"));
      Date placedOnDate = Date.valueOf(row.get("placedOnDate"));
      String description = row.get("description");
      String orderPlacer = row.get("orderPlacer");
      int containerNumber = Integer.parseInt(row.get("containerNumber"));
      int quantity = Integer.parseInt(row.get("quantity"));

      User placerUser = User.getWithUsername(orderPlacer);
      ItemContainer container = ItemContainer.getWithContainerNumber(containerNumber);
      wareFlow.addOrder(id, placedOnDate, description, quantity, placerUser);
      ShipmentOrder thisOrder = ShipmentOrder.getWithId(id);
      thisOrder.setContainer(container);
    }
    error = "";
  }

  /**
   * @author Tiffany Miller
   * @param username The username of the user that wants to add a shipment order.
   * @param idString The new shipment order id.
   * @param dateString The new shipment order date
   * @param description The new shipment order description.
   * @param containerNumberString The new shipment order's container number.
   */
  @When("the user with username {string} attempts to add a new shipment order to the system with id {string}, date {string}, description {string}, quantity {string}, and container number {string} \\(f12)")
  public void the_user_with_username_attempts_to_add_a_new_maintenance_order_to_the_system_with_id_date_description_quantity_and_container_number_f12(
      String username, String idString, String dateString, String description,
      String quantityString, String containerNumberString) {

    int id = Integer.parseInt(idString);
    Date date = Date.valueOf(dateString);

    int containerNumber =
        containerNumberString.length() > 0 ? Integer.parseInt(containerNumberString) : -1;
    int quantity = Integer.parseInt(quantityString);

    callController(ShipmentOrderController.addShipmentOrder(id, date, description, username,
        containerNumber, quantity));

  }

  /**
   * @author Tiffany Miller
   * @param username The username of the user that wants to add a shipment order.
   * @param idString The new shipment order id.
   * @param dateString The new shipment order date
   * @param description The new shipment order description.
   */
  @When("the user with username {string} attempts to add a new shipment order to the system with id {string}, date {string}, description {string}, and quantity {string} but no container number \\(f12)")
  public void the_user_with_username_attempts_to_add_a_new_maintenance_order_to_the_system_with_id_date_and_description_quantity_but_no_container_number_f12(
      String username, String idString, String dateString, String description,
      String quantityString) {

    int id = Integer.parseInt(idString);
    Date date = Date.valueOf(dateString);
    int containerNumber = -1;
    int quantity = Integer.parseInt(quantityString);

    callController(ShipmentOrderController.addShipmentOrder(id, date, description, username,
        containerNumber, quantity));

  }

  /**
   * @author Tiffany Miller
   * @param idString The id of the shipment order the manager wants to update.
   * @param username Updated shipment order placer.
   * @param dateString Updated shipment order date.
   * @param description Updated shipment order container description.
   */
  @When("the manager attempts to update the shipment order with id {string} to order placer {string}, date {string}, description {string}, and quantity {string} but no container number \\(f12)")
  public void the_manager_attempts_to_update_the_maintenance_order_with_id_to_order_placer_date_and_description_but_no_container_number_f12(
      String idString, String username, String dateString, String description,
      String quantityString) {

    int id = Integer.parseInt(idString);
    Date date = Date.valueOf(dateString);
    int containerNumber = -1;
    int quantity = Integer.parseInt(quantityString);

    callController(ShipmentOrderController.updateShipmentOrder(id, date, description, username,
        containerNumber, quantity));

  }

  /**
   * @author Tiffany Miller
   * @param idString The id of the shipment order the manager wants to update.
   * @param username Updated shipment order placer.
   * @param dateString Updated shipment order date.
   * @param description Updated shipment order description.
   * @param containerNumberString Updated shipment order container number.
   */
  @When("the manager attempts to update the shipment order with id {string} to order placer {string}, date {string}, description {string}, quantity {string}, and container number {string} \\(f12)")
  public void the_manager_attempts_to_update_the_maintenance_order_with_id_to_order_placer_date_description_quantity_and_container_number_f12(
      String idString, String username, String dateString, String description,
      String quantityString, String containerNumberString) {

    int id = Integer.parseInt(idString);
    Date date = Date.valueOf(dateString);
    int containerNumber =
        containerNumberString.length() > 0 ? Integer.parseInt(containerNumberString) : -1;
    int quantity = Integer.parseInt(quantityString);

    callController(ShipmentOrderController.updateShipmentOrder(id, date, description, username,
        containerNumber, quantity));
  }

  /**
   * @author Tiffany Miller
   * @param expectedNumOrdersString The number of shipment orders that is expected in the system.
   */
  @Then("the number of orders in the system shall be {string} \\(f12)")
  public void the_number_of_orders_in_the_system_shall_be_f12(String expectedNumOrdersString) {
    int numberOfOrders = wareFlow.getOrders().size();
    int expectedNumOrders = Integer.parseInt(expectedNumOrdersString);
    assertEquals(expectedNumOrders, numberOfOrders);

  }

  /**
   * @author Tiffany Miller
   * @param dataTable Data table of shipment orders that is expected in the system.
   */
  @Then("the following orders shall exist in the system \\(f12)")
  public void the_following_orders_shall_exist_in_the_system_f12(
      io.cucumber.datatable.DataTable dataTable) {

    Map<String, List<String>> expectedOrdersMap = new HashMap<>();
    Map<String, List<String>> actualOrdersMap = new HashMap<>();

    List<List<String>> rows = dataTable.asLists(String.class);

    for (int i = 1; i < rows.size(); i++) {
      List<String> row = rows.get(i);
      String id = row.get(0);
      expectedOrdersMap.put(id, row);
    }

    List<ShipmentOrder> shipmentOrders = wareFlow.getOrders();

    for (var order : shipmentOrders) {
      List<String> oneOrder = new ArrayList<>();
      String id = String.valueOf(order.getId());
      String orderPlacer = order.getOrderPlacer().getUsername();
      String placedOnDate = order.getPlacedOnDate().toString();
      String description = order.getDescription();
      String containerNumber = String.valueOf(order.getContainer().getContainerNumber());
      String quantity = String.valueOf(order.getQuantity());

      oneOrder.add(id);
      oneOrder.add(orderPlacer);
      oneOrder.add(placedOnDate);
      oneOrder.add(description);
      oneOrder.add(containerNumber);
      oneOrder.add(quantity);

      actualOrdersMap.put(id, oneOrder);
    }

    assertEquals(expectedOrdersMap.size(), actualOrdersMap.size());
    assertEquals(expectedOrdersMap, actualOrdersMap);
  }

  /**
   * @author Tiffany Miller
   * @param username The username of the person who raised the order.
   * @param idString The id of the new shipment order raised.
   * @param dateString The date of the new shipment order raised.
   * @param description The description of the new shipment order raised.
   */
  @Then("the order raised by {string} and with id {string}, date {string}, description {string}, and quantity {string} but no container shall exist in the system \\(f12)")
  public void the_order_raised_by_and_with_id_date_description_and_quantity_but_no_container_shall_exist_in_the_system_f12(
      String username, String idString, String dateString, String description,
      String quantityString) {

    User orderPlacer = User.getWithUsername(username);
    int id = Integer.valueOf(idString);
    Date date = Date.valueOf(dateString);
    int quantity = Integer.valueOf(quantityString);

    ShipmentOrder thisOrder = ShipmentOrder.getWithId(id);
    assertEquals(orderPlacer, thisOrder.getOrderPlacer());
    assertEquals(date, thisOrder.getPlacedOnDate());
    assertEquals(description, thisOrder.getDescription());
    assertEquals(quantity, thisOrder.getQuantity());
    assertTrue(!thisOrder.hasContainer());

  }

  /**
   * @author Tiffany Miller
   * @param username The username of the person who raised the order.
   * @param idString The id of the new shipment order raised.
   * @param dateString The date of the new shipment order raised.
   * @param description The description of the new shipment order raised.
   * @param containerNumberString The container number associated to the new shipment order raised.
   */
  @Then("the order raised by {string} and with id {string}, date {string}, description {string}, quantity {string}, and container number {string} shall exist in the system \\(f12)")
  public void the_order_raised_by_and_with_id_date_description_quantity_and_container_number_shall_exist_in_the_system_f12(
      String username, String idString, String dateString, String description,
      String quantityString, String containerNumberString) {

    User orderPlacer = User.getWithUsername(username);
    int id = Integer.valueOf(idString);
    Date date = Date.valueOf(dateString);
    int containerNumber = Integer.valueOf(containerNumberString);
    int quantity = Integer.valueOf(quantityString);
    ItemContainer container = ItemContainer.getWithContainerNumber(containerNumber);

    ShipmentOrder thisOrder = ShipmentOrder.getWithId(id);
    assertEquals(orderPlacer, thisOrder.getOrderPlacer());
    assertEquals(date, thisOrder.getPlacedOnDate());
    assertEquals(description, thisOrder.getDescription());
    assertEquals(quantity, thisOrder.getQuantity());
    assertEquals(container, thisOrder.getContainer());
  }

  /**
   * @author Tiffany Miller
   * @param string The error string that should be raised.
   */
  @Then("the system shall raise the error {string} \\(f12)")
  public void the_system_shall_raise_the_error_f12(String string) {
    assertEquals(string, error);
  }

  /**
   * @author Tiffany Miller
   * @param result The error result after calling controller method.
   */
  private void callController(String result) {
    if (!result.isEmpty()) {
      error += result;
    }
  }
}
