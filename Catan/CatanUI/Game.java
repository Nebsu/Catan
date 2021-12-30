package Catan.CatanUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Choice;
import java.awt.Font;

import Catan.CatanTerminal.Deck;

public class Game extends JFrame implements ActionListener {

    ////////// Attributs //////////

    // Variables IG :
    static int botnumber;
    static String name;
    static int number;
    static int n = 0;
    static JPanel contentPane;
    static JTextField textField = new JTextField();
    static boolean visible = false;
    static Choice choice = new Choice();
    static Choice choice2 = new Choice();
    static JLabel lblNewLabel = new JLabel("Choisissez le nombre de joueur total");
    static JPanel panel = new JPanel();
    static JButton cbutton = new JButton("Confirmer");
    static int action = 0;
    static int acc = 0;

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
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 600, 600);
		contentPane = new JPanel();
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
                        choice2.add("1");
                        choice2.add("2");
                    }else{
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
                    PLAYERS[n] = new PlayerIG(name, n+1);
                    System.out.println("Joueur " + Integer.valueOf(n+1) + " = " + name);
                    textField.setText("");
                    n++;
                    acc++;
                    if(action == 2 && acc == number-botnumber){
                        lblNewLabel.setText("CATAN");
                        contentPane.remove(textField); 
                        cbutton.setText("Commencer le jeu");
                        cbutton.setBounds(140, 220, 300, 40);
                        // for(int i = number-botnumber; i < number; i++){
                        //     PLAYERS[i] = new PlayerIG("IA"+String.valueOf(i), i);
                        //     System.out.println(PLAYERS[i]);
                        // }
                    }
                    return;
                }
                if(action == 2 && acc == number-botnumber){
                    contentPane.remove(cbutton);
                    PLAYBOARD = new PlayBoardIG();
                    PLAYBOARD.setVisible(true);
                    stop();
                    return;
                }
            }
        });
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

    public static void main(String[] args) {
        catan();
    }

}