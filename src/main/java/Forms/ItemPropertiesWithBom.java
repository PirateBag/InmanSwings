package Forms;

import Application.*;
import Verifiers.*;
import com.inman.entity.Item;
import com.inman.model.request.BomSearchRequest;
import com.inman.model.response.BomPresentResponse;
import com.inman.model.response.ItemResponse;
import com.inman.model.rest.ErrorLine;
import com.inman.model.rest.ItemAddRequest;
import com.inman.model.rest.ItemUpdateRequest;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.client.RestTemplate;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Optional;

public class ItemPropertiesWithBom extends InmanPanel {
    JLabel title = Utility.titleMaker("Add Item ");
    JTextField id = Utility.createTextField("Id");
    JTextField summaryId = Utility.createTextField("Summary Id");
    JTextField description = Utility.createTextField("Description");
    JTextField unitCost = Utility.createTextField("Unit Cost");
    JComboBox sourcing;

    JButton clearButton = new JButton("Clear");
    JButton saveButton = new JButton("Save");
    JButton cancelButton = new JButton("Cancel & Return");
    JButton updateButton = new JButton("Update");
    NextAction action;

    IdVerifier idVerifier = new IdVerifier();
    SummaryIdVerifier summaryIdVerifier = new SummaryIdVerifier();
    DescriptionVerifier descriptionVerifier = new DescriptionVerifier();
    CostVerifier costVerifier = new CostVerifier();
    Sourcing sourcingVerifier = new Verifiers.Sourcing();

    ParentIdVerifier parentIdVerifier = new ParentIdVerifier();
    ChildIdVerifier childIdVerifier = new ChildIdVerifier();
    QuantityPer quantityPer = new QuantityPer();

    DefaultTableModel tableModel = new DefaultTableModel();

    BomPresentResponse responsePackage = new BomPresentResponse();


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

        summaryId.setInputVerifier( summaryIdVerifier );
        add(summaryId);

        add(description);

        add(unitCost);

        sourcing = sourcingVerifier.renderComponent();

        add(sourcing);

        add(Utility.labelMaker(" ", JLabel.TRAILING),
                BorderLayout.LINE_START);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(updateButton);
        add(buttonPanel);

        add(Utility.labelMaker("Components", JLabel.TRAILING),
                BorderLayout.LINE_START);


        JTable queryResults = new JTable(tableModel);
        String[] columnNames = {
                childIdVerifier.getColumnHeader(),
                summaryIdVerifier.getColumnHeader(),
                descriptionVerifier.getColumnHeader(),
                quantityPer.getColumnHeader(),
                sourcingVerifier.getColumnHeader()
        };

