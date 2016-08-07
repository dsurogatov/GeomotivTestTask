/**
 * 
 */
package org.dsu.domain;

import java.util.ArrayList;
import java.util.List;

/** SiteBunch represents a list of instancies of the Site class
 * 
 * @author nescafe
 */
public class SiteBunch {

	private String collectionId = "";
	private List<Site> sites = new ArrayList<Site>();
	
	public SiteBunch() {
		
	}

	public SiteBunch(String collectionId, List<Site> sites) {
		this.collectionId = collectionId;
		this.sites = sites;
	}

	public String getCollectionId() {
		return collectionId;
	}

	public void setCollectionId(String collectionId) {
		this.collectionId = collectionId;
	}

	public List<Site> getSites() {
		return sites;
	}

	public void setSites(List<Site> sites) {
		this.sites = sites;
	}
}
