package pt.iscte.pidesco.byeworld;

import java.io.File;

import pt.iscte.pidesco.search.extensibility.SearchSuggestion;

public class Byeworld implements SearchSuggestion{

	@Override
	public int[] offsetResults(File f, String word) {
		int[] results = {300,400};
		return results;
	}
	
}
