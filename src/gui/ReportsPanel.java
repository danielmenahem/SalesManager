package gui;

import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;

public class ReportsPanel extends BorderPane{
	
	
	private VBox paneSubManu = new VBox();
	private Button btnBalanceReport = new Button("Items Balance\n Report");
	private Button btnOpenOrderReport = new Button("Open Orders\n Report");
	private Button btnTransactionReport = new Button("Transactions\n Report");
	private Button btnSalesReport = new Button("Sales Report");
	
	private BalanceReportPanel paneBalanceReport;
	private OpenOrdersPanel paneOpenOrders;
	private TransactionsPanel paneTransactions;
	private SalesReportPannel paneSalesReport;
	
	public ReportsPanel(Stage stage){
		buildSubManu();
		paneBalanceReport = new BalanceReportPanel();
		paneOpenOrders = new OpenOrdersPanel();
		paneTransactions = new TransactionsPanel();
		paneSalesReport = new SalesReportPannel();
		this.setLeft(paneSubManu);
	}
	
	
	private void buildSubManu() {
		paneSubManu.getChildren().addAll(btnBalanceReport, btnOpenOrderReport, btnTransactionReport,btnSalesReport);
		setSubManuButtonsStyle();
		
		setSubManuButtonsActions();
		
		paneSubManu.setPadding(new Insets(90, 30, 10, 30));
		paneSubManu.setSpacing(60);
		paneSubManu.setStyle(StylePatterns.SUB_MANU_PANE_CSS);
	}

	private void setSubManuButtonsActions() {
		btnBalanceReport.setOnMousePressed(e->setBtnPressedStyle(btnBalanceReport));
		btnBalanceReport.setOnMouseReleased(e->setBtnReleasedStyle(btnBalanceReport));
		btnBalanceReport.setOnAction(e->{
			this.setCenter(paneBalanceReport);
			setAlignment(paneBalanceReport, Pos.CENTER);

		});
		
		
		btnOpenOrderReport.setOnMousePressed(e->setBtnPressedStyle(btnOpenOrderReport));
		btnOpenOrderReport.setOnMouseReleased(e->setBtnReleasedStyle(btnOpenOrderReport));
		btnOpenOrderReport.setOnAction(e->{
			this.setCenter(paneOpenOrders);
			setAlignment(paneOpenOrders, Pos.CENTER);
		});
		
		btnTransactionReport.setOnMousePressed(e->setBtnPressedStyle(btnTransactionReport));
		btnTransactionReport.setOnMouseReleased(e->setBtnReleasedStyle(btnTransactionReport));
		btnTransactionReport.setOnAction(e->{
			this.setCenter(paneTransactions);
			setAlignment(paneTransactions, Pos.CENTER);
		});
		
		
		btnSalesReport.setOnMousePressed(e->setBtnPressedStyle(btnSalesReport));
		btnSalesReport.setOnMouseReleased(e->setBtnReleasedStyle(btnSalesReport));
		btnSalesReport.setOnAction(e->{
			this.setCenter(paneSalesReport);
			setAlignment(paneSalesReport, Pos.CENTER);
		});

	}
	
	private void setBtnPressedStyle(Button btn){
		Platform.runLater(() -> {				
			btn.setStyle(StylePatterns.SUB_MANU_BUTTON_HOVERD_CSS);
		});
	}
	
	private void setBtnReleasedStyle(Button btn){
		Platform.runLater(() -> {	
			setSubManuButtonsStyle();
			btn.setStyle(StylePatterns.SUB_MANU_SELECTED_BUTTON_CSS);
		});
	}
	
	private void setSubManuButtonsStyle(){
		btnBalanceReport.setStyle(StylePatterns.SUB_MANU_BUTTON_CSS);
		btnOpenOrderReport.setStyle(StylePatterns.SUB_MANU_BUTTON_CSS);
		btnTransactionReport.setStyle(StylePatterns.SUB_MANU_BUTTON_CSS);
		btnSalesReport.setStyle(StylePatterns.SUB_MANU_BUTTON_CSS);
	}
	
	

}
