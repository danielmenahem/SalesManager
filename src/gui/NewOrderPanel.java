package gui;
import java.util.ArrayList;

import bl.controlers.AppManager;
import bl.entities.Customer;
import bl.entities.Item;
import bl.entities.Order;
import bl.entities.OrderLine;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class NewOrderPanel extends BorderPane{
	
	private static String MESSAGES [] = new String []{"Customer ID is required"
			,"Customer ID must contain digits only"
			,"Customer does not exist in system yet"
			,"Customer Name: "
			,"Item is required"
			,"Quantity is required"
			,"Quanity must contain digits only"
			,null
			};
	private AppManager manager;
	
	private Customer customer;
	
	private Stage stage;
	
	private VBox paneOrder = new VBox();
	private HBox paneCustomer = new HBox();
	private Label lblCustomerID = new Label("Insert Customer ID: ");
	private TextField tfCustomerID = new TextField();
	private Label lblMsg = new Label();
	private Button btnStartOrder = new Button("Start Order");
	
	private VBox paneAddLine = new VBox();
	private GridPane paneInsertOrderLines = new GridPane();
	private Label lblChooseItem = new Label("Choose Item: ");
	private Label lblQuantity = new Label("Insert Quantity: ");
	private Label lblValidation = new Label();
	private ComboBox <Item> cmbItems;
	private ObservableList<Item> items;
	private TextField tfQuantity = new TextField();
	private Button btnAddLine = new Button("Add to Order");
	private Button btnClear = new Button("Clear Item");
	
	private VBox paneLinesView = new VBox();
	private ListView<OrderLine> lvOrderLines;
	private ObservableList<OrderLine> orderLines;
	private Button btnDeleteLine = new Button("Delete");
	
	private HBox paneSaveOrClear = new HBox();
	private Button btnSaveOrder = new Button("Save Order");
	private Button btnClearOrder = new Button("Clear Order");
	private Label lblOrderMsg = new Label();
	
	
	
	public NewOrderPanel(Stage stage){
		this.stage = stage;
		manager = AppManager.getInstance();
		buildOrderPane();
		buildLineViewPane();
		buildOrderLinesPanel();
		buildSaveOrClearPannel();
		setOrderLinesPanesVisability(false);
		this.setTop(paneOrder);
		this.setLeft(paneAddLine);
		this.setRight(paneLinesView);
		this.setBottom(paneSaveOrClear);
		this.setPadding(new Insets(70,10,10,10));
		this.setStyle(StylePatterns.CONTENT_PANE_CSS);
	}


	private void buildSaveOrClearPannel() {
		paneSaveOrClear.getChildren().addAll(btnSaveOrder, btnClearOrder, lblOrderMsg);
		paneSaveOrClear.setSpacing(15);
		btnSaveOrder.setStyle(StylePatterns.MAJOR_ACTION_BUTTON_CSS);
		btnClearOrder.setStyle(StylePatterns.MAJOR_ACTION_BUTTON_CSS);
		lblOrderMsg.setStyle(StylePatterns.LABEL_MESSAGE_CSS);

		btnSaveOrder.setOnMousePressed(e -> setMajorBtnPressedStyle(btnSaveOrder));
		btnSaveOrder.setOnMouseReleased(e -> setMajorBtnReleasedStyle(btnSaveOrder));
		btnSaveOrder.setOnAction( e-> onSaveOrderPress());
		
		btnClearOrder.setOnAction(e->clearAllOrder());
		btnClearOrder.setOnMousePressed(e -> setMajorBtnPressedStyle(btnClearOrder));
		btnClearOrder.setOnMouseReleased(e -> setMajorBtnReleasedStyle(btnClearOrder));
		paneSaveOrClear.setPadding(new Insets(0,0,20,70));
		
	}


	private void onSaveOrderPress() {
		try{
			Order order = new Order();
			order.setCustomer(customer);
			for(OrderLine line : orderLines){
				order.addOrderLine(line);
			}
			int id = manager.saveOrder(order);
			order.setId(id);
			Platform.runLater(() -> {
				stage.setAlwaysOnTop(false);
				Stage dialog = new MessageForm("Order Saved\n"+order.toString());
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
				lblOrderMsg.setText(ex.getMessage());
				ex.printStackTrace();
			});
		}
	}

	public void clearAllOrder() {
		tfCustomerID.setEditable(true);
		tfCustomerID.setText(null);
		lblMsg.setText("");
		customer = null;
		clearLineDetails();
		
		orderLines = FXCollections.observableArrayList(new ArrayList<OrderLine>());
		lvOrderLines.setItems(orderLines);
		setOrderLinesPanesVisability(false);
	}


	private void buildLineViewPane() {
		orderLines = FXCollections.observableArrayList(new ArrayList<OrderLine>());
		lvOrderLines = new ListView<>(orderLines);
		lvOrderLines.setMinWidth(350);
		lvOrderLines.setStyle(StylePatterns.LV_ITEM_CSS);
		btnDeleteLine.setStyle(StylePatterns.ACTION_BUTTON_CSS);
		btnDeleteLine.setOnMousePressed(e -> setBtnPressedStyle(btnDeleteLine));
		btnDeleteLine.setOnMouseReleased(e -> setBtnReleasedStyle(btnDeleteLine));
		btnDeleteLine.setOnAction(e->{
			try{
				orderLines.remove(lvOrderLines.getSelectionModel().getSelectedIndex());
			}
			catch(Exception ex){}
		});
		paneLinesView.setPadding(new Insets(10));
		paneLinesView.getChildren().addAll(lvOrderLines, btnDeleteLine);
		paneLinesView.setSpacing(15);
		paneLinesView.setAlignment(Pos.CENTER);
	}


	private void buildOrderLinesPanel() {
		items = FXCollections.observableArrayList(manager.getItems());
		cmbItems = new ComboBox<>(items);
		
		paneInsertOrderLines.add(lblChooseItem, 0, 0);
		paneInsertOrderLines.add(cmbItems, 1, 0);
		paneInsertOrderLines.add(lblQuantity, 0, 1);
		paneInsertOrderLines.add(tfQuantity, 1, 1);
		paneInsertOrderLines.add(btnAddLine, 0, 2);
		paneInsertOrderLines.add(btnClear, 1, 2);
		paneInsertOrderLines.setVgap(10);
		paneInsertOrderLines.setHgap(15);
		
		paneAddLine.getChildren().addAll(paneInsertOrderLines,lblValidation);
		paneAddLine.setSpacing(10);
		paneInsertOrderLines.setPadding(new Insets(70,70,20,70));
		
		lblChooseItem.setStyle(StylePatterns.LABEL_CSS);
		lblQuantity.setStyle(StylePatterns.LABEL_CSS);
		tfQuantity.setStyle(StylePatterns.TEXT_FIELD_CSS);
		btnAddLine.setStyle(StylePatterns.ACTION_BUTTON_CSS);
		btnClear.setStyle(StylePatterns.ACTION_BUTTON_CSS);
		lblValidation.setStyle(StylePatterns.LABEL_MESSAGE_CSS);
		lblValidation.setPadding(new Insets(0,0,0,70));
		btnAddLine.setOnMousePressed(e -> setBtnPressedStyle(btnAddLine));
		btnAddLine.setOnMouseReleased(e -> setBtnReleasedStyle(btnAddLine));
		btnAddLine.setOnAction(e->onAddLinePress());
		
		btnClear.setOnMousePressed(e -> setBtnPressedStyle(btnClear));
		btnClear.setOnMouseReleased(e -> setBtnReleasedStyle(btnClear));
		btnClear.setOnAction(e->clearLineDetails());
	
	}
	
	private void onAddLinePress(){
		if(cmbItems.getSelectionModel().getSelectedIndex()==-1)
			showValidationMsg(4);
		else if(tfQuantity.getText().equals(""))
			showValidationMsg(5);
		else{
			try{
				int quantity = Integer.parseInt(tfQuantity.getText());
				OrderLine line = new OrderLine();
				line.setItem(cmbItems.getSelectionModel().getSelectedItem());
				line.setQuantity(quantity);
				orderLines.add(line);
				showValidationMsg(7);
				clearLineDetails();
			}
			catch(Exception ex){
				showValidationMsg(6);
			}
		}
	}


	private void clearLineDetails() {
		cmbItems.getSelectionModel().select(-1);
		tfQuantity.setText("");
		showValidationMsg(7);
		
	}


	private void buildOrderPane() {
		paneCustomer.getChildren().addAll(lblCustomerID,tfCustomerID);
		paneCustomer.setSpacing(15);
		paneOrder.getChildren().addAll(paneCustomer, btnStartOrder, lblMsg);
		paneOrder.setSpacing(10);
		paneOrder.setPadding(new Insets(10,0,0,250));
		lblCustomerID.setStyle(StylePatterns.LABEL_CSS);
		lblMsg.setStyle(StylePatterns.LABEL_MESSAGE_CSS);
		tfCustomerID.setStyle(StylePatterns.TEXT_FIELD_CSS);
		btnStartOrder.setStyle(StylePatterns.ACTION_BUTTON_CSS);
		paneOrder.setStyle(StylePatterns.CONTENT_PANE_CSS);
		
		btnStartOrder.setOnMousePressed(e->setBtnPressedStyle(btnStartOrder));
		btnStartOrder.setOnMouseReleased(e->setBtnReleasedStyle(btnStartOrder));
		btnStartOrder.setOnAction(e->{
			if(tfCustomerID.getText().equals(""))
				showMsg(MESSAGES[0], StylePatterns.LABEL_MESSAGE_CSS);
			else{
				try{					
					int id = Integer.parseInt(tfCustomerID.getText());
					if(manager.isCustomerExist(id)){
						this.customer = manager.getCustomer(id);
						showMsg(MESSAGES[3]+customer.getName(),StylePatterns.LABEL_CONFIRM_MESSAGE_CSS);
						setOrderLinesPanesVisability(true);
						tfCustomerID.setEditable(false);
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
		});
	}
	
	private void showMsg(String msg, String style) {
		Platform.runLater(() -> {				
			lblMsg.setStyle(style);
			lblMsg.setText(msg);
		});
	}
	
	private void showValidationMsg(int msgNum) {
		Platform.runLater(() -> {				
			lblValidation.setText(MESSAGES[msgNum]);
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
	
	private void setOrderLinesPanesVisability(boolean isVisible){
		paneAddLine.setVisible(isVisible);
		paneLinesView.setVisible(isVisible);
		paneSaveOrClear.setVisible(isVisible);
	}
}
