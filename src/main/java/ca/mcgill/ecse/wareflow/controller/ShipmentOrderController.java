package ca.mcgill.ecse.wareflow.controller;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import ca.mcgill.ecse.wareflow.application.WareFlowApplication;
import ca.mcgill.ecse.wareflow.model.ItemContainer;
import ca.mcgill.ecse.wareflow.model.ShipmentNote;
import ca.mcgill.ecse.wareflow.model.ShipmentOrder;
import ca.mcgill.ecse.wareflow.model.User;
import ca.mcgill.ecse.wareflow.model.WareFlow;
import ca.mcgill.ecse.wareflow.persistence.WareflowPersistence;

public class ShipmentOrderController {

  // We need a global instance of the WareFlow class as it will be used across the file.
  private static WareFlow wareFlow = WareFlowApplication.getWareFlow();

  /**
   * @author Nicolas Saade, Mohamed Abdelhady
   * 
   * This method is used to add a new shipment order to the system. containerNumber -1 means
   * that no container is specified and quantity has to be -1 as well
   */
  public static String addShipmentOrder(int id, Date placedOnDate, String description,
      String username, Integer containerNumber, int quantity) {
    // Check if the order ID already exists
    if (ShipmentOrder.getWithId(id) != null) {
      return "Order id already exists";
    }

    // Validate the description
    if (description == null || description.trim().isEmpty()) {
      return "Order description cannot be empty";
    }

    // Validate the user
    User user = User.getWithUsername(username);
    if (user == null) {
      return "The order placer does not exist";
    }

    // Validate the container (if specified)
    ItemContainer container = null;

    // Assuming 0 is not a valid container number amd is used as a placeholder for "no container"
    if (containerNumber != null && containerNumber >= 0) { 
      container = ItemContainer.getWithContainerNumber(containerNumber);
      if (container == null) {
        return "The container does not exist";
      }
    }

    // Validate the quantity based on whether a container is specified
    if (container == null && quantity != 0) {
      return "Order quantity must 0 when container is not specified";
    } else if (container != null && quantity <= 0) {
      return "Order quantity must be larger than 0 when container is specified";
    }

    try {
      ShipmentOrder shipmentOrder =
          new ShipmentOrder(id, placedOnDate, description, quantity, wareFlow, user);
      if (container != null) {
        shipmentOrder.setContainer(container);
        WareflowPersistence.save();
      }
      wareFlow.addOrder(shipmentOrder);
      WareflowPersistence.save();
      return "";
    } catch (Exception e) {
      return "An error occurred: " + e.getMessage();
    }
  }

  /**
   * @author Nicolas Saade, Mohamed Abdelhady 
   * 
   * This method is used to update an existing shipment order in the system. newContainerNumber -1 means 
   * that no container is specified and newQuantity has to be -1 as well
   */
  public static String updateShipmentOrder(int id, Date newPlacedOnDate, String newDescription,
      String newUsername, int newContainerNumber, int newQuantity) {

    // Validate the user
    User user = User.getWithUsername(newUsername);
    if (user == null) {
      return "The order placer does not exist";
    }

    ItemContainer container = null;

    // Assuming 0 is not a valid container number and is used as a placeholder for "no container"
    if (newContainerNumber != -1 && newContainerNumber >= 0) {
      container = ItemContainer.getWithContainerNumber(newContainerNumber);
      if (container == null) {
        return "The container does not exist";
      }
    }

    // Validate the description
    if (newDescription == null || newDescription.trim().isEmpty()) {
      return "Order description cannot be empty";
    }

    // Validate the quantity based on whether a container is specified
    container = ItemContainer.getWithContainerNumber(newContainerNumber);
    if (container == null && newQuantity != 0) {
      return "Order quantity must 0 when container is not specified";
    } else if (container != null && newQuantity <= 0) {
      return "Order quantity must be larger than 0 when container is specified";
    }
    ShipmentOrder shipmentorder = ShipmentOrder.getWithId(id);
    // Attempts to update the shipment order
    try {
      shipmentorder.setPlacedOnDate(newPlacedOnDate);
      shipmentorder.setDescription(newDescription);
      shipmentorder.setOrderPlacer(user);
      shipmentorder.setContainer(container);
      shipmentorder.setQuantity(newQuantity);
      WareflowPersistence.save();
      return "";
    } catch (Exception e) {
      return "An error occurred: " + e.getMessage();
    }

  }

  /**
   * @author Nicolas Saade, Mohamed Abdelhady 
   * 
   * This method is used to delete an existing shipment order in the system.
   */
  public static void deleteShipmentOrder(int id) {
    ShipmentOrder shipment = ShipmentOrder.getWithId(id);
    if (shipment != null) {
      wareFlow.removeOrder(shipment);
      shipment.delete();
      WareflowPersistence.save();
    }
  }

  /**
   * @author Nicolas Saade, Mohamed Abdelhady
   * 
   * This method is used to get all the shipment orders in the system.
   */
  public static List<TOShipmentOrder> getOrders() {
	    List<ShipmentOrder> orders = wareFlow.getOrders();
	    List<TOShipmentOrder> shipmentOrders = new ArrayList<>();

	    if (orders == null || orders.isEmpty()) {
	        return shipmentOrders;
	    }

	    for (ShipmentOrder order : orders) {
	        String orderPlacer = order.getOrderPlacer().getName() != null ? order.getOrderPlacer().getName().toLowerCase() : "manager";
	        String processedBy = order.hasOrderPicker() ? order.getOrderPicker().getName() : null;
	        String timeToResolve = order.getTimeToFullfill() != null ? order.getTimeToFullfill().toString() : null;
	        String priority = order.getPriority() != null ? order.getPriority().toString() : null;
	        boolean approvalRequired = order.hasOrderApprover();

	        String itemName = null;
	        int expectedLifeSpanInDays = -1;
	        Date addedOnDate = null;
	        int areaNumber = -1, slotNumber = -1;
	        if (order.getContainer() != null) {
	            itemName = order.getContainer().getItemType().getName();
	            expectedLifeSpanInDays = order.getContainer().getItemType().getExpectedLifeSpanInDays();
	            addedOnDate = order.getContainer().getAddedOnDate();
	            areaNumber = order.getContainer().getAreaNumber();
	            slotNumber = order.getContainer().getSlotNumber();
	        }

	        List<ShipmentNote> shipmentNotes = order.getShipmentNotes();
	        TOShipmentNote[] toNotes = new TOShipmentNote[shipmentNotes.size()];
	        int index = 0;
	        for (ShipmentNote note : shipmentNotes) {
	            String noteTakerUsername = note.getNoteTaker().getName() != null ? note.getNoteTaker().getName().toLowerCase() : "manager";
	            toNotes[index++] = new TOShipmentNote(note.getDate(), note.getDescription(), noteTakerUsername);
	        }

	        TOShipmentOrder toShipmentOrder = new TOShipmentOrder(order.getId(), order.getQuantity(), order.getPlacedOnDate(), order.getDescription(), 
	                orderPlacer, order.getStatusFullName(), processedBy, timeToResolve, priority, approvalRequired, itemName, expectedLifeSpanInDays, 
	                addedOnDate, areaNumber, slotNumber, toNotes);
	        shipmentOrders.add(toShipmentOrder);
	        WareflowPersistence.save();
	    }
	    return shipmentOrders;
	}
}