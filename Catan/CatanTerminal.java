package Catan;

import java.util.*;
import Catan.Exceptions.*;

public final class CatanTerminal {

    ////////// Attributs //////////

    // Constantes couleurs pour des éléments du plateau dans le terminal :
    public static final String RESET = "\033[0m";
    public static final String WHITE_BOLD_BRIGHT = "\033[1;97m";
    public static final String BLACK_BOLD_BRIGHT = "\033[1;90m";
    public static final String RED_BOLD = "\033[1;31m";
    public static final String GREEN = "\033[0;32m"; 
    public static final String GREEN_BOLD_BRIGHT = "\033[1;92m";
    public static final String YELLOW_BOLD_BRIGHT = "\033[1;93m";
    public static final String CYAN_BOLD_BRIGHT = "\033[1;96m"; 
    // Constantes couleurs des joueurs (pions blanc, rouge, violet et bleu) :
    public static final String WHITE_BRIGHT = "\033[0;97m";
    public static final String BLUE_BRIGHT = "\033[0;94m";
    public static final String PURPLE_BRIGHT = "\033[0;95m";
    public static final String RED_BRIGHT = "\033[0;91m";

    // Données de base de la partie :
    static final Player[] PLAYERS = setPlayers(); // joueurs 
    static final PlayBoard PLAYBOARD = new PlayBoard(); // plateau
    static final Deck DECK = new Deck(); // pile de cartes développement 
    static boolean army = false; // devient true quand un joueur utilise 3 chevaliers
    static Player hasTheStrongestArmy = null; // joueur qui a l'armée la plus puissante
    static Player hasTheLongestRoad = null; // joueur qui a la route la plus longue


    ////////// FONCTION PRINCIPALE DU JEU //////////

    public static final void catan() {
        PLAYBOARD.display(); // affichage du plateau vide
        // Placement des colonies et routes de base :
        for (int k=0; k<2; k++) {
            for (int i=0; i<PLAYERS.length; i++) {
                PLAYERS[i].buildColony(true);
                PLAYERS[i].buildRoad(true);
            }
        }
        // Affichage des joueurs au commencement du jeu :
        for (int i=0; i<PLAYERS.length; i++) System.out.println(PLAYERS[i]);
        // Début de la partie :
        boolean end = false; // devient true quand un joueur a 10 points de victoire
        Player winner = null; // gagnant de la partie
        int turns = 0; // nombre de tours
        while (!end) {
            turns++;
            System.out.println("TOUR "+turns);
            for (int i=0; i<PLAYERS.length; i++) {
                PLAYERS[i].play();
                System.out.println();
                hasTheLongestRoad = longestRoad();
                if (PLAYERS[i].isWinner()) {
                    end = true;
                    winner = PLAYERS[i];
                    break;
                } 
            }
            System.out.println();
            for (int i=0; i<PLAYERS.length; i++) 
                System.out.println(PLAYERS[i]);
            System.out.println();
        }
        System.out.println("Félicitations "+winner.name+" ! Vous avez gagné la partie !");
        System.out.println("Nombre de tours total de la partie : "+turns);
    }


    ////////// Fonctions auxiliaires //////////

    // Créations des différents joueurs :
    private static final Player[] setPlayers() {
        // Nombre de joueurs :
        byte nbPlayers = requestNumPlayers();
        Player[] PLAYERS = new Player[nbPlayers];
        // Joueur 1 (joueur principal) (forcément un humain) :
        PLAYERS[0] = createRealPlayer(1);
        // Créations des autres joueurs (humains ou IAs) :
        for (int i=1; i<PLAYERS.length; i++) PLAYERS[i] = createPlayer(i+1);
        return PLAYERS;
    }
    // Demande du nombre de joueurs :
    private static final byte requestNumPlayers() {
        byte nb = 0; // type byte pour économiser de la mémoire
        System.out.println("Choisissez le nombre de joueurs (3 ou 4) :");
        do {
            System.out.println("Tapez 3 ou 4 :");
            try {
                Scanner sc = new Scanner(System.in);
                nb = sc.nextByte();
                if (nb!=3 && nb!=4) throw new WrongInputException();
            } catch (Exception e) {
                System.out.println(WrongInputException.MESSAGE);
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
            System.out.println("Tapez H ou I");
            try {
                Scanner sc = new Scanner(System.in);
                c = sc.nextLine().charAt(0);
                if (c!='H' && c!='I') throw new WrongInputException();
            } catch (Exception e) {
                System.out.println(WrongInputException.MESSAGE);
                // on met e pour que la boucle se répète :
                c = 'e';
            }
        } while (c!='H' && c!='I');
        Player res = null;
        // Si le joueur choisi humain, on crée un joueur humain:
        if (c=='H') res = createRealPlayer(i);
        // Sinon on crée un nouvel IA :
        if (c=='I') res = new IA("IA "+String.valueOf(i), i);
        return res;
    }  
    // Création d'un "vrai" joueur (humain) :
    private static final Player createRealPlayer(int i) {
        // On définit cette fonction, car chaque joueur humain doit entrer
        // son nom. Pour les IA, on les appelle automatiquement par exemple : 
        // IA2 , si l'IA est le joueur 2
        Scanner sc = new Scanner(System.in);
        String name = null;
        do {
            System.out.println("Joueur "+i+", entrez votre nom :");
            try {
                name = sc.nextLine();
                if (name.length()==0) throw new InvalidNameException();
            } catch (InvalidNameException exp) {
                System.out.println(exp);
                name = null;
            } catch (Exception e) {
                System.out.println(WrongInputException.MESSAGE);
                name = null;
            }
            // Se répète s'il y a une erreur de format ou si le joueur
            // n'a rien mis (le nom doit contenir au moins une lettre) :
        } while (name==null);
        return (new Player(name, i));
    }

    ////////// Route la plus longue //////////

    private static Player longestRoad() {
        int[] sizes = new int[PLAYERS.length];
        for (int i=0; i<PLAYERS.length; i++) {
            sizes[i] = PLAYERS[i].longestRoad();
        }
        int max = 0;
        int index = 0;
        for (int i=0; i<sizes.length; i++) {
            if (sizes[i]>max) {
                max = sizes[i];
                index = i;
            }
            if (sizes[i]==max) return null;
        }
        return PLAYERS[index];
    }

}