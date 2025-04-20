package Verifiers;

import Application.ScreenStateService;

import javax.swing.*;
import java.util.Optional;

public class QuanityPerVerifier extends DomainVerifier {
	public static Double defaultValue = 1.0;

	public QuanityPerVerifier() {
		validationRules = new ValidationRules(
		"Quantity Per", (Double) 0.0, Double.MAX_VALUE, Optional.of( (Object) defaultValue )  );
		columnHeader = "Qty Per";
		rowHeader = "Quantity Per";

	}

	@Override
	public JTextField getJTextField() {
		JTextField newTextField = super.getJTextField();
		newTextField.setText( Double.toString( defaultValue ) );
		return newTextField;
	}

	@Override
	public boolean verify(JComponent input) {
		ScreenStateService.getCurrentPanel().invalidate();
		ScreenStateService.getCurrentPanel().repaint();
		return validationRules.doesComponentObeyRules( input );
	}
}



