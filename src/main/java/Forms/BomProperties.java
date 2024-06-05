package Forms;

import Application.*;
import Buttons.ClearButton;
import Buttons.DoneButton;
import ISwing.IComboBox;
import Verifiers.VerifierLibrary;
import com.inman.entity.BomPresent;
import com.inman.lists.Items;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Optional;


public class BomProperties extends InmanPanel {
    static Logger logger = LoggerFactory.getLogger( "controller: " + BomProperties.class.getName() );
   BomPresent bomPresent= new BomPresent();
    Items itemPickList = new Items ();

    protected JLabel title = Utility.titleMaker("Where Used Properties ");

    IComboBox parentDescription = new IComboBox( VerifierLibrary.descriptionVerifier, Optional.of("Parent" ) );
    IComboBox childDescription = new IComboBox( VerifierLibrary.descriptionVerifier, Optional.of( "Child" ) );

    JTextField quantityPer = VerifierLibrary.quanityPerVerifier.getJTextField();

    JButton clearButton = new ClearButton( "Cancel changes and stay" );

    JButton saveButton = new DoneButton();
    JButton doneButton = new DoneButton();

    private class DoneButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            ScreenStateService.evaluate(new NextAction(
                    "Return",
                    ScreenTransitionType.POP,
                    BomProperties.class,
                    null,
                    null,
                    ScreenMode.CHANGE));
        }
    }

    public BomProperties() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(Utility.labelMaker(" ", JLabel.TRAILING),
                BorderLayout.LINE_START);
        setBorder(Utility.blackLine);

        add(title, BorderLayout.LINE_START);

        add(Utility.labelMaker(" ", JLabel.TRAILING),
                BorderLayout.LINE_START);


        errorText.clearError();
        add(errorText);

        add( parentDescription );
        add( childDescription );

        add( quantityPer );

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        saveButton.addActionListener( new DoneButtonListener() );
        clearButton.addActionListener( new DoneButtonListener() );
        buttonPanel.add(saveButton);
        buttonPanel.add(clearButton);
        add(buttonPanel);

    }

    public void updateStateWhenOpeningNewChild(NextAction action ) {
        itemPickList.refreshFromServer();
        bomPresent = (BomPresent) action.getResponsePackage().getData()[ 0 ];
        bomPresentToFields();
       }

    private void bomPresentToFields() {
        int index ;
        parentDescription.refresh( itemPickList );
        index = itemPickList.getIndexByid( bomPresent.getParentId() );
        parentDescription.setSelectedIndex( index ) );
        childDescription.refresh( itemPickList );
        index = itemPickList.getIndexByid( bomPresent.getChildId() );
        childDescription.setSelectedIndex( index )itemPickList.getIndexByid( bomPresent.getChildId() ))==-1 ? 0 : index   );;
        logger.info( "indexOfChildId is " + index );
        quantityPer.setText(String.valueOf( bomPresent.getQuantityPer()));
        errorText.clearError();
        this.invalidate();
    }
}