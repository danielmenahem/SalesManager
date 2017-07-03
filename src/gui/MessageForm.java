package gui;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class MessageForm extends Stage {
	
	private String msg = null;
	 

	public MessageForm(String message){
		this.msg = message;
		Label lblMessage = new Label(msg);
		lblMessage.setStyle("-fx-font-weight: bold;-fx-text-fill: blue;");
		lblMessage.setWrapText(true);
		lblMessage.setFont(new Font(11));
		Button btnOk = new Button("OK");
		btnOk.setStyle("-fx-font-weight: bold;");
		btnOk.setPadding(new Insets(10,10,10,10));
		BorderPane paneMain = new BorderPane();
		Scene scene = new Scene(paneMain);
		
		paneMain.setCenter(lblMessage);
		paneMain.setBottom(btnOk);
		BorderPane.setAlignment(lblMessage, Pos.CENTER);
		BorderPane.setAlignment(btnOk, Pos.CENTER);
		
		btnOk.setOnAction(e -> {
			onClose();
		});
		
		this.setOnCloseRequest(e -> {
			onClose();
		});
		
		this.setWidth(400);
		this.setHeight(400);
		this.setTitle("Order");
		this.setScene(scene);
		this.setAlwaysOnTop(true);
	}

	private void onClose(){
		Platform.runLater(() -> {
			this.close();
		});
	}

}