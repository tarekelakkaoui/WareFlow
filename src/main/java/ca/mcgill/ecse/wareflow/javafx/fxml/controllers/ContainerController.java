package ca.mcgill.ecse.wareflow.javafx.fxml.controllers;

import java.sql.Date;
import java.util.stream.Collectors;

import ca.mcgill.ecse.wareflow.controller.ItemContainerController;
import ca.mcgill.ecse.wareflow.javafx.fxml.WareFlowFxmlView;
import ca.mcgill.ecse.wareflow.model.ItemContainer;
import ca.mcgill.ecse.wareflow.model.ItemType;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class ContainerController {
	@FXML
	private TableView<ItemContainer> containerTableView;
	@FXML
	private TableColumn<ItemContainer, Integer> containerNumberColumn;
	@FXML
	private TableColumn<ItemContainer, String> containerTypeColumn;
	@FXML
	private TableColumn<ItemContainer, Date> containerAddedOnColumn;
	@FXML
	private TableColumn<ItemContainer, Integer> containerAreaNumberColumn;
	@FXML
	private TableColumn<ItemContainer, Integer> containerSlotNumberColumn;
	@FXML
	private TextField itemContainerNumberTextField;
	@FXML
	private ChoiceBox<String> itemContainerTypeChoiceBox;
	@FXML
	private DatePicker itemContainerAddedOnDatePicker;
	@FXML
	private TextField itemContainerAreaNumberTextField;
	@FXML
	private TextField itemContainerSlotNumberTextField;
	@FXML
	private Button newItemContainerButton;
	@FXML
	private TextField updateItemContainerNumberTextField;
	@FXML
	private ChoiceBox<String> newItemContainerTypeChoiceBox;
	@FXML
	private DatePicker newItemContainerAddedOnDatePicker;
	@FXML
	private TextField newItemContainerAreaNumberTextField;
	@FXML
	private TextField newItemContainerSlotNumberTextField;
	@FXML
	private Button updateItemContainerButton;
	@FXML
	private TextField deleteItemContainerNumberTextField;
	@FXML
	private Button deleteItemContainerButton;

	/**
	 * @author Marc Abou-Abdallah, Nicolas Saade
	 */
	@FXML
	public void initialize() {
		containerNumberColumn.setCellValueFactory(new PropertyValueFactory<>("containerNumber"));
		containerTypeColumn
				.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getItemType().getName()));

		containerAddedOnColumn.setCellValueFactory(new PropertyValueFactory<>("addedOnDate"));
		containerAreaNumberColumn.setCellValueFactory(new PropertyValueFactory<>("areaNumber"));
		containerSlotNumberColumn.setCellValueFactory(new PropertyValueFactory<>("slotNumber"));

		// Populate ChoiceBox
		ObservableList<String> itemTypeNames = ViewUtils.getItemTypes().stream().map(ItemType::getName)
				.collect(Collectors.toCollection(FXCollections::observableArrayList));
		itemContainerTypeChoiceBox.addEventHandler(WareFlowFxmlView.REFRESH_EVENT, e -> {
			itemContainerTypeChoiceBox.setItems(ViewUtils.getItemTypes().stream().map(ItemType::getName)
					.collect(Collectors.toCollection(FXCollections::observableArrayList)));
			itemContainerTypeChoiceBox.setValue(null);
		});

		newItemContainerTypeChoiceBox.addEventHandler(WareFlowFxmlView.REFRESH_EVENT, e -> {
			newItemContainerTypeChoiceBox.setItems(ViewUtils.getItemTypes().stream().map(ItemType::getName)
					.collect(Collectors.toCollection(FXCollections::observableArrayList)));
			newItemContainerTypeChoiceBox.setValue(null);
		});

		WareFlowFxmlView.getInstance().registerRefreshEvent(newItemContainerTypeChoiceBox);
		WareFlowFxmlView.getInstance().registerRefreshEvent(itemContainerTypeChoiceBox);
		containerTableView.setItems(ViewUtils.getItemContainers());
		containerTableView.refresh();
		containerTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
			if (newSelection != null) {
				deleteItemContainerNumberTextField.setText(String.valueOf(newSelection.getContainerNumber()));
				updateItemContainerNumberTextField.setText(String.valueOf(newSelection.getContainerNumber()));
			}
		});
		// Load and set items
		refreshTableView();
	}

	private void refreshTableView() {
		ObservableList<ItemContainer> containers = ViewUtils.getItemContainers();
		containerTableView.setItems(containers);
		containerTableView.refresh();
	}

	/**
	 * @author Marc Abou-Abdallah, Nicolas Saade
	 */
	@FXML
	public void newItemContainerClicked(ActionEvent event) {
		try {
			String containerNumberStr = itemContainerNumberTextField.getText();
			String containerType = itemContainerTypeChoiceBox.getValue();
			Date addedOnDate = java.sql.Date.valueOf(itemContainerAddedOnDatePicker.getValue());

			int containerNumber = Integer.parseInt(containerNumberStr);
			int areaNumber = Integer.parseInt(itemContainerAreaNumberTextField.getText());
			int slotNumber = Integer.parseInt(itemContainerSlotNumberTextField.getText());

			// Your method to add container
			if (ViewUtils.successful(ItemContainerController.addItemContainer(containerNumber, areaNumber, slotNumber,
					addedOnDate, containerType))) {
				itemContainerNumberTextField.setText("");
				itemContainerAreaNumberTextField.setText("");
				itemContainerSlotNumberTextField.setText("");
				containerTableView.setItems(ViewUtils.getItemContainers());
				refreshTableView(); // Refresh the table view
				WareFlowFxmlView.getInstance().registerRefreshEvent(containerTableView);
			}
		} catch (NumberFormatException e) {
			// Check which field caused the NumberFormatException and display an appropriate
			// error message
			String errorMessage;
			if (!isInteger(itemContainerNumberTextField.getText())) {
				errorMessage = "Please enter a valid integer for container number.";
			} else if (!isInteger(itemContainerAreaNumberTextField.getText())) {
				errorMessage = "Please enter a valid integer for area number.";
			} else if (!isInteger(itemContainerSlotNumberTextField.getText())) {
				errorMessage = "Please enter a valid integer for slot number.";
			} else {
				errorMessage = "Please enter valid numbers for container number, area number, and slot number.";
			}
			ViewUtils.showError(errorMessage);
		} catch (Exception e) {
			if (itemContainerNumberTextField.getText().isEmpty() || itemContainerTypeChoiceBox.getValue() == null
					|| itemContainerAddedOnDatePicker.getValue() == null) {
				String errorMessage = "";
				if (itemContainerNumberTextField.getText().isEmpty()) {
					errorMessage += "Container number must be filled.\n";
				} else if (itemContainerTypeChoiceBox.getValue() == null) {
					errorMessage += "Container type must be selected.\n";
				} else {
					errorMessage += "Added on date must be selected.";
				}
				ViewUtils.showError(errorMessage);

			}

		}
	}

	/**
	 * @author Marc Abou-Abdallah, Nicolas Saade
	 */
	private boolean isInteger(String str) {
		try {
			Integer.parseInt(str);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	/**
	 * @author Marc Abou-Abdallah, Nicolas Saade
	 */
	@FXML
	public void updateItemContainerClicked(ActionEvent event) {
		// Get the entered container number and the new details from the form
		try {
			String containerNumberStr = updateItemContainerNumberTextField.getText();
			String containerType = newItemContainerTypeChoiceBox.getValue();

			String areaNumberStr = newItemContainerAreaNumberTextField.getText();
			String slotNumberStr = newItemContainerSlotNumberTextField.getText();

			Date addedOnDate = java.sql.Date.valueOf(newItemContainerAddedOnDatePicker.getValue());
			int containerNumber = Integer.parseInt(containerNumberStr);
			int areaNumber = Integer.parseInt(areaNumberStr);
			int slotNumber = Integer.parseInt(slotNumberStr);

			// Call the backend method to update the container
			if (ViewUtils.successful(ItemContainerController.updateItemContainer(containerNumber, areaNumber,
					slotNumber, addedOnDate, containerType))) {
				// Clear fields and refresh view
				updateItemContainerNumberTextField.setText("");
				newItemContainerAreaNumberTextField.setText("");
				newItemContainerAreaNumberTextField.setText("");
				newItemContainerSlotNumberTextField.setText("");
				// ... clear other update fields
				containerTableView.setItems(ViewUtils.getItemContainers());
				refreshTableView(); // Refresh the table view
				WareFlowFxmlView.getInstance().registerRefreshEvent(containerTableView);
			}
		} catch (NumberFormatException e) {
			// Check which field caused the NumberFormatException and display an appropriate
			// error message
			String errorMessage;
			if (!isInteger(updateItemContainerNumberTextField.getText())) {
				errorMessage = "Please enter a valid integer for container number.";
			} else if (!isInteger(newItemContainerAreaNumberTextField.getText())) {
				errorMessage = "Please enter a valid integer for area number.";
			} else if (!isInteger(newItemContainerSlotNumberTextField.getText())) {
				errorMessage = "Please enter a valid integer for slot number.";
			} else {
				errorMessage = "Please enter valid numbers for container number, area number, and slot number.";
			}
			ViewUtils.showError(errorMessage);
		} catch (Exception e) {
			if (updateItemContainerNumberTextField.getText().isEmpty()
					|| newItemContainerTypeChoiceBox.getValue() == null
					|| newItemContainerAddedOnDatePicker.getValue() == null) {
				String errorMessage = "";
				if (updateItemContainerNumberTextField.getText().isEmpty()) {
					errorMessage += "Container number must be filled.\n";
				} else if (newItemContainerTypeChoiceBox.getValue() == null) {
					errorMessage += "Container type must be selected.\n";
				} else {
					errorMessage += "Added on date must be selected.";
				}
				ViewUtils.showError(errorMessage);

			}

		}
	}

	/**
	 * @author Marc Abou-Abdallah, Nicolas Saade
	 */
	@FXML
	public void deleteItemContainerClicked(ActionEvent event) {

		String containerNumberStr = deleteItemContainerNumberTextField.getText();

		try {
			int containerNumber = Integer.parseInt(containerNumberStr);

			ItemContainer itemContainer = ItemContainer.getWithContainerNumber(containerNumber);
			if (itemContainer != null) {
				ItemContainerController.deleteItemContainer(containerNumber);
				// Clear field and refresh view
				deleteItemContainerNumberTextField.setText("");
				refreshTableView();
				WareFlowFxmlView.getInstance().refresh();
			} else {
				ViewUtils.showError("Item Container does not exist");
			}
		} catch (NumberFormatException e) {
			ViewUtils.showError("Please enter a valid integer for the container number.");
		} catch (Exception e) {
			ViewUtils.showError("An unexpected error occurred: " + e.getMessage());
		}
	}

}
