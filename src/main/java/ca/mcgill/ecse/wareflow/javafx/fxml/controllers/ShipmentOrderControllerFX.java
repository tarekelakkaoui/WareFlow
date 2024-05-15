package ca.mcgill.ecse.wareflow.javafx.fxml.controllers;

import java.sql.Date;
import java.util.stream.Collectors;

import ca.mcgill.ecse.wareflow.controller.ItemContainerController;
import ca.mcgill.ecse.wareflow.controller.ShipmentOrderController;
import ca.mcgill.ecse.wareflow.javafx.fxml.WareFlowFxmlView;
import ca.mcgill.ecse.wareflow.model.ItemContainer;
import ca.mcgill.ecse.wareflow.model.ItemType;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class ShipmentOrderControllerFX {

	@FXML
	private TextField OrderIdTextField, OrderUsernameTextField, OrderDescriptionTextField, OrderQuantityTextField;
	@FXML
	private DatePicker OrderDateTextField;

	@FXML
	private Button addOrderButton, updateOrderButton, deleteOrderButton;
	@FXML
	private ChoiceBox<Integer> OrderContainerNumberChoiceBox;

	/**
	 * @author Ahmed Mossa
	 */
	@FXML
	public void initialize() {
		OrderContainerNumberChoiceBox.addEventHandler(WareFlowFxmlView.REFRESH_EVENT, e -> {
			OrderContainerNumberChoiceBox
					.setItems(ViewUtils.getItemContainers().stream().map(ItemContainer::getContainerNumber)
							.collect(Collectors.toCollection(FXCollections::observableArrayList)));
			OrderContainerNumberChoiceBox.setValue(null);
		});

		WareFlowFxmlView.getInstance().registerRefreshEvent(OrderContainerNumberChoiceBox);

	}

	/**
	 * @author Ahmed Mossa
	 */
	@FXML
	private void addOrderButtonClicked(ActionEvent event) {
		String idString = OrderIdTextField.getText().trim();
		String userNameString = OrderUsernameTextField.getText().trim();
		Date addedOnDate = java.sql.Date.valueOf(OrderDateTextField.getValue());
		String description = OrderDescriptionTextField.getText().trim();
		Integer containerNumberString = OrderContainerNumberChoiceBox.getValue();
		String quantityString = OrderQuantityTextField.getText().trim();

		// Checking for null or empty strings
		if (idString == null || idString.trim().isEmpty()) {
			ViewUtils.showError("Please input a valid order id. ");
		} else if (userNameString == null || userNameString.trim().isEmpty()) {
			ViewUtils.showError("Please input a valid username. ");

		} else if (description == null || description.trim().isEmpty()) {
			ViewUtils.showError("Please input a valid description. ");
		} else if (containerNumberString == null) {
			ViewUtils.showError("Please input a valid container number. ");
		} else if (quantityString == null || quantityString.trim().isEmpty()) {
			ViewUtils.showError("Please input a valid quantity. ");
		} else {
			int id = Integer.parseInt(idString);

			if (ViewUtils.successful(ShipmentOrderController.addShipmentOrder(id, addedOnDate, description,
					userNameString, containerNumberString, Integer.parseInt(quantityString)))) {
				OrderIdTextField.setText("");

				OrderDescriptionTextField.setText("");
				OrderUsernameTextField.setText("");

				OrderQuantityTextField.setText("");

			}
		}
	}

	/**
	 * @author Ahmed Mossa
	 */
	@FXML
	private void updateOrderButtonClicked(ActionEvent event) {
		String idString = OrderIdTextField.getText().trim();
		String userNameString = OrderUsernameTextField.getText().trim();
		Date addedOnDate = java.sql.Date.valueOf(OrderDateTextField.getValue());
		String description = OrderDescriptionTextField.getText().trim();
		Integer containerNumberString = OrderContainerNumberChoiceBox.getValue();
		String quantityString = OrderQuantityTextField.getText().trim();

		// Checking for null or empty strings
		if (idString == null || idString.trim().isEmpty()) {
			ViewUtils.showError("Please input a valid order id. ");
		} else if (userNameString == null || userNameString.trim().isEmpty()) {
			ViewUtils.showError("Please input a valid username. ");

		} else if (description == null || description.trim().isEmpty()) {
			ViewUtils.showError("Please input a valid description. ");
		} else if (containerNumberString == null) {
			ViewUtils.showError("Please input a valid container number. ");
		} else if (quantityString == null || quantityString.trim().isEmpty()) {
			ViewUtils.showError("Please input a valid quantity. ");
		} else {
			int id = Integer.parseInt(idString);

			if (ViewUtils.successful(ShipmentOrderController.updateShipmentOrder(id, addedOnDate, description,
					userNameString, containerNumberString, Integer.parseInt(quantityString)))) {
				OrderIdTextField.setText("");

				OrderDescriptionTextField.setText("");
				OrderUsernameTextField.setText("");

				OrderQuantityTextField.setText("");

			}
		}
	}

	/**
	 * @author Ahmed Mossa
	 */
	@FXML
	private void deleteOrderButtonClicked(ActionEvent event) {
		String idString = OrderIdTextField.getText().trim();
		if (idString == null || idString.trim().isEmpty()) {
			ViewUtils.showError("Please input a order id. ");
		} else {
			try {
				int id = Integer.parseInt(idString);
				ShipmentOrderController.deleteShipmentOrder(id); // since it's void we don't need to check for errors...
				OrderIdTextField.setText("");

				OrderDescriptionTextField.setText("");
				OrderUsernameTextField.setText("");

				OrderQuantityTextField.setText("");
			} catch (Exception e) {
				ViewUtils.showError("Please input a order id as an integer. ");
			}
		}
	}
}
