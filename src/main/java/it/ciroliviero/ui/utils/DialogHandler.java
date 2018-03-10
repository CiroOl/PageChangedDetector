package it.ciroliviero.ui.utils;

import java.util.Optional;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class DialogHandler {

	private static DialogHandler instance = new DialogHandler();

	public static DialogHandler getInstance(){
		return instance;
	}
	
	private void setWrappedContent(Alert alert,String textToBeWrapped){
		Text text = new Text(textToBeWrapped);
		text.setWrappingWidth(400);
		HBox container = new HBox();
		container.setAlignment(Pos.CENTER_LEFT);
		container.setPadding(new Insets(10));
		container.getChildren().add(text);
		alert.getDialogPane().setContent(container);
	}

	/* *************************************************************************
	 *                                                                         *
	 * WARNING					                                               *
	 *                                                                         *
	 **************************************************************************/
	public void showWarningDialog(String problemDescription, String hint){
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Warning");
		alert.setHeaderText(problemDescription);
		setWrappedContent(alert, hint);
		alert.showAndWait();
	}

	/* *************************************************************************
	 *                                                                         *
	 * INFORMATION				                                               *
	 *                                                                         *
	 **************************************************************************/
	public void showInformationDialog(String info) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("News for you");
		alert.setHeaderText(null);
		setWrappedContent(alert, info);
		alert.showAndWait();

	}

	/* *************************************************************************
	 *                                                                         *
	 * CONFIRMATION				                                               *
	 *                                                                         *
	 **************************************************************************/
	public boolean showConfirmationDialog(String question, String effect){
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirmation");
		alert.setHeaderText(question);
		setWrappedContent(alert, effect);
		Optional<ButtonType> result = alert.showAndWait();
		return result.get() == ButtonType.OK;
	}

	/* *************************************************************************
	 *                                                                         *
	 * NOTIFICATION				                                               *
	 *                                                                         *
	 **************************************************************************/
	public void showNotificationDialog(String pageInfo, String details) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Something changed");
		alert.setHeaderText(pageInfo);
		setWrappedContent(alert, details);
		alert.showAndWait();
	}

}
