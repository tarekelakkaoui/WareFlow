package ca.mcgill.ecse.wareflow.javafx.fxml.controllers;

import java.sql.Date;
import java.util.List;

import ca.mcgill.ecse.wareflow.model.WareFlow;
import ca.mcgill.ecse.wareflow.model.WarehouseStaff;
import ca.mcgill.ecse.wareflow.model.User;
import ca.mcgill.ecse.wareflow.model.Employee;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import ca.mcgill.ecse.wareflow.controller.WareFlowStatesController;
import ca.mcgill.ecse.wareflow.controller.UserController;
import ca.mcgill.ecse.wareflow.javafx.fxml.WareFlowFxmlView;

public class AssignUpdateController {

	@FXML
	private TextField orderIdTextField, noteTextField;
	@FXML
	private ChoiceBox<String> staffUsernameChoiceBox, priorityChoiceBox, timeEstimateChoiceBox, approvalChoiceBox;
	@FXML
	private Button assignButton, startWorkButton, completeWorkButton, approveButton, disapproveButton;
	@FXML
	private DatePicker noteDate;

	/**
	 * @author Tarek El-Akkaoui
	 */
	@FXML
	public void initialize() {
		ObservableList<String> priorities = FXCollections.observableArrayList("Low", "Normal", "Urgent");
		ObservableList<String> timeEstimates = FXCollections.observableArrayList("LessThanADay", "OneToThreeDays",
				"ThreeToSevenDays", "OneToThreeWeeks", "ThreeOrMoreWeeks");
		ObservableList<String> approvals = FXCollections.observableArrayList("true", "false");

		priorityChoiceBox.setItems(priorities);
		timeEstimateChoiceBox.setItems(timeEstimates);
		approvalChoiceBox.setItems(approvals);

		// staffUsernameChoiceBox.setItems(usernames);
		staffUsernameChoiceBox.addEventHandler(WareFlowFxmlView.REFRESH_EVENT, e -> {
			staffUsernameChoiceBox.setItems(UserController.getEmployees());
			staffUsernameChoiceBox.setValue(null);
		});
		WareFlowFxmlView.getInstance().registerRefreshEvent(staffUsernameChoiceBox);
	}

	/**
	 * @author Tarek El-Akkaoui
	 */
	@FXML
	private void assignButtonClicked(ActionEvent event) {
		String orderId = orderIdTextField.getText();
		String employeeEmail = staffUsernameChoiceBox.getValue();
		String priority = priorityChoiceBox.getValue();
		String timeEstimate = timeEstimateChoiceBox.getValue();
		String approval = approvalChoiceBox.getValue();

		if (orderId.isEmpty()) {
			ViewUtils.showError("Order ID cannot be empty");
			return;
		}

		String result = WareFlowStatesController.assignOrder(orderId, employeeEmail, timeEstimate, priority, approval);
		if (!result.isEmpty()) {
			ViewUtils.showError(result);
		} else {
			ViewUtils.showConfirmation("Order assigned successfully");
			orderIdTextField.clear();
		}
	}

	/**
	 * @author Tarek El-Akkaoui
	 */
	@FXML
	private void startWorkButtonClicked(ActionEvent event) {
		String orderId = orderIdTextField.getText();
		if (orderId.isEmpty()) {
			ViewUtils.showError("Order ID cannot be empty");
			return;
		}

		String result = WareFlowStatesController.startOrder(orderId);
		if (!result.isEmpty()) {
			ViewUtils.showError(result);
		} else {
			ViewUtils.showConfirmation("Work started on order");
		}
	}

	/**
	 * @author Anthony Saber
	 */
	@FXML
	private void completeWorkButtonClicked(ActionEvent event) {
		String orderId = orderIdTextField.getText();
		if (orderId.isEmpty()) {
			ViewUtils.showError("Order ID cannot be empty");
			return;
		}

		String result = WareFlowStatesController.completeOrder(orderId);
		if (!result.isEmpty()) {
			ViewUtils.showError(result);
		} else {
			ViewUtils.showConfirmation("Order completed successfully");
		}
	}

	/**
	 * @author Anthony Saber
	 */
	@FXML
	private void approveButtonClicked(ActionEvent event) {
		String orderId = orderIdTextField.getText();
		if (orderId.isEmpty()) {
			ViewUtils.showError("Order ID cannot be empty");
			return;
		}

		String result = WareFlowStatesController.approveOrder(orderId);
		if (!result.isEmpty()) {
			ViewUtils.showError(result);
		} else {
			ViewUtils.showConfirmation("Order approved successfully");
		}
	}

	/**
	 * @author Anthony Saber
	 */
	@FXML
	private void disapproveButtonClicked(ActionEvent event) {
		String orderId = orderIdTextField.getText();
		String reason = noteTextField.getText();
		String approval = approvalChoiceBox.getValue();

		if (orderId.isEmpty()) {
			ViewUtils.showError("Order ID must not me empty");
			return;
		}
		if (approval.equalsIgnoreCase("true")) {
			if (reason.isEmpty() || noteDate.getValue() == null) {
				ViewUtils.showError("Note missing date or reson");
				return;
			}
			Date date = java.sql.Date.valueOf(noteDate.getValue());
			String result = WareFlowStatesController.disapproveOrder(orderId, date.toString(), reason);
			if (!result.isEmpty()) {
				ViewUtils.showError(result);
			} else {
				ViewUtils.showConfirmation("Order disapproved successfully");
				orderIdTextField.clear();
				noteTextField.clear();
			}
		} else {
			ViewUtils.showError("Cannot Disapprove an order without manager approval");
			return;
		}

	}
}
