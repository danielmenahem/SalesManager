package gui;

import java.sql.Timestamp;
import java.time.Duration;
import java.util.ArrayList;
import bl.controlers.AppManager;
import bl.entities.Invoice;
import bl.entities.InvoiceLine;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;


public class EditInvoicePanel extends VBox {
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
	private TextField tfinvoiceID = new TextField();
	private Label lblMsg = new Label();
	private Button btnStartInvoiceCreation = new Button("Start Edit Invoice");
	
	private HBox paneLinesView = new HBox();
	private VBox paneLinesActions = new VBox();
	private ListView<InvoiceLine> lvInvoiceLines;
	private ObservableList<InvoiceLine> invoiceLines;
	private Button btnDeleteLine = new Button("Delete Selected");
	private Button btnDecreaseSelected = new Button("Decrease Selected Quantity");
	
	private HBox paneEditOrCancel = new HBox();
	private Button btnEditInvoice = new Button("Edit Invoice");
	private Button btnCancel = new Button("Cancel");
	private Label lblEditMsg = new Label();
	
	public EditInvoicePanel(Stage stage){
		super();
		this.stage = stage;
		this.manager = AppManager.getInstance();
		buildInvoicePane();
		buildLineViewPane();
		buildEditOrCancelInvoicePanel();
		setEditIvoicePanesVisability(false);
		this.getChildren().addAll(paneInvoice, paneLinesView, paneEditOrCancel);
		this.setPadding(new Insets(70,30,10,30));
		this.setSpacing(30);
		this.setStyle(StylePatterns.CONTENT_PANE_CSS);
	}
	
