package view;
import java.awt.EventQueue;
import java.io.File;
import java.io.FilenameFilter;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;


public class Open {
	
	public static void listerRepertoire(File repertoire){ 
		String [] listefichiers; 

		int i; 
		listefichiers=repertoire.list(); 
		for(i=0;i<listefichiers.length;i++){ 
		System.out.println(listefichiers[i]); 

		} 
		}
	 
		
//		public static void main(String[] args) {
//			EventQueue.invokeLater(new Runnable() {
//				public void run() {
//					listerRepertoire(new File(System.getProperty("C:\\Users\\houssam\\workspace\\ChessComple")));
//				}
//			});
//		}
}
