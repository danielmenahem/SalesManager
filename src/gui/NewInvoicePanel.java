package gui;

import bl.controlers.AppManager;
import bl.entities.Order;
import bl.entities.Invoice;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.*;


public class NewInvoicePanel extends VBox{
	
	private static String MESSAGES [] = new String []{
			"Order ID is required"
				,"Order ID must contain digits only"
				,"Order ID does not exist in system"
				,"Selected Order already closed"
				,"Invoice Creation Available"
	};
	
	
	private AppManager manager;
	private Order order;
	private Stage stage;
	
	private VBox paneOrder = new VBox();
	private HBox paneOrderID = new HBox();
	private Label lblOrderID = new Label("Insert Order ID: ");
	private TextField tfOrderID = new TextField();
	private Label lblMsg = new Label();
	private Button btnStartInvoiceCreation = new Button("Show Order");
	
	private TextArea taOrderDetails = new TextArea();
	
	private HBox paneCreateOrCancel = new HBox();
	private Button btnCreateInvoice = new Button("Create Invoice");
	private Button btnCancel = new Button("Cancel");
	private Label lblCreateMsg = new Label();
	
	public NewInvoicePanel(Stage stage){
		super();
		buildOrderPane();
		buildCreateInvoicePanel();
		this.stage = stage;
		this.manager = AppManager.getInstance();
		setCreateIvoicePanesVisability(false);
		taOrderDetails.setStyle(StylePatterns.TA_ITEM_CSS);
		this.getChildren().addAll(paneOrder,taOrderDetails, paneCreateOrCancel);
		this.setPadding(new Insets(70,30,10,30));
		this.setSpacing(30);
		this.setStyle(StylePatterns.CONTENT_PANE_CSS);
	}
	
	private void buildOrderPane() {
		paneOrderID.getChildren().addAll(lblOrderID,tfOrderID);
		paneOrderID.setSpacing(15);
		paneOrder.getChildren().addAll(paneOrderID, btnStartInvoiceCreation, lblMsg);
		paneOrder.setSpacing(10);
		paneOrder.setPadding(new Insets(10,0,0,250));
		lblOrderID.setStyle(StylePatterns.LABEL_CSS);
		lblMsg.setStyle(StylePatterns.LABEL_MESSAGE_CSS);
		tfOrderID.setStyle(StylePatterns.TEXT_FIELD_CSS);
		btnStartInvoiceCreation.setStyle(StylePatterns.ACTION_BUTTON_CSS);
		paneOrder.setStyle(StylePatterns.CONTENT_PANE_CSS);
		
		btnStartInvoiceCreation.setOnMousePressed(e->setBtnPressedStyle(btnStartInvoiceCreation));
		btnStartInvoiceCreation.setOnMouseReleased(e->setBtnReleasedStyle(btnStartInvoiceCreation));
		btnStartInvoiceCreation.setOnAction(e -> onStartCreatePress());
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
	
	private void onStartCreatePress() {
		if(tfOrderID.getText().equals(""))
			showMsg(MESSAGES[0], StylePatterns.LABEL_MESSAGE_CSS);
		else{
			try{					
				int id = Integer.parseInt(tfOrderID.getText());
				if(manager.isOrderExist(id)){
					if(manager.isOrderOpen(id)){
						this.order = manager.getOrder(id);
						showMsg(MESSAGES[4],StylePatterns.LABEL_CONFIRM_MESSAGE_CSS);
						setCreateIvoicePanesVisability(true);
						taOrderDetails.setText(order.toString());
						tfOrderID.setEditable(false);
					}
					else{
						showMsg(MESSAGES[3],StylePatterns.LABEL_MESSAGE_CSS);
					}
				}
				else{
					showMsg(MESSAGES[2],StylePatterns.LABEL_MESSAGE_CSS);
				}
			}
			catch(Exception ex){
				showMsg(MESSAGES[1],StylePatterns.LABEL_MESSAGE_CSS);
				ex.printStackTrace();
			}
		}
	}
	
	private void showMsg(String msg, String style) {
		Platform.runLater(() -> {				
			lblMsg.setStyle(style);
			lblMsg.setText(msg);
		});
	}

	private void setCreateIvoicePanesVisability(boolean isVisible) {
		taOrderDetails.setVisible(isVisible);
		paneCreateOrCancel.setVisible(isVisible);
		
	}
	
	private void buildCreateInvoicePanel() {
		paneCreateOrCancel.getChildren().addAll(btnCreateInvoice, btnCancel, lblCreateMsg);
		paneCreateOrCancel.setSpacing(15);
		btnCreateInvoice.setStyle(StylePatterns.MAJOR_ACTION_BUTTON_CSS);
		btnCancel.setStyle(StylePatterns.MAJOR_ACTION_BUTTON_CSS);
		lblCreateMsg.setStyle(StylePatterns.LABEL_MESSAGE_CSS);

		btnCreateInvoice.setOnMousePressed(e -> setMajorBtnPressedStyle(btnCreateInvoice));
		btnCreateInvoice.setOnMouseReleased(e -> setMajorBtnReleasedStyle(btnCreateInvoice));
		btnCreateInvoice.setOnAction( e-> onCreateInvoicePress());
		
		btnCancel.setOnAction(e->clearAllOrder());
		btnCancel.setOnMousePressed(e -> setMajorBtnPressedStyle(btnCancel));
		btnCancel.setOnMouseReleased(e -> setMajorBtnReleasedStyle(btnCancel));
		
	}
	
	
	private void setMajorBtnReleasedStyle(Button btn) {
		Platform.runLater(() -> {	
			btn.setStyle(StylePatterns.MAJOR_ACTION_BUTTON_CSS);
		});
	}


	private void setMajorBtnPressedStyle(Button btn) {
		Platform.runLater(() -> {				
			btn.setStyle(StylePatterns.MAJOR_ACTION_BUTTON_HOVERD_CSS);
		});
	}
	
	private void onCreateInvoicePress() {
		try{			
			Invoice invoice = manager.createInvoice(order);
			Platform.runLater(() -> {
				stage.setAlwaysOnTop(false);
				Stage dialog = new MessageForm("Invoice Created\n"+invoice.toString());
				try {
					dialog.showAndWait();
				} catch (Exception ex) {
				}
			});
			clearAllOrder();

		}
		catch(Exception ex){
			Platform.runLater(()->{
				lblCreateMsg.setText(ex.getMessage());
			});
		}
	}

	public void clearAllOrder() {
		tfOrderID.setEditable(true);
		tfOrderID.setText("");
		lblMsg.setText("");
		taOrderDetails.setText("");
		lblCreateMsg.setText("");
		order = null;
		setCreateIvoicePanesVisability(false);
	}
}

