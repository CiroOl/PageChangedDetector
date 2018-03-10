package it.ciroliviero.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.jsoup.Jsoup;
import org.junit.Before;
import org.junit.Test;

public class PageTest {
	
	private final static String FAKE_URL = "www.fakeURL.it";
	private Page firstVersion;
	
	@Before
	public void setUp(){
		this.firstVersion = new Page(FAKE_URL,
				Jsoup.parseBodyFragment("<body>Hello there!</body>").body(),null);
	}
	
	@Test
	public void testEquals_samePage() {
		assertEquals(firstVersion, new Page(FAKE_URL,
				Jsoup.parseBodyFragment("<body>Hello there!</body>").body(),null));
	}
	
	@Test
	public void testEquals_differentPage_differentBody() {
		assertNotEquals(firstVersion, new Page(FAKE_URL,
				Jsoup.parseBodyFragment("<body>Hello everybody!</body>").body(),null));
	}
	
	@Test
	public void testEquals_differentPage_differentUrl() {
		assertNotEquals(firstVersion, new Page("www.differentUrl.it",
				Jsoup.parseBodyFragment("<body>Hello there!</body>").body(),null));
	}
	
	@Test
	public void testEquals_differentPage_differentUrlAndBody() {
		assertNotEquals(firstVersion, new Page("www.differentUrl.it",
				Jsoup.parseBodyFragment("<body>Hello everybody!</body>").body(),null));
	}
}
