package Catan.CatanUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.*;
import java.awt.Choice;

public class Victim extends JFrame{
    private JPanel contentPane;
    private PlayerIG player;
	public String get;
    public String get2;
	Choice getChoice = new Choice();
    
    public Victim(PlayerIG player) {
        this.player = player;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 600, 190);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
        Game.disableAll();
        Game.PLAYBOARD.lancerDe.setEnabled(false);

		JLabel victimes = new JLabel("Victimes");
		victimes.setBounds(227, 27, 46, 14);
		contentPane.add(victimes);

        JButton confirmer = new JButton("Confirmer");
		confirmer.setBounds(247, 117, 89, 23);
		contentPane.add(confirmer);
		confirmer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
                get = getChoice.getSelectedItem();
                for(PlayerIG p : Game.PLAYERS){
                    if(p.name.equals(get)){
                        player.stealVictim = p;
                        break;
                    }
                }
                Game.PLAYBOARD.lancerDe.setEnabled(true);
                Game.enableAllExcept(Game.PLAYBOARD.annuler);
				stop();
			}
		});

		getChoice.setBounds(225, 47, 132, 20);
		contentPane.add(getChoice);


    }

	void stop(){
		this.dispose();
	}
}

