package Catan.CatanUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.*;
import java.awt.Choice;

public class Monopole extends JFrame{
    private JPanel contentPane;
    private PlayerIG player;
	public String get;
	Choice getChoice = new Choice();
    
    public Monopole(PlayerIG player) {
        this.player = player;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 600, 190);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		Game.disableAll();
		Game.PLAYBOARD.lancerDe.setEnabled(false);

		JLabel prendre = new JLabel("Prendre");
		prendre.setBounds(274, 27, 46, 14);
		contentPane.add(prendre);

		JButton confirmer = new JButton("Confirmer");
		confirmer.setBounds(247, 117, 89, 23);
		confirmer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
                get = getChoice.getSelectedItem();
                Integer n = 0;
                for(PlayerIG p: Game.PLAYERS){
                    if(p!=player){
                        n += p.inventory.get(get);
                        p.inventory.replace(get, 0);
                    }
                }
                player.inventory.replace(get, player.inventory.get(get) + n);
                Game.showInventory(player);
				Game.PLAYBOARD.lancerDe.setEnabled(true);
				Game.enableAllExcept(Game.PLAYBOARD.annuler);
				stop();
			}
		});

		contentPane.add(confirmer);

		getChoice.setBounds(223, 47, 132, 20);
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
