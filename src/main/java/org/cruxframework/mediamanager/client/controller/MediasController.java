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
package org.cruxframework.mediamanager.client.controller;

import java.util.Calendar;
import java.util.Date;

import org.cruxframework.crux.core.client.Crux;
import org.cruxframework.crux.core.client.controller.Controller;
import org.cruxframework.crux.core.client.controller.Expose;
import org.cruxframework.crux.core.client.ioc.Inject;
import org.cruxframework.crux.core.client.rest.Callback;
import org.cruxframework.crux.core.client.screen.views.BindView;
import org.cruxframework.crux.core.client.screen.views.BindableView;
import org.cruxframework.crux.core.client.screen.views.View;
import org.cruxframework.crux.core.client.screen.views.ViewActivateEvent;
import org.cruxframework.crux.core.client.screen.views.WidgetAccessor;
import org.cruxframework.crux.smartfaces.client.dialog.DialogViewContainer;
import org.cruxframework.crux.smartfaces.client.dialog.MessageBox;
import org.cruxframework.crux.smartfaces.client.dialog.MessageBox.MessageType;
import org.cruxframework.crux.smartfaces.client.dialog.WaitBox;
import org.cruxframework.crux.smartfaces.client.dialog.animation.DialogAnimation;
import org.cruxframework.crux.widgets.client.datebox.DateBox;
import org.cruxframework.crux.widgets.client.deviceadaptivegrid.DeviceAdaptiveGrid;
import org.cruxframework.crux.widgets.client.event.SelectEvent;
import org.cruxframework.crux.widgets.client.grid.DataRow;
import org.cruxframework.mediamanager.client.controller.datasource.MediaDTODatasource;
import org.cruxframework.mediamanager.client.reuse.controller.CallbackAdapter;
import org.cruxframework.mediamanager.client.reuse.controller.EditOperation;
import org.cruxframework.mediamanager.client.reuse.controller.SearchController;
import org.cruxframework.mediamanager.client.service.MediaServiceProxy;
import org.cruxframework.mediamanager.shared.dto.MediaDTO;
import org.cruxframework.mediamanager.shared.enums.MediaType;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * Controller for medias view.
 * 
 * @author alexandre.costa
 */
@Controller("mediasController")
public class MediasController extends SearchController<MediaDTO>
{
	private static final String LEND = "lend";
	private static final String NAME_TXT = "nameTxt";
	private static final String DATE = "date";

	@Inject
	private MediasViewWidgetAccessor mediaViewWidgetAccessor;

	@Inject
	private MediaServiceProxy mediaServiceProxy;

	/**
	 * Handles onActivate view event.
	 */
	@Expose
	public void onActivate()
	{
		/* Clean grid */
		getResultGrid().clear();
		getResultGrid().refresh();

		/* Get all media types */
		ListBox types = mediaViewWidgetAccessor.typeListBox();
		if (types.getItemCount() == 0)
		{
			types.addItem("", "");
			types.setSelectedIndex(0);
			for (MediaType type : MediaType.values())
			{
				types.addItem(type.name(), type.name());
			}
		}

		/* animations */
		animateContent();
		animateResults();
	}

	/**
	 * Handles search command invocation.
	 */
	@Expose
	public void search()
	{
		WaitBox.show("Wait", DialogAnimation.fadeDownUp);
		MediaType type = null;
		String typeValue = mediaViewWidgetAccessor.typeListBox().getValue(mediaViewWidgetAccessor.typeListBox().getSelectedIndex());

		if (typeValue != null && typeValue.trim().length() > 0)
		{
			type = MediaType.valueOf(typeValue);
		}

		String name = mediaViewWidgetAccessor.nameTextBox().getValue();
		String person = mediaViewWidgetAccessor.personTextBox().getValue();
		mediaServiceProxy.search(type, name, person, new SearchCallback());
	}

	/**
	 * Handles lend view activate event.
	 * 
	 * @param event view activate event
	 */
	@Expose
	public void onActivateLendView(ViewActivateEvent event)
	{
		View view = View.of(this);
		DateBox dateBox = (DateBox) view.getWidget(DATE);
		dateBox.getTextBox().setReadOnly(true);
		dateBox.setFormat(new org.cruxframework.crux.widgets.client.datebox.DateBox.CruxDefaultFormat(DateTimeFormat
		    .getFormat(PredefinedFormat.DATE_MEDIUM)));

		MediaDTO media = event.getParameterObject();
		mediaServiceProxy.get(media.getId(), new LendMediaCallback());
	}

	/**
	 * Handles lend command action.
	 * 
	 * @param selectEvent event
	 */
	@Expose
	public void lend(SelectEvent selectEvent)
	{
		DataRow row = getResultGrid().getRow((Widget) selectEvent.getSource());
		MediaDTO dto = (MediaDTO) row.getBoundObject();
		DialogViewContainer container = DialogViewContainer
		    .createDialog(LEND, LEND, false, false, false, true, null, null, null, 0, 0, dto);

		container.center();
		container.show();
	}

