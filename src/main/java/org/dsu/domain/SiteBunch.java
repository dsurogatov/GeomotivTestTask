/**
 * 
 */
package org.dsu.domain;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.StringUtils;

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
		// TODO add tests
		if(StringUtils.isEmpty(collectionId)) {
			throw new IllegalArgumentException("The param 'collectionId' must not be empty.");
		}
		if(sites == null) {
			throw new IllegalArgumentException("The param 'sites' must not be null.");
		}
		
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
