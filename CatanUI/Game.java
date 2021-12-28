package CatanUI;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.awt.Choice;

public class Game extends JFrame implements ActionListener {
    //Variables IG
    static int botnumber;
    static String name;
    static int number;
    static int n = 0;
    static JPanel contentPane;
    static JTextField textField;
    static boolean visible = false;

    //
    static PlayerIG[] PLAYERS; // joueurs 
    //static PlayBoardIG PLAYBOARD = new PlayBoardIG(); // plateau
    static DeckIG DECK = new DeckIG(); // pile de cartes développement 
    static boolean army = false; // devient true quand un joueur utilise 3 chevaliers
    static PlayerIG hasTheStrongestArmy = null; // joueur qui a l'armée la plus puissante
    static PlayerIG hasTheLongestRoad = null; // joueur qui a la route la plus longue

    Game(){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 600, 600);
		contentPane = new JPanel();
		contentPane.setBorder(null);
		setContentPane(contentPane);
		contentPane.setLayout(null);
        
        //Choix du nombre de joueur
        Choice choice = new Choice();
		choice.setBounds(180, 180, 245, 20);
        choice.add("3");
        choice.add("4");
		contentPane.add(choice);
        JButton cbutton = new JButton("Submit");
        cbutton.setBounds(160, 180, 20, 20);
        contentPane.add(cbutton);
        cbutton.addActionListener(new ActionListener() {     
            @Override
            public void actionPerformed(ActionEvent e) {
                number = Integer.valueOf(choice.getSelectedItem());
                PLAYERS = new PlayerIG[number];
                System.out.println("Nombre de joueur : " + number);
                contentPane.remove(choice);
                contentPane.remove(cbutton);              
                repaint();

                //Demande le nombre d'IA
                Choice choice = new Choice();
                choice.setBounds(180, 180, 245, 20);
                if(number == 3){
                    choice.add("1");
                    choice.add("2");
                }else{
                    choice.add("1");
                    choice.add("2");
                    choice.add("3");
                }
                contentPane.add(choice);
                JButton cbutton = new JButton("Submit");
                cbutton.setBounds(160, 180, 20, 20);
                contentPane.add(cbutton);
                cbutton.addActionListener(new ActionListener() {     
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        botnumber = Integer.valueOf(choice.getSelectedItem());
                        System.out.println("Nombre d'IA = " + botnumber);
                        contentPane.remove(choice);
                        contentPane.remove(cbutton);  
                        repaint();
                        //Choix du nom des vrais joueurs
                        textField = new JTextField();
                        textField.setBounds(180, 180, 300, 40);                
                        JButton button = new JButton("Submit");
                        button.setBounds(140, 180, 40, 40);
                        textField.setColumns(10);
                        contentPane.add(button);
                        contentPane.add(textField);
                        button.addActionListener(new ActionListener() {     
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                name = textField.getText();                          
                                PLAYERS[n] = new PlayerIG(name, n+1);
                                n++;
                                System.out.println("Joueur " + n + " = " + name);
                                textField.setText("");
                                if(n==number-botnumber){
                                    //Ajoute les IA
                                    for(int i = number; i < botnumber; i++){
                                        PLAYERS[i] = new PlayerIG("IA"+String.valueOf(i), i);
                                    }
                                    contentPane.remove(button);
                                    contentPane.remove(textField);      
                                    repaint(); 
                                }                               
                        }});
                }});
        }});   
    }
    @Override
    public void actionPerformed(ActionEvent e) {   
    }
}
