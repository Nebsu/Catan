package Catan.CatanUI;

import java.util.Collections;
import java.util.Stack;

final class DeckIG {

    ////////// Attributs //////////

    private Stack<CardIG> deck = new Stack<CardIG>();

    /*
       5 cartes pts de victoire 
       6 cartes progres 
       14 cartes chevalier 

       0 = Point de Victoire
       1 = Chevalier
       2 = Construction de routes
       3 = Invention
       4 = Monopole
    */

    ////////// Constructeur et fonctions associées à ce dernier //////////

    DeckIG() {
        for(int i = 0; i < 5; i++)
            deck.add(new CardIG("Point de Victoire", 0));
        //Contruction de route (peut contruire 2 routes)
        deck.add(new CardIG("Construction de route", 2));
        deck.add(new CardIG("Construction de route", 2));
        //Invention (choisi 2 ressources de son choix)
        deck.add(new CardIG("Invention", 3));
        deck.add(new CardIG("Invention", 3));
        //Monopole (Tous les autres jours doivent lui donner 
        //toutes les ressources d'un type de son choix)
        deck.add(new CardIG("Monopole", 4));
        deck.add(new CardIG("Monopole", 4));
        for(int i = 0; i < 14; i++)
            deck.add(new CardIG("Chevalier", 1));
        Collections.shuffle(deck);
    }

    final Stack<CardIG> getDeck() {return deck;}

}