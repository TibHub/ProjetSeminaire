package outilsBD;

import java.sql.*;

public class RequeteSQL {
	private static String[] requetesSQL = {
			// 0 authentification
			"SELECT login, password, estAdmin, idSoc, idAnim"
					+ " FROM utilisateur WHERE login = ? AND password = ?",

			// 1 modification mot de passe
			"UPDATE utilisateur SET password = ? WHERE login = ?",

			/**************************** SOCIETES ****************************/

			// 2 liste des thèmes (liste déroulante) 
			// sert aussi pour les animateurs
			"SELECT * FROM theme ORDER BY LibelleTheme",

			/* liste des séminaires pour un thème donné (radiobuttons) */
			/* MODIF */// WHERE IdTheme IN (SELECT IdTheme FROM theme)
			// 3
			"SELECT s.*, t.LibelleTheme FROM seminaire s, theme t WHERE s.IdTheme = ?" +
			" AND s.IdTheme = t.IdTheme ORDER BY s.DateSem, s.IntituleSem",

			/**
			 * inscription à un séminaire
			 */
			// 4 pour l'affichage... liste de tous les séminaires
			"SELECT s.*, t.LibelleTheme FROM seminaire s, theme t"
					+ " WHERE s.IdTheme = t.IdTheme" 
					+ " ORDER BY s.DateSem, s.IntituleSem",
			// 5 l'inscription à proprement dite */ // On aura récupéré l'id
			// du séminaire sélectionné ainsi que celui de la société
			// on a l'id de la société dans la requête d'authetification
			"INSERT INTO inscription (IdSociete, IdSem, NbrePlace, DateInscription)"
					+ " values ( ?, ?, ?, SYSDATE)",
			// n.b. : La date d'inscription correspond à la date système
			/**** fin inscription à un séminaire ****/

			// 6 liste des séminaires ayant lieu à une date donnée
			"SELECT s.*, t.*" + " FROM seminaire s, theme t WHERE DateSem = ?"
					+ " AND s.IdTheme = t.IdTheme"
					+ " ORDER BY IntituleSem",

			// 7 /* liste des séminaires ayant lieu entre deux dates */
			"SELECT s.*, t.* FROM seminaire s, theme t"
					+ " WHERE DateSem >= ? AND DateSem <= ?"
					+ " AND s.IdTheme = t.IdTheme"
					+ " ORDER BY DateSem, IntituleSem",
			// sup à la plus petite et inf à la plus grande date

			/**************************** ANIMATEURS **************************/
			// 8 /* liste de ses séminaires <=> ceux sur lesquels il intervient
			// */
			/* MODIF le 01/03*/
			"SELECT s.*, i.DureeInter, i.OrdrePassage, t.LibelleTheme"
					+ " FROM seminaire s, animateur a, intervention i, theme t"
					+ " WHERE s.IdSem = i.IdSem AND a.IdAnim = ?"
					+ " AND i.IdAnim = a.idAnim"
					+ " AND t.IdTheme = s.IdTheme"
					+ " ORDER BY s.DateSem, s.IntituleSem",
			// 9 /* liste de ses séminaires concernant un thème donné */
			/* MODIF le 01/03*/
			"SELECT s.*, i.DureeInter, i.OrdrePassage, t.LibelleTheme"
					+ " FROM seminaire s, animateur a, intervention i, theme t"
					+ " WHERE s.IdSem = i.IdSem AND i.IdAnim = a.idAnim"
					+ " AND s.IdTheme = ? AND a.IdAnim = ?"
					+ " AND s.IdTheme = t.IdTheme"
					+ " ORDER BY s.DateSem, s.IntituleSem",
			// il faudra au préalable lui avoir fait choisir un thème !!!

			// 10 /* suppression intervention de l'animateur */
					/*MODIF le 01/03*/
			"DELETE FROM intervention WHERE IdAnim = ? AND IdSem = ?",

			/*************************** ADMINISTRATEUR ***********************/
			// 11 /* créer un séminaire */
			// 
			"INSERT INTO SEMINAIRE (IdSem, DateSem, IntituleSem, PrixSem, " +
			" IdTheme, NbrePlaceMax) VALUES(seqseminaire.nextval, ?, ?, ?, ?, ?)",

			// 12 // avant de supprimer, lui afficher les séminaires
			// pareillement que pour les sociétés
			/* supprimer un séminaire */
			"DELETE FROM seminaire WHERE IdSem = ?",

			/**
			 * Faire intervenir un animateur sur un séminaire
			 */
			// lister les animateurs ainsi que les seminaires
			// au préalable (liste déroulante ou radiobuttons)
			/* enregistrer intervention d'un anim */

			// 13 // liste des anim' */
			"SELECT * FROM animateur ORDER BY NomAnim, PrenomAnim",

			// 14 // enregistrement de l'intervention */
			"INSERT INTO intervention (IdAnim, IdSem, DureeInter, OrdrePassage)"
					+ " values (?, ?, ?, ?)",
	// la date d(inscription correspond à la date système
			/**************************** générales *****************************/
			// 15 // liste des animateurs
			"SELECT * FROM animateur WHERE IdAnim = ?",
			// 16 // liste des sociétés
			"SELECT * FROM societe WHERE IdSociete = ?",
			// 17 // nombre de places déjà prises et celui des places que l'on peut prendre
			"SELECT S.IdSem, S.NbrePlaceMax, COUNT(*) as NbInterventions"
			+ " FROM INTERVENTION I, SEMINAIRE S"
			+ " WHERE S.IdSem = ?"
			+ " AND I.IdSem = S.IdSem"
			+ " GROUP BY S.IdSem, S.NbrePlaceMax",
			
			// 18 // Details d'un seminaire
			"SELECT S.*, T.LibelleTheme FROM SEMINAIRE S, THEME T WHERE S.IDSEM = ? AND S.IDTHEME = T.IDTHEME",
			
			// 19 - Selection d'une inscription pour une societe et un séminaire particuliers.
			"SELECT * FROM INSCRIPTION WHERE IDSOCIETE = ? AND IDSEM = ?",
			
			// 20 - Recherche d'un séminaire en fonction de son intitule et de sa date
			"SELECT * FROM SEMINAIRE WHERE INTITULESEM = ? AND DATESEM = ?",
			
			// 21 - Selection de tous les animateurs.
			"SELECT * FROM ANIMATEUR",
			
			// 22 - Recherche d'une intervention en fonction d'un animateur et d'un seminaire.
			"SELECT * FROM INTERVENTION WHERE IDANIM = ? AND IDSEM = ?"
	};

