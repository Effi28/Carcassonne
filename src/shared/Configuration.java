package shared;

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This utility class provides paths and constants for the whole project. It shouldn't be instantiated.
 */
public final class Configuration {

	private static Logger log = LogManager.getLogger();

	public final static String SEPARATOR = File.separator;

	// regular json files
	public final static String GAMECARDSPATH = "files" + SEPARATOR + "json"
			+ SEPARATOR + "gameCards.json";

	public final static String GAMECARDSPATHINNS = "files" + SEPARATOR + "json"
			+ SEPARATOR + "gameCardsWithInns.json";

	// test json files
	public final static String BISHOPTEST = "files" + SEPARATOR + "json"
			+ SEPARATOR + "TestJsonFiles" + SEPARATOR + "BishopTest.json";

	public final static String BIGMEEPLETEST = "files" + SEPARATOR + "json"
			+ SEPARATOR + "TestJsonFiles" + SEPARATOR + "BigMeepleTest.json";

	public final static String INNSTEST = "files" + SEPARATOR + "json"
			+ SEPARATOR + "TestJsonFiles" + SEPARATOR
			+ "CathedralsAndInnsTest.json";

	public final static String MEADOWSTEST = "files" + SEPARATOR + "json"
			+ SEPARATOR + "TestJsonFiles" + SEPARATOR + "MeadowsTest.json";

	public final static int STANDARDTURNTIME = 180;

	public final static int MAXUSER = 5;

	// GameView

	public static String STARTCARD = "file:files" + SEPARATOR + "cardimages"
			+ SEPARATOR;

	public static String CARDIMAGE = "file:files" + SEPARATOR + "cardimages"
			+ SEPARATOR;

	public static String MEEPLEIMAGE = "file:files" + SEPARATOR + "meeples"
			+ SEPARATOR;

	public static String BISHOPIMAGE = "file:files" + SEPARATOR + "meeples"
			+ SEPARATOR;

	public final static String BACKSIDE = "file:files" + SEPARATOR + "gui"
			+ SEPARATOR + "backsideCard.png";

	public final static String BTNROTATEGRASS = "file:files" + SEPARATOR
			+ "gui" + SEPARATOR + "RotateButtonGrass.png";

	public final static String BTNOBSERVEWOOD = "file:files" + SEPARATOR
			+ "gui" + SEPARATOR + "BtnObserve.png";

	public final static String BTNEXITWOOD = "file:files" + SEPARATOR + "gui"
			+ SEPARATOR + "BtnExit.png";

	public final static String BTNHOSTWOOD = "file:files" + SEPARATOR + "gui"
			+ SEPARATOR + "BtnHost.png";

	public final static String BTNJOINWOOD = "file:files" + SEPARATOR + "gui"
			+ SEPARATOR + "BtnJoin.png";

	public final static String BTNSENDWOOD = "file:files" + SEPARATOR + "gui"
			+ SEPARATOR + "BtnSend.png";

	public final static String BTNSTARTWOOD = "file:files" + SEPARATOR + "gui"
			+ SEPARATOR + "BtnStart.png";

	public final static String BTNOKWOOD = "file:files" + SEPARATOR + "gui"
			+ SEPARATOR + "BtnOk.png";

	public final static String MEEPLESHADOW = "file:files" + SEPARATOR + "gui"
			+ SEPARATOR + "meeple_shadow.png";

	public final static String SHADOW = "file:files" + SEPARATOR + "gui"
			+ SEPARATOR + "shadow.png";

	public final static String POSSIBLEMOVE = "file:files" + SEPARATOR + "gui"
			+ SEPARATOR + "possibleMove.png";

	// css stylesheets

	public final static String STYLESHEETOBSERVEVIEW = "file:files" + SEPARATOR
			+ "stylesheets" + SEPARATOR + "ObserveView.css";
	public final static String STYLESHEETCHATLOBBYVIEW = "file:files"
			+ SEPARATOR + "stylesheets" + SEPARATOR + "ChatLobby.css";
	public final static String STYLESHEETLOGINVIEW = "file:files" + SEPARATOR
			+ "stylesheets" + SEPARATOR + "Login.css";
	public final static String STYLESHEETJOINGAMEVIEW = "file:files"
			+ SEPARATOR + "stylesheets" + SEPARATOR + "ChooseColor.css";
	public final static String STYLESHEETHOSTGAMEVIEW = "file:files"
			+ SEPARATOR + "stylesheets" + SEPARATOR + "HostGame.css";
	public final static String STYLESHEETGAMEVIEW = "file:files" + SEPARATOR
			+ "stylesheets" + SEPARATOR + "GameView.css";
	public final static String STYLESHEETOBSERVERVIEW = "file:files"
			+ SEPARATOR + "stylesheets" + SEPARATOR + "ObserverView.css";
	public final static String STYLESHEETSPECTATORVIEW = "file:files"
			+ SEPARATOR + "stylesheets" + SEPARATOR + "SpectatorView.css";
	public final static String STYLESHEETHELPERVIEW = "file:files" + SEPARATOR
			+ "stylesheets" + SEPARATOR + "HelperView.css";

	// AI

	public final static String AISTANDARDMESSAGE1 = "Im the artificial intelligence with the Strength ";
	public final static String AISTANDARDMESSAGE2 = " and can't answer messages.";

	private Configuration() {
		log.error("Configuration shouldn't be instantiated");
		throw new AssertionError("Configuration shouldnt be instantiated");
	}

	// design for cards
	public static void chooseCardDesign(String s) {
		if ("original design".equals(s)) {
			CARDIMAGE += "standard" + SEPARATOR;
			STARTCARD += "standard" + SEPARATOR + "D.png";
		} else if ("pirate design".equals(s)) {
			CARDIMAGE += "pirate" + SEPARATOR;
			STARTCARD += "pirate" + SEPARATOR + "D.png";
		} else if ("graveyard design".equals(s)) {
			CARDIMAGE += "graveyard" + SEPARATOR;
			STARTCARD += "graveyard" + SEPARATOR + "D.png";
		}
	}

	// design for meeples
	public static void chooseMeepleDesign(String s) {
		if ("original design".equals(s)) {
			MEEPLEIMAGE += "standard" + SEPARATOR + "meeple";
			BISHOPIMAGE += "standard_bischof" + SEPARATOR + "meeple";
		} else if ("pirate design".equals(s)) {
			MEEPLEIMAGE += "pirate" + SEPARATOR + "meeple";
			BISHOPIMAGE += "pirate_bischof" + SEPARATOR + "meeple";
		} else if ("graveyard design".equals(s)) {
			MEEPLEIMAGE += "graveyard" + SEPARATOR + "meeple";
			BISHOPIMAGE += "graveyard_bischof" + SEPARATOR + "meeple";
		}
	}
}
