package edu.wpi.cs.wpisuitetng.modules.planningpoker.models;

import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractListModel;

import edu.wpi.cs.wpisuitetng.modules.core.models.Project;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.controller.AddDeckController;

public class ProjectModel extends AbstractListModel {
	
	private List<Project> existingProject;	
	private static ProjectModel instance; // static object to allow the decklistmodel 
	

	/**
	 * Constructs an list of decks for the project that contains only the default deck
	 */
	private ProjectModel(){
		existingProject = new ArrayList<Project>();
	}
	
	/** 
	 * if the decklistmodel has not yet been created, create it
	 * @return the instance of the decklistmodel 
	 */
	public static ProjectModel getInstance(){
		if (instance == null){
			instance = new ProjectModel();
		}
		return instance;
	}

	@Override
	public int getSize() {
		return existingProject.size();
	}

	/**
	 * Finds a deck with the deck numbers matching the given list
	 * 
	 * @param deckNums list of numbers to match to decks in decklistmodel
	 */
	public List<Project> getProjects(){
		return existingProject;
	}

	@Override
	public Object getElementAt(int index) {
		// TODO Auto-generated method stub
		return null;
	}

	public void emptyModel() {
		existingProject.clear();
		
	}

	public void addProject(Project project) {
		existingProject.add(project);		
	}
}
