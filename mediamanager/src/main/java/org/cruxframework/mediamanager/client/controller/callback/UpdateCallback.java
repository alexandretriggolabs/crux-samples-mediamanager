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
package org.cruxframework.mediamanager.client.controller.callback;

import org.cruxframework.mediamanager.client.reuse.controller.CallbackAdapter;
import org.cruxframework.mediamanager.client.reuse.controller.EditController;
import org.cruxframework.mediamanager.core.client.reuse.AbstractDTO;
import org.cruxframework.mediamanager.core.client.reuse.EditOperation;

/**Class description: 
 * @author Bruno Medeiros (bruno@triggolabs.com)
 *
 */
public class UpdateCallback<T extends AbstractDTO> extends CallbackAdapter<EditOperation>
{
	private final EditController<T> controller;
	
	public UpdateCallback(EditController<T> controller)
	{
		this.controller = controller;
	}
	
	@Override
	public void onComplete(EditOperation result)
	{
		controller.completeUpdate();
	}
}