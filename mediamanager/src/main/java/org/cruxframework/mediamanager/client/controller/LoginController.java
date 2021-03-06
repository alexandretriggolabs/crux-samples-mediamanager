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

import org.cruxframework.crux.core.client.controller.Controller;
import org.cruxframework.crux.core.client.controller.Expose;
import org.cruxframework.crux.core.client.css.animation.Animation;
import org.cruxframework.crux.core.client.css.animation.StandardAnimation;
import org.cruxframework.crux.core.client.ioc.Inject;
import org.cruxframework.crux.core.client.screen.Screen;
import org.cruxframework.crux.core.client.screen.views.View;
import org.cruxframework.crux.smartfaces.client.dialog.MessageBox;
import org.cruxframework.crux.smartfaces.client.dialog.MessageBox.MessageType;
import org.cruxframework.crux.smartfaces.client.dialog.WaitBox;
import org.cruxframework.crux.smartfaces.client.dialog.animation.DialogAnimation;
import org.cruxframework.crux.widgets.client.simplecontainer.SimpleViewContainer;
import org.cruxframework.mediamanager.client.proxy.LoginProxy;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * Class description: 
 * @author alexandre.costa
 */
@Controller("loginController")
public class LoginController
{
	private static final String DEFAULT_SIGN_UP_MESSAGE = "Login: admin / admin";
	private static final String DEFAULT_ERROR_SIGN_UP = "Username and password invalid!";
	
	@Inject
	public LoginProxy loginProxy;
	
	private TextBox loginTextBox;
	private PasswordTextBox passwrodTextBox;
	
	/**
	 * Process user login.
	 */
	@Expose
	public void login()
	{	
		/* Get login and password */
		View view = View.of(this);
		loginTextBox = (TextBox) view.getWidget("loginTextBox");
		passwrodTextBox = 
			(PasswordTextBox)view.getWidget("passwordTextBox");
		WaitBox.show("Wait", DialogAnimation.fadeDownUp);
		/* invoke login service */
		
		loginProxy.login(loginTextBox.getValue(), passwrodTextBox.getValue(), this);
	}
	
	@Expose
	public void createNewAccount()
	{
		MessageBox.show(DEFAULT_SIGN_UP_MESSAGE, MessageType.INFO, DialogAnimation.fadeDownUp);
	}
	
	@Expose
	public void animateLogin()
	{
		animateLogo();
		animateIntroduction();
		animateFormWrapper();
	}
	
	
	/*****************************************
	 * Callback States
	 *****************************************/
	
	public void LoginSuccessState(Boolean result)
	{
		if (result)
		{
			((SimpleViewContainer) Screen.get("views")).showView("statistics");
			LoginController.showMenu();
		}else
		{
			WaitBox.hideAllDialogs();
			MessageBox.show(null, DEFAULT_ERROR_SIGN_UP, MessageType.ERROR, true,
					false, true, true,"faces-MessageBox", DialogAnimation.fadeDownUp);
		}
	}
	
	public void LoginFailueState(Throwable caught)
	{
		Window.alert("Log in unsuccessful");
		this.loginTextBox.setValue("");
		this.passwrodTextBox.setValue("");
	}
	
	
	/******************************************
	 * Utilities
	 ******************************************/
	
	private static void showMenu()
	{
		Screen.get("menuView").setVisible(true);
	}
	
	/******************************************
	 * Animations
	 ******************************************/
	
	private void animateLogo()
	{
		StandardAnimation animation = new StandardAnimation(StandardAnimation.Type.fadeInUp);
		animation.animate(View.of(this).getWidget("logo"));
	}
	
	private void animateIntroduction()
	{
		Timer delay = new Timer()
		{
			@Override
			public void run()
			{
				final Widget introductionText = View.of(LoginController.this)
					.getWidget("introductionText");
				
				StandardAnimation animation = new StandardAnimation(
					StandardAnimation.Type.fadeIn);
				
				animation.animate(introductionText, new Animation.Callback()
				{
					@Override
					public void onAnimationCompleted()
					{
						introductionText.addStyleName("opaque");
					}
				});
			}
		};
		delay.schedule(600);
	}
	
	private void animateFormWrapper()
	{
		StandardAnimation animation = new StandardAnimation(StandardAnimation.Type.fadeInUp);
		animation.animate(View.of(this).getWidget("formWrapper"));
	}
}
