package Verifiers;

import Application.ScreenStateService;
import Application.Utility;

import javax.swing.*;
import java.util.Optional;


public class IdVerifier extends DomainVerifier {
	public static String defaultValue = "";


	public IdVerifier() {
		validationRules = new ValidationRules(
				"SummaryId", 3, 10, CaseConversion.UPPER, Optional.of(defaultValue));
		rowHeader = "Id";
		columnHeader = "Id";

	}

	@Override
	public boolean verify(JComponent xInput) {
		ScreenStateService.getCurrentPanel().invalidate();
		ScreenStateService.getCurrentPanel().repaint();

		return validationRules.doesComponentObeyRules( xInput );
	}
}



