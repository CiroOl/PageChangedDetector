package it.ciroliviero.controller;

import it.ciroliviero.StringReader;
import it.ciroliviero.ui.utils.ViewLoader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class WelcomeController {

	@FXML
	private Label welcomeLabel;

	/* **************************************************************************
	 *                                                                         *
	 * VIEW INIZIALITAZION                                                     *
	 *                                                                         *
	 **************************************************************************/
	@FXML
	private void initialize() {
		welcomeLabel.setText(StringReader.getInstance().getString("WELCOME_LABEL"));
	}
	
	/* **************************************************************************
	 *                                                                         *
	 * CHANGE VIEW		                                                       *
	 *                                                                         *
	 **************************************************************************/

	@FXML
	void goToNextView(ActionEvent event) {
		ViewLoader.getInstance().goToView("siteAndIntervalSelection");	
	}

}

