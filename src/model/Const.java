package model;
import java.awt.Color;


public interface Const {
	final String[] piece_noir={"pieces/pion_noir.png","pieces/fou_noir.png","pieces/cavalier_noir.png","pieces/tour_noir.png","pieces/reine_noir.png","pieces/roi_noir.png"};
	final String[] piece_blanc={"pieces/pion_blanc.png","pieces/fou_blanc.png","pieces/cavalier_blanc.png","pieces/tour_blanc.png","pieces/reine_blanc.png","pieces/roi_blanc.png"};

	final Color beg=new Color(223,191,255).darker();
	final Color beeg=new Color(223,191,255).darker().darker();
	final Color blue=new Color(152,231,228);
	final Color bluee=new Color(167,167,167);
}