	private static PreparedStatement[] requetes;

	static {
		try {
			requetes = new PreparedStatement[requetesSQL.length];
			for (int i = 0; i < requetesSQL.length; i++)
				requetes[i] = BDConnexion.getConnection().prepareStatement(
						requetesSQL[i]);

		} catch (SQLException e) {
			// problème, on arrête le serveur
			System.out.println(e);
			System.exit(1);
		}
	}

	public static ResultSet executeQ(int numeroRequete, Object argument)
			throws SQLException {
		switch (numeroRequete) {
		case 0:
			return execute0(argument);
		/*case 1:
			return execute1(argument);*/
		case 2:
			return execute2(argument);
		case 3:
			return execute3(argument);
		case 4:
			return execute4(argument);
		/*case 5:
			return execute5(argument);*/
		case 6:
			return execute6(argument);
		case 7:
			return execute7(argument);
		case 8:
			return execute8(argument);
		case 9:
			return execute9(argument);
		/*case 10:
			return execute10(argument);
		case 11:
			return execute11(argument);
		case 12:
			return execute12(argument);*/
		case 13:
			return execute13(argument);
		/*case 14:
			return execute14(argument);*/
		case 15:
			return execute15(argument);
		case 16:
			return execute16(argument);
		case 17:
			return execute17(argument);
		case 18:
			return execute18(argument);
		case 19:
			return execute19(argument);
		case 20:
			return execute20(argument);
		case 21:
			return execute21(argument);
		case 22:
			return execute22(argument);
		}
		return null;
	}

