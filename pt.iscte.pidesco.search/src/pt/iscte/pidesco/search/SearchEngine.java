package pt.iscte.pidesco.search;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

public class SearchEngine extends ASTVisitor{

	private String word = "";
	private ArrayList<String> options;
	private Queue<Integer> results;
	
	public SearchEngine(String word, ArrayList<String> options) {
		System.out.println("SearchEngine Created, starting...");
		this.word=word;
		this.options=options;
		results = new LinkedList<Integer>();
	}
	
	public Queue<Integer> getResults() {return results;}
	
	public boolean visit(FieldDeclaration node) {
		System.out.println("entered in fields");
		if(options.contains("fields") && options.size() == 1) {
			results.add(node.getStartPosition());
			return false;
		}else
			return true;
	}
	
	public boolean visit(MethodDeclaration node) {
		System.out.println("entered in methods");
		String name = node.getName().toString();
		if(options.contains("methods") && options.size() == 1) {
			if(name.equals(word))
				results.add(node.getStartPosition());
			return false;
		}else if(options.contains("methods")){
			if(name.equals(word))
				results.add(node.getStartPosition());
			return true;
		}else {
			return true;
		}
	}
	
	public boolean visit(VariableDeclarationFragment node) {
		System.out.println("entered in variables");
		String name = node.getName().toString();
		
		if(options.contains("variables")) {
			if(name.equals(word))
				results.add(node.getStartPosition());
		}
		
		int pos = node.getStartPosition(); // posição do primeiro character no ficheiro
		int length = node.getLength(); // tamanho do nome
		System.out.println("Variable name: " + name + ", offset: " + pos + ", length: " + length);
		return false;
	
	}
	
}
