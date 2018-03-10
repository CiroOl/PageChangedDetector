package it.ciroliviero.controller;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import org.jsoup.HttpStatusException;

import it.ciroliviero.StringReader;
import it.ciroliviero.model.PageChangedDetector;
import it.ciroliviero.ui.utils.DialogHandler;
import it.ciroliviero.ui.utils.IllegalResearchIntervalException;
import it.ciroliviero.ui.utils.ShowTargetSitePreview;
import it.ciroliviero.ui.utils.ViewLoader;
import javafx.beans.binding.Bindings;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;

public class SiteAndIntervalSelectionController {

	private boolean targetSiteUrlChecked;
	@FXML
	private Button checkButton;
	@FXML
	private Label checkingLabel;
	@FXML
	private Label targetSiteLabel;
	@FXML
	private TextField targetSiteUrlTextField;

	@FXML
	private TextField researchIntervalTextField;
	@FXML
	private Label researchIntervalLabel;
	@FXML
	private Button researchIntervalDownButton;

	@FXML
	private VBox selectionContainer;
	
	@FXML
	private Button goToPreviousViewButton;
	@FXML
	private Button goToNextViewButton;

	/* *************************************************************************
	 *                                                                         *
	 * VIEW INIZIALITAZION                                                     *
	 *                                                                         *
	 **************************************************************************/
	@FXML
	private void initialize() {
		targetSiteUrlChecked = false;
		checkingLabel.setVisible(false);
		targetSiteUrlTextField.setText(PageChangedDetector.getInstace().getTargetURL());
		researchIntervalTextField.setText(PageChangedDetector.getInstace().getResearchInterval()+"");
		initializeButtons();
		targetSiteLabel.setText(StringReader.getInstance().getString("TARGET_SITE_LABEL"));
		researchIntervalLabel.setText(StringReader.getInstance().getString("RESEARCH_INTERVAL_LABEL"));
	}

	private void initializeButtons() {
		checkButton.disableProperty().bind(
				Bindings.isEmpty(targetSiteUrlTextField.textProperty()));
		goToNextViewButton.disableProperty().bind(
				Bindings.isEmpty(targetSiteUrlTextField.textProperty()).or(
						Bindings.isEmpty(researchIntervalTextField.textProperty())));		
		researchIntervalDownButton.disableProperty().bind(
				Bindings.isEmpty(researchIntervalTextField.textProperty()).or(
						Bindings.equal("1", researchIntervalTextField.textProperty())));
	}

