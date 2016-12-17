package org.dsu;

import java.util.ArrayList;
import java.util.List;

import org.dsu.domain.Site;
import org.dsu.service.keywords.FillSiteKeywordService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import junit.framework.TestCase;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestConfig.class })
@ActiveProfiles("test")
public class SimpleFillSiteKeywordServiceTest {

	@Autowired
	private FillSiteKeywordService service;
	
	@Test
	public void givenSiteWithEmptyName_WhenFillKeywords_ThenReturnEmptyKeywords() {
		List<Site> sites = new ArrayList<>();
		Site siteWithNullName = new Site();
		siteWithNullName.setName(null);
		sites.add(siteWithNullName);
		
		Site siteWithName = new Site();
		siteWithName.setName("a");
		sites.add(siteWithName);
		
		sites = service.fillKeywords(sites);
		TestCase.assertEquals(sites.size(), 2);
		TestCase.assertEquals(sites.get(0).getKeywords().isEmpty(), true);
		TestCase.assertEquals(sites.get(1).getKeywords().isEmpty(), false);
	}
	
	@Test
	public void givenNullSitesParam_WhenFillKeywords_ThenReturnEmptyList() {
		List<Site> sites = service.fillKeywords(null);
		TestCase.assertEquals(sites.size(), 0);
	}
}
