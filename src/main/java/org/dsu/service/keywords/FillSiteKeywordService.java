/**
 * 
 */
package org.dsu.service.keywords;

import java.util.List;

import org.dsu.domain.Site;

/**
 * The interface for populating the field keyword to a site object.
 * 
 * @author nescafe
 */
public interface FillSiteKeywordService {

	/**
	 * Fill the field keyword in site instances
	 * 
	 * @param sites    A list of sites objects
	 * 
	 * @return    A list of sites objects with filled keywords. 
	 * 			  If the param is empty then return an empty list.
	 */
	List<Site> fillKeywords(List<Site> sites);
}
