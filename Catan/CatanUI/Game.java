package Catan.CatanUI;

import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.*;

import Catan.CatanTerminal.Deck;

public class Game extends JFrame implements ActionListener, MouseInputListener {

    ////////// Attributs //////////

    // Variables IG :
    static Color[] colors = new Color[4];
    static Color[] cityColors = new Color[4];
    static JPanel contentPane = new JPanel();
    static JTextField textField = new JTextField();
    static boolean visible = false;
    static Choice choice = new Choice();
    static Choice choice2 = new Choice();
    static JLabel lblNewLabel = new JLabel("Choisissez le nombre de joueur total");
    static JButton cbutton = new JButton("Confirmer");
    static PopUpEchange popupechange = new PopUpEchange(null);


    // Données de base de la partie :
    static PlayerIG[] PLAYERS; // joueurs 
    static PlayBoardIG PLAYBOARD = new PlayBoardIG(); // plateau
    static Deck DECK = new Deck(); // pile de cartes développement 
    static boolean army = false; // devient true quand un joueur utilise 3 chevaliers
    static PlayerIG hasTheStrongestArmy = null; // joueur qui a l'armée la plus puissante
    static boolean longestRoad = false; // indique si un joueur possède la route la plus longue
    static PlayerIG hasTheLongestRoad = null; // joueur qui a la route la plus longue
    static PlayerIG previousLongestRoad = null; 
    static boolean end = false;
    static PlayerIG winner = null;
    static int action = 0; // numero de l'action qui est joué
    static int acc = 0;
    static int player = 0; // numero du joueur
    static boolean firstRound = true;
    static int firstRoundacc = 1;
    static boolean canConstructColony = true;
    static boolean canConstructRoad = true;
    static boolean canConstructCity = false;
    static boolean harborEnable = false;
    static boolean roadCard = false;
    static boolean moveThief = false;
    static boolean echange41 = false;
    static int roadCardacc = 0; // accumulateur pour le nombre de route posé avec la carte route
    static int botnumber; // nombre de bot
    static int number; // nombre de joueur
    static String name; // variable pour récuperer le nom dans le textField
    static int n = 0;
    ////////// Constructeur et fonctions associées à ce dernier //////////

    Game(){
        //Couleur des joueurs
        colors[0] = new Color(70,70,255);
        colors[1] = new Color(70,255,70);
        colors[2] = new Color(255,70,70);
        colors[3] = new Color(255,255,70);
        //Couleur des villes des joueurs
        cityColors[0] = new Color(0,0,155);
        cityColors[1] = new Color(0,155,0);
        cityColors[2] = new Color(155,0,0);
        cityColors[3] = new Color(155,155,0);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 600, 600);
		contentPane.setBorder(null);
		setContentPane(contentPane);
		contentPane.setLayout(null);

