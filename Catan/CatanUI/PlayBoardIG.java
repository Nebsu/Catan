package Catan.CatanUI;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.*;

public class PlayBoardIG extends JFrame{

	////////// Attributs ////////// 

	final BoxIG[][] boxes; // cases terrain
	LocationIG[][] locations; // intersections
	final SeaBoxIG[] seaBoxes; // cases maritimes
	PathIG[][] horizontalPaths; // chemins horizontaux
	PathIG[][] verticalPaths; // chemins verticaux
	BoxIG thief; // case contenant le voleur 
	JPanel contentPane = new JPanel();
    JButton passeTour = new JButton("<html><body>Passer<div>Tour</body></html>");
    JButton lancerDe = new JButton("<html><body>Lancer<div>De</body></html>");
    JLabel playername  = new JLabel();
    JLabel diceresult = new JLabel();
    JButton placerColonie = new JButton("<html><body>Placer<div>une<div>colonie</body></html>");
    JButton placerRoute = new JButton("<html><body>Placer<div>une<div>route</body></html>");

    ArrayList<JButton> boutonsJeu = new ArrayList<JButton>();
    JButton construireColonie = new JButton("Colonie");
    JButton route = new JButton("Route");
    JButton construireVille = new JButton("Ville");
    JButton annuler = new JButton("Annuler");
    //PopUp
    JButton echangePort = new JButton("Port");
    JButton cartes = new JButton("Cartes");

    JLabel qtArgile = new JLabel();
    JLabel qtBois = new JLabel();
    JLabel qtLaine = new JLabel();
    JLabel qtBle = new JLabel();
    JLabel qtRoche = new JLabel();

    ArrayList<PathIG> roads = new ArrayList<PathIG>();
    ArrayList<LocationIG> colonies = new ArrayList<LocationIG>();
	
	////////// Constructeur et fonctions associees à ce dernier //////////
    