	/* *************************************************************************
	 *                                                                         *
	 * TARGET SITE				                                               *
	 *                                                                         *
	 **************************************************************************/
	@FXML
	void checkTargetSiteUrl(ActionEvent event) {

		Task<Boolean> checkingSiteUrlTask = new Task<Boolean>() {
			@Override
			protected Boolean call() throws Exception {
				selectionContainer.setDisable(true);
				goToPreviousViewButton.setDisable(true);
				targetSiteUrlTextField.setText(targetSiteUrlTextField.getText().replaceAll("https?://",""));
				return tryConnectionToTargetSite("http://"+targetSiteUrlTextField.getText());
			}
		};

		checkingSiteUrlTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent event) {
				try {
					targetSiteUrlChecked = checkingSiteUrlTask.get();
					if(targetSiteUrlChecked)
						DialogHandler.getInstance().showInformationDialog(
								StringReader.getInstance().getString("TARGET_SITE_FOUND"));
					else
						DialogHandler.getInstance().showWarningDialog(
								StringReader.getInstance().getString("TARGET_SITE_UNREACHABLE"), 
								StringReader.getInstance().getString("CHECK_CONNECTIVITY_AND_URL"));
				} catch (InterruptedException | ExecutionException e) {
					System.out.println("Checking task interrupted...");
					e.printStackTrace();
				}
				finally {
					selectionContainer.setDisable(false);
					goToPreviousViewButton.setDisable(false);
				}
			}
		});
		
		checkingLabel.visibleProperty().bind(checkingSiteUrlTask.runningProperty());
		new Thread(checkingSiteUrlTask).start();
	}

	private boolean tryConnectionToTargetSite(String urlAsString){
		try 
		{
			System.out.println("Trying to find target site: "+urlAsString);
			URL siteTargetUrl = new URL(urlAsString);
			HttpURLConnection.setFollowRedirects(true);
			HttpURLConnection connection = (HttpURLConnection) siteTargetUrl.openConnection();
			
			connection.setRequestProperty("User-Agent",StringReader.getInstance().getConfiguration("USER_AGENT"));
			connection.setConnectTimeout(Integer.parseInt(StringReader.getInstance().getConfiguration("CONNECTION_TIMEOUT")));
			
			switch (connection.getResponseCode()) {
			case HttpURLConnection.HTTP_OK:{
				System.out.println("Target site found: "+siteTargetUrl.toString());
				targetSiteUrlTextField.setText(siteTargetUrl.toString().replaceAll("https?://",""));
				new Thread(new ShowTargetSitePreview(urlAsString)).start();
				return true;
			}
			case HttpURLConnection.HTTP_MOVED_TEMP:
			case HttpURLConnection.HTTP_MOVED_PERM: 
			case HttpURLConnection.HTTP_SEE_OTHER:{
				// Redirection
				System.out.println("Redirection...");
				return tryConnectionToTargetSite(connection.getHeaderField("Location"));
			}
			default:
				throw new HttpStatusException("HTTP status exception",
						connection.getResponseCode(), siteTargetUrl.toString());
			}
		}catch (Exception e){
			System.out.println("Something went wrong trying to find target site...");
			return false;
		} 

	}

	/* *************************************************************************
	 *                                                                         *
	 * RESEARCH INTERVAL		                                               *
	 *                                                                         *
	 **************************************************************************/

	@FXML
	void researchIntervalChanged(KeyEvent event) {
		try {
			if(!researchIntervalTextField.getText().isEmpty() &&
					Integer.parseInt(researchIntervalTextField.getText())<1)
				throw new IllegalResearchIntervalException("Research interval: illegal format!");
		}
		catch(NumberFormatException | IllegalResearchIntervalException e){
			DialogHandler.getInstance().showWarningDialog(
					StringReader.getInstance().getString("ILLEGAL_RESEARCH_INTERVAL"),
					StringReader.getInstance().getString("PLEASE_INSERT_VALID_VALUE"));
			researchIntervalTextField.setText("1");
		}
	}

	@FXML
	void researchIntervalDown(ActionEvent event) {
		addToCurrentResearchInterval(-1);
	}

	@FXML
	void researchIntervalUp(ActionEvent event) {
		addToCurrentResearchInterval(+1);
	}

	private void addToCurrentResearchInterval(int delta){
		researchIntervalTextField.setText(""+(Integer.parseInt(researchIntervalTextField.getText())+delta));

	}

	/* *************************************************************************
	 *                                                                         *
	 * CHANGE VIEW		                                                       *
	 *                                                                         *
	 **************************************************************************/

	@FXML
	void goToNextView(ActionEvent event) {
		if(!targetSiteUrlChecked)
			DialogHandler.getInstance().showWarningDialog(
					StringReader.getInstance().getString("SITE_NOT_CHECKED"),
					StringReader.getInstance().getString("PLEASE_CHECK"));
		else{
			PageChangedDetector detector = PageChangedDetector.getInstace();
			detector.inizializeTargetSite(targetSiteUrlTextField.getText());
			detector.inizializeResearchInteval(Integer.parseInt(researchIntervalTextField.getText()));
			ViewLoader.getInstance().goToView("notificationMethodsSelection");
		}
	}

	@FXML
	void goToPreviousView(ActionEvent event) {
		if(DialogHandler.getInstance().showConfirmationDialog(
				StringReader.getInstance().getString("ASK_FOR_CANCEL_DETECTION"),
				StringReader.getInstance().getString("CONFIRM_CANCEL_DETECTION"))){
			PageChangedDetector.getInstace().resetDetectionSettings();
			ViewLoader.getInstance().goToView("welcome");
		}

	}

}