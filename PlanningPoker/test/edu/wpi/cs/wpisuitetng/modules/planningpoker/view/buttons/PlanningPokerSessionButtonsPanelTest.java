package edu.wpi.cs.wpisuitetng.modules.planningpoker.view.buttons;

import static org.junit.Assert.*;

import javax.swing.JButton;

import org.junit.Test;

public class PlanningPokerSessionButtonsPanelTest {
	PlanningPokerSessionButtonsPanel buttonsPanel = new PlanningPokerSessionButtonsPanel();

	@Test
	public void testGetCreateButton() {
		JButton createButton = buttonsPanel.getCreateButton();
		assertTrue(createButton.getText().contains("Create"));
		assertTrue(createButton.getText().contains("Session"));
	}

	@Test
	public void testDisableEditButton() {
		buttonsPanel.disableEditButton();
		JButton editButton = buttonsPanel.getEditButton();
		assertFalse(editButton.isEnabled());
	}

	@Test
	public void testEnableEditButton() {
		buttonsPanel.enableEditButton();
		JButton editButton = buttonsPanel.getEditButton();
		assertTrue(editButton.isEnabled());
	}

	@Test
	public void testDisableVoteButton() {
		buttonsPanel.disableVoteButton();
		JButton voteButton = buttonsPanel.getVoteButton();
		assertFalse(voteButton.isEnabled());
	}

	@Test
	public void testEnableVoteButton() {
		buttonsPanel.enableVoteButton();
		JButton voteButton = buttonsPanel.getVoteButton();
		assertTrue(voteButton.isEnabled());
	}

	@Test
	public void testDisableStatisticsButton() {
		buttonsPanel.disableStatisticsButton();
		JButton statsButton = buttonsPanel.getStatisticsButton();
		assertFalse(statsButton.isEnabled());
	}

	@Test
	public void testEnableStatisticsButton() {
		buttonsPanel.enableStatisticsButton();
		JButton statsButton = buttonsPanel.getStatisticsButton();
		assertTrue(statsButton.isEnabled());
	}
	
	@Test
	public void testDisableAllButtons() {
		buttonsPanel.disableAllButtons();
		
		// Check if all buttons are disabled
		JButton editButton = buttonsPanel.getEditButton();
		assertFalse(editButton.isEnabled());
		
		JButton voteButton = buttonsPanel.getVoteButton();
		assertFalse(voteButton.isEnabled());
		
		JButton statsButton = buttonsPanel.getStatisticsButton();
		assertFalse(statsButton.isEnabled());
	}

}
