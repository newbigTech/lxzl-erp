package com.lxzl.erp.dataaccess.domain.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lxzl.se.dataaccess.mysql.domain.BaseDO;

import java.util.Date;
import java.util.List;

public class UserDO extends BaseDO {

	private Integer id;
	private Integer userType;
	private String userName;
	private String password;
	private String realName;
	private String email;
	private String phone;
	private Integer isActivated;
	private Integer isDisabled;
	@JsonFormat(shape = JsonFormat.Shape.STRING,timezone = "GMT+8")
	private Date registerTime;
	@JsonFormat(shape = JsonFormat.Shape.STRING,timezone = "GMT+8")
	private Date lastLoginTime;
	private String lastLoginIp;
	private String remark;
	private List<Integer> roleList;
	private List<UserRoleDO> userRoleList;
	private List<Integer> departmentList;
	private List<UserDepartmentDO> userDepartmentList;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getUserType() {
		return userType;
	}

	public void setUserType(Integer userType) {
		this.userType = userType;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Integer getIsActivated() {
		return isActivated;
	}

	public void setIsActivated(Integer isActivated) {
		this.isActivated = isActivated;
	}

	public Integer getIsDisabled() {
		return isDisabled;
	}

	public void setIsDisabled(Integer isDisabled) {
		this.isDisabled = isDisabled;
	}

	public Date getRegisterTime() {
		return registerTime;
	}

	public void setRegisterTime(Date registerTime) {
		this.registerTime = registerTime;
	}

	public Date getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public String getLastLoginIp() {
		return lastLoginIp;
	}

	public void setLastLoginIp(String lastLoginIp) {
		this.lastLoginIp = lastLoginIp;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public List<Integer> getRoleList() {
		return roleList;
	}

	public void setRoleList(List<Integer> roleList) {
		this.roleList = roleList;
	}

	public List<UserRoleDO> getUserRoleList() {
		return userRoleList;
	}

	public void setUserRoleList(List<UserRoleDO> userRoleList) {
		this.userRoleList = userRoleList;
	}

	public List<Integer> getDepartmentList() {
		return departmentList;
	}

	public void setDepartmentList(List<Integer> departmentList) {
		this.departmentList = departmentList;
	}

	public List<UserDepartmentDO> getUserDepartmentList() {
		return userDepartmentList;
	}

	public void setUserDepartmentList(List<UserDepartmentDO> userDepartmentList) {
		this.userDepartmentList = userDepartmentList;
	}
}
