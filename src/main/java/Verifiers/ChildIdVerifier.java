package Verifiers;

import Application.ScreenStateService;

import javax.swing.*;


public class ChildIdVerifier extends IdVerifier {
	public ChildIdVerifier() {
		rowHeader = "Child";
		columnHeader = "Child";
	}

	@Override
	public boolean verify(JComponent xInput) {
		ScreenStateService.getCurrentPanel().invalidate();
		ScreenStateService.getCurrentPanel().repaint();

		return validationRules.doesComponentObeyRules( xInput );
	}
}



