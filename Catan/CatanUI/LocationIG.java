package Catan.CatanUI;

import java.awt.event.MouseEvent;

import javax.swing.JPanel;
import javax.swing.event.MouseInputListener;

class LocationIG extends JPanel implements MouseInputListener{

    ////////// Attributs ////////// 
    
    protected final BoxIG[] boxes;
    protected final int indexI;
    protected final int indexJ;

    ////////// Constructeur et fonctions associées à ce dernier //////////

    LocationIG(BoxIG[] boxes, int i, int j) {
        if (boxes.length<=0 || boxes.length==3 || boxes.length>4) 
            throw new IllegalArgumentException();
        // boxes.length peut seulement être égal à 1, 2 et 4 
        this.boxes = boxes;
        this.indexI = i;
        this.indexJ = j;
    }

    ////////// Fonctions auxiliaires //////////


    ////////// Fonctions des ports ////////// 

    // Renvoie true si l'intersection est à côté d'un port :
    protected boolean hasAnHarbor() {
        return (this.getHarbor()!=null);
    }

    // Si ce dernier existe, cette fonction renvoie le port en question :
    protected HarborIG getHarbor() {
        for (int i=0; i<Game.PLAYBOARD.seaBoxes.length; i++) {
            if (Game.PLAYBOARD.seaBoxes[i] instanceof HarborIG) {
                if (Game.PLAYBOARD.seaBoxes[i].loc1==this || 
                    Game.PLAYBOARD.seaBoxes[i].loc2==this) 
                    return ((HarborIG) Game.PLAYBOARD.seaBoxes[i]);
            }
        }
        return null;
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