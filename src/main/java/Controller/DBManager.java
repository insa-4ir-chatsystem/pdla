package Controller;

import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import Model.Mission;
import Model.User;

public class DBManager {

	// données de connexion
	private static final String URL = "jdbc:mysql://srv-bdens.insa-toulouse.fr:3306/projet_gei_026"; // Remplacez par
																										// l'URL de
																										// votre base de
																										// données
	private static final String USER = "projet_gei_026"; // Votre nom d'utilisateur
	private static final String PASSWORD = "weiy8Tha"; // Votre mot de passe
	Connection connection = null;

	// requêtes SQL
	String afficher_tout = "SELECT * FROM ";
	String missions_attente = """
			SELECT
			    m.nom AS Name,
			    m.id AS MissionID,
			    m.mdate AS Date,
			    m.nature AS Nature,
			    m.mtime AS Time
			FROM
			    Missions m
			JOIN
			    Users u ON m.reclamant_id = u.id
			WHERE
			    m.state = ?;
			""";
	String missions_attente_reclamant = """
			SELECT
			    m.id AS MissionID,
			    m.nom AS Name,
			    m.mdate AS Date,
			    m.nature AS Nature,
			    m.mtime AS Time
			FROM
			    Missions m
			WHERE
			    m.state = "en attente"
			    AND m.reclamant_id = ?;

			""";

	// CONNEXION AVEC LA BDD
	public void Connection() {
		try {
			connection = DriverManager.getConnection(URL, USER, PASSWORD);
			System.out.println("¤ Connexion créée ¤");
		} catch (SQLException e) {
			System.out.println("¤ Impossible de se connecter ¤");
			e.printStackTrace();
		}
	}

	public void Disconnection() {
		if (this.connection != null) {
			try {
				connection.close();
				System.out.println("¤ Déconnexion de la base de donnée ¤");
				connection = null;
			} catch (SQLException e) {
				System.out.println("¤ Erreur de déconnexion :" + e.getMessage());
			}
		}
	}

