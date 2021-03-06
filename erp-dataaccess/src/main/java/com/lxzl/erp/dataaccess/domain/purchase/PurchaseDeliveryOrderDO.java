package com.lxzl.erp.dataaccess.domain.purchase;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;
import org.springframework.data.annotation.Transient;

import java.util.Date;
import java.math.BigDecimal;
import java.util.List;

public class PurchaseDeliveryOrderDO  extends BaseDO {

	private Integer id;
	private Integer purchaseOrderId;
	private String purchaseDeliveryNo;
	private Integer warehouseId;
	private String warehouseSnapshot;
	private Integer isInvoice;
	private Integer isNew;
	private Integer purchaseDeliveryOrderStatus;
	private Date deliveryTime;
	private Integer dataStatus;
	private Integer ownerSupplierId;
	private String remark;

	@Transient
	private List<PurchaseDeliveryOrderProductDO> purchaseDeliveryOrderProductDOList;
	@Transient
	private List<PurchaseDeliveryOrderMaterialDO> purchaseDeliveryOrderMaterialDOList;
	@Transient
	private String purchaseOrderNo;
	@Transient
	private String ownerSupplierName;

	public Integer getId(){
		return id;
	}

	public void setId(Integer id){
		this.id = id;
	}

	public Integer getPurchaseOrderId(){
		return purchaseOrderId;
	}

	public void setPurchaseOrderId(Integer purchaseOrderId){
		this.purchaseOrderId = purchaseOrderId;
	}

	public String getPurchaseDeliveryNo(){
		return purchaseDeliveryNo;
	}

	public void setPurchaseDeliveryNo(String purchaseDeliveryNo){
		this.purchaseDeliveryNo = purchaseDeliveryNo;
	}

	public Integer getWarehouseId(){
		return warehouseId;
	}

	public void setWarehouseId(Integer warehouseId){
		this.warehouseId = warehouseId;
	}

	public String getWarehouseSnapshot() {
		return warehouseSnapshot;
	}

	public void setWarehouseSnapshot(String warehouseSnapshot) {
		this.warehouseSnapshot = warehouseSnapshot;
	}

	public Integer getIsInvoice(){
		return isInvoice;
	}

	public void setIsInvoice(Integer isInvoice){
		this.isInvoice = isInvoice;
	}

	public Integer getIsNew(){
		return isNew;
	}

	public void setIsNew(Integer isNew){
		this.isNew = isNew;
	}

	public Integer getPurchaseDeliveryOrderStatus(){
		return purchaseDeliveryOrderStatus;
	}

	public void setPurchaseDeliveryOrderStatus(Integer purchaseDeliveryOrderStatus){
		this.purchaseDeliveryOrderStatus = purchaseDeliveryOrderStatus;
	}

	public Date getDeliveryTime(){
		return deliveryTime;
	}

	public void setDeliveryTime(Date deliveryTime){
		this.deliveryTime = deliveryTime;
	}

	public Integer getDataStatus(){
		return dataStatus;
	}

	public void setDataStatus(Integer dataStatus){
		this.dataStatus = dataStatus;
	}

	public Integer getOwnerSupplierId(){
		return ownerSupplierId;
	}

	public void setOwnerSupplierId(Integer ownerSupplierId){
		this.ownerSupplierId = ownerSupplierId;
	}

	public String getRemark(){
		return remark;
	}

	public void setRemark(String remark){
		this.remark = remark;
	}

	public List<PurchaseDeliveryOrderProductDO> getPurchaseDeliveryOrderProductDOList() {
		return purchaseDeliveryOrderProductDOList;
	}

	public void setPurchaseDeliveryOrderProductDOList(List<PurchaseDeliveryOrderProductDO> purchaseDeliveryOrderProductDOList) {
		this.purchaseDeliveryOrderProductDOList = purchaseDeliveryOrderProductDOList;
	}

	public List<PurchaseDeliveryOrderMaterialDO> getPurchaseDeliveryOrderMaterialDOList() {
		return purchaseDeliveryOrderMaterialDOList;
	}

	public void setPurchaseDeliveryOrderMaterialDOList(List<PurchaseDeliveryOrderMaterialDO> purchaseDeliveryOrderMaterialDOList) {
		this.purchaseDeliveryOrderMaterialDOList = purchaseDeliveryOrderMaterialDOList;
	}

	public String getPurchaseOrderNo() {
		return purchaseOrderNo;
	}

	public void setPurchaseOrderNo(String purchaseOrderNo) {
		this.purchaseOrderNo = purchaseOrderNo;
	}

	public String getOwnerSupplierName() {
		return ownerSupplierName;
	}

	public void setOwnerSupplierName(String ownerSupplierName) {
		this.ownerSupplierName = ownerSupplierName;
	}
}