package com.whenling.centralize.service;

import java.util.List;

import org.apache.shiro.util.SimpleByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.whenling.centralize.model.Menu;
import com.whenling.module.domain.model.User;
import com.whenling.module.domain.repository.UserRepository;
import com.whenling.module.domain.service.BaseService;
import com.whenling.module.security.DatabaseRealm;

@Service
public class UserService extends BaseService<User, Long> {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private DatabaseRealm databaseRealm;

	@Autowired
	private MenuService menuService;

	public User findByUsername(String username) {
		return userRepository.findByUsername(username);
	}

	/**
	 * 修改密码。新用户oldPassword传入null
	 * 
	 * @param entity
	 * @param oldPassword
	 * @param newPassword
	 * @return
	 */
	public User changePassword(User user, String oldPassword, String newPassword) {
		Assert.notNull(user.getUsername());
		Assert.notNull(newPassword);

		Object salt = new SimpleByteSource(user.getUsername());

		if (!user.isNew()) {
			Assert.isTrue(databaseRealm.doCredentialsMatch(oldPassword, new SimpleByteSource(user.getUsername()),
					user.getPassword()));
		}

		user.setPassword(databaseRealm.hashProvidedCredentials(newPassword, salt).toString());
		return user;
	}

	public List<Menu> getMenus(User user) {
		return menuService.findRoots();
	}

}