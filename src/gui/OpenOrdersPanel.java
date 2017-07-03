package gui;

import java.sql.Timestamp;

import bl.controlers.AppManager;
import bl.reports.OpenOrders;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;

public class OpenOrdersPanel extends BorderPane{
	
	private AppManager manager;
	
	private TableView<OpenOrders> table;
	private TableColumn<OpenOrders, Integer> tcOrderID = new TableColumn<>("orderID");
	private TableColumn<OpenOrders, Integer> tcCustomerID = new TableColumn<>("customerID");
	private TableColumn<OpenOrders, String> tcCustomerName = new TableColumn<>("name");
	private TableColumn<OpenOrders, Timestamp> tcOrderDate = new TableColumn<>("orderDate");
	
	public OpenOrdersPanel(){
		super();
		manager = AppManager.getInstance();
		buildTable();
		this.setCenter(table);
		this.setPadding(new Insets(40,20,20,20));
		this.setStyle(StylePatterns.CONTENT_PANE_CSS);
	}
	

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void buildTable() {
		table = new TableView(FXCollections.observableList(manager.getOpenOrders()));
		tcOrderID.setCellValueFactory(new PropertyValueFactory<OpenOrders, Integer>("orderID"));
		tcCustomerID.setCellValueFactory(new PropertyValueFactory<OpenOrders, Integer>("customerID"));
		tcCustomerName.setCellValueFactory(new PropertyValueFactory<OpenOrders, String>("name"));
		tcOrderDate.setCellValueFactory(new PropertyValueFactory<OpenOrders, Timestamp>("orderDate"));

		table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		tcOrderID.setText("Order ID");
		tcCustomerID.setText("Customer ID");
		tcCustomerName.setText("Customer Name");
		tcOrderDate.setText("Order Date");
		table.getColumns().setAll(tcOrderID, tcCustomerID, tcCustomerName, tcOrderDate);
		for(TableColumn<?,?> tc : table.getColumns()){
			tc.setStyle(StylePatterns.TABLE_CELL_CSS);
		}
	}
}