        //Choix du nombre de joueur
        lblNewLabel.setBounds(118, 56, 344, 55);
        lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 21));
        contentPane.add(lblNewLabel);
		choice.setBounds(230, 200, 100, 20);
        choice.add("3");
        choice.add("4");
		contentPane.add(choice);
        cbutton.setBounds(230, 360, 100, 55);
        contentPane.add(cbutton);

        //Menu principal
        cbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {   
                if(action==0){
                    number = Integer.valueOf(choice.getSelectedItem());
                    PLAYERS = new PlayerIG[number];             
                    PLAYBOARD.add(PLAYBOARD.player1);
                    PLAYBOARD.add(PLAYBOARD.player2);  
                    PLAYBOARD.add(PLAYBOARD.player3);        
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
                        PLAYBOARD.add(PLAYBOARD.player4);
                    }
                    action++;
                    return;
                }
                if(action == 1){
                    if(choice2.getSelectedItem()!=null){
                        botnumber = Integer.valueOf(choice2.getSelectedItem());
                        lblNewLabel.setText("Veuillez choisir votre nom");
                        contentPane.remove(choice2);
                        textField.setBounds(140, 220, 300, 40);  
                        textField.setColumns(10);
                        textField.setBackground(new Color(240,240,240));
                        contentPane.add(textField);
                        action++;
                        return;
                    }
                }
                if(action == 2 && acc < number-botnumber && !textField.getText().equals("")){
                    name = textField.getText();
                    PLAYERS[n] = new PlayerIG(name, n+1, colors[n], cityColors[n]);
                    textField.setText("");
                        n++;
                        acc++;
                    if(action == 2 && acc == number-botnumber){
                        lblNewLabel.setText("CATAN");
                        contentPane.remove(textField); 
                        cbutton.setText("Commencer le jeu");
                        cbutton.setBounds(140, 220, 300, 40);
                        for(int i = number-botnumber; i < number; i++){
                            PLAYERS[i] = new IAIG("IA"+String.valueOf(i+1), i, colors[i], cityColors[i]);
                        }
                    }
                    return;
                }
                if(action == 2 && acc == number-botnumber){
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

        //Lancer de dé
        PLAYBOARD.lancerDe.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PLAYBOARD.contentPane.remove(PLAYBOARD.lancerDe);
                PLAYBOARD.contentPane.add(PLAYBOARD.passeTour);
                int dice = PLAYERS[player].throwDices();
                PLAYBOARD.diceresult.setText(String.valueOf(dice));
                if(dice == 7){
                    PLAYERS[player].thief();
                }
                enableAllExcept(PLAYBOARD.annuler);
                PlayerIG.earnResources(Integer.parseInt(PLAYBOARD.diceresult.getText()));
                showInventory(PLAYERS[player]);
                PLAYBOARD.repaint();
                return;
            }
        });

        //Système des tours
        PLAYBOARD.passeTour.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(player == number-botnumber-1){
                    player = 0;
                    PLAYBOARD.contentPane.remove(PLAYBOARD.passeTour);
                    PLAYBOARD.repaint();
                    for(int i = number-botnumber; i < number; i++){
                        if(!firstRound){
                            ((IAIG)PLAYERS[i]).play();
                            hasTheLongestRoad = longestRoad();
                        }
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
                        refreshVictoryPoints();
                    }
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
                        PLAYERS[player].specialCards.addAll(PLAYERS[player].notUsableCards);
                        PLAYERS[player].notUsableCards.clear();
                        if (PLAYERS[player].hasAVictoryPointCard() && PLAYERS[player].victoryPoints==9) PLAYERS[player].victoryPoints = 10;
                        for (int i=0; i<PLAYERS.length; i++) {
                            if (PLAYERS[i].isWinner()) {
                                end = true;
                                winner = PLAYERS[i];
                                break;
                            } 
                        }
                        if (PLAYERS[player].isWinner()) {
                            end = true;
                            winner = PLAYERS[player];
                        } 
                        if (hasTheStrongestArmy != null){
                            PLAYBOARD.knightPanellbl.setText(PLAYERS[player].name);
                        }
                        if(end){
                            stop();
                            Victory v = new Victory();
                            v.winnername = winner.name;
                            v.winner.setText(winner.name);
                            v.setVisible(true);
                        }
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
                    PLAYERS[player].specialCards.addAll(PLAYERS[player].notUsableCards);
                    PLAYERS[player].notUsableCards.clear();
                    if (PLAYERS[player].hasAVictoryPointCard() && PLAYERS[player].victoryPoints==9) PLAYERS[player].victoryPoints = 10;
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
                    PLAYERS[player].specialCards.addAll(PLAYERS[player].notUsableCards);
                    PLAYERS[player].notUsableCards.clear();
                    if (PLAYERS[player].hasAVictoryPointCard() && PLAYERS[player].victoryPoints==9) PLAYERS[player].victoryPoints = 10;
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
                    PLAYERS[player].specialCards.addAll(PLAYERS[player].notUsableCards);
                    PLAYERS[player].notUsableCards.clear();
                    PLAYBOARD.repaint();
                    return;
                }
            }
        });

        //Affichage fenêtre des cartes
        PLAYBOARD.cartes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame popup = new PopUp(PLAYERS[player]);
                popup.setVisible(true);
            }
        });

        //Bouton annuler
        PLAYBOARD.annuler.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enableAllExcept(PLAYBOARD.annuler);
                PLAYBOARD.passeTour.setEnabled(true);
                canConstructColony = false;
                canConstructRoad = false;               
            }
        });

        //Construction de route
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
                                        PLAYBOARD.passeTour.setEnabled(true);
                                        PLAYBOARD.contentPane.remove(PLAYBOARD.placerRoute);
                                        PLAYBOARD.contentPane.add(PLAYBOARD.passeTour);
                                        disableAll();
                                        return;
                                    }
                                }else if(roadCard == true){
                                    if(PLAYERS[player].buildRoad('H',k,l,true,false) && roadCardacc == 0){
                                        roadCardacc++;
                                        return;
                                    }
                                    if(PLAYERS[player].buildRoad('H',k,l,true,false) && roadCardacc == 1){
                                        roadCardacc = 0;
                                        roadCard = false;
                                        enableAllExcept(PLAYBOARD.annuler);
                                        PLAYBOARD.passeTour.setEnabled(true);
                                        return;
                                    }
                                }else if(canConstructRoad == true && !firstRound && roadCard == false){
                                    if(PLAYERS[player].buildRoad('H',k,l,false,false)){
                                        canConstructRoad = false;
                                        PLAYBOARD.passeTour.setEnabled(true);
                                        enableAllExcept(PLAYBOARD.annuler);
                                        showInventory(PLAYERS[player]);
                                        hasTheLongestRoad = longestRoad();
                                        return;
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
                                        PLAYBOARD.passeTour.setEnabled(true);
                                        disableAll();
                                        return;
                                    }
                                }else if(roadCard == true){
                                    if(PLAYERS[player].buildRoad('V',k,l,true,false) && roadCardacc == 0){
                                        roadCardacc++;
                                        return;
                                    }
                                    if(PLAYERS[player].buildRoad('V',k,l,true,false) && roadCardacc == 1){
                                        roadCardacc = 0;
                                        roadCard = false;
                                        enableAllExcept(PLAYBOARD.annuler);
                                        PLAYBOARD.passeTour.setEnabled(true);
                                        return;
                                    }
                                }else if(canConstructRoad == true && !firstRound && roadCard == false){
                                    if(PLAYERS[player].buildRoad('V',k,l,false,false)){
                                        canConstructRoad = false;
                                        enableAllExcept(PLAYBOARD.annuler);
                                        showInventory(PLAYERS[player]);
                                        PLAYBOARD.passeTour.setEnabled(true);
                                        hasTheLongestRoad = longestRoad();
                                        if(hasTheLongestRoad == null){
                                            PLAYBOARD.lrPlayer.setText(" ");
                                            refreshVictoryPoints();
                                        }else{
                                            PLAYBOARD.lrPlayer.setText(hasTheLongestRoad.name);
                                            refreshVictoryPoints();
                                        }
                                        return;
                                    }
                                }
                            }
                        });
                    }
                }
            }
        });
        
        //Construction de colonie
        PLAYBOARD.construireColonie.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) { 
                if(!firstRound){
                    disableAllExcept(PLAYBOARD.annuler);
                    PLAYBOARD.passeTour.setEnabled(false);
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
                                            refreshVictoryPoints();
                                        }
                                    }else if(canConstructColony == true && !firstRound){
                                        if(PLAYERS[player].buildColony(k,l,false)){
                                            canConstructColony = false;
                                            enableAllExcept(PLAYBOARD.annuler);
                                            PLAYBOARD.passeTour.setEnabled(true);
                                            showInventory(PLAYERS[player]);
                                            refreshVictoryPoints();
                                        }
                                    }
                                }
                            });
                        }
                    }
                }
            }
        });

        //Construction de ville
        PLAYBOARD.construireVille.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                canConstructCity = true;
                disableAllExcept(PLAYBOARD.annuler);
                PLAYBOARD.passeTour.setEnabled(false);
                for(int i = 0; i < PLAYBOARD.locations.length; i++) {
                    int k = i;
                    for(int j = 0; j < PLAYBOARD.locations[i].length; j++) {
                        int l = j;
                        PLAYBOARD.locations[i][j].addMouseListener(new MouseAdapter(){
                            @Override
                            public void mousePressed(MouseEvent e) {
                                if(canConstructCity){
                                    if(PLAYERS[player].buildCity(k,l)){
                                        enableAllExcept(PLAYBOARD.annuler);
                                        canConstructCity = false;
                                        PLAYBOARD.passeTour.setEnabled(true);
                                        showInventory(PLAYERS[player]);
                                        refreshVictoryPoints();
                                    }
                                }
                                
                            }
                        });
                    }
                }
            }
        });

        //Echange Port
        PLAYBOARD.echangePort.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                harborEnable = true;
                PLAYBOARD.passeTour.setEnabled(false);
                disableAllExcept(PLAYBOARD.annuler);
                popupechange = new PopUpEchange(PLAYERS[player]);
                for(int i = 0; i < PLAYBOARD.seaBoxes.length; i++){
                    int k = i;
                    if(PLAYBOARD.seaBoxes[i] instanceof HarborIG){
                        PLAYBOARD.seaBoxes[i].contentPane.addMouseListener(new MouseAdapter(){
                            @Override
                            public void mousePressed(MouseEvent e) {
                                if(harborEnable){
                                    int selectedId = ((HarborIG)PLAYBOARD.seaBoxes[k]).id;
                                    if(PLAYERS[player].useHarbor(selectedId)) {
                                        enableAllExcept(PLAYBOARD.annuler);
                                        showInventory(PLAYERS[player]);
                                        PLAYBOARD.passeTour.setEnabled(true);
                                        harborEnable = false;
                                        return;
                                    }else{
                                        harborEnable = false;
                                        enableAllExcept(PLAYBOARD.annuler);
                                        PLAYBOARD.passeTour.setEnabled(true);
                                        return;                               
                                    }
                                }
                            }
                        });
                    }
                }
            }
        });

        
        //Mouvement du voleur (ne marche pas)
        for(int i = 0; i < PLAYBOARD.boxes.length; i++){
            int k = i;
            for(int j = 0; j < PLAYBOARD.boxes[i].length; j++){
                int l = j;
                PLAYBOARD.boxes[i][j].addMouseListener(new MouseAdapter(){
                    @Override
                    public void mousePressed(MouseEvent e) {
                        if(moveThief){
                            PLAYERS[player].moveThief(PLAYBOARD.boxes[k][l].indexI, PLAYBOARD.boxes[k][l].indexJ);
                        }
                    }
                });
            }
        }

        //Echange 4 - 1
        PLAYBOARD.echange41.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                popupechange = new PopUpEchange(PLAYERS[player]);
                disableAllExcept(PLAYBOARD.annuler);
                PLAYERS[player].exchange(4, null);
            }
        });
        
        
    }

    //Met à jour l'affiche de l'inventaire
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

    public void stop(){
        this.dispose();
    }
    

    private static PlayerIG longestRoad(PlayerIG previous) {
        boolean b = true;
        int[] sizes = new int[PLAYERS.length];
        for (int i=0; i<PLAYERS.length; i++) {
            sizes[i] = PLAYERS[i].longestRoad();
            System.out.println(PLAYERS[i].name+" : "+sizes[i]);
        }
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

    public static void disableAll(){
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
    
    static void refreshVictoryPoints(){
        PLAYBOARD.victory1.setText(Integer.toString(PLAYERS[0].victoryPoints));
        PLAYBOARD.victory2.setText(Integer.toString(PLAYERS[1].victoryPoints));
        PLAYBOARD.victory3.setText(Integer.toString(PLAYERS[2].victoryPoints));
        if(number == 4){
            PLAYBOARD.victory4.setText(Integer.toString(PLAYERS[3].victoryPoints));
        }
    }

    private static PlayerIG longestRoad() {
        boolean b = true;
        int[] sizes = new int[PLAYERS.length];
        for (int i=0; i<PLAYERS.length; i++) {
            sizes[i] = PLAYERS[i].longestRoad();
        }
        int max = sizes[0];
        int index = 0;
        for (int i=1; i<sizes.length; i++) { 
            if (sizes[i]==max) b = false;
            if (sizes[i]>max) {
                max = sizes[i];
                index = i;
            }
        }
        if (b && previousLongestRoad!=PLAYERS[index]) {
            if (previousLongestRoad!=null) previousLongestRoad.victoryPoints -= 2;
            previousLongestRoad = PLAYERS[index];
            PLAYERS[index].victoryPoints += 2;
        }
        if(!b) return null;
        return PLAYERS[index];
    }

    @Override
    public void mouseClicked(MouseEvent e) {
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

    @Override
    public void mousePressed(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void actionPerformed(ActionEvent e) { 
    }

}