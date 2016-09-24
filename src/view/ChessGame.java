package view;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import model.Const;
import model.IA;
import model.Mouvement;

/**
 * cette class contient la construction de l'echiquier du zero jusqua'à la fin 
 * et contient aussi les régles du jeu 
 * 
 * @author BOUGHTI Houssam/GAIDI Abdelali
 * 
 * */

public class ChessGame extends JFrame implements MouseListener,ActionListener,Const {
	
	JPanel chessBoard = new JPanel();
	private JLabel chessPiece=null;
	private JLabel chessPiecePrec=null;
	  
	/**
	 * les boolean du programme
	 * 
	 * @param genre: indique le genre qui va jouer (le noir ou bien le blanc) 
	 * @param check: indique le type de la situation (si check ou non)
	 * @param Tour : indique en cas de situation de check,le genre qui la cause 
	 * @param PossibleDeDefendre: indique en cas de check si au moins une piece peut intervenir pour annuler cette situation sans que le roi bouge
	 * @param  DejaPasser:
	 * @param PremierMvtDeRoiBlanc: indique si le roi des blancs a bouger au moins une fois (true) sert à connetre si le roi a le droit de Rook (c-à-d changer la place du roi avec la tour)
	 * @param PremierMvtDeRoiNoir : de même que le boolean precedant mais cette fois c'est pour les noirs
	 * */
	private boolean genre=true,check=false,Tour;
	private boolean PossibleDeDefendre=false,DejaPasser=false;
	private boolean PremierMvtDeRoiBlanc=false,PremierMvtDeRoiNoir=false;
	private boolean VsOrdi;
	  
	/**
	 * @param XCauseCheck: enregitre l'absice de la piece qui a causer la situation de check
	 * @param YCauseCheck: enregitre l'ordonne de la piece qui a causer la situation de check
	 * @param XChemin,YChemin : enregistre la combinaison quand doit ajouter aux coordoners de la cause de check (XCauseCheck,YCauseCheck) pour arriver au roi 
	 * 
	 * */
	private int XCauseCheck=-1,YCauseCheck=-1;
	private int XChemin=0,YChemin=0;
	  
	/**
	 * @param PlacePossibleRoiBlancComp = toutes les places qui entour le roi blanc plus sa place actuelle (9 places)
	 * @param PlacePossibleRoiBlanc = toutes les places que le roi peut bouger sur parmit les 9 places precedent
	 * @param DefenceRoi = les places que les autres pieces de même genre peut defendre parmit les 9 places
	 * 
	 * */
	private int[] PlacePossibleRoiBlanc = {-1,-1,-1,-1,-1,-1,-1,-1,-1};
	private int[] PlacePossibleRoiNoir = {-1,-1,-1,-1,-1,-1,-1,-1,-1};
	private int[] PlacePossibleRoiBlancComp = {-1,-1,-1,-1,-1,-1,-1,-1,-1};
	private int[] PlacePossibleRoiNoirComp = {-1,-1,-1,-1,-1,-1,-1,-1,-1};
	private int[] DefenceRoi = {-1,-1,-1,-1,-1,-1,-1,-1,-1};
	  
	
	private JTextField Noir=new JTextField();
	private JTextField Blanc=new JTextField();
	JEditorPane edit;
	
	TypeButton Prec=new TypeButton("<-");
	TypeButton New=new TypeButton("New");
	TypeButton Save=new TypeButton("Save");
	TypeButton Open=new TypeButton("Open");
	TypeButton Next=new TypeButton("->");
	
	String ligne="";
	
	/**
	 * @param l : liste des mouvement de la partie actuelle 
	 * @param mvt : contient le mouvement de ce tour sous forme de String 
	 * @param x,y : les coordonners de la nouvelle place de la piece qu'on a jouer
	 * @piece u,v : les coordonners de l'ancienne place de la piece qu'on a jouer
	 * */
	private LinkedList<Mouvement> l;
	private String mvt="";
	private int x,y,u,v;
	
	private BufferedReader br;

	private IA IntelArtif;
	  
	  public ChessGame(boolean VsOrdi) {
		   
		  this.VsOrdi=VsOrdi;
		  
		  if(VsOrdi){
			  IntelArtif=new IA(chessBoard,PlacePossibleRoiBlanc,PlacePossibleRoiNoir);
			  Prec.setEnabled(false);
		  }
	
		  l = new LinkedList<Mouvement>();
		  edit=new JEditorPane();
		  
		  Dimension boardSize = new Dimension(640, 640);
		  edit.setSize(100,730);
		  edit.setEditable(false);
		  
		  JLayeredPane layered=new JLayeredPane();
		  layered.setPreferredSize( boardSize );
		  layered.addMouseListener(this);
		  
		  layered.add(chessBoard,JLayeredPane.DEFAULT_LAYER);
		  chessBoard.setLayout( new GridLayout(8, 8) );
		  chessBoard.setPreferredSize( boardSize );
		  chessBoard.setBounds(0, 0, boardSize.width, boardSize.height);
		 
		  JPanel buttonPane =new JPanel();
		  buttonPane.setLayout(new GridLayout(1,4));
		  
		  buttonPane.add(New);
		  buttonPane.add(Open);
		  buttonPane.add(Save);
		  buttonPane.add(Prec);
		  buttonPane.add(Next);
		  Next.setEnabled(false);
		  
		  BorderLayout b=new BorderLayout();
		  getContentPane().add(buttonPane,b.NORTH);
	
		  JPanel numPane = new JPanel(new GridLayout(8,1));
		  JPanel lettrePane = new JPanel(new GridLayout(1,8));
		  getContentPane().add(numPane,b.WEST);
		  getContentPane().add(lettrePane,b.SOUTH);
		  getContentPane().add(layered,b.CENTER);
		  JPanel EastPanel = new JPanel();
		  getContentPane().add(EastPanel,b.EAST);
		 
		  Noir.setEnabled(false);
		  Blanc.setEnabled(false);
		  BorderLayout bl=new BorderLayout();
		  EastPanel.setLayout(bl);
		  EastPanel.add(Noir,bl.NORTH);
		  EastPanel.add(new JScrollPane(edit),bl.CENTER);
		  EastPanel.add(Blanc,bl.SOUTH);
		  Blanc.setBackground(Color.BLACK);
		  
		  String c =null;
		  for(int i=0;i<8;i++){
			  JLabel lab=new JLabel(c.valueOf(i+1));
			  numPane.add(lab);
		  }
		  
			  JLabel lab;
			  c="A";lab=new JLabel(c);lettrePane.add(lab);
			  c="B";lab=new JLabel(c);lettrePane.add(lab);
			  c="C";lab=new JLabel(c);lettrePane.add(lab);
			  c="D";lab=new JLabel(c);lettrePane.add(lab);
			  c="E";lab=new JLabel(c);lettrePane.add(lab);
			  c="F";lab=new JLabel(c);lettrePane.add(lab);
			  c="G";lab=new JLabel(c);lettrePane.add(lab);
			  c="H";lab=new JLabel(c);lettrePane.add(lab);
			  
		  Prec.addActionListener(this);
		  New.addActionListener(this);
		  Save.addActionListener(this);
		  Open.addActionListener(this);
		  Next.addActionListener(this);
		  
		  for (int i = 0; i < 64; i++) {
			  JPanel square = new JPanel();
			  square.setBorder(BorderFactory.createLineBorder(Color.WHITE));
			  chessBoard.add( square );
			  square.setBounds((i%8)*80, (i/8)*80, 80, 80);
			  int row = (i/8) % 2;
			  if (row == 0)  square.setBackground( i % 2 == 0 ? blue : bluee);
			  else  square.setBackground( i % 2 == 0 ? bluee : blue);
			  }
		  //ajout des pieces
		 New();
		 
		 FileInputStream file;
		  InputStreamReader fileReader;
		  try{
		  	file=new FileInputStream("Standare.txt"); 
		  	fileReader=new InputStreamReader(file);
			br=new BufferedReader(fileReader);
			}
		  catch(Exception e){System.out.println(e.toString());}
		 
	} 
	  public void mouseEntered(MouseEvent e){}
	  public void mouseExited(MouseEvent e) {}
	  public void mouseClicked(MouseEvent me) {}
	  
