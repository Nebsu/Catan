package Catan;

import java.util.*;
import Catan.Exceptions.*;

public final class CatanTerminal {

    final static Player[] players = setPlayers();
    final static PlayBoard p = new PlayBoard();
    protected static boolean army = false;

    public static final void catan() {
        p.display(); // affichage du plateau vide
        // Placement des colonies et routes de base :
        for (int k=0; k<2; k++) {
            for (int i=0; i<players.length; i++) {
                players[i].buildColony(true);
                players[i].buildRoad(true);
                p.display();
            }
        }
        // Affichage des joueurs et du plateau au commencement du jeu :
        for (int i=0; i<players.length; i++) System.out.println(players[i]);
        p.display();
        // Début de la partie :
        boolean end = false;
        Player winner = null; 
        int turns = 0;
        while (!end) {
            turns++;
            for (int i=0; i<players.length; i++) {
                players[i].play();
                for (int j=0; j<players.length; j++) 
                    System.out.println(players[j].name+" possède "+
                    players[j].getVictoryPoints()+" points de victoire");
                System.out.println();
                if (players[i].isWinner()) {
                    end = true;
                    winner = players[i];
                    break;
                } 
            }
        }
        System.out.println("Félicitations "+winner.name+" ! Vous avez gagné la partie !");
        System.out.println("Nombre de tours total de la partie : "+turns);
    }

    // Créations des différents joueurs :
    static final Player[] setPlayers() {
        // Nombre de joueurs :
        byte nbPlayers = requestNumPlayers();
        Player[] players = new Player[nbPlayers];
        // Joueur 1 (joueur principal) (forcément un humain) :
        players[0] = createRealPlayer(1);
        // Créations des autres joueurs (humains ou IAs) :
        for (int i=1; i<players.length; i++) players[i] = createPlayer(i+1);
        return players;
    }

    // Demande du nombre de joueurs :
    private static final byte requestNumPlayers() {
        byte nb = 0; // type byte pour économiser de la mémoire
        System.out.println("Choisissez le nombre de joueurs (3 ou 4) :");
        do {
            System.out.println("Tapez 3 ou 4 :");
            Scanner sc1 = new Scanner(System.in);
            try {
                nb = sc1.nextByte();
                if (nb!=3 && nb!=4) throw new WrongInputException();
            } catch (Exception e) {
                System.out.println(WrongInputException.message);
                // on met 1 pour que la boucle se répète :
                nb = 1;
            }
        } while (nb!=3 && nb!=4);
        return nb;
    }

    // Créations des joueurs :
    private static final Player createPlayer(int i) {
        System.out.println("Joueur "+i+" : Humain ou IA ?");
        char c;
        do {
            Scanner sc2 = new Scanner(System.in);
            System.out.println("Tapez h ou i");
            try {
                c = sc2.nextLine().charAt(0);
                if (c!='h' && c!='i') throw new WrongInputException();
            } catch (Exception e) {
                System.out.println(WrongInputException.message);
                // on met e pour que la boucle se répète :
                c = 'e';
            }
        } while (c!='h' && c!='i');
        Player res = null;
        // Si le joueur choisi humain, on crée un joueur humain:
        if (c=='h') res = createRealPlayer(i);
        // Sinon on crée un nouvel IA :
        if (c=='i') res = new IA("IA "+String.valueOf(i), i);
        return res;
    }  

    // Création d'un "vrai" joueur (humain) :
    private static final Player createRealPlayer(int i) {
        // On définit cette fonction, car chaque joueur humain doit entrer
        // son nom. Pour les IA, on les appelle automatiquement par exemple : 
        // IA2 , si l'IA est le joueur 2
        Scanner sc3 = new Scanner(System.in);
        String name = null;
        do {
            System.out.println("Joueur "+i+", entrez votre nom :");
            try {
                name = sc3.nextLine();
                if (name.length()==0) throw new InvalidNameException();
            } catch (InvalidNameException exp) {
                System.out.println(exp);
                name = null;
            } catch (Exception e) {
                System.out.println(WrongInputException.message);
                name = null;
            }
            // Se répète s'il y a une erreur de format ou si le joueur
            // n'a rien mis (le nom doit contenir au moins une lettre) :
        } while (name==null);
        return (new Player(name, i));
    }

}