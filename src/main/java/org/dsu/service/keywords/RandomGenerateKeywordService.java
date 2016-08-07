/**"
 * 
 */
package org.dsu.service.keywords;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.StringJoiner;

import org.springframework.stereotype.Service;

/**
 * RandomGenerateKeywordService generates random keywords for a site object
 * 
 * @author nescafe
 *
 */
@Service
class RandomGenerateKeywordService implements KeywordService {

	private static final String[] KEYWORDS = { "key1", "key2", "key3", "key4", "key5" };
	private static final Random RANDOM = new Random();

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dsu.service.keywords.KeywordService#resolveKeywords(java.lang.Object)
	 */
	@Override
	public String resolveKeywords(Object site) {
		if (site == null) {
			return "";
		}

		Set<String> keywords = new HashSet<>();
		StringJoiner join = new StringJoiner(",");
		for (int i = 0; i < KEYWORDS.length; i++) {
			int index = RANDOM.nextInt(KEYWORDS.length);
			if (keywords.add(KEYWORDS[index])) {
				join.add(KEYWORDS[index]);
			}
		}
		return join.toString();
	}

}
