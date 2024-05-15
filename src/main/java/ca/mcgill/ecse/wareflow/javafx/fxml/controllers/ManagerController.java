package ca.mcgill.ecse.wareflow.javafx.fxml.controllers;

import java.util.List;

import ca.mcgill.ecse.wareflow.controller.ItemContainerController;
import ca.mcgill.ecse.wareflow.controller.ItemTypeController;
import ca.mcgill.ecse.wareflow.controller.UserController;
import ca.mcgill.ecse.wareflow.javafx.fxml.WareFlowFxmlView;
import ca.mcgill.ecse.wareflow.model.ItemType;
import ca.mcgill.ecse.wareflow.model.Manager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ChoiceBox;

public class ManagerController {
	@FXML
	private TextField managerPasswordTextField;
	@FXML
	private TextField passwordTextField;
	@FXML
	private Button newPasswordButton;
	@FXML
	private Button resetPassword;

	/**
	 * @author Tarek El-Akkaoui, Mohamed Abdelhady
	 */
	@FXML
	public void updatePasswordClicked(ActionEvent event) {

		String confirmOldManagerPasswordTextString = managerPasswordTextField.getText();
		String updateManagerPasswordString = passwordTextField.getText();

		if (confirmOldManagerPasswordTextString == null) {
			ViewUtils.showError("The old manager password cannot be empty");
			return;
		}
		if (updateManagerPasswordString == null) {
			ViewUtils.showError("The new password cannot be empty");
			return;
		}
		Manager manager = (Manager) Manager.getWithUsername("manager");
		if (confirmOldManagerPasswordTextString.equals(manager.getPassword())) {
			if (ViewUtils.successful(UserController.updateManager(updateManagerPasswordString))) {
				managerPasswordTextField.setText("");
				passwordTextField.setText("");

			}
		} else {
			ViewUtils.showError("The Old Password does not match");

		}
	}

	/**
	 * @author Tarek El-Akkaoui
	 */
	public void resetPassword(ActionEvent event) {
		if (ViewUtils.successful(UserController.resetPassword())) {
			managerPasswordTextField.setText("");
			passwordTextField.setText("");
		}

	}
}