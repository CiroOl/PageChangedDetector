package it.ciroliviero.model;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.jsoup.nodes.Element;

public class Page {
	
	private Date dateOfFinding;
	private Element body;
	private String url;
	
	public Page(String url, Element body, Date dateOfFinding) {
		this.dateOfFinding = dateOfFinding;
		this.url = url;
		this.body = body;
	}
	
	public Element getBody() {
		return body;
	}
	
	public String getUrl() {
		return url;
	}
	
	public String getDateOfFinding() {
		return new SimpleDateFormat("HH:mm dd-MM-yyyy").format(dateOfFinding);
	}
	
	@Override
	public int hashCode() {
		return url.hashCode()+body.toString().hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		Page that = (Page) obj;
		return this.getBody().toString().equals(that.getBody().toString())
				&& this.getUrl().equals(that.getUrl());
	}
	
	
}
