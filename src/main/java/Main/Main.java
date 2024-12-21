package Main;


import java.sql.SQLException;
import Controller.DBManager;
import Controller.User_Controller;
import Model.User;

public class Main {
    // Initialisation des composants principaux
    static DBManager DBM = new DBManager();
    

    public static void main(String[] args) {

    	User U = new User("", "", -1, "", "", "") ;
        User_Controller UC = new User_Controller(U);
        
        System.out.println("Démarrage de l'application...");
        
        try {
            System.out.println("Connexion à la base de données...");
            DBM.Connection(); 
        	try {
    			DBM.removeTable("Missions") ;
    			DBM.removeTable("Users") ;
    		} catch (SQLException e) {
    			System.out.println("Impossible de supprimer les tables") ;
    			e.printStackTrace();
    		
    		}
            System.out.println("Lancement du menu utilisateur...");
            UC.menu(DBM); 
        } catch (Exception e) {
            System.err.println("Erreur dans l'application : " + e.getMessage());
            e.printStackTrace();
        } finally {
            System.out.println("Fermeture de la connexion à la base de données...");
            DBM.Disconnection(); 
        }
        System.out.println("Fin de l'application.");
    
    }
}
