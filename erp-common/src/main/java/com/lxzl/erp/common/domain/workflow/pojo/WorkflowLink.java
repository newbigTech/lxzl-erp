package com.lxzl.erp.common.domain.workflow.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.base.BasePO;
import org.springframework.data.annotation.Transient;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


@JsonIgnoreProperties(ignoreUnknown = true)
public class WorkflowLink extends BasePO {

	private Integer workflowLinkId;   //唯一标识
	private String workflowLinkNo;		// 工作流编号
	private Integer workflowType;   //工作流类型
	private Integer workflowTemplateId;   //工作流模板ID
	private String workflowReferNo;   //工作流关联ID
	private Integer workflowStep;   //流程当前步骤
	private Integer workflowLastStep;   //流程最后步骤
	private Integer workflowCurrentNodeId;   //当前结点ID
	private Integer commitUser;				//提交人
	private String commitUserName;				//提交人姓名
	private Integer currentVerifyUser;		// 当前审核人
	private String verifyUserGroupId;		// 审核人组UUID，审核人为空时，该字段有值
	private String currentVerifyUserName;	// 当前审核人姓名
	private Integer currentVerifyStatus;	// 当前审核状态
	private String verifyMatters;
	private Integer dataStatus;   //状态：0不可用；1可用；2删除
	private String remark;   //备注
	private Date createTime;   //添加时间
	private String createUser;   //添加人
	private Date updateTime;   //添加时间
	private String updateUser;   //修改人
	private String dingdingWorkflowId;   //钉钉工作流编号

	private List<WorkflowLinkDetail> workflowLinkDetailList;
	private List<WorkflowVerifyUserGroup> workflowVerifyUserGroupList;

	private String workflowCurrentNodeName;
	private String workflowTemplateName;   //工作流模板名称

	private Boolean isLastStep;

	public Integer getWorkflowLinkId(){
		return workflowLinkId;
	}

	public void setWorkflowLinkId(Integer workflowLinkId){
		this.workflowLinkId = workflowLinkId;
	}

	public String getWorkflowLinkNo() {
		return workflowLinkNo;
	}

	public void setWorkflowLinkNo(String workflowLinkNo) {
		this.workflowLinkNo = workflowLinkNo;
	}

	public Integer getWorkflowType(){
		return workflowType;
	}

	public void setWorkflowType(Integer workflowType){
		this.workflowType = workflowType;
	}

	public Integer getWorkflowTemplateId(){
		return workflowTemplateId;
	}

	public void setWorkflowTemplateId(Integer workflowTemplateId){
		this.workflowTemplateId = workflowTemplateId;
	}

	public String getWorkflowReferNo() {
		return workflowReferNo;
	}

	public void setWorkflowReferNo(String workflowReferNo) {
		this.workflowReferNo = workflowReferNo;
	}

	public Integer getWorkflowStep(){
		return workflowStep;
	}

	public void setWorkflowStep(Integer workflowStep){
		this.workflowStep = workflowStep;
	}

	public Integer getWorkflowLastStep(){
		return workflowLastStep;
	}

	public void setWorkflowLastStep(Integer workflowLastStep){
		this.workflowLastStep = workflowLastStep;
	}

	public Integer getWorkflowCurrentNodeId(){
		return workflowCurrentNodeId;
	}

	public void setWorkflowCurrentNodeId(Integer workflowCurrentNodeId){
		this.workflowCurrentNodeId = workflowCurrentNodeId;
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

	public List<WorkflowLinkDetail> getWorkflowLinkDetailList() {
		return workflowLinkDetailList;
	}

	public void setWorkflowLinkDetailList(List<WorkflowLinkDetail> workflowLinkDetailList) {
		this.workflowLinkDetailList = workflowLinkDetailList;
	}

	public Integer getCurrentVerifyUser() {
		return currentVerifyUser;
	}

	public void setCurrentVerifyUser(Integer currentVerifyUser) {
		this.currentVerifyUser = currentVerifyUser;
	}

	public Integer getCurrentVerifyStatus() {
		return currentVerifyStatus;
	}

	public void setCurrentVerifyStatus(Integer currentVerifyStatus) {
		this.currentVerifyStatus = currentVerifyStatus;
	}

	public String getCurrentVerifyUserName() {
		return currentVerifyUserName;
	}

	public void setCurrentVerifyUserName(String currentVerifyUserName) {
		this.currentVerifyUserName = currentVerifyUserName;
	}

	public Integer getCommitUser() {
		return commitUser;
	}

	public void setCommitUser(Integer commitUser) {
		this.commitUser = commitUser;
	}

	public String getWorkflowCurrentNodeName() {
		return workflowCurrentNodeName;
	}

	public void setWorkflowCurrentNodeName(String workflowCurrentNodeName) {
		this.workflowCurrentNodeName = workflowCurrentNodeName;
	}

	public String getCommitUserName() {
		return commitUserName;
	}

	public void setCommitUserName(String commitUserName) {
		this.commitUserName = commitUserName;
	}

	public Boolean getLastStep() {
		return isLastStep;
	}

	public void setLastStep(Boolean lastStep) {
		isLastStep = lastStep;
	}

	public String getWorkflowTemplateName() {
		return workflowTemplateName;
	}

	public void setWorkflowTemplateName(String workflowTemplateName) {
		this.workflowTemplateName = workflowTemplateName;
	}

	public String getVerifyMatters() {
		return verifyMatters;
	}

	public void setVerifyMatters(String verifyMatters) {
		this.verifyMatters = verifyMatters;
	}

	public String getVerifyUserGroupId() { return verifyUserGroupId; }

	public void setVerifyUserGroupId(String verifyUserGroupId) { this.verifyUserGroupId = verifyUserGroupId; }

	public String getDingdingWorkflowId() {
		return dingdingWorkflowId;
	}

	public void setDingdingWorkflowId(String dingdingWorkflowId) {
		this.dingdingWorkflowId = dingdingWorkflowId;
	}

	public List<WorkflowVerifyUserGroup> getWorkflowVerifyUserGroupList() { return workflowVerifyUserGroupList; }

	public void setWorkflowVerifyUserGroupList(List<WorkflowVerifyUserGroup> workflowVerifyUserGroupList) { this.workflowVerifyUserGroupList = workflowVerifyUserGroupList; }
}