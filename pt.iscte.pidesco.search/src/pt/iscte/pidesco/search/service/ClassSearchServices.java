package pt.iscte.pidesco.search.service;

import java.io.File;
import java.util.Queue;

public interface ClassSearchServices {

	/**
	 * Not implemented
	 * @return the results
	 */
	int[] getAvailableOptions(String word); 
	
	/**
	 * Function that searches a given word in methods and/or variables and/or fields in a given file. 
	 * To select the desired options, write them correctly like the given example.
	 * 
	 * @param word 		-> the string you require to search (it has to be a complete word)
	 * @param options 	-> set which javaTypes you want to search, 
	 * 					give at least one of the options[variables, methods, fields]
	 * @param file		-> the desired file you want to make the search
	 * @return 			returns the offsets of the searched word in the file, 
	 * 					returns an empty array if it does not find matches
	 */
	Queue<Integer> wordToSearch(String word, String[] options, File file);
}
