package com.lxzl.erp.core.service.user;


import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.erp.user.LoginParam;
import com.lxzl.erp.common.domain.erp.user.User;
import com.lxzl.erp.common.domain.erp.user.UserPageParam;
import com.lxzl.se.core.service.BaseService;

public interface UserService extends BaseService {

    ServiceResult<String,User> login(LoginParam loginParam, String ip);
    ServiceResult<String, Integer> addUser(User user);
    ServiceResult<String, Integer> updateUser(User user);
    ServiceResult<String, Integer> updateUserPassword(User user);
    ServiceResult<String,User> getUserById(Integer userId);
    ServiceResult<String,Page<User>> userPage(UserPageParam userPageParam);
}
