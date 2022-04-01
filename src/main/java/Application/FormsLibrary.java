package Application;

import Forms.*;

import javax.swing.*;

public class FormsLibrary {
	private static JPanel startServer;
	private static JPanel login;
	private static JPanel empty;
	private static JPanel itemQuery;
	private static JPanel itemProperties;

	public static JPanel getLogin() {
		if ( null == login ) {
			login = new Login();

		}
		return login;
	}

	public static JPanel getStartServer( ) {
		if (startServer == null) {
			startServer = new StartServer();
		}
		return startServer;
	}

	
	public static JPanel getEmpty( ) {
		if ( empty == null ) {
			empty = new Empty();
		}
		return empty;
	}

	public static JPanel getItemQuery() {
		if ( itemQuery == null ) {
			itemQuery = new ItemQuery();
		}
		return itemQuery;
	}

	public static JPanel getItemProperties() {
		if ( itemProperties == null ) {
			itemProperties = new ItemProperties();
		}
		return itemProperties;
	}
}
