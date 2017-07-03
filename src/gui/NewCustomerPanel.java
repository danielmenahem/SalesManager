package gui;

import javafx.scene.layout.*;
import javafx.scene.control.*;
import bl.controlers.AppManager;
import javafx.application.Platform;
import javafx.geometry.Insets;



public class NewCustomerPanel extends VBox{
	
	private static String [] MESSAGES = new String []{
			"Customer already exist"
			,"Customer name is required"
			,"Customer ID is required"
			,"Customer ID must include numbers only"
			,"New Customer Added"};
	
	private GridPane paneDetails = new GridPane();
	private Label lblCustomerName = new Label("Customer Name: ");
	private Label lblCustomerID = new Label("Customer Id: ");
	private Label lblMsg = new Label();
	private TextField tfCustomerName = new TextField();	
	private TextField tfCustomerID = new TextField();
	private Button btnAddCustomer = new Button("Add Customer");
	private Button btnClear = new Button("Clear");
	
	private AppManager manager;
	
	public NewCustomerPanel(){
		super();
		this.manager = AppManager.getInstance();
		buildGui();
		this.setPadding(new Insets(70,10,10,30));
		this.getChildren().addAll(paneDetails, lblMsg);
		this.setStyle(StylePatterns.CONTENT_PANE_CSS);
	}

	private void buildGui() {
		paneDetails.setHgap(10);
		paneDetails.setVgap(20);
		paneDetails.add(lblCustomerName,0, 0);
		paneDetails.add(tfCustomerName,1, 0);
		paneDetails.add(lblCustomerID,0, 1);
		paneDetails.add(tfCustomerID,1, 1);
		paneDetails.add(btnAddCustomer, 0, 2);
		paneDetails.add(btnClear, 1, 2);
		lblMsg.setPadding(new Insets(20,0,0,0));
		
		lblCustomerID.setStyle(StylePatterns.LABEL_CSS);
		lblCustomerName.setStyle(StylePatterns.LABEL_CSS);
		lblMsg.setStyle(StylePatterns.LABEL_MESSAGE_CSS);
		tfCustomerID.setStyle(StylePatterns.TEXT_FIELD_CSS);
		tfCustomerName.setStyle(StylePatterns.TEXT_FIELD_CSS);
		btnAddCustomer.setStyle(StylePatterns.ACTION_BUTTON_CSS);
		btnClear.setStyle(StylePatterns.ACTION_BUTTON_CSS);
		
		setButtonsActions();
	}

	private void setButtonsActions() {
		btnAddCustomer.setOnMousePressed(e -> setBtnPressedStyle(btnAddCustomer));
		btnAddCustomer.setOnMouseReleased(e -> setBtnReleasedStyle(btnAddCustomer));
		btnAddCustomer.setOnAction(e -> onAddPress());
		
		btnClear.setOnMousePressed(e -> setBtnPressedStyle(btnClear));
		btnClear.setOnMouseReleased(e -> setBtnReleasedStyle(btnClear));
		btnClear.setOnAction(e -> clear(true));
	}
	
	private void onAddPress(){
		if(tfCustomerName.getText().equals(""))
			setMsg(1, StylePatterns.LABEL_MESSAGE_CSS);
		else if(tfCustomerID.getText().equals(""))
			setMsg(2, StylePatterns.LABEL_MESSAGE_CSS);
		else{
			try{
				int id = Integer.parseInt(tfCustomerID.getText());
				try{
					manager.insertNewCustomer(id,tfCustomerName.getText());
					setMsg(4,StylePatterns.LABEL_CONFIRM_MESSAGE_CSS);
					clear(false);
				}
				catch(Exception ex){
					setMsg(0,StylePatterns.LABEL_MESSAGE_CSS);
				}
			}
			catch(Exception ex){
				setMsg(3, StylePatterns.LABEL_MESSAGE_CSS);
				ex.printStackTrace();
			}
		}
	}
	
	private void clear(boolean isOnClearAction){
		tfCustomerID.setText("");
		tfCustomerName.setText("");
		if(isOnClearAction)
			lblMsg.setText("");
	}
	
	private void setMsg(int msgNum, String style) {
		Platform.runLater(() -> {				
			lblMsg.setText(MESSAGES[msgNum]);
			lblMsg.setStyle(style);
		});
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
