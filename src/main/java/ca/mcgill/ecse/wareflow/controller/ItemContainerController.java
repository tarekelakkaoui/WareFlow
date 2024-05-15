package ca.mcgill.ecse.wareflow.controller;

import java.sql.Date;
import java.util.List;

import ca.mcgill.ecse.wareflow.application.WareFlowApplication;
import ca.mcgill.ecse.wareflow.model.ItemContainer;
import ca.mcgill.ecse.wareflow.model.ItemType;
import ca.mcgill.ecse.wareflow.model.WareFlow;
import ca.mcgill.ecse.wareflow.persistence.WareflowPersistence;

public class ItemContainerController {

  // We need a global instance of the WareFlow class as it will be used across the file.
  private static WareFlow wareFlow = WareFlowApplication.getWareFlow();

  /**
   * A helper method to validate the attributes of a container.
   * 
   * @author Ahmed Mossa
   *
   * @param containerNumber The number of the container.
   * @param areaNumber The area number of the container.
   * @param slotNumber The slot number of the container.
   * @param addedOnDate The date when the container was added to WareFlow.
   * @param itemType The type of the item stored in the container.
   * @return An error message if any of the attributes are invalid, otherwise an empty string
   *         indicating that the container attributes are valid.
   */
  private static String containtersAttributesChecker(int containerNumber, int areaNumber,
      int slotNumber, Date addedOnDate, ItemType itemType) {

    // No need to check its existence in the system, because of the Umple 'unique' keyword.
    if (containerNumber < 1) {
      return "The container number shall not be less than 1";
    } else if (areaNumber < 0) {
      return "The area number shall not be less than 0";
    } else if (slotNumber < 0) {
      return "The slot number shall not be less than 0";
    } else if (itemType == null) {
      return "The item type does not exist";
    } else if (addedOnDate == null) {
      return "The adding date does not exist";
    }
    // Containers attributes are valid.
    return "";
  }

  /**
   * @author Ahmed Mossa, Anthony Saber 
   * 
   * This method is used to add a new item container to the system.
   */
  public static String addItemContainer(int containerNumber, int areaNumber, int slotNumber,
      Date addedOnDate, String itemTypeName) {

    ItemType itemType = ItemType.getWithName(itemTypeName);
    ItemContainer itemContainer = ItemContainer.getWithContainerNumber(containerNumber);
    // Calling the helper method to validate the input parameters.
    String potentialError = containtersAttributesChecker(containerNumber, areaNumber, slotNumber,
        addedOnDate, itemType);

    // Checks whether the helper method caught an error related to the parameters.
    if (!potentialError.equals("")) {
      return potentialError;
    }else if (itemContainer != null) {
		return "The item container number already exists";
	}

    try {
      wareFlow.addItemContainer(containerNumber, areaNumber, slotNumber, addedOnDate, itemType);
      WareflowPersistence.save();
      return "";
    } catch (Exception e) {
      return ("Something went wrong, full error message:" + e.getMessage());
    }
  }
  /**
   * @author Tarek El-Akkaoui, Mohamed Abdelhady
   * 
   * This method is used to update an existing item container in the system.
   */
  public static String updateItemContainer(int containerNumber, int newAreaNumber,
      int newSlotNumber, Date newAddedOnDate, String newItemTypeName) {

		ItemType newItemType = ItemType.getWithName(newItemTypeName);

		// Calling the helper method to validate the input parameters.
		String potentialError = containtersAttributesChecker(containerNumber, newAreaNumber, newSlotNumber,
				newAddedOnDate, newItemType);

		// Checks whether the helper method caught an error related to the parameters.
		if (!potentialError.equals("")) {
			return potentialError;
		}

		// Checks if the item container with a specific number exists
		ItemContainer itemContainer = ItemContainer.getWithContainerNumber(containerNumber);
		if (itemContainer == null) {
			return "Item container with number %d does NOT exist, make sure it's created beforehand!"
					.formatted(containerNumber);
		}

		// Attempts to update the added date, the area number, the item type, and the
		// slot number
		try {
			itemContainer.setAddedOnDate(newAddedOnDate);
			itemContainer.setAreaNumber(newAreaNumber);
			itemContainer.setItemType(newItemType);
			itemContainer.setSlotNumber(newSlotNumber);
			WareflowPersistence.save();
			return "";
		} catch (Exception e) {
			return ("Something went wrong, full error message:" + e.getMessage());
		}
  }
  
	/**
	 * @author Nicolas Saade, Marc Abou Abdallah
   * 
   * This method is used to delete an existing item container from the system.
	 */
	public static void deleteItemContainer(int containerNumber) {
		ItemContainer itemContainer = ItemContainer.getWithContainerNumber(containerNumber);
		if (itemContainer != null) {
      // First, delete the container from the system, and then delete the object itself
			wareFlow.removeItemContainer(itemContainer);
			itemContainer.delete();
			WareflowPersistence.save();
		}
	}

	public static List<ItemContainer> getAllItemContainers() {
			List<ItemContainer> itemContainer = wareFlow.getItemContainers();
			return itemContainer;
		}
	}

