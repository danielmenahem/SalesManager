package gui;

import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

import java.util.ArrayList;

import bl.controlers.AppManager;
import bl.entities.Item;
import bl.entities.Order;
import bl.entities.OrderLine;


public class EditOrderPanel  extends BorderPane{
	private static String MESSAGES [] = new String []{
			"Order ID is required"
				,"Order ID must contain digits only"
				,"Order ID does not exist in system"
				,"Selected Order already closed"
				,"Order edit available"
				,"Item is required"
				,"Quantity is required"
				,"Quanity must contain digits only"
				,""
		};		
	private AppManager manager;
	private Order order;
	private Stage stage;
	
	private VBox paneOrder = new VBox();
	private HBox paneOrderID = new HBox();
	private Label lblOrderID = new Label("Insert Order ID: ");
	private TextField tfOrderID = new TextField();
	private Label lblMsg = new Label();
	private Button btnStartEditOrder = new Button("Edit Order");
	
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
	private Button btnDeleteLine = new Button("Delete Selected");
	
	private HBox paneEditOrCancel = new HBox();
	private Button btnSaveEditedOrder = new Button("Edit Order");
	private Button btnCancelEdit = new Button("Cancel");
	private Label lblOrderMsg = new Label();
	
	public EditOrderPanel(Stage stage){
		this.stage = stage;
		manager = AppManager.getInstance();
		buildOrderPane();
		buildLineViewPane();
		buildOrderLinesPanel();
		buildSaveOrClearPannel();
		setEditOrderLinesPanesVisability(false);
		this.setTop(paneOrder);
		this.setLeft(paneLinesView);
		this.setRight(paneAddLine);
		this.setBottom(paneEditOrCancel);
		this.setPadding(new Insets(70,10,10,10));
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
		btnStartEditOrder.setOnAction(e -> onStartEditPress());
	}

	private void onStartEditPress() {
		if(tfOrderID.getText().equals(""))
			showMsg(MESSAGES[0], StylePatterns.LABEL_MESSAGE_CSS);
		else{
			try{					
				int id = Integer.parseInt(tfOrderID.getText());
				if(manager.isOrderExist(id)){
					if(manager.isOrderOpen(id)){
						this.order = manager.getOrder(id);
						showMsg(MESSAGES[4],StylePatterns.LABEL_CONFIRM_MESSAGE_CSS);
						setEditOrderLinesPanesVisability(true);
						pupulteListView();
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
	
	private void pupulteListView() {
		for(OrderLine line : order.getLines())
			orderLines.add(line);
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
		lblValidation.setPadding(new Insets(0,0,0,70));
		
		lblChooseItem.setStyle(StylePatterns.LABEL_CSS);
		lblQuantity.setStyle(StylePatterns.LABEL_CSS);
		lblValidation.setStyle(StylePatterns.LABEL_MESSAGE_CSS);
		tfQuantity.setStyle(StylePatterns.TEXT_FIELD_CSS);
		btnAddLine.setStyle(StylePatterns.ACTION_BUTTON_CSS);
		btnClear.setStyle(StylePatterns.ACTION_BUTTON_CSS);
		
		btnAddLine.setOnMousePressed(e -> setBtnPressedStyle(btnAddLine));
		btnAddLine.setOnMouseReleased(e -> setBtnReleasedStyle(btnAddLine));
		btnAddLine.setOnAction(e->onAddLinePress());
		
		btnClear.setOnMousePressed(e -> setBtnPressedStyle(btnClear));
		btnClear.setOnMouseReleased(e -> setBtnReleasedStyle(btnClear));
		btnClear.setOnAction(e->clearLineDetails());
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
		paneLinesView.setPadding(new Insets(70,70,20,70));
		paneLinesView.getChildren().addAll(lvOrderLines, btnDeleteLine);
		paneLinesView.setSpacing(15);
		paneLinesView.setAlignment(Pos.CENTER);
	}
	
	private void buildSaveOrClearPannel() {
		paneEditOrCancel.getChildren().addAll(btnSaveEditedOrder, btnCancelEdit, lblOrderMsg);
		paneEditOrCancel.setSpacing(15);
		btnSaveEditedOrder.setStyle(StylePatterns.MAJOR_ACTION_BUTTON_CSS);
		btnCancelEdit.setStyle(StylePatterns.MAJOR_ACTION_BUTTON_CSS);
		lblOrderMsg.setStyle(StylePatterns.LABEL_MESSAGE_CSS);

		btnSaveEditedOrder.setOnMousePressed(e -> setMajorBtnPressedStyle(btnSaveEditedOrder));
		btnSaveEditedOrder.setOnMouseReleased(e -> setMajorBtnReleasedStyle(btnSaveEditedOrder));
		btnSaveEditedOrder.setOnAction( e-> onSaveOrderPress());
		
		btnCancelEdit.setOnAction(e->clearAllOrder());
		btnCancelEdit.setOnMousePressed(e -> setMajorBtnPressedStyle(btnCancelEdit));
		btnCancelEdit.setOnMouseReleased(e -> setMajorBtnReleasedStyle(btnCancelEdit));
		paneEditOrCancel.setPadding(new Insets(0,0,20,370));
		
	}
	
	private void onSaveOrderPress() {
		try{
			order.removeAllLines();
			for(OrderLine line : orderLines){
				order.addOrderLine(line);
			}
			System.out.println(order);
			manager.editOrder(order);
			String details = order.toString();
			Platform.runLater(() -> {
				stage.setAlwaysOnTop(false);
				Stage dialog = new MessageForm("Order Edited\n"+details);
				//Stage dialog = new MessageForm("avsddad sdsad\nsadsd dsda");
				try {
					dialog.showAndWait();
				} catch (Exception ex) {
				}
			});
		}

		catch(Exception ex){
			Platform.runLater(()->{
				lblOrderMsg.setText(ex.getMessage());
			});
		}
		clearAllOrder();
	}

	public void clearAllOrder() {
		tfOrderID.setEditable(true);
		tfOrderID.setText("");
		lblMsg.setText("");
		lblOrderMsg.setText("");
		order = null;
		clearLineDetails();
		
		orderLines = FXCollections.observableArrayList(new ArrayList<OrderLine>());
		lvOrderLines.setItems(orderLines);
		setEditOrderLinesPanesVisability(false);
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
	
	private void onAddLinePress(){
		if(cmbItems.getSelectionModel().getSelectedIndex()==-1)
			showValidationMsg(5);
		else if(tfQuantity.getText().equals(""))
			showValidationMsg(6);
		else{
			try{
				int quantity = Integer.parseInt(tfQuantity.getText());
				OrderLine line = new OrderLine();
				line.setItem(cmbItems.getSelectionModel().getSelectedItem());
				line.setQuantity(quantity);
				orderLines.add(line);
				showValidationMsg(8);
			}
			catch(Exception ex){
				showValidationMsg(7);
			}
		}
	}


	private void clearLineDetails() {
		cmbItems.getSelectionModel().select(-1);
		tfQuantity.setText("");
		showValidationMsg(8);
		
	}
	
	private void showValidationMsg(int msgNum) {
		Platform.runLater(() -> {				
			lblValidation.setText(MESSAGES[msgNum]);
		});
	}
	
	private void setEditOrderLinesPanesVisability(boolean isVisible) {
		paneAddLine.setVisible(isVisible);
		paneLinesView.setVisible(isVisible);
		paneEditOrCancel.setVisible(isVisible);		
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
	
	private void showMsg(String msg, String style) {
		Platform.runLater(() -> {				
			lblMsg.setStyle(style);
			lblMsg.setText(msg);
		});
	}
	
	

}
