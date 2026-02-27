package com.pgms.tenant.auth.login.user.service;

import com.pgms.tenant.auth.login.user.dto.LoginRequest;
import com.pgms.tenant.auth.login.user.dto.LoginResponse;

public interface UserService {

  LoginResponse login(LoginRequest request);
}
