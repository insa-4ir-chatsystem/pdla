package Controller;

import java.sql.SQLException;
import java.util.Scanner;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import Model.Mission;
import Model.User;

public class User_Controller {
	public Scanner scanner;
	User U;

	public User_Controller(User U) {
		this.scanner = new Scanner(System.in);
		this.U = U;
		this.U.setNull();
	}

	// Méthode pour collecter les informations d'un utilisateur via le Scanner en
	// attribut
	public User signIn(DBManager DBM) {

		if (scanner == null) {
			scanner = new Scanner(System.in);
		}

		System.out.println();
		System.out.println("Processus d'inscription ... veuillez remplir vos données :");
		System.out.println();

		// Saisie et validation du prénom
		String firstName;
		do {
			System.out.print("> Entrez votre prénom : ");
			firstName = scanner.nextLine().trim();
			if (firstName.isEmpty()) {
				System.out.println("Erreur : le prénom ne peut pas être vide.");
			}
		} while (firstName.isEmpty());

		// Saisie et validation du nom de famille
		String lastName;
		do {
			System.out.print("> Entrez votre nom de famille : ");
			lastName = scanner.nextLine().trim();
			if (lastName.isEmpty()) {
				System.out.println("Erreur : le nom de famille ne peut pas être vide.");
			}
		} while (lastName.isEmpty());

		// Saisie et validation de l'âge
		int age = -1;
		while (age <= 0) {
			System.out.print("> Entrez votre âge : ");
			if (scanner.hasNextInt()) {
				age = scanner.nextInt();
				if (age <= 0) {
					System.out.println("Erreur : l'âge doit être un entier positif.");
				}
			} else {
				System.out.println("Erreur : veuillez entrer un entier valide pour l'âge.");
				scanner.next();
			}
		}
		scanner.nextLine();

		String email;
		boolean emailValid = false;
		do {
			System.out.print("> Entrez votre adresse email : ");
			email = scanner.nextLine().trim();
			if (email.isEmpty()) {
				System.out.println("Erreur : l'adresse email ne peut pas être vide.");
			} else {
				try {
					if (DBM.isEmailExist(email)) {
						System.out.println("Erreur : l'adresse email est déjà utilisée. Veuillez en choisir une autre");
					} else emailValid = true;
				} catch (SQLException e) {
					System.out.println("Impossible de vérifier l'adresse mail");
					e.printStackTrace();
				}
			}
		} while (email.isEmpty() || !emailValid);

		// Saisie et validation du mot de passe
		String pw;
		do {
			System.out.print("> Veuillez créer un mot de passe : ");
			pw = scanner.nextLine().trim();
			if (pw.isEmpty()) {
				System.out.println("Erreur : le mot de passe ne peut pas être vide.");
			}
		} while (pw.isEmpty());

		System.out.println(
				"> Souhaitez-vous vous inscrire en tant que bénévole, réclamant ou valideur ? (taper benevole, reclamant, valideur) : ");
		String role = scanner.nextLine().trim().toLowerCase();

		while (!role.equals("benevole") && !role.equals("reclamant") && !role.equals("valideur")) {
			System.out.println(
					"Choix invalide. Tapez benevole pour bénévole, reclamant pour réclamant ou valideur pour valideur.");
			role = scanner.nextLine().trim().toLowerCase();
		}

		switch (role) {
		case "benevole":
		case "reclamant":
		case "valideur":
			U = new User(firstName, lastName, age, role, pw, email);
			try {
				DBM.addUser(U);
			} catch (SQLException e) {
				System.out.println("Impossible d'ajouter cet utilisateur");
				e.printStackTrace();
			}
			System.out.println();
			System.out.println("--------------------------------  -------------------------------- ");
			System.out.println();
			return U;
		default:
			return null;
		}

	}

