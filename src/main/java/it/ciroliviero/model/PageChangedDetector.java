package it.ciroliviero.model;

import java.io.IOException;
import java.util.Date;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import it.ciroliviero.StringReader;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class PageChangedDetector {
	
	private static PageChangedDetector instance;
	private PageChangedNotifier notifier;
	private String targetURL;
	private int researchInterval;
	private Page oldVersionPage;
	private IntegerProperty numberOfTries;

	public static PageChangedDetector getInstace(){
		if(instance==null) instance = new PageChangedDetector();
		return instance;
	}

	public PageChangedDetector() {
		this.researchInterval = 1;
		this.targetURL = "";
		this.notifier = new PageChangedNotifier();
		this.numberOfTries = new SimpleIntegerProperty(0);
	}

	public String getTargetURL() {
		return targetURL;
	}

	public int getResearchInterval() {
		return researchInterval;
	}

	public IntegerProperty getNumberOfTries() {
		return numberOfTries;
	}

	/* **********************************************************************
	 * 																		*
	 * 	DETECTION SETTINGS INITIALIZATION									*
	 * 																		*
	 ************************************************************************/

	public void inizializeTargetSite(String targetUrl){
		this.targetURL = targetUrl;
		this.oldVersionPage = findCurrentVersionOfPage();
	}

	public void inizializeResearchInteval(int researchInterval){
		if(researchInterval>=1)
			this.researchInterval = researchInterval;
	}

	private Page findCurrentVersionOfPage() {
		try {
			Document doc = Jsoup.connect("http://"+this.targetURL)
					.userAgent(StringReader.getInstance().getConfiguration("USER_AGENT"))
					.get();
			return new Page(this.targetURL,doc.body(),new Date());
		} catch (IOException e) {
			System.out.println("Page not found...");
			e.printStackTrace();
			return null;
		}

	}

	/* **********************************************************************
	 * 																		*
	 * 	DETECTION SETTINGS RESET											*
	 * 																		*
	 ************************************************************************/
	public void resetDetectionSettings() {
		this.instance = null;
	}

	/* **********************************************************************
	 * 																		*
	 * 	NOTIFICATION METHODS INITIALIZATION									*
	 * 																		*
	 ************************************************************************/

	public boolean initializeMailNotificationMethod(String username, String password){
		return notifier.addMailNotificationMethod(username,password);
	}

	public boolean initializePopUpNotificationMethod() {
		return notifier.addPopUpNotificationMethod();
	}

	/* **********************************************************************
	 * 																		*
	 * 	CHANGES DETECTION OPERATIONS										*
	 * 																		*
	 ************************************************************************/

	public void startDetection(){
		if(oldVersionPage!=null){
			Page newVersionPage;
			do{
				try {
					Platform.runLater(()->numberOfTries.set(numberOfTries.get()+1));
					Thread.sleep(researchInterval*1000);
					System.out.println("Looking for changes...");
					newVersionPage = findCurrentVersionOfPage();
				} catch (InterruptedException e) {
					System.out.println("Thread interrupted:");
					return;
				}
			}
			while(oldVersionPage.equals(newVersionPage));
			System.out.println("Something changed!");
			notifier.notifyChanges(oldVersionPage,newVersionPage);
		}
	}



}
