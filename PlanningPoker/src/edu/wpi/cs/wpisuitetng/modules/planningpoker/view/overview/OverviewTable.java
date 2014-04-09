package edu.wpi.cs.wpisuitetng.modules.planningpoker.view.overview;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DropMode;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import edu.wpi.cs.wpisuitetng.modules.planningpoker.controller.GetSessionController;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.PlanningPokerSession;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.PlanningPokerSessionModel;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.ViewEventController;

/**
 * @author rossfoley
 *
 */
public class OverviewTable extends JTable
{
	private DefaultTableModel tableModel = null;
	private boolean initialized;
	private boolean changedByRefresh = false;	
	private Border paddingBorder = BorderFactory.createEmptyBorder(0, 4, 0, 0);
	
	/**
	 * Sets initial table view
	 * 
	 * @param data	Initial data to fill OverviewTable
	 * @param columnNames	Column headers of OverviewTable
	 */
	public OverviewTable(Object[][] data, String[] columnNames)
	{
		this.tableModel = new DefaultTableModel(data, columnNames);
		this.setModel(tableModel);
		this.setDefaultRenderer(Object.class, new DefaultTableCellRenderer());
		this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.setDragEnabled(true);
        this.setDropMode(DropMode.ON);

		this.getTableHeader().setReorderingAllowed(false);
		this.setAutoCreateRowSorter(true);
		setFillsViewportHeight(true);

		//ViewEventController.getInstance().setOverviewTable(this);
		initialized = false;

		/* Create double-click event listener */
		this.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				
				if(getRowCount() > 0)
				{
					int mouseY = e.getY();
					Rectangle lastRow = getCellRect(getRowCount() - 1, 0, true);
					int lastRowY = lastRow.y + lastRow.height;

					if(mouseY > lastRowY) 
					{
						getSelectionModel().clearSelection();
						repaint();
					}					

					// Open detail overview panel
					ViewEventController.getInstance().displayDetailedSession();
				}
				
				
				
				// Open edit session tab after 2 mouse clicks
				if (e.getClickCount() == 2)
				{
					ViewEventController.getInstance().editSelectedSession();
				}
			}
		});
		
		 System.out.println("finished constructing the table");
	}
	
	/**
	 * updates OverviewTable with the contents of the requirement model	 * 
	 */
	public void refresh() {
		List<PlanningPokerSession> pokerSessions = PlanningPokerSessionModel.getInstance().getPlanningPokerSessions();
				
		// clear the table
		tableModel.setRowCount(0);		

		for (PlanningPokerSession pokerSession : pokerSessions) {	
			String endDate, createrName, deckName;
			// Handle if there was no end date set
			try {
				endDate = DateFormat.getDateInstance(DateFormat.MEDIUM).format(pokerSession.getEndDate().getTime());
			} catch (NullPointerException ex) {
				endDate = new String("No end date");
			}
			
			// Handle if there was no owner
			try {
				createrName = pokerSession.getSessionCreatorName();
			} catch (NullPointerException ex) {
				createrName = new String("Creater not set");
			}
			
			// Handle if there was no deck set
			try {
				deckName = pokerSession.getSessionDeck().getDeckName();
			} catch (NullPointerException ex) {
				deckName = new String("None");
			}

			tableModel.addRow(new Object[]{
					pokerSession.getID(),
					pokerSession.getName(),
					endDate,
					Integer.toString(pokerSession.requirementsGetSize()), 
					deckName,
					createrName});	
		}
		// indicate that refresh is no longer affecting the table
		setChangedByRefresh(false);
		
		System.out.println("finished refreshing the table");		
	}
	
	/**
	
	 * @return the changedByRefresh */
	public boolean wasChangedByRefresh() {
		return changedByRefresh;
	}

	/**
	 * @param changedByRefresh the changedByRefresh to set
	 */
	public void setChangedByRefresh(boolean changedByRefresh) {
		this.changedByRefresh = changedByRefresh;
	}

	/**
	 * Overrides the paintComponent method to retrieve the requirements on the first painting.
	 * 
	 * @param g	The component object to paint
	 */
	@Override
	public void paintComponent(Graphics g)
	{
		if(!initialized) {
			try {
				GetSessionController.getInstance().retrieveSessions();
				initialized = true;
			} catch (Exception e) {}
		}

		super.paintComponent(g);
	}
	

	
	/**
	 * Method prepareRenderer.
	 * @param renderer TableCellRenderer
	 * @param row int
	 * @param column int
	 * @return Component
	 */
	@Override
    public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
        Component comp = super.prepareRenderer(renderer, row, column);

        if (JComponent.class.isInstance(comp)){
            ((JComponent)comp).setBorder(paddingBorder);
        }
		return comp;

    }
	
	
	/**
	 * Overrides the isCellEditable method to ensure no cells are editable.
	 * 
	 * @param row	row of OverviewTable cell is located
	 * @param col	column of OverviewTable cell is located
	
	 * @return boolean */
	@Override
	public boolean isCellEditable(int row, int col)
	{
		return false;
	}
	
}

