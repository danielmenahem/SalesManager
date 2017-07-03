package gui;

import bl.controlers.AppManager;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Background;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class MainScreen extends Application{
	
	private AppManager manager;	
	private Stage primaryStage;
	
	private static Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
	private BorderPane paneMane = new BorderPane();
	private VBox paneManu = new VBox();
	
	private Button btnNewUserPnl = new Button("Add New\nCustomer");
	private Button btnOrderPnl = new Button("Orders");
	private Button btnInvoicePnl = new Button("Invoices");
	private Button btnReportsPnl = new Button("Reports");
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		manager = AppManager.getInstance();
		this.primaryStage = primaryStage;
		buildManu();
		paneMane.setLeft(paneManu);
		Pane paneTop = new Pane();
		paneTop.setMaxHeight(20);
		paneTop.setMinHeight(20);
		paneTop.setStyle("-fx-background-color:#0E0045; ");
		paneMane.setTop(paneTop);
		BackgroundImage background = new BackgroundImage(new Image("/images/sales.png"),
		        BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
		         BackgroundSize.DEFAULT);
		Pane paneBackground = new Pane();
		paneBackground.setBackground(new Background(background));
		paneMane.setCenter(paneBackground);
        primaryStage.initStyle(StageStyle.UTILITY);
		primaryStage.setTitle("Sales Manager");
		primaryStage.setScene(new Scene(paneMane,primaryScreenBounds.getWidth()/1.01,primaryScreenBounds.getHeight()/1.05));
		primaryStage.show();
		primaryStage.setAlwaysOnTop(true);
		primaryStage.setOnCloseRequest(e -> {
			manager.closeConnection();
		});
		
	}
	
	
	private void buildManu() {
		setButtonsStyle();
		setManuButtonsActions();
		
		paneManu.setPadding(new Insets(70, 10, 10, 10));
		paneManu.setSpacing(50);
		
		paneManu.getChildren().addAll(btnNewUserPnl, btnOrderPnl, btnInvoicePnl, btnReportsPnl);
		paneManu.setStyle(StylePatterns.MANU_PANE_CSS);
		
	}


	private void setManuButtonsActions() {
		btnNewUserPnl.setOnMousePressed(e->{
			setBtnPressedStyle(btnNewUserPnl);
		});
		btnNewUserPnl.setOnMouseReleased(e->{
			setBtnReleasedStyle(btnNewUserPnl);
		});
		btnNewUserPnl.setOnAction(e -> paneMane.setCenter(new NewCustomerPanel()));
		
		btnOrderPnl.setOnMousePressed(e->{
			setBtnPressedStyle(btnOrderPnl);
		});
		btnOrderPnl.setOnMouseReleased(e->{
			setBtnReleasedStyle(btnOrderPnl);
		});
		btnOrderPnl.setOnAction(e -> paneMane.setCenter(new OrderPanel(primaryStage)));
		
		btnInvoicePnl.setOnMousePressed(e->{
			setBtnPressedStyle(btnInvoicePnl);
		});
		btnInvoicePnl.setOnMouseReleased(e->{
			setBtnReleasedStyle(btnInvoicePnl);
		});
		btnInvoicePnl.setOnAction(e -> paneMane.setCenter(new InvoicePanel(primaryStage)));
		
		btnReportsPnl.setOnMousePressed(e->{
			setBtnPressedStyle(btnReportsPnl);
		});
		btnReportsPnl.setOnMouseReleased(e->{
			setBtnReleasedStyle(btnReportsPnl);
		});
		btnReportsPnl.setOnAction(e -> paneMane.setCenter(new ReportsPanel(primaryStage)));
		
		
	}
	
	private void setButtonsStyle(){
		btnNewUserPnl.setStyle(StylePatterns.MANU_BUTTON_CSS);
		btnOrderPnl.setStyle(StylePatterns.MANU_BUTTON_CSS);
		btnInvoicePnl.setStyle(StylePatterns.MANU_BUTTON_CSS);
		btnReportsPnl.setStyle(StylePatterns.MANU_BUTTON_CSS);
	}
	
	private void setBtnPressedStyle(Button btn){
		Platform.runLater(() -> {				
			btn.setStyle(StylePatterns.MANU_BUTTON_HOVERD_CSS);
		});
	}
	
	private void setBtnReleasedStyle(Button btn){
		Platform.runLater(() -> {			
			setButtonsStyle();
			btn.setStyle(StylePatterns.MANU_SELECTED_BUTTON_CSS);
		});
	}


	public static void main(String[] args) {
		try{
			
			System.out.println("Connected");
		}
		catch(Exception e){
			System.out.println(e.getMessage());
		}
		launch(args);
	}

}
