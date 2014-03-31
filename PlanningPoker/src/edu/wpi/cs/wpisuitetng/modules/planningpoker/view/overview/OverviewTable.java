/**
 * 
 */
package edu.wpi.cs.wpisuitetng.modules.planningpoker.view.overview;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DateFormat;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DropMode;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.JTextField;
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
			tableModel.addRow(new Object[]{ pokerSession.getID(), 
					//pokerSession.getName(),
					"Planning Poker Session Name goes here",
					DateFormat.getDateInstance(DateFormat.SHORT).format(pokerSession.getEndDate().getTime())
			});	
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
		if(!initialized)
		{
			try 
			{
				GetSessionController.getInstance().retrieveSessions();
				initialized = true;
			}
			catch (Exception e)
			{

			}
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
	
}

