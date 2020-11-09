import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.stream.Collectors;

public class tp3 extends JFrame implements ActionListener {

    private JMenuBar mb;
    private JMenu file, jeu, fabricant, console, cote;
    private JMenuItem mi, open, chercher, ajouter, chercherFabricant, chercherConsole, chercherCote;
    private JTextArea ta;
    private JButton quitter;
   
    Bdd myBdd;

public tp3(int l,int h, String titre){

    setSize(l,h);
    setTitle(titre);

    open=new JMenuItem("Ouvrir un tableau");
    open.setMnemonic('o');
    open.addActionListener(this);
    
    file=new JMenu("Fichier");
    file.setMnemonic('f');
    file.add(open);
    
    chercher=new JMenuItem("Chercher un jeu");
    chercher.setMnemonic('c');
    chercher.addActionListener(this);
    jeu=new JMenu("JEU");//
    jeu.setMnemonic('j');
    jeu.add(chercher);

    ajouter=new JMenuItem("Ajouter un jeu");
    ajouter.setMnemonic('a');
    ajouter.addActionListener(this);
    jeu.add(ajouter); 
    
    chercherFabricant = new JMenuItem("Chercher par fabricant");
    chercherFabricant.setMnemonic('c');
    chercherFabricant.addActionListener(this);
    fabricant = new JMenu("Fabricant");
    fabricant.setMnemonic('f');
    fabricant.add(chercherFabricant);
    
    chercherConsole = new JMenuItem("Chercher par console");
    chercherConsole.setMnemonic('c');
    chercherConsole.addActionListener(this);
    console = new JMenu("Console");
    console.setMnemonic('c');
    console.add(chercherConsole);

    chercherCote = new JMenuItem("Chercher par cote");
    chercherCote.setMnemonic('c');
    chercherCote.addActionListener(this);
    cote = new JMenu("Cote");
    cote.setMnemonic('c');
    cote.add(chercherCote);

    mb=new JMenuBar();
    mb.setBounds(0,0,800,20);
    mb.add(file);
    mb.add(jeu);
    mb.add(fabricant);
    mb.add(console);
    mb.add(cote);
        
    mi=new JMenuItem("A propos");
    mb.add(mi);
    mi.addActionListener(this);

    ta=new JTextArea(50,50);
    ta.setLineWrap(true);
    ta.setBorder(new EmptyBorder(50, 50, 50, 50));

    quitter = new JButton ("QUITTER");
    quitter.setMnemonic('q');
    quitter.addActionListener(this);
    quitter.setPreferredSize(new Dimension(300, 50));
    Font fQuitter = quitter.getFont();
    quitter.setFont(fQuitter.deriveFont(fQuitter.getSize()*1.8f));
    quitter.setBorder(BorderFactory.createLineBorder(Color.lightGray, 2));
    quitter.addActionListener(new ActionListener(){
        public void actionPerformed(ActionEvent e){
            System.exit(0);
        }
    });

    add(mb, BorderLayout.PAGE_START);
    add(ta, BorderLayout.CENTER);
    add(quitter, BorderLayout.AFTER_LAST_LINE);
    setVisible(true);

}

private final static String newline = "\n";

