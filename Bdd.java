/* TP3 de Valeriya Popenko
 * # matricule 1014631*/

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

import java.io.*;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;


public class Bdd {

    private Map<String, Collection<Jeu>> laBase;
    private Connection connection;

    public Bdd() {
        try{

            Class.forName(UserData.JDBC_DRIVER);
            connection = (Connection) DriverManager.getConnection(UserData.DB_URL,UserData.USER,UserData.PASS);
        } catch(Exception se){

            se.printStackTrace();
        }
        laBase = new LinkedHashMap<>();
    }

    public void addJeu(Jeu unJeu) {
        try {
            Statement stmt = (Statement) connection.createStatement();
            String sql = "INSERT INTO Jeu(fabricant, titre, cote, console) VALUES(" + unJeu.toSql() + ")";

            stmt.executeUpdate(sql);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    
  public ArrayList<Jeu> getAll() {
    	  ArrayList<Jeu> jeuList = new ArrayList<>();

    	  try {
    	      Statement stmt = (Statement) connection.createStatement();
    	      String sql = "SELECT * FROM Jeu ";
    	      ResultSet rs = stmt.executeQuery(sql);

    	      while(rs.next()) {
    	          String foundFabricant  = rs.getString("fabricant");
    	          String foundNom = rs.getString("titre");
    	          String foundCote = rs.getString("cote");
    	          Collection<String> foundConsole = Arrays.asList(rs.getString("console").split(","));

    	          jeuList.add(new Jeu(foundFabricant, foundNom, foundCote, foundConsole));
    	      }

    	  } catch (SQLException throwables) {
    	      throwables.printStackTrace();
    	  }

    	  return jeuList;
    	}

    public Jeu getJeu(String titre, String fabricant) {
        if (titre == null || fabricant == null || titre.equals("") || fabricant.equals("")) {
            return null;
        }

        Jeu result = null;

        try {
            Statement stmt = (Statement) connection.createStatement();
            String sql = "SELECT * FROM Jeu WHERE titre LIKE '" + titre + "' AND fabricant LIKE '" + fabricant + "' LIMIT 0,1";
            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next()) {
                String foundFabricant  = rs.getString("fabricant");
                String foundtitre = rs.getString("titre");
                String foundCote = rs.getString("cote");
                Collection<String> foundConsole = Arrays.asList(rs.getString("console").split(","));

                result = new Jeu(foundFabricant, foundtitre, foundCote, foundConsole);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return result;
    }

	public Jeu jeuEnLigne(String ligne){
		Jeu nouveau;

		String[] premiere = ligne.split(";");
		String[] con = premiere[3].split(",");

		nouveau = new Jeu(premiere[0],premiere[1],premiere[2]);
		for(String s : con)
			nouveau.addConsole(s);

		return nouveau;
	}

	public void addBdd(String nomFile) {
        try {
            Statement stmt = (Statement) connection.createStatement();
            String sql = "DELETE FROM Jeu";
            stmt.executeUpdate(sql);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        FileReader fr = null;
		boolean existeFile = true;
		boolean finFichier = false;

		try {
		   fr = new FileReader(nomFile);
		} catch (java.io.FileNotFoundException e) {
		   System.out.println("Probleme d'ouvrir le fichier " + nomFile);
		   existeFile = false;
		}

		if (existeFile) {
			try{

		  		BufferedReader entree = new BufferedReader (fr);

		  		while (!finFichier) {
			  		String ligne = entree.readLine();
			  		if (ligne != null){
			  			addJeu(jeuEnLigne(ligne));
			  		}
			  		else
			  			finFichier = true;
		  		}
		  		entree.close();
			}catch(IOException e){
				System.out.println("Probleme lors de la lecture du fichier");
			}
		}
	}

    public void loadBdd(String nomFile)
    {

        addBdd(nomFile);
    }

    public ArrayList<Jeu> chercheConsole(String console) {
        ArrayList<Jeu> jeuList = new ArrayList<>();

        try {
            Statement stmt = (Statement) connection.createStatement();
            String sql = "SELECT * FROM Jeu WHERE console LIKE '%" + console + "%'";
            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next()) {
                String foundFabricant  = rs.getString("fabricant");
                String foundtitre = rs.getString("titre");
                String foundCote = rs.getString("cote");
                Collection<String> foundConsole = Arrays.asList(rs.getString("console").split(","));

                jeuList.add(new Jeu(foundFabricant, foundtitre, foundCote, foundConsole));
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return jeuList;
    }

    public Collection<Jeu> getJeuxFabricant(String fabricant) {
        ArrayList<Jeu> jeuList = new ArrayList<>();

        try {
            Statement stmt = (Statement) connection.createStatement();
            String sql = "SELECT * FROM Jeu WHERE fabricant LIKE '%" + fabricant + "%'";
            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next()) {
                String foundFabricant  = rs.getString("fabricant");
                String foundtitre = rs.getString("titre");
                String foundCote = rs.getString("cote");
                Collection<String> foundConsole = Arrays.asList(rs.getString("console").split(","));

                jeuList.add(new Jeu(foundFabricant, foundtitre, foundCote, foundConsole));
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return jeuList;
    }

	public void saveBdd(String nomFichier){
		if(laBase != null && laBase.isEmpty()==false){
			try{

				PrintWriter aCreer = new PrintWriter( new FileWriter(nomFichier));

				for(Collection<Jeu> col : laBase.values())
					for(Jeu j : col)
						aCreer.println(j.pourSauvegarde());

				aCreer.close();

			}catch(IOException e){
				System.out.println("Probleme d'ouverture ou d'ecriture du fichier de sauvegarde");
			}
		}
	}
	
    public ArrayList<Jeu> chercheCote(String laCote){
        ArrayList<Jeu> jeuList = new ArrayList<>();

        try {
            Statement stmt = (Statement) connection.createStatement();
            String sql = "SELECT * FROM Jeu WHERE cote LIKE '%" + laCote + "%'";
            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next()) {
                String foundFabricant  = rs.getString("fabricant");
                String foundtitre = rs.getString("titre");
                String foundCote = rs.getString("cote");
                Collection<String> foundConsole = Arrays.asList(rs.getString("console").split(","));

                jeuList.add(new Jeu(foundFabricant, foundtitre, foundCote, foundConsole));
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return jeuList;

    }
}


