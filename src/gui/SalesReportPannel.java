package gui;

import java.sql.Timestamp;

import bl.controlers.AppManager;
import bl.reports.Sales;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;

public class SalesReportPannel extends BorderPane{
	
	private AppManager manager;
	
	private TableView<Sales> table = new TableView<>();
	private TableColumn<Sales, String> tcItem = new TableColumn<>("itemName");
	private TableColumn<Sales, Integer> tcQuantity = new TableColumn<>("quantity");
	
	private HBox paneDates = new HBox();
	private DatePicker fromDate = new DatePicker();
	private DatePicker toDate = new DatePicker();
	private Button btnGetReport = new Button("Get Rerport");
	
	public SalesReportPannel(){
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
					System.out.println("A");
					table.setItems(FXCollections.observableList(manager.getSalesReport(from, to)));
				}
			}
			catch(Exception ex){}
		});
	}

	@SuppressWarnings("unchecked")
	private void buildTable() {
		table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		tcItem.setCellValueFactory(new PropertyValueFactory<Sales, String>("itemName"));
		tcQuantity.setCellValueFactory(new PropertyValueFactory<Sales, Integer>("quantity"));

		tcItem.setText("Item ID");
		tcQuantity.setText("Quantity");
		table.getColumns().setAll(tcItem, tcQuantity);
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
