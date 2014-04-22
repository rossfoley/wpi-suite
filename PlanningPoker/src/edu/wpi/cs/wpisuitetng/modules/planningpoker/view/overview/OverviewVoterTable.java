package edu.wpi.cs.wpisuitetng.modules.planningpoker.view.overview;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.List;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.DropMode;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import edu.wpi.cs.wpisuitetng.janeway.config.ConfigManager;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.controller.GetSessionController;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.Estimate;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.PlanningPokerSession;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.ViewEventController;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.RequirementModel;

public class OverviewVoterTable extends JTable{
	private DefaultTableModel tableModel = null;
	private boolean initialized;
	private boolean changedByRefresh = false;
	private Border paddingBorder = BorderFactory.createEmptyBorder(0, 0, 0, 0);
	private OverviewReqTable newTable;
	private JScrollPane tablePanel;
	
	/**
	 * Sets initial table view
	 * @param data	Initial data to fill OverviewReqTable
	 * @param columnNames	Column headers of OverviewReqTable
	 */
	public OverviewVoterTable(Object[][] data, String[] columnNames) {
		this.tableModel = new DefaultTableModel(data, columnNames);
		this.setModel(tableModel);
		this.setDefaultRenderer(Object.class, new DefaultTableCellRenderer());
		this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.setDragEnabled(true);
        this.setDropMode(DropMode.ON);
        ViewEventController.getInstance().setOverviewVoterTable(this);
		this.getTableHeader().setReorderingAllowed(false);
		this.setAutoCreateRowSorter(true);
		setFillsViewportHeight(true);
		initialized = false;

		newTable = new OverviewReqTable(data, columnNames);
		tablePanel = new JScrollPane(newTable);
		
		newTable.getColumnModel().getColumn(0).setMinWidth(200); // Requirement Name
		newTable.getColumnModel().getColumn(1).setMinWidth(100); // User Vote		
		// Put the info panel and table panel into the split pane
	
		// Makes the split pane divide 50/50 for each portion
		Dimension d = new Dimension(200, 200);
        tablePanel.setMinimumSize(d);
        //add(tablePanel, BorderLayout.EAST);
	}
	
	public void populateVotePanel(PlanningPokerSession session) {
		// TODO Implement Your Vote, Estimate columns
		// Currently is 0 for every estimate
		
		Set<Integer> requirementIDs = session.getRequirementIDs();
		RequirementModel reqs = RequirementModel.getInstance();
		int vote = 0;
		int estimate = 0;

		// clear the table
		tableModel.setRowCount(0);		

		for (Integer requirementID : requirementIDs) {
			Requirement req = reqs.getRequirement(requirementID);
			String reqName = req.getName();
			vote = 0;
			for (Estimate e : session.getEstimates()) {
				if (e.getRequirementID() == requirementID && e.getOwnerName().equals(ConfigManager.getConfig().getUserName())) {
					vote = e.getVote();
				}
			}

			tableModel.addRow(new Object[]{
					reqName,
					vote,
					estimate});	
		}
	}
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

        if (JComponent.class.isInstance(comp)) {
            ((JComponent)comp).setBorder(paddingBorder);
        }
		return comp;

    }
	
	/**
	 * Overrides the isCellEditable method to ensure no cells are editable.
	 * @param row	row of OverviewReqTable cell is located
	 * @param col	column of OverviewReqTable cell is located
	 * @return boolean */
	@Override
	public boolean isCellEditable(int row, int col)	{
		return false;
	}
}
