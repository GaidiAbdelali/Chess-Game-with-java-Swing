package view;
import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.border.EmptyBorder;

public class Accueil extends JFrame {
	
	ChessGame chess;
	private JPanel contentPane;
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Accueil frame = new Accueil();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Accueil() {
		setAutoRequestFocus(false);
		setTitle("Jeu des \u00E9checs");
		setIconImage(Toolkit.getDefaultToolkit().getImage("pieces/cavalier_noir.png"));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setLocationRelativeTo(null);
		//setLocation(456, 145);
		setBounds(300, 200, 470, 380);
		contentPane = new JPanel();
		contentPane.setBackground(SystemColor.inactiveCaptionBorder);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		TypeButton btnNewButton = new TypeButton("Joueur Vs Joueur");

		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				chess = new ChessGame(false);
				chess.setSize(760,730);
				chess.setLocationRelativeTo(null);
				chess.setVisible(true);
			}
		});
		btnNewButton.setBounds(120, 100, 230, 36);
		contentPane.add(btnNewButton);
		
		TypeButton btnNewButton_1 = new TypeButton("Joueur Vs Ordinateur");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				chess = new ChessGame(true);
				chess.setSize(760,730);
				chess.setLocationRelativeTo(null);
				chess.setVisible(true);
			}
		});
		btnNewButton_1.setBounds(120, 140, 230, 36);
		contentPane.add(btnNewButton_1);
		
		TypeButton btnNewButton_2 = new TypeButton("Quitter");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int retour = JOptionPane.showConfirmDialog(null, "voulez vous vraiment quitter !?", " Quitter ",JOptionPane.YES_NO_OPTION);
				if( retour == JOptionPane.YES_OPTION ){
					System.exit(0);
				}
			}
		});
		btnNewButton_2.setBounds(120, 180, 230, 36);
		contentPane.add(btnNewButton_2);
		
		JLabel lblJeuDeschecs = new JLabel("Jeu des \u00E9checs");
		lblJeuDeschecs.setForeground(Color.BLACK);
		lblJeuDeschecs.setBackground(new Color(51, 204, 0));
		lblJeuDeschecs.setFont(new Font("Century Schoolbook", Font.BOLD, 23));
		lblJeuDeschecs.setBounds(140, 11, 230, 50);
		contentPane.add(lblJeuDeschecs);
		
		ImageIcon ic = new ImageIcon("pieces/Chess-Photo.jpg");
		JLabel label = new JLabel(ic);
		label.setBounds(-30, 0, 635, 402);
		contentPane.add(label);
	}
	
	
}