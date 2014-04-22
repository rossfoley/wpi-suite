package edu.wpi.cs.wpisuitetng.modules.planningpoker.view.overview;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Set;
import java.util.LinkedList;
import java.util.List;

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
import edu.wpi.cs.wpisuitetng.modules.core.models.User;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.controller.GetSessionController;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.controller.GetUserController;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.Estimate;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.PlanningPokerSession;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.PlanningPokerSessionModel;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.UserModel;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.ViewEventController;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.RequirementModel;

public class OverviewVoterTable extends JTable {
	private DefaultTableModel tableModel = null;
	private boolean initialized;
	private boolean changedByRefresh = false;
	private Border paddingBorder = BorderFactory.createEmptyBorder(0, 0, 0, 0);
	private OverviewReqTable newTable;
	private JScrollPane tablePanel;
	private PlanningPokerSession planningPokerSession;
	
	/**
	 * Sets initial table view
	 * @param data	Initial data to fill OverviewReqTable
	 * @param columnNames	Column headers of OverviewReqTable
	 */
	public OverviewVoterTable(Object[][] data, String[] columnNames) {
		this.planningPokerSession = ViewEventController.getInstance().getOverviewDetailPanel().getCurrentSession();
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

	}
	public List<String> getAllVoterNamesList() {
		List<String> allVoters = new ArrayList<String>();
		GetUserController.getInstance().retrieveUsers();
		List<User> user = UserModel.getInstance().getUsers();
		for(User u : user) {
			try {
				allVoters.add(u.getUsername());
			} catch (Exception E) {
				System.out.println("User is null");
			}
		}
		return allVoters;
	}
	
	public List<Requirement> getSessionReqs(){
		Set<Integer> sessionReqIds = planningPokerSession.getRequirementIDs();
		List<Requirement> sessionReqs = new LinkedList<Requirement>();
		for (Integer id : sessionReqIds) {
			Requirement current = RequirementModel.getInstance().getRequirement(id);
			sessionReqs.add(current);			
		}
		return sessionReqs;
	}
	public void populateVotePanel() {
		System.out.println("POPULATEVOTEDPANEL");
		tableModel.setRowCount(0);	
		Set<Integer> requirementIDs = planningPokerSession.getRequirementIDs();
		RequirementModel reqs = RequirementModel.getInstance();	
		List<String> allUserList = getAllVoterNamesList();
		List<Requirement> ListOfRequirements =  getSessionReqs();
		for (Requirement r : ListOfRequirements) {
			int reqID = r.getId();
			String reqName = r.getName();
			boolean vote = false;
			for (int i = 0; i < planningPokerSession.getEstimateVoterList().size(); i++) {
				if(planningPokerSession.getEstimateVoterList().get(i).getRequirementID() == r.getId()) {
					for (String s : allUserList) {
						String username = s;
						if(planningPokerSession.getEstimateVoterList().get(i).getVoterNameList().contains(s)) {
							vote = true; //mean voted
							tableModel.addRow(new Object[]{
									reqID,
									reqName,
									username,
									vote});	
						} else {
							tableModel.addRow(new Object[]{
									reqID,
									reqName,
									username,
									vote});	
						}
					}
				}
			}
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