	// MENU PRINCIPAL
	public void menu(DBManager DBM) {
		boolean continuerMenu = false;
		boolean sortir = false;
		boolean réponseValide = false;
		String réponse;

		while (!sortir) {
			System.out.println("\n\n\n");
			System.out.println(
					"[MENU PRINCIPAL] Bienvenue dans \033[3mMon bénévole\033[0m, l'application de bénévolat spontané !");
			System.out.println("Que souhaitez-vous faire ?");
			System.out.println("> inscription (taper inscription)");
			System.out.println("> connexion (taper connexion)");
			System.out.println("> quitter l'application (taper quitter)");

			réponse = scanner.nextLine();

			if (réponse.equals("inscription")) {
				U.setNull();
				U = signIn(DBM);
				
				réponseValide = false;
				while (!réponseValide) {
					System.out.println("Souhaitez-vous poursuivre dans l'application ? (yes/no) ");
					réponse = scanner.nextLine();
					if (réponse.equals("yes")) {
						réponseValide = true;
						continuerMenu = true;
					} else if (réponse.equals("no")) {
						réponseValide = true;
						continuerMenu = false;
						System.out.println();
						System.out.println("----------------------------------------------------------------- ");
						System.out.println("Retour au menu principal ... ");
						System.out.println("----------------------------------------------------------------- ");
						System.out.println();

					} else
						System.out.println("Réponse non valide. Veuillez répondre yes ou no");
				}
			} else if (réponse.equals("connexion")) {
				U.setNull();
				String email;
				String pw;

				réponseValide = false;
				while (!réponseValide) {
					System.out.println("> Quel est votre adresse mail ? (taper menu pour revenir au menu principal)");
					email = scanner.nextLine();
					U.setEmail(email);

					if (DBM.userExists(U)) {
						System.out.println("> Veuillez entrer votre mot de passe  :");
						pw = scanner.nextLine();
						try {
							if (DBM.authenticateUser(U, email, pw)) {
								réponseValide = true;
								continuerMenu = true;
								System.out.println("Mot de passe correct ! Redirection vers le menu utilisateur ... ");
							} else
								System.out.println("Mot de passe incorrect. \n> Veuillez réessayer à nouveau : ");
						} catch (SQLException e) {
							e.printStackTrace();
						}
						// }
					} else if (email.equals("menu")) {
						continuerMenu = false;
						réponseValide = true;
						System.out.println();
						System.out.println("----------------------------------------------------------------- ");
						System.out.println("Retour au menu principal ... ");
						System.out.println("----------------------------------------------------------------- ");
						System.out.println();
					} else
						System.out.println("Adresse mail ou commande inconnue. Veuillez rééssayer");
				}
			} else if (réponse.equals("quitter")) {
				réponseValide = false;
				while (!réponseValide) {
					System.out.println(
							"Etes-vous sûr de vouloir quiiter l'application ? (tapez menu pour revenir ou quitter pour quitter)");
					réponse = scanner.nextLine();

					if (réponse.equals("quitter")) {
						System.out.println("Au plaisir de vous revoir !");
						sortir = true;
						continuerMenu = false;
						réponseValide = true;
					} else if (réponse.equals("menu")) {
						réponseValide = true;
						System.out.println();
						System.out.println("----------------------------------------------------------------- ");
						System.out.println("Retour au menu principal ... ");
						System.out.println("----------------------------------------------------------------- ");
						System.out.println();
					} else
						System.out.println("Commande inconnue. Veuilez réessayer.");
				}
			} else
				System.out.println("Commande inconnue. Veuillez réessayer.");
			while (continuerMenu) {
				if (U.getRole().equals("benevole")) {
					System.out.println();
					System.out.println();
					handleBenevoleMenu(DBM);
					continuerMenu = false;
				} else if (U.getRole().equals("reclamant")) {
					System.out.println();
					System.out.println();
					handleReclamantMenu(DBM);
					continuerMenu = false;

				} else {
					System.out.println("Menu non disponible pour cet utilisateur.");
					continuerMenu  = false ;
				}
			}
		}
	}

