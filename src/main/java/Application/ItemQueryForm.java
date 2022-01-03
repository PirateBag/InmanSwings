package Application;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import com.inman.business.QueryParameterException;
import com.inman.model.rest.ItemResponse;
import com.inman.model.rest.SearchItemRequest;

public class ItemQueryForm {
	private static JPanel itemQuery;
	
	public static JPanel getItemQuery( ) {
		if ( itemQuery != null ) {
			return itemQuery;
		}
		
		JPanel itemQueryPanel = new JPanel( );
		itemQueryPanel.setLayout( new BoxLayout( itemQueryPanel, BoxLayout.Y_AXIS ) );
		itemQueryPanel.add( Utility.titleMaker( "Search Parameters" ), BorderLayout.PAGE_START );
		itemQueryPanel.add( Utility.labelMaker(" ", JLabel.TRAILING),
				BorderLayout.LINE_START  );
		
		JLabel errorMessage = Utility.createErrorMessage( JLabel.TRAILING  ) ;
		itemQueryPanel.add( errorMessage );
		
		JTextField itemId = Utility.createTextField( "Item Id" );
		itemQueryPanel.add( itemId );

		JTextField summaryId = Utility.createTextField( "Summary Id" );
		itemQueryPanel.add( summaryId );

		JTextField description = Utility.createTextField( "Description" );
		itemQueryPanel.add( description );

		itemQueryPanel.add( Utility.labelMaker(" ", JLabel.TRAILING),
				BorderLayout.LINE_START  );

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout( new BoxLayout( buttonPanel, BoxLayout.X_AXIS ) );

		var searchButton = new JButton( "Search");
		buttonPanel.add( searchButton);

		var exitButton = new JButton( "Exit");
		buttonPanel.add( exitButton );
		
		var addButton = new JButton( "Add" );
		buttonPanel.add( addButton );
		itemQueryPanel.add( buttonPanel );

		itemQueryPanel.add( Utility.labelMaker(" ", JLabel.TRAILING),
				BorderLayout.LINE_START  );
		
		DefaultTableModel tableModel = new DefaultTableModel();
		JTable queryResults = new JTable( tableModel );
		String [] columnNames = { "id", "Summary Id", "Description" };
		for (String columnName : columnNames ) {
			tableModel.addColumn( columnName );
		}
		queryResults.setFillsViewportHeight(true);
		itemQueryPanel.add( new JScrollPane( queryResults) );
			
		searchButton.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					SearchItemRequest searchItemRequest = new SearchItemRequest(
						itemId.getText(), summaryId.getText(), description.getText() );

					String completeUrl = "http://localhost:8080/" + SearchItemRequest.queryUrl;
					ItemResponse responsePackage = Main.restTemplate.postForObject( completeUrl, searchItemRequest, ItemResponse.class );
					
					errorMessage.setText( "" );
					
					var numRows = responsePackage.getData().length;
					
					for ( int row = 0; row < numRows ; row++ ) {
						var item = responsePackage.getData()[ row ];
						tableModel.insertRow( row, new Object[] { item.getId(), item.getSummaryId(), item.getDescription() } );
					}
				} catch (QueryParameterException qfe ) {
					errorMessage.setText( qfe.getMessage() );
				} catch ( Exception e1 ) {
					errorMessage.setText( e1.getMessage() );
				}
			}
		} );
		
		addButton.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ScreenStateService.evaluate(
						new Action( "itemAdd", ScreenTransitionType.REPLACE, ItemPropertiesForm.getItemProperties( ) ) );

			}
		} );  

		FormsLibrary.itemQuery = itemQueryPanel;
		ScreenStateService.primaryPanel.add( itemQueryPanel );
		return itemQueryPanel; 
	}
}
