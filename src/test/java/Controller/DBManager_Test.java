package Controller;

import java.sql.SQLException;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import Model.Mission;
import Model.User;
import static org.junit.Assert.*;

public class DBManager_Test {
        
        private DBManager DBM; // Utiliser une instance au lieu de la variable statique
        static User reclamant = new User("Alice", "Doe", 56, "reclamant", "alice", "alice@gmail.com");
        static User benevole = new User("Bob", "Smith", 24, "benevole", "bob", "bob@outlook.fr");
        static User valideur = new User("Charlie", "Brown", 54, "valideur", "charlie", "charlie@insa-toulouse.fr");
        static Mission mission = new Mission(
                1,
                "2024-12-03",
                "Transport médical",
                "02:30:00",
                "en attente",
                reclamant,
                benevole,
                valideur,
                "Transport médical");

        // Méthode d'initialisation avant chaque test
        @Before
    	public void setUp() {
    	    DBM = new DBManager();
    	    try {
    	        DBM.Connection();  // Assurez-vous que la connexion est bien établie
    	        // Réinitialisez les tables si nécessaire
    	        DBM.removeTable("Missions");
    	        DBM.removeTable("Users");
    	    } catch (SQLException e) {
    	        e.printStackTrace();
    	    }
    	}
    	
    	@After
    	public void tearDown() {
    	    try {
    	        // Ne fermez pas la connexion avant d'appeler removeTable
    	        DBM.removeTable("Missions");
    	        DBM.removeTable("Users");
    	    } catch (SQLException e) {
    	        e.printStackTrace();
    	    } finally {
    	        DBM.Disconnection();  // Fermez la connexion après les opérations de test
    	    }
    }

        @Test
        public void addUser_Test() {
            try {
            	DBM = new DBManager();
                DBM.Connection(); 
            	System.out.println();
            	System.out.println("addUser_Test()") ;
                DBM.addUser(benevole);
                DBM.removeTable("Users");
            } catch (SQLException e) {
                e.printStackTrace();
                fail("Exception lors de l'ajout de l'utilisateur");
            }
        }

        @Test
        public void removeTable_Test() {
            try {
            	DBM = new DBManager();
                DBM.Connection(); 
            	System.out.println();
            	System.out.println("removeTable_Test()");
                DBM.addUser(benevole);
                DBM.addUser(reclamant);
                DBM.removeTable("Users");
            } catch (SQLException e) {
                e.printStackTrace();
                fail("Exception lors de la suppression de la table");
            }
        }

        @Test
        public void printTable_Test() {
            try {
            	DBM = new DBManager();
                DBM.Connection(); 
            	System.out.println();
            	System.out.println("printTable_Test()");
                DBM.removeTable("Users");
                DBM.addUser(benevole);
                DBM.addUser(reclamant);
                DBM.printTable("Users");
                DBM.removeTable("Users");
            } catch (SQLException e) {
                e.printStackTrace();
                fail("Exception lors de l'affichage de la table");
            }
        }

        @Test
        public void addMission_Test() {
            try {
            	DBM = new DBManager();
                DBM.Connection(); 
            	System.out.println();
            	System.out.println("addMission_Test()");
                DBM.removeTable("Missions");
                DBM.removeTable("Users");
                DBM.addUser(reclamant);
                DBM.addUser(benevole);
                DBM.addUser(valideur);
                DBM.addMission(mission);
                System.out.println("La mission " + mission.toString() + " a été ajoutée à la base de données.");
                DBM.printTable("Missions");
                DBM.removeTable("Missions");
            } catch (SQLException e) {
                e.printStackTrace();
                fail("Exception lors de l'ajout de la mission");
            }
        }

        @Test
        public void removeMission_Test() {
            try {
            	DBM = new DBManager();
                DBM.Connection(); 
                System.out.println();
                System.out.println("removeMission_Test()");
                DBM.removeTable("Missions");
                DBM.removeTable("Users");

                // Ajout des utilisateurs
                DBM.addUser(reclamant);
                DBM.addUser(benevole);
                DBM.addUser(valideur);

                // Ajout de la mission
                DBM.addMission(mission);

                // Vérifier que la mission a bien été ajoutée
                assertTrue("La mission devrait être ajoutée", DBM.isMissionExist(String.valueOf(mission.getId())));

                // Suppression de la mission
                DBM.removeMission(String.valueOf(mission.getId()));

                // Vérification que la mission a bien été supprimée
                assertFalse("La mission devrait être supprimée", DBM.isMissionExist(String.valueOf(mission.getId())));

                DBM.removeTable("Missions");
                DBM.removeTable("Users");
            } catch (SQLException e) {
                e.printStackTrace();
                fail("Exception lors de la suppression de la mission");
            }
        }


