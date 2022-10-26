package Verifiers;

import Application.ErrorText;
import Application.Utility;

import javax.swing.*;
import java.util.Optional;

public class ValidationRules {
    private int minLength = 0 ;
    private int maxLength = 0;
    private CaseConversion caseConversion = CaseConversion.NONE;
    private String fieldName;
    private Double minValue = -Double.MAX_VALUE;
    private Double maxValue = Double.MAX_VALUE;;
    Class type = Object.class;
    private Optional<Object> preventThisValue = Optional.empty();
    public Object [] values;


    public ValidationRules( String xFieldName, int xMinLength, int xMaxLength, CaseConversion xCaseConversion, Optional<Object> xPreventThisValue ) {
        fieldName = xFieldName;
        minLength = xMinLength;
        maxLength = xMaxLength;

        caseConversion = xCaseConversion;
        type = String.class;
        preventThisValue = xPreventThisValue;
    }

    public ValidationRules(String xFieldName, Double xMinValue, Double xMaxValue, Optional<Object> xPreventThisValue ) {
        fieldName = xFieldName;
        minValue = xMinValue;
        maxValue = xMaxValue;
        type = Double.class;
        preventThisValue = xPreventThisValue;
    }

    public ValidationRules(String xFieldName, String [] xValues, int xMinLength, int xMaxLength, CaseConversion xCaseConversion, String xPreventThisValue  ) {
        fieldName = xFieldName;
        values = xValues;
        minLength = xMinLength;
        maxLength = xMaxLength;
        caseConversion = xCaseConversion;
        values = xValues;
        minValue = null;
        maxValue = null;
        type = String.class;
        preventThisValue = Optional.of (xPreventThisValue );
    }


    /**
     * Enforce rules on a JComponent.  As a side effect, sets the text of any error essages.
     * @param xComponent
     * @return true if complies with rules.  False also search for and sets error message object.
     */
    public  boolean doesComponentObeyRules(JComponent xComponent ) {
        if (Double.class.equals(type)) {
            return enforceDouble( xComponent );
        } else if (String.class.equals(type)) {
            return doesStringComplyWithRules( xComponent );
        } else {
            throw new RuntimeException( "Cannot enforce rules on this type.");
        }
    }


    /**
     * Validates the text of a presumed component against validation rules.
     * Does not modify the components.
     * @param xTextValueOfField
     * @return empty() when no violations.  Otherwise text of violation.
     */
    public Optional<String> applyRulesToStringValue(String xTextValueOfField )  {
        StringBuilder errorMessage = new StringBuilder();
        String reformatedTextOfField = reformatStringUsingRules( xTextValueOfField  );

        if (reformatedTextOfField.length() < minLength) {
            errorMessage.append( String.format("'%s' must be at least %d characters in length.\n", fieldName, minLength ) );
        } else if ( reformatedTextOfField.length() > maxLength) {
            errorMessage.append( String.format("'%s' must not exceed %d characters in length.\n", fieldName, maxLength ) );
        }
        if ( preventThisValue.isPresent() && reformatedTextOfField.equals( preventThisValue.get() ) ) {
            errorMessage.append( String.format("Please enter a value for '%s', the default is not sufficient.\n", fieldName ) );
        } else {
            /*  Did the creator provide a list of allowed values, and is the input in that list.  */
            if (this.values != null) {
                var oneMatch = false;
                for (Object string : values) {

                    if (string.equals(reformatedTextOfField)) {
                        oneMatch = true;
                        break;
                    }
                }
                if (!oneMatch) {
                    errorMessage.append(String.format("'%s' should be one of the following:  {%s}",
                            fieldName, getListOfPermittedValues()));
                }
            }
        }

        if ( Utility.isEmptyOrNull( errorMessage )) {
            return Optional.empty();
        }
        return Optional.of( errorMessage.toString() );
    }


    /**
     * Enforce validation rules for String data type.  As a side effect, set any on screen error fields.
     * @param xValue containing string to be checked.
     * @return Optional.empty() if no error detected, otherwise a String message with the error.
     */
    public Optional<String> doesStringComplyWithRules(String xValue) {

        var valueAfterReformat = this.reformatStringUsingRules( xValue.trim() );
        var resultOfVerification = this.applyRulesToStringValue( xValue.trim() );

        return resultOfVerification;
    }


    /**
     * Enforce validation rules for String data type.  As a side effect, set any on screen error fields.
     * @param xComponent containing string to be checked.
     * @return true if no error detected, otherwise false.
     */
    private boolean doesStringComplyWithRules(JComponent xComponent) {
        String value = ((JTextField) xComponent).getText().trim();

        var valueAfterReformat = this.reformatStringUsingRules( value );
        var resultOfVerification = this.applyRulesToStringValue( value );

        if ( resultOfVerification.isPresent() ) {
            ErrorText errorText = VerifierUtility.findErrorTextAmongPeers(xComponent);
            errorText.signalError( resultOfVerification.get() );
            return false;
        }

        ((JTextField) xComponent).setText( valueAfterReformat );
        return true;
    }

    private boolean enforceDouble(JComponent xComponent) {
        Double value = Double.valueOf( ((JTextField) xComponent).getText());
        var errorString = applyRulesToDoubleValue( value );

        if ( errorString.isPresent() ) {
            ErrorText errorText = VerifierUtility.findErrorTextAmongPeers(xComponent);
            errorText.signalError( errorString.get() );
            return false;
        }

        //  Perhaps reformat as currency...
        //  ((JTextField) xComponent).setText( rValue );
        return true;
    }
    public Optional<String> applyRulesToDoubleValue( Double value ) {
        Optional<String> errorString = Optional.empty();
        if ( value < minValue ) {
            errorString = Optional.of( String.format("'%s' must be greater than or equal to %f.", fieldName, minValue ) );
        } else if ( value > maxValue) {
            errorString = Optional.of( String.format("'%s' must not exceed %f.", fieldName, maxValue ) );
        }
        return errorString;
    }



    /**
     * Reformat the input string based on validation rules.  Method does not enforce any rules.
     * @param xValue
     * @return reformatted value.
     */
    public String reformatStringUsingRules(String xValue ) {
        String reformatedTextOfField = xValue;
        switch (caseConversion) {
            case NONE:
                break;
            case LOWER:
                reformatedTextOfField = xValue.toLowerCase().trim();
                break;
            case UPPER:
                reformatedTextOfField = xValue.toUpperCase().trim();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + caseConversion);
        }
        return reformatedTextOfField;
    }

    /**
     * Return a comma seperated list of permitted values.
     *
     * @return list of values, or if no values or not a String values, returns null.
     */
    public String getListOfPermittedValues() {

        if ( values == null || this.type != String.class ) { return null;  }
        var returnValue = "";

        for ( String value : (String[]) values ) {
            returnValue += value + ",";
        }
        returnValue = Utility.removeLastChar(returnValue);
        return returnValue;
    }

    public String[] valuesAsString() {
        return (String[]) values;
    }
}
