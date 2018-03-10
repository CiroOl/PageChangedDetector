package it.ciroliviero.controller;

import java.util.concurrent.ExecutionException;

import it.ciroliviero.StringReader;
import it.ciroliviero.model.PageChangedDetector;
import it.ciroliviero.ui.utils.DialogHandler;
import it.ciroliviero.ui.utils.ViewLoader;
import javafx.beans.binding.Bindings;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class NotificationMethodsSelectionController {
	@FXML
	private Label notificationMethodsLabel;

	@FXML
	private VBox notificationMethodsContainer;

	@FXML
	private CheckBox popUpCheckBox;

	@FXML
	private CheckBox mailCheckBox;
	@FXML
	private Label loginResult;
	@FXML
	private TextField emailTextField;
	@FXML
	private PasswordField passwordTextField;

	@FXML
	private Button startDetectionButton;
	@FXML
	private Button goToPreviousViewButton;

	private void disableInteractionWithView(boolean disableChoice) {
		goToPreviousViewButton.setDisable(disableChoice);
		notificationMethodsContainer.setDisable(disableChoice);
	}

	/* *************************************************************************
	 *                                                                         *
	 * VIEW INIZIALITAZION                                                     *
	 *                                                                         *
	 **************************************************************************/

	@FXML
	private void initialize() {
		startDetectionButton.disableProperty().bind(
				Bindings.when(popUpCheckBox.selectedProperty().or(
						mailCheckBox.selectedProperty()))
				.then(false)
				.otherwise(true));
		notificationMethodsLabel.setText(
				StringReader.getInstance().getString("NOTIFICATION_METHODS_LABEL"));
	}

	/* *************************************************************************
	 *                                                                         *
	 * START DETECTION	                                                       *
	 *                                                                         *
	 **************************************************************************/

	@FXML
	void startDetection(ActionEvent event) {
		disableInteractionWithView(true);
		if(mailCheckBox.isSelected())
			tryToAuthenticate();
		else if(popUpCheckBox.isSelected()){
			PageChangedDetector.getInstace().initializePopUpNotificationMethod();
			goToDetectionView();
		}

	}
	
	/* *************************************************************************
	 *                                                                         *
	 * MAIL AUTHENTICATION	                                                       *
	 *                                                                         *
	 **************************************************************************/

	private void tryToAuthenticate() {

		if(emailTextField.getText().isEmpty() || passwordTextField.getText().isEmpty()){
			DialogHandler.getInstance().showWarningDialog(
					StringReader.getInstance().getString("UNABLE_TO_AUTHENTICATE"), 
					StringReader.getInstance().getString("PLEASE_INSERT_CREDENTIALS"));
			disableInteractionWithView(false);
		}else{
			updateLoginResult("Authentication in progress...",Color.BLACK);
			new Thread(setUpLoginTask()).start();
		}
	}

	private Task<Boolean> setUpLoginTask(){
		Task<Boolean> tryToLoginTask = new Task<Boolean>() {
			@Override
			protected Boolean call() throws Exception {
				return PageChangedDetector.getInstace().
						initializeMailNotificationMethod(emailTextField.getText(), passwordTextField.getText());
			}
		};

		tryToLoginTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent event) {
				try {
					if(tryToLoginTask.get()){
						updateLoginResult("Authentication succeded!",Color.GREEN);
						if(popUpCheckBox.isSelected())
							PageChangedDetector.getInstace().initializePopUpNotificationMethod();
						goToDetectionView();
					}
					else{
						updateLoginResult("Authentication failed!", Color.RED);
						mailCheckBox.setSelected(false);
						disableInteractionWithView(false);
					}
				} catch (InterruptedException | ExecutionException e) {
					disableInteractionWithView(false);
				}
			}
		});
		return tryToLoginTask;
	}

	/* *************************************************************************
	 *                                                                         *
	 * CHANGE VIEW		                                                       *
	 *                                                                         *
	 **************************************************************************/

	private void goToDetectionView() {
		disableInteractionWithView(false);
		ViewLoader.getInstance().goToView("detection");
	}


	@FXML
	void goToPreviousView(ActionEvent event) {
		ViewLoader.getInstance().goToView("siteAndIntervalSelection");
	}

	/* *************************************************************************
	 *                                                                         *
	 * LOGIN RESULT		                                                       *
	 *                                                                         *
	 **************************************************************************/

	@FXML
	void loginCredentialsChanged(KeyEvent event) {
		updateLoginResult("", Color.BLACK);
	}

	private void updateLoginResult(String text, Color color) {
		loginResult.setText(text);
		loginResult.setTextFill(color);
	}
}
