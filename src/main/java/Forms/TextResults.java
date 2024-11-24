package Forms;

import Application.InmanPanel;
import Application.NextAction;
import Application.Utility;
import Buttons.DoneButton;
import Verifiers.VerifierLibrary;
import com.inman.entity.Text;
import com.inman.model.response.ItemExplosionResponse;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;


public class TextResults extends InmanPanel {
    ItemExplosionResponse itemExplosionResponse = new ItemExplosionResponse();
    DefaultTableModel queryResultsTableModel = new DefaultTableModel();
    JLabel reportLabel;
    JTable queryResultsTable;

    class LeftTableCellRenderer extends DefaultTableCellRenderer {
        protected LeftTableCellRenderer()
        {
            setHorizontalAlignment( JLabel.LEFT );
        }
    }
    class RightTableCellRenderer extends DefaultTableCellRenderer {
        protected RightTableCellRenderer()
        {
            setHorizontalAlignment( JLabel.RIGHT );
            setFont( Utility.monoFont );
        }
    }
    public TextResults() {

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(Utility.titleMaker("Item Explosion Report"), BorderLayout.PAGE_START);
        add(Utility.labelMaker(" ", JLabel.TRAILING),
                BorderLayout.LINE_START);

        JLabel errorMessage = Utility.createErrorMessage(JLabel.TRAILING);
        add(errorMessage);

        add(Utility.labelMaker(" ", JLabel.TRAILING),
                BorderLayout.LINE_START);

        add(VerifierLibrary.idVerifier.getJTextField("Number"));
        add(VerifierLibrary.textVerifier.getJTextField("Value"));


        add( (reportLabel = Utility.labelMaker(" ", JLabel.TRAILING) ),
                BorderLayout.LINE_START);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.add( new DoneButton() );
        add( buttonPanel );

        add( (reportLabel = Utility.labelMaker(" ", JLabel.TRAILING) ),
                BorderLayout.LINE_START);

        /*  A 1 column table.  */
        queryResultsTable = new JTable( queryResultsTableModel );
        queryResultsTableModel.addColumn( "Report" );
        queryResultsTable.getColumnModel().getColumn( 0).setCellRenderer( new LeftTableCellRenderer() );

        queryResultsTable.setFillsViewportHeight(true);
        add(new JScrollPane(queryResultsTable));
    }

    /**
     * Create the table model based on response package.
     * No values are returned; side effect is the changes to the table model.
     */
    private void createTableModelFromResponsePackage() {

        var numRows = itemExplosionResponse.getData().length;
        Text text;

        for (int row = 0; row < numRows; row++) {
            text = (Text) itemExplosionResponse.getData()[row];
            queryResultsTableModel.insertRow(row, new Object[]{text.getMessage()});

    }
        }

    @Override
    /**
     * Update the items associated with this screen based on the server response from the prior screen (ItemProperties).
     *
     * The updated or inserted item is added to the table model.
     *
     */
    public void updateStateWhenChildCloses(NextAction xAction) {
        /*  No response package may mean the cancel button was pushed.  */
        if (xAction.getResponsePackage() == null) {
            return;
        }
    }
    /**
     * Set the visibility of the component list based on the existence of components.
     * Called when panel is constructed and whenever updated with components.
     */
    private void showComponents() {

        if (itemExplosionResponse == null || itemExplosionResponse.getData().length == 0) {
            reportLabel.setText( "This Item has no components.");
            queryResultsTable.setFillsViewportHeight(false);
            queryResultsTable.setVisible(false);

            return;
        }
        queryResultsTable.setFillsViewportHeight(false);
        queryResultsTable.setVisible(true);
    }


    @Override
    public void updateStateWhenOpeningNewChild(NextAction xAction) {
        errorText.clearError();
        switch (xAction.getScreenMode()) {
            case DISPLAY:
                this.itemExplosionResponse = (ItemExplosionResponse) xAction.getResponsePackage();
                createTableModelFromResponsePackage();
                showComponents();
                break;
        }

    }

}
