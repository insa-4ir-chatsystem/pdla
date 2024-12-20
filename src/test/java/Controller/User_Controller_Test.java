package Controller;

import java.sql.SQLException;
import java.util.Scanner;

import org.junit.Before;
import org.junit.After;
import org.junit.Test;

import com.google.protobuf.TextFormat.ParseException;

import static org.junit.Assert.*;
import Model.User;

public class User_Controller_Test {

    private User user;
    private User_Controller UC;
    private DBManager DBM;

    // Initialisation des objets avant chaque test
    @Before
    public void setUp() {
        // Créer les utilisateurs avant chaque test
        user = new User("Alice", "Doe", 30, "reclamant", "alice123", "alice@gmail.com");

        // Instancier les classes nécessaires avant chaque test + connexion
        UC = new User_Controller(user);
        DBM = new DBManager();
        DBM.Connection();
    }

    // Nettoyage après chaque test
    @After
    public void tearDown() {
        try {
            DBM.removeTable("Users");
            DBM.removeTable("Missions");
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'effacement des tables");
            e.printStackTrace();
        }
        DBM.Disconnection();
    }

    @Test
    public void convertDate_test_validFormat() throws ParseException {
        String inputDate = "19/12/2024";
        String expectedDate = "2024-12-19";
        String actualDate = null;
        try {
            actualDate = User_Controller.convertDate(inputDate);
        } catch (java.text.ParseException e) {
            System.out.println("Impossible de convertir la date");
            e.printStackTrace();
        }
        assertEquals(expectedDate, actualDate);
    }

    @Test
    public void convertDate_test_invalidFormat() {
        String inputDate = "2024/12/19";
        assertThrows(java.text.ParseException.class, () -> User_Controller.convertDate(inputDate));
    }

    @Test
    public void isValidDate_test_valid() {
        assertTrue(User_Controller.isValidDate("19/12/2024"));
    }

    @Test
    public void isValidDate_test_invalid() {
        assertFalse(User_Controller.isValidDate("2024/12/19"));
        assertFalse(User_Controller.isValidDate("31/02/2024"));
    }

    @Test
    public void signIn_test_correctInput() {
        // Simuler les entrées utilisateur via un Scanner avec un texte prédéfini
        String userInput = """
                John
                Doe
                30
                john.doe@example.com
                securepassword
                benevole
                """;
        UC.scanner = new Scanner(userInput);

        User newUser = UC.signIn(DBM);

        assertNotNull(newUser);
        assertEquals("John", newUser.getFirstName());
        assertEquals("Doe", newUser.getLastName());
        assertEquals(30, newUser.getAge());
        assertEquals("john.doe@example.com", newUser.getEmail());
        assertEquals("securepassword", newUser.getPassword());
        assertEquals("benevole", newUser.getRole());
    }
}

