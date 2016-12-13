package org.dsu.domain;

import java.math.BigDecimal;

/**
 * Site represents data of a row in an input file
 * 
 * @author nescafe
 */
public class Site implements Cloneable {

	private int id;
	private String name = "";
	private boolean mobile;
	private String keywords = "";
	private BigDecimal score;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isMobile() {
		return mobile;
	}

	public void setMobile(boolean mobile) {
		this.mobile = mobile;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public BigDecimal getScore() {
		return score;
	}

	public void setScore(BigDecimal score) {
		this.score = score;
	}

	@Override
	public String toString() {
		return "Site [id=" + id + ", name=" + name + ", mobile=" + mobile + ", keywords=" + keywords + ", score="
		        + score + "]";
	}
	
	public Site clone() throws CloneNotSupportedException {
	    return (Site) super.clone();
	}
}
