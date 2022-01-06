package Catan.CatanTerminal;

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
    static Player previousLongestRoad = null; 


    ////////// FONCTION PRINCIPALE DU JEU //////////

    public static final void catan() {
        ////////// PHASE INITIALE //////////

        PLAYBOARD.display(); // affichage du plateau vide
        // Début de la partie :
        // Placement des colonies et routes de base :
        for (int i=0; i<PLAYERS.length; i++) {
            PLAYERS[i].buildColony(true);
            PLAYERS[i].buildRoad(true, true);
            // buildRoad(true, true) = route construite sans frais pendant la phase initiale
            // buildRoad(true, false) = route construite sans frais pendant le jeu grâce à la carte progrès route
            // buildRoad(false, false) = route construite normalement pendant le jeu
            // buildRoad(false, true) = IMPOSSIBLE, les routes construites pendant la phase initiale sont forcément gratuites
        }
        for (int i=PLAYERS.length-1; i>=0; i--) {
            PLAYERS[i].buildColony(true);
            PLAYERS[i].buildRoad(true, true);
            // Le joueur gagne ces premières ressources (une unité par terrain adjacent à sa deuxième colonie) :
            PLAYERS[i].gainInitialResources();
        }
        // Route la plus longue au début :
        hasTheLongestRoad = longestRoad();
        if (hasTheLongestRoad!=null)
            System.out.println(hasTheLongestRoad.name+" a la route la plus longue\n");
        else 
            System.out.println("Aucun joueur a une route plus longue que les autres : Egalite\n");
        // Affichage des joueurs au commencement du jeu :
        for (int i=0; i<PLAYERS.length; i++) System.out.println(PLAYERS[i]);
        System.out.println();

        ////////// PHASE DU JEU //////////
        
        boolean end = false; // devient true quand un joueur a 10 points de victoire
        Player winner = null; // gagnant de la partie
        int turns = 0; // nombre de tours
        // Boucle du jeu :
        while (!end) {
            turns++;
            System.out.println("TOUR "+turns);
            for (int i=0; i<PLAYERS.length; i++) {
                // Tour du joueur :
                PLAYERS[i].play();
                System.out.println();
                if (PLAYERS[i].isWinner()) {
                    end = true;
                    winner = PLAYERS[i];
                    break;
                } 
                // Route la plus longue :
                hasTheLongestRoad = longestRoad();
                if (hasTheLongestRoad!=null)
                    System.out.println(hasTheLongestRoad.name+" a la route la plus longue\n");
                else 
                    System.out.println("Aucun joueur ne possede la route plus longue : Egalite\n");
                // Armée la plus puissante :
                System.out.println("Nombre de chevaliers joues :");
                for (int j=0; j<PLAYERS.length; j++) {
                    System.out.println(PLAYERS[j].name+" : "+PLAYERS[j].knights);
                }
                System.out.println();
                if (hasTheStrongestArmy!=null)
                    System.out.println(hasTheStrongestArmy.name+" possede l'armee la plus puissante");
                else 
                    System.out.println("Personne ne possede l'armee la plus puissante");
                if (!end) {
                    System.out.println();
                    for (int j=0; j<PLAYERS.length; j++) 
                        System.out.println(PLAYERS[j]);
                    System.out.println();
                }
            }
        }

        ////////// Fin du jeu //////////

        System.out.println("Felicitations "+winner.name+" ! Vous avez gagne la partie !");
        System.out.println("Classement :");
        System.out.println("1er : "+winner.name);
        ArrayList<Player> players = new ArrayList<Player>();
        for (int i=0; i<PLAYERS.length; i++) {
            if (PLAYERS[i]!=winner) players.add(PLAYERS[i]);
        }
        int index = 2;
        while (!players.isEmpty()) {
            Player best = null;
            int max = 0;
            for (Player play : players) {
                if (play.victoryPoints>max) {
                    max = play.victoryPoints;
                    best = play;
                } 
            }
            players.remove(best);
            System.out.println(index+"eme : "+best.name);
            index++;
        }
        System.out.println("\nNombre de tours total de la partie : "+turns+"\n");
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
            Scanner sc = new Scanner(System.in);
            try {
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
        Scanner sc = new Scanner(System.in);
        do {
            System.out.println("Tapez H ou I");
            try {
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
        if (c=='I') res = new IA("IA"+String.valueOf(i), i);
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
            } catch (InvalidNameException inv) {
                System.out.println(inv);
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
        boolean b = true;
        int[] sizes = new int[PLAYERS.length];
        System.out.println("\nRoute la plus longue de chaque joueur :");
        for (int i=0; i<PLAYERS.length; i++) {
            sizes[i] = PLAYERS[i].longestRoad();
            System.out.println(PLAYERS[i].name+" : "+sizes[i]);
        }
        System.out.println();
        int max = sizes[0];
        int index = 0;
        for (int i=1; i<sizes.length; i++) { 
            if (sizes[i]==max) b = false;
            if (sizes[i]>max) {
                max = sizes[i];
                index = i;
            }
        }
        if (b && previousLongestRoad!=PLAYERS[index]) {
            if (previousLongestRoad!=null) previousLongestRoad.victoryPoints -= 2;
            previousLongestRoad = PLAYERS[index];
            PLAYERS[index].victoryPoints += 2;
        }
        if(!b) return null;
        return PLAYERS[index];
    }

    public static int getMax(int[] t) {
        int max = t[0];
        for (int i=1; i<t.length; i++) {
            if (t[i]>max) max = t[i];
        }
        return max;
    }

}