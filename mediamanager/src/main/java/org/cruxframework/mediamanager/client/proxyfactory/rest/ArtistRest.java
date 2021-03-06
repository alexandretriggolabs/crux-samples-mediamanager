package org.cruxframework.mediamanager.client.proxyfactory.rest;

import org.cruxframework.mediamanager.client.controller.ArtistController;
import org.cruxframework.mediamanager.client.controller.ArtistsController;
import org.cruxframework.mediamanager.client.controller.callback.DeleteCallback;
import org.cruxframework.mediamanager.client.controller.callback.EditArtistCallback;
import org.cruxframework.mediamanager.client.controller.callback.InsertCallback;
import org.cruxframework.mediamanager.client.controller.callback.SearchCallback;
import org.cruxframework.mediamanager.client.controller.callback.UpdateCallback;
import org.cruxframework.mediamanager.client.proxy.ArtistProxy;
import org.cruxframework.mediamanager.client.reuse.controller.AbstractController;
import org.cruxframework.mediamanager.client.service.ArtistServiceProxy;
import org.cruxframework.mediamanager.client.service.EditArtistServiceProxy;
import org.cruxframework.mediamanager.core.client.dto.ArtistDTO;

import com.google.gwt.core.client.GWT;

/**Class description: This class is responsible for running the service  REST communication (ArtistServiceProxy)  with 
 * the server when the project is switched to use JPA database.
 * @author Bruno Medeiros (bruno@triggolabs.com)
 */
public class ArtistRest implements ArtistProxy
{
	private final ArtistServiceProxy restServiceProxy = GWT.create(ArtistServiceProxy.class);
	private final EditArtistServiceProxy editArtistServiceProxy = GWT.create(EditArtistServiceProxy.class);
	private final ArtistServiceProxy artistServiceProxy = GWT.create(ArtistServiceProxy.class);

	/********************************
	 * Insert
	 *******************************/
	@Override
	public void insert(ArtistDTO dto, AbstractController abstractController)
	{
		ArtistController controller = (ArtistController) abstractController;
		getRestServiceProxy().insert(dto, new InsertCallback(controller));
	}


	/********************
	 * Update
	 *******************/
	@Override
	public void update(Integer id, ArtistDTO dto, AbstractController abstractController)
	{
		ArtistController controller = (ArtistController) abstractController;
		getRestServiceProxy().update(id, dto, new UpdateCallback<ArtistDTO>(controller));
	}
	

	/********************************
	 * Get
	 *******************************/
	
	@Override
	public void get(Integer identificator, AbstractController abstractController)	{
		ArtistController controller = (ArtistController) abstractController;
		getEditArtistServiceProxy().get(identificator, new EditArtistCallback(controller));
	}
	
	
	/********************************
	 * Search
	 *******************************/

	@Override
	public void search(String name, ArtistsController controller)
	{
		getArtistServiceProxy().search(name, new SearchCallback<ArtistDTO>(controller)); 
	}


	/********************************
	 * Delete
	 *******************************/
	@Override
	public void delete(ArtistDTO dto, ArtistsController controller)
	{
		getRestServiceProxy().delete(dto, new DeleteCallback<ArtistDTO>(dto, controller));
	}


	/**
	 * @return the restServiceProxy
	 */
	public ArtistServiceProxy getRestServiceProxy()
	{
		return restServiceProxy;
	}


	/**
	 * @return the editArtistServiceProxy
	 */
	public EditArtistServiceProxy getEditArtistServiceProxy()
	{
		return editArtistServiceProxy;
	}


	/**
	 * @return the artistServiceProxy
	 */
	public ArtistServiceProxy getArtistServiceProxy()
	{
		return artistServiceProxy;
	}
}