        @Test
        public void printQuery_Test() {
            try {
            	DBM = new DBManager();
                DBM.Connection(); 
            	System.out.println();
            	System.out.println("printQuery_Test");
            	DBM.removeTable("Missions");
                DBM.removeTable("Users");
                DBM.addUser(reclamant);
                DBM.addUser(benevole);
                DBM.addUser(valideur);
                DBM.addMission(mission);
                System.out.println("La mission " + mission.toString() + " a été ajoutée à la base de données.");
                DBM.printQuery(DBM.missions_attente, "en attente");
            } catch (SQLException e) {
                e.printStackTrace();
                fail("Exception lors de l'affichage de la requête");
            }
        }
        
        @Test
        public void isMissionExist_Test() {
            try {
            	DBM = new DBManager();
                DBM.Connection(); 
            	System.out.println();
            	System.out.println("isMissionExist_Test()");
                DBM.removeTable("Missions");
                DBM.removeTable("Users");
                
                DBM.addUser(reclamant);
                DBM.addUser(benevole);
                DBM.addUser(valideur);
                DBM.addMission(mission);

                boolean exists = DBM.isMissionExist(String.valueOf(mission.getId()));
                assertTrue("La mission devrait exister dans la base de données", exists);

                DBM.removeTable("Missions");
                DBM.removeTable("Users");
            } catch (SQLException e) {
                e.printStackTrace();
                fail("Exception lors du test de l'existence de la mission");
            }
        }
        
        @Test
        public void isEmailExist_Test() {
            try {
            	DBM = new DBManager();
                DBM.Connection(); 
            	System.out.println();
            	System.out.println("isMissionEmail_Test()");
                DBM.removeTable("Missions");
                DBM.removeTable("Users");
                
                DBM.addUser(reclamant);

                boolean exists = DBM.isEmailExist(reclamant.getEmail());
                assertTrue("L'email devrait exister dans la base de données", exists);
                
                boolean notExists = DBM.isEmailExist("wrongEmail@fake.com");
                assertFalse("L'email ne devrait pas exister dans la base de données", notExists);

                DBM.removeTable("Missions");
                DBM.removeTable("Users");
            } catch (SQLException e) {
                e.printStackTrace();
                fail("Exception lors du test de l'unicité de l'email");
            }
        }
        
        @Test
        public void userExists_Test() {
            try {
            	DBM = new DBManager();
                DBM.Connection(); 
                System.out.println();
                System.out.println("userExists_Test()");
                DBM.removeTable("Users");

                // Ajout de l'utilisateur
                DBM.addUser(reclamant);

                // Vérification de l'existence de l'utilisateur
                boolean exists = DBM.userExists(reclamant);
                assertTrue("L'utilisateur devrait exister dans la base de données", exists);

                // Vérification pour un utilisateur inexistant
                User fakeUser = new User("Fake", "User", 30, "benevole", "fake", "fake@nonexistent.com");
                boolean notExists = DBM.userExists(fakeUser);
                assertFalse("L'utilisateur ne devrait pas exister dans la base de données", notExists);

                // Suppression des données
                DBM.removeTable("Users");
            } catch (SQLException e) {
                e.printStackTrace();
                fail("Exception lors du test de l'existence de l'utilisateur");
            }
        }

        
        @Test
        public void authenticateUser_Test_ValidUser() {
            try {
                DBM = new DBManager();
                DBM.Connection(); 

                User user = new User("", "", 0, "", "", "");  // Utilisateur vide pour le test

                // Ajout de l'utilisateur valide avec la méthode addUser()
                DBM.addUser(new User("Test", "User", 30, "user", "password123", "testuser@example.com"));

                // Appel à authenticateUser pour un utilisateur valide
                DBM.authenticateUser(user, "testuser@example.com", "password123");

                user.getUser();
                // Vérification des données de l'utilisateur après authentification
                assertEquals("Test", user.getFirstName());
                assertEquals("User", user.getLastName());
                assertEquals(30, user.getAge());
                assertEquals("testuser@example.com", user.getEmail());
                assertEquals("user", user.getRole());

                // Optionnel: suppression de l'utilisateur après test
                DBM.removeTable("Users");  // Si vous voulez nettoyer la table après le test

            } catch (SQLException e) {
                e.printStackTrace();
                fail("Exception lors de l'authentification de l'utilisateur valide");
            }
        }

        @Test
        public void authenticateUser_Test_InvalidUser() {
            try {
                DBM = new DBManager();
                DBM.Connection(); 

                User user = new User("", "", 0, "", "", "");  // Utilisateur vide pour le test

                DBM.addUser(new User("Test", "User", 30, "user", "password123", "testuser@example.com"));

                // Tentative d'authentification avec des informations incorrectes
                DBM.authenticateUser(user, "testuser@example.com", "wrongpassword");

                // Vérification que l'utilisateur n'a pas été authentifié (les données doivent rester nulles)
                assertEquals("", user.getFirstName());

            } catch (SQLException e) {
                e.printStackTrace();
                fail("Exception lors de l'authentification de l'utilisateur invalide");
            }
        }

    }