	  public void mousePressed(MouseEvent me) {
		  if(genre)
			  TourDeRole(me,genre,piece_blanc);
		  else
			  TourDeRole(me,genre,piece_noir);
		  }
	  /**
	   * permet l'execution du mouvement plus les calculs pour les places possible pour le roi, 
	   * les pieces qui ont le droit de bouger, 
	   * plus l'implementation de l'intelligence artificial
	   * */
	  public void mouseReleased(MouseEvent me) {
		  JPanel panel = (JPanel)chessBoard.getComponent(position(ligne(me),colonne(me)));
		  Color backg=panel.getBackground();
		  if(backg.equals(beg)){
			  Component p=getComponentAt(panel);
			  if(p instanceof JLabel){
				  if(chessPiecePrec!=null){
				  Component c =  chessBoard.findComponentAt(me.getX(), me.getY());
				  if(verifierPieceBlanc(!genre,chessPiece)==1){
					  chessPiece.setVisible(false);
					  chessPiecePrec.setVisible(false);
					  if (c instanceof JLabel){
						  Container parent = c.getParent();
						  parent.remove(chessPiece);
						  parent.add(chessPiecePrec);  }
					  else {
						  Container parent = (Container)c;
						  parent.remove(chessPiece);
						  parent.add(chessPiecePrec); }
					  chessPiecePrec.setVisible(true);
					  decolorer(false);
					  TraitementDeFinDuTour(me);
					  
				  }
			  }
			  }
			  else{
				  Component c =  chessBoard.findComponentAt(me.getX(), me.getY());
				  chessPiece.setVisible(false);
				  if (c instanceof JLabel){
					  Container parent = c.getParent(); 
					  parent.add(chessPiece);}
				  else {
					  Container parent = (Container)c;
					  parent.add(chessPiece); }
				  chessPiece.setVisible(true);
				  decolorer(false);
				  TraitementDeFinDuTour(me);
			  }
			  if(VsOrdi){
				  if(ligne!=null){
					  ExecuterMvt();
					  genre=!genre;
					  if(ligne==null){genre=!genre;}
				  }
				  if(ligne==null){
					  IntelArtif.Prondeur1Tree();
					  TraitementDeFinDuTour(IntelArtif.getDebutMvt(),IntelArtif.getFinMvt());
				  }
			  }
		  }
		  if(backg.equals(beeg)){
			  Component c =  chessBoard.findComponentAt(me.getX(), me.getY());
			  chessPiece.setVisible(false);
			  if (c instanceof JLabel){
				  Container parent = c.getParent(); 
				  parent.add(chessPiece);}
			  else {
				  Container parent = (Container)c;
				  parent.add(chessPiece); }
			  chessPiece.setVisible(true);
			  decolorer(false);
			  
			  int pos=position(ligne(me),colonne(me));
			  if(pos==62){RockMvt(63,61);}
			  if(pos==58){RockMvt(56,59);}
			  if(pos==6){RockMvt(7,5);}
			  if(pos==2){RockMvt(0,3);}
			  
			  TraitementDeFinDuTour(me);
			  if(VsOrdi){
				  IntelArtif.Prondeur1Tree();
				  TraitementDeFinDuTour(IntelArtif.getDebutMvt(),IntelArtif.getFinMvt());
			  }
		  }
		  
	}
	  /**
	   * la gestion du debut du mouvment c-à-d ici que le joueur choisit la piece à jouer et la fonction lui montre ses mouvements possible
	   * 
	   *   @param type: c'est le controleur du cote à jouer si les blancs ou bien les noirs
	   *   @param tab: c'est le tableau des piece blanc en cas si type est true et noir dans l'autre cas
	   * */
	  public void TourDeRole(MouseEvent me,boolean type,String[] tab){
		  
			 Component c =  chessBoard.findComponentAt(me.getX(), me.getY());
			 if (c instanceof JLabel){
				  chessPiecePrec=chessPiece;
				  chessPiece=(JLabel)c;
				  Icon piece=chessPiece.getIcon();
					  if(chessPiecePrec==null){
						  x=ligne(me);y=colonne(me);
						  mvt="";
						  mvt=mvt+transf(x)+(y+1)+"-";
						  
						  if(piece.toString().equals(tab[0])){pion(x,y,type,true);}
						  else if(piece.toString().equals(tab[1])){fou(x,y,type,true);}
						  else if(piece.toString().equals(tab[2])){cavalier(x,y,type,true);}
						  else if(piece.toString().equals(tab[3])){tour(x,y,type,true);}
						  else if(piece.toString().equals(tab[4])){reine(x,y,type,true);}
						  else if(piece.toString().equals(tab[5])){Rock();roi(x,y,type,true);}
						  else if(verifierPieceBlanc(!type,chessPiece)==1){ chessPiece=null;}
					  }
					  else{
						  if(verifierPieceBlanc(genre,chessPiece)==1){decolorer(false);chessPiece=null;}
					  }
			}
			else return;
	}
	  /**
	   * @param arg: un bouton de la barre des option possible
	   * 
	   * 1.commencer une nouvelle partie
	   * 2.ouvrir une partie déja existante
	   * 3.sauvgarder la partie en court ou bien la partie qui vien de se terminer
	   * 4.annuler un mouvement ou plus
	   * 5.quand on charge unr partie deje enregistrer c'est le bouton qui nous permet de la faire passer mouvement par mouvement 
	   * */
	  public void actionPerformed(ActionEvent arg) {
		  if(arg.getSource()==Prec){
			  if(l!=null){
				  Mouvement m =(Mouvement)l.getFirst();
				  Retour(m.u,m.v,m.x,m.y,m.label);
				  check=m.check;
				  l.remove(0);  
			  }
		  }
		  else if(arg.getSource()==Save){
			  FileWriter writer = null;
			  String texte=edit.getText();
			  JOptionPane op = new JOptionPane();
			  String NomPartie;
				NomPartie = op.showInputDialog(null,"Nom de la partie:" ,"Sauvegarder la partie", JOptionPane.QUESTION_MESSAGE);
				NomPartie =NomPartie+".txt";
				try{
					writer = new FileWriter(NomPartie, true);
					writer.write(texte,0,texte.length());
				}catch(IOException ex){
					ex.printStackTrace();
				}finally{if(writer != null){try {writer.close();} catch (IOException e) {e.printStackTrace();}}}
		  }
		  else if(arg.getSource()==New){
			  removeAll();
		  }
		  else if(arg.getSource()==Open){
			  String NomPartie=OuvrirPartie();
			  FileInputStream file;
			  InputStreamReader fileReader;
			  try{
			  	file=new FileInputStream(NomPartie); 
			  	fileReader=new InputStreamReader(file);
				br=new BufferedReader(fileReader);
				br.readLine();
				}
			  catch(Exception e){System.out.println(e.toString());}
			  Next.setEnabled(true);
		  }
		  else if(arg.getSource()==Next){
			  ExecuterMvt();
		  }
	  }
	  