	// MENU RECLAMANT
	public void handleReclamantMenu(DBManager DBM) {
		boolean continuerMenu = true;
		boolean continuerMission = true;
		boolean missionChoisie = false;

		// variables du scanner
		String id;
		String commande = null;
		String réponse;

		// variable des missions
		String date;
		String nature;
		String time;
		String Name;

		System.out.println("[MENU RECLAMANT] Bienvenue " + U.getFirstName() + " " + U.getLastName()
				+ ", que souhaitez-vous faire ?");
		while (continuerMenu) {
			System.out.println("Vous pouvez  : ");
			System.out.println("> consulter vos missions (taper consulter)");
			System.out.println("> ou vous déconnecter (taper disconnect)");
			System.out.println();

			commande = scanner.nextLine();

			if (commande.equals("consulter")) {
				
				continuerMission = true ;
				while (continuerMission) {
					
					System.out.println("\n\nPage des missions :");
	
					boolean FoundMission = false;
					try {
						FoundMission = DBM.printQuery(DBM.missions_attente_reclamant, U.getID()); // aucune dire qu'on en a
																									// pas trouvé
					} catch (SQLException e) {
						System.out.println("Affichage des missions impossible");
						e.printStackTrace();
					}
	
					if (!FoundMission) {
						System.out.println("\n\t--- Aucune mission --- \n");
					}

					System.out.println("Vous souhaitez.. ");
					System.out.println("> ajouter un mission (taper ajouter)");
					if (FoundMission) System.out.println("> effacer une mission (taper effacer)");
					System.out.println("> ou revenir au menu principal (taper menu)");

					commande = scanner.nextLine();

					if (commande.equals("ajouter")) {
						System.out.println("Veuillez rentrer les modalités de la mission :");

						System.out.println("Quelle est la nature de la missions ?");
						nature = scanner.nextLine();

						System.out.println("Combien de temps dure la mission");
						time = scanner.nextLine();

						System.out.println("A partir de quand les mission doit-elle être effectuée ?");
						date = scanner.nextLine();

						while (!isValidDate(date)) {
							System.out.println("Date invalide. Veuillez entrer une date au format DD/MM/YYYY.");
							date = scanner.nextLine();
						}
						try {
							date = convertDate(date);
						} catch (ParseException e) {
							System.out.print("problème avec la conversion de date");
							e.printStackTrace();
						}

						System.out.println("Veuillez donner un nom à la mission :");
						Name = scanner.nextLine();

						try {
							DBM.addMission(new Mission(0, date, nature, time, "en attente", U, null, null, Name));
							System.out.println();
							DBM.printQuery(DBM.missions_attente_reclamant, U.getID());
						} catch (SQLException e) {
							System.out.println("Erreur d'affichage");
							e.printStackTrace();
						}
					} else if (commande.equals("effacer")) { // si pas de mission > pas possible d'effacer

						if (FoundMission) {

							missionChoisie = false;
							while (!missionChoisie) {
								System.out.println("Laquelle souhaitez-vous supprimer ? (taper son id sinon menu)");

								id = scanner.nextLine();

								if (id.equals("menu")) {
									missionChoisie = true;
									continue;
								}
								try {
									if (DBM.isMissionExist(id)) {
										System.out.println("S'agit-il de cette mission ? (yes/no)");

										try {
											DBM.printMissionById(id);
										} catch (SQLException e) {
											System.out.println("Affichage de la mission impossible");
											e.printStackTrace();
										}

										réponse = scanner.nextLine();

										if (réponse.equals("yes")) {
											try {
												DBM.removeMission(id);
												missionChoisie = true;
											} catch (SQLException e) {
												System.out.println("Impossible de supprimer la missions");
												e.printStackTrace();
											}
										} else if (réponse.equals("no")) {
											System.out.println("La mission n'a pas été supprimée");
											System.out.println();
										} else {
											System.out.println("Réponse non valide");
										}
									} else {
										System.out.println("Aucune mission trouvée avec cet ID. Veuillez réessayer.");
										continue;
									}
								} catch (SQLException e) {
									System.out.println(
											"Une erreur s'est produite lors de la vérification de la mission.");
									e.printStackTrace();
								}
							}
						} else
							System.out.println("Vous ne pouvez pas effacer de missions");
					} else if (commande.equals("menu")) {
						continuerMission= false ;
					}
					else System.out.println("Commande inconnue. Veuillez rééssayer") ;
				}
			} else if (commande.equals("disconnect"))

			{
				boolean réponseValide = false;
				while (!réponseValide) {
					System.out.println("Souhaitez-vous vous déconnecter ? (yes/no)");
					réponse = scanner.nextLine();
					if (réponse.equals("yes")) {
						System.out.println();
						System.out.println("Déconnexion ...");
						System.out.println();
						System.out.println();
						réponseValide = true;
						continuerMenu = false;

					} else if (réponse.equals("no")) {
						réponseValide = true;
						System.out.println();
						System.out.println("----------------------------------------------------------------- ");
						System.out.println("Retour au menu réclamant ... ");
						System.out.println("----------------------------------------------------------------- ");
						System.out.println();
					} else
						System.out.println("Réponse non valide, veuillez répondre yes ou no");
				}
				continue;
			}
			else System.out.println("Commande inconnue. Veuillez rééssayer") ;
			System.out.println();
			System.out.println("----------------------------------------------------------------- ");
			System.out.println("Retour au menu réclamant ... ");
			System.out.println("----------------------------------------------------------------- ");
			System.out.println();
		}
	}

