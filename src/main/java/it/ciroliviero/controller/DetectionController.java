package it.ciroliviero.controller;

import it.ciroliviero.StringReader;
import it.ciroliviero.model.PageChangedDetector;
import it.ciroliviero.ui.utils.DialogHandler;
import it.ciroliviero.ui.utils.ViewLoader;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.WindowEvent;

public class DetectionController {

	Thread detectionThread;

	@FXML
	private Label detectionStatusLabel;
	@FXML
	private Label targetSiteLabel;
	@FXML
	private Label researchIntervalLabel;
	@FXML
	private Label numberOfTriesLabel;
	@FXML
	private Button stopDetectionButton;
	
	/* **********************************************************************
	 * 																		*
	 * 	VIEW INITIALIZATION													*
	 * 																		*
	 ************************************************************************/
	@FXML
	void initialize() {
		ViewLoader.getInstance().getStage().setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent windowEvent) {
				if(DialogHandler.getInstance().showConfirmationDialog(
						StringReader.getInstance().getString("ASK_FOR_CANCEL_DETECTION"),
						StringReader.getInstance().getString("CONFIRM_CANCEL_DETECTION"))){
					detectionThread.interrupt();
					Platform.exit();
				}
				else 
					windowEvent.consume();
			}
		});

		targetSiteLabel.setText(PageChangedDetector.getInstace().getTargetURL());
		researchIntervalLabel.setText(PageChangedDetector.getInstace().getResearchInterval()+"");
		numberOfTriesLabel.textProperty().bind(Bindings.convert(PageChangedDetector.getInstace().getNumberOfTries()));
		setUpDetectionThread();
		detectionThread.start();

	}

	private void setUpDetectionThread() {
		detectionThread = new Thread(() -> {
			PageChangedDetector.getInstace().startDetection();
			Platform.runLater(() -> {
				stopDetectionButton.setDisable(true);
				detectionStatusLabel.setText("DETECTION COMPLETED");
				ViewLoader.getInstance().getStage().setOnCloseRequest(null);
			});
		});
	}
	
	/* **********************************************************************
	 * 																		*
	 * 	STOP DETECTION														*
	 * 																		*
	 ************************************************************************/

	@FXML
	void stopDetection(ActionEvent event) {
		if(DialogHandler.getInstance().showConfirmationDialog(
				StringReader.getInstance().getString("ASK_FOR_CANCEL_DETECTION"),
				StringReader.getInstance().getString("CONFIRM_CANCEL_DETECTION")))
			detectionThread.interrupt();
	}

}
