package it.ciroliviero;

import it.ciroliviero.ui.utils.ViewLoader;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class PageChangedDetectorApplication extends Application {

	@Override
	public void start(Stage stage) {
		ViewLoader.getInstance().setStage(stage);
		stage.setTitle(StringReader.getInstance().getConfiguration("TITLE"));
		setUpStageSize(stage);
		ViewLoader.getInstance().goToView(StringReader.getInstance().getConfiguration("WELCOME_SCENE"));
		stage.show();
	}

	private void setUpStageSize(Stage stage) {
		Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();
		stage.setWidth(visualBounds.getHeight()/1.5);
		stage.setHeight(visualBounds.getHeight());
		stage.setX(visualBounds.getMaxX()-stage.getWidth());
		stage.setMinWidth(stage.getWidth());
		stage.setMinHeight(stage.getHeight());
	}

	public static void main(String[] args) {
		launch(args);
	}

}
