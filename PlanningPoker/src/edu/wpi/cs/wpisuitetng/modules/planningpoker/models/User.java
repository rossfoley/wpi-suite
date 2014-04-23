/*******************************************************************************
 * Copyright (c) 2014 WPI-Suite
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: The Team8s
 ******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.planningpoker.models;


import com.google.gson.*;

import edu.wpi.cs.wpisuitetng.modules.AbstractModel;
import edu.wpi.cs.wpisuitetng.modules.core.models.Role;
import edu.wpi.cs.wpisuitetng.modules.core.models.UserDeserializer;
import edu.wpi.cs.wpisuitetng.modules.core.models.UserSerializer;
/**
 * The Data Model representation of a User. Implements
 * 	database interaction and serializing.
 * @author mdelladonna, twack, bgaffey
 */

public class User extends AbstractModel
{

	private String name;
	private String username;
	private int idNum;
	private Role role;
	
	transient private String password; // excluded from serialization, still stored.
	
	/**
	 * The primary constructor for a User
	 * @param name	User's full name
	 * @param username	User's username (nickname)
	 * @param idNum	User's ID number
	 */
	public User(String name, String username, String password, int idNum)
	{
		this.name = name;
		this.username = username;
		this.password = password;
		this.idNum = idNum;
		role = Role.USER;
	}
	
	@Override
	public boolean equals(Object other) {
		if(other instanceof User)
		{
			if( ((User)other).idNum == idNum)
			{
				//things that can be null
				if(name != null && !name.equals(((User)other).name))
				{
					return false;
				}
				
				if(username != null && !username.equals(((User)other).username))
				{
					return false;
				}
				
				if(password != null && !password.equals(((User)other).password))
				{
					return false;
				}
				
				if(role != null && !role.equals(((User)other).role))
				{
					return false;
				}
				
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Performs password checking logic. Fails if password field is null, which happens
	 * 	when User is deserialized so as to protect the password.
	 * @param pass	the password String to compare
	 * @return	True if the password matches, False otherwise.
	 */
	public boolean matchPassword(String pass)
	{
		return (password == null) ? false : password.equals(pass);
	}
	
	/**
	 * Sets password (please encrypt before using this method)
	 * @param pass
	 */
	public void setPassword(String pass)
	{
		password = pass;
	}
	
	public String getPassword()
	{
		return password;
	}
	
	/* Accessors */
	public String getName()
	{
		return name;
	}
	
	public int getIdNum()
	{
		return idNum;
	}
	
	public String getUsername()
	{
		return username;
	}
	
	/* database interaction */
	public void save()
	{
		return;
	}
	
	public void delete()
	{
		return;
	}
	
	/* Serializing */
	
	/**
	 * Serializes this User model into a JSON string.
	 * 
	 * @return	the JSON representation of this User
	 */
	public String toJSON()
	{
		final String json;
		
		final Gson gson = new GsonBuilder().registerTypeAdapter(User.class, new UserSerializer()).create();
		
		json = gson.toJson(this, User.class);
		
		return json;	
	}
	
	/**
	 * Static method offering comma-delimited JSON
	 * 	serializing of User lists
	 * @param u	an array of Users
	 * @return	the serialized array of Users
	 */
	public static String toJSON(User[] u)
	{
		String json ="[";
		
		for(User a : u)
		{
			json += a.toJSON() + ", ";
		}
		
		json += "]";
				
		return json;
		
	}
	
	/* Built-in overrides/overloads */
	
	/**
	 * Override of toString() to return a JSON string for now.
	 * 	May override in the future.
	 */
	public String toString()
	{
		return this.toJSON();
	}

	@Override
	public Boolean identify(Object o)
	{
		Boolean b  = false;
		
		if(o instanceof User)
			if(((User) o).username.equalsIgnoreCase(username))
				b = true;
		
		if(o instanceof String)
			if(((String) o).equalsIgnoreCase(username))
				b = true;
		return b;
	}
	
	/**
	 * Determines if this is equal to another user
	 * @param anotherUser
	 * @return true if this and anotherUser are equal
	 */
	public boolean equals(User anotherUser){
		return name.equalsIgnoreCase(anotherUser.getName()) &&
				username.equalsIgnoreCase(anotherUser.getUsername()) &&
				idNum == anotherUser.getIdNum();
	}
	
	public User setName(String newName){
		name = newName;
		return this;
	}
	
	public User setUserName(String newUserName){
		username = newUserName;
		return this;
	}
	
	public User setIdNum(int newidNum){
		idNum = newidNum;
		return this;
	}
	
	
	public Role getRole()
	{
		return role;
	}
	
	public void setRole(Role r)
	{
		role = r;
	}

	
	public static User fromJSON(String json) {
		// build the custom serializer/deserializer
		final Gson gson;
		final GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(User.class, new UserDeserializer());

		gson = builder.create();
		
		return gson.fromJson(json, User.class);
	}
	public static User[] fromJsonArray(String json) {
		final Gson parser = new Gson();
		return parser.fromJson(json, User[].class);
	}
}
