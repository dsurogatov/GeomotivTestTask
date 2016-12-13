/**
 * 
 */
package org.dsu.builder;

import java.math.BigDecimal;

import org.dsu.domain.Site;

/** The builder constructs the <code>{@link Site}</code>
 *
 * @author nescafe
 */
public class SiteBuilder implements Builder<Site> {
    
    private Site site = new Site();
    
    /** Sets the <code>Site.id</code> field
     * @param id    The value of the field
     * @return      this
     */
    public SiteBuilder id(int id) {
        site.setId(id);
        return this;
    }
    
    /** Sets the code>Site.name</code> field
     * @param name    The value of the field
     * @return        this
     */
    public SiteBuilder name(String name) {
        site.setName(name);
        return this;
    }
    
    /** Sets the code>Site.mobile</code> field
     * @param name    The value of the field
     * @return        this
     */
    public SiteBuilder mobile(boolean mobile) {
        site.setMobile(mobile);
        return this;
    }

    /** Sets the code>Site.keywords</code> field
     * @param name    The value of the field
     * @return        this
     */
    public SiteBuilder keywords(String keywords) {
        site.setKeywords(keywords);
        return this;
    }
    
    /** Sets the code>Site.score</code> field
     * @param name    The value of the field
     * @return        this
     */
    public SiteBuilder score(String score) {
        site.setScore(new BigDecimal(score));
        return this;
    }
    
    /* (non-Javadoc)
     * @see org.dsu.builder.Builder#build()
     */
    @Override
    public Site build() {
        try {
            return site.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }

}
