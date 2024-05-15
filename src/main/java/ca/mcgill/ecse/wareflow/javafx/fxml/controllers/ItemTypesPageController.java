package ca.mcgill.ecse.wareflow.javafx.fxml.controllers;

import java.sql.Date;
import java.util.List;

import ca.mcgill.ecse.wareflow.controller.ItemTypeController;
import ca.mcgill.ecse.wareflow.javafx.fxml.WareFlowFxmlView;
import ca.mcgill.ecse.wareflow.model.ItemType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class ItemTypesPageController {
	@FXML
	private TableView<ItemType> itemTypeTableView;
	@FXML
	private TableColumn<ItemType, String> itemTypeNameColumn;
	@FXML
	private TableColumn<ItemType, Integer> itemTypeLifespanColumn;
	@FXML
	private TextField createNewItemTypeTextField;
	@FXML
	private TextField createExpectedLifespanTextField;
	@FXML
	private TextField updateOldItemTypeTextField;
	@FXML
	private TextField deleteItemTypeTextField;
	@FXML
	private TextField updateNewItemTypeTextField;
	@FXML
	private TextField updateExpectedLifespanTextField;
	@FXML
	private Button createButton;
	@FXML
	private Button updateButton;
	@FXML
	private Button deleteButton;

	/**
	 * @author Nicolas Saade
	 */
	@FXML
	public void initialize() {
		itemTypeNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
		itemTypeLifespanColumn.setCellValueFactory(new PropertyValueFactory<>("expectedLifeSpanInDays"));

		itemTypeTableView.setItems(ViewUtils.getItemTypes()); // Load item types

		itemTypeTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
			if (newSelection != null) {
				updateOldItemTypeTextField.setText(newSelection.getName());
				deleteItemTypeTextField.setText(newSelection.getName());
			}
		});
		WareFlowFxmlView.getInstance().registerRefreshEvent(itemTypeTableView);
	}

	/**
	 * @author Nicolas Saade
	 */
	@FXML
	public void createButtonClicked(ActionEvent event) {
		String createNewItemTypeString = createNewItemTypeTextField.getText();
		String createExpectedLifespanString = createExpectedLifespanTextField.getText();

		if (createNewItemTypeString == null || createNewItemTypeString.trim().isEmpty()) {
			ViewUtils.showError("The New Item Type field cannot be empty");
		} else if (createExpectedLifespanString == null || createExpectedLifespanString.trim().isEmpty()) {
			ViewUtils.showError("The Expected Lifespan field cannot be empty");
		} else if (createExpectedLifespanString == null || createExpectedLifespanString.trim().isEmpty()) {
			ViewUtils.showError("The Expected Lifespan field cannot be empty");
		} else {
			try {
				int expectedLifeSpanInDays = Integer.parseInt(createExpectedLifespanString);
				if (ViewUtils
						.successful(ItemTypeController.addItemType(createNewItemTypeString, expectedLifeSpanInDays))) {
					createNewItemTypeTextField.setText("");
					createExpectedLifespanTextField.setText("");
					itemTypeTableView.setItems(ViewUtils.getItemTypes());
					itemTypeTableView.refresh();
					WareFlowFxmlView.getInstance().refresh();
				}

			} catch (NumberFormatException e) {
				ViewUtils.showError("The Expected Lifespan must be a numeric value");
			}
		}
	}

	/**
	 * @author Marc Abou-Abdallah
	 */
	@FXML
	public void updateButtonClicked(ActionEvent event) {
		String updateOldItemTypeString = updateOldItemTypeTextField.getText();
		String updateItemTypeString = updateNewItemTypeTextField.getText();
		String updateExpectedLifespanString = updateExpectedLifespanTextField.getText();

		if (updateOldItemTypeString == null || updateOldItemTypeString.trim().isEmpty()) {
			ViewUtils.showError("The Old Item Type field cannot be empty");
		} else if (updateItemTypeString == null || updateItemTypeString.trim().isEmpty()) {
			ViewUtils.showError("The New Item Type field cannot be empty");
		} else if (updateExpectedLifespanString == null || updateExpectedLifespanString.trim().isEmpty()) {
			ViewUtils.showError("The Expected Lifespan field cannot be empty");
		} else {
			try {
				int expectedLifeSpanInDays = Integer.parseInt(updateExpectedLifespanString);
				if (ViewUtils.successful(ItemTypeController.updateItemType(updateOldItemTypeString,
						updateItemTypeString, expectedLifeSpanInDays))) {
					updateOldItemTypeTextField.setText("");
					updateNewItemTypeTextField.setText("");
					updateExpectedLifespanTextField.setText("");
					itemTypeTableView.setItems(ViewUtils.getItemTypes());
					WareFlowFxmlView.getInstance().refresh();
					itemTypeTableView.refresh();
				}
			} catch (NumberFormatException e) {
				ViewUtils.showError("The Expected Lifespan must be a number");
			}
		}
	}

	/**
	 * @author Marc Abou-Abdallah
	 */
	@FXML
	public void deleteButtonClicked(ActionEvent event) {
		String deleteItemTypeString = deleteItemTypeTextField.getText().trim();

		if (deleteItemTypeString.isEmpty()) {
			ViewUtils.showError("The Item Type field to be deleted cannot be empty");
			return;
		}

		// Check if the ItemType exists using the list from ViewUtils
		boolean exists = false;
		for (ItemType itemType : ViewUtils.getItemTypes()) {
			if (itemType.getName().equalsIgnoreCase(deleteItemTypeString)) {
				exists = true;
				break;
			}
		}

		if (!exists) {
			ViewUtils.showError("The item type you tried to delete does not exist");
			return;
		}
		ItemTypeController.deleteItemType(deleteItemTypeString);
		itemTypeTableView.setItems(ViewUtils.getItemTypes());
		itemTypeTableView.refresh();
		deleteItemTypeTextField.setText("");
		WareFlowFxmlView.getInstance().refresh();
	}

}

