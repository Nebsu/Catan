package Catan.CatanUI;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import Catan.CatanTerminal.Card;

import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import java.awt.event.*;
import java.awt.Font;

public class PopUp extends JFrame {

	private JPanel contentPane;
    private PlayerIG player;
	JLabel qtCh = new JLabel();
	JLabel qtVic = new JLabel();
	JLabel qtRt = new JLabel();
	JLabel qtInv = new JLabel();
	JLabel qtMon = new JLabel();

	public PopUp(PlayerIG player) {
        this.player = player;
		//Initialisation des éléments du JFrame
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 600, 190);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		qtCh.setText(nbCh());
		qtVic.setText(nbVic());
		qtRt.setText(nbRt());
		qtInv.setText(nbInv());
		qtMon.setText(nbMon());
		
		JPanel chevalier = new JPanel();
		chevalier.setBackground(Color.LIGHT_GRAY);
		chevalier.setBounds(31, 11, 70, 90);
		contentPane.add(chevalier);
		chevalier.setLayout(null);
		
		JLabel lblCh = new JLabel("Chevalier");
		lblCh.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblCh.setHorizontalAlignment(SwingConstants.CENTER);
		lblCh.setBounds(10, 11, 46, 14);
		chevalier.add(lblCh);
		
		
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
		lblVic.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblVic.setHorizontalAlignment(SwingConstants.CENTER);
		lblVic.setBounds(10, 11, 46, 14);
		victoire.add(lblVic);
		
		
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
		lblRt.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblRt.setHorizontalAlignment(SwingConstants.CENTER);
		lblRt.setBounds(10, 11, 46, 14);
		route.add(lblRt);
		
		
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
		lblInv.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblInv.setHorizontalAlignment(SwingConstants.CENTER);
		lblInv.setBounds(10, 11, 46, 14);
		invention.add(lblInv);
		
		
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
		lblMon.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblMon.setHorizontalAlignment(SwingConstants.CENTER);
		lblMon.setBounds(10, 11, 46, 14);
		monopole.add(lblMon);
		
		
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
		retour.setBounds(354, 117, 89, 23);
		contentPane.add(retour);

		JButton acheterCarte = new JButton("Acheter");
		acheterCarte.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				player.buySpecialCard();
				Game.showInventory(player);
				refreshCard();
			}
		});
		acheterCarte.setBounds(143, 117, 89, 23);
		contentPane.add(acheterCarte);

		//Action sur chaque carte
		chevalier.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if(Integer.parseInt(nbCh())>0){
				player.useSpecialCard(1);
				refreshCard();
				}
			}
		});
		victoire.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if(Integer.parseInt(nbVic())>0){
				player.useSpecialCard(0);
				refreshCard();
				}
			}
		});
		route.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if(Integer.parseInt(nbRt())>0){
				stop();
				Game.PLAYBOARD.passeTour.setEnabled(false);
				player.useSpecialCard(2);
				refreshCard();
				}
			}
		});
		monopole.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if(Integer.parseInt(nbMon())>0){
					stop();
					Game.PLAYBOARD.passeTour.setEnabled(false);
					player.useSpecialCard(4);
					refreshCard();
					Game.showInventory(player);
				}
			}
		});
		invention.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if(Integer.parseInt(nbInv())>0){
					stop();
					Game.PLAYBOARD.passeTour.setEnabled(false);
					player.useSpecialCard(3);
					refreshCard();
					Game.showInventory(player);
				}
			}
		});
		
	}
	void stop() {
		this.dispose();
	}

	//Récupération du nombre de chaque ressource
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

	//Mise a jour de l'affichage
	void refreshCard(){
		qtInv.setText(nbInv());
		qtCh.setText(nbCh());
		qtRt.setText(nbRt());
		qtVic.setText(nbVic());
		qtMon.setText(nbMon());
		this.repaint();
	}
}

