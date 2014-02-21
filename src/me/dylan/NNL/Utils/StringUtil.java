package me.dylan.NNL.Utils;

import java.util.ArrayList;

public class StringUtil {
	public static int calculateArraySimilarityCount(String[] a, String[] b) {
		int intersect = 0;
		int totalLength = a.length+b.length;
		for (String s : a) {
			for(String s2 : b) {
				if(s.equals(s2)) {
					intersect++;
					break;
				}
			}
		}
		return (intersect*2)/totalLength;
	}

	/*
	 * The following methods are related to the StrikeAMatch method.
	 * http://www.catalysoft.com/articles/StrikeAMatch.html
	 */
	private static String[] extractLetterPairs(String str) {
		int numPairs = str.length() - 1;
		String[] pairs = new String[numPairs];
		for (int i = 0; i < numPairs; i++) {
			pairs[i] = str.substring(i, i + 2);
		}
		return pairs;
	}

	public static ArrayList<String> getLetterPairsFromWords(String str) {
		ArrayList<String> pairs = new ArrayList<String>();
		for (String s : str.split("\\s")) {
			for (String pair : extractLetterPairs(s)) {
				pairs.add(pair);
			}
		}
		return pairs;
	}

	public static int calculateStringDifferenceCount(String a, String b) {
		ArrayList<String> aArr = getLetterPairsFromWords(a.toUpperCase());
		ArrayList<String> bArr = getLetterPairsFromWords(b.toUpperCase());
		return calculateArraySimilarityCount((String[])aArr.toArray(), (String[])bArr.toArray());
	}
}