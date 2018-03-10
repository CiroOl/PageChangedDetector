package it.ciroliviero.ui.utils;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class ShowTargetSitePreview implements Runnable {
	
	private String urlAsString;
	
	public ShowTargetSitePreview(String urlAsString) {
		this.urlAsString = urlAsString;
	}

	@Override
	public void run() {
		try {
			Desktop.getDesktop().browse(new URI(urlAsString));
		} catch (IOException | URISyntaxException e) {
			System.out.println("Something went wrong trying to show target site preview: "+urlAsString);
			e.printStackTrace();
		}

	}

}
