package pt.iscte.pidesco.search.service;

public interface ClassSearchServices {

	/**
	 * Not implemented
	 * @return the results
	 */
	int[] getAvailableOptions(String word); 
	
	/**
	 * Not implemented
	 * @param word -> the string you require to search (it has to be a complete word)
	 * @param options -> set which javaTypes you want to search [variables, methods, fields]
	 * @return returns the results of the search, returns an empty array if it does not find matches
	 */
	String[] wordToSearch(String word, boolean[] options);
}
