package gui;

import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;

public class InvoicePanel extends BorderPane{
		
	private VBox paneSubManu = new VBox();
	private Button btnNewInvoice = new Button("New Invoice");
	private Button btnEditInvoice = new Button("Edit Invioce");
	private Button btnCancelInvoice = new Button("Remove\nInvoice");
	
	private NewInvoicePanel paneNewInvoice;
	private EditInvoicePanel paneEditInvoice;
	private CancelInvoicePanel paneCancelInvoice;
	
	
	public InvoicePanel(Stage stage){
		super();
		paneNewInvoice = new NewInvoicePanel(stage);
		paneEditInvoice = new EditInvoicePanel(stage);
		paneCancelInvoice = new CancelInvoicePanel(stage);
		buildSubManu();
		this.setLeft(paneSubManu);
	}
	
	private void buildSubManu() {
		paneSubManu.getChildren().addAll(btnNewInvoice, btnEditInvoice, btnCancelInvoice);
		setSubManuButtonsStyle();
		
		setSubManuButtonsActions();
		
		paneSubManu.setPadding(new Insets(120, 30, 10, 30));
		paneSubManu.setSpacing(60);
		paneSubManu.setStyle(StylePatterns.SUB_MANU_PANE_CSS);
	}

	private void setSubManuButtonsActions() {
		btnNewInvoice.setOnMousePressed(e->setBtnPressedStyle(btnNewInvoice));
		btnNewInvoice.setOnMouseReleased(e->setBtnReleasedStyle(btnNewInvoice));
		btnNewInvoice.setOnAction(e->{
			this.setCenter(paneNewInvoice);
			setAlignment(paneNewInvoice, Pos.CENTER);
		});
		
		
		btnEditInvoice.setOnMousePressed(e->setBtnPressedStyle(btnEditInvoice));
		btnEditInvoice.setOnMouseReleased(e->setBtnReleasedStyle(btnEditInvoice));
		btnEditInvoice.setOnAction(e->{
			this.setCenter(paneEditInvoice);
			setAlignment(paneEditInvoice, Pos.CENTER);
		});
		
		btnCancelInvoice.setOnMousePressed(e->setBtnPressedStyle(btnCancelInvoice));
		btnCancelInvoice.setOnMouseReleased(e->setBtnReleasedStyle(btnCancelInvoice));
		btnCancelInvoice.setOnAction(e->{
			this.setCenter(paneCancelInvoice);
			setAlignment(paneCancelInvoice, Pos.CENTER);
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
		btnNewInvoice.setStyle(StylePatterns.SUB_MANU_BUTTON_CSS);
		btnEditInvoice.setStyle(StylePatterns.SUB_MANU_BUTTON_CSS);
		btnCancelInvoice.setStyle(StylePatterns.SUB_MANU_BUTTON_CSS);
	}
	
}
