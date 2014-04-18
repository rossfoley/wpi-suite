/**
 * 
 */
package edu.wpi.cs.wpisuitetng.modules.planningpoker.view.updateestimates;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.DropMode;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import edu.wpi.cs.wpisuitetng.janeway.config.ConfigManager;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.Estimate;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.PlanningPokerSession;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.RequirementModel;

/**
 * @author Amanda
 *
 */
public class SelectRequirementToUpdateTable extends JTable {
	private DefaultTableModel tableModel = null;
	private boolean initialized;
	private boolean changedByRefresh = false;
	private Border paddingBorder = BorderFactory.createEmptyBorder(0, 4, 0, 0);
	private List<Integer> requirementsToDisplay;

	/**
	 * Sets initial table view
	 * @param data	Initial data to fill OverviewTable
	 * @param columnNames	Column headers of OverviewTable
	 */
	public SelectRequirementToUpdateTable(Object[][] data, String[] columnNames, List<Integer> displayRequirementIDs)
	{
		this.tableModel = new DefaultTableModel(data, columnNames);
		this.setModel(tableModel);
		this.setDefaultRenderer(Object.class, new DefaultTableCellRenderer());
		this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.setDragEnabled(true);
		this.setDropMode(DropMode.ON);
		this.requirementsToDisplay = displayRequirementIDs;


		//ViewEventController.getInstance().setOverviewReqTable(this);

		this.getTableHeader().setReorderingAllowed(false);
		this.setAutoCreateRowSorter(true);
		setFillsViewportHeight(true);

		//ViewEventController.getInstance().setOverviewReqTable(this);
		initialized = false;

		/* Create double-click event listener */
		this.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {

				if(getRowCount() > 0) {
					int mouseY = e.getY();
					Rectangle lastRow = getCellRect(getRowCount() - 1, 0, true);
					int lastRowY = lastRow.y + lastRow.height;

					if(mouseY > lastRowY) {
						getSelectionModel().clearSelection();
						repaint();
					}
				}
			}
		});
	}

	public void refresh(PlanningPokerSession session) {
		// TODO Implement Your Vote, Estimate columns
		// Currently is 0 for every estimate

		Set<Integer> requirementIDs = session.getRequirementIDs();
		RequirementModel reqs = RequirementModel.getInstance();
		int vote = 0;
		int estimate = 0;
		List<Estimate> estimates = session.getEstimates();

		// clear the table
		tableModel.setRowCount(0);		

		for (Integer requirementID : requirementIDs) {
			Requirement req = reqs.getRequirement(requirementID);
			String reqName = req.getName();
			vote = 0;
			for (Estimate e : estimates) {
				if (e.getRequirementID() == requirementID && e.getOwnerName().equals(ConfigManager.getConfig().getUserName())) {
					vote = e.getVote();
				}
			}

			tableModel.addRow(new Object[]{
					reqName,
					vote,
					estimate});	
		}
		// indicate that refresh is no longer affecting the table
		setChangedByRefresh(false);
	}

	/**
	 * @return the changedByRefresh 
	 */
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
	 * Overrides the isCellEditable method to ensure no cells are editable.
	 * @param row	row of OverviewReqTable cell is located
	 * @param col	column of OverviewReqTable cell is located
	 * @return boolean */
	@Override
	public boolean isCellEditable(int row, int col)	{
		if (col == 0){
			return false;
		}
		else {
			return true;
		}
	}



}
