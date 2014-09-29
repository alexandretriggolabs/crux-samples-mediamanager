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
package org.cruxframework.mediamanager.server.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;

import org.cruxframework.mediamanager.server.reuse.entity.AbstractEntity;
import org.cruxframework.mediamanager.shared.dto.CountryDTO;

/**
 * Class description: Implements country entity.
 * 
 * @author alexandre.costa
 */
@Entity
@SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_COUNTRY")
public class Country extends AbstractEntity<CountryDTO>
{
	private String name;

	/**
	 * Default constructor.
	 */
	public Country()
	{

	}

	/**
	 * Constructor.
	 * 
	 * @param name country name
	 */
	public Country(String name)
	{
		this.name = name;
	}

	/**
	 * @return the name
	 */
	@Column(nullable = false)
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

	@Override
	@Transient
	public CountryDTO getDTORepresentation()
	{
		CountryDTO dto = new CountryDTO();
		dto.setId(getId());
		dto.setName(getName());
		return dto;
	}
}