	public static int executeU(int numeroRequete, Object argument)
			throws SQLException {
		switch (numeroRequete) {
		case 1:
			return execute1(argument);
		case 5:
			return execute5(argument);	
		case 10:
			return execute10(argument);
		case 11:
			return execute11(argument);
		case 12:
			return execute12(argument);
		case 14:
			return execute14(argument);
		}
		return -1;
	}
	
	// méthodes private : 1 par fonctionnalité

	private static ResultSet execute0(Object argument) throws SQLException {
		String[] args = (String[]) argument;
		String login = args[0];
		String pass = args[1];
		synchronized (requetes[0]) {
			requetes[0].setString(1, login);
			requetes[0].setString(2, pass);
			return requetes[0].executeQuery();
		}
	}

	private static int execute1(Object argument) throws SQLException {
		String[] args = (String[]) argument;
		String pass = args[0];
		String login = args[1];
		synchronized (requetes[1]) {
			requetes[1].setString(1, pass);
			requetes[1].setString(2, login);
			return requetes[1].executeUpdate();
		}
	}

	private static ResultSet execute2(Object argument) throws SQLException {
		synchronized (requetes[2]) {
			return requetes[2].executeQuery();
		}
	}

	private static ResultSet execute3(Object argument) throws SQLException {
	
		int realArg = Integer.parseInt((String)argument);
		synchronized (requetes[3]) {
			requetes[3].setInt(1, realArg);
			return requetes[3].executeQuery();
		}
	}

	private static ResultSet execute4(Object argument) throws SQLException {
		synchronized (requetes[4]) {
			return requetes[4].executeQuery();
		}
	}

	private static int execute5(Object argument) throws SQLException {
		Integer[] args = (Integer[]) argument;
		/*
		 * int idSoc = Integer.parseInt(args[0]); int idSem =
		 * Integer.parseInt(args[1]); int nbPl = Integer.parseInt(args[2]);
		 */

		synchronized (requetes[5]) {
				for (int i = 0; i < args.length; ++i) {
					int k = args[i];
					requetes[5].setInt(i + 1, k);
				}
				return requetes[5].executeUpdate();
		}
	}

	private static ResultSet execute6(Object argument) throws SQLException {
		Date date = (Date) argument;

		synchronized (requetes[6]) {
			requetes[6].setDate(1, date);
			return requetes[6].executeQuery();
		}
	}

	private static ResultSet execute7(Object argument) throws SQLException {
		Date[] argz = (Date[]) argument;
		/*
		 * Date debut = argz[0]; Date fin = argz[1];
		 */

		synchronized (requetes[7]) {
			for (int d = 0; d < argz.length; ++d)
				requetes[7].setDate(d + 1, argz[d]);
			return requetes[7].executeQuery();
		}
	}
	// MODIF
	private static ResultSet execute8(Object argument) throws SQLException {
		int idAnim = (Integer) argument;

		synchronized (requetes[8]) {
			requetes[8].setInt(1, idAnim);
			return requetes[8].executeQuery();
		}
	}
	//MODIF
	private static ResultSet execute9(Object argument) throws SQLException {
		Integer[] args = (Integer[]) argument;
		/*
		 * String idThm = args[0]; String idAnim = args[1];
		 */

		synchronized (requetes[9]) {
			for (int nbId = 0; nbId < args.length; ++nbId)
				requetes[9].setInt(nbId + 1, args[nbId]);
			return requetes[9].executeQuery();
		}
	}

