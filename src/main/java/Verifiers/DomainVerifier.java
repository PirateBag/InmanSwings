package Verifiers;

import Application.ScreenStateService;
import Application.Utility;

import javax.swing.*;

public class DomainVerifier extends InputVerifier {

    ValidationRules validationRules;

    public String getColumnHeader() {
        return columnHeader;
    }

    public String getRowHeader() {
        return rowHeader;
    }

    protected  String columnHeader;
    protected  String rowHeader;

    @Override
    public boolean verify(JComponent xInput) {
        ScreenStateService.getCurrentPanel().invalidate();
        ScreenStateService.getCurrentPanel().repaint();

        return validationRules.doesComponentObeyRules( xInput );
    }


    /**
     * Verify that a string matches the verification rules of the specified domain.
     * Side effect:  add new verification to errorMessages
     * @param errorMessages
     * @param xDomainVerifier
     * @param xValueToBeVerified
     */
    public void validateValueDomain(StringBuilder errorMessages, String xValueToBeVerified ) {
        var verifierResult = validationRules.applyRulesToStringValue(xValueToBeVerified);
        if (verifierResult.isPresent()) {
            errorMessages.append(verifierResult.get());
        }
    }
    public void validateValueDomain(StringBuilder errorMessages, Double xValueToBeVerified ) {
        var verifierResult = validationRules.applyRulesToDoubleValue(xValueToBeVerified);
        if (verifierResult.isPresent()) {
            errorMessages.append(verifierResult.get());
        }
    }

    public JTextField getJTextField () {
        return Utility.createTextField(rowHeader);
    }

    public ValidationRules getValidationRules() {
        return validationRules;
    }
    public void setValidationRules( ValidationRules xValidationRules ) {
        validationRules = xValidationRules;
    }
}
