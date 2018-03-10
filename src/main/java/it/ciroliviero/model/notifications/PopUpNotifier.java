package it.ciroliviero.model.notifications;

import it.ciroliviero.model.Page;
import it.ciroliviero.ui.utils.DialogHandler;
import javafx.application.Platform;

public class PopUpNotifier implements NotificationMethod{

	public PopUpNotifier() {}

	@Override
	public void notifyPageChanged(Page oldVersionPage, Page newVersionPage) {
		Platform.runLater(()-> {
			DialogHandler.getInstance().showNotificationDialog(
					"Found changes in "+oldVersionPage.getUrl(),
					"At "+ newVersionPage.getDateOfFinding() +" something changed."
					+ "\nCheck it out :)");
			});
	}
}

