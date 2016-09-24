package model;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * cette class contient la construction de l'intelligence artificielle du zero jusqua'à la fin 
 *  
 * @author BOUGHTI Houssam/GAIDI Abdelali
 * 
 * */
public class IA implements Const{
	
	  private int [][] TabIA=new int[8][8];
	  
	  private int point=-1000;
	  private int[] debutMvt=new int[2],finMvt=new int[2];
	  private int pointSup=1000;
	  private int[] debutMvtSup=new int[2],finMvtSup=new int[2];
	  private int pointSupSup=-1000;
	  private int[] debutMvtSupSup=new int[2],finMvtSupSup=new int[2];
	  
	  private Boolean Prof1=true;
	  private JPanel chessBoard;
	  
	  private int[] PlacePossibleRoiBlanc;
	  private int[] PlacePossibleRoiNoir;
	  
	  public IA(JPanel chessBoard,int[] PlacePossibleRoiBlanc,int[] PlacePossibleRoiNoir) {
		this.chessBoard=chessBoard;
		this.PlacePossibleRoiBlanc=PlacePossibleRoiBlanc;
		this.PlacePossibleRoiNoir=PlacePossibleRoiNoir;
	}

	  public int[] getDebutMvt(){
		  return debutMvtSupSup;
	  }
	  public int[] getFinMvt(){
		  return finMvtSupSup;
	  }
	 
	  public void InitTabIA(Boolean type){
		  String[] tab1,tab2;
		  if(type) {tab1=piece_blanc;tab2=piece_noir;}
		  else {tab1=piece_noir;tab2=piece_blanc;}
		  for(int i=0;i<8;i++){
			  for(int j=0;j<8;j++){
			  JPanel panel = (JPanel)chessBoard.getComponent(position(i,j));
			  Component p= getComponentAt(panel);
			  if(p instanceof JLabel){
				  Icon piece=((JLabel)p).getIcon();
				  if(verifierPieceBlanc(type,(JLabel) p)==1){
					  if(piece.toString().equals(tab1[0])){ TabIA[i][j]=1;}
					  else if(piece.toString().equals(tab1[1])){ TabIA[i][j]=5;}
					  else if(piece.toString().equals(tab1[2])){ TabIA[i][j]=6;}
					  else if(piece.toString().equals(tab1[3])){ TabIA[i][j]=10;}
					  else if(piece.toString().equals(tab1[4])){ TabIA[i][j]=20;}
					  else if(piece.toString().equals(tab1[5])){ TabIA[i][j]=40;}
				  }
				  else{
					  if(piece.toString().equals(tab2[0])){ TabIA[i][j]=-1;}
					  else if(piece.toString().equals(tab2[1])){ TabIA[i][j]=-5;}
					  else if(piece.toString().equals(tab2[2])){ TabIA[i][j]=-6;}
					  else if(piece.toString().equals(tab2[3])){ TabIA[i][j]=-10;}
					  else if(piece.toString().equals(tab2[4])){ TabIA[i][j]=-20;}
					  else if(piece.toString().equals(tab2[5])){ TabIA[i][j]=0;}
					  
				  }
					  
			  }
			  else TabIA[i][j]=0;
		  }
		 }
	  }
	  public int MaterielScore(){
		  
		  int somme=0;
		  for(int i=0;i<8;i++)
			  for(int j=0;j<8;j++)
				  somme+=TabIA[i][j];
		  return somme;
	  }
	  public void ChangerSigneTabIA(){
		  for(int i=0;i<8;i++){
			  for(int j=0;j<8;j++){
				  TabIA[i][j]*=-1;
			  }  
		  }
	  }
	  
