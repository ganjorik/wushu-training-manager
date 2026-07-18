package com.wushu.service;

import com.wushu.entity.User;

public interface UserService {

	User findByUsername(String username);
}