	  /*petites fonctions*/
	  public int ligne(MouseEvent me){
		  int x=me.getX();
		  int s=80,i=0;
		  while(x>s){
			  s=s+80;i++;}
		  return i;
	  }
	  public int colonne(MouseEvent me){
		  int y=me.getY();
		  int s=80,j=0;
		  while(y>s){
			  s=s+80;j++;}
		  return j;
	  } 
	  public int position(int i,int j){
		  return i+8*j;
	  }
	  public char transf(int x){
		  switch(x){
		  case 0:return('A');
		  case 1:return('B');
		  case 2:return('C');
		  case 3:return('D');
		  case 4:return('E');
		  case 5:return('F');
		  case 6:return('G');
		  case 7:return('H');
		  default :return 'A';
		  }
		
	  }
	  public char transfIvers(char c){
		  switch(c){
		  case 'A':return(0);
		  case 'B':return(1);
		  case 'C':return(2);
		  case 'D':return(3);
		  case 'E':return(4);
		  case 'F':return(5);
		  case 'G':return(6);
		  case 'H':return(7);
		  default :return 19;
		  }
	  }
	  /**
	   * fonction qui rend la table de jeu a sa couleur initiale apres chaque mouvement
	   * 
	   * @param ConsCheck:si se boolean est true,on decolore même la couleur jaune sur le roi en cas de check  
	   * */
	  public void decolorer(boolean ConsCheck){
		  JPanel panel;
		  Color col1;
		  Color col2;
		  for(int i=0;i<8;i++){
			  if(i%2==0){col1=blue;col2= bluee;}
			  else{col1= bluee;col2=blue;}
			  for(int j=0;j<8;j++){
				  panel = (JPanel)chessBoard.getComponent(i+j*8);
				  Color yel=panel.getBackground();
				  if(ConsCheck && yel.equals(Color.YELLOW)){}
				  else{
					  if(j%2==0){panel.setBackground(col1);}
					  else{panel.setBackground(col2);}
				  }
			  }
		  }
	  } 
	  public Component getComponentAt(Container parent) {
	        for (Component child : parent.getComponents()) {
	            if (child instanceof JLabel) {
	                return child;
	            }
	        }
	        return null;
	   }
	  /*fonctions des boutons*/
	  /**
	   * enregistere les mouvement dans une liste afin de pouvoir revenir en arriere
	   * 
	   * @param label c'est la piece prise par l'adversere dans se tour 
	   * @param check c'est l'etat du boolean Check a se tour 
	   * */
	  public void saveList(int x,int y,int u,int v,JLabel label,boolean Check){
		  Mouvement m=new Mouvement(x,y,u,v,label,Check);
		  l.addFirst(m);
	  }
	  public void Retour(int x,int y,int u,int v,JLabel label){
		  
		  JLabel piece=null;
		  JPanel panel = (JPanel)chessBoard.getComponent(position(x,y));
		  Component p=getComponentAt(panel);
	  	  if(p instanceof JLabel){
			  piece=(JLabel)p; 		  
	  		  piece.setVisible(false);
	  		  panel.remove(piece);
	  		  if(label !=null){
	  			  panel.add(label);
	  			  label.setVisible(true);}
	  		  
	  	  
	  	  panel = (JPanel)chessBoard.getComponent(position(u,v));
	  	  p= getComponentAt(panel);
	  	  panel.add(piece);
	  	  piece.setVisible(true);
	  	  }
	  	  mvt="";
	  	  mvt=mvt+transf(x)+(y+1)+"-"+transf(u)+(v+1);
	  	  edit.setText(edit.getText()+("\n"+mvt)+(",Annuler"));
	  	  decolorer(false); 
	  	  chessPiece=null;chessPiecePrec=null;
	  	  genre=!genre;
	  	  String[] tab;
	  	  if(genre){tab=piece_blanc;}
	  	  else{tab=piece_noir;}
	  	  Icon Ipiece = piece.getIcon();
	  	  if(Ipiece.toString().equals(tab[5])){roi(u,v,genre,false);}
	  	  
	  	  MvtImpossiblePourRoiTotal(!genre);decolorer(false);
	  	
	  }
	  /**
	   * ces deux fonctions recommence une nouvelle partie on initialisons tous ce qu'il faut
	   * removeAll()
	   * New()
	   * */
	  public void removeAll(){
		  for(int i=0;i<64;i++){
			    JPanel panel = (JPanel)chessBoard.getComponent(i);
				Component p=getComponentAt(panel);
			    if(p instanceof JLabel){
			    	JLabel piece=(JLabel)p;
			    	piece.setVisible(false);
		 			panel.remove(piece);
			    }
		  }
		  New();
		  edit.setText("");
		  genre=true;
		  check=false;
		  decolorer(false);
		  for(int i=0;i<9;i++){
			  PlacePossibleRoiBlanc[i]=-1;
			  PlacePossibleRoiNoir[i]=-1;
			  PlacePossibleRoiBlancComp[i] =-1;
			  PlacePossibleRoiNoirComp[i] =-1;
			  DefenceRoi[i]=-1;
		  }
		  l = new LinkedList();
		  PremierMvtDeRoiBlanc=false;
		  PremierMvtDeRoiNoir=false;
		  PossibleDeDefendre=false;
		  DejaPasser=false;
		  XCauseCheck=-1;YCauseCheck=-1;
		  XChemin=0;YChemin=0;
	  }
	  public void New(){
		  
		  JLabel piece = new JLabel( new ImageIcon("pieces/tour_noir.png") );
		  JPanel panel = (JPanel)chessBoard.getComponent(0);
		  panel.add(piece);
		  piece = new JLabel(new ImageIcon("pieces/cavalier_noir.png"));
		  panel = (JPanel)chessBoard.getComponent(1);
		  panel.add(piece);
		  piece = new JLabel(new ImageIcon("pieces/fou_noir.png"));
		  panel = (JPanel)chessBoard.getComponent(2);
		  panel.add(piece);
		  piece = new JLabel(new ImageIcon("pieces/reine_noir.png"));
		  panel = (JPanel)chessBoard.getComponent(3);
		  panel.add(piece);
		  piece = new JLabel(new ImageIcon("pieces/roi_noir.png"));
		  panel = (JPanel)chessBoard.getComponent(4);
		  panel.add(piece);
		  piece = new JLabel(new ImageIcon("pieces/fou_noir.png"));
		  panel = (JPanel)chessBoard.getComponent(5);
		  panel.add(piece);
		  piece = new JLabel(new ImageIcon("pieces/cavalier_noir.png"));
		  panel = (JPanel)chessBoard.getComponent(6);
		  panel.add(piece);
		  piece = new JLabel( new ImageIcon("pieces/tour_noir.png") );
		  panel = (JPanel)chessBoard.getComponent(7);
		  panel.add(piece);
		  piece = new JLabel(new ImageIcon("pieces/pion_noir.png"));
		  panel = (JPanel)chessBoard.getComponent(8);
		  panel.add(piece);
		  piece = new JLabel(new ImageIcon("pieces/pion_noir.png"));
		  panel = (JPanel)chessBoard.getComponent(9);
		  panel.add(piece);
		  piece = new JLabel(new ImageIcon("pieces/pion_noir.png"));
		  panel = (JPanel)chessBoard.getComponent(10);
		  panel.add(piece);
		  piece = new JLabel(new ImageIcon("pieces/pion_noir.png"));
		  panel = (JPanel)chessBoard.getComponent(11);
		  panel.add(piece);
		  piece = new JLabel(new ImageIcon("pieces/pion_noir.png"));
		  panel = (JPanel)chessBoard.getComponent(12);
		  panel.add(piece);
		  piece = new JLabel(new ImageIcon("pieces/pion_noir.png"));
		  panel = (JPanel)chessBoard.getComponent(13);
		  panel.add(piece);
		  piece = new JLabel(new ImageIcon("pieces/pion_noir.png"));
		  panel = (JPanel)chessBoard.getComponent(14);
		  panel.add(piece);
		  piece = new JLabel(new ImageIcon("pieces/pion_noir.png"));
		  panel = (JPanel)chessBoard.getComponent(15);
		  panel.add(piece);
		  
		  
		  piece = new JLabel(new ImageIcon("pieces/tour_blanc.png"));
		  panel = (JPanel)chessBoard.getComponent(63);
		  panel.add(piece);
		  piece = new JLabel(new ImageIcon("pieces/cavalier_blanc.png"));
		  panel = (JPanel)chessBoard.getComponent(62);
		  panel.add(piece);
		  piece = new JLabel(new ImageIcon("pieces/fou_blanc.png"));
		  panel = (JPanel)chessBoard.getComponent(61);
		  panel.add(piece);
		  piece = new JLabel(new ImageIcon("pieces/roi_blanc.png"));
		  panel = (JPanel)chessBoard.getComponent(60);
		  panel.add(piece);
		  piece = new JLabel(new ImageIcon("pieces/reine_blanc.png"));
		  panel = (JPanel)chessBoard.getComponent(59);
		  panel.add(piece);
		  piece = new JLabel(new ImageIcon("pieces/fou_blanc.png"));
		  panel = (JPanel)chessBoard.getComponent(58);
		  panel.add(piece);
		  piece = new JLabel(new ImageIcon("pieces/cavalier_blanc.png"));
		  panel = (JPanel)chessBoard.getComponent(57);
		  panel.add(piece);
		  piece = new JLabel(new ImageIcon("pieces/tour_blanc.png"));
		  panel = (JPanel)chessBoard.getComponent(56);
		  panel.add(piece);
		  piece = new JLabel(new ImageIcon("pieces/pion_blanc.png"));
		  panel = (JPanel)chessBoard.getComponent(55);
		  panel.add(piece);
		  piece = new JLabel(new ImageIcon("pieces/pion_blanc.png"));
		  panel = (JPanel)chessBoard.getComponent(54);
		  panel.add(piece);
		  piece = new JLabel(new ImageIcon("pieces/pion_blanc.png"));
		  panel = (JPanel)chessBoard.getComponent(53);
		  panel.add(piece);
		  piece = new JLabel(new ImageIcon("pieces/pion_blanc.png"));
		  panel = (JPanel)chessBoard.getComponent(52);
		  panel.add(piece);
		  piece = new JLabel(new ImageIcon("pieces/pion_blanc.png"));
		  panel = (JPanel)chessBoard.getComponent(51);
		  panel.add(piece);
		  piece = new JLabel(new ImageIcon("pieces/pion_blanc.png"));
		  panel = (JPanel)chessBoard.getComponent(50);
		  panel.add(piece);
		  piece = new JLabel(new ImageIcon("pieces/pion_blanc.png"));
		  panel = (JPanel)chessBoard.getComponent(49);
		  panel.add(piece);
		  piece = new JLabel(new ImageIcon("pieces/pion_blanc.png"));
		  panel = (JPanel)chessBoard.getComponent(48);
		  panel.add(piece);
		  
	  }
	  public String OuvrirPartie(){
		  	removeAll();
		    String nomPartie;
		  	JOptionPane op = new JOptionPane();
			nomPartie = op.showInputDialog(null,"Nom de la partie:" ,"Ouvrir une partie", JOptionPane.QUESTION_MESSAGE);
			nomPartie =nomPartie+".txt";
			return nomPartie;
	  }
	  public void ExecuterMvt(){
		  	String[] decouper;char[] chars;String[] Deuxiemedecouper;
			int ordI,absI,ordF,absF;
			JPanel panel;Component p;
			
			try{
				ligne=br.readLine();
				if(ligne!=null){
					decouper=ligne.split("-");
				
					chars = decouper[0].toCharArray();
					ordI=transfIvers(chars[0]);
					absI=(int)chars[1]-'0'-1;
					
					Deuxiemedecouper=decouper[1].split(",");
					chars = Deuxiemedecouper[0].toCharArray();
					ordF=transfIvers(chars[0]);
					absF=(int)chars[1]-'0'-1;
				
					panel = (JPanel)chessBoard.getComponent(position(ordI,absI));
					p= getComponentAt(panel);
					JLabel piece = (JLabel)p;
					piece.setVisible(false);
					
					panel= (JPanel)chessBoard.getComponent(position(ordF,absF));
					p= getComponentAt(panel);
					if(p instanceof JLabel){
						JLabel Npiece = (JLabel)p;
						Npiece.setVisible(false);
					}
					panel.add(piece);
					piece.setVisible(true);
			}
			}catch(Exception me){me.toString();}
			
	}
	  /*fonctions très utiles*/
	  public void CheckMat(boolean type){
		  DefencePourRoi(!type);
		  DefencePourRoiTotal(type);
		  for(int i=0;i<9;i++){
			  if(type){if(PlacePossibleRoiNoir[i]!=-1 && i!=6 ){PossibleDeDefendre=false;return ;}}
			  else{if(PlacePossibleRoiBlanc[i]!=-1 && i!=6 ){PossibleDeDefendre=false;return ;}}
		  }
		  if(PossibleDeDefendre) {PossibleDeDefendre=false;return ;}
		  JOptionPane checkMate = new JOptionPane();
		  checkMate.showMessageDialog(null, "check Mat","check Mat",JOptionPane.INFORMATION_MESSAGE);
	}
	  /**
	   * @param position: cherche cette position dans le tableau des positions d'un roi suivant la valeur du boolean genre
	   * @param genre: si true tableau du roi blanc sinon tableau du roi noir 
	   * */
	  public int RecherchePosition(int position,boolean genre){
		  if(!genre){
			  for(int i=0;i<9;i++)
				  if(position==PlacePossibleRoiNoir[i])
					  return 1;
			  return 0;
		  }
		  else{
			  for(int i=0;i<9;i++)
				  if(position==PlacePossibleRoiBlanc[i])
					  return 1;
			  return 0;
		  }
	  }
	  public int verifierPieceBlanc(boolean genre,JLabel piece){
		  Icon Ipiece = piece.getIcon();
		  if(!genre){
			  for(int i=0;i<6;i++) 
				  if(Ipiece.toString().equals(piece_noir[i]))
					  	return 1;
					  return 0;
		  }
		  else{
			  for(int i=0;i<6;i++) 
				  if(Ipiece.toString().equals(piece_blanc[i]))
					  	return 1;
					  return 0;
		  } 
	  }
	  /**
	   * cette fonction détermine les positions a éléminé en cas que les deux roi sont l'un près de l'autre
	   * */
	  public void RoiVsRoi(boolean genre){
		  if(genre){
			  for(int i=0;i<9;i++)
				  for(int j=0;j<9;j++)
					  if(PlacePossibleRoiBlanc[i]==PlacePossibleRoiNoirComp[j])
						  {PlacePossibleRoiBlanc[i]=-1;}
		  }
		  else{
			  for(int i=0;i<9;i++)
				  for(int j=0;j<9;j++)
					  if(PlacePossibleRoiNoir[i]==PlacePossibleRoiBlancComp[j])
						  {PlacePossibleRoiNoir[i]=-1;}
		  }
	  }
	  /**
	   * si un pion est arriver à la dernier ligne de l'adversere en le change par une piece de haute valeur 
	   * */
	  public void ChangerPion(int x,int y,boolean genre){
		  	String[] Cpiece = {"Cavalier","Fou","Tour","Reine"};
			JOptionPane jop = new JOptionPane(), jop2 = new JOptionPane();
			String nom = (String)jop.showInputDialog(null,"Veuillez choisir une piece","Changer Pion:",JOptionPane.QUESTION_MESSAGE,null,Cpiece,Cpiece[3]);
			JPanel panel = (JPanel)chessBoard.getComponent(position(x,y));
			Component p=getComponentAt(panel);
			JLabel piece =(JLabel)p;
			piece.setVisible(false);
			panel.remove(piece);
			String[] tab;
			if(genre) tab=piece_blanc;
			else tab=piece_noir;
			switch(nom){
			case "Reine":	piece = new JLabel( new ImageIcon(tab[4]) );
			  				panel = (JPanel)chessBoard.getComponent(position(x,y));
			  				panel.add(piece);break;
			case "tour":	piece = new JLabel( new ImageIcon(tab[3]) );
							panel = (JPanel)chessBoard.getComponent(position(x,y));
							panel.add(piece);break;
			case "Fou":		piece = new JLabel( new ImageIcon(tab[2]) );
							panel = (JPanel)chessBoard.getComponent(position(x,y));
							panel.add(piece);break;
			case "Cavalier":piece = new JLabel( new ImageIcon(tab[1]) );
							panel = (JPanel)chessBoard.getComponent(position(x,y));
							panel.add(piece);break;
				default:break;
			}
	  }
	  /**
	   * @param TourDeMvt: designe si la fonction est appele pour les calculs du fin tour (check,eliminer des positions pour le roi,etc..) s'il est false, 
	   * 		ou pour le debut du tour c'est à dire colorer les places possible pour la piece tout simplement, s'il est true. 
	   * */
	  public boolean mvtFouTour(int x,int y,boolean genre,boolean TourDeMvt){
		  boolean stop=false;
		  JPanel panel = (JPanel)chessBoard.getComponent(position(x,y));
		  Component p= getComponentAt(panel);
		  if(TourDeMvt){
			  if(!check){
			  if(p instanceof JLabel && verifierPieceBlanc(genre,(JLabel) p)==1){stop=true;}
			  if(stop==false){panel.setBackground(beg);}
			  if(!stop && p instanceof JLabel && verifierPieceBlanc(!genre,(JLabel) p)==1){stop=true;}
			  return stop;
			  }
			  else{
				  if(p instanceof JLabel && verifierPieceBlanc(genre,(JLabel) p)==1){return true;} 
				  for(int i=0;i<9;i++){if(position(x,y)==DefenceRoi[i]){panel.setBackground(beg);PossibleDeDefendre=true;return true;}}
				  if(p instanceof JLabel && verifierPieceBlanc(!genre,(JLabel) p)==1){return true;}
				  return false;
			  }
		  }
		  else{
			  if(p instanceof JLabel){
		  			Icon piece=((JLabel) p).getIcon();
		  			if(genre){ 
		  				if(piece.toString().equals(piece_noir[5])){check=true;panel.setBackground(Color.yellow);Tour=genre;return false;}
		  				else{
		  					for(int k=0;k<9;k++){
		  						if(position(x,y)==PlacePossibleRoiNoir[k])
	  								{PlacePossibleRoiNoir[k]=-1;}}
		  				}
		  			}
		  			else{
		  				if(piece.toString().equals(piece_blanc[5])){check=true;panel.setBackground(Color.yellow);Tour=genre;return false;}
		  				else{
		  					for(int k=0;k<9;k++){
		  						if(position(x,y)==PlacePossibleRoiBlanc[k])
	  								{PlacePossibleRoiBlanc[k]=-1;}}
		  				}
		  			
		  			}
		  			if(Tour!=genre){check=false;DejaPasser=false;}
		  			return true;
		  		}
		  		else{
		  			if(genre){
		  					for(int k=0;k<9;k++){
		  						if(position(x,y)==PlacePossibleRoiNoir[k])
		  							{PlacePossibleRoiNoir[k]=-1;}
		  						}
		  				}
		  			else{
		  					for(int k=0;k<9;k++){
		  						if(position(x,y)==PlacePossibleRoiBlanc[k])
		  							{PlacePossibleRoiBlanc[k]=-1;}}
		  				}
		  			return false;
		  			}
		  }
	  }
	  public void fou(int x,int y,boolean genre,boolean TourDeMvt){
		  boolean stop_gh=false; boolean stop_gb=false;boolean stop_dh=false;boolean stop_db=false;
		  for(int i=1;i<8;i++){
			  if(x-i>=0 && y-i>=0 && !stop_gh){stop_gh=mvtFouTour(x-i,y-i,genre,TourDeMvt);if(!TourDeMvt)CheminCheck(x,y,-1,-1);}
			  if(x-i>=0 && y+i<=7 && !stop_gb){stop_gb=mvtFouTour(x-i,y+i,genre,TourDeMvt);if(!TourDeMvt)CheminCheck(x,y,-1,1);}
			  if(x+i<=7 && y-i>=0 && !stop_dh){stop_dh=mvtFouTour(x+i,y-i,genre,TourDeMvt);if(!TourDeMvt)CheminCheck(x,y,1,-1);}
			  if(x+i<=7 && y+i<=7 && !stop_db){stop_db=mvtFouTour(x+i,y+i,genre,TourDeMvt);if(!TourDeMvt)CheminCheck(x,y,1,1);}  
		  }
	  }
	  public void tour(int x,int y,boolean genre,boolean TourDeMvt){
		  boolean stop_h=false; boolean stop_b=false;boolean stop_d=false;boolean stop_g=false;
		  for(int i=1;i<8;i++){
			  if(y-i>=0 && !stop_h){stop_h=mvtFouTour(x,y-i,genre,TourDeMvt);if(!TourDeMvt)CheminCheck(x,y,0,-1);}
			  if(y+i<=7 && !stop_b){stop_b=mvtFouTour(x,y+i,genre,TourDeMvt);if(!TourDeMvt)CheminCheck(x,y,0,1);}
			  if(x+i<=7 && !stop_d){stop_d=mvtFouTour(x+i,y,genre,TourDeMvt);if(!TourDeMvt)CheminCheck(x,y,1,0);}
			  if(x-i>=0 && !stop_g){stop_g=mvtFouTour(x-i,y,genre,TourDeMvt);if(!TourDeMvt)CheminCheck(x,y,-1,0);}
		  }  
	  }
	  public void reine(int x,int y,boolean genre,boolean TourDeMvt){
			 fou(x,y,genre,TourDeMvt);
			 tour(x,y,genre,TourDeMvt);
		 } 

