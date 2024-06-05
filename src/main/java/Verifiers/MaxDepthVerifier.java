package Verifiers;

import Application.ScreenStateService;

import javax.swing.*;
import java.util.Optional;
public class MaxDepthVerifier extends DomainVerifier {
	public static String defaultValue = "0";

	public MaxDepthVerifier() {
		validationRules = new ValidationRules( "maxDepth", 0.0, 20.0, Optional.empty()  );
		rowHeader = "Depth";
		columnHeader = "Depth";
	}

	@Override
	public boolean verify(JComponent xInput) {
		ScreenStateService.getCurrentPanel().invalidate();
		ScreenStateService.getCurrentPanel().repaint();

		return validationRules.doesComponentObeyRules( xInput );
	}
}



