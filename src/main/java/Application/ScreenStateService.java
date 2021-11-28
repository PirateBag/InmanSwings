package Application;

import java.util.Optional;

import dto.User;


public class ScreenStateService {
	static boolean isServerConnected;
	static Optional<User> currentUser;
	
	/** 
	 * Check to see if we able to connect to an Inman server.
	 */
	public static void refreshServer() {
		isServerConnected = Utility.isServiceUp();
	}
	
	public static boolean isServerConnected() {
		return isServerConnected;
	}

	public void setServerConnected(boolean isServerConnected) {
		ScreenStateService.isServerConnected = isServerConnected;
	}

	public Optional<User> getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(Optional<User> currentUser) {
		ScreenStateService.currentUser = currentUser;
	}
}
