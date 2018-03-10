package it.ciroliviero.model.notifications;

import it.ciroliviero.model.Page;

public interface NotificationMethod {

	void notifyPageChanged(Page oldVersionPage, Page newVersionPage);

}
