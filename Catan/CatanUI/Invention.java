package Catan.CatanUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.*;
import java.awt.Choice;

public class Invention extends JFrame{
    private JPanel contentPane;
    private PlayerIG player;
	public String get;
    public String get2;
	Choice getChoice = new Choice();
    Choice getChoice2 = new Choice();
    
    public Invention(PlayerIG player) {
        this.player = player;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 600, 190);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
        Game.disableAll();
        Game.PLAYBOARD.lancerDe.setEnabled(false);

		JLabel ressource1 = new JLabel("Ressource 1");
		ressource1.setBounds(108, 27, 60, 14);
		contentPane.add(ressource1);

        JLabel ressource2 = new JLabel("Ressource 2");
		ressource2.setBounds(354, 27, 60, 14);
		contentPane.add(ressource2);

		JButton confirmer = new JButton("Confirmer");
		confirmer.setBounds(247, 117, 89, 23);
		confirmer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
                get = getChoice.getSelectedItem();
                get2 = getChoice2.getSelectedItem();
                player.inventory.replace(get, player.inventory.get(get) + 1);
                player.inventory.replace(get2, player.inventory.get(get) + 1);
                Game.showInventory(player);
                Game.PLAYBOARD.lancerDe.setEnabled(true);
                Game.enableAllExcept(Game.PLAYBOARD.annuler);
				stop();
			}
		});

		contentPane.add(confirmer);

		getChoice.setBounds(108, 47, 132, 20);
		contentPane.add(getChoice);
		getChoice.add("Ble");
		getChoice.add("Argile");
		getChoice.add("Laine");
		getChoice.add("Bois");
		getChoice.add("Roche");

        getChoice2.setBounds(354, 47, 132, 20);
		contentPane.add(getChoice2);
		getChoice2.add("Ble");
		getChoice2.add("Argile");
		getChoice2.add("Laine");
		getChoice2.add("Bois");
		getChoice2.add("Roche");
    }

	void stop(){
		this.dispose();
	}
}