    public void actionPerformed(ActionEvent e) {
    	
        if (e.getSource() == open) {
            checkIfBddInitialized();
            JFrame openFrame = new JFrame("Afficher un tableau");
            JPanel openPanel = new JPanel();
            openFrame.setSize(600, 600);

            TextArea OpenResultArea = new TextArea();
            OpenResultArea.setBounds(50, 150, 500, 300);

            JButton openButton = new JButton();
            openButton.setText("AFFICHER");
            openButton.setPreferredSize(new Dimension(300, 50));
            Font f = openButton.getFont();
            openButton.setFont(f.deriveFont(f.getSize()*1.8f));

            openButton.addActionListener(new ActionListener() {
                @Override
                    public void actionPerformed(ActionEvent e) {

                        Collection<Jeu> jeux = myBdd.getAll();

                        String result = jeux.stream().map(Jeu::toString).collect(Collectors.joining (","));
                        OpenResultArea.setText(result);
                    }
                });

            openPanel.setLayout(null);
            openPanel.add(OpenResultArea);
            openFrame.add(openPanel);
            openFrame.add(openButton, BorderLayout.PAGE_END);
            openFrame.setLocationRelativeTo(null);
            openFrame.setVisible(true);
        }

        if(e.getSource()==chercher){
        	checkIfBddInitialized();
            JFrame frame = new JFrame("Chercher un jeu");
            JPanel panel = new JPanel();
            frame.setSize(600, 600);

            TextField t1,t2;
            t1=new TextField();
            t1.setBounds(150,50, 300,30);
            JLabel forFabr = new JLabel("FABRICANT:");
            forFabr.setFont (forFabr.getFont ().deriveFont (15.0f));
            forFabr.setBounds(20,50, 100,30);

            t2=new TextField(20);
            t2.setBounds(150,100, 300,30);
            JLabel forTitre = new JLabel("TITRE:");
            forTitre.setFont (forTitre.getFont ().deriveFont (15.0f));
            forTitre.setBounds(20,100, 100,30);

            JLabel forResultat = new JLabel("RESULTAT:");
            forResultat.setFont (forResultat.getFont ().deriveFont (15.0f));
            forResultat.setBounds(20,150, 100,20);
            TextArea area = new TextArea();
            area.setBounds(50, 180, 500, 200);
                        
            JButton button = new JButton();
            button.setText("CHERCHER");
            Font f = button.getFont();
            button.setFont(f.deriveFont(f.getSize()*1.8f));

            button.addActionListener(new ActionListener() {
              public void actionPerformed(ActionEvent e) {
                String fab = t1.getText().toUpperCase();
                String titre = t2.getText().toUpperCase();
                area.setText(afficherJeu(fab, titre));
              }
            });
            
            panel.setLayout(null);

            panel.add(forFabr);
            panel.add(t1);
            panel.add(forTitre);
            panel.add(t2);
            panel.add(forResultat);
            panel.add(area);

            frame.add(button, BorderLayout.PAGE_END);
            frame.add(panel);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        }
   
        if(e.getSource()==ajouter){
        	checkIfBddInitialized(); 
            JFrame frame = new JFrame("Ajouter un jeu");
            JPanel panel = new JPanel();
            frame.setSize(600, 500);

            TextField t1,t2,t3;
            t1=new TextField(20);
            JLabel forFabr = new JLabel("FABRICANT:", SwingConstants.LEFT);
            forFabr.setFont (forFabr.getFont ().deriveFont (15.0f));
            forFabr.setBounds(20, 40, 100, 30);
            t1.setBounds(130, 40, 300, 30);

            t2=new TextField(20);
            JLabel forTitre = new JLabel("TITRE:");
            forTitre.setFont (forTitre.getFont ().deriveFont (15.0f));
            forTitre.setBounds(20, 80, 100, 30);
            t2.setBounds(130, 80, 300, 30);

            t3=new TextField(20);
            JLabel forCote = new JLabel("COTE:");
            forCote.setFont (forCote.getFont ().deriveFont (15.0f));
            forCote.setBounds(20, 120, 100, 30);
            t3.setBounds(130, 120, 300, 30);
              
            JLabel ajouterConsoleLabel = new JLabel("CONSOLE:");
            ajouterConsoleLabel.setFont (ajouterConsoleLabel.getFont ().deriveFont (15.0f));
            TextField consoleText = new TextField(20);
            ajouterConsoleLabel.setBounds(20, 160, 100, 30);
            consoleText.setBounds(130, 160, 300, 30);

            JButton sauver = new JButton("AJOUTER");
            Font font = sauver.getFont();
            sauver.setFont(font.deriveFont(font.getSize()*1.8f));
            
            sauver.addActionListener(new ActionListener() {
              public void actionPerformed(ActionEvent e) {
                String fab = t1.getText();
                String titre = t2.getText();
                String cote = t3.getText();
                String console = consoleText.getText().toUpperCase();
                
                sauverJeu(fab, titre, cote, console);
                frame.dispose();
                System.out.printf("Un jeu est ajoute\n");
               }
             });
            panel.setLayout(null);

            panel.add(forFabr);
            panel.add(t1);
            panel.add(forTitre);
            panel.add(t2);
            panel.add(forCote);
            panel.add(t3);
            panel.add(ajouterConsoleLabel);
            panel.add(consoleText);

            frame.add(panel);
            frame.add(sauver, BorderLayout.AFTER_LAST_LINE);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
     }
        
        if (e.getSource() == chercherFabricant) {
            checkIfBddInitialized();
            JFrame fabrFrame = new JFrame("Cherche par fabricant");
            JPanel fabrPanel = new JPanel();
            fabrFrame.setSize(600, 600);

            JLabel chercheByFabrLabel = new JLabel("ENTREZ LE FABRICANT:");
            chercheByFabrLabel.setFont (chercheByFabrLabel.getFont ().deriveFont (15.0f));
            chercheByFabrLabel.setBounds(50, 50, 200, 30);
            TextField fabrText = new TextField();
            fabrText.setBounds(50, 90, 300, 30);

            TextArea resultArea = new TextArea();
            resultArea.setBounds(50, 150, 500, 300);

            JButton searchButton = new JButton();
            searchButton.setText("CHERCHER");
            searchButton.setPreferredSize(new Dimension(300, 50));
            Font f = searchButton.getFont();
            searchButton.setFont(f.deriveFont(f.getSize()*1.8f));

            searchButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String fabrName = fabrText.getText().toUpperCase();
                    Collection<Jeu> jeux = myBdd.getJeuxFabricant(fabrName);
                    String result = jeux.stream().map(Jeu::toString).collect(Collectors.joining (","));
                    resultArea.setText(result);
                }
            });

            fabrPanel.setLayout(null);

            fabrPanel.add(chercheByFabrLabel);
            fabrPanel.add(fabrText);
            fabrPanel.add(resultArea);
            fabrFrame.add(fabrPanel);

            fabrFrame.add(searchButton, BorderLayout.PAGE_END);
            fabrFrame.setLocationRelativeTo(null);
            fabrFrame.setVisible(true);

        }

    if (e.getSource() == chercherConsole) {
        checkIfBddInitialized();
        JFrame consoleFrame = new JFrame("Cherche par console");
        JPanel consolePanel = new JPanel();
        consoleFrame.setSize(600, 600);

        JLabel chercheByConsoleLabel = new JLabel("ENTREZ LA CONSOLE:");
        chercheByConsoleLabel.setFont (chercheByConsoleLabel.getFont ().deriveFont (15.0f));
        chercheByConsoleLabel.setBounds(50, 50, 200, 30);
        TextField consoleText = new TextField();
        consoleText.setBounds(50, 90, 300, 30);

        TextArea resultArea = new TextArea();
        resultArea.setBounds(50, 100, 500, 500);
        resultArea.setBounds(50, 150, 500, 300);

        JButton searchButton = new JButton();
        searchButton.setText("CHERCHER");
        searchButton.setPreferredSize(new Dimension(300, 50));
        Font f = searchButton.getFont();
        searchButton.setFont(f.deriveFont(f.getSize()*1.8f));

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String consoleName = consoleText.getText().toUpperCase();
                Collection<Jeu> jeux = myBdd.chercheConsole(consoleName);
                String result = jeux.stream().map(Jeu::toString).collect(Collectors.joining (","));
                resultArea.setText(result);
            }
        });

        consolePanel.setLayout(null);
        consolePanel.add(chercheByConsoleLabel);
        consolePanel.add(consoleText);
        consolePanel.add(resultArea);

        consoleFrame.add(consolePanel);
        consoleFrame.add(searchButton, BorderLayout.PAGE_END);
        consoleFrame.setLocationRelativeTo(null);
        consoleFrame.setVisible(true);
    }

        if (e.getSource() == chercherCote) {
        checkIfBddInitialized();
        JFrame coteFrame = new JFrame("Cherche par cote");
        JPanel cotePanel = new JPanel();
        coteFrame.setSize(600, 600);

        JLabel chercheByCoteLabel = new JLabel("ENTREZ LA COTE:");
        chercheByCoteLabel.setFont (chercheByCoteLabel.getFont ().deriveFont (15.0f));
        chercheByCoteLabel.setBounds(50, 50, 200, 30);
        TextField coteText = new TextField();
        coteText.setBounds(50, 90, 300, 30);

        TextArea resultArea = new TextArea();
        resultArea.setBounds(50, 150, 500, 300);

        JButton searchButton = new JButton();
        searchButton.setText("CHERCHER");
        searchButton.setPreferredSize(new Dimension(300, 50));
        Font f = searchButton.getFont();
        searchButton.setFont(f.deriveFont(f.getSize()*1.8f));

        searchButton.addActionListener(new ActionListener() {
            @Override
                public void actionPerformed(ActionEvent e) {
                    String coteName = coteText.getText().toUpperCase();

                    Collection<Jeu> jeux = myBdd.chercheCote(coteName);

                    String result = jeux.stream().map(Jeu::toString).collect(Collectors.joining (","));
                    resultArea.setText(result);
                }
            });

        cotePanel.setLayout(null);
        cotePanel.add(chercheByCoteLabel);
        cotePanel.add(coteText);
        cotePanel.add(resultArea);
        coteFrame.add(cotePanel);
        coteFrame.add(searchButton, BorderLayout.PAGE_END);
        coteFrame.setLocationRelativeTo(null);
        coteFrame.setVisible(true);
    }
        
        if(((JMenuItem)e.getSource()).getText().equals("A propos"))
        {
        	JFrame apropos = new JFrame("A propos");
            JPanel aproposPanel = new JPanel();
            apropos.setSize(1000, 400);

        	JLabel welcome = new JLabel("Travail pratique #3 de VALERIYA POPENKO", SwingConstants.CENTER);
        	welcome.setFont(new Font("Serif", Font.BOLD, 20));
        	welcome.setForeground(Color.BLUE);
            welcome.setBounds(50, 50, 700, 30);
            
        	JLabel description = new JLabel("Pour la compilation de ce projet, il faut utiliser le driver mysql-connector-java-5.1.49-bin.jar", SwingConstants.CENTER);
        	description.setFont(new Font("Serif", Font.BOLD, 18));
        	description.setForeground(Color.WHITE);
        	description.setBackground(Color.GRAY);
        	description.setOpaque(true);
        	description.setBounds(50, 100, 900, 30);

            aproposPanel.setLayout(null);
            aproposPanel.add(welcome);
            aproposPanel.add(description);
            apropos.add(aproposPanel);
            apropos.setLocationRelativeTo(null);
        	apropos.setVisible(true);
        }
    }


    private void checkIfBddInitialized() {
        if (myBdd != null) {
          return;
        }
        myBdd = new Bdd();
        myBdd.loadBdd("jeux.txt");
      }

    public String afficherJeu(String fab, String titre) {
        Jeu aAfficher = myBdd.getJeu(titre, fab);

        String aff;
        aff = aAfficher.toString();
        return aff;
    }
    
    public void sauverJeu(String fab, String titre, String cote, String console) {
    	
    	Jeu unJeu;
    	unJeu = new Jeu (fab, titre, cote);
    	unJeu.addConsole(console);
        myBdd.addJeu(unJeu);
        myBdd.saveBdd("sauvegarde.txt");
    };
    
    public static void main(String [] args) {
        tp3 om=new tp3(800,800,"TP3 de VALERIYA POPENKO");
        om.setLayout(null);
        om.setVisible(true);
        om.setDefaultCloseOperation(EXIT_ON_CLOSE);
           
       
        Connection conn = null;
        Statement stmt = null;
        try{
          
           Class.forName(UserData.JDBC_DRIVER);
           System.out.println("Connecting to database...");
           conn = (Connection) DriverManager.getConnection(UserData.DB_URL,UserData.USER,UserData.PASS);

          
           System.out.println("Creating statement...");
           stmt = (Statement) conn.createStatement();
           String sql;
           sql = "SELECT fabricant, titre, cote, console FROM Jeu";
           ResultSet rs = stmt.executeQuery(sql);

        
           while(rs.next()){
              String fabricant  = rs.getString("fabricant");
        	  String titre = rs.getString("titre");
              String cote = rs.getString("cote");
              String console = rs.getString("console");

             
              System.out.print("fabricant: " + fabricant);
              System.out.print(", titre: " + titre);
              System.out.print(", cote: " + cote);
              System.out.println(", console: " + console);
           }
           
           rs.close();
           stmt.close();
           conn.close();
        }catch(SQLException se){
           
           se.printStackTrace();
        }catch(Exception e){
           
           e.printStackTrace();
        }finally{
           
           try{
              if(stmt!=null)
                 stmt.close();
           }catch(SQLException se2){
           }
           try{
              if(conn!=null)
                 conn.close();
           }catch(SQLException se){
              se.printStackTrace();
           }
        }
        System.out.println("connection est ferme");
     
    }
}


