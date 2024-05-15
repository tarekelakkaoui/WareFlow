package ca.mcgill.ecse.wareflow.controller;

import java.util.List;

import ca.mcgill.ecse.wareflow.application.WareFlowApplication;
import ca.mcgill.ecse.wareflow.model.ItemType;
import ca.mcgill.ecse.wareflow.model.WareFlow;
import ca.mcgill.ecse.wareflow.persistence.WareflowPersistence;

public class ItemTypeController {

	// We need a global instance of the WareFlow class as it will be used across the file.
	private static WareFlow wareFlow = WareFlowApplication.getWareFlow();

	/**
	 * @author Anthony Saber 
	 * 
	 * This method allows to add new item type to the system while specifying the expected life span of this specific item type
	 */
	public static String addItemType(String name, int expectedLifeSpanInDays) {
		// Check if the ItemType with the given name already exists
		ItemType itemType = ItemType.getWithName(name);
		// Validate the name is not empty
		if (name == null || name.equals("")) {
			return "The name must not be empty";
			// Validate the expected lifespan is positive
		} else if (expectedLifeSpanInDays <= 0) {
			return "The expected life span must be greater than 0 days";
			// Check for duplicate ItemType
		} else if (itemType != null) {
			return "The item type already exists";
		}

		try {
			// Add the new ItemType to the WareFlow system
			wareFlow.addItemType(name, expectedLifeSpanInDays);
			WareflowPersistence.save();
			return "";
		} catch (Exception e) {
			// Catch any exceptions and return the error message
			return ("Something went wrong, full error message:" + e.getMessage());
		}
	}

	/**
	 * @author Anthony Saber
	 * 
	 * This method allows to update an existing item type to the system while specifying the new expected life span of the new specific item type
	 */
	public static String updateItemType(String oldName, String newName, int newExpectedLifeSpanInDays) {
		// Retrieve the ItemType by the old name
		ItemType oldItemType = ItemType.getWithName(oldName);
		// Validate the new name is not empty
		if (newName.equals("")) {
			return "The name must not be empty";
			// Validate the new expected lifespan is positive
		} else if (newExpectedLifeSpanInDays <= 0) {
			return "The expected life span must be greater than 0 days";
			// Ensure the ItemType exists
		} else if (oldItemType == null) {
			return "The item type does not exist";
		}

		try {
			// If the name has changed, check for conflicts and update the name
			if (!newName.equals(oldName)) {
				ItemType newItemType = ItemType.getWithName(newName);
				if (newItemType != null) {
					return "The item type already exists";
				}
				oldItemType.setName(newName);
				WareflowPersistence.save();
			}
			// Update the expected lifespan
			oldItemType.setExpectedLifeSpanInDays(newExpectedLifeSpanInDays);
			WareflowPersistence.save();
			return "";
		} catch (Exception e) {
			// Catch any exceptions and return the error message
			return ("Something went wrong, full error message:" + e.getMessage());
		}
	}

	/**
	 * @author Anthony Saber 
	 * 
	 * This method allows to delete item types from the system and subsequently to delete the item type object
	 */
	public static void deleteItemType(String name) {
		// Retrieve the ItemType by name
		ItemType itemType = ItemType.getWithName(name);
		if (itemType != null) {
			// Remove the ItemType from the WareFlow system
			wareFlow.removeItemType(itemType);
			WareflowPersistence.save();
			// Delete the ItemType object
			itemType.delete();
		}
	}
	
	public static List<ItemType> getAllItemTypes(){
		List<ItemType> itemType = wareFlow.getItemTypes();
		return itemType;
	}
	
}