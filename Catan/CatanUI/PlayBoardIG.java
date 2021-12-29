package Catan.CatanUI;

import java.awt.Color;

import javax.swing.*;
import javax.swing.border.*;

public class PlayBoardIG extends JFrame{

	////////// Attributs ////////// 

	final BoxIG[][] boxes; // cases terrain
	LocationIG[][] locations; // intersections
	//final SeaBox[] seaBoxes; // cases maritimes
	PathIG[][] horizontalPaths; // chemins horizontaux
	PathIG[][] verticalPaths; // chemins verticaux
	BoxIG thief; // case contenant le voleur 
	JPanel contentPane;

	////////// Constructeur et fonctions associées à ce dernier //////////
    
    PlayBoardIG() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 600, 600);
		contentPane = new JPanel();
		contentPane.setBorder(null);
		setContentPane(contentPane);
		contentPane.setLayout(null);

        this.boxes = new BoxIG[4][4];
        this.constructBoxes();
        this.constructLocations();
        //this.seaBoxes = new SeaBox[16];
        //this.constructSeaBoxes();
        this.constructPaths();
        this.thief = this.boxes[2][1];
    }

    private final void constructBoxes() {
        JPanel box11 = new JPanel();
		box11.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		box11.setBackground(new Color(0, 102, 51));
		box11.setBounds(55, 55, 50, 50);
        contentPane.add(box11);
		
		JPanel box12 = new JPanel();
		box12.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		box12.setBackground(new Color(102, 204, 51));
		box12.setBounds(125, 55, 50, 50);
		contentPane.add(box12);
		
		JPanel box13 = new JPanel();
		box13.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		box13.setBackground(new Color(255, 255, 51));
		box13.setBounds(195, 55, 50, 50);
		contentPane.add(box13);
		
		JPanel box14 = new JPanel();
		box14.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		box14.setBackground(new Color(102, 204, 51));
		box14.setBounds(265, 55, 50, 50);
		contentPane.add(box14);
		
		JPanel box21 = new JPanel();
		box21.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		box21.setBackground(new Color(255, 255, 51));
		box21.setBounds(55, 125, 50, 50);
		contentPane.add(box21);
		
		JPanel box22 = new JPanel();
		box22.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		box22.setBackground(new Color(153, 102, 0));
		box22.setBounds(125, 125, 50, 50);
		contentPane.add(box22);
		
		JPanel box23 = new JPanel();
		box23.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		box23.setBackground(new Color(0, 102, 51));
		box23.setBounds(195, 125, 50, 50);
		contentPane.add(box23);
		
		JPanel box24 = new JPanel();
		box24.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		box24.setBackground(new Color(204, 204, 204));
		box24.setBounds(265, 125, 50, 50);
		contentPane.add(box24);
		
		JPanel box31 = new JPanel();
		box31.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		box31.setBackground(new Color(204, 204, 204));
		box31.setBounds(55, 195, 50, 50);
		contentPane.add(box31);
		
		JPanel box32 = new JPanel();
		box32.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		box32.setBackground(new Color(255, 255, 153));
		box32.setBounds(125, 195, 50, 50);
		contentPane.add(box32);
		
		JPanel box33 = new JPanel();
		box33.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		box33.setBackground(new Color(255, 255, 51));
		box33.setBounds(195, 195, 50, 50);
		contentPane.add(box33);
		
		JPanel box34 = new JPanel();
		box34.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		box34.setBackground(new Color(153, 102, 0));
		box34.setBounds(265, 195, 50, 50);
		contentPane.add(box34);
		
		JPanel box41 = new JPanel();
		box41.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		box41.setBackground(new Color(153, 102, 0));
		box41.setBounds(55, 265, 50, 50);
		contentPane.add(box41);
		
		JPanel box42 = new JPanel();
		box42.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		box42.setBackground(new Color(204, 204, 204));
		box42.setBounds(125, 265, 50, 50);
		contentPane.add(box42);
		
		JPanel box43 = new JPanel();
		box43.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		box43.setBackground(new Color(102, 204, 51));
		box43.setBounds(195, 265, 50, 50);
		contentPane.add(box43);
		
		JPanel box44 = new JPanel();
		box44.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		box44.setBackground(new Color(0, 102, 51));
		box44.setBounds(265, 265, 50, 50);
		contentPane.add(box44);

        this.boxes[0][0] = new BoxIG("Forêt", "Bois", 6, 0, 0, false, box11);
        this.boxes[0][1] = new BoxIG("Pré", "Laine", 10, 0, 1, false, box12);
        this.boxes[0][2] = new BoxIG("Champs", "Blé", 11, 0, 2, false, box13);
        this.boxes[0][3] = new BoxIG("Pré", "Laine", 8, 0, 3, false, box14);
        this.boxes[1][0] = new BoxIG("Champs", "Blé", 4, 1,0, false, box21);
        this.boxes[1][1] = new BoxIG("Colline", "Argile", 9, 1, 1, false, box22);
        this.boxes[1][2] = new BoxIG("Forêt", "Bois", 5, 1, 2, false, box23);
        this.boxes[1][3] = new BoxIG("Montagne", "Roche", 12, 1, 3, false, box24);
        this.boxes[2][0] = new BoxIG("Montagne", "Roche", 3, 2, 0, false, box31);
        this.boxes[2][1] = new BoxIG("Désert", null, 7, 2, 1, true, box32);
        this.boxes[2][2] = new BoxIG("Champs", "Blé", 10, 2, 2, false, box33);
        this.boxes[2][3] = new BoxIG("Colline", "Argile", 6, 2, 3, false, box34);
        this.boxes[3][0] = new BoxIG("Colline", "Argile", 9, 3, 0, false, box41);
        this.boxes[3][1] = new BoxIG("Montagne", "Roche", 8, 3, 1, false, box42);
        this.boxes[3][2] = new BoxIG("Pré", "Laine", 5, 3, 2, false, box43);
        this.boxes[3][3] = new BoxIG("Forêt", "Bois", 2, 3, 3, false, box44);
    }

	// Construction des intersections (nommées emplacements) :
    private final void constructLocations() throws IllegalArgumentException {
        this.locations = new LocationIG[5][5];
        int x = 110;
        int y = -30;
        for (int i=0; i<5; i++) {
            x = 110;
            y += 70;
            for (int j=0; j<5; j++) {
                // Coins du plateau (1 case adjacente) :
                if (i==0 && j==0 || i==0 && j==4
                ||  i==4 && j==0 || i==4 && j==4) {
                    int k = (i==4)? 3 : i; 
                    int l = (j==4)? 3 : j;
                    int m = (i==4)? 320 : 40;
                    int n = (j==4)? 40 : 320;
                    BoxIG[] b = {this.boxes[k][l]};
                    try {this.locations[i][j] = new LocationIG(b, i, j);
                        JPanel c = new JPanel();
                        c.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
                        c.setBounds(n, m, 10, 10);
                        contentPane.add(c);
                    }catch (IllegalArgumentException e) {
                        System.out.println("Erreur d'argument");
                        return;
                    }
                // Arêtes horizontales du plateau (2 cases adjacentes) :
                } else if (i==0 && (j!=0 && j!=4) || i==4 && (j!=0 && j!=4)) {
                    int k = (i==4)? 3 : i;
                    int n = 0;
                    if(j==1)n = 110;
                    if(j==2)n = 180;
                    if(j==3)n = 250;
                    int m = (i==4)? 320 : 40;
                    BoxIG[] b = {this.boxes[k][j-1], this.boxes[k][j]};
                    this.locations[i][j] = new LocationIG(b, i, j);
                    JPanel c = new JPanel();
                    c.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
                    c.setBounds(n, m, 10, 10);
                    contentPane.add(c);
                // Arêtes verticales du plateau (2 cases adjacentes) :
                } else if (j==0 && (i!=0 && i!=4) || j==4 && (i!=0 && i!=4)) {
                    int l = (j==4)? 3 : j;
                    int m = 0;
                    if(i==1)m = 110;
                    if(i==2)m = 180;
                    if(i==3)m = 250;
                    int n = (j==4)? 320 : 40;
                    BoxIG[] b = {this.boxes[i-1][l], this.boxes[i][l]};
                    try {this.locations[i][j] = new LocationIG(b, i, j);
                        JPanel c = new JPanel();
                        c.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
                        c.setBounds(n, m, 10, 10);
                        contentPane.add(c);
                    }catch (IllegalArgumentException e) {
                        System.out.println("Erreur d'argument");
                        return;
                    }
                // Centre du plateau (4 cases adjacentes) :
                } else {
                    BoxIG[] b = {this.boxes[i-1][j-1], this.boxes[i-1][j], this.boxes[i][j-1], this.boxes[i][j]};
                    try {this.locations[i][j] = new LocationIG(b, i, j);
                        JPanel c = new JPanel();
                        c.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
                        c.setBounds(x, y, 10, 10);
                        contentPane.add(c);
                        x += 70;
                    }catch (IllegalArgumentException e) {
                        System.out.println("Erreur d'argument");
                        return;
                    }
                }
            }
        }
    }

    private final void constructPaths() {
        this.horizontalPaths = new PathIG[5][4];
        int n = 55;
        int m = -30;
        for (int i=0; i<5; i++) {
            n = 55;
            m += 70;
            for (int j=0; j<4; j++) {
                JPanel hr = new JPanel();
                hr.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
                hr.setBounds(n, m, 50, 10);
                contentPane.add(hr);
                this.horizontalPaths[i][j] = new PathIG('H', this.locations[i][j], this.locations[i][j+1], hr);
                n += 70;
                if(j==4){
                    m = 40;
                }
            }
        }
        this.verticalPaths = new PathIG[4][5];
        int n2 = 40;
        int m2 = 55;
        for (int k=0; k<4; k++) {
            n2 = 40;
            for (int l=0; l<5; l++) {
                JPanel vr = new JPanel();
                vr.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
                vr.setBounds(n2, m2, 10, 50);
                contentPane.add(vr);
                this.verticalPaths[k][l] = new PathIG('V', this.locations[k][l], this.locations[k+1][l], vr);
                n2 += 70;
                if(l==4){
                    m2 += 70;
                }
            }
        }
    }
}
