/**
 * 
 */
package org.dsu.service.keywords;

import java.util.ArrayList;
import java.util.List;

import org.dsu.domain.Site;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * SimpleFillSiteKeywordService traverses through a list using {@link RandomGenerateKeywordService} to resolve keywords.
 * 
 * @author nescafe
 */
@Service
class SimpleFillSiteKeywordService implements FillSiteKeywordService {

	@Autowired
	private KeywordService randomGenerateKeywordService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dsu.service.keywords.FillSiteKeywordService#fillKeywords(java.util.List)
	 */
	@Override
	public List<Site> fillKeywords(List<Site> sites) {
		if (sites == null) {
			return new ArrayList<Site>();
		}

		sites.forEach(s -> s.setKeywords(randomGenerateKeywordService.resolveKeywords(s.getName())));

		return sites;
	}

}
