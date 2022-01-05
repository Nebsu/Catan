package Catan.CatanUI;

import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.util.Random;
import java.awt.*;
import java.awt.event.*;
import javax.swing.JButton.*;

import Catan.CatanTerminal.Deck;

public class Game extends JFrame implements ActionListener, MouseInputListener {

    ////////// Attributs //////////

    // Variables IG :
    static Color[] colors = new Color[4];
    static int botnumber;
    static String name;
    static int number;
    static int n = 0;
    static JPanel contentPane = new JPanel();
    static JTextField textField = new JTextField();
    static boolean visible = false;
    static Choice choice = new Choice();
    static Choice choice2 = new Choice();
    static JLabel lblNewLabel = new JLabel("Choisissez le nombre de joueur total");
    static JPanel panel = new JPanel();
    static JButton cbutton = new JButton("Confirmer");
    static int action = 0;
    static int acc = 0;
    static int player = 0;
    static int bot = 0;
    static boolean firstRound = true;
    static int firstRoundacc = 1;
    static boolean canConstructColony = true;
    static boolean canConstructRoad = true;

    // Données de base de la partie :
    static PlayerIG[] PLAYERS; // joueurs 
    static PlayBoardIG PLAYBOARD = new PlayBoardIG(); // plateau
    static Deck DECK = new Deck(); // pile de cartes développement 
    static boolean army = false; // devient true quand un joueur utilise 3 chevaliers
    static PlayerIG hasTheStrongestArmy = null; // joueur qui a l'armée la plus puissante
    static boolean longestRoad = false; // indique si un joueur possède la route la plus longue
    static PlayerIG hasTheLongestRoad = null; // joueur qui a la route la plus longue
    ////////// Constructeur et fonctions associées à ce dernier //////////

    Game(){
        colors[0] = new Color(0,0,255);
        colors[1] = new Color(0,255,0);
        colors[2] = new Color(255,0,0);
        colors[3] = new Color(255,255,0);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 600, 600);
		contentPane.setBorder(null);
		setContentPane(contentPane);
		contentPane.setLayout(null);