    PlayBoardIG() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 600, 600);
		contentPane.setBorder(null);
		setContentPane(contentPane);
		contentPane.setLayout(null);
        passeTour.setBounds(475, 430, 85, 65);
        lancerDe.setBounds(475, 430, 85, 65);
        cartes.setBounds(10, 91, 70, 23);
        placerColonie.setBounds(475, 430, 85, 65);
        placerRoute.setBounds(475, 430, 85, 65);
        JPanel playerpanel = new JPanel();
        playerpanel.setBounds(10, 335, 115, 25);
        playerpanel.setBorder(new LineBorder(new Color(0, 0, 0)));
        playerpanel.add(playername);
        contentPane.add(playerpanel);
        contentPane.add(cartes);
        

        route.setBounds(470, 80, 90, 40);
		contentPane.add(route);
        route.setEnabled(false);
        construireColonie.setBounds(470, 135, 90, 40);
		contentPane.add(construireColonie);
        construireColonie.setEnabled(false);
        construireVille.setBounds(470, 195, 90, 40);
		contentPane.add(construireVille);
        construireVille.setEnabled(false);
        echangePort.setBounds(470, 249, 90, 40);
		contentPane.add(echangePort);
        echangePort.setEnabled(false);
        annuler.setBounds(470, 304, 90, 40);
		contentPane.add(annuler);
        annuler.setEnabled(false);

        boutonsJeu.add(route);
        boutonsJeu.add(construireColonie);
        boutonsJeu.add(construireVille);
        boutonsJeu.add(echangePort);
        boutonsJeu.add(cartes);
        boutonsJeu.add(annuler);

        JPanel invArgile = new JPanel();
		invArgile.setBackground(new Color(153, 102, 0));
		invArgile.setBounds(20, 400, 80, 125);
		contentPane.add(invArgile);
        invArgile.setLayout(null);
        JLabel labelArgile = new JLabel("Argile");
		invArgile.add(labelArgile);
        qtArgile.setFont(new Font("Tahoma", Font.PLAIN, 30));
		qtArgile.setHorizontalAlignment(SwingConstants.CENTER);
		qtArgile.setBounds(17, 24, 46, 73);
		invArgile.add(qtArgile);
		
		JPanel invBois = new JPanel();
		invBois.setBackground(new Color(0, 102, 51));
		invBois.setBounds(110, 400, 80, 125);
		contentPane.add(invBois);
        invBois.setLayout(null);
        JLabel labelBois = new JLabel("Bois");
		invBois.add(labelBois);
        qtBois.setFont(new Font("Tahoma", Font.PLAIN, 30));
		qtBois.setHorizontalAlignment(SwingConstants.CENTER);
		qtBois.setBounds(17, 24, 46, 73);
		invBois.add(qtBois);
		
		JPanel invLaine = new JPanel();
		invLaine.setBackground(new Color(102, 204, 51));
		invLaine.setBounds(200, 400, 80, 125);
		contentPane.add(invLaine);
        invLaine.setLayout(null);
        JLabel labelLaine = new JLabel("Laine");
		invLaine.add(labelLaine);
        qtLaine.setFont(new Font("Tahoma", Font.PLAIN, 30));
		qtLaine.setHorizontalAlignment(SwingConstants.CENTER);
		qtLaine.setBounds(17, 24, 46, 73);
		invLaine.add(qtLaine);
		
		JPanel invBle = new JPanel();
		invBle.setBackground(new Color(255, 255, 51));
		invBle.setBounds(290, 400, 80, 125);
		contentPane.add(invBle);
        invBle.setLayout(null);
        JLabel labelBle = new JLabel("Ble");
		invBle.add(labelBle);
        qtBle.setFont(new Font("Tahoma", Font.PLAIN, 30));
		qtBle.setHorizontalAlignment(SwingConstants.CENTER);
		qtBle.setBounds(17, 24, 46, 73);
		invBle.add(qtBle);
		
		JPanel invRoche = new JPanel();
		invRoche.setBackground(new Color(204, 204, 204));
		invRoche.setBounds(380, 400, 80, 125);
		contentPane.add(invRoche);
        invRoche.setLayout(null);
        JLabel labelRoche = new JLabel("Roche");
		invRoche.add(labelRoche);
        qtRoche.setFont(new Font("Tahoma", Font.PLAIN, 30));
		qtRoche.setHorizontalAlignment(SwingConstants.CENTER);
		qtRoche.setBounds(17, 24, 46, 73);
		invRoche.add(qtRoche);

        JPanel dicezone = new JPanel();
		dicezone.setBorder(new LineBorder(new Color(0, 0, 0)));
		dicezone.setBounds(10, 10, 70, 70);
		contentPane.add(dicezone);
		dicezone.setLayout(null);
		diceresult.setHorizontalAlignment(SwingConstants.CENTER);
		diceresult.setFont(new Font("Tahoma", Font.PLAIN, 30));
		diceresult.setBounds(10, 11, 46, 48);
		dicezone.add(diceresult);



        this.boxes = new BoxIG[4][4];
        this.constructBoxes();
        this.constructLocations();
        this.seaBoxes = new SeaBoxIG[16];
        this.constructSeaBoxes();
        this.constructPaths();
        this.thief = this.boxes[2][1];
    }

    private final void constructBoxes() {
        JPanel box11 = new JPanel();
		box11.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		box11.setBackground(new Color(0, 102, 51));
		box11.setBounds(145, 55, 50, 50);
		contentPane.add(box11);
        JLabel foret1 = new JLabel("<html><body>Foret<br>06</body></html>");
		box11.add(foret1);
		
		JPanel box12 = new JPanel();
		box12.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		box12.setBackground(new Color(102, 204, 51));
		box12.setBounds(215, 55, 50, 50);
		contentPane.add(box12);
        JLabel pre1 = new JLabel("<html><body>Pre<br>10</body></html>");
		box12.add(pre1);
		
		JPanel box13 = new JPanel();
		box13.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		box13.setBackground(new Color(255, 255, 51));
		box13.setBounds(285, 55, 50, 50);
		contentPane.add(box13);
        JLabel champ1 = new JLabel("<html><body>Champs<br>11</body></html>");
		box13.add(champ1);
		
		JPanel box14 = new JPanel();
		box14.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		box14.setBackground(new Color(102, 204, 51));
		box14.setBounds(355, 55, 50, 50);
		contentPane.add(box14);
        JLabel pre2 = new JLabel("<html><body>Pre<br>08</body></html>");
		box14.add(pre2);
		
		JPanel box21 = new JPanel();
		box21.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		box21.setBackground(new Color(255, 255, 51));
		box21.setBounds(145, 125, 50, 50);
		contentPane.add(box21);
        JLabel champ2 = new JLabel("<html><body>Champs<br>04</body></html>");
		box21.add(champ2);
		
		JPanel box22 = new JPanel();
		box22.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		box22.setBackground(new Color(153, 102, 0));
		box22.setBounds(215, 125, 50, 50);
		contentPane.add(box22);
        JLabel colline1 = new JLabel("<html><body>Colline<br>09</body></html>");
		box22.add(colline1);
		
		JPanel box23 = new JPanel();
		box23.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		box23.setBackground(new Color(0, 102, 51));
		box23.setBounds(285, 125, 50, 50);
		contentPane.add(box23);
        JLabel foret2 = new JLabel("<html><body>Foret<br>05</body></html>");
		box23.add(foret2);
		
		JPanel box24 = new JPanel();
		box24.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		box24.setBackground(new Color(204, 204, 204));
		box24.setBounds(355, 125, 50, 50);
		contentPane.add(box24);
        JLabel montagne1 = new JLabel("<html><body>Montagne<br>12</body></html>");
		box24.add(montagne1);
		
		JPanel box31 = new JPanel();
		box31.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		box31.setBackground(new Color(204, 204, 204));
		box31.setBounds(145, 195, 50, 50);
		contentPane.add(box31);
        JLabel montagne2 = new JLabel("<html><body>Montagne<br>03</body></html>");
		box31.add(montagne2);
		
		JPanel box32 = new JPanel();
		box32.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		box32.setBackground(new Color(255, 255, 153));
		box32.setBounds(215, 195, 50, 50);
		contentPane.add(box32);
        JLabel desert = new JLabel("<html><body>Desert<br>07</body></html>");
		box32.add(desert);
		
		JPanel box33 = new JPanel();
		box33.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		box33.setBackground(new Color(255, 255, 51));
		box33.setBounds(285, 195, 50, 50);
		contentPane.add(box33);
        JLabel champ3 = new JLabel("<html><body>Champs<br>10</body></html>");
		box33.add(champ3);
		
		JPanel box34 = new JPanel();
		box34.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		box34.setBackground(new Color(153, 102, 0));
		box34.setBounds(355, 195, 50, 50);
		contentPane.add(box34);
        JLabel colline2 = new JLabel("<html><body>Colline<br>06</body></html>");
		box34.add(colline2);
		
		JPanel box41 = new JPanel();
		box41.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		box41.setBackground(new Color(153, 102, 0));
		box41.setBounds(145, 265, 50, 50);
		contentPane.add(box41);
        JLabel colline3 = new JLabel("<html><body>Colline<br>09</body></html>");
		box41.add(colline3);
		
		JPanel box42 = new JPanel();
		box42.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		box42.setBackground(new Color(204, 204, 204));
		box42.setBounds(215, 265, 50, 50);
		contentPane.add(box42);
        JLabel montagne3 = new JLabel("<html><body>Montagne<br>08</body></html>");
		box42.add(montagne3);
		
		JPanel box43 = new JPanel();
		box43.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		box43.setBackground(new Color(102, 204, 51));
		box43.setBounds(285, 265, 50, 50);
		contentPane.add(box43);
        JLabel pre3 = new JLabel("<html><body>Pre<br>05</body></html>");
		box43.add(pre3);
		
		JPanel box44 = new JPanel();
		box44.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		box44.setBackground(new Color(0, 102, 51));
		box44.setBounds(355, 265, 50, 50);
		contentPane.add(box44);
        JLabel foret3 = new JLabel("<html><body>Foret<br>02</body></html>");
		box44.add(foret3);

        this.boxes[0][0] = new BoxIG("Foret", "Bois", 6, 0, 0, false, box11);
        this.boxes[0][1] = new BoxIG("Pre", "Laine", 10, 0, 1, false, box12);
        this.boxes[0][2] = new BoxIG("Champs", "Ble", 11, 0, 2, false, box13);
        this.boxes[0][3] = new BoxIG("Pre", "Laine", 8, 0, 3, false, box14);
        this.boxes[1][0] = new BoxIG("Champs", "Ble", 4, 1,0, false, box21);
        this.boxes[1][1] = new BoxIG("Colline", "Argile", 9, 1, 1, false, box22);
        this.boxes[1][2] = new BoxIG("Foret", "Bois", 5, 1, 2, false, box23);
        this.boxes[1][3] = new BoxIG("Montagne", "Roche", 12, 1, 3, false, box24);
        this.boxes[2][0] = new BoxIG("Montagne", "Roche", 3, 2, 0, false, box31);
        this.boxes[2][1] = new BoxIG("Desert", null, 7, 2, 1, true, box32);
        this.boxes[2][2] = new BoxIG("Champs", "Ble", 10, 2, 2, false, box33);
        this.boxes[2][3] = new BoxIG("Colline", "Argile", 6, 2, 3, false, box34);
        this.boxes[3][0] = new BoxIG("Colline", "Argile", 9, 3, 0, false, box41);
        this.boxes[3][1] = new BoxIG("Montagne", "Roche", 8, 3, 1, false, box42);
        this.boxes[3][2] = new BoxIG("Pre", "Laine", 5, 3, 2, false, box43);
        this.boxes[3][3] = new BoxIG("Foret", "Bois", 2, 3, 3, false, box44);
    }

	// Construction des intersections (nommees emplacements) :
    private final void constructLocations() throws IllegalArgumentException {
        this.locations = new LocationIG[5][5];
        int x = 200;
        int y = -30;
        for (int i=0; i<5; i++) {
            x = 200;
            y += 70;
            for (int j=0; j<5; j++) {
                // Coins du plateau (1 case adjacente) :
                if (i==0 && j==0 || i==0 && j==4
                ||  i==4 && j==0 || i==4 && j==4) {
                    int k = (i==4)? 3 : i; 
                    int l = (j==4)? 3 : j;
                    int m = (i==4)? 320 : 40;
                    int n = (j==4)? 410 : 130;
                    BoxIG[] b = {this.boxes[k][l]};
                    try {this.locations[i][j] = new LocationIG(b, i, j, n, m, this);
                        colonies.add(this.locations[i][j]);
                    }catch (IllegalArgumentException e) {
                        System.out.println("Erreur d'argument");
                        return;
                    }
                // Aretes horizontales du plateau (2 cases adjacentes) :
                } else if (i==0 && (j!=0 && j!=4) || i==4 && (j!=0 && j!=4)) {
                    int k = (i==4)? 3 : i;
                    int n = 0;
                    if(j==1)n = 200;
                    if(j==2)n = 270;
                    if(j==3)n = 340;
                    int m = (i==4)? 320 : 40;
                    BoxIG[] b = {this.boxes[k][j-1], this.boxes[k][j]};
                    this.locations[i][j] = new LocationIG(b, i, j, n, m, this);
                    colonies.add(this.locations[i][j]);
                    // JPanel c = new JPanel();
                    // c.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
                    // c.setBounds(n, m, 10, 10);
                    // contentPane.add(c);
                // Aretes verticales du plateau (2 cases adjacentes) :
                } else if (j==0 && (i!=0 && i!=4) || j==4 && (i!=0 && i!=4)) {
                    int l = (j==4)? 3 : j;
                    int m = 0;
                    if(i==1)m = 110;
                    if(i==2)m = 180;
                    if(i==3)m = 250;
                    int n = (j==4)? 410 : 130;
                    BoxIG[] b = {this.boxes[i-1][l], this.boxes[i][l]};
                    try {this.locations[i][j] = new LocationIG(b, i, j, n, m, this);
                        colonies.add(this.locations[i][j]);
                        // JPanel c = new JPanel();
                        // c.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
                        // c.setBounds(n, m, 10, 10);
                        // contentPane.add(c);
                    }catch (IllegalArgumentException e) {
                        System.out.println("Erreur d'argument");
                        return;
                    }
                // Centre du plateau (4 cases adjacentes) :
                } else {
                    BoxIG[] b = {this.boxes[i-1][j-1], this.boxes[i-1][j], this.boxes[i][j-1], this.boxes[i][j]};
                    try {this.locations[i][j] = new LocationIG(b, i, j, x, y, this);
                        colonies.add(this.locations[i][j]);
                        // JPanel c = new JPanel();
                        // c.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
                        // c.setBounds(x, y, 10, 10);
                        // contentPane.add(c);
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
        int n = 145;
        int m = -30;
        for (int i=0; i<5; i++) {
            n = 145;
            m += 70;
            for (int j=0; j<4; j++) {
                // JPanel hr = new JPanel();
                // hr.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
                // hr.setBounds(n, m, 50, 10);
                // contentPane.add(hr);
                this.horizontalPaths[i][j] = new PathIG('H', this.locations[i][j], this.locations[i][j+1], n, m, 50, 10, this);
                roads.add(this.horizontalPaths[i][j]);
                n += 70;
                if(j==4){
                    m = 40;
                }
            }
        }
        this.verticalPaths = new PathIG[4][5];
        int n2 = 130;
        int m2 = 55;
        for (int k=0; k<4; k++) {
            n2 = 130;
            for (int l=0; l<5; l++) {
                // JPanel vr = new JPanel();
                // vr.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
                // vr.setBounds(n2, m2, 10, 50);
                // contentPane.add(vr);
                this.verticalPaths[k][l] = new PathIG('V', this.locations[k][l], this.locations[k+1][l], n2, m2, 10, 50, this);
                roads.add(this.verticalPaths[k][l]);
                n2 += 70;
                if(l==4){
                    m2 += 70;
                }
            }
        }
    }

    private final void constructSeaBoxes() {
        JPanel seabox1 = new JPanel();
		seabox1.setBackground(new Color(0, 0, 102));
		seabox1.setBounds(145, 10, 50, 25);
		contentPane.add(seabox1);

        JPanel seabox2 = new JPanel();
		seabox2.setBackground(new Color(0, 102, 204));
		seabox2.setBounds(215, 10, 50, 25);
		contentPane.add(seabox2);
        JLabel laine = new JLabel("PLa");
		seabox2.add(laine);

        JPanel seabox3 = new JPanel();
		seabox3.setBackground(new Color(0, 0, 102));
		seabox3.setBounds(285, 10, 50, 25);
		contentPane.add(seabox3);

        JPanel seabox4 = new JPanel();
		seabox4.setBackground(new Color(0, 102, 204));
		seabox4.setBounds(355, 10, 50, 25);
		contentPane.add(seabox4);
        JLabel lblNewLabel_3 = new JLabel("PS");
		seabox4.add(lblNewLabel_3);

        JPanel seabox5 = new JPanel();
		seabox5.setBackground(new Color(0, 0, 102));
		seabox5.setBounds(425, 55, 25, 50);
		contentPane.add(seabox5);

        JPanel seabox6 = new JPanel();
		seabox6.setBackground(new Color(0, 102, 204));
		seabox6.setBounds(425, 125, 25, 50);
		contentPane.add(seabox6);
        JLabel bois = new JLabel("PBo");
		seabox6.add(bois);

        JPanel seabox7 = new JPanel();
		seabox7.setBackground(new Color(0, 0, 102));
		seabox7.setBounds(425, 195, 25, 50);
		contentPane.add(seabox7);

        JPanel seabox8 = new JPanel();
		seabox8.setBackground(new Color(0, 102, 204));
		seabox8.setBounds(425, 265, 25, 50);
		contentPane.add(seabox8);
        JLabel lblNewLabel_4 = new JLabel("PS");
		seabox8.add(lblNewLabel_4);

        JPanel seabox9 = new JPanel();
		seabox9.setBackground(new Color(0, 0, 102));
		seabox9.setBounds(355, 335, 50, 25);
		contentPane.add(seabox9);

        JPanel seabox10 = new JPanel();
		seabox10.setBackground(new Color(0, 102, 204));
		seabox10.setBounds(285, 335, 50, 25);
		contentPane.add(seabox10);
        JLabel argile = new JLabel("PAr");
		seabox10.add(argile);

        JPanel seabox11 = new JPanel();
		seabox11.setBackground(new Color(0, 0, 102));
		seabox11.setBounds(215, 335, 50, 25);
		contentPane.add(seabox11);

        JPanel seabox12 = new JPanel();
		seabox12.setBackground(new Color(0, 102, 204));
		seabox12.setBounds(145, 335, 50, 25);
		contentPane.add(seabox12);
        JLabel roche = new JLabel("PRo");
		seabox12.add(roche);

        JPanel seabox13 = new JPanel();
		seabox13.setBackground(new Color(0, 0, 102));
		seabox13.setBounds(100, 265, 25, 50);
		contentPane.add(seabox13);

        JPanel seabox14 = new JPanel();
		seabox14.setBackground(new Color(0, 102, 204));
		seabox14.setBounds(100, 195, 25, 50);
		contentPane.add(seabox14);
        JLabel ble = new JLabel("PBl");
		seabox14.add(ble);

        JPanel seabox15 = new JPanel();
		seabox15.setBackground(new Color(0, 0, 102));
		seabox15.setBounds(100, 125, 25, 50);
		contentPane.add(seabox15);

        JPanel seabox16 = new JPanel();
		seabox16.setBackground(new Color(0, 102, 204));
		seabox16.setBounds(100, 55, 25, 50);
		contentPane.add(seabox16);
        JLabel lblNewLabel_5 = new JLabel("PS");
		seabox16.add(lblNewLabel_5);

        this.seaBoxes[0] = new SeaBoxIG(this.locations[0][0], this.locations[0][1], seabox1);
        this.seaBoxes[1] = new HarborIG(this.locations[0][1], this.locations[0][2], 2, "Laine", seabox2);
        this.seaBoxes[2] = new SeaBoxIG(this.locations[0][2], this.locations[0][3], seabox3);
        this.seaBoxes[3] = new HarborIG(this.locations[0][3], this.locations[0][4], seabox4);
        this.seaBoxes[4] = new SeaBoxIG(this.locations[0][4], this.locations[1][4], seabox5);
        this.seaBoxes[5] = new HarborIG(this.locations[1][4], this.locations[2][4], 2, "Bois", seabox6);
        this.seaBoxes[6] = new SeaBoxIG(this.locations[2][4], this.locations[3][4], seabox7);
        this.seaBoxes[7] = new HarborIG(this.locations[3][4], this.locations[4][4], seabox8);
        this.seaBoxes[8] = new SeaBoxIG(this.locations[4][4], this.locations[4][3], seabox9);
        this.seaBoxes[9] = new HarborIG(this.locations[4][3], this.locations[4][2], 2, "Argile", seabox10);
        this.seaBoxes[10] = new SeaBoxIG(this.locations[4][2], this.locations[4][1], seabox11);
        this.seaBoxes[11] = new HarborIG(this.locations[4][1], this.locations[4][0], 2, "Roche", seabox12);
        this.seaBoxes[12] = new SeaBoxIG(this.locations[4][0], this.locations[3][0], seabox13);
        this.seaBoxes[13] = new HarborIG(this.locations[3][0], this.locations[2][0], 2, "Ble", seabox14);
        this.seaBoxes[14] = new SeaBoxIG(this.locations[2][0], this.locations[1][0], seabox15);
        this.seaBoxes[15] = new HarborIG(this.locations[1][0], this.locations[0][0], seabox16);
    }
 
    final void updatePaths() {
        for (int i=0; i<this.horizontalPaths.length; i++) {
            for (int j=0; j<this.horizontalPaths[i].length; j++) {
                this.horizontalPaths[i][j].point1 = this.locations[i][j];
                this.horizontalPaths[i][j].point2 = this.locations[i][j+1];
            }
        }
        for (int i=0; i<this.verticalPaths.length; i++) {
            for (int j=0; j<this.verticalPaths[i].length; j++) {
                this.verticalPaths[i][j].point1 = this.locations[i][j];
                this.verticalPaths[i][j].point2 = this.locations[i+1][j];
            }
        }
    }

    // Recuperation des cases designees par les des :
    final ArrayList<BoxIG> getBoxes(int dice) {
        ArrayList<BoxIG> res = new ArrayList<BoxIG>();
        for (int i=0; i<boxes.length; i++) {
            for (int j=0; j<boxes[i].length; j++) {
                if (this.boxes[i][j].number==dice) {
                    res.add(this.boxes[i][j]);
                }
            }
        }
        return res;
    }

    // Recuperation du port qui possède l'id passe en argument :
    final HarborIG getHarbor(int id) {
        for (int i=0; i<this.seaBoxes.length; i++) {
            if (this.seaBoxes[i] instanceof HarborIG) {
                if (((HarborIG) this.seaBoxes[i]).id==id) return ((HarborIG) this.seaBoxes[i]);
            }
        }
        return null;
    }


    ////////// Fonctions booleennes //////////

    // Renvoie true si toutes les intersections sont occupees par une colonie/ville :
    final boolean isFilledLocations() {
        int numberOfColonies = 0;
        for (int i=0; i<this.locations.length; i++) {
            for (int j=0; j<this.locations[i].length; j++) {
                if (this.locations[i][j] instanceof ColonyIG) {
                    numberOfColonies++;
                }
            }
        }
        return (numberOfColonies==25);
    }
    
    // Renvoie true si tous les chemins ont ete pris :
    final boolean isFilledPaths() {
        int numberOfRoads = 0;
        for (int i=0; i<this.horizontalPaths.length; i++) {
            for (int j=0; j<this.horizontalPaths[i].length; j++) {
                if (this.horizontalPaths[i][j] instanceof RoadIG) numberOfRoads++;
            }
        }
        for (int i=0; i<this.verticalPaths.length; i++) {
            for (int j=0; j<this.verticalPaths[i].length; j++) {
                if (this.verticalPaths[i][j] instanceof RoadIG) numberOfRoads++;
            }
        }
        return (numberOfRoads==40);
    }

}

