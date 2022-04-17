package Application;

import com.inman.model.response.ResponsePackage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;

public class NextAction {
	private String actionName;
	private ScreenTransitionType screenTransitionType;
	private ResponsePackage responsePackage;
	private Long[] idsToActOn;
	private ScreenMode screenMode;
	private Class nextPanelClass;

	public String getActionName() {
		return actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	public ScreenTransitionType getScreenTransitionType() {
		return screenTransitionType;
	}

	Logger logger = LoggerFactory.getLogger(NextAction.class);

	public void setScreenTransitionType(ScreenTransitionType screenTransitionType) {
		this.screenTransitionType = screenTransitionType;
	}

	public JPanel getNextPanel() {
		JPanel rValue;
		try {
			 rValue = (JPanel) nextPanelClass.getDeclaredConstructor().newInstance();
		} catch ( NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException  e) {
			logger.error( "Unable to find form " + e.getMessage() );
			return new Forms.Error( e );
		}
		return rValue;
	}


	public NextAction(String xActionName, ScreenTransitionType xScreenTransitionType ) {
		actionName = xActionName;
		screenTransitionType = xScreenTransitionType;
	}


	public NextAction(String xActionName, ResponsePackage xResponsePackage ) {
		actionName = xActionName;
		screenTransitionType = ScreenTransitionType.POP;
		responsePackage = xResponsePackage;
	}

	public NextAction(String xActionName, ScreenTransitionType xScreenTransitionType, Class xNextPanelClass )
	{
		actionName = xActionName;
		screenTransitionType = xScreenTransitionType;
		nextPanelClass = xNextPanelClass;
	}

	public NextAction(String xActionName, ScreenTransitionType xScreenTransitionType, Class xNextPanelClass,
					  ResponsePackage xResponsePackage, Long [] xIdsToActOn, ScreenMode xScreenMode ) {
		actionName = xActionName;
		screenTransitionType = xScreenTransitionType;
		nextPanelClass = xNextPanelClass;
		responsePackage = xResponsePackage;
		idsToActOn = xIdsToActOn;
		screenMode = xScreenMode;
	}

	public ResponsePackage getResponsePackage() {
		return responsePackage;
	}

	public void setResponsePackage(ResponsePackage responsePackage) {
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
