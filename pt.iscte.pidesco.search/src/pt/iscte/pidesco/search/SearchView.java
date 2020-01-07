package pt.iscte.pidesco.search;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
//import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import pt.iscte.pidesco.search.Activator;
import pt.iscte.pidesco.search.extensibility.SearchSuggestion;
import pt.iscte.pidesco.extensibility.PidescoView;
import pt.iscte.pidesco.javaeditor.service.JavaEditorServices;

public class SearchView implements PidescoView {

	private Queue<Integer> results = new LinkedList<Integer>();
	private HashMap<String, int[]> resultsFromExtensions = new HashMap<String, int[]>();
	private ArrayList<Button> checkBtnsFromExtensions = new ArrayList<Button>();
	IConfigurationElement[] elements;
	private String wordInput;
	private File openedFile;

	@Override
	public void createContents(Composite viewArea, Map<String, Image> imageMap) {
		
		BundleContext context = Activator.getContext();

		GridLayout layout = new GridLayout(3, true);
		viewArea.setLayout(layout);
		Text text = new Text(viewArea, SWT.FILL);
		text.setSize(100, SWT.FILL);
		text.setText("find");
		Button findBtn = new Button(viewArea,SWT.PUSH);
		findBtn.setText("find");
		//		findBtn.setEnabled(false);
		final Button nextBtn = new Button(viewArea, SWT.ARROW | SWT.RIGHT);
		nextBtn.setBounds(100, 55, 20, 15);
		//	    final Button previousBtn = new Button(viewArea, SWT.ARROW | SWT.LEFT);
		//	    previousBtn.setBounds(100, 70, 20, 15);
		Button checkMethodsBtn = new Button(viewArea, SWT.CHECK);
		Button checkVariablesBtn = new Button(viewArea, SWT.CHECK);
		Button checkFieldsBtn = new Button(viewArea, SWT.CHECK);
		checkMethodsBtn.setText("methods");
		checkVariablesBtn.setText("variables");
		checkFieldsBtn.setText("fields");

		ServiceReference<JavaEditorServices> serviceReference = 
				context.getServiceReference(JavaEditorServices.class);
		JavaEditorServices javaServ = context.getService(serviceReference);

		findBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e){
				wordInput = text.getText();
				openedFile = javaServ.getOpenedFile();
				ArrayList<String> selectedCheckBoxes = new ArrayList<String>();

				results.clear();
				
				if(!checkMethodsBtn.getSelection() && !checkVariablesBtn.getSelection() && !checkFieldsBtn.getSelection() && !isExtChecked())
					System.out.println("nothing was selected");
				else {
					// Hard coded way to check if the standard boxes are selected 
					if(checkMethodsBtn.getSelection())
						selectedCheckBoxes.add("methods");
					if(checkVariablesBtn.getSelection())
						selectedCheckBoxes.add("variables");
					if(checkFieldsBtn.getSelection())
						selectedCheckBoxes.add("fields");
					System.out.println(selectedCheckBoxes.toString());

					try {
						if(!selectedCheckBoxes.isEmpty() || isExtChecked())
							processFile(openedFile, wordInput, selectedCheckBoxes);
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

					for(Button btn : checkBtnsFromExtensions) {
						if(btn.getSelection()) {
							System.out.println(btn.getText() + " is selected");
							int[] extOffsets = resultsFromExtensions.get(btn.getText());
							for(int offset:  extOffsets) {
								results.add(offset);
							}
						}
					}
					
					if(!results.isEmpty())
						javaServ.selectText(openedFile, results.poll(), wordInput.length());
					else
						new Label(viewArea, SWT.COLOR_DARK_RED).setText(wordInput + " was not found");
					viewArea.layout();
				}
			}			

			// --------------------------Helper methods--------------------------------
			private void processFile(File f, String input, ArrayList<String> selectedCheckBoxes) throws FileNotFoundException {
				SearchEngine se = new SearchEngine(input, selectedCheckBoxes);
				javaServ.parseFile(f, se);
				results = se.getResults();
				
				// giving to the extensions the resources needed for the search
				for(IConfigurationElement e : elements) {
					String name = e.getAttribute("name");
					try {
						SearchSuggestion option = (SearchSuggestion) e.createExecutableExtension("class");
						resultsFromExtensions.put(name, option.offsetResults(f, input));
						System.out.println("results from extensions: " + resultsFromExtensions);
					} catch (CoreException e1) {
						e1.printStackTrace();
					}
				}
			}
			
			private boolean isExtChecked() {
				for(Button btn : checkBtnsFromExtensions) {
					if(btn.getSelection())
						return true;
				}
				return false;
			}
		});

		nextBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(!results.isEmpty()) {
					javaServ.selectText(openedFile, results.poll(), wordInput.length());
				}
			} 
		});

		//Get the extensions and create check boxes 
		IExtensionRegistry reg = Platform.getExtensionRegistry();
		elements = reg.getConfigurationElementsFor("pt.iscte.pidesco.search.suggestions");
		for(IConfigurationElement e : elements) {
			String name = e.getAttribute("name");
			Button newCheckBtn = new Button(viewArea, SWT.CHECK);
			newCheckBtn.setText(name);
			checkBtnsFromExtensions.add(newCheckBtn);			
		}
	}

}
