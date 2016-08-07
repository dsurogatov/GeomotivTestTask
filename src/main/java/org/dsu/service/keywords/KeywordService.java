package org.dsu.service.keywords;

/**
 * <p>A service to resolve keywords from a site object.</p>
 *
 */
public interface KeywordService {

    /**
     * Resolves a list of keywords associated with a site.
     *
     * @param site
     * @return a comma delimited string or an empty string if there are no keywords associated with the site.
     */
    String resolveKeywords(Object site);
    
}