package Catan.CatanUI;

import java.awt.event.*;

import javax.swing.JPanel;
import javax.swing.event.MouseInputListener;

class SeaBoxIG extends JPanel implements MouseInputListener{
 
    ////////// Attributs //////////

    protected final LocationIG loc1;
    protected final LocationIG loc2;
    PlayBoardIG playboard;
    JPanel contentPane = new JPanel();

    ////////// Constructeur et fonctions associées à ce dernier //////////

    SeaBoxIG(LocationIG loc1, LocationIG loc2,JPanel panel , PlayBoardIG playboard) {
        this.loc1 = loc1;
        this.loc2 = loc2;
        this.playboard = playboard;
        contentPane = panel;
        playboard.add(panel);
        Game.PLAYBOARD = this.playboard;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }


}