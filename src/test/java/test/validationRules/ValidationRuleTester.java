package test.validationRules;

import Verifiers.CaseConversion;
import Verifiers.ValidationRules;
import com.inman.entity.Item;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ValidationRuleTester {
    ValidationRules validationRules;

    @Before
    public void setUp() {
        validationRules = new ValidationRules(
                "Sourcing", new String [] { Item.SOURCE_PUR, Item.SOURCE_MAN }, 3, 3, CaseConversion.UPPER,
                "   " );
    }

    @Test
    public void optimist() {
        Optional<String> actual = validationRules.doesStringComplyWithRules( "PUR" );
        assertTrue( actual.isEmpty() );
    }

    @Test
    public void emptyValue() {
        var expected = "'Sourcing' must be at least 3 characters in length.\n" +
                        "'Sourcing' should be one of the following:  {PUR,MAN}";
        Optional<String> actual = validationRules.doesStringComplyWithRules( "" );

        assertTrue( actual.isPresent() );
        assertEquals( expected, actual.get() );

    }

    @Test
    public void notOneOfTheValidValues() {
        var expected =  "'Sourcing' should be one of the following:  {PUR,MAN}";

        Optional<String> actual = validationRules.doesStringComplyWithRules( "abc" );

        assertTrue( actual.isPresent() );
        assertEquals( expected, actual.get() );
    }
}
