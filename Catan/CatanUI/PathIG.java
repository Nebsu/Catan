package Catan.CatanUI;

import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;
import javax.swing.event.MouseInputListener;

class PathIG extends JPanel implements MouseInputListener{

    ////////// Attributs //////////
    
    protected final char position;
    protected LocationIG point1;
    protected LocationIG point2;
    protected int x;
    protected int y;
    protected PlayBoardIG playboard;
    protected int height;
    protected int width;

    ////////// Constructeur et fonctions associées à ce dernier //////////

    PathIG(char position, LocationIG point1, LocationIG point2, int x, int y, int height, int width, PlayBoardIG playboard) {
        if (position!='H' && position!='V') throw new IllegalArgumentException();
        this.position = position;
        this.point1 = point1;
        this.point2 = point2;
        this.x = x;
        this.y = y;
        this.height = height;
        this.width = width;
        this.playboard = playboard;
        this.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
        this.setBounds(x, y, height, width);
        playboard.add(this);
        Game.PLAYBOARD = playboard;
    }

    ////////// Fonctions auxiliaires //////////

    // Renvoie les chemins "voisins" (qui sont à côté) du chemin courant :
    protected static final ArrayList<PathIG> getNeighborsPaths(LocationIG point) {
        ArrayList<PathIG> neighbors = new ArrayList<PathIG>();
        // Route à gauche :
        try {
            neighbors.add(Game.PLAYBOARD.horizontalPaths[point.indexI][point.indexJ-1]);
        } catch (Exception e) {}
        // Route à droite :
        try {
            neighbors.add(Game.PLAYBOARD.horizontalPaths[point.indexI][point.indexJ]);
        } catch (Exception e) {}
        // Route en haut :
        try {
            neighbors.add(Game.PLAYBOARD.verticalPaths[point.indexI-1][point.indexJ]);
        } catch (Exception e) {}
        // Route en bas :
        try {
            neighbors.add(Game.PLAYBOARD.verticalPaths[point.indexI][point.indexJ]);
        } catch (Exception e) {}
        return neighbors;
    }

    @Override
    public void mouseClicked(MouseEvent e) {

        
    }

    @Override
    public void mousePressed(MouseEvent e) {
        System.out.println("Click");
        
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