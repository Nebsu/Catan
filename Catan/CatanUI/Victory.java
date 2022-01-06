package Catan.CatanUI;

import java.awt.Font;


import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class Victory extends JFrame{
    JPanel contentPane;
    String winnername;
	JLabel winner = new JLabel(winnername);

    public Victory(){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 600, 190);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel congrats = new JLabel("F\u00E9licitation !");
		congrats.setHorizontalAlignment(SwingConstants.CENTER);
		congrats.setFont(new Font("Tahoma", Font.PLAIN, 30));
		congrats.setBounds(158, 25, 289, 47);
		contentPane.add(congrats);
		

		winner.setHorizontalAlignment(SwingConstants.CENTER);
		winner.setFont(new Font("Tahoma", Font.PLAIN, 25));
		winner.setBounds(193, 67, 200, 57);
		contentPane.add(winner);
    }
}
