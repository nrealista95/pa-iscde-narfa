package pt.iscte.pidesco.search.service;

public interface ClassSearchServices {

	/**
	 * Not implemented
	 * @return number of matches of the found word
	 */
	int numberOfMatches();
	
	/**
	 * Not implemented
	 * @param word -> the string you require to search (it has to be a complete word)
	 * @param options -> set which javaTypes you want to search [variables, methods, fields]
	 */
	void wordToSearch(String word, boolean[] options);
}