	  public Boolean mvtFouTourIA(int x,int y,int a,int b,Boolean max,Boolean PointDeCalcul){
		  boolean stop=false;
		  int som=MaterielScore()+(-1)*TabIA[a][b];
		  int coeff=1;
		  if(!max)coeff=-1;
		  
		  if(TabIA[a][b]>0){stop=true;}
		  
		  if(stop==false){
			  if(PointDeCalcul){
				  if(coeff*point<coeff*som){point=som;finMvt[0]=a;finMvt[1]=b;debutMvt[0]=x;debutMvt[1]=y;}
			  }
			  else{
				  Prondeur2Tree(x,y,a,b);
			  }
		  }
		  if(!stop && TabIA[a][b]<0){stop=true;}
		  return stop;
	  }
	  public void fouIA(int x,int y,Boolean max,Boolean PointDeCalcul){
		  boolean stop_gh=false; boolean stop_gb=false;boolean stop_dh=false;boolean stop_db=false;
		  for(int i=1;i<8;i++){
			  if(x-i>=0 && y-i>=0 && !stop_gh){stop_gh=mvtFouTourIA(x,y,x-i,y-i,max,PointDeCalcul);}
			  if(x-i>=0 && y+i<=7 && !stop_gb){stop_gb=mvtFouTourIA(x,y,x-i,y+i,max,PointDeCalcul);}
			  if(x+i<=7 && y-i>=0 && !stop_dh){stop_dh=mvtFouTourIA(x,y,x+i,y-i,max,PointDeCalcul);}
			  if(x+i<=7 && y+i<=7 && !stop_db){stop_db=mvtFouTourIA(x,y,x+i,y+i,max,PointDeCalcul);}  
		  }
	  }
	  public void tourIA(int x,int y,Boolean max,Boolean PointDeCalcul){
		  boolean stop_h=false; boolean stop_b=false;boolean stop_d=false;boolean stop_g=false;
		  for(int i=1;i<8;i++){
			  if(y-i>=0 && !stop_h){stop_h=mvtFouTourIA(x,y,x,y-i,max,PointDeCalcul);}
			  if(y+i<=7 && !stop_b){stop_b=mvtFouTourIA(x,y,x,y+i,max,PointDeCalcul);}
			  if(x+i<=7 && !stop_d){stop_d=mvtFouTourIA(x,y,x+i,y,max,PointDeCalcul);}
			  if(x-i>=0 && !stop_g){stop_g=mvtFouTourIA(x,y,x-i,y,max,PointDeCalcul);}
		  }  
	  }
	  public void reineIA(int x,int y,Boolean max,Boolean PointDeCalcul){
			 fouIA(x,y,max,PointDeCalcul);
			 tourIA(x,y,max,PointDeCalcul);
	}
	  
	  public void mvtCavalierIA(int x,int y,int a,int b,Boolean max,Boolean PointDeCalcul){
			 boolean stop=false;
			 int som=MaterielScore()+(-1)*TabIA[a][b];
			 int coeff=1;
			  if(!max)coeff=-1;
			 if(TabIA[a][b]>0){stop=true;}
		  	 if(stop==false) {
		  		 if(PointDeCalcul){
		  			 if(coeff*point<=coeff*som){point=som;finMvt[0]=a;finMvt[1]=b;debutMvt[0]=x;debutMvt[1]=y;}
		  		 }
		  		 else{
		  			Prondeur2Tree(x,y,a,b);
		  		 }
		  	}
		  }
	  public void cavalierIA(int x,int y,Boolean max,Boolean PointDeCalcul){
			  
			  if(x-2>=0){ if(y-1>=0){mvtCavalierIA(x,y,x-2,y-1,max,PointDeCalcul);}
					  	  if(y+1<=7){mvtCavalierIA(x,y,x-2,y+1,max,PointDeCalcul);}
			  }
			  if(x+2<=7){ if(y-1>=0){mvtCavalierIA(x,y,x+2,y-1,max,PointDeCalcul);}
			  			  if(y+1<=7){mvtCavalierIA(x,y,x+2,y+1,max,PointDeCalcul);}
			  }
			  if(y-2>=0) { if(x-1>=0){mvtCavalierIA(x,y,x-1,y-2,max,PointDeCalcul);}  
				 		   if(x+1<=7){mvtCavalierIA(x,y,x+1,y-2,max,PointDeCalcul);}
			  }
			  if(y+2<=7){ if(x-1>=0){mvtCavalierIA(x,y,x-1,y+2,max,PointDeCalcul);}
			  			  if(x+1<=7){mvtCavalierIA(x,y,x+1,y+2,max,PointDeCalcul);}
			  }
			  
		  }
	  