	  public void mvtCavalier(int x,int y,boolean genre,boolean TourDeMvt){
		  Component p;JPanel panel;
		  boolean stop=false;
		  panel = (JPanel)chessBoard.getComponent(position(x,y));
	  	  p= getComponentAt(panel);
	  	  if(TourDeMvt){
	  		  if(!check){
	  		  if(p instanceof JLabel && verifierPieceBlanc(genre,(JLabel) p)==1){stop=true;}
	  		  if(stop==false) panel.setBackground(beg);
	  		  }
	  		else{
	  			if(p instanceof JLabel && verifierPieceBlanc(genre,(JLabel) p)==1){stop=true;}
	  			if(stop==false) {for(int i=0;i<9;i++){if(position(x,y)==DefenceRoi[i]){panel.setBackground(beg);PossibleDeDefendre=true;}}}
			  }
	  	  }
	  	else{
	  		if(p instanceof JLabel){
	  			Icon piece=((JLabel) p).getIcon();
	  			if(genre){ 
	  				if(piece.toString().equals(piece_noir[5])){check=true;panel.setBackground(Color.yellow);Tour=genre;}
	  				else{
	  					for(int k=0;k<9;k++){
	  						if(position(x,y)==PlacePossibleRoiNoir[k])
								{PlacePossibleRoiNoir[k]=-1;}}
	  				}
	  			}
	  			else{
	  				if(piece.toString().equals(piece_blanc[5])){check=true;panel.setBackground(Color.yellow);Tour=genre;}
	  				else{
	  					for(int k=0;k<9;k++){
	  						if(position(x,y)==PlacePossibleRoiBlanc[k])
								{PlacePossibleRoiBlanc[k]=-1;}}
	  				}
	  			
	  			}
	  			if(Tour!=genre){check=false;DejaPasser=false;}
	  		}
	  		else{
	  			if(genre){
	  					for(int k=0;k<9;k++){
	  						if(position(x,y)==PlacePossibleRoiNoir[k])
	  							{PlacePossibleRoiNoir[k]=-1;}
	  						}
	  				}
	  			else{
	  					for(int k=0;k<9;k++){
	  						if(position(x,y)==PlacePossibleRoiBlanc[k])
	  							{PlacePossibleRoiBlanc[k]=-1;}}
	  				}
	  		}
	  	}
	  }
	  public void cavalier(int x,int y,boolean genre,boolean TourDeMvt){
		  
		  if(x-2>=0){ if(y-1>=0){mvtCavalier(x-2,y-1,genre,TourDeMvt);if(!TourDeMvt){CheminCheck(x,y,0,0);}}
				  	  if(y+1<=7){mvtCavalier(x-2,y+1,genre,TourDeMvt);if(!TourDeMvt){CheminCheck(x,y,0,0);}}
		  }
		  if(x+2<=7){ if(y-1>=0){mvtCavalier(x+2,y-1,genre,TourDeMvt);if(!TourDeMvt){CheminCheck(x,y,0,0);}}
		  			  if(y+1<=7){mvtCavalier(x+2,y+1,genre,TourDeMvt);if(!TourDeMvt){CheminCheck(x,y,0,0);}}
		  }
		  if(y-2>=0) { if(x-1>=0){mvtCavalier(x-1,y-2,genre,TourDeMvt);if(!TourDeMvt){CheminCheck(x,y,0,0);}}  
			 		   if(x+1<=7){mvtCavalier(x+1,y-2,genre,TourDeMvt);if(!TourDeMvt){CheminCheck(x,y,0,0);}}
		  }
		  if(y+2<=7){ if(x-1>=0){mvtCavalier(x-1,y+2,genre,TourDeMvt);if(!TourDeMvt){CheminCheck(x,y,0,0);}}
		  			  if(x+1<=7){mvtCavalier(x+1,y+2,genre,TourDeMvt);if(!TourDeMvt){CheminCheck(x,y,0,0);}}
		  }
		  
	  }

