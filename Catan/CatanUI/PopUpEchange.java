package Catan.CatanUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.*;
import java.awt.Choice;

public class PopUpEchange extends JFrame{
    private JPanel contentPane;
    private PlayerIG player;
	public String give;
	public String get;
	Choice getChoice = new Choice();
	Choice giveChoice = new Choice();
	int quantity = 0;
    
    public PopUpEchange(PlayerIG player) {
        this.player = player;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 600, 190);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel donner = new JLabel("Donner");
		donner.setBounds(103, 27, 46, 14);
		contentPane.add(donner);

		JLabel lblRecevoir = new JLabel("Recevoir");
		lblRecevoir.setBounds(354, 27, 46, 14);
		contentPane.add(lblRecevoir);

		JButton retour = new JButton("Retour");
		retour.setBounds(354, 117, 89, 23);
		retour.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				stop();
				Game.harborEnable = false;
			}
		});

		JButton confirmer = new JButton("Confirmer");
		confirmer.setBounds(143, 117, 89, 23);
		confirmer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				get = getChoice.getSelectedItem();
				give = giveChoice.getSelectedItem();
				player.inventory.replace(give, player.inventory.get(give)-quantity);
				player.inventory.replace(get, player.inventory.get(get)+1);
				player.harborExchange = false;
				Game.showInventory(player);
				stop();
			}
		});

		contentPane.add(confirmer);
		contentPane.add(retour);
		
		giveChoice.setBounds(100, 47, 132, 52);
		contentPane.add(giveChoice);

		getChoice.setBounds(354, 47, 132, 20);
		contentPane.add(getChoice);
		getChoice.add("Ble");
		getChoice.add("Argile");
		getChoice.add("Laine");
		getChoice.add("Bois");
		getChoice.add("Roche");
    }

	void stop(){
		this.dispose();
	}
}
