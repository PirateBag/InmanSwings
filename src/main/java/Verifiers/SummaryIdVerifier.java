package Verifiers;

import Application.ScreenStateService;

import javax.swing.*;
import java.util.Optional;


public class SummaryIdVerifier extends DomainVerifier {
	public static String defaultValue = "W-000";

	public SummaryIdVerifier() {
		validationRules = new ValidationRules(
				"SummaryId", 3, 10, CaseConversion.UPPER, Optional.of(defaultValue));
		rowHeader = "Summary Id";
		columnHeader = "Summary Id";
	}

	@Override
	public boolean verify(JComponent xInput) {
		ScreenStateService.getCurrentPanel().invalidate();
		ScreenStateService.getCurrentPanel().repaint();

		return validationRules.doesComponentObeyRules( xInput );
	}
}



