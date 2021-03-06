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
package org.cruxframework.mediamanager.client.proxy;

import org.cruxframework.mediamanager.client.controller.LoginController;
import org.cruxframework.mediamanager.client.controller.MenuController;
import org.cruxframework.mediamanager.client.controller.RootController;

/**Class description: 
 * @author Bruno Medeiros (bruno@triggolabs.com)
 *
 */
public interface LoginProxy
{

	void login(String login, String password, LoginController controller);
//	void login(String login, String password, AsyncCallback<Boolean> callback);
	
	void isSessionActive(RootController controller);
//	void isSessionActive(AsyncCallback<Boolean> callback);
//
	void logout(MenuController controller);
//	void logout(AsyncCallback<Void> callback);
}
