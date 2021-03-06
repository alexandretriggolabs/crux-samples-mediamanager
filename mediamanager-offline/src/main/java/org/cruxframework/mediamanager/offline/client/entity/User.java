/*
 * Copyright 2011 cruxframework.org.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package org.cruxframework.mediamanager.offline.client.entity;


import org.cruxframework.crux.core.client.db.annotation.Store;
import org.cruxframework.crux.core.client.db.annotation.Store.Indexed;
import org.cruxframework.mediamanager.core.client.dto.UserDTO;
import org.cruxframework.mediamanager.offline.client.reuse.OfflineEntity;

/**
 * Class description: Defines an entity User with the properties that must be stored in the database.
 * 
 * @author Bruno Medeiros (bruno@triggolabs.com)
 */


@Store(User.STORE_NAME)
public class User extends OfflineEntity<UserDTO> 
//implements AbstractEntity<UserDTO> 
{
	public static final String STORE_NAME = "user";
	private String name;
	private String login;
	private String password;
	
	/*******************************
	 * Methods Abstract
	 ******************************/
	@Override
	public String getStoreName()
	{
		return STORE_NAME;
	}
	@Override
	public UserDTO getDTORepresentation()
	{
		UserDTO dto = new UserDTO();
		dto.setName(getName());
		dto.setLogin(getLogin());
		dto.setPassword(getPassword());
		return dto;
	}
	
	
	
	/*******************************
	 * GETTERS and SETTERS 
	 ******************************/
	/**
	 * @return the login
	 */
	@Indexed()
	public String getLogin() {
		return login;
	}
	/**
	 * @param login the login to set
	 */
	public void setLogin(String login) {
		this.login = login;
	}
	/**
	 * @return the password
	 */
	@Indexed()
	public String getPassword() {
		return password;
	}
	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}	
	
	/**
	 * @return the name
	 */
	@Indexed()
	public String getName() 
	{
		return name;
	}
	
	/**
	 * @param name the name to set
	 */
	public void setName(String name) 
	{
		this.name = name;
	}
}
