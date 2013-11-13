package com.t3.client.models;

import com.t3.client.controllers.SessionCommunicate;
import com.t3.common.models.User;

/**
 * Interface using to communicate between Client and Login panel
 * @author Luan Vu
 *
 */
public interface LoginListener {
	public void onLoginSuccess(SessionCommunicate sessionComm, User user);
}
