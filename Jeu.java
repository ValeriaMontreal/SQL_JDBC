/* TP3 de Valeriya Popenko
 *  # matricule 1014631*/

import java.util.ArrayList;
import java.util.Collection;

public class Jeu implements Comparable<Jeu> {
    private String fabricant;
    private String titre;
    private String cote;
    private Collection<String> console;

    public Jeu(String fabricant, String titre, String cote) {
        this.fabricant = fabricant;
        this.titre = titre;
        this.cote = cote;
        console = new ArrayList<String>();
    }

    public Jeu(String fabricant, String titre, String cote, Collection<String> console) {
        this.fabricant = fabricant;
        this.titre = titre;
        this.cote = cote;
        this.console = console;
    }

    public void addConsole(String uneConsole) {
        console.add(uneConsole);
    }

    public int compareTo(Jeu autre) {
        int result = fabricant.compareTo(autre.fabricant);
        if (result != 0)
            return result;
        else
            return titre.compareTo(autre.titre);
    }

    public String toSql() {
    	return "\"" + fabricant + "\", \"" + titre + "\", \"" + cote + "\", \"" + String.join(",", console) + "\"";
	}

    public String toString() {
        String res = "\n" + fabricant + "\t" + titre + "\t" + cote + "\n";
        for (int i = 0; i < titre.length(); i++)
            res += "=";
        res += "\n";
        for (String s : console)
            res += s + "\n";
        return res;
    }

    public int chercheConsole(String uneConsole) {
        return ((ArrayList<String>) console).indexOf(uneConsole);
    }

    public String getTitre() {
        return titre;
    }

    public String getFabricant() {
        return fabricant;
    }

    public String getCote() {
        return cote;
    }

    public Collection<String> getConsole() {
        return console;
    }

    public int hashCode() {
        return fabricant.hashCode() + titre.hashCode();
    }

    public boolean equals(Object o) {
        Jeu autre;
        if (o instanceof Jeu) {
            autre = (Jeu) o;
            return fabricant.equals(autre.fabricant) && titre.equals(autre.titre);
        } else
            return this == o;
    }

    public String pourSauvegarde() {
        String ch = fabricant + ";" + titre + ";" + cote + ";";
        for (String c : console)
            ch += c + ",";
        return ch.substring(0, ch.length() - 1);
    }
}


