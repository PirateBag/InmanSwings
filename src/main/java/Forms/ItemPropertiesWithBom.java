package Forms;

import Application.*;
import Buttons.AddButton;
import Buttons.ReportButton;
import Verifiers.*;
import com.inman.entity.ActivityState;
import com.inman.entity.BomPresent;
import com.inman.entity.Item;
import com.inman.model.request.BomSearchRequest;
import com.inman.model.request.BomUpdate;
import com.inman.model.request.ItemReportRequest;
import com.inman.model.response.BomResponse;
import com.inman.model.response.ItemExplosionResponse;
import com.inman.model.response.ItemResponse;
import com.inman.model.response.ResponsePackage;
import com.inman.model.rest.ErrorLine;
import com.inman.model.rest.ItemAddRequest;
import com.inman.model.rest.ItemUpdateRequest;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.client.RestTemplate;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Optional;

import static Application.ScreenTransitionType.POP;

public class ItemPropertiesWithBom extends InmanPanel {
    private class ItemExplosionActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            ItemExplosionResponse responsePackage = null;

            var itemReportRequest = new ItemReportRequest(  Long.parseLong(id.getText() ) );

            try {
                errorText.clearError();
                String completeUrl = "http://localhost:8080/" + ItemReportRequest.EXPLOSION_URL;
                RestTemplate restTemplate = new RestTemplate();
                responsePackage = restTemplate.postForObject(completeUrl, itemReportRequest, ItemExplosionResponse.class);
            } catch (Exception e1) {
                errorText.signalError(e1.toString());
            }