	  public void verifierAttackPiont(int x,int y,boolean genre,boolean TourDeMvt,int PlusMoin){ 
		  JPanel  panel=null;Component p;int position,lignedebut=-1,lignefin=1;
		  if(x==0){lignedebut=1;}
		  else {if(x==7){lignefin=-2;}}
		  for(int i=lignedebut;i<=lignefin;i=i+2){
			 position=position(x+i,y+PlusMoin);
			 panel = (JPanel)chessBoard.getComponent(position);
			 p= getComponentAt(panel);
			 if(TourDeMvt){
				 if(!check){
				 if(p instanceof JLabel && verifierPieceBlanc(!genre,(JLabel) p)==1){panel.setBackground(beg);}
				 }
				 else{
					 if(p instanceof JLabel && verifierPieceBlanc(!genre,(JLabel) p)==1){
					  for(int l=0;l<9;l++){
						  if(position==DefenceRoi[l]){panel.setBackground(beg);PossibleDeDefendre=true;}
					  }
				  }
				 }
			 }
			 else{
				 if(p instanceof JLabel){
			  			Icon piece=((JLabel) p).getIcon();
			  			if(genre){ 
			  				if(piece.toString().equals(piece_noir[5])){check=true;CheminCheck(x,y,0,0);panel.setBackground(Color.yellow);Tour=genre;}
			  				else{
			  					for(int k=0;k<9;k++){
			  						if(position==PlacePossibleRoiNoir[k])
		  								{PlacePossibleRoiNoir[k]=-1;}}
			  				}
			  			}
			  			else{
			  				if(piece.toString().equals(piece_blanc[5])){check=true;CheminCheck(x,y,0,0);panel.setBackground(Color.yellow);Tour=genre;}
			  				else{
			  					for(int k=0;k<9;k++){
			  						if(position==PlacePossibleRoiBlanc[k])
		  								{PlacePossibleRoiBlanc[k]=-1;}}
			  				}
			  			
			  			}
			  			if(Tour!=genre){check=false;DejaPasser=false;}
			  		}
			  		else{
			  			if(genre){
			  					for(int k=0;k<9;k++){
			  						if(position==PlacePossibleRoiNoir[k])
			  							{PlacePossibleRoiNoir[k]=-1;}
			  						}
			  				}
			  			else{
			  					for(int k=0;k<9;k++){
			  						if(position==PlacePossibleRoiBlanc[k])
			  							{PlacePossibleRoiBlanc[k]=-1;}}
			  				}
			  			} 
			  }
		  
		  }	  
	  }
	  public void mvtPion(int x,int y,boolean TourDeMvt,int PlusMoin){
		  JPanel panel = null;Component p;
		  int pos=position(x,y),a;
		  if(PlusMoin==1){a=0;}
		  else a=40;
		  if(pos<16+a && pos>=8+a)
		  { verifierAttackPiont(x,y,genre,TourDeMvt,PlusMoin);
		  	if(TourDeMvt){
		  		panel = (JPanel)chessBoard.getComponent(pos+8*PlusMoin);
		  		p=getComponentAt(panel);
		  		if(p instanceof JLabel){}
		  		else{
		  			if(!check) panel.setBackground(beg);
		  			else{for(int l=0;l<9;l++){
						  if((pos+8*PlusMoin)==DefenceRoi[l]){panel.setBackground(beg);PossibleDeDefendre=true;}
					  }
		  			}
		  		}
		  			panel = (JPanel)chessBoard.getComponent(pos+16*PlusMoin);
		  			p=getComponentAt(panel);
		  			if(p instanceof JLabel){}
		  			else{
		  				if(!check) panel.setBackground(beg);
		  				else{for(int l=0;l<9;l++){
		  					if((pos+16*PlusMoin)==DefenceRoi[l]){panel.setBackground(beg);PossibleDeDefendre=true;}
		  				}
		  				}	
		  			}
		  	}
		  }
		  else{
			  if(y<7 && y>0){
				  verifierAttackPiont(x,y,genre,TourDeMvt,PlusMoin);
				  if(TourDeMvt){
					  panel = (JPanel)chessBoard.getComponent(pos+8*PlusMoin);
					  p=getComponentAt(panel);
					  if(p instanceof JLabel){}
					  else{
						  if(!check) panel.setBackground(beg);
			  			  else{for(int l=0;l<9;l++){
							  if((pos+8*PlusMoin)==DefenceRoi[l]){panel.setBackground(beg);PossibleDeDefendre=true;}
						  }
			  			}
					  }
			  		}
			  }
			  else{
				  if(!TourDeMvt)
					  ChangerPion(x,y,genre);
			  }
		  }
	  }
	  public void pion(int x,int y,boolean genre,boolean TourDeMvt){
		  if(genre){mvtPion(x,y,TourDeMvt,-1);}
		  else {mvtPion(x,y,TourDeMvt,1);}
	  }
	  public void roi(int x,int y,boolean genre,boolean TourDeMvt){
		  JPanel  panel;Component p;
		  int i,j,k=0;
		  int debutligne=-1,debutcolonne=-1;
		  int finligne=1,fincolonne=1;
		  boolean stop=true;
		  if(x==0){debutligne=0;}else{if(x==7){finligne=0;}}
		  if(y==0){debutcolonne=0;}else{if(y==7){fincolonne=0;}}
		  i=debutligne;j=debutcolonne;
		  while(i<=finligne){
			  j=debutcolonne;
			  while(j<=fincolonne){
				  stop=false;
				  panel = (JPanel)chessBoard.getComponent(position(x+i,y+j));
				  p= getComponentAt(panel);
				  if(TourDeMvt){
					  if(p instanceof JLabel && verifierPieceBlanc(genre,(JLabel) p)==1){stop=true;}		  
					  if(!stop && RecherchePosition(position(x+i,y+j),genre)==1)
					  { panel.setBackground(beg);}
					  j++;
				  }
				  else{
					  if(genre){
						  PlacePossibleRoiBlancComp[k]=position(x+i,y+j);
						  PlacePossibleRoiBlanc[k]=position(x+i,y+j);
						  if(p instanceof JLabel && verifierPieceBlanc(genre,(JLabel) p)==1){PlacePossibleRoiBlanc[k]=-1;}
						  k++;
						  }
					  else	{
						  PlacePossibleRoiNoirComp[k]=position(x+i,y+j);
						  PlacePossibleRoiNoir[k]=position(x+i,y+j);
						  if(p instanceof JLabel && verifierPieceBlanc(genre,(JLabel) p)==1){PlacePossibleRoiNoir[k]=-1;}
						  k++;
						  }
					  j++;
				  }
			  }
			  i++;
			}
		
		  
	  }

