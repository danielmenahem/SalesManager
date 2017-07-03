package gui;

import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.control.*;

import bl.controlers.AppManager;
import bl.entities.Order;
import javafx.application.Platform;
import javafx.geometry.Insets;

public class CancelOrderPanel extends VBox{
	
	private static String MESSAGES [] = new String []{
			"Order ID is required"
				,"Order ID must contain digits only"
				,"Order ID does not exist in system"
				,"Selected Order already closed"
				,"Order removale available"
	};		
	
	private AppManager manager;
	private Order order;
	private Stage stage;
	
	private VBox paneOrder = new VBox();
	private HBox paneOrderID = new HBox();
	private Label lblOrderID = new Label("Insert Order ID: ");
	private TextField tfOrderID = new TextField();
	private Label lblMsg = new Label();
	private Button btnStartEditOrder = new Button("Show Order");
	
	private TextArea taOrderDetails = new TextArea();
	
	private HBox paneDeleteOrCancel = new HBox();
	private Button btnDeletedOrder = new Button("Remove Order");
	private Button btnCancel = new Button("Cancel");
	private Label lblDeleteMsg = new Label();
	
	public CancelOrderPanel(Stage stage){
		super();
		this.stage = stage;
		manager = AppManager.getInstance();
		buildOrderPane();
		buildDeleteOrCancelPannel();
		setDeleteOrderPanesVisability(false);
		taOrderDetails.setStyle(StylePatterns.TA_ITEM_CSS);
		this.getChildren().addAll(paneOrder, taOrderDetails, paneDeleteOrCancel);
		this.setPadding(new Insets(70,30,10,30));
		this.setSpacing(30);
		this.setStyle(StylePatterns.CONTENT_PANE_CSS);
	}
	
	
	private void buildOrderPane() {
		paneOrderID.getChildren().addAll(lblOrderID,tfOrderID);
		paneOrderID.setSpacing(15);
		paneOrder.getChildren().addAll(paneOrderID, btnStartEditOrder, lblMsg);
		paneOrder.setSpacing(10);
		paneOrder.setPadding(new Insets(10,0,0,250));
		lblOrderID.setStyle(StylePatterns.LABEL_CSS);
		lblMsg.setStyle(StylePatterns.LABEL_MESSAGE_CSS);
		tfOrderID.setStyle(StylePatterns.TEXT_FIELD_CSS);
		btnStartEditOrder.setStyle(StylePatterns.ACTION_BUTTON_CSS);
		paneOrder.setStyle(StylePatterns.CONTENT_PANE_CSS);
		
		btnStartEditOrder.setOnMousePressed(e->setBtnPressedStyle(btnStartEditOrder));
		btnStartEditOrder.setOnMouseReleased(e->setBtnReleasedStyle(btnStartEditOrder));
		btnStartEditOrder.setOnAction(e -> onStartDeletePress());
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
	
	private void onStartDeletePress() {
		if(tfOrderID.getText().equals(""))
			showMsg(MESSAGES[0], StylePatterns.LABEL_MESSAGE_CSS);
		else{
			try{					
				int id = Integer.parseInt(tfOrderID.getText());
				if(manager.isOrderExist(id)){
					if(manager.isOrderOpen(id)){
						this.order = manager.getOrder(id);
						showMsg(MESSAGES[4],StylePatterns.LABEL_CONFIRM_MESSAGE_CSS);
						setDeleteOrderPanesVisability(true);
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
			}
		}
	}
	
	private void showMsg(String msg, String style) {
		Platform.runLater(() -> {				
			lblMsg.setStyle(style);
			lblMsg.setText(msg);
		});
	}

	private void setDeleteOrderPanesVisability(boolean isVisible) {
		taOrderDetails.setVisible(isVisible);
		paneDeleteOrCancel.setVisible(isVisible);
		
	}
	
	private void buildDeleteOrCancelPannel() {
		paneDeleteOrCancel.getChildren().addAll(btnDeletedOrder, btnCancel, lblDeleteMsg);
		paneDeleteOrCancel.setSpacing(15);
		btnDeletedOrder.setStyle(StylePatterns.MAJOR_ACTION_BUTTON_CSS);
		btnCancel.setStyle(StylePatterns.MAJOR_ACTION_BUTTON_CSS);
		lblDeleteMsg.setStyle(StylePatterns.LABEL_MESSAGE_CSS);

		btnDeletedOrder.setOnMousePressed(e -> setMajorBtnPressedStyle(btnDeletedOrder));
		btnDeletedOrder.setOnMouseReleased(e -> setMajorBtnReleasedStyle(btnDeletedOrder));
		btnDeletedOrder.setOnAction( e-> onDeleteOrderPress());
		
		btnCancel.setOnAction(e->clearAllOrder());
		btnCancel.setOnMousePressed(e -> setMajorBtnPressedStyle(btnCancel));
		btnCancel.setOnMouseReleased(e -> setMajorBtnReleasedStyle(btnCancel));
		paneDeleteOrCancel.setPadding(new Insets(0,0,20,0));
		
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
	
	private void onDeleteOrderPress() {
		try{			
			manager.deleteOrder(order);
			String s = order.toString();
			Platform.runLater(() -> {
				stage.setAlwaysOnTop(false);
				Stage dialog = new MessageForm("Order Deleted\n"+s);
				//Stage dialog = new MessageForm("avsddad sdsad\nsadsd dsda");
				try {
					dialog.showAndWait();
				} catch (Exception ex) {
				}
			});
			clearAllOrder();
		}

		catch(Exception ex){
			Platform.runLater(()->{
				lblDeleteMsg.setText(ex.getMessage());
			});
		}
	}

	public void clearAllOrder() {
		tfOrderID.setEditable(true);
		tfOrderID.setText("");
		lblMsg.setText("");
		taOrderDetails.setText("");
		lblDeleteMsg.setText("");
		order = null;
		setDeleteOrderPanesVisability(false);
	}
	
}


