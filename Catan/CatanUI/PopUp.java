package Catan.CatanUI;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import Catan.CatanTerminal.Card;

import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;

public class PopUp extends JFrame {

	private JPanel contentPane;
    private PlayerIG player;

	public PopUp(PlayerIG player) {
        this.player = player;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 600, 190);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel chevalier = new JPanel();
		chevalier.setBackground(Color.LIGHT_GRAY);
		chevalier.setBounds(31, 11, 70, 90);
		contentPane.add(chevalier);
		chevalier.setLayout(null);
		
		JLabel lblCh = new JLabel("Chevalier");
		lblCh.setHorizontalAlignment(SwingConstants.CENTER);
		lblCh.setBounds(10, 11, 46, 14);
		chevalier.add(lblCh);
		
		JLabel qtCh = new JLabel(nbCh());
		qtCh.setHorizontalAlignment(SwingConstants.CENTER);
		qtCh.setFont(new Font("Tahoma", Font.PLAIN, 18));
		qtCh.setBounds(10, 30, 46, 49);
		chevalier.add(qtCh);
		
		JPanel victoire = new JPanel();
		victoire.setBackground(Color.LIGHT_GRAY);
		victoire.setBounds(143, 11, 70, 90);
		contentPane.add(victoire);
		victoire.setLayout(null);
		
		JLabel lblVic = new JLabel("Victoire");
		lblVic.setHorizontalAlignment(SwingConstants.CENTER);
		lblVic.setBounds(10, 11, 46, 14);
		victoire.add(lblVic);
		
		JLabel qtVic = new JLabel(nbVic());
		qtVic.setHorizontalAlignment(SwingConstants.CENTER);
		qtVic.setFont(new Font("Tahoma", Font.PLAIN, 18));
		qtVic.setBounds(10, 30, 46, 49);
		victoire.add(qtVic);
		
		JPanel route = new JPanel();
		route.setBackground(Color.LIGHT_GRAY);
		route.setBounds(258, 11, 70, 90);
		contentPane.add(route);
		route.setLayout(null);
		
		JLabel lblRt = new JLabel("Route");
		lblRt.setHorizontalAlignment(SwingConstants.CENTER);
		lblRt.setBounds(10, 11, 46, 14);
		route.add(lblRt);
		
		JLabel qtRt = new JLabel(nbRt());
		qtRt.setHorizontalAlignment(SwingConstants.CENTER);
		qtRt.setFont(new Font("Tahoma", Font.PLAIN, 18));
		qtRt.setBounds(10, 30, 46, 49);
		route.add(qtRt);
		
		JPanel invention = new JPanel();
		invention.setBackground(Color.LIGHT_GRAY);
		invention.setBounds(373, 11, 70, 90);
		contentPane.add(invention);
		invention.setLayout(null);
		
		JLabel lblInv = new JLabel("Invention");
		lblInv.setHorizontalAlignment(SwingConstants.CENTER);
		lblInv.setBounds(10, 11, 46, 14);
		invention.add(lblInv);
		
		JLabel qtInv = new JLabel(nbInv());
		qtInv.setHorizontalAlignment(SwingConstants.CENTER);
		qtInv.setFont(new Font("Tahoma", Font.PLAIN, 18));
		qtInv.setBounds(10, 30, 46, 49);
		invention.add(qtInv);
		
		JPanel monopole = new JPanel();
		monopole.setBackground(Color.LIGHT_GRAY);
		monopole.setBounds(482, 11, 70, 90);
		contentPane.add(monopole);
		monopole.setLayout(null);
		
		JLabel lblMon = new JLabel("Monopole");
		lblMon.setHorizontalAlignment(SwingConstants.CENTER);
		lblMon.setBounds(10, 11, 46, 14);
		monopole.add(lblMon);
		
		JLabel qtMon = new JLabel(nbMon());
		qtMon.setHorizontalAlignment(SwingConstants.CENTER);
		qtMon.setFont(new Font("Tahoma", Font.PLAIN, 18));
		qtMon.setBounds(10, 30, 46, 49);
		monopole.add(qtMon);
		
		JButton retour = new JButton("Retour");
		retour.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				stop();
			}
		});
		retour.setBounds(247, 117, 89, 23);
		contentPane.add(retour);
	}
	void stop() {
		this.dispose();
	}

    String nbCh(){
        int acc = 0;
        for(Card c : player.specialCards){
            if(c.cardname.equals("Chevalier")){
                acc++;
            }
        }
        return String.valueOf(acc);
    }
    String nbMon(){
        int acc = 0;
        for(Card c : player.specialCards){
            if(c.cardname.equals("Monopole")){
                acc++;
            }
        }
        return String.valueOf(acc);
    }
    String nbVic(){
        int acc = 0;
        for(Card c : player.specialCards){
            if(c.cardname.equals("Point de Victoire")){
                acc++;
            }
        }
        return String.valueOf(acc);
    }
    String nbRt(){
        int acc = 0;
        for(Card c : player.specialCards){
            if(c.cardname.equals("Construction de route")){
                acc++;
            }
        }
        return String.valueOf(acc);
    }
    String nbInv(){
        int acc = 0;
        for(Card c : player.specialCards){
            if(c.cardname.equals("Invention")){
                acc++;
            }
        }
        return String.valueOf(acc);
    }
}

