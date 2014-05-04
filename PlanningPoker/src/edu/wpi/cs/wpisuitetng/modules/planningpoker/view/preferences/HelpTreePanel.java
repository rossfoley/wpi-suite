package edu.wpi.cs.wpisuitetng.modules.planningpoker.view.preferences;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JScrollPane;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.JTree;
import javax.swing.JPanel;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.AbstractListModel;

public class HelpTreePanel extends JScrollPane implements MouseListener, TreeSelectionListener{
	public HelpTreePanel() {
		
		JList list = new JList();
		list.setModel(new AbstractListModel() {
			String[] values = new String[] {"What is Planning Poker?", 
					"The Overview Panel", "Creating a Planning Poker Session", 
					"Editing a Planning Poker Session", "Voting in a Planning Poker Session", 
					"Viewing Session Statistics", "Setting the Final Estimates"};
			public int getSize() {
				return values.length;
			}
			public Object getElementAt(int index) {
				return values[index];
			}
		});
		list.setToolTipText("");
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		setRowHeaderView(list);
	}

	@Override
	public void valueChanged(TreeSelectionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
