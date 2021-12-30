package Catan.CatanUI;

import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.event.MouseInputListener;

class PathIG extends JPanel implements MouseInputListener{

    ////////// Attributs //////////
    
    protected final char position;
    protected LocationIG point1;
    protected LocationIG point2;
    protected JPanel panel;

    ////////// Constructeur et fonctions associées à ce dernier //////////

    PathIG(char position, LocationIG point1, LocationIG point2, JPanel panel) {
        if (position!='H' && position!='V') throw new IllegalArgumentException();
        this.position = position;
        this.point1 = point1;
        this.point2 = point2;
        this.panel = panel;
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