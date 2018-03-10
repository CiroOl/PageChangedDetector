package it.ciroliviero.model;

import java.util.ArrayList;
import java.util.List;

import it.ciroliviero.model.notifications.MailSender;
import it.ciroliviero.model.notifications.NotificationMethod;
import it.ciroliviero.model.notifications.PopUpNotifier;

public class PageChangedNotifier {
	private List<NotificationMethod> notificationMethods;

	public PageChangedNotifier() {
		notificationMethods = new ArrayList<>();
	}

	public void notifyChanges(Page oldVersionPage, Page newVersionPage) {
		for(NotificationMethod m : notificationMethods)
			m.notifyPageChanged(oldVersionPage,newVersionPage);
	}

	public boolean addMailNotificationMethod(String username, String password) {
		MailSender sender = new MailSender(username, password);
		if(sender.checkAuthenticationState(username, password)){
			notificationMethods.add(sender);
			return true;
		}
		return false;
	}

	public boolean addPopUpNotificationMethod() {
		return notificationMethods.add(new PopUpNotifier());
	}

}
