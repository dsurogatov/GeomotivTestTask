package org.dsu;

import java.util.ArrayList;

import org.dsu.domain.SiteBunch;
import org.junit.Test;

public class SiteBunchTest {

	@Test(expected = IllegalArgumentException.class)
	public void WhenPassNullCollectionId_ThenThrowsException() {
		new SiteBunch(null, new ArrayList<>());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void WhenPassEmptyCollectionId_ThenThrowsException() {
		new SiteBunch("", new ArrayList<>());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void WhenPassNullSiteList_ThenThrowsException() {
		new SiteBunch("gg", null);
	}
}