	  public void verifierAttackPiontIA(int x,int y,Boolean max,Boolean PointDeCalcul,int PlusMoin){ 
		  int som;
		  int lignedebut=-1,lignefin=1;
		  if(x==0){lignedebut=1;}
		  else {if(x==7){lignefin=-1;}}
		  int coeff=1;
		  if(!max)coeff=-1;
		  for(int i=lignedebut;i<=lignefin;i=i+2){
			  som=MaterielScore()+(-1)*TabIA[x+i][y+1*PlusMoin];
			  
			  if(TabIA[x+i][y+1*PlusMoin]<0){
				  if(PointDeCalcul){
					  if(coeff*point<=coeff*som){
						  point=som;finMvt[0]=x+i;finMvt[1]=y+1*PlusMoin;debutMvt[0]=x;debutMvt[1]=y;}
				  }
				  else{
					  Prondeur2Tree(x,y,x+i,y+1*PlusMoin);
				  }
			 }	
		  }
	  }
	  public void mvtPionIA(int x,int y,Boolean max,Boolean PointDeCalcul,int PlusMoin){
		  int pos=position(x,y);
		  int coeff=1;
		  if(!max)coeff=-1;
		  int a;
		  if(PlusMoin==1){a=0;}
		  else a=40;
		  
			  if(pos<16+a && pos>=8+a)
			  { verifierAttackPiontIA(x,y,max,PointDeCalcul,PlusMoin);
				  Boolean stop=true;
				  int som=MaterielScore()+(-1)*TabIA[x][y+1*PlusMoin];
				  if(TabIA[x][y+1*PlusMoin]==0){
					  if(PointDeCalcul){
					  		if(coeff*point<coeff*som){point=som;finMvt[0]=x;finMvt[1]=y+1*PlusMoin;debutMvt[0]=x;debutMvt[1]=y;}
					  		stop=false;
					  	}
					  else{
						  Prondeur2Tree(x,y,x,y+1*PlusMoin);
					  }
					 }
				  som=MaterielScore()+(-1)*TabIA[x][y+2*PlusMoin];
				  if(TabIA[x][y+2*PlusMoin]==0 && !stop){
					  
					  if(PointDeCalcul){
						  
						  if(coeff*point<=coeff*som){point=som;finMvt[0]=x;finMvt[1]=y+2*PlusMoin;debutMvt[0]=x;debutMvt[1]=y;}
					  }
					  else{
						  Prondeur2Tree(x,y,x,y+2*PlusMoin);
					  }
				  }
			  }
			  else{
				  verifierAttackPiontIA(x,y,max,PointDeCalcul,PlusMoin);
				  if(y<7 && y>0){
					  int som=MaterielScore()+(-1)*TabIA[x][y+1*PlusMoin];
					  if(TabIA[x][y+1*PlusMoin]==0)
						  if(PointDeCalcul){ 
							  if(coeff*point<coeff*som){point=som;finMvt[0]=x;finMvt[1]=y+1*PlusMoin;debutMvt[0]=x;debutMvt[1]=y;}
						  }
						  else{
							  Prondeur2Tree(x,y,x,y+1*PlusMoin);
						  }
					}
			  }
	  }
	  public void pionIA(int x,int y,Boolean max,Boolean PointDeCalcul,boolean type){
		  if(type){/*verifierAttackPiontIA(x,y,max,PointDeCalcul,-1);*/mvtPionIA(x,y,max,PointDeCalcul,-1);}
		  else {/*verifierAttackPiontIA(x,y,max,PointDeCalcul,1);*/mvtPionIA(x,y,max,PointDeCalcul,1);}
	  }

