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
	
	public static final SiteBunch POISON = new SiteBunch();

	private String collectionId = "";
	private List<Site> sites = new ArrayList<Site>();
	
	private SiteBunch() {
		
	}

	public SiteBunch(String collectionId, List<Site> sites) {
		this.collectionId = collectionId;
		this.sites = sites;
	}

	public String getCollectionId() {
		return collectionId;
	}

	public List<Site> getSites() {
		return new ArrayList<Site>(sites);
	}

}
