package pt.iscte.pidesco.search;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import pt.iscte.pidesco.javaeditor.service.JavaEditorServices;
import pt.iscte.pidesco.search.service.ClassSearchServices;

public class ClassSearchServicesImpl implements ClassSearchServices{

	@Override
	public int[] getAvailableOptions(String word) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Queue<Integer> wordToSearch(String word, String[] options, File f) {
		BundleContext context = Activator.getContext();
		
		ServiceReference<JavaEditorServices> serviceReference = 
				context.getServiceReference(JavaEditorServices.class);
		JavaEditorServices javaServ = context.getService(serviceReference);
		
		// First step, open the file in the editor
		javaServ.openFile(f);
		
		Queue<Integer> results = new LinkedList<Integer>();
		ArrayList<String> optionsArray = new ArrayList<String>();
		if(options.length == 0)
			return results;
		else {
			for(String option : options) {
				// assuming that the options are correctly written
				optionsArray.add(option);
			}
		
			SearchEngine se = new SearchEngine(word, optionsArray);
			javaServ.parseFile(f, se);
			results = se.getResults();
		}
		return results;
	}

}