            if (responsePackage != null && responsePackage.getErrors().size() > 0) {
                errorText.signalError(( responsePackage.getErrors().get(0)).getMessage());
            }
            ScreenStateService.evaluate(new NextAction(
                    "Show Report Results",
                    ScreenTransitionType.PUSH,
                    TextResults.class,
                    responsePackage,
                    null,
                    ScreenMode.DISPLAY));
        }
    }


    protected JLabel title = Utility.titleMaker("Add Item ");
    JTextField id = Utility.createTextField("Id");
    JTextField summaryId = Utility.createTextField("Summary Id");
    JTextField description = Utility.createTextField("Description");
    JTextField unitCost = Utility.createTextField("Unit Cost");

    JTextField maxDepth = Utility.createTextField( "Max Depth" );
     protected JComboBox<String> sourcing;

    ImageIcon clearIcon = new ImageIcon("gui/icons/undo_FILL0_wght400_GRAD0_opsz24.png");
    JButton clearButton = new JButton( clearIcon);

    ImageIcon saveIcon = new ImageIcon("gui/icons/done_FILL0_wght400_GRAD0_opsz24.png");

    JButton saveButton = new JButton(saveIcon);

    Icon closeIcon = new ImageIcon("gui/icons/close_FILL0_wght400_GRAD0_opsz24.png");

    JButton cancelButton = new JButton(closeIcon);

    JButton updateButton = new JButton("Update");

    JButton addButton = new AddButton( );

    JButton itemExplosionButton = new ReportButton( "Item Explosion",
            new ItemExplosionActionListener() );

    JLabel componentsHeader;
    BomChildGrid bomChildGrid;
    NextAction action;

    BomResponse componentResponse = null;
    JTable bomChildTable;


    private class AddButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            BomPresent emptyNewElement = new BomPresent();
            emptyNewElement.setParentId( Long.valueOf( id.getText() ) );
            emptyNewElement.setParentSummary( summaryId.getText() );
            emptyNewElement.setParentDescription( description.getText() );
            emptyNewElement.setChildId( 999 );
            emptyNewElement.setQuantityPer( 1.0 );
            emptyNewElement.setChildDescription( "No child selected" );
            emptyNewElement.setChildSummary( "No child selected");
            emptyNewElement.setParentId(   Long.parseLong(id.getText()));
            emptyNewElement.setUnitCost( 0.0 );
            emptyNewElement.setActivityState( ActivityState.INSERT );

            ResponsePackage<BomPresent> responsePackage = new ResponsePackage<>();
            BomPresent[ ] newElementArray  = new BomPresent[ 1 ];
            newElementArray[ 0 ] = emptyNewElement;

            responsePackage.setData( newElementArray );
            ScreenStateService.evaluate(new NextAction(
                        "Insert Component",
                        ScreenTransitionType.PUSH,
                        BomProperties.class,
                        responsePackage,
                    null,
                    ScreenMode.ADD));
        }
    }


    private class oldUpdateButtonListener implements ActionListener  {
        @Override
        public void actionPerformed(ActionEvent e) {
            ItemResponse responsePackage = null;

            var itemUpdateRequest = new ItemUpdateRequest(
                    Long.parseLong(id.getText()), summaryId.getText(), description.getText(), Double.parseDouble(unitCost.getText()),
                    (String) sourcing.getSelectedItem());

            var errorMessages = itemUpdateRequestVerifier(itemUpdateRequest);
            if (errorMessages.isPresent()) {
                errorText.signalError(errorMessages.get());
            } else {
                try {
                    String completeUrl = "http://localhost:8080/" + ItemUpdateRequest.updateUrl;
                    RestTemplate restTemplate = new RestTemplate();
                    responsePackage = restTemplate.postForObject(completeUrl, itemUpdateRequest, ItemResponse.class);

                    var componentsToUpdate = componentResponse.getArrayOfUpdatedComponents();
                    if (componentsToUpdate.length > 0) {
                        completeUrl = "http://localhost:8080/" + BomUpdate.updateUrl;
                        componentResponse = restTemplate.postForObject(completeUrl, componentsToUpdate, BomResponse.class);
                    }
                    errorText.clearError();
                } catch (Exception e1) {
                    errorText.signalError(e1.toString());
                }
            }

            if (responsePackage != null && responsePackage.getErrors().size() > 0) {
                errorText.signalError(( responsePackage.getErrors().get(0)).getMessage());
            }
        }
    }


    public ItemPropertiesWithBom() {

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(Utility.labelMaker(" ", JLabel.TRAILING),
                BorderLayout.LINE_START);
        setBorder(Utility.blackLine);

        add(title, BorderLayout.LINE_START);

        add(Utility.labelMaker(" ", JLabel.TRAILING),
                BorderLayout.LINE_START);

        errorText.clearError();
        add(errorText);

        add(Utility.labelMaker(" ", JLabel.TRAILING),
                BorderLayout.LINE_START);

        summaryId.setInputVerifier( VerifierLibrary.idVerifier );
        add(summaryId);

        add(description);

        add(unitCost);

        sourcing = Utility.createCombobox( VerifierLibrary.sourcing);

        add(sourcing);

        add( maxDepth );

        add(Utility.labelMaker(" ", JLabel.TRAILING),
                BorderLayout.LINE_START);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        buttonPanel.add(clearButton);
        buttonPanel.add( updateButton );
        buttonPanel.add(addButton);
        buttonPanel.add( itemExplosionButton );

        add(buttonPanel);


        String[] columnNames = {
                VerifierLibrary.childIdVerifier.getColumnHeader(),
                VerifierLibrary.summaryIdVerifier.getColumnHeader(),
                VerifierLibrary.descriptionVerifier.getColumnHeader(),
                VerifierLibrary.costVerifier.getColumnHeader(),
                VerifierLibrary.quanityPerVerifier.getColumnHeader(),
                VerifierLibrary.costVerifier.getColumnHeader()
        };
        bomChildGrid = new BomChildGrid(columnNames, this  );
        bomChildTable = new JTable(bomChildGrid);

        componentsHeader = Utility.subTitleMaker("Components", JLabel.TRAILING);
        add(componentsHeader);
        add(new JScrollPane(bomChildTable));

        showComponents();
        ScreenStateService.primaryPanel.add(this);

        updateButton.addActionListener( new oldUpdateButtonListener() );
        //noinspection Convert2Lambda
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ItemResponse responsePackage = null;

                var itemAddRequest = new ItemAddRequest(
                        summaryId.getText(), description.getText(), Double.parseDouble(unitCost.getText()),
                        (String) sourcing.getSelectedItem());

                var errorMessages = itemAddRequestVerifier(itemAddRequest);
                if (errorMessages.isPresent()) {
                    errorText.signalError(errorMessages.get());
                } else {
                    try {
                        String completeUrl = "http://localhost:8080/" + ItemAddRequest.addUrl;
                        RestTemplate restTemplate = new RestTemplate();
                        responsePackage = restTemplate.postForObject(completeUrl, itemAddRequest, ItemResponse.class);
                        errorText.clearError();
                    } catch (Exception e1) {
                        errorText.signalError(e1.toString());
                    }
                }

                if ((responsePackage != null) && (responsePackage.getErrors().size() > 0)) {
                    ArrayList<ErrorLine> errorLines = responsePackage.getErrors();
                    errorText.signalError(errorLines.get(0).getMessage());
                }

                if (errorText.hasNoError()) {
                    ScreenStateService.evaluate(new NextAction(
                            "Added Item " + itemAddRequest.getSummaryId(), POP, responsePackage));
                }
            }
        });

        //noinspection Convert2Lambda
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ScreenStateService.evaluate(new NextAction(
                        "Operation cancelled", POP));
            }
        });

        //noinspection Convert2Lambda
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switch (action.getScreenMode()) {
                    case ADD:
                        setDefaultValues();
                        break;
                    case CHANGE:
                        setValuesFromAction();
                        break;
                }
            }
        });

        addButton.addActionListener( new AddButtonActionListener() );

    }

    /**
     * Set the visibility of the component list based on the existence of components.
     * Called when panel is constructed and whenever updated with components.
     */
    private void showComponents() {

        if (componentResponse == null || componentResponse.getData().length == 0) {
            componentsHeader.setText( "This Item has no components.");
            bomChildTable.setFillsViewportHeight(false);
            bomChildTable.setVisible(false);

           return;
        }
        bomChildGrid.recomputeParentUnitCost();
        componentsHeader.setText( "Components of Item.");
        bomChildTable.setFillsViewportHeight(false);
        bomChildTable.setVisible(true);
    }



    /**
     * Validate the fields of the ItemAddRequest, for use with Save button.
     *
     * @param itemAddRequest proposed ItemAddRequest
     * @return Optional error message text, or empty()
     */
    @SuppressWarnings("DuplicatedCode")
    private Optional<String> itemAddRequestVerifier(@NotNull ItemAddRequest itemAddRequest) {

        //  Multiple messages will likely be implemented as verification is extended to all fields.
        StringBuilder errorMessages = new StringBuilder();

        VerifierLibrary.summaryIdVerifier.validateValueDomain(errorMessages, itemAddRequest.getSummaryId());
        VerifierLibrary.descriptionVerifier.validateValueDomain(errorMessages, itemAddRequest.getDescription());
        VerifierLibrary.costVerifier.validateValueDomain(errorMessages, itemAddRequest.getUnitCost());
        VerifierLibrary.sourcing.validateValueDomain(errorMessages, itemAddRequest.getSourcing());

        return errorMessages.toString().length() == 0 ? Optional.empty() : Optional.of(errorMessages.toString());
    }

    /**
     * Validate the fields of the ItemUpdateRequest, for use with Update button.
     *
     * @param xItemUpdateRequest Update Request
     * @return Optional error message text, or empty()
     */
    @SuppressWarnings("DuplicatedCode")
    private Optional<String> itemUpdateRequestVerifier(@NotNull ItemUpdateRequest xItemUpdateRequest) {

        //  Multiple messages will likely be implemented as verification is extended to all fields.
        StringBuilder errorMessages = new StringBuilder();

        VerifierLibrary.summaryIdVerifier.validateValueDomain(errorMessages, xItemUpdateRequest.getSummaryId());
        VerifierLibrary.descriptionVerifier.validateValueDomain(errorMessages, xItemUpdateRequest.getDescription());
        VerifierLibrary.costVerifier.validateValueDomain(errorMessages, xItemUpdateRequest.getUnitCost());
        VerifierLibrary.sourcing.validateValueDomain(errorMessages, xItemUpdateRequest.getSourcing());


        return errorMessages.toString().length() == 0 ? Optional.empty() : Optional.of(errorMessages.toString());
    }

    public void setDefaultValues() {
        this.errorText.clearError();
        this.description.setText(DescriptionVerifier.defaultValue);
        this.summaryId.setText(SummaryIdVerifier.defaultValue);
        this.unitCost.setText(Double.toString(CostVerifier.defaultValue));
        this.sourcing.setSelectedIndex(0);
        this.maxDepth.setText( MaxDepthVerifier.defaultValue );
        this.maxDepth.setEditable( false );
    }

    @Override
    public void updateStateWhenOpeningNewChild(NextAction xAction) {
        action = xAction;
        errorText.clearError();
        switch (xAction.getScreenMode()) {
            case ADD:
                setDefaultValues();
                title.setText("Add Item");
                saveButton.setVisible(true);
                updateButton.setVisible(false);
                break;

            case CHANGE:
                setValuesFromAction();
                title.setText("Change Item Properties");

                try {
                    String completeUrl = "http://localhost:8080/" + BomSearchRequest.findByParent;
                    RestTemplate restTemplate = new RestTemplate();
                    BomSearchRequest bomSearchRequest = new BomSearchRequest();
                    bomSearchRequest.setIdToSearchFor(Long.parseLong(id.getText()));

                    componentResponse = restTemplate.postForObject(completeUrl, bomSearchRequest, BomResponse.class);
                    errorText.clearError();

                } catch (Exception e1) {
                    errorText.signalError(e1.toString());
                }
                bomChildGrid.initializeRows(componentResponse.getData());
                showComponents();
                saveButton.setVisible( true );
                updateButton.setVisible(true);
                break;
        }

    }

    private void setValuesFromAction() {
        Item[] items = (Item[]) action.getResponsePackage().getData();
        Item itemToBeModified = null;
        for (var item : items) {
            if (item.getId() == action.getIdsToActOn()[0]) {
                itemToBeModified = item;
                break;
            }
        }

        if (itemToBeModified != null) {
            id.setText(Long.toString(itemToBeModified.getId()));
            summaryId.setText(itemToBeModified.getSummaryId());
            description.setText(itemToBeModified.getDescription());
            unitCost.setText(Double.toString(itemToBeModified.getUnitCost()));
            maxDepth.setText( Integer.toString( itemToBeModified.getMaxDepth() ));
            maxDepth.setEditable( false );
            for (int i = 0; i < VerifierLibrary.sourcing.getValidationRules().values.length; i++) {
                if (itemToBeModified.getSourcing().equals( VerifierLibrary.sourcing.getValidationRules().values[i])) {
                    sourcing.setSelectedIndex(i);
                    break;
                }
            }
            id.setVisible(true);
        } else {
            id.setVisible(false);
        }
    }



}
