package ca.mcgill.ecse.wareflow.javafx.fxml.controllers;

import java.sql.Date;
import ca.mcgill.ecse.wareflow.javafx.fxml.WareFlowFxmlView;
import ca.mcgill.ecse.wareflow.model.ShipmentOrder;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;

public class NoteController {

	@FXML
	private Button AddButton, DeleteButton, UpdateButton;

	@FXML
	private TextField OrderIDTextField, IndexTextField, NoteTakerTextField;

	@FXML
	private TextField newOrderIDTextField, newIndexTextField, newNoteTakerTextField;

	@FXML
	private TextField deletedOrderIDTextField, deletedIndexTextField;

	@FXML
	private TextArea DescriptionTextArea, newDescriptionTextArea;

	@FXML
	private DatePicker NoteDatePicker, newNoteDatePicker;

	/**
	 * @author Anthony Saber
	 * 
	 * Add a note
	 */
	@FXML
	public void addNote(ActionEvent event) {

		try {
			int id = Integer.parseInt(OrderIDTextField.getText());
			String noteTaker = NoteTakerTextField.getText();
			String description = DescriptionTextArea.getText();
			Date date = java.sql.Date.valueOf(NoteDatePicker.getValue());

			if (ViewUtils.successful(ca.mcgill.ecse.wareflow.controller.ShipmentNoteController.addShipmentNote(date,
					description, id, noteTaker))) {
				OrderIDTextField.setText("");
				NoteTakerTextField.setText("");
				DescriptionTextArea.setText("");
			}

		} catch (NumberFormatException e) {
			ViewUtils.showError("Order ID should be a number");
		} catch (Exception e) {
			ViewUtils.showError(e.getMessage());

		}

		WareFlowFxmlView.getInstance().refresh();

	}

	/**
	 * @author Anthony Saber
	 * 
	 * Update a note
	 */
	@FXML
	public void updateNote(ActionEvent event) {

		try {
			int id = Integer.parseInt(newOrderIDTextField.getText());
			String noteTaker = newNoteTakerTextField.getText();
			String description = newDescriptionTextArea.getText();
			Date date = java.sql.Date.valueOf(newNoteDatePicker.getValue());
			int index = Integer.parseInt(newIndexTextField.getText());

			;
			if (ViewUtils.successful(ca.mcgill.ecse.wareflow.controller.ShipmentNoteController.updateShipmentNote(id,
					index, date, description, noteTaker))) {
				newOrderIDTextField.setText("");
				newNoteTakerTextField.setText("");
				newDescriptionTextArea.setText("");
			}
		} catch (NumberFormatException e) {
			ViewUtils.showError("The order ID and index should both be numbers");
		} catch (Exception e) {
			ViewUtils.showError(e.getMessage());
		}

		WareFlowFxmlView.getInstance().refresh();

	}

	/**
	 * @author Anthony Saber
	 * 
	 * Delete a note
	 */
	@FXML
	public void deleteNote(ActionEvent event) {

		try {
			int id = Integer.parseInt(deletedOrderIDTextField.getText());
			int index = Integer.parseInt(deletedIndexTextField.getText());

			if (!ShipmentOrder.hasWithId(id)) {
				ViewUtils.showError("No order with the given ID");
			} else if (!ShipmentOrder.getWithId(id).hasShipmentNotes()) {
				ViewUtils.showError("Order does not have any note.");
			} else {
				ca.mcgill.ecse.wareflow.controller.ShipmentNoteController.deleteShipmentNote(id, index);
				deletedOrderIDTextField.setText("");
				deletedIndexTextField.setText("");
			}
		} catch (NumberFormatException e) {
			ViewUtils.showError("The order ID and index should both be numbers");
		} catch (IndexOutOfBoundsException e) {
			ViewUtils.showError("Invalid index");
		} catch (Exception e) {
			ViewUtils.showError(e.getMessage());
		}

		WareFlowFxmlView.getInstance().refresh();

	}

}