	  /**
	   * fonction qui limite les mouvements du roi chaque tour
	   * */
	  public void MvtImpossiblePourRoiParPiece(int x,int y,boolean genre){
		  String[] tab;
		  if(genre)	{tab=piece_blanc;}
		  else tab=piece_noir;
		  JPanel panel = (JPanel)chessBoard.getComponent(position(x,y));
		  Component p=getComponentAt(panel);
		  if(p instanceof JLabel && verifierPieceBlanc(genre,(JLabel) p)==1){
			  Icon piece=((JLabel)p).getIcon();
			  if(piece.toString().equals(tab[0])){ if(genre){if(y==0)ChangerPion(x,y,genre);verifierAttackPiont(x,y,genre,false,-1);}
			  										 else {if(y==7)ChangerPion(x,y,genre);verifierAttackPiont(x,y,genre,false,1);}
			  									}
			  else if(piece.toString().equals(tab[1])){fou(x,y,genre,false);}
			  else if(piece.toString().equals(tab[2])){cavalier(x,y,genre,false);}
			  else if(piece.toString().equals(tab[3])){tour(x,y,genre,false);}
			  else if(piece.toString().equals(tab[4])){reine(x,y,genre,false);}
			  else if(piece.toString().equals(tab[5])){roi(x,y,genre,false);RoiVsRoi(!genre);}
		  }
	  }
	  public void MvtImpossiblePourRoiTotal(boolean type){
		  for(int i=0;i<8;i++){
			  for(int j=0;j<8;j++){
				  MvtImpossiblePourRoiParPiece(i,j,!type);
			  }
		  }
		  for(int i=0;i<8;i++){
			  for(int j=0;j<8;j++){
				  MvtImpossiblePourRoiParPiece(i,j,type);
			  }
		  }
		 
	  }
	  
