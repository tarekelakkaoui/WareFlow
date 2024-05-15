package ca.mcgill.ecse.wareflow.javafx.fxml.controllers;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import com.google.common.base.Objects;

import ca.mcgill.ecse.wareflow.controller.ShipmentOrderController;
import ca.mcgill.ecse.wareflow.controller.TOShipmentNote;
import ca.mcgill.ecse.wareflow.controller.TOShipmentOrder;
import ca.mcgill.ecse.wareflow.javafx.fxml.WareFlowFxmlView;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class ListViewController {
	@FXML
	private ResourceBundle resources;

	@FXML
	private DatePicker searchByDate;

	@FXML
	private TableView<TOShipmentOrder> orderTable;

	@FXML
	private Button searchOrderButton, searchByPlacerButton, fetchAllOrdersButton, searchByDateButton;

	@FXML
	private TextField searchPlacerTextField, searchOrderTextField;

	/**
	 * @author Anthony Saber, Ahmed Mossa
	 */
	public static TableColumn<TOShipmentOrder, String> createTableColumn(String header, String propertyName) {
		TableColumn<TOShipmentOrder, String> column = new TableColumn<>(header);
		column.setCellValueFactory(new PropertyValueFactory<>(propertyName));
		return column;
	}

	/**
	 * @author Anthony Saber, Ahmed Mossa
	 */
	public void initialize() {
		var idcolumn = createTableColumn("Order ID", "id");
		orderTable.getColumns().add(idcolumn);

		var statusColumn = createTableColumn("Status", "status");
		orderTable.getColumns().add(statusColumn);

		var placedColumn = new TableColumn<TOShipmentOrder, String>("Order Placer");
		placedColumn.setCellValueFactory(data -> Bindings.createStringBinding(() -> data.getValue().getOrderPlacer()));
		orderTable.getColumns().add(placedColumn);

		var descriptionColumn = new TableColumn<TOShipmentOrder, String>("Description");
		descriptionColumn
				.setCellValueFactory(data -> Bindings.createStringBinding(() -> data.getValue().getDescription()));
		orderTable.getColumns().add(descriptionColumn);

		var processedByColumn = new TableColumn<TOShipmentOrder, String>("Processed By");
		processedByColumn
				.setCellValueFactory(data -> Bindings.createStringBinding(() -> data.getValue().getProcessedBy()));
		orderTable.getColumns().add(processedByColumn);

		var timeResColumn = new TableColumn<TOShipmentOrder, String>("Time To Resolve");
		timeResColumn.setCellValueFactory(data -> Bindings.createStringBinding(() -> {
			TOShipmentOrder order = data.getValue();
			if (order != null && order.getTimeToResolve() != null) {
				return order.getTimeToResolve();
			} else {
				return "";
			}
		}));
		orderTable.getColumns().add(timeResColumn);

		orderTable.getColumns().add(createTableColumn("Priority", "priority"));

		orderTable.getColumns().add(createTableColumn("Approval Required", "approvalRequired"));

		var itemNameColumn = new TableColumn<TOShipmentOrder, String>("Item Name");
		itemNameColumn.setCellValueFactory(data -> Bindings.createStringBinding(() -> data.getValue().getItemName()));
		orderTable.getColumns().add(itemNameColumn);

		var lifeSpanColumn = new TableColumn<TOShipmentOrder, String>("Lifespan");
		lifeSpanColumn.setCellValueFactory(data -> Bindings
				.createStringBinding(() -> String.valueOf(data.getValue().getExpectedLifeSpanInDays())));
		orderTable.getColumns().add(lifeSpanColumn);

		var raisedOnDateColumn = new TableColumn<TOShipmentOrder, String>("Added On");
		raisedOnDateColumn.setCellValueFactory(data -> Bindings.createStringBinding(() -> {
			TOShipmentOrder order = data.getValue();
			if (order != null && order.getAddedOnDate() != null) {
				return order.getAddedOnDate().toString();
			} else {
				return "";
			}
		}));
		orderTable.getColumns().add(raisedOnDateColumn);

		var placementDateColumn = new TableColumn<TOShipmentOrder, String>("Placed On");
		placementDateColumn.setCellValueFactory(
				data -> Bindings.createStringBinding(() -> data.getValue().getPlacedOnDate().toString()));
		orderTable.getColumns().add(placementDateColumn);

		orderTable.getColumns().add(createTableColumn("Area Number", "areaNumber"));

		// FloorNumber int
		orderTable.getColumns().add(createTableColumn("Slot Number", "slotNumber"));
		// missing quantity, container number.
		// Order Notes
		var notesColumn = new TableColumn<TOShipmentOrder, String>("Notes");
		notesColumn
				.setCellValueFactory(data -> Bindings.createStringBinding(() -> data.getValue().getNotes().toString()));
		orderTable.getColumns().add(notesColumn);

		// Set colors
		orderTable.setRowFactory(tv -> new TableRow<TOShipmentOrder>() {
			@Override
			protected void updateItem(TOShipmentOrder item, boolean empty) {
				super.updateItem(item, empty);

				if (item == null || item.getStatus() == null) {
					// If the status is null, set the default row color
					setStyle("");
				} else {
					// Set row color based on the value of the "Status" column
					setStyle("");
				}
			}
		});

		searchByDate.setValue(LocalDate.now());

		orderTable.addEventHandler(WareFlowFxmlView.REFRESH_EVENT, e -> orderTable.setItems(getAllOrders()));
		WareFlowFxmlView.getInstance().registerRefreshEvent(orderTable);

		assert orderTable != null : "fx:id=\"orderTable\" was not injected: check your FXML file 'ListViewPage.fxml'.";
		assert searchByDate != null
				: "fx:id=\"searchByDate\" was not injected: check your FXML file 'ListViewPage.fxml'.";
	}

	/**
	 * @author Anthony Saber
	 */
	public ObservableList<TOShipmentOrder> getAllOrders() {
		if (ShipmentOrderController.getOrders() == null) {
			return FXCollections.emptyObservableList();
		}
		return FXCollections.observableList(ShipmentOrderController.getOrders());
	}

	/**
	 * @author Ahmed Mossa
	 */
	@FXML
	void selectedDateChanged(ActionEvent event) {
		WareFlowFxmlView.getInstance().refresh();
	}

	/**
	 * @author Anthony Saber, Ahmed Mossa
	 */
	@FXML
	private void searchOrderButtonClicked(ActionEvent event) {
		String stringID = searchOrderTextField.getText().trim();
		if (stringID.isEmpty()) {
			ViewUtils.showError("Please input a valid order ID");
		} else {
			int id = Integer.parseInt(stringID);
			// Search orders by ID and update the table
			ObservableList<TOShipmentOrder> filteredOrders = getAllOrders().filtered(order -> order.getId() == id);

			if (filteredOrders.isEmpty()) {
				ViewUtils.showError("No orders found with the specified ID");
			} else {
				orderTable.setItems(filteredOrders);
				WareFlowFxmlView.getInstance().registerRefreshEvent(orderTable);
			}
		}
	}

	/**
	 * @author Anthony Saber, Ahmed Mossa
	 */
	@FXML
	private void searchByPlacerButtonClicked(ActionEvent event) {
		String selectedPlacer = searchPlacerTextField.getText().trim();

		if (selectedPlacer == null) {
			ViewUtils.showError("Please input a valid user name");
		} else {

			ObservableList<TOShipmentOrder> filteredOrders = getAllOrders()
					.filtered(order -> order.getOrderPlacer().equals(selectedPlacer));

			if (filteredOrders.isEmpty()) {
				ViewUtils.showError("No orders found with the specified placer");
			} else {

				orderTable.setItems(FXCollections.observableList(filteredOrders));
				WareFlowFxmlView.getInstance().registerRefreshEvent(orderTable);

			}
		}
	}

	/**
	 * @author Anthony Saber, Ahmed Mossa
	 */
	@FXML
	private void searchByDateButtonClicked(ActionEvent event) {
		LocalDate selectedDate = searchByDate.getValue();
		if (selectedDate == null) {
			ViewUtils.showError("Please select a valid date");
		} else {
			var date = Date.valueOf(selectedDate);
			ObservableList<TOShipmentOrder> filteredOrders = getAllOrders()
					.filtered(order -> 0 == order.getPlacedOnDate().compareTo(date));

			if (filteredOrders.isEmpty()) {
				ViewUtils.showError("No orders found with the specified date");
			} else {
				orderTable.setItems(filteredOrders);
				WareFlowFxmlView.getInstance().registerRefreshEvent(orderTable);
			}
		}
	}

	/**
	 * @author Anthony Saber, Ahmed Mossa
	 */
	@FXML
	private void fetchAllOrdersButtonClicked(ActionEvent event) {
		orderTable.setItems(FXCollections.observableList(getAllOrders()));
		WareFlowFxmlView.getInstance().registerRefreshEvent(orderTable);
		WareFlowFxmlView.getInstance().refresh();
	}
}
