package ISwing;

import Application.Utility;
import Verifiers.DomainVerifier;
import com.inman.lists.Items;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.util.Optional;

public class IComboBox extends JComboBox {
    DomainVerifier verifier;
    static Logger logger = LoggerFactory.getLogger( IComboBox.class);

    public IComboBox(DomainVerifier verifier, Optional<String> label ) {
        this.verifier = verifier;
        var validationRules = verifier.getValidationRules();
        if (validationRules.values != null) {
            for (Object object : validationRules.values) {
                this.addItem(object);
            }
        }

        var titledBorder = BorderFactory.createTitledBorder(Utility.blackLine,
                label.orElseGet(verifier::getColumnHeader));
        titledBorder.setTitleFont(Utility.labelFont);
        setFont(Utility.textFont);
        setMaximumSize(new Dimension(400, 35));
        setBorder(titledBorder);
    }



    public void refresh( Items items ) {
        for (String item : items.toStringArray() ) {
            this.addItem(item);
        }
    }
}