        //Choix du nombre de joueur
		panel.setBounds(125, 45, 340, 40);
		contentPane.add(panel);
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 21));
		panel.add(lblNewLabel);
		choice.setBounds(230, 200, 100, 20);
        choice.add("3");
        choice.add("4");
		contentPane.add(choice);
        cbutton.setBounds(230, 360, 100, 55);
        contentPane.add(cbutton);

        cbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {   
                if(action==0){
                    number = Integer.valueOf(choice.getSelectedItem());
                    PLAYERS = new PlayerIG[number];
                    System.out.println("Nombre de joueur : " + number);                   
                    lblNewLabel.setText("Veuillez choisir le nombre d'IA");  
                    contentPane.remove(choice);
                    choice2.setBounds(230, 200, 100, 20);
                    contentPane.add(choice2);
                    if(number == 3){
                        choice2.add("0");
                        choice2.add("1");
                        choice2.add("2");
                    }else{
                        choice2.add("0");
                        choice2.add("1");
                        choice2.add("2");
                        choice2.add("3");
                    }
                    action++;
                    return;
                }
                if(action == 1){
                    if(choice2.getSelectedItem()!=null){
                        botnumber = Integer.valueOf(choice2.getSelectedItem());
                        System.out.println("Nombre d'IA = " + botnumber);
                        lblNewLabel.setText("Veuillez choisir votre nom");
                        contentPane.remove(choice2);
                        textField.setBounds(140, 220, 300, 40);  
                        textField.setColumns(10);
                        contentPane.add(textField);
                        action++;
                        return;
                    }
                }
                if(action == 2 && acc < number-botnumber && !textField.getText().equals("")){
                    name = textField.getText();
                    PLAYERS[n] = new PlayerIG(name, n+1, colors[n]);
                    System.out.println("Joueur " + Integer.valueOf(n+1) + " = " + name);
                    textField.setText("");
                        n++;
                        acc++;
                    if(action == 2 && acc == number-botnumber){
                        lblNewLabel.setText("CATAN");
                        contentPane.remove(textField); 
                        cbutton.setText("Commencer le jeu");
                        cbutton.setBounds(140, 220, 300, 40);
                        for(int i = number-botnumber; i < number; i++){
                            PLAYERS[i] = new IAIG("IA"+String.valueOf(i+1), i, colors[i]);
                            System.out.println(PLAYERS[i]);
                        }
                    }
                    return;
                }
                if(action == 2 && acc == number-botnumber){
                    bot = number-botnumber;
                    PLAYBOARD.playername.setText(PLAYERS[player].name);
                    showInventory(PLAYERS[player]);
                    PLAYBOARD.contentPane.add(PLAYBOARD.placerColonie);
                    PLAYBOARD.setVisible(true);
                    stop();
                    return;
                }
            }
        });

        PLAYBOARD.placerColonie.addActionListener((event)-> {
            PLAYBOARD.construireColonie.setEnabled(true);
        });

        PLAYBOARD.placerRoute.addActionListener((event)-> {
            PLAYBOARD.route.setEnabled(true);
        });

        PLAYBOARD.lancerDe.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PLAYBOARD.contentPane.remove(PLAYBOARD.lancerDe);
                PLAYBOARD.contentPane.add(PLAYBOARD.passeTour);
                PLAYBOARD.diceresult.setText(String.valueOf(PLAYERS[player].throwDices()));
                enableAllExcept(PLAYBOARD.annuler);
                PlayerIG.earnResources(Integer.parseInt(PLAYBOARD.diceresult.getText()));
                showInventory(PLAYERS[player]);
                PLAYBOARD.repaint();
                return;
            }
        });

        PLAYBOARD.passeTour.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(player == number-botnumber-1){
                    player = 0;
                    PLAYBOARD.contentPane.remove(PLAYBOARD.passeTour);
                    PLAYBOARD.repaint();
                    while(bot!=number){
                        PLAYBOARD.playername.setText(PLAYERS[bot].name);
                        PLAYBOARD.diceresult.setText(String.valueOf(PLAYERS[bot].throwDices()));
                        PlayerIG.earnResources(Integer.parseInt(PLAYBOARD.diceresult.getText()));
                        System.out.println(PLAYBOARD.diceresult.getText());
                        bot++;
                    }
                    if(firstRound && botnumber !=0 && firstRoundacc == 1){
                        for(int i = number-botnumber; i < number; i++){
                            ((IAIG)PLAYERS[i]).buildColony(0, 0, true);
                            ((IAIG)PLAYERS[i]).buildRoad(' ',0,0,true,true);
                        }
                        for(int i = number-1; i > number-botnumber-1; i--) {
                            ((IAIG)PLAYERS[i]).buildColony(0, 0, true);
                            ((IAIG)PLAYERS[i]).buildRoad(' ',0,0,true,true);
                        }
                    }
                    bot = number-botnumber;
                    if(firstRoundacc==2){
                        for(PlayerIG p : PLAYERS){
                            p.gainInitialResources();
                        }
                    }
                    firstRoundacc++;
                    if(firstRoundacc>2){
                        firstRound = false;
                    }
                    if(firstRound == false){
                        PLAYBOARD.contentPane.add(PLAYBOARD.lancerDe);
                        PLAYBOARD.playername.setText(PLAYERS[player].name);
                        disableAll();
                        showInventory(PLAYERS[player]);
                        PLAYBOARD.repaint();
                        return;
                    }else{
                        PLAYBOARD.contentPane.add(PLAYBOARD.placerColonie);
                        return;
                    }                      
                }
                if(player == 0){
                    if(firstRound){
                        PLAYBOARD.contentPane.add(PLAYBOARD.placerColonie);
                    }
                    player++;
                    disableAll();
                    changePlayer(PLAYERS[player]);
                    showInventory(PLAYERS[player]);
                    PLAYBOARD.repaint();
                    return;
                }
                if(player == 1){      
                    if(firstRound){
                        PLAYBOARD.contentPane.add(PLAYBOARD.placerColonie);
                    }
                    player++;
                    disableAll();
                    changePlayer(PLAYERS[player]);
                    showInventory(PLAYERS[player]);
                    PLAYBOARD.repaint();
                    return;
                }
                if(player == 2){
                    if(firstRound){
                        PLAYBOARD.contentPane.add(PLAYBOARD.placerColonie);
                    }
                    player++;
                    disableAll();
                    changePlayer(PLAYERS[player]);
                    showInventory(PLAYERS[player]);
                    PLAYBOARD.repaint();
                    return;
                }
            }
        });

        PLAYBOARD.cartes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new PopUp(PLAYERS[player]).setVisible(true);
            }
        });

        PLAYBOARD.annuler.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enableAllExcept(PLAYBOARD.annuler);
                canConstructColony = false;
                canConstructRoad = false;               
            }
        });

        PLAYBOARD.route.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!firstRound){
                    disableAllExcept(PLAYBOARD.annuler);
                }
                canConstructRoad = true;
                for(int i = 0; i < PLAYBOARD.horizontalPaths.length; i++) {
                    int k = i;
                    for(int j = 0; j < PLAYBOARD.horizontalPaths[i].length; j++) {
                        int l = j;
                        PLAYBOARD.horizontalPaths[i][j].addMouseListener(new MouseAdapter(){
                            @Override
                            public void mousePressed(MouseEvent e) {
                                if(canConstructRoad == true && firstRound){
                                    if(PLAYERS[player].buildRoad('H',k,l,true,true)){
                                        canConstructRoad = false;
                                        PLAYBOARD.contentPane.remove(PLAYBOARD.placerRoute);
                                        PLAYBOARD.contentPane.add(PLAYBOARD.passeTour);
                                        disableAll();
                                    }
                                }else if(canConstructRoad == true && !firstRound){
                                    if(PLAYERS[player].buildRoad('H',k,l,false,false)){
                                        canConstructRoad = false;
                                        enableAllExcept(PLAYBOARD.annuler);
                                        showInventory(PLAYERS[player]);
                                    }    
                                }
                            }
                        });
                    }
                }

                for(int i = 0; i < PLAYBOARD.verticalPaths.length; i++) {
                    int k = i;
                    for(int j = 0; j < PLAYBOARD.verticalPaths[i].length; j++) {
                        int l = j;
                        PLAYBOARD.verticalPaths[i][j].addMouseListener(new MouseAdapter(){
                            @Override
                            public void mousePressed(MouseEvent e) {
                                if(canConstructRoad == true && firstRound){
                                    if(PLAYERS[player].buildRoad('V',k,l,true,true)){
                                        canConstructRoad = false;
                                        PLAYBOARD.contentPane.remove(PLAYBOARD.placerRoute);
                                        PLAYBOARD.contentPane.add(PLAYBOARD.passeTour);
                                        disableAll();
                                    }
                                }else if(canConstructRoad == true && !firstRound){
                                    if(PLAYERS[player].buildRoad('V',k,l,false,false)){
                                        canConstructRoad = false;
                                        enableAllExcept(PLAYBOARD.annuler);
                                        showInventory(PLAYERS[player]);
                                    }    
                                }
                            }
                        });
                    }
                }
            }
        });
        
        PLAYBOARD.construireColonie.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) { 
                if(!firstRound){
                    disableAllExcept(PLAYBOARD.annuler);
                }
                canConstructColony = true;
                for(int i = 0; i < PLAYBOARD.locations.length; i++) {
                    int k = i;
                    for(int j = 0; j < PLAYBOARD.locations[i].length; j++) {
                        int l = j;
                        if(canConstructColony){
                            PLAYBOARD.locations[i][j].addMouseListener(new MouseAdapter(){
                                @Override
                                public void mousePressed(MouseEvent e) {
                                    //Premier tour
                                    if(canConstructColony == true && firstRound){
                                        if(PLAYERS[player].buildColony(k,l,true)){
                                            canConstructColony = false;
                                            PLAYBOARD.contentPane.remove(PLAYBOARD.placerColonie);
                                            PLAYBOARD.contentPane.add(PLAYBOARD.placerRoute);
                                            disableAll();
                                        }
                                    }else if(canConstructColony == true && !firstRound){
                                        if(PLAYERS[player].buildColony(k,l,false)){
                                            canConstructColony = false;
                                        }
                                    }
                                }
                            });
                        }
                    }
                }
            }
        });

        PLAYBOARD.construireVille.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                disableAllExcept(PLAYBOARD.annuler);
            }
        });

        PLAYBOARD.echangePort.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                disableAllExcept(PLAYBOARD.annuler);
            }
        });
    }

    public static void showInventory(PlayerIG p){
        PLAYBOARD.qtArgile.setText(String.valueOf(p.inventory.get("Argile")));
        PLAYBOARD.qtBois.setText(String.valueOf(p.inventory.get("Bois")));
        PLAYBOARD.qtLaine.setText(String.valueOf(p.inventory.get("Laine")));
        PLAYBOARD.qtBle.setText(String.valueOf(p.inventory.get("Ble")));
        PLAYBOARD.qtRoche.setText(String.valueOf(p.inventory.get("Roche")));
    }

    public static void changePlayer(PlayerIG p){
        PLAYBOARD.playername.setText(p.name);
        PLAYBOARD.contentPane.remove(PLAYBOARD.passeTour);
        if(!firstRound){
            PLAYBOARD.contentPane.add(PLAYBOARD.lancerDe);
        }
    }

    public final static void catan() {
        Game frame = new Game();
        frame.setVisible(true);
    } 

    @Override
    public void actionPerformed(ActionEvent e) { 
    }

    public void stop(){
        this.dispose();
    }
    

    private static PlayerIG longestRoad(PlayerIG previous) {
        boolean b = true;
        int[] sizes = new int[PLAYERS.length];
        System.out.println();
        for (int i=0; i<PLAYERS.length; i++) {
            sizes[i] = PLAYERS[i].longestRoad();
            System.out.println(PLAYERS[i].name+" : "+sizes[i]);
        }
        System.out.println();
        int max = sizes[0];
        int index = 0;
        for (int i=1; i<sizes.length; i++) { 
            if (sizes[i]==max) b = false;
            if (sizes[i]>max) {
                max = sizes[i];
                index = i;
            }
        }
        if (previous!=PLAYERS[index] && b) PLAYERS[index].victoryPoints += 2;
        if (previous!=null && previous!=PLAYERS[index] && !b) 
            previous.victoryPoints -= 2;
        longestRoad = b;
        return PLAYERS[index];
    }

    public static int getMax(int[] t) {
        int max = t[0];
        for (int i=1; i<t.length; i++) {
            if (t[i]>max) max = t[i];
        }
        return max;
    }

    void disableAllExcept(JButton cancel){
        cancel.setEnabled(true);
        for(JButton b : PLAYBOARD.boutonsJeu){
            if(cancel != b){
                b.setEnabled(false);
            }
        }
    }

    void disableAll(){
        for(JButton b : PLAYBOARD.boutonsJeu){
            b.setEnabled(false);
        }
    }

    static void enableAllExcept(JButton cancel){
        cancel.setEnabled(false);
        for(JButton b : PLAYBOARD.boutonsJeu){
            if(cancel != b){
                b.setEnabled(true);
            }
        }
    }

    public static void main(String[] args) {
        catan();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }

}