package Catan;

import java.util.*;
import Catan.Exceptions.*;

public class Tests {
    
    public final static void catanTerminal() {
        // Créations des différents joueurs :
        Player[] players = CatanTerminal.setPlayers();
        // Création du plateau :
        PlayBoard p = new PlayBoard();
        p.display(); // affichage du plateau vide
        // Placement des colonies de base :
        for (int i=0; i<players.length; i++) {
            players[i].buildColony(p);
            players[i].buildRoad(p);
            p.display();
        }
        for (int i=0; i<players.length; i++) {
            players[i].buildColony(p);
            players[i].buildRoad(p);
            p.display();
        }
        for (int i=0; i<players.length; i++) {
            System.out.println(players[i]);
        }
        p.display();
        for (int i=0; i<3; i++) {
            for (int j=0; j<players.length; j++) {
                players[j].play(p);
            }
        }
    }

}