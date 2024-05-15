package ca.mcgill.ecse.wareflow.features;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.sql.Date;
import java.util.List;
import java.util.Map;
import ca.mcgill.ecse.wareflow.application.WareFlowApplication;
import ca.mcgill.ecse.wareflow.controller.TOShipmentNote;
import ca.mcgill.ecse.wareflow.controller.TOShipmentOrder;
import ca.mcgill.ecse.wareflow.controller.WareFlowStatesController;
import ca.mcgill.ecse.wareflow.model.ItemContainer;
import ca.mcgill.ecse.wareflow.model.ItemType;
import ca.mcgill.ecse.wareflow.model.Manager;
import ca.mcgill.ecse.wareflow.model.ShipmentNote;
import ca.mcgill.ecse.wareflow.model.ShipmentOrder;
import ca.mcgill.ecse.wareflow.model.ShipmentOrder.PriorityLevel;
import ca.mcgill.ecse.wareflow.model.ShipmentOrder.TimeEstimate;
import ca.mcgill.ecse.wareflow.model.User;
import ca.mcgill.ecse.wareflow.model.WareFlow;
import ca.mcgill.ecse.wareflow.model.WarehouseStaff;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class ShipmentOrderStepDefinitions {

	private List<TOShipmentOrder> orders;
	private String error;
	private WareFlow wareFlow = WareFlowApplication.getWareFlow();



	/**
	 * Create the employee in the system with the features specified
	 * 
	 * @author Anthony Saber, Marc Abou Abdallah
	 * @param datatable is the data table that contains the features of the employee.
	 */
	@Given("the following employees exist in the system")
	public void the_following_employees_exist_in_the_system(
			io.cucumber.datatable.DataTable dataTable) {
		List<Map<String, String>> rows = dataTable.asMaps();
		for (var row : rows) {
			String aUsername = row.get("username");
			String aPassword = row.get("password");
			String aName = row.get("name");
			String aPhoneNumber = row.get("aPhoneNumber");
			wareFlow.addEmployee(aUsername, aName, aPassword, aPhoneNumber);
		}
	}

	/**
	 * Create a manager in the system with the features specified
	 * 
	 * @author Anthony Saber, Marc Abou Abdallah
	 * @param datatable is the data table that contains the features of the manager.
	 */
	@Given("the following manager exists in the system")
	public void the_following_manager_exists_in_the_system(
			io.cucumber.datatable.DataTable dataTable) {
		List<Map<String, String>> rows = dataTable.asMaps();
		for (var row : rows) {
			String aUsername = row.get("username");
			String aPassword = row.get("password");
			Manager aNewManager = new Manager(aUsername, null, aPassword, null, wareFlow);
			wareFlow.setManager(aNewManager);
		}
	}

	/**
	 * Create item type in the system with the features specified
	 * 
	 * @author Anthony Saber, Marc Abou Abdallah
	 * @param datatable is the data table that contains the features of the item type.
	 */
	@Given("the following items exist in the system")
	public void the_following_items_exist_in_the_system(io.cucumber.datatable.DataTable dataTable) {
		List<Map<String, String>> rows = dataTable.asMaps();
		for (var row : rows) {
			String name = row.get("name");
			String expectedLifeSpanInDaysString = row.get("expectedLifeSpanInDays");
			if (expectedLifeSpanInDaysString != null) {
				int expectedLifeSpanInDays = Integer.parseInt(expectedLifeSpanInDaysString);
				wareFlow.addItemType(name, expectedLifeSpanInDays);
			} else {
				wareFlow.addItemType(name, -1);
			}
		}
	}

	/**
	 * Ensure that orders are at different stages matching the feature file.
	 * 
	 * @author Anthony Saber, Marc Abou Abdallah
	 * @param string is the order Id.
	 * @param string2 is the status of the order.
	 * @param string3 is the confirmation or denial of order approval.
	 */
	@Given("order {string} is marked as {string} with requires approval {string}")
	public void order_is_marked_as_with_requires_approval(String string, String string2,
			String string3) {
		int orderId = Integer.parseInt(string);
		ShipmentOrder order = ShipmentOrder.getWithId(orderId);
		WarehouseStaff staff = wareFlow.getManager();
		TimeEstimate timeEstimate = TimeEstimate.LessThanADay;
		PriorityLevel priority = PriorityLevel.Normal;
		boolean requiresApproval = Boolean.parseBoolean(string3);

		if (string2.equals("Assigned")) {
			order.assignOrder(staff, timeEstimate, priority, requiresApproval);
		}
		if (string2.equals("InProgress")) {
			order.assignOrder(staff, timeEstimate, priority, requiresApproval);
			order.startOrder();
		}
		if (string2.equals("Completed")) {
			order.assignOrder(staff, timeEstimate, priority, true);
			order.startOrder();
			order.completeOrder();
		}
		if (string2.equals("Closed")) {
			order.assignOrder(staff, timeEstimate, priority, false);
			order.startOrder();
			order.completeOrder();
		}
	}

	/**
	 * Creates a container with the specified features.
	 * 
	 * @author Anthony Saber
	 * @param dataTable is the data table that contains the features of the container.
	 */
	@Given("the following containers exist in the system")
	public void the_following_containers_exist_in_the_system(
			io.cucumber.datatable.DataTable dataTable) {
		List<Map<String, String>> rows = dataTable.asMaps();
		for (var row : rows) {
			int aContainerNumber = Integer.parseInt(row.get("containerNumber"));
			ItemType aItemType = ItemType.getWithName(row.get("type"));
			Date purchaseDate = Date.valueOf(row.get("purchaseDate"));
			int aAreaNumber = Integer.parseInt(row.get("areaNumber"));
			int aSlotNumber = Integer.parseInt(row.get("slotNumber"));
			wareFlow.addItemContainer(aContainerNumber, aAreaNumber, aSlotNumber, purchaseDate,
					aItemType);
		}
	}

	/**
	 * Ensure that orders are at different stages matching the feature file with their specified
	 * features.
	 * 
	 * @author Anthony Saber
	 * @param dataTable is the data table that contains the features of the orders.
	 */
	@Given("the following orders exist in the system")
	public void the_following_orders_exist_in_the_system(io.cucumber.datatable.DataTable dataTable) {
		List<Map<String, String>> rows = dataTable.asMaps();
		for (var row : rows) {
			int id = Integer.parseInt(row.get("id"));
			Date placedOnDate = Date.valueOf(row.get("placedOnDate"));
			String description = row.get("description");
			String orderPlacer = row.get("orderPlacer");
			User placerUser = User.getWithUsername(orderPlacer);
			int containerNumber = Integer.parseInt(row.get("containerNumber"));
			int quantity = Integer.parseInt(row.get("quantity"));

			ItemContainer container = ItemContainer.getWithContainerNumber(containerNumber);
			wareFlow.addOrder(id, placedOnDate, description, quantity, placerUser);
			ShipmentOrder order = ShipmentOrder.getWithId(id);
			order.setContainer(container);
			String orderStatus = row.get("status");
			if (!orderStatus.equals("Open")) {
				TimeEstimate timeEstimate = TimeEstimate.valueOf(row.get("timeToResolve"));
				PriorityLevel priorityLevel = PriorityLevel.valueOf(row.get("priority"));
				WarehouseStaff staff = (WarehouseStaff) User.getWithUsername(row.get("processedBy"));
				boolean requiresApproval = Boolean.parseBoolean(row.get("approvalRequired"));

				if (orderStatus.equals("Assigned")) {
					order.assignOrder(staff, timeEstimate, priorityLevel, requiresApproval);
				}
				if (orderStatus.equals("InProgress")) {
					order.assignOrder(staff, timeEstimate, priorityLevel, requiresApproval);
					order.startOrder();
				}
				if (orderStatus.equals("Completed")) {
					order.assignOrder(staff, timeEstimate, priorityLevel, true);
					order.startOrder();
					order.completeOrder();
				}
				if (orderStatus.equals("Closed")) {
					order.assignOrder(staff, timeEstimate, priorityLevel, false);
					order.startOrder();
					order.completeOrder();
				}

			}
		}
		error = "";
	}

	/**
	 * Ensure that notes exists in different orders as specified .
	 * 
	 * @author Anthony Saber
	 * @param dataTable is the data table that contains the features of the notes.
	 */
	@Given("the following notes exist in the system")
	public void the_following_notes_exist_in_the_system(io.cucumber.datatable.DataTable dataTable) {
		List<Map<String, String>> rows = dataTable.asMaps();
		for (var row : rows) {
			String noteTaker = (row.get("noteTaker").toString());
			int orderID = Integer.parseInt(row.get("orderId").toString());
			Date addedOnDate = Date.valueOf(row.get("addedOnDate").toString());
			String description = (row.get("description").toString());

			// Instantiate and add the specified shipment order notes to the appropriate shipment order.
			ShipmentOrder order = ShipmentOrder.getWithId(orderID);
			WarehouseStaff staff = (WarehouseStaff) WarehouseStaff.getWithUsername(noteTaker);
			ShipmentNote note = new ShipmentNote(addedOnDate, description, order, staff);
			order.addShipmentNote(note);

		}
	}

	/**
	 * Ensure that orders are at different stages matching the feature file.
	 * 
	 * @author Marc Abou Abdallah
	 * @param string is the order Id.
	 * @param string2 is the status of the order.
	 */
	@Given("order {string} is marked as {string}")
	public void order_is_marked_as(String string, String string2) {
		int orderId = Integer.parseInt(string);
		ShipmentOrder order = ShipmentOrder.getWithId(orderId);
		WarehouseStaff staff = wareFlow.getManager();
		TimeEstimate timeEstimate = TimeEstimate.LessThanADay;
		PriorityLevel priority = PriorityLevel.Normal;
		boolean requiresApproval = order.hasOrderApprover();
		if (string2.equals("Open")) {
			return;
		}
		if (string2.equals("Assigned")) {
			order.assignOrder(staff, timeEstimate, priority, requiresApproval);
		}
		if (string2.equals("InProgress")) {
			order.assignOrder(staff, timeEstimate, priority, requiresApproval);
			order.startOrder();
		}
		if (string2.equals("Completed")) {
			order.assignOrder(staff, timeEstimate, priority, true);
			order.startOrder();
			order.completeOrder();
		}
		if (string2.equals("Closed")) {
			order.assignOrder(staff, timeEstimate, priority, false);
			order.startOrder();
			order.completeOrder();
		}
	}

	/**
	 * View all shipment orders in the system.
	 * 
	 * @author Ahmed Mossa
	 */
	@When("the manager attempts to view all shipment orders in the system")
	public void the_manager_attempts_to_view_all_shipment_orders_in_the_system() {
		orders = WareFlowStatesController.viewAllShipmentOrder();
	}

	/**
	 * Starts an order.
	 * 
	 * @author Ahmed Mossa
	 * @param string the orderId of a specific order in the system.
	 */
	@When("the warehouse staff attempts to start the order {string}")
	public void the_warehouse_staff_attempts_to_start_the_order(String string) {
		error = WareFlowStatesController.startOrder(string);
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
	@When("the manager attempts to assign the order {string} to {string} with estimated time {string}, priority {string}, and requires approval {string}")
	public void the_manager_attempts_to_assign_the_order_to_with_estimated_time_priority_and_requires_approval(
			String string, String string2, String string3, String string4, String string5) {
		error = WareFlowStatesController.assignOrder(string, string2, string3, string4, string5);
	}

	/**
	 * Completes an order.
	 * 
	 * @author Ahmed Mossa
	 * @param string the orderId of a specific order in the system.
	 */
	@When("the warehouse staff attempts to complete the order {string}")
	public void the_warehouse_staff_attempts_to_complete_the_order(String string) {
		error = WareFlowStatesController.completeOrder(string);
	}

	/**
	 * Disapproves an order.
	 * 
	 * @author Ahmed Mossa
	 * @param string the orderId of a specific order in the system.
	 * @param string2 the note to add to the order.
	 * @param string3 the note taker's username.
	 */
	@When("the manager attempts to disapprove the order {string} on date {string} and with reason {string}")
	public void the_manager_attempts_to_disapprove_the_order_on_date_and_with_reason(String string,
			String string2, String string3) {
		error = WareFlowStatesController.disapproveOrder(string, string2, string3);
	}

	/**
	 * Approves an order.
	 * 
	 * @author Ahmed Mossa
	 * @param string the orderId of a specific order in the system.
	 */
	@When("the manager attempts to approve the order {string}")
	public void the_manager_attempts_to_approve_the_order(String string) {
		error = WareFlowStatesController.approveOrder(string);
	}

	/**
	 * Checks if the orders are presented as specified in the feature file.
	 * 
	 * @author Anthony Saber
	 * @param dataTable is the data table that contains the features of the order.
	 */
	@Then("the following shipment orders shall be presented")
	public void the_following_shipment_orders_shall_be_presented(
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
			String assetName = row.get("assetName");
			assertEquals(assetName, currOrder.getItemName());
			String expectLifeSpanStr = row.get("expectedLifeSpanInDays");
			int expectLifeSpan = -1;
			if (expectLifeSpanStr != null) {
				expectLifeSpan = Integer.parseInt(expectLifeSpanStr);
			}
			assertEquals(expectLifeSpan, currOrder.getExpectedLifeSpanInDays());
			String purchaseDateStr = row.get("purchaseDate");
			Date purchaseDate = null;
			if (purchaseDateStr != null) {
				purchaseDate = Date.valueOf(purchaseDateStr);
			}
			assertEquals(purchaseDate, currOrder.getAddedOnDate());
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
			String quantityStr = row.get("quantity");
			int quantity = -1;
			if (quantityStr != null) {
				quantity = Integer.parseInt(quantityStr);
			}
			assertEquals(quantity, currOrder.getQuantity());

			String aStatus = row.get("status");
			assertEquals(aStatus, currOrder.getStatus());

			String processedByStr = row.get("processedBy");
			if (processedByStr != null) {
				assertEquals(processedByStr, currOrder.getProcessedBy().toLowerCase());
			} else {
				assertEquals(processedByStr, currOrder.getProcessedBy());
			}

			String aTimeToResolve = row.get("timeToResolve");
			assertEquals(aTimeToResolve, currOrder.getTimeToResolve());
			String priority = row.get("priority");
			assertEquals(priority, currOrder.getPriority());

			String approvalRequiredStr = row.get("approvalRequired");
			boolean approvalRequired = Boolean.valueOf(approvalRequiredStr);
			assertEquals(approvalRequired, currOrder.getApprovalRequired());
			i++;
		}
	}

	/**
	 * The order with id shall have no notes
	 * 
	 * @author Nicolas Saade
	 * @param string the orderId of a specific order in the system.
	 */
	@Then("the order with id {string} shall have no notes")
	public void the_order_with_id_shall_have_no_notes(String string) {
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

	/**
	 * The order with if shall not exist in the system
	 * 
	 * @author Nicolas Saade, Marc Abou ABdallah
	 * @param string the orderId of a specific order in the system.
	 */
	@Then("the order {string} shall not exist in the system") // saade
	public void the_order_shall_not_exist_in_the_system(String string) {
		int orderId = Integer.parseInt(string);
		ShipmentOrder order = ShipmentOrder.getWithId(orderId);
		assertEquals(order, null);
	}

	/**
	 * The number of orders in the system should be
	 * 
	 * @author Nicolas Saade
	 * @param string the number of orders in the system
	 */
	@Then("the number of orders in the system shall be {string}")
	public void the_number_of_orders_in_the_system_shall_be(String string) {
		int numberOfOrders = wareFlow.getOrders().size();
		int expectedNumOrders = Integer.parseInt(string);
		assertEquals(expectedNumOrders, numberOfOrders);
	}

	/**
	 * The system should raise the specified error
	 * 
	 * @author Nicolas Saade, Marc Abou Abdallah
	 * @param string the error that should be raised by the system
	 */
	@Then("the system shall raise the error {string}")
	public void the_system_shall_raise_the_error(String string) {
		assertEquals(string, error);
	}

	/**
	 * The order should be marked as the specified state
	 * 
	 * @author Nicolas Saade, Marc Abou Abdallah
	 * @param string the order that has to be checked
	 * @param string2 the state that the order should have
	 */
	@Then("the order {string} shall be marked as {string}")
	public void the_order_shall_be_marked_as(String string, String string2) {
		int orderId = Integer.parseInt(string);
		ShipmentOrder order = ShipmentOrder.getWithId(orderId);
		assertEquals(order.getStatusFullName(), string2);
	}

	/**
	 * The order should be assigned to the specified employee
	 * 
	 * @author Nicolas Saade, Marc Abou ABdallah
	 * @param string the orderId of a specific order in the system.
	 * @param string2 the employee that has to be assigned with the order
	 */
	@Then("the order {string} shall be assigned to {string}")
	public void the_order_shall_be_assigned_to(String string, String string2) {
		int orderId = Integer.parseInt(string);
		ShipmentOrder order = ShipmentOrder.getWithId(orderId);
		WarehouseStaff staff = order.getOrderPicker();
		assertEquals(staff.getUsername(), string2);
	}

	/**
	 * The order with id shall have the following notes. Note: This method was not included in the
	 * original file and was added manually.
	 * 
	 * @author Ahmed Mossa
	 * @param string the orderId of a specific order in the system.
	 */
	@Then("the order with id {string} shall have the following notes")
	public void the_order_with_id_shall_have_the_following_notes(String string,
			io.cucumber.datatable.DataTable dataTable) {
		int orderID = Integer.parseInt(string);
		TOShipmentOrder currOrder = null;
		orders = WareFlowStatesController.viewAllShipmentOrder();
		for (var order : orders) {
			if (order.getId() == orderID) {
				currOrder = order;
				break;
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
			Date date = Date.valueOf(row.get("addedOnDate"));
			assertEquals(date, currNote.getDate());
			String description = row.get("description");
			assertEquals(description, currNote.getDescription());
			i++;
		}
	}

	/**
	 * The order should have the following time estimate, priority and approval requirement
	 * 
	 * @author Nicolas Saade
	 * @param string the order that has to be checked
	 * @param string2 the order time estimate
	 * @param string3 the order priority
	 * @param string4 whether the order requires approval or not
	 */
	@Then("the order {string} shall have estimated time {string}, priority {string}, and requires approval {string}")
	public void the_order_shall_have_estimated_time_priority_and_requires_approval(String string,
			String string2, String string3, String string4) {
		int orderId = Integer.parseInt(string);
		ShipmentOrder order = ShipmentOrder.getWithId(orderId);
		assertEquals(order.getTimeToFullfill().toString(), string2);
		assertEquals(order.getPriority().toString(), string3);
		if (string4.equals("true")) {
			assertTrue(order.hasOrderApprover());
		} else {
			assertFalse(order.hasOrderApprover());
		}
	}
}
