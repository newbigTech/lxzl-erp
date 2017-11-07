package com.lxzl.erp.common.domain.workflow;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.Date;


@JsonIgnoreProperties(ignoreUnknown = true)
public class WorkflowLinkDetail implements Serializable {

	private Integer workflowLinkDetailId;   //唯一标识
	private Integer workflowLinkId;   //工作流线ID
	private Integer workflowReferId;   //工作流关联ID
	private Integer workflowStep;   //流程当前步骤
	private Integer workflowCurrentNodeId;   //当前结点ID
	private Integer workflowPreviousNodeId;   //上节点ID
	private Integer workflowNextNodeId;   //下节点ID
	private Integer verifyUser;   //审核人
	private Date verifyTime;   //审核时间
	private Integer verifyStatus;   //审核状态
	private String verifyOpinion;   //审核意见
	private Integer dataStatus;   //状态：0不可用；1可用；2删除
	private String remark;   //备注
	private Date createTime;   //添加时间
	private String createUser;   //添加人
	private Date updateTime;   //添加时间
	private String updateUser;   //修改人


	public Integer getWorkflowLinkDetailId(){
		return workflowLinkDetailId;
	}

	public void setWorkflowLinkDetailId(Integer workflowLinkDetailId){
		this.workflowLinkDetailId = workflowLinkDetailId;
	}

	public Integer getWorkflowLinkId(){
		return workflowLinkId;
	}

	public void setWorkflowLinkId(Integer workflowLinkId){
		this.workflowLinkId = workflowLinkId;
	}

	public Integer getWorkflowReferId(){
		return workflowReferId;
	}

	public void setWorkflowReferId(Integer workflowReferId){
		this.workflowReferId = workflowReferId;
	}

	public Integer getWorkflowStep(){
		return workflowStep;
	}

	public void setWorkflowStep(Integer workflowStep){
		this.workflowStep = workflowStep;
	}

	public Integer getWorkflowCurrentNodeId(){
		return workflowCurrentNodeId;
	}

	public void setWorkflowCurrentNodeId(Integer workflowCurrentNodeId){
		this.workflowCurrentNodeId = workflowCurrentNodeId;
	}

	public Integer getWorkflowPreviousNodeId(){
		return workflowPreviousNodeId;
	}

	public void setWorkflowPreviousNodeId(Integer workflowPreviousNodeId){
		this.workflowPreviousNodeId = workflowPreviousNodeId;
	}

	public Integer getWorkflowNextNodeId(){
		return workflowNextNodeId;
	}

	public void setWorkflowNextNodeId(Integer workflowNextNodeId){
		this.workflowNextNodeId = workflowNextNodeId;
	}

	public Integer getVerifyUser(){
		return verifyUser;
	}

	public void setVerifyUser(Integer verifyUser){
		this.verifyUser = verifyUser;
	}

	public Date getVerifyTime(){
		return verifyTime;
	}

	public void setVerifyTime(Date verifyTime){
		this.verifyTime = verifyTime;
	}

	public Integer getVerifyStatus(){
		return verifyStatus;
	}

	public void setVerifyStatus(Integer verifyStatus){
		this.verifyStatus = verifyStatus;
	}

	public String getVerifyOpinion(){
		return verifyOpinion;
	}

	public void setVerifyOpinion(String verifyOpinion){
		this.verifyOpinion = verifyOpinion;
	}

	public Integer getDataStatus(){
		return dataStatus;
	}

	public void setDataStatus(Integer dataStatus){
		this.dataStatus = dataStatus;
	}

	public String getRemark(){
		return remark;
	}

	public void setRemark(String remark){
		this.remark = remark;
	}

	public Date getCreateTime(){
		return createTime;
	}

	public void setCreateTime(Date createTime){
		this.createTime = createTime;
	}

	public String getCreateUser(){
		return createUser;
	}

	public void setCreateUser(String createUser){
		this.createUser = createUser;
	}

	public Date getUpdateTime(){
		return updateTime;
	}

	public void setUpdateTime(Date updateTime){
		this.updateTime = updateTime;
	}

	public String getUpdateUser(){
		return updateUser;
	}

	public void setUpdateUser(String updateUser){
		this.updateUser = updateUser;
	}

}