	private void buildInvoicePane() {
		paneInvoiceID.getChildren().addAll(lblInvoiceID,tfinvoiceID);
		paneInvoiceID.setSpacing(15);
		paneInvoice.getChildren().addAll(paneInvoiceID, btnStartInvoiceCreation, lblMsg);
		paneInvoice.setSpacing(10);
		paneInvoice.setPadding(new Insets(10,0,0,250));
		lblInvoiceID.setStyle(StylePatterns.LABEL_CSS);
		lblMsg.setStyle(StylePatterns.LABEL_MESSAGE_CSS);
		tfinvoiceID.setStyle(StylePatterns.TEXT_FIELD_CSS);
		btnStartInvoiceCreation.setStyle(StylePatterns.ACTION_BUTTON_CSS);
		paneInvoice.setStyle(StylePatterns.CONTENT_PANE_CSS);
		
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
		if(tfinvoiceID.getText().equals(""))
			showMsg(MESSAGES[0], StylePatterns.LABEL_MESSAGE_CSS);
		else{
			try{					
				int id = Integer.parseInt(tfinvoiceID.getText());
				if(manager.isInvoiceExist(id)){
					this.invoice = manager.getInvoice(id);
					Timestamp currentDate = new Timestamp(System.currentTimeMillis());
					int daysBetween = 0;
					try{
						daysBetween = (int)Duration.between(currentDate.toLocalDateTime(), invoice.getDate().toLocalDateTime()).toDays();						
					}
					catch(Exception ex){}
					if(daysBetween <= 14){
						showMsg(MESSAGES[4],StylePatterns.LABEL_CONFIRM_MESSAGE_CSS);
						setEditIvoicePanesVisability(true);
						for(InvoiceLine line : invoice.getLines()){
							invoiceLines.add(line);
						}
						tfinvoiceID.setEditable(false);
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
	
	private void buildLineViewPane() {
		invoiceLines = FXCollections.observableArrayList(new ArrayList<InvoiceLine>());
		lvInvoiceLines = new ListView<>(invoiceLines);
		lvInvoiceLines.setMinWidth(350);
		lvInvoiceLines.setStyle(StylePatterns.LV_ITEM_CSS);
		CustomSkin<?> skin = new CustomSkin<>(this.lvInvoiceLines); // Injected by FXML
		this.lvInvoiceLines.setSkin(skin);
		btnDeleteLine.setStyle(StylePatterns.ACTION_BUTTON_CSS);
		btnDeleteLine.setOnMousePressed(e -> setBtnPressedStyle(btnDeleteLine));
		btnDeleteLine.setOnMouseReleased(e -> setBtnReleasedStyle(btnDeleteLine));
		btnDeleteLine.setOnAction(e->{
			try{
				invoiceLines.remove(lvInvoiceLines.getSelectionModel().getSelectedIndex());
			}
			catch(Exception ex){}
		});
		
		btnDecreaseSelected.setStyle(StylePatterns.ACTION_BUTTON_CSS);
		btnDecreaseSelected.setOnMousePressed(e -> setBtnPressedStyle(btnDecreaseSelected));
		btnDecreaseSelected.setOnMouseReleased(e -> setBtnReleasedStyle(btnDecreaseSelected));
		btnDecreaseSelected.setOnAction(e->{
			try{
				int quantity = invoiceLines.get(lvInvoiceLines.getSelectionModel().getSelectedIndex()).getQuantity();
				quantity--;
				if(quantity>0){
					invoiceLines.get(lvInvoiceLines.getSelectionModel().getSelectedIndex()).setQuantity(quantity);
				}
				else{
					invoiceLines.remove(lvInvoiceLines.getSelectionModel().getSelectedIndex());
				}
				((CustomSkin<?>)lvInvoiceLines.getSkin()).refresh();
			}
			catch(Exception ex){}
		});
		
		paneLinesActions.getChildren().addAll(btnDeleteLine, btnDecreaseSelected);
		paneLinesActions.setPadding(new Insets(10));
		paneLinesActions.setSpacing(25);
		
		
		
		paneLinesView.setPadding(new Insets(10,10,10,0));
		paneLinesView.getChildren().addAll(lvInvoiceLines, paneLinesActions);
		paneLinesView.setSpacing(20);
		//paneLinesView.setAlignment(Pos.CENTER);
	}

	private void setEditIvoicePanesVisability(boolean isVisible) {
		paneLinesView.setVisible(isVisible);
		paneEditOrCancel.setVisible(isVisible);
	}
	
	private void buildEditOrCancelInvoicePanel() {
		paneEditOrCancel.getChildren().addAll(btnEditInvoice, btnCancel, lblEditMsg);
		paneEditOrCancel.setSpacing(15);
		btnEditInvoice.setStyle(StylePatterns.MAJOR_ACTION_BUTTON_CSS);
		btnCancel.setStyle(StylePatterns.MAJOR_ACTION_BUTTON_CSS);
		lblEditMsg.setStyle(StylePatterns.LABEL_MESSAGE_CSS);

		btnEditInvoice.setOnMousePressed(e -> setMajorBtnPressedStyle(btnEditInvoice));
		btnEditInvoice.setOnMouseReleased(e -> setMajorBtnReleasedStyle(btnEditInvoice));
		btnEditInvoice.setOnAction( e-> onEditInvoicePress());
		
		btnCancel.setOnAction(e->clearAllInvoice());
		btnCancel.setOnMousePressed(e -> setMajorBtnPressedStyle(btnCancel));
		btnCancel.setOnMouseReleased(e -> setMajorBtnReleasedStyle(btnCancel));
		paneEditOrCancel.setPadding(new Insets(0,0,20,0));
		
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
	
	private void onEditInvoicePress() {
		try{
			invoice.clearAllLines();
			for(InvoiceLine line : invoiceLines){
				invoice.addLine(line);
			}
			manager.editInvoice(this.invoice);
			String s = invoice.toString();
			Platform.runLater(() -> {
				stage.setAlwaysOnTop(false);
				Stage dialog = new MessageForm("Invoice Edited\n"+s);
				try {
					dialog.showAndWait();
				} catch (Exception ex) {
				}
			});
			clearAllInvoice();
		}
		catch(Exception ex){
			Platform.runLater(()->{
				lblEditMsg.setText(ex.getMessage());
			});
		}
	}

	public void clearAllInvoice() {
		tfinvoiceID.setEditable(true);
		tfinvoiceID.setText("");
		lblMsg.setText("");
		lblEditMsg.setText("");
		invoice = null;
		invoiceLines = FXCollections.observableArrayList(new ArrayList<InvoiceLine>());
		lvInvoiceLines.setItems(invoiceLines);
		setEditIvoicePanesVisability(false);
	}
}
