package ca.mcgill.ecse.wareflow.controller;

import java.util.ArrayList;
import java.util.List;

import ca.mcgill.ecse.wareflow.application.WareFlowApplication;
import ca.mcgill.ecse.wareflow.model.Client;
import ca.mcgill.ecse.wareflow.model.Employee;
import ca.mcgill.ecse.wareflow.model.ItemType;
import ca.mcgill.ecse.wareflow.model.Manager;
import ca.mcgill.ecse.wareflow.model.User;
import ca.mcgill.ecse.wareflow.model.WareFlow;
import ca.mcgill.ecse.wareflow.model.WarehouseStaff;
import ca.mcgill.ecse.wareflow.persistence.WareflowPersistence;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class UserController {

	// We need a global instance of the WareFlow class as it will be used across the
	// file.
	private static WareFlow wareFlow = WareFlowApplication.getWareFlow();

	/**
	 * 
	 * @author Marc Abou Abdallah, Tarek el-Akkaoui
	 * 
	 *         This helper method returns a boolean indicating whether a string
	 *         contains exactly one of "#$!". It will be called when checking the
	 *         new password of the manager
	 * @param str the string to validate
	 * @return True if the string contains exactly one of the characters, False
	 *         otherwise
	 *
	 */
	private static boolean charCheck(String str) {
		int count = 0;
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);

			if (c == '!' || c == '$' || c == '#') {
				count++;
				if (count > 1) {
					return false;
				}
			}
		}
		// return whether the count is 1 or not
		return count == 1;
	}

	/**
	 * @author Marc Abou Abdallah, Tarek el-Akkaoui
	 *
	 *         This method updates the password of the manager only if the given
	 *         password is a valid string.
	 */
	public static String updateManager(String password) {

		if (password.equals("")) {
			return "Password cannot be empty";
		} else if (password.length() < 4) {
			return "Password must be at least four characters long";
			// check if password contains exactly one lower-case letter
		} else if (!password.matches("[^a-z]*[a-z][^a-z]*")) {
			return "Password must contain one lower-case character";
			// check if password contains exactly one upper-case letter
		} else if (!password.matches("[^A-Z]*[A-Z][^A-Z]*")) {
			return "Password must contain one upper-case character";
		} else if (!charCheck(password)) {
			return "Password must contain one character out of !#$";
		}

		try {
			Manager manager = (Manager) Manager.getWithUsername("manager");
			manager.setPassword(password);
			WareflowPersistence.save();
			return "";
		} catch (Exception e) {
			return ("Something went wrong, full error message:" + e.getMessage());
		}
	}

	public static String resetPassword() {
		Manager manager = (Manager) Manager.getWithUsername("manager");
		manager.setPassword("password");
		WareflowPersistence.save();
		return "";
	}

	/**
	 * @author Marc Abou Abdallah, Tarek el-Akkaoui
	 *
	 *         This method adds an employee or a client with the given user name,
	 *         name , password, phone number and address (in the case of a client)
	 *         if these given parameters are valid
	 */
	public static String addEmployeeOrClient(String username, String password, String name, String phoneNumber,
			boolean isEmployee, String address) {

		if (username.equals("manager")) {
			return "Username cannot be manager";
		} else if (username == null || username.equals("")) {
			return "Username cannot be empty";
		}
		// check if user name is alphanumeric
		else if (!username.matches("^[a-zA-Z0-9]+$")) {
			return "Invalid username";
		} else if (password == null || password.equals("")) {
			return "Password cannot be empty";
		} else if (!isEmployee) {
			User user = User.getWithUsername(username);
			if (user != null && user instanceof Client) {
				return "Username already linked to a client account";
			}
		} else if (isEmployee) {
			User user = User.getWithUsername(username);
			if (user != null && user instanceof Employee) {
				return "Username already linked to an employee account";
			}
		}

		try {
			if (isEmployee) {
				wareFlow.addEmployee(username, name, password, phoneNumber);
				WareflowPersistence.save();
			}
			if (!isEmployee) {
				wareFlow.addClient(username, name, password, phoneNumber, address);
				WareflowPersistence.save();
			}
			return "";
		} catch (Exception e) {
			return ("Something went wrong, full error message:" + e.getMessage());
		}
	}

	/**
	 * @author Marc Abou Abdallah, Tarek el-Akkaoui 
	 * 
	 *         This method updates the user name, password, name, phone number and
	 *         address (in the case of a client)
	 */
	public static String updateEmployeeOrClient(String username, String newPassword, String newName,
			String newPhoneNumber, String newAddress) {

		if (newPassword.equals("")) {
			return "Password cannot be empty";
		}

		try {
			User user = User.getWithUsername(username);

			// Update the name, password and phone number of the user (client or employee)
			user.setName(newName);
			user.setPassword(newPassword);
			user.setPhoneNumber(newPhoneNumber);
			WareflowPersistence.save();

			// Update the address only if the user is a client
			if (user instanceof Client) {
				Client client = (Client) user;
				client.setAddress(newAddress);
				WareflowPersistence.save();
			}
			return "";
		} catch (Exception e) {
			return ("Something went wrong, full error message:" + e.getMessage());
		}
	}

	/**
	 * @author Marc Abou Abdallah, Tarek el-Akkaoui 
	 * 
	 *         This method deletes the given employee or client
	 */
	public static void deleteEmployeeOrClient(String username) {

		User user = User.getWithUsername(username);
		if (user != null && user instanceof Client) {
			wareFlow.removeClient((Client) user);
			WareflowPersistence.save();

			// delete the user object
			user.delete();
		}
		if (user != null && user instanceof Employee) {
			wareFlow.removeEmployee((Employee) user);
			WareflowPersistence.save();

			// delete the user object
			user.delete();
		}

	}

	/**
	 * @author Tarek El-Akkaoui
	 */
	public static ObservableList<String> getEmployees() {
		List<String> usernames = wareFlow.getEmployees().stream().map(Employee::getUsername).toList();
		List<String> wareFlowEmp = new ArrayList<>();
		for (String u : usernames) {
			wareFlowEmp.add(u);
		}
		wareFlowEmp.add("manager");
		return FXCollections.observableList(wareFlowEmp);
	}

	/**
	 * @author Tarek El-Akkaoui
	 */
	public static boolean userExists(String username) {
		return User.hasWithUsername(username);
	}

}