	// AFFICHAGE LES TABLES
	// afficher n'importe quelle requête sql
	public boolean printQuery(String sql, Object... params) throws SQLException {
		boolean FoundMission;
		try (PreparedStatement pstmt = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
				ResultSet.CONCUR_READ_ONLY)) {
			// Remplir les paramètres de la requête
			for (int i = 0; i < params.length; i++) {
				pstmt.setObject(i + 1, params[i]);
			}

			// Exécuter la requête
			try (ResultSet rs = pstmt.executeQuery()) {
				FoundMission = rs.isBeforeFirst();
				if (!FoundMission)
					return FoundMission;
				else {
					ResultSetMetaData metaData = rs.getMetaData();
					int columnCount = metaData.getColumnCount();

					// Déterminer la largeur maximale de chaque colonne
					int[] columnWidths = new int[columnCount];
					for (int i = 1; i <= columnCount; i++) {
						columnWidths[i - 1] = metaData.getColumnName(i).length();
					}

					// Vérifier la longueur des données dans chaque colonne pour ajuster la largeur
					while (rs.next()) {
						for (int i = 1; i <= columnCount; i++) {
							String value = rs.getString(i);
							if (value != null && value.length() > columnWidths[i - 1]) {
								columnWidths[i - 1] = value.length();
							}
						}
					}

					// Afficher les noms des colonnes avec alignement
					for (int i = 1; i <= columnCount; i++) {
						System.out.print(
								String.format("%-" + columnWidths[i - 1] + "s", metaData.getColumnName(i)) + "  ");
					}
					System.out.println();

					// Ligne séparatrice
					for (int width : columnWidths) {
						System.out.print("-".repeat(width) + "  ");
					}
					System.out.println();

					// Revenir au début du ResultSet après avoir ajusté les largeurs
					rs.beforeFirst();

					// Afficher les données des lignes
					while (rs.next()) {
						for (int i = 1; i <= columnCount; i++) {
							String value = rs.getString(i);
							if (value == null) {
								value = "NULL";
							}
							System.out.print(String.format("%-" + columnWidths[i - 1] + "s", value) + "  ");
						}
						System.out.println();
					}
					return FoundMission;
				}
			}
		} catch (SQLException e) {
			System.err.println("Erreur lors de l'exécution de la requête : " + e.getMessage());
			throw e;
		}
	}

	public void printMissionById(String missionId) throws SQLException {
		// Requête SQL pour afficher une mission avec un ID spécifique
		String sql = "SELECT nom, mdate, nature, mtime FROM Missions WHERE id = ?";
		printQuery(sql, missionId);
	}

	// normalement non utilisée
	public void printTable(String TableName) throws SQLException {
		// Déclaration de la requête SQL avec des paramètres
		String sql = "SELECT * from " + TableName;

		// Connexion à la base de données
		try {
			Statement stmt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			ResultSet rs = stmt.executeQuery(sql);
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();

			// Déterminer la largeur maximale de chaque colonne
			int[] columnWidths = new int[columnCount];
			for (int i = 1; i <= columnCount; i++) {
				columnWidths[i - 1] = metaData.getColumnName(i).length(); // Largeur du nom de la colonne
			}

			// Vérifier la longueur des données dans chaque colonne pour ajuster la largeur
			while (rs.next()) {
				for (int i = 1; i <= columnCount; i++) {
					String value = rs.getString(i);
					if (value != null && value.length() > columnWidths[i - 1]) {
						columnWidths[i - 1] = value.length();
					}
				}
			}

			// Afficher les noms des colonnes avec alignement
			for (int i = 1; i <= columnCount; i++) {
				System.out.print(String.format("%-" + columnWidths[i - 1] + "s", metaData.getColumnName(i)) + "  ");
			}
			System.out.println();

			// Revenir au début du ResultSet après avoir ajusté les largeurs
			rs.beforeFirst();

			// Afficher les données des lignes
			while (rs.next()) {
				for (int i = 1; i <= columnCount; i++) {
					String value = rs.getString(i);
					if (value == null) {
						value = "NULL";
					}
					System.out.print(String.format("%-" + columnWidths[i - 1] + "s", value) + "  ");
				}
				System.out.println();
			}

		} catch (SQLException e) {
			System.err.println("Erreur lors de l'affichage : " + e.getMessage());
			throw e;
		}
	}

	// MODIFICATION LES TABLES
	public void addUser(User user) throws SQLException {
		// Déclaration de la requête SQL avec des paramètres
		String sql = "INSERT INTO Users (FirstName, LastName, age, password, email, role) VALUES (?, ?, ?, ?, ?, ?)";

		// Connexion à la base de données
		try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

			// Remplir les paramètres de la requête
			pstmt.setString(1, user.getFirstName());
			pstmt.setString(2, user.getLastName());
			pstmt.setInt(3, user.getAge());
			pstmt.setString(4, user.getPassword());
			pstmt.setString(5, user.getEmail());
			pstmt.setString(6, user.getRole());

			// Exécuter la requête
			int rowsInserted = pstmt.executeUpdate();

			// Vérifier si l'insertion a réussi
			if (rowsInserted > 0) {
				try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
					if (generatedKeys.next()) {
						user.setID(generatedKeys.getInt(1)); // changer l'id du USER pour qu'il corresponde à celui de
																// la table
						System.out.println("¤ L'utilisateur " + user.getFirstName() + " " + user.getLastName()
								+ " a été ajouté avec succès ! ¤");
					} else {
						throw new SQLException("¤ La création de l'utilisateur a échoué, aucun ID généré. ¤");
					}
				}
			} else {
				throw new SQLException("¤ Aucune ligne insérée dans la base de données. ¤");
			}
		}
	}

	public void addMission(Mission mission) throws SQLException {
		// Déclaration de la requête SQL avec des paramètres
		String sql = "INSERT INTO Missions (mdate, nature, mtime, state, reclamant_id, benevole_id, valideur_id, nom) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

		// Connexion à la base de données
		try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			// Remplir les paramètres de la requête
			pstmt.setString(1, mission.getDate());
			pstmt.setString(2, mission.getNature());
			pstmt.setString(3, mission.getTime());
			pstmt.setString(4, mission.getState());
			pstmt.setInt(5, mission.getReclamant().getID());

			// Si benevole est null, on passe NULL à la base de données
			if (mission.getBenevole() != null) {
				pstmt.setInt(6, mission.getBenevole().getID());
			} else {
				pstmt.setNull(6, java.sql.Types.INTEGER);
			}

			// Même traitement pour valideur
			if (mission.getValideur() != null) {
				pstmt.setInt(7, mission.getValideur().getID());
			} else {
				pstmt.setNull(7, java.sql.Types.INTEGER);
			}

			pstmt.setString(8, mission.getName());

			// Exécuter la requête
			int rowsInserted = pstmt.executeUpdate();

			// Vérifier si l'insertion a réussi
			if (rowsInserted > 0) {
				try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
					if (generatedKeys.next()) {
						mission.setId(generatedKeys.getInt(1)); // changer l'id de la mission pour qu'il corresponde à
																// celui de la table
						System.out.println(
								"¤ La mission de nature : " + mission.getNature() + " a été ajouté avec succès ! ¤");
					} else {
						throw new SQLException("¤ La création de la mission a échoué, aucun ID généré. ¤");
					}
				}
			} else {
				throw new SQLException("¤ Aucune ligne insérée dans la base de données. ¤");
			}
		}
	}

	public void removeMission(String strId) throws SQLException {
		// Vérification que l'objet mission est non nul et a un ID valide

		int id = Integer.parseInt(strId);
		if (id <= 0) {
			throw new IllegalArgumentException("¤ Mission invalide ou ID non valide. ¤");
		}

		// Requête SQL pour supprimer la mission en fonction de son ID
		String sql = "DELETE FROM Missions WHERE id = ?";

		// Connexion à la base de données et exécution de la requête
		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			// Injection de l'ID de la mission dans la requête
			pstmt.setInt(1, id);

			// Exécuter la requête
			int rowsDeleted = pstmt.executeUpdate();

			// Vérifier si la suppression a réussi
			if (rowsDeleted > 0) {
				System.out.println("¤ Mission supprimée avec succès. ¤");
			} else {
				System.out.println("¤ Aucune mission trouvée avec cet ID. ¤");
			}
		} catch (SQLException e) {
			// Gestion des erreurs SQL
			System.err.println("¤ Erreur lors de la suppression de la mission : " + e.getMessage());
			throw e;
		}
	}

	public void updateMissionState(String missionId, String newState) throws SQLException {
		// Requête SQL pour mettre à jour l'état d'une mission
		String sql = "UPDATE Missions SET state = ? WHERE id = ?";

		try (
				// Préparer la requête
				PreparedStatement pstmt = connection.prepareStatement(sql)) {
			// Assigner les paramètres
			pstmt.setString(1, newState);
			pstmt.setString(2, missionId);

			// Exécuter la mise à jour
			int rowsUpdated = pstmt.executeUpdate();

			// Vérifier si la mise à jour a été effectuée
			if (rowsUpdated > 0) {
				System.out.println("L'état de la mission a été mis à jour avec succès !");
			} else {
				System.out.println("Aucune mission trouvée avec l'ID spécifié.");
			}
		} catch (SQLException e) {
			System.err.println("Erreur lors de la mise à jour de l'état de la mission : " + e.getMessage());
			throw e;
		}
	}

	public void removeTable(String TableName) throws SQLException {
		// Déclaration de la requête SQL avec des paramètres
		String sql = "DELETE FROM " + TableName + " WHERE id > 0";

		// Connexion à la base de données
		try (PreparedStatement pstmt = this.connection.prepareStatement(sql)) {

			// Exécuter la requête
			int rowsDeleted = pstmt.executeUpdate();

			// Vérifier si la suppression est réussie
			System.out.println(rowsDeleted + " ligne(s) supprimée(s).");

		} catch (SQLException e) {
			System.err.println("Erreur lors de la suppression : " + e.getMessage());
			throw e;
		}
	}

	// INTERROGATION DE LA BASE DE DONNEES
	public boolean isMissionExist(String missionId) throws SQLException {
		// Requête SQL pour vérifier l'existence d'une mission
		String sql = "SELECT COUNT(*) FROM Missions WHERE id = ?";

		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setString(1, missionId);

			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next() && rs.getInt(1) > 0) {
					return true;
				}
			}
		} catch (SQLException e) {
			System.err.println("¤ Erreur lors de la vérification de l'existence de la mission : " + e.getMessage());
			throw e;
		}
		return false;
	}

	public boolean isEmailExist(String email) throws SQLException {
		String sql = "SELECT COUNT(*) FROM Users WHERE email = ?";

		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			if (email == null || email.isEmpty()) {
				throw new IllegalArgumentException("L'email ne peut pas être null ou vide");
			}
			pstmt.setString(1, email);

			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next() && rs.getInt(1) > 0) {
					return true;
				}
			}
		} catch (SQLException e) {
			System.err.println("¤ Erreur lors de la vérification de l'unicité de l'email : " + e.getMessage());
			throw new RuntimeException("Erreur SQL lors de la vérification de l'email", e);
		}
		return false;
	}

	public boolean userExists(User user) {
		if (user == null || user.getEmail() == null || user.getEmail().isEmpty()) {
			System.err.println("¤ L'utilisateur ou son email est invalide. ¤");
			return false;
		}

		String sql = "SELECT COUNT(*) FROM Users WHERE email = ?";
		try (PreparedStatement stmt = connection.prepareStatement(sql)) {
			stmt.setString(1, user.getEmail());
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					return rs.getInt(1) > 0;
				}
			}
		} catch (SQLException e) {
			System.err.println("¤ Erreur lors de la vérification de l'existence de l'utilisateur : " + e.getMessage());
		}
		return false;
	}

	// changer la modification des données du user retrouvé
	public boolean authenticateUser(User user, String email, String password) throws SQLException {
		String sql = "SELECT * FROM Users WHERE email = ? AND password = ?";
		PreparedStatement stmt = connection.prepareStatement(sql);

		// Paramétrage de la requête pour éviter les injections SQL
		stmt.setString(1, email);
		stmt.setString(2, password);

		ResultSet rs = stmt.executeQuery();

		if (rs.next()) {
			// Si un utilisateur correspondant est trouvé, on remplit l'objet User avec les
			// données récupérées
			user.setFirstName(rs.getString("FirstName"));
			user.setLastName(rs.getString("LastName"));
			user.setAge(rs.getInt("age"));
			user.setID(rs.getInt("id"));
			user.setPassword(rs.getString("password"));
			user.setRole(rs.getString("role"));
			user.setEmail(rs.getString("email"));
			return true; // Authentification réussie
		}
		return false; // Authentification échouée
	}

}
