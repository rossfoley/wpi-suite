/*******************************************************************************
 * Copyright (c) 2014 WPI-Suite
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: The Team8s
 ******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.planningpoker.view.overview;

import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.PlanningPokerSession;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.PlanningPokerSession.SessionState;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.overview.icons.EstimatedIcon;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.overview.icons.IterationIcon;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.overview.icons.RequirementIcon;

/**
 * @author Randy Acheson
 * @version $Revision: 1.0 $
 */
public class CustomTreeCellRenderer extends DefaultTreeCellRenderer {
	Icon requirementIcon;
	Icon iterationIcon;
	Icon estimatedIcon;

	public CustomTreeCellRenderer() {
		super();
		requirementIcon = new RequirementIcon();
		iterationIcon = new IterationIcon();
		estimatedIcon = new EstimatedIcon();
	}

	/**
	 * Method getTreeCellRendererComponent.
	 * Implements custom icons
	 * @param tree JTree
	 * @param value Object
	 * @param sel boolean
	 * @param expanded boolean
	 * @param leaf boolean
	 * @param row int
	 * @param hasFocus boolean
	 * @return Component 
	 * @see javax.swing.tree.TreeCellRenderer#getTreeCellRendererComponent(JTree,
	 *      Object, boolean, boolean, boolean, int, boolean) 
	 */
	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean sel, boolean expanded, boolean leaf, int row,
			boolean hasFocus) {

		super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf,
				row, hasFocus);
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
		
		if (node.getUserObject() instanceof PlanningPokerSession) {
			PlanningPokerSession nodeSession = (PlanningPokerSession) node.getUserObject();
			if (nodeSession.getGameState() == SessionState.VOTINGENDED && (nodeSession.requirementsGetSize() == nodeSession.getFinalEstimates().size())){
				setIcon(estimatedIcon);
			}
			else{
			setIcon(requirementIcon);
			}
		} 
		else {
			setIcon(iterationIcon);
		}
		return this; 
	}
}
