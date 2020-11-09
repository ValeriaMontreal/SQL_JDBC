CREATE TABLE Jeu (fabricant VARCHAR (20) NOT NULL,
                      titre VARCHAR (50) NOT NULL,
                      cote VARCHAR(5) DEFAULT 'E' NOT NULL,
                      console VARCHAR (50) DEFAULT NULL,
                      PRIMARY KEY (fabricant, titre)
                    );

 


