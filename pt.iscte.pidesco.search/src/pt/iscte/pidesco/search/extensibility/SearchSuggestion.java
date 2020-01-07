package pt.iscte.pidesco.search.extensibility;

import java.io.File;

public interface SearchSuggestion {

	int[] offsetResults(File file, String word);
}
