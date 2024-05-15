package ca.mcgill.ecse.wareflow.javafx.fxml.controllers;

import java.sql.Date;
import java.util.List;

import ca.mcgill.ecse.wareflow.javafx.fxml.controllers.ViewUtils;
import ca.mcgill.ecse.wareflow.controller.UserController;
import ca.mcgill.ecse.wareflow.javafx.fxml.WareFlowFxmlView;
import ca.mcgill.ecse.wareflow.model.ItemType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.PasswordField;

public class GuestController {

	@FXML
	private TextField guestUsernameTextField;
	@FXML
	private PasswordField passwordTextField;
	@FXML
	private TextField guestNameTextField;
	@FXML
	private TextField guestPhoneTextField;
	@FXML
	private TextField guestAddressField;
	@FXML
	private Button addGuestButton;
	@FXML
	private TextField newGuestUsernameTextField;
	@FXML
	private PasswordField newPasswordTextField;
	@FXML
	private TextField newGuestNameTextField;
	@FXML
	private TextField newGuestPhoneTextField;
	@FXML
	private TextField newGuestAddressField;
	@FXML
	private Button updateGuestButton;
	@FXML
	private TextField deleteGuestUsernameTextField;
	@FXML
	private Button deleteGuestButton;

	/**
	 * @author Mohamed Abdelhady
	 */
	@FXML
	private void addGuestClicked(ActionEvent event) {
		String employeeUsername = guestUsernameTextField.getText();
		String password = passwordTextField.getText();
		String employeeName = guestNameTextField.getText();
		String employeePhone = guestPhoneTextField.getText();
		String address = guestAddressField.getText();

		if (employeeUsername == null) {
			ViewUtils.showError("Username cannot be empty");
			return;
		}
		if (employeeName == null) {
			employeeName = "";
		}
		if (employeePhone == null) {
			employeePhone = "";
		}
		if (password == null) {
			ViewUtils.showError("Password cannot be empty");
			return;
		}
		if (address == null) {
			ViewUtils.showError("Address cannot be empty");
			return;
		}

		else {
			String errorMessage = UserController.addEmployeeOrClient(employeeUsername, password, employeeName,
					employeePhone, false, address);

			if (errorMessage.isEmpty()) {
				guestUsernameTextField.setText("");
				passwordTextField.setText("");
				guestNameTextField.setText("");
				guestPhoneTextField.setText("");
				guestAddressField.setText("");
				ViewUtils.showConfirmation("Guest Added Successfuly!");
			} else {

				ViewUtils.showError(errorMessage);
				return;

			}
			WareFlowFxmlView.getInstance().refresh();
			;
			return;
		}

	}

	/**
	 * @author Mohamed Abdelhady
	 */
	@FXML
	private void updateGuestClicked(ActionEvent event) {
		// Get user input
		String guestUsername = newGuestUsernameTextField.getText();
		String password = newPasswordTextField.getText();
		String guestName = newGuestNameTextField.getText();
		String guestPhone = newGuestPhoneTextField.getText();
		String address = newGuestAddressField.getText();

		if (guestUsername == null || guestUsername.isEmpty()) {
			ViewUtils.showError("The username cannot be empty");
			return;
		}
		if (guestName == null) {
			guestName = "";
		}
		if (guestPhone == null) {
			guestPhone = "";
		}
		if (password == null) {
			ViewUtils.showError("The new user password cannot be empty");
			return;
		}
		if (address == null) {
			ViewUtils.showError("The new address cannot be empty");
			return;
		} else {

			String errorMessage = UserController.updateEmployeeOrClient(guestUsername, password, guestName, guestPhone,
					address);
			if (!errorMessage.isEmpty()) {
				ViewUtils.showError(errorMessage);
				return;
			} else {
				newGuestUsernameTextField.setText("");
				newPasswordTextField.setText("");
				newGuestNameTextField.setText("");
				newGuestPhoneTextField.setText("");
				newGuestAddressField.setText("");
				ViewUtils.showConfirmation("Guest Updated Successfuly!");
				WareFlowFxmlView.getInstance().refresh();
				return;
			}
		}

	}

	/**
	 * @author Mohamed Abdelhady
	 */
	@FXML
	private void deleteGuestClicked(ActionEvent event) {
		String username = deleteGuestUsernameTextField.getText();

		if (username == null || username.isEmpty()) {
			ViewUtils.showError("The username cannot be empty");
			return;
		}
		if (!UserController.userExists(username)) {
			ViewUtils.showError("This user does not exist");
			return;
		} else {
			UserController.deleteEmployeeOrClient(username);
			deleteGuestUsernameTextField.setText("");
			ViewUtils.showConfirmation("Guest Deleted Successfuly!");
			WareFlowFxmlView.getInstance().refresh();
			return;
		}

	}

}