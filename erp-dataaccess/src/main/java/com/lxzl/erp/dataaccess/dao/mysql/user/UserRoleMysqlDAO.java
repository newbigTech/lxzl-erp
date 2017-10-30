package com.lxzl.erp.dataaccess.dao.mysql.user;

import com.lxzl.erp.dataaccess.domain.user.UserRoleDO;
import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: gaochao
 * Date: 2016/12/26.
 * Time: 9:21.
 */
public interface UserRoleMysqlDAO extends BaseMysqlDAO<UserRoleDO> {

    // 根据用户ID查找其拥有哪些角色
    List<Integer> findRoleListByUserId(@Param("userId") Integer userId);
    UserRoleDO findUserRole(@Param("userId") Integer userId,
                            @Param("roleId") Integer roleId);

    List<UserRoleDO> findListByUserId(@Param("userId") Integer userId);
    List<UserRoleDO> findListByRoleId(@Param("roleId") Integer roleId);
}
