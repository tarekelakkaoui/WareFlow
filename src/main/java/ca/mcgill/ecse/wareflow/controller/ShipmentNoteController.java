package ca.mcgill.ecse.wareflow.controller;

import java.sql.Date;
import ca.mcgill.ecse.wareflow.model.ShipmentNote;
import ca.mcgill.ecse.wareflow.model.ShipmentOrder;
import ca.mcgill.ecse.wareflow.model.WarehouseStaff;
import ca.mcgill.ecse.wareflow.persistence.WareflowPersistence;

public class ShipmentNoteController {
  /**
   * A helper method to validate the attributes of a shipment note.
   * 
   * @author Ahmed Mossa
   * 
   * @param date The date of the note.
   * @param description The description of the note.
   * @param order The order to which the note is attached.
   * @param noteTaker The staff member who took the note.
   * @param index The index of the note in the order's list of notes. -1 if not required.
   * @return An error message if any of the attributes are invalid, otherwise an empty string
   *         indicating that the shipment note attributes are valid.
   */
  private static String attributesChecker(Date date, String description, ShipmentOrder order,
      WarehouseStaff noteTaker, int index) {
    if (description == "") {
      return "Order description cannot be empty";
    } else if (order == null) {
      return "Order does not exist";
    } else if (noteTaker == null) {
      return "Staff does not exist";
    } else if (order.getShipmentNotes().size() <= index) {
      return "Note does not exist";
    }
    return "";
  }

  /**
   * @author Ahmed Mossa
   * 
   * This method is used to add a new shipment note to the system.
   */
  public static String addShipmentNote(Date date, String description, int orderID,
      String username) {

    // Fetching the note taker and the order. Casting to WarehouseStaff is required to access the
    // method.
    WarehouseStaff noteTaker = (WarehouseStaff) WarehouseStaff.getWithUsername(username);
    ShipmentOrder order = ShipmentOrder.getWithId(orderID);

    // Calling the helper method to validate the input parameters. -1 is passed as the index (not
    // required here)
    String potentialError = attributesChecker(date, description, order, noteTaker, -1);

    // Checks whether the helper method caught a parameter-related error
    if (!potentialError.equals("")) {
      return potentialError;
    }

    try {
      order.addShipmentNote(date, description, noteTaker);
      WareflowPersistence.save();
      return "";
    } catch (Exception e) {
      return ("Something went wrong, full error message:" + e.getMessage());
    }
  }

  /**
   * @author Ahmed Mossa
   * 
   * This method is used to update a shipment note in the system.
   */
  public static String updateShipmentNote(int orderID, int index, Date newDate,
      String newDescription, String newUsername) {

    WarehouseStaff noteTaker = (WarehouseStaff) WarehouseStaff.getWithUsername(newUsername);
    ShipmentOrder order = ShipmentOrder.getWithId(orderID);

    String potentialError = attributesChecker(newDate, newDescription, order, noteTaker, index);

    if (!potentialError.equals("")) {
      return potentialError;
    }

    try {
      ShipmentNote note = order.getShipmentNote(index);
      note.setDescription(newDescription);
      note.setDate(newDate);
      note.setNoteTaker(noteTaker);
      WareflowPersistence.save();
      return "";
    } catch (Exception e) {
      return ("Something went wrong, full error message:" + e.getMessage());
    }
  }

  /**
   * @author Ahmed Mossa
   * 
   * This method is used to delete a shipment note from the system.
   */
  public static void deleteShipmentNote(int orderID, int index) {

    ShipmentOrder order = ShipmentOrder.getWithId(orderID);

    // Ensure that the order exists and that the note index is valid
    if (order != null) {
      if (order.getShipmentNotes().size() > index) {
        order.getShipmentNote(index).delete();
      }
    }
  }
}
