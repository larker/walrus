package org.j4.test;

import org.j4.service.SiteService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/spring.xml"})
public class SiteTest {
	@Autowired
	SiteService siteService;
	@Test
	public void testAddOpinion1() {
		siteService.initSite("127.0.0.1", "cn");
	}
}
