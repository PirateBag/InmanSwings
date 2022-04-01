package Application;

import com.inman.model.rest.ResponsePackage;

import javax.swing.*;
import java.util.Optional;

public class Action {
	private String actionName;
	private ScreenTransitionType screenTransitionType;
	private Optional<JPanel> nextPanel;
	private ResponsePackage<?> responsePackage;
	private Long[] idsToActOn;
	private ScreenMode screenMode;
	
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


	public Action( String xActionName, ScreenTransitionType xScreenTransitionType, ResponsePackage<?> xResponsePackage ) {
		actionName = xActionName;
		screenTransitionType = xScreenTransitionType;
		responsePackage = xResponsePackage;
		nextPanel = Optional.empty();
	}
	public Action(String xActionName, ScreenTransitionType xScreenTransitionType, JPanel xNextPanel,
				  ResponsePackage<?> xResponsePackage)
	{
		actionName = xActionName;
		screenTransitionType = xScreenTransitionType;
		nextPanel = Optional.of( xNextPanel );
		responsePackage = xResponsePackage;
	}


	public Action(String xActionName, ScreenTransitionType xScreenTransitionType, JPanel xNextPanel,
				  ResponsePackage<?> xResponsePackage, Long [] xIdsToActOn, ScreenMode xScreenMode ) {
		actionName = xActionName;
		screenTransitionType = xScreenTransitionType;
		nextPanel = Optional.of( xNextPanel );
		responsePackage = xResponsePackage;
		idsToActOn = xIdsToActOn;
		screenMode = xScreenMode;
	}

	public void setNextPanel(Optional<JPanel> nextPanel) {
		this.nextPanel = nextPanel;
	}

	public ResponsePackage<?> getResponsePackage() {
		return responsePackage;
	}

	public void setResponsePackage(ResponsePackage<?> responsePackage) {
		this.responsePackage = responsePackage;
	}

	public Long[] getIdsToActOn() {
		return idsToActOn;
	}

	public void setIdsToActOn(Long[] idsToActOn) {
		this.idsToActOn = idsToActOn;
	}

	public ScreenMode getScreenMode() {
		return screenMode;
	}

	public void setScreenMode(ScreenMode screenMode) {
		this.screenMode = screenMode;
	}
}
