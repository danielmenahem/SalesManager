package gui;

import java.sql.Timestamp;
import java.time.Duration;
import bl.controlers.AppManager;
import bl.entities.Invoice;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CancelInvoicePanel extends VBox{
	
	private static String MESSAGES [] = new String []{
			"Invoice ID is required"
			,"Invoice ID must contain digits only"
			,"Invoice ID does not exist in system"
			,"Invoice edit period has expired"
			,"Invoice edit available"
	};		
	
	private AppManager manager;
	private Invoice invoice;
	private Stage stage;
	
	private VBox paneInvoice = new VBox();
	private HBox paneInvoiceID = new HBox();
	private Label lblInvoiceID = new Label("Insert Invoice ID: ");
	private TextField tfInvoiceID = new TextField();
	private Label lblMsg = new Label();
	private Button btnStartDelete = new Button("Show Invoice");
	
	private TextArea taInvoiceDetails = new TextArea();
	
	private HBox paneDeleteOrCancel = new HBox();
	private Button btnDeletedInvoice = new Button("Remove Invoice");
	private Button btnCancel = new Button("Cancel");
	private Label lblDeleteMsg = new Label();
	

	public CancelInvoicePanel(Stage stage){
		super();
		this.stage = stage;
		manager = AppManager.getInstance();
		buildOrderPane();
		buildDeleteOrCancelPannel();
		setDeleteInvoicePanesVisability(false);
		taInvoiceDetails.setStyle(StylePatterns.TA_ITEM_CSS);
		this.getChildren().addAll(paneInvoice, taInvoiceDetails, paneDeleteOrCancel);
		this.setPadding(new Insets(70,30,10,30));
		this.setSpacing(30);
		this.setStyle(StylePatterns.CONTENT_PANE_CSS);
	}
	
	
	private void buildOrderPane() {
		paneInvoiceID.getChildren().addAll(lblInvoiceID,tfInvoiceID);
		paneInvoiceID.setSpacing(15);
		paneInvoice.getChildren().addAll(paneInvoiceID, btnStartDelete, lblMsg);
		paneInvoice.setSpacing(10);
		paneInvoice.setPadding(new Insets(10,0,0,250));
		lblInvoiceID.setStyle(StylePatterns.LABEL_CSS);
		lblMsg.setStyle(StylePatterns.LABEL_MESSAGE_CSS);
		tfInvoiceID.setStyle(StylePatterns.TEXT_FIELD_CSS);
		btnStartDelete.setStyle(StylePatterns.ACTION_BUTTON_CSS);
		paneInvoice.setStyle(StylePatterns.CONTENT_PANE_CSS);
		
		btnStartDelete.setOnMousePressed(e->setBtnPressedStyle(btnStartDelete));
		btnStartDelete.setOnMouseReleased(e->setBtnReleasedStyle(btnStartDelete));
		btnStartDelete.setOnAction(e -> onStartDeletePress());
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
		if(tfInvoiceID.getText().equals(""))
			showMsg(MESSAGES[0], StylePatterns.LABEL_MESSAGE_CSS);
		else{
			try{					
				int id = Integer.parseInt(tfInvoiceID.getText());
				if(manager.isInvoiceExist(id)){
					this.invoice = manager.getInvoice(id);
					Timestamp currentDate = new Timestamp(System.currentTimeMillis());
					int daysBetween = 0;
					try{
						daysBetween = (int)Duration.between(currentDate.toLocalDateTime(), invoice.getDate().toLocalDateTime()).toDays();				
					}
					catch(Exception ex){}
					if(daysBetween <= 14){
						this.invoice = manager.getInvoice(id);
						showMsg(MESSAGES[4],StylePatterns.LABEL_CONFIRM_MESSAGE_CSS);
						setDeleteInvoicePanesVisability(true);
						taInvoiceDetails.setText(invoice.toString());
						tfInvoiceID.setEditable(false);
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

	private void setDeleteInvoicePanesVisability(boolean isVisible) {
		taInvoiceDetails.setVisible(isVisible);
		paneDeleteOrCancel.setVisible(isVisible);
		
	}
	
	private void buildDeleteOrCancelPannel() {
		paneDeleteOrCancel.getChildren().addAll(btnDeletedInvoice, btnCancel, lblDeleteMsg);
		paneDeleteOrCancel.setSpacing(15);
		btnDeletedInvoice.setStyle(StylePatterns.MAJOR_ACTION_BUTTON_CSS);
		btnCancel.setStyle(StylePatterns.MAJOR_ACTION_BUTTON_CSS);
		lblDeleteMsg.setStyle(StylePatterns.LABEL_MESSAGE_CSS);

		btnDeletedInvoice.setOnMousePressed(e -> setMajorBtnPressedStyle(btnDeletedInvoice));
		btnDeletedInvoice.setOnMouseReleased(e -> setMajorBtnReleasedStyle(btnDeletedInvoice));
		btnDeletedInvoice.setOnAction( e-> onDeleteInvoicePress());
		
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
	
	private void onDeleteInvoicePress() {
		try{			
			manager.deleteInvoice(invoice);
			String s = invoice.toString();
			Platform.runLater(() -> {
				stage.setAlwaysOnTop(false);
				Stage dialog = new MessageForm("Invoice Deleted\n"+s);
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
		tfInvoiceID.setEditable(true);
		tfInvoiceID.setText("");
		lblMsg.setText("");
		taInvoiceDetails.setText("");
		lblDeleteMsg.setText("");
		invoice = null;
		setDeleteInvoicePanesVisability(false);
	}
	
	

}
