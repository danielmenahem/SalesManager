package gui;

import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Timestamp;

import bl.controlers.AppManager;
import bl.entities.TransactionType;
import bl.reports.Transaction;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;

public class TransactionsPanel extends BorderPane{
	
	private AppManager manager;
	
	private TableView<Transaction> table = new TableView<>();
	private TableColumn<Transaction, String> tcItem = new TableColumn<>("itemName");
	private TableColumn<Transaction, String> tcWareHouse = new TableColumn<>("warehouseName");
	private TableColumn<Transaction, Integer> tcQuantity = new TableColumn<>("quantity");
	private TableColumn<Transaction, TransactionType> tcType = new TableColumn<>("type");
	
	private HBox paneDates = new HBox();
	private DatePicker fromDate = new DatePicker();
	private DatePicker toDate = new DatePicker();
	private Button btnGetReport = new Button("Get Rerport");
	
	public TransactionsPanel(){
		super();
		manager = AppManager.getInstance();
		buildTable();
		buildDatePanel();
		this.setCenter(table);
		this.setTop(paneDates);
		this.setPadding(new Insets(40,20,20,20));
		this.setStyle(StylePatterns.CONTENT_PANE_CSS);
	}
	
	private void buildDatePanel(){
		fromDate.setPromptText("From Date");
		toDate.setPromptText("To Date");
		paneDates.getChildren().addAll(fromDate, toDate,btnGetReport);
		paneDates.setSpacing(20);
		paneDates.setPadding(new Insets(10));
		paneDates.setStyle(StylePatterns.CONTENT_PANE_CSS);
		btnGetReport.setStyle(StylePatterns.ACTION_BUTTON_CSS);
		btnGetReport.setOnMousePressed(e->setBtnPressedStyle(btnGetReport));
		btnGetReport.setOnMouseReleased(e->setBtnReleasedStyle(btnGetReport));
		btnGetReport.setOnAction(e->{
			try{				
				Timestamp from = Timestamp.valueOf(fromDate.getValue().atStartOfDay());
				Timestamp to = Timestamp.valueOf(toDate.getValue().atStartOfDay());
				if(from != null && to != null){
					table.setItems(FXCollections.observableList(manager.getTransactionReport(from, to)));
				}
			}
			catch(Exception ex){}
		});
	}

	@SuppressWarnings("unchecked")
	private void buildTable() {
		table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		tcItem.setCellValueFactory(new PropertyValueFactory<Transaction, String>("itemName"));
		tcWareHouse.setCellValueFactory(new PropertyValueFactory<Transaction, String>("warehouseName"));
		tcQuantity.setCellValueFactory(new PropertyValueFactory<Transaction, Integer>("quantity"));
		tcType.setCellValueFactory(new PropertyValueFactory<Transaction, TransactionType>("type"));
		
		

		tcItem.setText("Item ID");
		tcWareHouse.setText("Warehouse");
		tcQuantity.setText("Quantity");
		tcType.setText("Transaction Type");
		table.getColumns().setAll(tcItem, tcWareHouse, tcQuantity, tcType);
		for(TableColumn<?,?> tc : table.getColumns()){
			tc.setStyle(StylePatterns.TABLE_CELL_CSS);
		}
	}
	
	private void setBtnPressedStyle(Button btn){
		Platform.runLater(() -> {				
			btn.setStyle(StylePatterns.ACTION_BUTTON_HOVERD_CSS);
		});
	}
	
	private void setBtnReleasedStyle(Button btn){
		Platform.runLater(() -> {	
			btn.setStyle(StylePatterns.ACTION_BUTTON_CSS);
		});
	}

}
