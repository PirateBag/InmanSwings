package Verifiers;

import Application.ScreenStateService;

import javax.swing.*;
import java.util.Optional;

public class DescriptionVerifier extends DomainVerifier {
	public static String defaultValue = "";

	public DescriptionVerifier() {
		validationRules = new ValidationRules(
				"Description", 1, 30, CaseConversion.NONE, Optional.of((Object) defaultValue));
		rowHeader = "Description";
		columnHeader = "Description";
	}

	@Override
	public boolean verify(JComponent input) {
		ScreenStateService.getCurrentPanel().invalidate();
		ScreenStateService.getCurrentPanel().repaint();
		return validationRules.doesComponentObeyRules( input );
	}
}