        for (String columnName : columnNames) {
            tableModel.addColumn(columnName);
        }
        queryResults.setFillsViewportHeight(true);
        add(new JScrollPane(queryResults));


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
                    errorText.signalError( errorLines.get(0).getMessage() );
                }

                if (errorText.hasNoError()) {
                    ScreenStateService.evaluate(new NextAction(
                            "Added Item " + itemAddRequest.getSummaryId(), responsePackage ) );
                }
            }
        });

        //noinspection Convert2Lambda
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ItemResponse responsePackage = null;

                var itemUpdateRequest = new ItemUpdateRequest(
                        Long.parseLong(id.getText()), summaryId.getText(), description.getText(), Double.parseDouble(unitCost.getText()),
                        (String) sourcing.getSelectedItem());

                var errorMessages = itemUpdateRequestVerifier( itemUpdateRequest);
                if (errorMessages.isPresent()) {
                    errorText.signalError(errorMessages.get());
                } else {
                    try {
                        String completeUrl = "http://localhost:8080/" + ItemUpdateRequest.updateUrl;
                        RestTemplate restTemplate = new RestTemplate();
                        responsePackage = restTemplate.postForObject(completeUrl, itemUpdateRequest, ItemResponse.class);
                        errorText.clearError();
                    } catch (Exception e1) {
                        errorText.signalError(e1.toString());
                    }
                }

                if (responsePackage != null && responsePackage.getErrors().size() > 0) {
                    errorText.signalError(((ErrorLine) responsePackage.getErrors().get( 0 )).getMessage());
                }

                if (errorText.hasNoError()) {
                    ScreenStateService.evaluate(new NextAction(
                            "Updated Item " + itemUpdateRequest.getSummaryId(), responsePackage ));
                }
            }
        });


        //noinspection Convert2Lambda
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ScreenStateService.evaluate(new NextAction(
                        "Operation cancelled", ScreenTransitionType.POP));
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

        ScreenStateService.primaryPanel.add(this);
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

        summaryIdVerifier.validateValueDomain( errorMessages, itemAddRequest.getSummaryId() );
        descriptionVerifier.validateValueDomain( errorMessages, itemAddRequest.getDescription() );
        costVerifier.validateValueDomain( errorMessages, itemAddRequest.getUnitCost());
        sourcingVerifier.validateValueDomain( errorMessages,itemAddRequest.getSourcing() );

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

        summaryIdVerifier.validateValueDomain( errorMessages, xItemUpdateRequest.getSummaryId() );
        descriptionVerifier.validateValueDomain( errorMessages, xItemUpdateRequest.getDescription() );
        costVerifier.validateValueDomain( errorMessages, xItemUpdateRequest.getUnitCost());
        sourcingVerifier.validateValueDomain( errorMessages, xItemUpdateRequest.getSourcing() );

        return errorMessages.toString().length() == 0 ? Optional.empty() : Optional.of(errorMessages.toString());
    }

    public void setDefaultValues() {
        this.errorText.clearError();
        this.description.setText(DescriptionVerifier.defaultValue);
        this.summaryId.setText(SummaryIdVerifier.defaultValue);
        this.unitCost.setText(Double.toString(CostVerifier.defaultValue));
        this.sourcing.setSelectedIndex( 0 );
    }

    @Override
    public void updateStateWhenOpeningNewChild(NextAction xAction) {
        action = xAction;
        errorText.clearError();
        switch (xAction.getScreenMode()) {
            case ADD:
                setDefaultValues();
                clearButton.setText("Clear");
                title.setText("Add Item");
                saveButton.setVisible( true );
                updateButton.setVisible( false );
                break;

            case CHANGE:
                setValuesFromAction();
                clearButton.setText("Undo");
                title.setText("Change Item Properties");

                try {
                    String completeUrl = "http://localhost:8080/" + BomSearchRequest.findByParent;
                    RestTemplate restTemplate = new RestTemplate();
                    BomSearchRequest bomSearchRequest = new BomSearchRequest();
                    bomSearchRequest.setIdToSearchFor( Long.parseLong( id.getText() ) );
                    responsePackage = restTemplate.postForObject(completeUrl, bomSearchRequest, BomPresentResponse.class);
                    responsePackage = new BomPresentResponse( responsePackage  );
                    errorText.clearError();
                } catch (Exception e1) {
                    errorText.signalError(e1.toString());
                }
                createTableModelFromResponsePackage();

                saveButton.setVisible( false  );
                updateButton.setVisible( true );



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
            id.setText( Long.toString(itemToBeModified.getId() ) );
            summaryId.setText(itemToBeModified.getSummaryId());
            description.setText(itemToBeModified.getDescription());
            unitCost.setText(Double.toString(itemToBeModified.getUnitCost()));


            id.setVisible( true );
        } else {
            id.setVisible( false );
        }
    }

    /**
     * Create the table model based on response package.
     * No values are returned; side effect is the changes to the table model.
     */
    private void createTableModelFromResponsePackage() {
        tableModel.setRowCount(0);
        var numRows = responsePackage.getData().length;

        for (int row = 0; row < numRows; row++) {
            var bom = (com.inman.entity.BomPresent) responsePackage.getData()[row];
            tableModel.insertRow(row, new Object[] {
                    bom.getChildId(), bom.getChildSummary(), "Description", bom.getQuantityPer() });
        }
    }
}