	  public void DefencePourRoiParPiece(int x,int y,boolean genre){
		  String[] tab;
		  if(!genre){tab=piece_blanc;}
		  else  {tab=piece_noir;}
		  
		  JPanel panel = (JPanel)chessBoard.getComponent(position(x,y));
		  Component p= getComponentAt(panel);
		  if(p instanceof JLabel && verifierPieceBlanc(!genre,(JLabel) p)==1){
			  Icon piece=((JLabel)p).getIcon();
			  if(piece.toString().equals(tab[0])){pion(x,y,!genre,true);decolorer(true);}
			  if(piece.toString().equals(tab[1])){fou(x,y,!genre,true);decolorer(true);}
			  if(piece.toString().equals(tab[2])){cavalier(x,y,!genre,true);decolorer(true);}
			  if(piece.toString().equals(tab[3])){tour(x,y,!genre,true);decolorer(true);}
			  if(piece.toString().equals(tab[4])){reine(x,y,!genre,true);decolorer(true);}
		  } 
	  }
	  public void DefencePourRoiTotal(boolean type){
		  for(int i=0;i<8;i++){
			  for(int j=0;j<8;j++){
				  DefencePourRoiParPiece(i,j,type);
			  }
		  }
		 }
	  public void CheminCheck(int x,int y,int a,int b){
		  if(check && !DejaPasser){
			  XChemin=a;YChemin=b;
			  XCauseCheck=x;
			  YCauseCheck=y;
			  DejaPasser=true;}
	  }
	  public void DefencePourRoi(boolean type){
		  String[] tab;int i;
		  if(type)	{tab=piece_blanc;}
		  else tab=piece_noir;
		  DefenceRoi[0]=position(XCauseCheck,YCauseCheck);
		  
		  JPanel panel;Component p;
		  for(i=1;i<9;i++){
			  panel = (JPanel)chessBoard.getComponent(position(XCauseCheck+(XChemin*i),YCauseCheck+(YChemin*i)));
			  p= getComponentAt(panel);
			  if(p instanceof JLabel){
				  Icon piece=((JLabel)p).getIcon();
				  if(piece.toString().equals(tab[5])){break;}
			  }
			  DefenceRoi[i]=position(XCauseCheck+(XChemin*i),YCauseCheck+(YChemin*i));
		  }
		  for(int k=i;k<9;k++){DefenceRoi[k]=-1;}
	  }
	  /**
	   * les trois fonctions suivantes permet de changer le roi et la tour selon des condition: 
	   * 1.le roi n'a jamais bouger dès le debut de le partie
	   * 2.aucune pièces n'existe entre le roi et l'une des tours
	   * 3.les cases entre le roi et la tour n'est point menacer pas une piece le l'adversere
	   * */
	  public void Rock(){
		  if(genre){
			  if(!PremierMvtDeRoiBlanc){
				  TestRock(61,62,63);
				  TestRock(59,58,56);	  
			  }
		  }
		 else{
			 if(!PremierMvtDeRoiNoir){
				 TestRock(5,6,7);
				 TestRock(3,2,0);
			 }
		 }
	  }
	  public void TestRock(int a,int b,int c){
		  int i;
		  JPanel panel = (JPanel)chessBoard.getComponent(a);
		  Component p=getComponentAt(panel);
		  for(i=0;i<9;i++){
			  if(a<32){if(PlacePossibleRoiNoir[i]==a)break;}
			  else{if(PlacePossibleRoiBlanc[i]==a){break;}}
		  }
		  if(p instanceof JLabel || i>=9){}
		  else{
			  panel = (JPanel)chessBoard.getComponent(b);
			  p=getComponentAt(panel);
			  if(p instanceof JLabel){}
			  else{
				  int roi;boolean type;
				  if(a<32){roi=4;type=true;}
				  else {roi=60;type=false;}
				  panel = (JPanel)chessBoard.getComponent(roi);
				  p=getComponentAt(panel);
				  JLabel piece=(JLabel)p;
				  piece.setVisible(false);
				  panel = (JPanel)chessBoard.getComponent(a);
				  panel.add(piece);
				  piece.setVisible(true);
				  
				  for(i=0;i<9;i++){
					  if(a<32){if(PlacePossibleRoiNoir[i]==a)break;}
					  else{if(PlacePossibleRoiBlanc[i]==a){break;}}
				  }
				  if(i<9){
					  panel = (JPanel)chessBoard.getComponent(c);
					  p=getComponentAt(panel);
					  if(p instanceof JLabel){
						  panel = (JPanel)chessBoard.getComponent(b);
						  panel.setBackground(beeg);}
				  }
				  piece.setVisible(false);
				  panel = (JPanel)chessBoard.getComponent(roi);
				  panel.add(piece);
				  piece.setVisible(true);
			  }
		  }
	  }
	  public void RockMvt(int a,int b){
		  JPanel pan;
		  Component p;
		  JLabel piece;
		  pan = (JPanel)chessBoard.getComponent(a);
		  p = getComponentAt(pan);
		  piece = (JLabel)p;
		  piece.setVisible(false);
		  pan = (JPanel)chessBoard.getComponent(b);
		  pan.add(piece);
		  piece.setVisible(true);
	  }
	 