	  public void roiIA(int x,int y,Boolean max,Boolean PointDeCalcul){
		  
		  int coeff=1;
		  if(!max)coeff=-1;
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
				  int som=MaterielScore()+(-1)*TabIA[x+i][y+j];
					  if(TabIA[x+i][y+j]>0){stop=true;}		  
					  if(!stop && RecherchePosition(position(x+i,y+j),false)==1)
					  { 
						  if(PointDeCalcul){
							  if(coeff*point<coeff*som){point=som;finMvt[0]=x+i;finMvt[1]=y+j;debutMvt[0]=x;debutMvt[1]=y;}
						  }
						  else{
							  Prondeur2Tree(x,y,x+i,y+j);
						  }
					  }
					  j++;
			  }
			  i++;
			}
		
		  
	  }
	  
	  public void ExcuterMvtIA(){
		  JPanel panel = (JPanel)chessBoard.getComponent(position(debutMvtSupSup[0],debutMvtSupSup[1]));
		  Component	p= getComponentAt(panel);
		  JLabel piece = (JLabel)p;
		  piece.setVisible(false);
			
			panel= (JPanel)chessBoard.getComponent(position(finMvtSupSup[0],finMvtSupSup[1]));
			p= getComponentAt(panel);
			if(p instanceof JLabel){
				JLabel Npiece = (JLabel)p;
				panel.remove(Npiece);
			}
			panel.add(piece);
			piece.setVisible(true);
	  }
	  
	  public void Prondeur2Tree(int x,int y,int a,int b){
		  if(Prof1){
			  Prof1=false;
			  ChangerSigneTabIA();
			  int tmp1=TabIA[a][b];
			  TabIA[a][b]=TabIA[x][y];TabIA[x][y]=0;
			  pointSup=1000;
			  for(int i=0;i<8;i++){
				  for(int j=0;j<8;j++){
					  switch (TabIA[i][j]){
					  	case 1: pionIA(i,j,false,false,true);break;
					  	case 5: fouIA(i,j,false,false);break;
					  	case 6: cavalierIA(i,j,false,false);break;
					  	case 10: tourIA(i,j,false,false);break;
					  	case 20: reineIA(i,j,false,false);break;
					  	case 40: roiIA(i,j,false,false);break;
					  	default :break;
					  }
				  }
			  }
			  Prof1=true;
			  if(pointSupSup<pointSup){
			  	  pointSupSup=pointSup;
				  finMvtSupSup[0]=a;finMvtSupSup[1]=b;
				  debutMvtSupSup[0]=x;debutMvtSupSup[1]=y;
			  }
			  TabIA[x][y]=TabIA[a][b];
			  TabIA[a][b]=tmp1;
			  ChangerSigneTabIA();
		  }
		  else{
			  ChangerSigneTabIA();
			  int tmp2=TabIA[a][b];
			  TabIA[a][b]=TabIA[x][y];TabIA[x][y]=0;
			  point=-1000;
			  for(int i=0;i<8;i++){
				  for(int j=0;j<8;j++){
					  if(TabIA[i][j]==5) {fouIA(i,j,true,true);}
					  if(TabIA[i][j]==10) {tourIA(i,j,true,true);}
					  if(TabIA[i][j]==20) {reineIA(i,j,true,true);}
					  if(TabIA[i][j]==6) {cavalierIA(i,j,true,true);}
					  if(TabIA[i][j]==1) {pionIA(i,j,true,true,false);}
					  if(TabIA[i][j]==40) {roiIA(i,j,false,true);}
				  }
			  }
			  if(pointSup>point){
					 pointSup=point;
				 	 finMvtSup[0]=a;finMvtSup[1]=b;
				 	 debutMvtSup[0]=x;debutMvtSup[1]=y;
			  }
			  TabIA[x][y]=TabIA[a][b];
			  TabIA[a][b]=tmp2;
			  ChangerSigneTabIA();
			  
		  }
	  }
	  public void Prondeur1Tree(){
		  
		  pointSupSup=-1000;
		  
		  InitTabIA(false);
		  Prof1=true;
		  for(int i=0;i<8;i++){
			  for(int j=0;j<8;j++){
				  if(TabIA[i][j]==5) {fouIA(i,j,false,false);}
				  if(TabIA[i][j]==10) {tourIA(i,j,false,false);}
				  if(TabIA[i][j]==20) {reineIA(i,j,false,false);}
				  if(TabIA[i][j]==6) {cavalierIA(i,j,false,false);}
				  if(TabIA[i][j]==1) {pionIA(i,j,false,false,false);}
				  if(TabIA[i][j]==40) {roiIA(i,j,false,false);}
			  }
		  }
		  JPanel panel = (JPanel)chessBoard.getComponent(position(debutMvtSupSup[0],debutMvtSupSup[1]));
		  JLabel piece=(JLabel)getComponentAt(panel);

		  ExcuterMvtIA();
		  
	  }

	  public Component getComponentAt(Container parent) {
	        for (Component child : parent.getComponents()) {
	            if (child instanceof JLabel) {
	                return child;
	            }
	        }
	        return null;
	   }
	  public int position(int i,int j){
		  return i+8*j;
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

}
