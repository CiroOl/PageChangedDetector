package it.ciroliviero.ui.utils;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ViewLoader {
	
	private static ViewLoader instance = new ViewLoader();
	private Stage stage;
	
	public static ViewLoader getInstance(){
		return instance;
	}
	
	/* **************************************************************************
	 *                                                                         *
	 * SCENE	                                                     		   *
	 *                                                                         *
	 **************************************************************************/
	
	public void goToView(String viewName){
		this.stage.setScene(new Scene(loadViewFromName(viewName)));
	}
	
	private Parent loadViewFromName(String viewName){
		try {
			return FXMLLoader.load(getClass().getResource("/view/"+viewName+".fxml"));
		} catch (IOException e) {
			System.out.println("File fxml not found...");
			e.printStackTrace();
			return null;
		}

	}
	
	/* **************************************************************************
	 *                                                                         *
	 * STAGE	                                                     		   *
	 *                                                                         *
	 **************************************************************************/

	public void setStage(Stage stage) {
		this.stage = stage;
	}
	
	public Stage getStage() {
		return this.stage;
	}
}