	/*MODIF 01/03*/
	private static int execute10(Object argument) throws SQLException {
		Integer[] args = (Integer[]) argument;
		/*
		 * String idAnim = args[0]; String idSem = args[1];
		 */
		
		synchronized (requetes[10]) {
			for (int nbId = 0; nbId < args.length; ++nbId)
				requetes[10].setInt(nbId + 1, args[nbId]);
			return requetes[10].executeUpdate();
		}
	}

	private static int execute11(Object argument) throws SQLException {
		Object[] infoSem = (Object[]) argument;

		Date dSem = (Date) infoSem[0];
		String intitule = (String) infoSem[1];
		float px = (Float) infoSem[2];

		synchronized(requetes[11]) {
			requetes[11].setDate(1, dSem);
			requetes[11].setString(2, intitule);
			requetes[11].setFloat(3, px);

			for (int i = 3; i < infoSem.length; i++) {
				requetes[11].setInt(i + 1, (Integer) infoSem[i]);
			}
			return requetes[11].executeUpdate(); 
		}
	}

	private static int execute12(Object argument) throws SQLException {
		int idSem = (Integer) argument;

		synchronized (requetes[12]) {
			requetes[12].setInt(1, idSem);
			return requetes[12].executeUpdate();
		}
	}

	private static ResultSet execute13(Object argument) throws SQLException {
		synchronized (requetes[13]) {
			return requetes[13].executeQuery();
		}
	}

	private static int execute14(Object argument) throws SQLException {
		Integer[] args = (Integer[]) argument;

		synchronized (requetes[14]) {
			for (int i = 0; i < args.length; ++i)
				requetes[14].setInt(i + 1, args[i]);
			return requetes[14].executeUpdate();
		}
	}

	private static ResultSet execute15(Object argument) throws SQLException {
		int idAnim = (Integer) argument;
		synchronized (requetes[15]) {
			requetes[15].setInt(1, idAnim);
			return requetes[15].executeQuery();
		}
	}
	
	private static ResultSet execute16(Object argument) throws SQLException {
		int idSoc = (Integer) argument;
		//int idSoc = Integer.parseInt((String)argument);
		synchronized (requetes[16]) {
			requetes[16].setInt(1, idSoc);
			return requetes[16].executeQuery();
		}
	}
	
	private static ResultSet execute17(Object argument) throws SQLException {
		//int idSoc = (Integer) argument;
		int idSoc = Integer.parseInt((String)argument);
		synchronized (requetes[17]) {
			requetes[17].setInt(1, idSoc);
			return requetes[17].executeQuery();
		}
	}
	
	private static ResultSet execute18(Object argument) throws SQLException {
		int idSem = Integer.parseInt((String)argument);
		synchronized (requetes[18]) {
			requetes[18].setInt(1, idSem);
			return requetes[18].executeQuery();
		}
	}
	
	private static ResultSet execute19(Object argument) throws SQLException {
		Integer tInt[] = (Integer[]) argument;
		synchronized(requetes[19]) {
			for (int i = 0; i < tInt.length; i++) {
				requetes[19].setInt(i + 1, tInt[i]);
			}
			return requetes[19].executeQuery();
		}
	}
	
	private static ResultSet execute20(Object argument) throws SQLException {
		Object[] t = (Object[]) argument;
		
		String intitule = (String) t[0];
		Date date = (Date) t[1];
		synchronized(requetes[20]) {
			requetes[20].setString(1, intitule);
			requetes[20].setDate(2, date);
			return requetes[20].executeQuery();
		}
	}
	
	private static ResultSet execute21(Object argument) throws SQLException {
		synchronized(requetes[21]) {
			return requetes[21].executeQuery();
		}
	}
	
	private static ResultSet execute22(Object argument) throws SQLException {
		Integer[] t = (Integer[]) argument;
		
		synchronized(requetes[22]) {
			for (int i = 0; i < t.length; i++) {
				requetes[22].setInt(i + 1, t[i]);
			}
			return requetes[22].executeQuery();
		}
	}
} /* fin RequeteSQL */
