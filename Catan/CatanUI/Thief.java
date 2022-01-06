package Catan.CatanUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.*;
import java.awt.*;

public class Thief extends JFrame{
    public int total;
    public int given;
    public int acc;
    private JPanel contentPane;
    private PlayerIG player;
	public String get;
	Choice getChoice = new Choice();
    TextField t = new TextField();
    
    JLabel nombre = new JLabel("Nombre");
    JLabel ressource1 = new JLabel("Ressource");
    JLabel givenlbl = new JLabel("0");
    JLabel totallbl = new JLabel();
    
    public Thief(PlayerIG player) {
        this.player = player;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 600, 190);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
        Game.disableAll();
        Game.PLAYBOARD.lancerDe.setEnabled(false);

        JPanel panel = new JPanel();
		panel.setBounds(265, 27, 60, 53);
		contentPane.add(panel);
		panel.setLayout(null);
		

		givenlbl.setFont(new Font("Tahoma", Font.PLAIN, 15));
		givenlbl.setHorizontalAlignment(SwingConstants.CENTER);
		givenlbl.setBounds(10, 0, 40, 20);
		panel.add(givenlbl);
		

		totallbl.setFont(new Font("Tahoma", Font.PLAIN, 15));
		totallbl.setHorizontalAlignment(SwingConstants.CENTER);
		totallbl.setBounds(10, 31, 40, 20);
		panel.add(totallbl);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(10, 25, 40, 2);
		panel.add(separator);

		ressource1.setBounds(108, 27, 80, 14);
		contentPane.add(ressource1);

		nombre.setBounds(354, 27, 80, 14);
		contentPane.add(nombre);

		JButton confirmer = new JButton("Confirmer");
		confirmer.setBounds(247, 117, 89, 23);
		confirmer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println(total);
                get = getChoice.getSelectedItem();
                try {
                    given = Integer.parseInt(t.getText());
                } catch (Exception ex) {
                    return;
                }
				if(given <= player.inventory.get(get) && acc+given <= total){
					player.inventory.replace(get, player.inventory.get(get) - given);
					acc += given;
					given = 0;
					givenlbl.setText(String.valueOf(acc));
					repaint();
				}else{
					return;
				}
				Game.showInventory(player);
                if(acc == total){
                    Game.showInventory(player);
                    Game.PLAYBOARD.lancerDe.setEnabled(true);
                    Game.enableAllExcept(Game.PLAYBOARD.annuler);
                    stop();
                }
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

        t.setBounds(354, 47, 132, 20);
		contentPane.add(t);
    }

	void stop(){
		this.dispose();
	}
}
