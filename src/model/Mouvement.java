package model;
import javax.swing.JLabel;


public class Mouvement {
	
	public int x,y,u,v;
	public JLabel label;
	public boolean check; 
	
	public Mouvement(int X,int Y,int U,int V,JLabel Label,boolean Check) {
		x=X;y=Y;u=U;v=V;
		label=Label;
		check=Check;
	}

}
