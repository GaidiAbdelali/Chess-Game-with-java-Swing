package view;
import java.awt.Color;
import java.awt.Font;

import javax.swing.JButton;


public class TypeButton extends JButton{

	public TypeButton(String contenu) {
		this.setText(contenu);
		this.setForeground(Color.BLACK);
		this.setBackground(Color.WHITE);
		this.setFont(new Font("Century Schoolbook", Font.PLAIN, 16));
		this.setOpaque(true);
	}

}
