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

public class EmployeeController {

	@FXML
	private TextField employeeUsernameTextField;
	@FXML
	private PasswordField passwordTextField;
	@FXML
	private TextField employeeNameTextField;
	@FXML
	private TextField employeePhoneTextField;
	@FXML
	private Button addEmployeeButton;
	@FXML
	private TextField newEmployeeUsernameTextField;
	@FXML
	private PasswordField newPasswordTextField;
	@FXML
	private TextField newEmployeeNameTextField;
	@FXML
	private TextField newEmployeePhoneTextField;
	@FXML
	private Button updateEmployeeButton;
	@FXML
	private TextField deleteEmployeeUsernameTextField;
	@FXML
	private Button deleteEmployeeButton;

	/**
	 * @author Tarek El-Akkaoui
	 */
	@FXML
	private void addEmployeeClicked(ActionEvent event) {
		String employeeUsername = employeeUsernameTextField.getText();
		String password = passwordTextField.getText();
		String employeeName = employeeNameTextField.getText();
		String employeePhone = employeePhoneTextField.getText();

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

		else {
			String errorMessage = UserController.addEmployeeOrClient(employeeUsername, password, employeeName,
					employeePhone, true, "");

			if (errorMessage.isEmpty()) {
				employeeUsernameTextField.setText("");
				passwordTextField.setText("");
				employeeNameTextField.setText("");
				employeePhoneTextField.setText("");
				ViewUtils.showConfirmation("User Added Successfuly!");
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
	 * @author Tarek El-Akkaoui
	 */
	@FXML
	private void updateEmployeeClicked(ActionEvent event) {
		// Get user input
		String employeeUsername = newEmployeeUsernameTextField.getText();
		String password = newPasswordTextField.getText();
		String employeeName = newEmployeeNameTextField.getText();
		String employeePhone = newEmployeePhoneTextField.getText();

		if (employeeUsername == null || employeeUsername.isEmpty()) {
			ViewUtils.showError("The username cannot be empty");
			return;
		}
		if (employeeName == null) {
			employeeName = "";
		}
		if (employeePhone == null) {
			employeePhone = "";
		}
		if (password == null) {
			ViewUtils.showError("The new user password cannot be empty");
			return;
		} else {

			String errorMessage = UserController.updateEmployeeOrClient(employeeUsername, password, employeeName,
					employeePhone, "");
			if (!errorMessage.isEmpty()) {
				ViewUtils.showError(errorMessage);
				return;
			} else {
				newEmployeeUsernameTextField.setText("");
				newPasswordTextField.setText("");
				newEmployeeNameTextField.setText("");
				newEmployeePhoneTextField.setText("");
				ViewUtils.showConfirmation("User Updated Successfuly!");
				WareFlowFxmlView.getInstance().refresh();
				return;
			}
		}

	}

	/**
	 * @author Tarek El-Akkaoui
	 */
	@FXML
	private void deleteEmployeeClicked(ActionEvent event) {
		// Get username to delete
		String username = deleteEmployeeUsernameTextField.getText();

		if (username == null || username.isEmpty()) {
			ViewUtils.showError("The username cannot be empty");
			return;
		}
		if (!UserController.userExists(username)) {
			ViewUtils.showError("This user does not exist");
			return;
		} else {
			UserController.deleteEmployeeOrClient(username);
			deleteEmployeeUsernameTextField.setText("");
			ViewUtils.showConfirmation("User Deleted Successfuly!");
			WareFlowFxmlView.getInstance().refresh();
			return;
		}

	}
}

 