package ca.mcgill.ecse.wareflow.controller;

import java.sql.Date;
import java.util.List;
import ca.mcgill.ecse.wareflow.model.ShipmentOrder;
import ca.mcgill.ecse.wareflow.model.ShipmentOrder.PriorityLevel;
import ca.mcgill.ecse.wareflow.model.ShipmentOrder.TimeEstimate;
import ca.mcgill.ecse.wareflow.model.WarehouseStaff;
import ca.mcgill.ecse.wareflow.persistence.WareflowPersistence;

public class WareFlowStatesController {

  // private static WareFlow wareFlow = WareFlowApplication.getWareFlow();

  /**
   * Starts an order.
   * 
   * @author Ahmed Mossa
   * @param string the orderId of a specific order in the system.
   */
  public static String startOrder(String orderID) {
    Integer orderIDInteger = Integer.parseInt(orderID);
    ShipmentOrder order = ShipmentOrder.getWithId(orderIDInteger);
    if (order == null) {
      return "shipment order does not exist.";
    }
    try {
      order.startOrder();
      WareflowPersistence.save();
    } catch (Exception e) {
      return e.getMessage();
    }
    return "";
  }

  /**
   * Assigns an order to an employee.
   * 
   * @author Ahmed Mossa
   * @param string the orderId of a specific order in the system.
   * @param string2 the employeeId of the employee to assign the order to.
   * @param string3 the estimated time to complete the order.
   * @param string4 the priority level of the order.
   * @param string5 whether the order requires approval or not.
   */
  public static String assignOrder(String orderID, String employeeID, String estimatedTime,
      String priority, String requiresApproval) {
    Integer orderIDInteger = Integer.parseInt(orderID);
    ShipmentOrder order = ShipmentOrder.getWithId(orderIDInteger);
    if (order == null) {
      return "shipment order does not exist.";
    }
    WarehouseStaff employeeIDInteger = (WarehouseStaff) WarehouseStaff.getWithUsername(employeeID);
    TimeEstimate estimatedTimeInteger = TimeEstimate.valueOf(estimatedTime);
    PriorityLevel priorityInteger = PriorityLevel.valueOf(priority);
    Boolean requiresApprovalBoolean = Boolean.parseBoolean(requiresApproval);
    try {
      order.assignOrder(employeeIDInteger, estimatedTimeInteger, priorityInteger,
          requiresApprovalBoolean);
      WareflowPersistence.save();
    } catch (Exception e) {
      return e.getMessage();
    }
    return "";
  }

  /**
   * Completes an order.
   * 
   * @author Ahmed Mossa
   * @param string the orderId of a specific order in the system.
   */
  public static String completeOrder(String orderID) {
    Integer orderIDInteger = Integer.parseInt(orderID);
    ShipmentOrder order = ShipmentOrder.getWithId(orderIDInteger);
    if (order == null) {
      return "shipment order does not exist.";
    }
    try {
      order.completeOrder();
      WareflowPersistence.save();
    } catch (Exception e) {
      return e.getMessage();
    }
    return "";
  }

  /**
   * Disapproves an order.
   * 
   * @author Ahmed Mossa
   * @param string the orderId of a specific order in the system.
   * @param string2 the note to add to the order.
   * @param string3 the note taker's username.
   */
  public static String disapproveOrder(String orderID, String disapprovalDate,
      String disapprovalReason) {
    Integer orderIDInteger = Integer.parseInt(orderID);
    Date disapprovalDateActual = Date.valueOf(disapprovalDate);
    ShipmentOrder order = ShipmentOrder.getWithId(orderIDInteger);
    if (order == null) {
      return "shipment order does not exist.";
    }
    try {
      order.disapproveOrder(disapprovalDateActual, disapprovalReason);
      WareflowPersistence.save();
    } catch (Exception e) {
      return e.getMessage();
    }
    return "";
  }

  /**
   * Approves an order.
   * 
   * @author Ahmed Mossa
   * @param string the orderId of a specific order in the system.
   */
  public static String approveOrder(String orderID) {
    Integer orderIDInteger = Integer.parseInt(orderID);
    ShipmentOrder order = ShipmentOrder.getWithId(orderIDInteger);
    if (order == null) {
      return "shipment order does not exist.";
    }
    try {
      order.approveShipment();
      WareflowPersistence.save();
    } catch (Exception e) {
      return e.getMessage();
    }
    return "";
  }

  /**
   * View all shipment orders in the system.
   * 
   * @author Ahmed Mossa
   */
  public static List<TOShipmentOrder> viewAllShipmentOrder() {
    List<TOShipmentOrder> orders = ShipmentOrderController.getOrders();
    return orders;
  }
}