	  /**
	   * la premier fonction c'est pour la fin du tour de joueur 
	   * la deuxieme c'est pour le tour de l'Ordinateur 
	   * 
	   * fonction qui écrit le mouvement dans la zone à gauche 
	   * enregistere le mouvement dans la liste des mouvements
	   * et fait appele a la fonction MvtImpossiblePourRoiTotal(boolean type) qui élémine les place impossible pour le roi selon le boolean type
	   * */
	  public void TraitementDeFinDuTour(MouseEvent me){
		  if(genre)if(chessPiece.getIcon().toString().equals(piece_blanc[5]))PremierMvtDeRoiBlanc=true;
		  else if(chessPiece.getIcon().toString().equals(piece_noir[5]))PremierMvtDeRoiNoir=true;
		  
		  u=ligne(me);v=colonne(me);
		  mvt=mvt+transf(u)+(v+1);
		  
		  edit.setText(edit.getText()+("\n"+mvt));
		  saveList(x,y,u,v,chessPiece,check);
		  
		  MvtImpossiblePourRoiTotal(genre);
		  if(check){CheckMat(genre);DejaPasser=false;}
		  chessPiece=null;chessPiecePrec=null;
		  genre=!genre;
		  if(!VsOrdi){
			  if(genre){
				  Noir.setBackground(Color.WHITE);
				  Blanc.setBackground(Color.BLACK);
			  }
			  else{
				  Blanc.setBackground(Color.WHITE);
				  Noir.setBackground(Color.BLACK);
			  }
		  } 
	  }
	  public void TraitementDeFinDuTour(int[] debutMvtSupSup,int[] finMvtSupSup){
		  
		  JPanel panel = (JPanel)chessBoard.getComponent(position(debutMvtSupSup[0],debutMvtSupSup[1]));
		  JLabel piece=(JLabel)getComponentAt(panel);
		  
		  mvt="";
		  mvt=mvt+transf(debutMvtSupSup[0])+(debutMvtSupSup[1]+1)+"-";
		  mvt=mvt+transf(finMvtSupSup[0])+(finMvtSupSup[1]+1);
		  edit.setText(edit.getText()+("\n"+mvt));
		  saveList(x,y,u,v,piece,check);
		  
		  MvtImpossiblePourRoiTotal(genre);
		  if(check){CheckMat(genre);DejaPasser=false;}
		  decolorer(false);
		  genre=!genre;
	  }
	  	  
}




	  
	  