	/**
	 * Change media status.
	 * 
	 * @param event event
	 */
	@Expose
	public void changeBorrowed(ClickEvent event)
	{
		boolean checked = ((CheckBox) event.getSource()).getValue();
		updateBorrowedCheckBoxState(checked);
	}

	/**
	 * Save media status.
	 */
	@Expose
	public void saveLend()
	{
		BindableView<MediaDTO> view = View.of(this);
		final MediaDTO DTO = view.getData();
		String validation = validate(DTO);

		if (validation == null)
		{
			mediaServiceProxy.update(DTO.getId(), DTO, new SaveLendCallback(DTO));
		}
		else
		{
			MessageBox.show(validation, MessageType.ERROR);
		}
	}

	/**
	 * Cancels media lend process.
	 */
	@Expose
	public void cancelLend()
	{
		closeDialogViewContainer(View.of(this));
	}

	/**************************************
	 * Utilities
	 *************************************/

	private void updateBorrowedCheckBoxState(boolean checked)
	{
		View view = View.of(this);
		TextBox text = (TextBox) view.getWidget(NAME_TXT);
		DateBox date = (DateBox) view.getWidget(DATE);

		if (!checked)
		{
			text.setValue("");
			text.setEnabled(false);
			date.setValue(null);
			date.setEnabled(false);
		}
		else
		{
			text.setEnabled(true);
			date.setEnabled(true);
		}
	}

	private String validate(MediaDTO dto)
	{
		/* all fields */
		if (dto.getBorrowed() && (isEmpty(dto.getPerson()) || dto.getDate() == null))
		{
			return "Fill all fields";
		}
		
		Calendar date = Calendar.getInstance();
		date.set(Calendar.HOUR_OF_DAY, 0);
		date.set(Calendar.MINUTE, 0);
		date.set(Calendar.SECOND, 1);

		/* date validation */
		Date now = date.getTime();

		if (dto.getDate() != null && dto.getDate().after(now))
		{
			return "The date you are trying to use is greater than the actual date.";
		}

		return null;
	}

	private static boolean isEmpty(String string)
	{
		return string == null || string.trim().length() == 0;
	}

	private static void closeDialogViewContainer(View view)
	{
		DialogViewContainer container = (DialogViewContainer) view.getContainer();
		container.setUnloadViewOnClose(true);
		container.hide();
	}

	/*************************************
	 * Overwritten methods
	 *************************************/

	@Override
	protected String getEditViewOutcome()
	{
		return "media";
	}

	@Override
	protected DeviceAdaptiveGrid getResultGrid()
	{
		return mediaViewWidgetAccessor.tableGrid();
	}

	@Override
	protected MediaServiceProxy getRestServiceProxy()
	{
		return mediaServiceProxy;
	}

	/*************************************
	 * Getters and setters
	 *************************************/

	/**
	 * @param mediaViewWidgetAccessor the mediaViewWidgetAccessor to set
	 */
	public void setMediaViewWidgetAccessor(MediasViewWidgetAccessor mediaViewWidgetAccessor)
	{
		this.mediaViewWidgetAccessor = mediaViewWidgetAccessor;
	}

	/**
	 * @param mediaServiceProxy the mediaServiceProxy to set
	 */
	public void setMediaServiceProxy(MediaServiceProxy mediaServiceProxy)
	{
		this.mediaServiceProxy = mediaServiceProxy;
	}

	/**********************************************
	 * Callback and widget accessor classes
	 **********************************************/

	private class SaveLendCallback implements Callback<EditOperation>
	{
		private final MediaDTO dto;

		public SaveLendCallback(MediaDTO dto)
		{
			this.dto = dto;
		}

		@Override
		public void onSuccess(EditOperation result)
		{
			MediaDTODatasource datasource = (MediaDTODatasource) getResultGrid().getDataSource();

			datasource.add(dto);
			getResultGrid().clear();
			getResultGrid().loadData();
			getResultGrid().refresh();
			closeDialogViewContainer(View.of(MediasController.this));
		}

		@Override
		public void onError(Exception e)
		{
			Crux.getErrorHandler().handleError(e);
		}
	}

	private class LendMediaCallback extends CallbackAdapter<MediaDTO>
	{
		@Override
		public void onComplete(MediaDTO result)
		{
			BindableView<MediaDTO> view = View.of(MediasController.this);
			view.setData(result);
			if (result != null)
			{
				updateBorrowedCheckBoxState(result.getBorrowed());
			}
		}
	}

	// CHECKSTYLE:OFF
	@BindView("medias")
	public static interface MediasViewWidgetAccessor extends WidgetAccessor
	{
		TextBox nameTextBox();

		ListBox typeListBox();

		TextBox personTextBox();

		DeviceAdaptiveGrid tableGrid();
	}

}
