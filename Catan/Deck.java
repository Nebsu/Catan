package Catan;

import java.util.Collections;
import java.util.Stack;

class Deck {

    private Stack<Card> deck = new Stack<Card>();

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

    Deck() {
        for(int i = 0; i < 5; i++){
            deck.add(new Card("Point de Victoire", 0));
        }
        //Contruction de route (peut contruire 2 routes)
        deck.add(new Card("Construction de route", 2));
        deck.add(new Card("Construction de route", 2));
        //Invention (choisi 2 ressources de son choix)
        deck.add(new Card("Invention", 3));
        deck.add(new Card("Invention", 3));
        //Monopole (Tous les autres jours doivent lui donner 
        //toutes les ressources d'un type de son choix)
        deck.add(new Card("Monopole", 4));
        deck.add(new Card("Monopole", 4));
        for(int i = 0; i < 14; i++){
            deck.add(new Card("Chevalier", 1));
        }
        Collections.shuffle(deck);
    }

    public Stack<Card> getDeck() {
        return deck;
    }

}