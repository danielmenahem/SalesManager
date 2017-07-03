package gui;

import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import bl.controlers.AppManager;
import bl.reports.ItemBalance;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;

public class BalanceReportPanel extends BorderPane {
	
	private AppManager manager;
	
	private TableView<ItemBalance> table;
	private TableColumn<ItemBalance, String> tcItem = new TableColumn<>("itemName");
	private TableColumn<ItemBalance, String> tcWareHouse = new TableColumn<>("wareHouseName");
	private TableColumn<ItemBalance, Integer> tcQuantity = new TableColumn<>("quantity");
	
	public BalanceReportPanel(){
		super();
		manager = AppManager.getInstance();
		buildTable();
		this.setCenter(table);
		this.setPadding(new Insets(40,20,20,20));
		this.setStyle(StylePatterns.CONTENT_PANE_CSS);
	}

	@SuppressWarnings("unchecked")
	private void buildTable() {
		tcItem.setText("Item");
		tcWareHouse.setText("Warehouse");
		tcQuantity.setText("Quantity");
		tcItem.setCellValueFactory(new PropertyValueFactory<ItemBalance, String>("itemName"));
		tcWareHouse.setCellValueFactory(new PropertyValueFactory<ItemBalance, String>("wareHouseName"));
		tcQuantity.setCellValueFactory(new PropertyValueFactory<ItemBalance, Integer>("quantity"));

		table =  new TableView<>(FXCollections.observableList(manager.getItemBalanceReport()));
		table.getColumns().setAll(tcItem, tcWareHouse, tcQuantity);
		table.getColumns().get(0).setVisible(false);
		table.getColumns().get(0).setVisible(true);
		table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		for(TableColumn<?,?> tc : table.getColumns()){
			tc.setStyle(StylePatterns.TABLE_CELL_CSS);
		}
	}
	

}