	// MENU BENEVOLE > à retester
	public void handleBenevoleMenu(DBManager DBM) {
		boolean continuerMenu = true;
		boolean missionChoisie = false;
		boolean réponseValide = false;

		// variables du scanner
		String id;
		String commande = null;
		String réponse;

		while (continuerMenu) {
			System.out.println(
					"[MENU BENEVOLE] Bienvenue " + U.getFirstName() + U.getLastName() + ", que souhaitez-vous faire ?");
			System.out.println("Vous pouvez  : ");
			System.out.println("> consulter les missions en attente (taper consulter)");
			System.out.println("> proposer directement des aides spontannées (taper proposer)");
			System.out.println("> ou vous déconnecter (taper disconnect)");
			System.out.println();

			commande = scanner.nextLine();

			// CHOISIR MISSION
			if (commande.equals("consulter")) {
				System.out.println("Voici les missions en attente :");
				try {
					DBM.printQuery(DBM.missions_attente, "en attente");
				} catch (SQLException e) {
					System.out.println("Affichage des missions en attente impossible");
					e.printStackTrace();
				}
				System.out.println();
				System.out.println(
						"Souhaitez-vous proposer votre aide pour une des missions ? (taper aider) ou souhaitez-vous revenir au menu ? (taper menu)");

				commande = scanner.nextLine();

				if (commande.equals("aider")) {
					missionChoisie = false;
					while (!missionChoisie) {
						System.out.println(
								"Veuillez choisir la mission sur laquelle vous souhaitez vous positionner (taper son id) ou revenir au menu principal (taper menu)");

						id = scanner.nextLine();
						try {
							if (DBM.isMissionExist(id)) {
								System.out.println("Veuillez confirmer qu'il s'agit de cette mission. (yes/no)");

								try {
									DBM.printMissionById(id);
								} catch (SQLException e) {
									System.out.println("Affichage de la mission impossible");
									e.printStackTrace();
								}
								réponse = scanner.nextLine();
								if (réponse.equals("yes")) {
									System.out.println("Vous vous êtes positionné sur la mission d'id " + id);
									réponseValide = true;
									try {
										DBM.updateMissionState(id, "en cours");
										missionChoisie = true;
									} catch (SQLException e) {
										System.out.println("Impossible de se positionner sur la mission d'id" + id);
										e.printStackTrace();
									}
								} else if (réponse.equals("no")) {
									System.out.println("La missions ne vous a pas été attribuée.");
									réponseValide = true;
									;
								} else
									System.out.println("Réponse non valide, veuillez répondre yes ou no");
							} else if (id.equals("menu")) {
								missionChoisie = true;
							} else {
								System.out.println("Aucune mission trouvée avec cet ID. Veuillez réessayer.");
								continue;
							}
						} catch (SQLException e) {
							System.out.println("Une erreur s'est produite lors de la vérification de la mission.");
							e.printStackTrace();
						}
					}
				}
			}

			// PROPOSER MISSION
			else if (commande.equals("proposer")) {
				System.out.println("Vous avez choisi de proposer une aide. (pas encore implémentée)");
				// ...
				continue;
			} else if (commande.equals("disconnect")) {

				réponseValide = false;
				while (!réponseValide) {
					System.out.println("Souhaitez-vous vous déconnecter ? (yes/no)");
					réponse = scanner.nextLine();
					if (réponse.equals("yes")) {
						System.out.println();
						System.out.println("Déconnexion ...");
						System.out.println();
						System.out.println();
						réponseValide = true;
						continuerMenu = false;

					} else if (réponse.equals("no")) {
						réponseValide = true;
					} else
						System.out.println("Réponse non valide, veuillez répondre yes ou no");
				}
				continue;
			} else {
				System.out.println("Commande inconnue. ");
				continue;
			}
			System.out.println();
			System.out.println("----------------------------------------------------------------- ");
			System.out.println("Retour au menu bénévole ... ");
			System.out.println("-----------------------------------------------------------------");
			System.out.println();
		}
	}

	// convertir un String en format date
	public static String convertDate(String date) throws ParseException {
		SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
		inputFormat.setLenient(false);

		Date parsedDate = inputFormat.parse(date);
		return outputFormat.format(parsedDate);
	}

	// vérifier que le format date est bien appliqué au String en paramètre
	public static boolean isValidDate(String date) {
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		format.setLenient(false);
		try {
			format.parse(date);
			return true;
		} catch (ParseException e) {
			return false;
		}
	}

	// Méthode pour fermer le scanner
	public void closeScanner() {
		if (scanner != null) {
			scanner.close();
		}
	}
}
