package gui;

import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;


public class OrderPanel extends BorderPane {
	
	
	private VBox paneSubManu = new VBox();
	private Button btnNewOrder = new Button("New Order");
	private Button btnEditOrder = new Button("Edit Order");
	private Button btnCancelOrder = new Button("Cancel Order");
	private NewOrderPanel paneNewOrder;
	private EditOrderPanel paneEditOrder;
	private CancelOrderPanel paneCancelOrder;
	
	public OrderPanel(Stage stage){
		super();
		paneNewOrder = new NewOrderPanel(stage);
		paneEditOrder = new EditOrderPanel(stage);
		paneCancelOrder = new CancelOrderPanel(stage);
		buildSubManu();
		this.setLeft(paneSubManu);
	}

	private void buildSubManu() {
		paneSubManu.getChildren().addAll(btnNewOrder, btnEditOrder, btnCancelOrder);
		setSubManuButtonsStyle();
		
		setSubManuButtonsActions();
		
		paneSubManu.setPadding(new Insets(120, 30, 10, 30));
		paneSubManu.setSpacing(60);
		paneSubManu.setStyle(StylePatterns.SUB_MANU_PANE_CSS);
	}

	private void setSubManuButtonsActions() {
		btnNewOrder.setOnMousePressed(e->setBtnPressedStyle(btnNewOrder));
		btnNewOrder.setOnMouseReleased(e->setBtnReleasedStyle(btnNewOrder));
		btnNewOrder.setOnAction(e->{
			this.setCenter(paneNewOrder);
			setAlignment(paneNewOrder, Pos.CENTER);

		});
		
		
		btnEditOrder.setOnMousePressed(e->setBtnPressedStyle(btnEditOrder));
		btnEditOrder.setOnMouseReleased(e->setBtnReleasedStyle(btnEditOrder));
		btnEditOrder.setOnAction(e->{
			this.setCenter(paneEditOrder);
			setAlignment(paneEditOrder, Pos.CENTER);
		});
		
		btnCancelOrder.setOnMousePressed(e->setBtnPressedStyle(btnCancelOrder));
		btnCancelOrder.setOnMouseReleased(e->setBtnReleasedStyle(btnCancelOrder));
		btnCancelOrder.setOnAction(e->{
			this.setCenter(paneCancelOrder);
			setAlignment(paneCancelOrder, Pos.CENTER);
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
		btnNewOrder.setStyle(StylePatterns.SUB_MANU_BUTTON_CSS);
		btnEditOrder.setStyle(StylePatterns.SUB_MANU_BUTTON_CSS);
		btnCancelOrder.setStyle(StylePatterns.SUB_MANU_BUTTON_CSS);
	}


}
