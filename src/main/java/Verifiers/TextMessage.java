package Verifiers;

import Application.ScreenStateService;

import javax.swing.*;
import java.util.Optional;

public class TextMessage extends DomainVerifier {
	public static String defaultValue = "";

	public TextMessage() {
		validationRules = new ValidationRules(
				"Text", 1, 70, CaseConversion.NONE, Optional.of((Object) defaultValue));
		rowHeader = "Text";
		columnHeader = "Text";
	}

	@Override
	public boolean verify(JComponent input) {
		ScreenStateService.getCurrentPanel().invalidate();
		ScreenStateService.getCurrentPanel().repaint();
		return validationRules.doesComponentObeyRules( input );
	}
}



