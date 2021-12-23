package Application;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Optional;

import javax.swing.JPanel;

public class Action {
	private String actionName;
	private ScreenTransitionType screenTransitionType;
	private Optional<JPanel> nextPanel;
	
	public String getActionName() {
		return actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	public ScreenTransitionType getScreenTransitionType() {
		return screenTransitionType;
	}

	public void setScreenTransitionType(ScreenTransitionType screenTransitionType) {
		this.screenTransitionType = screenTransitionType;
	}

	public JPanel getNextPanel() {
		return nextPanel.get();
	}

	public void setNextPanel( JPanel nextPanel) {
		this.nextPanel = Optional.of( nextPanel );
	}

	public Action( String xActionName, ScreenTransitionType xScreenTransitionType,  JPanel xNextPanel) {
		actionName = xActionName;
		screenTransitionType = xScreenTransitionType;
		nextPanel = Optional.of( xNextPanel );
	}
	
	public Action( String xActionName, ScreenTransitionType xScreenTransitionType ) {
		actionName = xActionName;
		screenTransitionType = xScreenTransitionType;
		nextPanel = Optional.empty();
	}

	
	/*
	button.addActionListener( new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			ScreenStateService.refreshServer();
			if ( ScreenStateService.isServerConnected() ) {
				ScreenStateService.evaluate(
					new Action( "login", ScreenTransitionType.REPLACE, loginScreen );
			}

			pane.revalidate();
			pane.repaint( );
		}
	*/
	
}
