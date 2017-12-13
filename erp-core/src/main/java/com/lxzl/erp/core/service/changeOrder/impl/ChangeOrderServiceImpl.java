package com.lxzl.erp.core.service.changeOrder.impl;

import com.alibaba.fastjson.JSON;
import com.lxzl.erp.common.constant.*;
import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.changeOrder.*;
import com.lxzl.erp.common.domain.changeOrder.pojo.ChangeOrder;
import com.lxzl.erp.common.domain.changeOrder.pojo.ChangeOrderConsignInfo;
import com.lxzl.erp.common.domain.changeOrder.pojo.ChangeOrderMaterialBulk;
import com.lxzl.erp.common.domain.changeOrder.pojo.ChangeOrderProductEquipment;
import com.lxzl.erp.common.domain.product.pojo.Product;
import com.lxzl.erp.common.domain.user.pojo.User;
import com.lxzl.erp.common.domain.warehouse.pojo.Warehouse;
import com.lxzl.erp.common.util.CollectionUtil;
import com.lxzl.erp.common.util.GenerateNoUtil;
import com.lxzl.erp.core.service.changeOrder.ChangeOrderService;
import com.lxzl.erp.core.service.customer.order.CustomerOrderSupport;
import com.lxzl.erp.core.service.product.ProductService;
import com.lxzl.erp.core.service.user.impl.support.UserSupport;
import com.lxzl.erp.core.service.warehouse.WarehouseService;
import com.lxzl.erp.core.service.workflow.WorkflowService;
import com.lxzl.erp.dataaccess.dao.mysql.changeOrder.*;
import com.lxzl.erp.dataaccess.dao.mysql.customer.CustomerMapper;
import com.lxzl.erp.dataaccess.dao.mysql.material.BulkMaterialMapper;
import com.lxzl.erp.dataaccess.dao.mysql.material.MaterialMapper;
import com.lxzl.erp.dataaccess.dao.mysql.product.ProductEquipmentMapper;
import com.lxzl.erp.dataaccess.dao.mysql.product.ProductSkuMapper;
import com.lxzl.erp.dataaccess.domain.changeOrder.*;
import com.lxzl.erp.dataaccess.domain.customer.CustomerDO;
import com.lxzl.erp.dataaccess.domain.material.BulkMaterialDO;
import com.lxzl.erp.dataaccess.domain.material.MaterialDO;
import com.lxzl.erp.dataaccess.domain.product.ProductEquipmentDO;
import com.lxzl.erp.dataaccess.domain.product.ProductSkuDO;
import com.lxzl.se.common.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.*;

@Service
public class ChangeOrderServiceImpl implements ChangeOrderService {

    private static final Logger logger = LoggerFactory.getLogger(ChangeOrderServiceImpl.class);

    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public ServiceResult<String, String> add(AddChangeOrderParam addChangeOrderParam) {

        ServiceResult<String, String> serviceResult = new ServiceResult<>();
        CustomerDO customerDO = customerMapper.findCustomerPersonByNo(addChangeOrderParam.getCustomerNo());
        if (customerDO == null) {
            serviceResult.setErrorCode(ErrorCode.CUSTOMER_NOT_EXISTS);
            return serviceResult;
        }
        List<ChangeProductSkuPairs> changeOrderParamProductSkuList = addChangeOrderParam.getChangeProductSkuPairsList();
        List<ChangeMaterialPairs> changeOrderParamMaterialList = addChangeOrderParam.getChangeMaterialPairsList();
        if (CollectionUtil.isEmpty(changeOrderParamProductSkuList) && CollectionUtil.isEmpty(changeOrderParamMaterialList)) {
            serviceResult.setErrorCode(ErrorCode.RECORD_NOT_EXISTS);
            return serviceResult;
        }
        //用户在租sku统计
        //todo 可退数量还是有问题，后面再处理
        Map<String, Object> findRentMap = customerOrderSupport.getCustomerAllMap(customerDO.getId());
        List<ProductSkuDO> oldProductSkuDOList = productSkuMapper.findSkuRent(findRentMap);
        Map<Integer, ProductSkuDO> oldSkuCountMap = new HashMap<>();
        if (CollectionUtil.isNotEmpty(oldProductSkuDOList)) {
            for (ProductSkuDO productSkuDO : oldProductSkuDOList) {
                oldSkuCountMap.put(productSkuDO.getId(), productSkuDO);
            }
        }
        //用户在租物料统计
        //todo 可退数量还是有问题，后面再处理
        List<MaterialDO> oldMaterialDOList = materialMapper.findMaterialRent(findRentMap);
        Map<String, MaterialDO> oldMaterialCountMap = new HashMap<>();
        if (CollectionUtil.isNotEmpty(oldMaterialDOList)) {
            for (MaterialDO materialDO : oldMaterialDOList) {
                oldMaterialCountMap.put(materialDO.getMaterialNo(), materialDO);
            }
        }
        //累计换货sku总数
        Integer totalChangeProductCount = 0;
        //累计换货物料总数
        Integer totalChangeMaterialCount = 0;
        Date now = new Date();
        //构造待保存换货单商品项
        List<ChangeOrderProductDO> changeOrderProductDOList = new ArrayList<>();
        //如果要更换的sku不在在租列表，或者要更换的sku数量大于可换数量，返回相应错误
        if (CollectionUtil.isNotEmpty(changeOrderParamProductSkuList)) {
            for (ChangeProductSkuPairs changeProductSkuPairs : changeOrderParamProductSkuList) {
                ProductSkuDO oldSkuRent = oldSkuCountMap.get(changeProductSkuPairs.getProductSkuIdSrc());
                if (changeProductSkuPairs.getChangeCount() > oldSkuRent.getCanProcessCount()) {//更换的sku数量大于可换数量
                    serviceResult.setErrorCode(ErrorCode.CUSTOMER_RETURN_TOO_MORE);
                    return serviceResult;
                }
                if (oldSkuRent == null) {//如果要更换的sku不在在租列表
                    serviceResult.setErrorCode(ErrorCode.CUSTOMER_NOT_RENT_THIS);
                    return serviceResult;
                }
                totalChangeProductCount += changeProductSkuPairs.getChangeCount();
                ServiceResult<String, Product> productSkuSrcResult = productService.queryProductBySkuId(changeProductSkuPairs.getProductSkuIdSrc());
                if (!ErrorCode.SUCCESS.equals(productSkuSrcResult.getErrorCode()) || productSkuSrcResult.getResult() == null) {
                    serviceResult.setErrorCode(ErrorCode.PRODUCT_SKU_IS_NULL_OR_NOT_EXISTS);
                }
                ServiceResult<String, Product> productSkuDestResult = productService.queryProductBySkuId(changeProductSkuPairs.getProductSkuIdDest());
                if (!ErrorCode.SUCCESS.equals(productSkuDestResult.getErrorCode()) || productSkuDestResult.getResult() == null) {
                    serviceResult.setErrorCode(ErrorCode.PRODUCT_SKU_IS_NULL_OR_NOT_EXISTS);
                }
                ChangeOrderProductDO changeOrderProductDO = new ChangeOrderProductDO();
                changeOrderProductDO.setChangeProductSkuIdSrc(changeProductSkuPairs.getProductSkuIdSrc());
                changeOrderProductDO.setChangeProductSkuIdDest(changeProductSkuPairs.getProductSkuIdDest());
                changeOrderProductDO.setChangeProductSkuCount(changeProductSkuPairs.getChangeCount());
                changeOrderProductDO.setRealChangeProductSkuCount(0);
                changeOrderProductDO.setChangeProductSkuSnapshotSrc(JSON.toJSONString(productSkuSrcResult.getResult()));
                changeOrderProductDO.setChangeProductSkuSnapshotDest(JSON.toJSONString(productSkuDestResult.getResult()));
                changeOrderProductDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                changeOrderProductDO.setCreateUser(userSupport.getCurrentUserId().toString());
                changeOrderProductDO.setCreateTime(now);
                changeOrderProductDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                changeOrderProductDO.setUpdateTime(now);
                changeOrderProductDOList.add(changeOrderProductDO);
            }
        }
        //构造待保存换货单物料项
        List<ChangeOrderMaterialDO> changeOrderMaterialDOList = new ArrayList<>();
        //如果要换货的物料不在在租列表，或者要换货的物料数量大于在租数量，返回相应错误
        if (CollectionUtil.isNotEmpty(changeOrderParamMaterialList)) {
            for (ChangeMaterialPairs changeMaterialPairs : changeOrderParamMaterialList) {
                MaterialDO srcMaterial = oldMaterialCountMap.get(changeMaterialPairs.getMaterialNoSrc());
                if (changeMaterialPairs.getChangeCount() > srcMaterial.getCanProcessCount()) {//退还的物料数量大于可租数量
                    serviceResult.setErrorCode(ErrorCode.CUSTOMER_RETURN_TOO_MORE);
                    return serviceResult;
                }
                if (srcMaterial == null) {//如果要退还的物料不在在租列表
                    serviceResult.setErrorCode(ErrorCode.CUSTOMER_NOT_RENT_THIS);
                    return serviceResult;
                }
                totalChangeMaterialCount += changeMaterialPairs.getChangeCount();
                MaterialDO destMaterial = materialMapper.findByNo(changeMaterialPairs.getMaterialNoDest());

                ChangeOrderMaterialDO changeOrderMaterialDO = new ChangeOrderMaterialDO();
                changeOrderMaterialDO.setChangeMaterialIdSrc(srcMaterial.getId());
                changeOrderMaterialDO.setChangeMaterialIdDest(destMaterial.getId());
                changeOrderMaterialDO.setChangeMaterialSnapshotSrc(JSON.toJSONString(srcMaterial));
                changeOrderMaterialDO.setChangeMaterialSnapshotSrc(JSON.toJSONString(destMaterial));
                changeOrderMaterialDO.setChangeMaterialCount(changeMaterialPairs.getChangeCount());
                changeOrderMaterialDO.setRealChangeMaterialCount(0);
                changeOrderMaterialDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                changeOrderMaterialDO.setCreateTime(now);
                changeOrderMaterialDO.setCreateUser(userSupport.getCurrentUserId().toString());
                changeOrderMaterialDO.setUpdateTime(now);
                changeOrderMaterialDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                changeOrderMaterialDOList.add(changeOrderMaterialDO);
            }
        }
        //创建租赁换货单
        ChangeOrderDO changeOrderDO = new ChangeOrderDO();
        changeOrderDO.setChangeOrderNo(GenerateNoUtil.generateChangeOrderNo(now));
        changeOrderDO.setCustomerId(customerDO.getId());
        changeOrderDO.setCustomerNo(customerDO.getCustomerNo());
        changeOrderDO.setTotalChangeProductCount(totalChangeProductCount);
        changeOrderDO.setTotalChangeMaterialCount(totalChangeMaterialCount);
        changeOrderDO.setChangeReasonType(ChangeReasonType.CHANGE_REASON_TYPE_GO_UP);
        changeOrderDO.setChangeReason(addChangeOrderParam.getChangeReason());
        changeOrderDO.setChangeMode(ChangeMode.CHANGE_MODE_TO_DOOR);
        changeOrderDO.setChangeOrderStatus(ChangeOrderStatus.CHANGE_ORDER_STATUS_WAIT_COMMIT);
        changeOrderDO.setOwner(userSupport.getCurrentUserId());
        changeOrderDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        changeOrderDO.setRemark(addChangeOrderParam.getRemark());
        changeOrderDO.setCreateTime(now);
        changeOrderDO.setUpdateTime(now);
        changeOrderDO.setCreateUser(userSupport.getCurrentUserId().toString());
        changeOrderDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        changeOrderMapper.save(changeOrderDO);

        //保存取货地址信息
        ChangeOrderConsignInfoDO changeOrderConsignInfoDO = new ChangeOrderConsignInfoDO();
        changeOrderConsignInfoDO.setChangeOrderId(changeOrderDO.getId());
        changeOrderConsignInfoDO.setChangeOrderNo(changeOrderDO.getChangeOrderNo());
        changeOrderConsignInfoDO.setConsigneeName(addChangeOrderParam.getChangeOrderConsignInfo().getConsigneeName());
        changeOrderConsignInfoDO.setConsigneePhone(addChangeOrderParam.getChangeOrderConsignInfo().getConsigneePhone());
        changeOrderConsignInfoDO.setProvince(addChangeOrderParam.getChangeOrderConsignInfo().getProvince());
        changeOrderConsignInfoDO.setCity(addChangeOrderParam.getChangeOrderConsignInfo().getCity());
        changeOrderConsignInfoDO.setDistrict(addChangeOrderParam.getChangeOrderConsignInfo().getDistrict());
        changeOrderConsignInfoDO.setAddress(addChangeOrderParam.getChangeOrderConsignInfo().getAddress());
        changeOrderConsignInfoDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        changeOrderConsignInfoDO.setRemark(addChangeOrderParam.getChangeOrderConsignInfo().getRemark());
        changeOrderConsignInfoDO.setCreateTime(now);
        changeOrderConsignInfoDO.setUpdateTime(now);
        changeOrderConsignInfoDO.setCreateUser(userSupport.getCurrentUserId().toString());
        changeOrderConsignInfoDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        changeOrderConsignInfoMapper.save(changeOrderConsignInfoDO);

        //保存退还商品项
        if (CollectionUtil.isNotEmpty(changeOrderProductDOList)) {
            changeOrderProductMapper.batchSave(changeOrderDO.getId(), changeOrderDO.getChangeOrderNo(), changeOrderProductDOList);
        }
        //保存退还物料项
        if (CollectionUtil.isNotEmpty(changeOrderMaterialDOList)) {
            changeOrderMaterialMapper.batchSave(changeOrderDO.getId(), changeOrderDO.getChangeOrderNo(), changeOrderMaterialDOList);
        }
        serviceResult.setResult(changeOrderDO.getChangeOrderNo());
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        return serviceResult;
    }

    @Override
    public ServiceResult<String, String> update(UpdateChangeOrderParam  updateChangeOrderParam) {
        ServiceResult<String, String> serviceResult = new ServiceResult<>();
        ChangeOrderDO changeOrderDO = changeOrderMapper.findByNo(updateChangeOrderParam.getChangeOrderNo());
        if (changeOrderDO == null) {
            serviceResult.setErrorCode(ErrorCode.CHANGE_ORDER_NOT_EXISTS);
            return serviceResult;
        }
        if(!ChangeOrderStatus.CHANGE_ORDER_STATUS_WAIT_COMMIT.equals(changeOrderDO.getChangeOrderStatus())){
            serviceResult.setErrorCode(ErrorCode.CHANGE_ORDER_NOT_EXISTS);
            return serviceResult;
        }
        ChangeOrderConsignInfoDO changeOrderConsignInfoDO = null;
        if(updateChangeOrderParam.getChangeOrderConsignInfo()!=null){
            changeOrderConsignInfoDO = changeOrderConsignInfoMapper.findById(updateChangeOrderParam.getChangeOrderConsignInfo().getChangeOrderConsignInfoId());
            if(changeOrderConsignInfoDO==null){
                serviceResult.setErrorCode(ErrorCode.CUSTOMER_CONSIGN_INFO_NOT_EXISTS);
                return serviceResult;
            }
        }
        List<ChangeProductSkuPairs> changeOrderParamProductSkuList = updateChangeOrderParam.getChangeProductSkuPairsList();
        List<ChangeMaterialPairs> changeOrderParamMaterialList = updateChangeOrderParam.getChangeMaterialPairsList();
        if (CollectionUtil.isEmpty(changeOrderParamProductSkuList) && CollectionUtil.isEmpty(changeOrderParamMaterialList)) {
            serviceResult.setErrorCode(ErrorCode.RECORD_NOT_EXISTS);
            return serviceResult;
        }
        //用户在租sku统计
        //todo 可退数量还是有问题，后面再处理
        Map<String, Object> findRentMap = customerOrderSupport.getCustomerAllMap(changeOrderDO.getCustomerId());
        List<ProductSkuDO> oldProductSkuDOList = productSkuMapper.findSkuRent(findRentMap);
        Map<Integer, ProductSkuDO> oldSkuCountMap = new HashMap<>();
        if (CollectionUtil.isNotEmpty(oldProductSkuDOList)) {
            for (ProductSkuDO productSkuDO : oldProductSkuDOList) {
                oldSkuCountMap.put(productSkuDO.getId(), productSkuDO);
            }
        }
        //用户在租物料统计
        //todo 可退数量还是有问题，后面再处理
        List<MaterialDO> oldMaterialDOList = materialMapper.findMaterialRent(findRentMap);
        Map<String, MaterialDO> oldMaterialCountMap = new HashMap<>();
        if (CollectionUtil.isNotEmpty(oldMaterialDOList)) {
            for (MaterialDO materialDO : oldMaterialDOList) {
                oldMaterialCountMap.put(materialDO.getMaterialNo(), materialDO);
            }
        }
        //累计换货sku总数
        Integer totalChangeProductCount = 0;
        //累计换货物料总数
        Integer totalChangeMaterialCount = 0;
        Date now = new Date();
        //构造待保存换货单商品项
        List<ChangeOrderProductDO> changeOrderProductDOListForSave = new ArrayList<>();
        List<ChangeOrderProductDO> changeOrderProductDOListForUpdate = new ArrayList<>();
        //如果要更换的sku不在在租列表，或者要更换的sku数量大于可换数量，返回相应错误
        if (CollectionUtil.isNotEmpty(changeOrderParamProductSkuList)) {
            for (ChangeProductSkuPairs changeProductSkuPairs : changeOrderParamProductSkuList) {
                if(changeProductSkuPairs.getChangeOrderProductId()==null){
                    ProductSkuDO oldSkuRent = oldSkuCountMap.get(changeProductSkuPairs.getProductSkuIdSrc());
                    if (changeProductSkuPairs.getChangeCount() > oldSkuRent.getCanProcessCount()) {//更换的sku数量大于可换数量
                        serviceResult.setErrorCode(ErrorCode.CUSTOMER_RETURN_TOO_MORE);
                        return serviceResult;
                    }
                    if (oldSkuRent == null) {//如果要更换的sku不在在租列表
                        serviceResult.setErrorCode(ErrorCode.CUSTOMER_NOT_RENT_THIS);
                        return serviceResult;
                    }
                    totalChangeProductCount += changeProductSkuPairs.getChangeCount();
                    ServiceResult<String, Product> productSkuSrcResult = productService.queryProductBySkuId(changeProductSkuPairs.getProductSkuIdSrc());
                    if (!ErrorCode.SUCCESS.equals(productSkuSrcResult.getErrorCode()) || productSkuSrcResult.getResult() == null) {
                        serviceResult.setErrorCode(ErrorCode.PRODUCT_SKU_IS_NULL_OR_NOT_EXISTS);
                    }
                    ServiceResult<String, Product> productSkuDestResult = productService.queryProductBySkuId(changeProductSkuPairs.getProductSkuIdDest());
                    if (!ErrorCode.SUCCESS.equals(productSkuDestResult.getErrorCode()) || productSkuDestResult.getResult() == null) {
                        serviceResult.setErrorCode(ErrorCode.PRODUCT_SKU_IS_NULL_OR_NOT_EXISTS);
                    }
                    ChangeOrderProductDO changeOrderProductDO = new ChangeOrderProductDO();
                    changeOrderProductDO.setChangeProductSkuIdSrc(changeProductSkuPairs.getProductSkuIdSrc());
                    changeOrderProductDO.setChangeProductSkuIdDest(changeProductSkuPairs.getProductSkuIdDest());
                    changeOrderProductDO.setChangeProductSkuCount(changeProductSkuPairs.getChangeCount());
                    changeOrderProductDO.setRealChangeProductSkuCount(0);
                    changeOrderProductDO.setChangeProductSkuSnapshotSrc(JSON.toJSONString(productSkuSrcResult.getResult()));
                    changeOrderProductDO.setChangeProductSkuSnapshotDest(JSON.toJSONString(productSkuDestResult.getResult()));
                    changeOrderProductDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                    changeOrderProductDO.setCreateUser(userSupport.getCurrentUserId().toString());
                    changeOrderProductDO.setCreateTime(now);
                    changeOrderProductDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                    changeOrderProductDO.setUpdateTime(now);
                    changeOrderProductDOListForSave.add(changeOrderProductDO);
                }else{
                    ChangeOrderProductDO changeOrderProductDO = changeOrderProductMapper.findById(changeProductSkuPairs.getChangeOrderProductId());
                    totalChangeProductCount = totalChangeProductCount+changeProductSkuPairs.getChangeCount()-changeOrderProductDO.getChangeProductSkuCount();
                    changeOrderProductDO.setChangeProductSkuCount(changeProductSkuPairs.getChangeCount());
                    changeOrderProductDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                    changeOrderProductDO.setUpdateTime(now);
                    changeOrderProductDOListForUpdate.add(changeOrderProductDO);
                }
            }
        }
        //构造待保存换货单物料项
        List<ChangeOrderMaterialDO> changeOrderMaterialDOListForSave = new ArrayList<>();
        List<ChangeOrderMaterialDO> changeOrderMaterialDOListForUpdate = new ArrayList<>();
        //如果要换货的物料不在在租列表，或者要换货的物料数量大于在租数量，返回相应错误
        if (CollectionUtil.isNotEmpty(changeOrderParamMaterialList)) {
            for (ChangeMaterialPairs changeMaterialPairs : changeOrderParamMaterialList) {
                if(changeMaterialPairs.getChangeOrderMaterialId()==null){
                    MaterialDO srcMaterial = oldMaterialCountMap.get(changeMaterialPairs.getMaterialNoSrc());
                    if (changeMaterialPairs.getChangeCount() > srcMaterial.getCanProcessCount()) {//退还的物料数量大于可租数量
                        serviceResult.setErrorCode(ErrorCode.CUSTOMER_RETURN_TOO_MORE);
                        return serviceResult;
                    }
                    if (srcMaterial == null) {//如果要退还的物料不在在租列表
                        serviceResult.setErrorCode(ErrorCode.CUSTOMER_NOT_RENT_THIS);
                        return serviceResult;
                    }
                    totalChangeMaterialCount += changeMaterialPairs.getChangeCount();
                    MaterialDO destMaterial = materialMapper.findByNo(changeMaterialPairs.getMaterialNoDest());

                    ChangeOrderMaterialDO changeOrderMaterialDO = new ChangeOrderMaterialDO();
                    changeOrderMaterialDO.setChangeMaterialIdSrc(srcMaterial.getId());
                    changeOrderMaterialDO.setChangeMaterialIdDest(destMaterial.getId());
                    changeOrderMaterialDO.setChangeMaterialSnapshotSrc(JSON.toJSONString(srcMaterial));
                    changeOrderMaterialDO.setChangeMaterialSnapshotSrc(JSON.toJSONString(destMaterial));
                    changeOrderMaterialDO.setChangeMaterialCount(changeMaterialPairs.getChangeCount());
                    changeOrderMaterialDO.setRealChangeMaterialCount(0);
                    changeOrderMaterialDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                    changeOrderMaterialDO.setCreateTime(now);
                    changeOrderMaterialDO.setCreateUser(userSupport.getCurrentUserId().toString());
                    changeOrderMaterialDO.setUpdateTime(now);
                    changeOrderMaterialDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                    changeOrderMaterialDOListForSave.add(changeOrderMaterialDO);
                }else{
                    ChangeOrderMaterialDO changeOrderMaterialDO = changeOrderMaterialMapper.findById(changeMaterialPairs.getChangeOrderMaterialId());
                    totalChangeProductCount = totalChangeProductCount+changeMaterialPairs.getChangeCount()-changeOrderMaterialDO.getChangeMaterialCount();
                    changeOrderMaterialDO.setChangeMaterialCount(changeMaterialPairs.getChangeCount());
                    changeOrderMaterialDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                    changeOrderMaterialDO.setUpdateTime(now);
                    changeOrderMaterialDOListForUpdate.add(changeOrderMaterialDO);
                }
            }
        }
        //更新租赁换货单
        changeOrderDO.setTotalChangeProductCount(totalChangeProductCount);
        changeOrderDO.setTotalChangeMaterialCount(totalChangeMaterialCount);
        changeOrderDO.setChangeReason(updateChangeOrderParam.getChangeReason());
        changeOrderDO.setRemark(updateChangeOrderParam.getRemark());
        changeOrderDO.setUpdateTime(now);
        changeOrderDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        changeOrderMapper.update(changeOrderDO);

        //更新取货地址信息
        if(changeOrderConsignInfoDO!=null){
            changeOrderConsignInfoDO.setConsigneeName(updateChangeOrderParam.getChangeOrderConsignInfo().getConsigneeName());
            changeOrderConsignInfoDO.setConsigneePhone(updateChangeOrderParam.getChangeOrderConsignInfo().getConsigneePhone());
            changeOrderConsignInfoDO.setProvince(updateChangeOrderParam.getChangeOrderConsignInfo().getProvince());
            changeOrderConsignInfoDO.setCity(updateChangeOrderParam.getChangeOrderConsignInfo().getCity());
            changeOrderConsignInfoDO.setDistrict(updateChangeOrderParam.getChangeOrderConsignInfo().getDistrict());
            changeOrderConsignInfoDO.setAddress(updateChangeOrderParam.getChangeOrderConsignInfo().getAddress());
            changeOrderConsignInfoDO.setRemark(updateChangeOrderParam.getChangeOrderConsignInfo().getRemark());
            changeOrderConsignInfoDO.setUpdateTime(now);
            changeOrderConsignInfoDO.setUpdateUser(userSupport.getCurrentUserId().toString());
            changeOrderConsignInfoMapper.update(changeOrderConsignInfoDO);
        }

        //批量保存换货商品项
        if (CollectionUtil.isNotEmpty(changeOrderProductDOListForSave)) {
            changeOrderProductMapper.batchSave(changeOrderDO.getId(), changeOrderDO.getChangeOrderNo(), changeOrderProductDOListForSave);
        }
        //批量更新换货商品项
        if (CollectionUtil.isNotEmpty(changeOrderProductDOListForUpdate)) {
            changeOrderProductMapper.batchUpdate(changeOrderProductDOListForUpdate);
        }
        //批量保存换货物料项
        if (CollectionUtil.isNotEmpty(changeOrderMaterialDOListForSave)) {
            changeOrderMaterialMapper.batchSave(changeOrderDO.getId(), changeOrderDO.getChangeOrderNo(), changeOrderMaterialDOListForSave);
        }
        //批量更新换货物料项
        if (CollectionUtil.isNotEmpty(changeOrderMaterialDOListForUpdate)) {
            changeOrderMaterialMapper.batchUpdate(changeOrderMaterialDOListForUpdate);
        }
        serviceResult.setResult(changeOrderDO.getChangeOrderNo());
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        return serviceResult;
    }

    @Override
    public ServiceResult<String, String> commit(ChangeOrderCommitParam changeOrderCommitParam) {
        ServiceResult<String, String> result = new ServiceResult<>();
        Date now = new Date();
        //校验换货单是否存在
        ChangeOrderDO changeOrderDO = changeOrderMapper.findByNo(changeOrderCommitParam.getChangeOrderNo());
        if (changeOrderDO == null) {
            result.setErrorCode(ErrorCode.CHANGE_ORDER_NOT_EXISTS);
            return result;
        } else if (!ChangeOrderStatus.CHANGE_ORDER_STATUS_WAIT_COMMIT.equals(changeOrderDO.getChangeOrderStatus())) {
            //只有待提交状态的换货单可以提交
            result.setErrorCode(ErrorCode.CHANGE_ORDER_COMMITTED_CAN_NOT_COMMIT_AGAIN);
            return result;
        }
        if (!changeOrderDO.getCreateUser().equals(userSupport.getCurrentUserId().toString())) {
            //只有创建换货单本人可以提交
            result.setErrorCode(ErrorCode.COMMIT_ONLY_SELF);
            return result;
        }
        ServiceResult<String, Boolean> needVerifyResult = workflowService.isNeedVerify(WorkflowType.WORKFLOW_TYPE_PURCHASE);
        if (!ErrorCode.SUCCESS.equals(needVerifyResult.getErrorCode())) {
            result.setErrorCode(needVerifyResult.getErrorCode());
            return result;
        } else if (needVerifyResult.getResult()) {
            if (changeOrderCommitParam.getVerifyUserId() == null) {
                result.setErrorCode(ErrorCode.VERIFY_USER_NOT_NULL);
                return result;
            }
            //调用提交审核服务
            ServiceResult<String, String> verifyResult = workflowService.commitWorkFlow(WorkflowType.WORKFLOW_TYPE_CHANGE, changeOrderDO.getChangeOrderNo(), changeOrderCommitParam.getVerifyUserId(), changeOrderCommitParam.getRemark());
            //修改提交审核状态
            if (ErrorCode.SUCCESS.equals(verifyResult.getErrorCode())) {
                changeOrderDO.setChangeOrderStatus(ChangeOrderStatus.CHANGE_ORDER_STATUS_VERIFYING);
                changeOrderDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                changeOrderDO.setUpdateTime(now);
                changeOrderMapper.update(changeOrderDO);
                return verifyResult;
            } else {
                result.setErrorCode(verifyResult.getErrorCode());
                return result;
            }
        } else {
            changeOrderDO.setChangeOrderStatus(ChangeOrderStatus.CHANGE_ORDER_STATUS_WAIT_STOCK);
            changeOrderDO.setUpdateUser(userSupport.getCurrentUserId().toString());
            changeOrderDO.setUpdateTime(now);
            changeOrderMapper.update(changeOrderDO);
            result.setErrorCode(ErrorCode.SUCCESS);
            return result;
        }
    }

    /**
     * 接收审核结果通知
     *
     * @param verifyResult
     * @param businessNo
     */
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    @Override
    public boolean receiveVerifyResult(boolean verifyResult, String businessNo) {
        try {
            ChangeOrderDO changeOrderDO = changeOrderMapper.findByNo(businessNo);
            if (changeOrderDO == null) {
                return false;
            }
            //不是审核中状态的收货单，拒绝处理
            if (!ChangeOrderStatus.CHANGE_ORDER_STATUS_VERIFYING.equals(changeOrderDO.getChangeOrderStatus())) {
                return false;
            }
            if (verifyResult) {
                changeOrderDO.setChangeOrderStatus(ChangeOrderStatus.CHANGE_ORDER_STATUS_WAIT_STOCK);
            } else {
                changeOrderDO.setChangeOrderStatus(ChangeOrderStatus.CHANGE_ORDER_STATUS_WAIT_COMMIT);
            }
            changeOrderMapper.update(changeOrderDO);
            return true;
        } catch (Exception e) {
            logger.error("【换货单审核后，业务处理异常】", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
            logger.error("【数据已回滚】");
            return false;
        } catch (Throwable t) {
            logger.error("【换货单审核后，业务处理异常】", t);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
            logger.error("【数据已回滚】");
            return false;
        }
    }

    @Override
    public ServiceResult<String, String> stockUpByChange(StockUpByChangeParam param) {
        ServiceResult<String, String> result = new ServiceResult<>();
        User loginUser = userSupport.getCurrentUser();
        Date currentTime = new Date();

        ChangeOrderDO changeOrderDO = changeOrderMapper.findByNo(param.getChangeOrderNo());
        if (changeOrderDO == null) {
            result.setErrorCode(ErrorCode.CHANGE_ORDER_NOT_EXISTS);
            return result;
        }
        if (!ChangeOrderStatus.CHANGE_ORDER_STATUS_WAIT_STOCK.equals(changeOrderDO.getChangeOrderStatus()) ||
                !ChangeOrderStatus.CHANGE_ORDER_STATUS_STOCKING.equals(changeOrderDO.getChangeOrderStatus())) {
            result.setErrorCode(ErrorCode.CHANGE_ORDER_STATUS_CAN_NOT_STOCK_UP);
            return result;
        }
        ServiceResult<String, List<Warehouse>> warehouseResult = warehouseService.getWarehouseByCurrentCompany();
        if (!ErrorCode.SUCCESS.equals(warehouseResult.getErrorCode())) {
            result.setErrorCode(warehouseResult.getErrorCode());
            return result;
        }
        // 取仓库，本公司的默认仓库和客户仓
        List<Warehouse> warehouseList = warehouseResult.getResult();
        Warehouse srcWarehouse = null;
        for (Warehouse warehouse : warehouseList) {
            if (WarehouseType.WAREHOUSE_TYPE_DEFAULT.equals(warehouse.getWarehouseType())) {
                srcWarehouse = warehouse;
            }
        }
        if (srcWarehouse == null) {
            result.setErrorCode(ErrorCode.WAREHOUSE_NOT_EXISTS);
            return result;
        }

        if (!CommonConstant.COMMON_DATA_OPERATION_TYPE_ADD.equals(param.getOperationType())
                && !CommonConstant.COMMON_DATA_OPERATION_TYPE_DELETE.equals(param.getOperationType())) {
            result.setErrorCode(ErrorCode.PARAM_IS_ERROR);
            return result;
        }


        if (CommonConstant.COMMON_DATA_OPERATION_TYPE_ADD.equals(param.getOperationType())) {
            //todo 添加更换后设备，添加更换后物料
            ServiceResult<String, Object> addOrderDestItemResult = addOrderDestItemResultDest(param, changeOrderDO);
            if (!ErrorCode.SUCCESS.equals(addOrderDestItemResult.getErrorCode())) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                result.setErrorCode(addOrderDestItemResult.getErrorCode());
                return result;
            }
        } else if (CommonConstant.COMMON_DATA_OPERATION_TYPE_DELETE.equals(param.getOperationType())) {
            //todo 删除更换后设备，删除更换后物料
            ServiceResult<String, Object> removeOrderDestItemResult = removeOrderDestItemResultDest(param, changeOrderDO);
            if (!ErrorCode.SUCCESS.equals(removeOrderDestItemResult.getErrorCode())) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                result.setErrorCode(removeOrderDestItemResult.getErrorCode());
                return result;
            }
        }

        changeOrderDO.setChangeOrderStatus(ChangeOrderStatus.CHANGE_ORDER_STATUS_STOCKING);
        changeOrderDO.setUpdateUser(loginUser.getUserId().toString());
        changeOrderDO.setUpdateTime(currentTime);
        changeOrderMapper.update(changeOrderDO);
        result.setResult(changeOrderDO.getChangeOrderNo());
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    @Override
    public ServiceResult<String, String> delivery(ChangeOrder changeOrder) {
        ServiceResult<String, String> serviceResult = new ServiceResult<>();
        ChangeOrderDO changeOrderDO = changeOrderMapper.findByNo(changeOrder.getChangeOrderNo());
        if(changeOrderDO==null){
            serviceResult.setErrorCode(ErrorCode.CHANGE_ORDER_NOT_EXISTS);
            return serviceResult;
        }
        //不是备货中的换货单不能发货
        if(!ChangeOrderStatus.CHANGE_ORDER_STATUS_STOCKING.equals(changeOrderDO.getChangeOrderStatus())){
            serviceResult.setErrorCode(ErrorCode.CHANGE_ORDER_STATUS_CAN_NOT_DELIVERY);
        }
        //todo 校验是否已经全部备货

        changeOrderDO.setChangeOrderStatus(ChangeOrderStatus.CHANGE_ORDER_STATUS_PICK_UP);
        changeOrderDO.setUpdateTime(new Date());
        changeOrderDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        changeOrderMapper.update(changeOrderDO);
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(changeOrderDO.getChangeOrderNo());
        return serviceResult;
    }

    @Override
    public ServiceResult<String, String> end(ChangeOrder changeOrder) {
        ServiceResult<String, String> serviceResult = new ServiceResult<>();

        ChangeOrderDO changeOrderDO = changeOrderMapper.findByNo(changeOrder.getChangeOrderNo());
        if(changeOrderDO==null){
            serviceResult.setErrorCode(ErrorCode.CHANGE_ORDER_NOT_EXISTS);
            return serviceResult;
        }
        //不是处理中的换货单不允许结束
        if(!ChangeOrderStatus.CHANGE_ORDER_STATUS_PROCESS.equals(changeOrderDO.getChangeOrderStatus())){
            serviceResult.setErrorCode(ErrorCode.CHANGE_ORDER_STATUS_CAN_NOT_END);
            return serviceResult;
        }
        //todo 校验是否预计目标换货全部换了，有没换的不允许结束

        changeOrderDO.setChangeOrderStatus(ChangeOrderStatus.CHANGE_ORDER_STATUS_END);
        changeOrderDO.setUpdateTime(new Date());
        changeOrderDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        changeOrderMapper.update(changeOrderDO);
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(changeOrderDO.getChangeOrderNo());
        return serviceResult;
    }

    @Override
    public ServiceResult<String, String> cancel(ChangeOrder changeOrder) {
        ServiceResult<String, String> serviceResult = new ServiceResult<>();

        ChangeOrderDO changeOrderDO = changeOrderMapper.findByNo(changeOrder.getChangeOrderNo());
        if(changeOrderDO==null){
            serviceResult.setErrorCode(ErrorCode.CHANGE_ORDER_NOT_EXISTS);
            return serviceResult;
        }
        if(!ChangeOrderStatus.CHANGE_ORDER_STATUS_WAIT_COMMIT.equals(changeOrderDO.getChangeOrderStatus())){

        }
        return null;
    }

    @Override
    public ServiceResult<String, ChangeOrder> detail(ChangeOrder changeOrder) {
        return null;
    }

    @Override
    public ServiceResult<String, Page<ChangeOrder>> page(ChangeOrder changeOrder) {
        return null;
    }

    @Override
    public ServiceResult<String, Page<ChangeOrderProductEquipment>> pageChangeOrderProductEquipment(ChangeOrder changeOrder) {
        return null;
    }

    @Override
    public ServiceResult<String, Page<ChangeOrderMaterialBulk>> pageChangeOrderMaterialBulk(ChangeOrder changeOrder) {
        return null;
    }

    @Override
    public ServiceResult<String, String> deleteChangeOrderProductEquipment(ChangeOrderProductEquipment changeOrderProductEquipment) {
        return null;
    }

    @Override
    public ServiceResult<String, String> deleteChangeOrderMaterialBulk(ChangeOrderMaterialBulk changeOrderMaterialBulk) {
        return null;
    }

    private ServiceResult<String, Object> addOrderDestItemResultDest(StockUpByChangeParam param, ChangeOrderDO changeOrderDO) {

        ServiceResult<String, Object> serviceResult = new ServiceResult<>();
        // 如果输入进来的设备skuID 为当前订单项需要的，那么就匹配
        if (StringUtil.isNotBlank(param.getEquipmentNo())) {
            ProductEquipmentDO productEquipmentDO = productEquipmentMapper.findByEquipmentNo(param.getEquipmentNo());
            if (productEquipmentDO == null) {
                serviceResult.setErrorCode(ErrorCode.EQUIPMENT_NOT_EXISTS);
                return serviceResult;
            }
            //todo 根据换货单号查找换货单所有商品项
            List<ChangeOrderProductDO> changeOrderProductDOList = changeOrderProductMapper.findByChangeOrderId(changeOrderDO.getId());
            //todo 根据换货单号查找换货单所有目前备货设备
            List<ChangeOrderProductEquipmentDO> changeOrderProductEquipmentDOList = new ArrayList<>();
            Map<Integer, Integer> changeOrderProductEquipmentMap = new HashMap<>();
            for (ChangeOrderProductEquipmentDO changeOrderProductEquipmentDO : changeOrderProductEquipmentDOList) {
                Integer skuId = changeOrderProductEquipmentDO.getDestProductEquipmentDO().getSkuId();
                if (!changeOrderProductEquipmentMap.containsKey(skuId)) {
                    changeOrderProductEquipmentMap.put(skuId, 0);
                }
                changeOrderProductEquipmentMap.put(skuId, changeOrderProductEquipmentMap.get(skuId) + 1);
            }
            //按照destSku归集后的countMap
            Map<Integer, Integer> changeOrderProductCountMap = new HashMap<>();
            //按照destSku归集后的changeOrderProductDOList Map
            Map<Integer, List<ChangeOrderProductDO>> changeOrderProductDOListMap = new HashMap<>();
            for (ChangeOrderProductDO changeOrderProductDO : changeOrderProductDOList) {
                if (!changeOrderProductCountMap.containsKey(changeOrderProductDO.getChangeProductSkuIdDest())) {
                    changeOrderProductCountMap.put(changeOrderProductDO.getChangeProductSkuIdDest(), 0);
                }
                changeOrderProductCountMap.put(changeOrderProductDO.getChangeProductSkuIdDest(), changeOrderProductDO.getChangeProductSkuCount() + changeOrderProductCountMap.get(changeOrderProductDO.getChangeProductSkuIdDest()));
                if (!changeOrderProductDOListMap.containsKey(changeOrderProductDO.getChangeProductSkuIdDest())) {
                    changeOrderProductDOListMap.put(changeOrderProductDO.getChangeProductSkuIdDest(), new ArrayList<ChangeOrderProductDO>());
                }
                changeOrderProductDOListMap.get(changeOrderProductDO.getChangeProductSkuIdDest()).add(changeOrderProductDO);
            }
            //计算是否可备货，可备货总数-已备货总数大于0可备货
            if (changeOrderProductCountMap.get(productEquipmentDO.getSkuId()) - changeOrderProductEquipmentMap.get(productEquipmentDO.getSkuId()) <= 0) {
                serviceResult.setErrorCode(ErrorCode.STOCK_NOT_MATCH);
                return serviceResult;
            }
            Date now = new Date();
            //保存换货单设备项
            ChangeOrderProductEquipmentDO changeOrderProductEquipmentDO = new ChangeOrderProductEquipmentDO();
            ChangeOrderProductDO changeOrderProductDO = changeOrderProductDOListMap.get(productEquipmentDO.getSkuId()).get(0);
            changeOrderProductEquipmentDO.setChangeOrderProductId(changeOrderProductDO.getId());
            changeOrderProductEquipmentDO.setChangeOrderId(changeOrderProductDO.getChangeOrderId());
            changeOrderProductEquipmentDO.setChangeOrderNo(changeOrderProductDO.getChangeOrderNo());
            changeOrderProductEquipmentDO.setDestEquipmentId(productEquipmentDO.getId());
            changeOrderProductEquipmentDO.setDestEquipmentNo(productEquipmentDO.getEquipmentNo());
            changeOrderProductEquipmentDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
            changeOrderProductEquipmentDO.setCreateTime(now);
            changeOrderProductEquipmentDO.setUpdateTime(now);
            changeOrderProductEquipmentDO.setCreateUser(userSupport.getCurrentUserId().toString());
            changeOrderProductEquipmentDO.setUpdateUser(userSupport.getCurrentUserId().toString());
            changeOrderProductEquipmentMapper.save(changeOrderProductEquipmentDO);
            //更新设备状态
            productEquipmentDO.setEquipmentStatus(ProductEquipmentStatus.PRODUCT_EQUIPMENT_STATUS_BUSY);
            productEquipmentMapper.update(productEquipmentDO);
            //todo 更新散料状态
        }
        // 如果输入进来的物料ID 为当前订单项需要的，那么就匹配
        if (StringUtil.isNotBlank(param.getMaterialNo())) {
            MaterialDO materialDO = materialMapper.findByNo(param.getMaterialNo());
            if (materialDO == null) {
                serviceResult.setErrorCode(ErrorCode.MATERIAL_NOT_EXISTS);
                return serviceResult;
            }
            //todo 根据换货单号查找换货单所有物料项
            List<ChangeOrderMaterialDO> changeOrderMaterialDOList = changeOrderMaterialMapper.findByChangeOrderId(changeOrderDO.getId());
            //todo 根据换货单号查找换货单所有目前备货散料
            List<ChangeOrderMaterialBulkDO> changeOrderMaterialBulkDOList = new ArrayList<>();
            Map<Integer, Integer> changeOrderProductBulkMap = new HashMap<>();
            for (ChangeOrderMaterialBulkDO changeOrderMaterialBulkDO : changeOrderMaterialBulkDOList) {
                Integer materialId = changeOrderMaterialBulkDO.getDestBulkMaterialDO().getMaterialId();
                if (!changeOrderProductBulkMap.containsKey(materialId)) {
                    changeOrderProductBulkMap.put(materialId, 0);
                }
                changeOrderProductBulkMap.put(materialId, changeOrderProductBulkMap.get(materialId) + 1);
            }
            //按照destMaterial归集后的countMap
            Map<Integer, Integer> changeOrderMaterialCountMap = new HashMap<>();
            //按照destSku归集后的changeOrderMaterialDOListMap Map
            Map<Integer, List<ChangeOrderMaterialDO>> changeOrderMaterialDOListMap = new HashMap<>();
            for (ChangeOrderMaterialDO changeOrderMaterialDO : changeOrderMaterialDOList) {
                if (!changeOrderMaterialCountMap.containsKey(changeOrderMaterialDO.getChangeMaterialIdDest())) {
                    changeOrderMaterialCountMap.put(changeOrderMaterialDO.getChangeMaterialIdDest(), 0);
                }
                changeOrderMaterialCountMap.put(changeOrderMaterialDO.getChangeMaterialIdDest(), changeOrderMaterialDO.getChangeMaterialCount() + changeOrderMaterialCountMap.get(changeOrderMaterialDO.getChangeMaterialIdDest()));
                if (!changeOrderMaterialDOListMap.containsKey(changeOrderMaterialDO.getChangeMaterialIdDest())) {
                    changeOrderMaterialDOListMap.put(changeOrderMaterialDO.getChangeMaterialIdDest(), new ArrayList<ChangeOrderMaterialDO>());
                }
                changeOrderMaterialDOListMap.get(changeOrderMaterialDO.getChangeMaterialIdDest()).add(changeOrderMaterialDO);
            }
            //计算是否可备货，可备货总数-已备货总数-本次备货数 大于0可备货
            if (changeOrderMaterialCountMap.get(materialDO.getId()) - changeOrderProductBulkMap.get(materialDO.getId()) - param.getMaterialCount() <= 0) {
                serviceResult.setErrorCode(ErrorCode.STOCK_NOT_MATCH);
                return serviceResult;
            }
            Date now = new Date();
            //要更新的散料
            List<BulkMaterialDO> bulkMaterialDOListForUpdate = new ArrayList<>();
            //保存换货单设备项
            for (int i = 0; i < param.getMaterialCount(); i++) {
                ChangeOrderMaterialBulkDO changeOrderMaterialBulkDO = new ChangeOrderMaterialBulkDO();
                ChangeOrderMaterialDO changeOrderMaterialDO = changeOrderMaterialDOListMap.get(materialDO.getId()).get(0);
                changeOrderMaterialBulkDO.setChangeOrderMaterialId(changeOrderMaterialDO.getId());
                changeOrderMaterialBulkDO.setChangeOrderId(changeOrderMaterialDO.getChangeOrderId());
                changeOrderMaterialBulkDO.setChangeOrderNo(changeOrderMaterialDO.getChangeOrderNo());
                //todo 寻找本仓库没有关联订单号且没有设备号，闲置状态的物料Id为materialId的散料
                BulkMaterialDO bulkMaterialDO = new BulkMaterialDO();
                changeOrderMaterialBulkDO.setDestBulkMaterialId(bulkMaterialDO.getId());
                changeOrderMaterialBulkDO.setDestBulkMaterialNo(bulkMaterialDO.getBulkMaterialNo());
                changeOrderMaterialBulkDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                changeOrderMaterialBulkDO.setCreateTime(now);
                changeOrderMaterialBulkDO.setUpdateTime(now);
                changeOrderMaterialBulkDO.setCreateUser(userSupport.getCurrentUserId().toString());
                changeOrderMaterialBulkDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                changeOrderMaterialBulkMapper.save(changeOrderMaterialBulkDO);

                //加入要更新的散料
                bulkMaterialDO.setBulkMaterialStatus(BulkMaterialStatus.BULK_MATERIAL_STATUS_BUSY);
                bulkMaterialDOListForUpdate.add(bulkMaterialDO);
            }
            //批量更新散料状态
            bulkMaterialMapper.updateList(bulkMaterialDOListForUpdate);
        }
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        return serviceResult;
    }

    private ServiceResult<String, Object> removeOrderDestItemResultDest(StockUpByChangeParam param, ChangeOrderDO changeOrderDO) {

        ServiceResult<String, Object> serviceResult = new ServiceResult<>();
        Date now = new Date();
        // 如果输入进来的设备skuID 为当前订单项需要的，那么就匹配
        if (StringUtil.isNotBlank(param.getEquipmentNo())) {
            ProductEquipmentDO productEquipmentDO = productEquipmentMapper.findByEquipmentNo(param.getEquipmentNo());
            if (productEquipmentDO == null) {
                serviceResult.setErrorCode(ErrorCode.EQUIPMENT_NOT_EXISTS);
                return serviceResult;
            }
            productEquipmentDO.setEquipmentStatus(ProductEquipmentStatus.PRODUCT_EQUIPMENT_STATUS_IDLE);
            productEquipmentMapper.update(productEquipmentDO);
            //todo 根据换货单号和设备号查找备货设备
            ChangeOrderProductEquipmentDO changeOrderProductEquipmentDO = new ChangeOrderProductEquipmentDO();
            if (changeOrderProductEquipmentDO == null) {
                serviceResult.setErrorCode(ErrorCode.RECORD_NOT_EXISTS);
            }
            changeOrderProductEquipmentDO.setDataStatus(CommonConstant.DATA_STATUS_DELETE);
            changeOrderProductEquipmentMapper.update(changeOrderProductEquipmentDO);
            //todo 更新散料状态
        }
        // 如果输入进来的物料ID 为当前订单项需要的，那么就匹配
        if (StringUtil.isNotBlank(param.getMaterialNo())) {
            MaterialDO materialDO = materialMapper.findByNo(param.getMaterialNo());
            if (materialDO == null) {
                serviceResult.setErrorCode(ErrorCode.MATERIAL_NOT_EXISTS);
                return serviceResult;
            }
            //todo 根据换货单号和物料ID查找目前备货散料
            List<ChangeOrderMaterialBulkDO> changeOrderMaterialBulkDOList = new ArrayList<>();
            if (CollectionUtil.isEmpty(changeOrderMaterialBulkDOList) || changeOrderMaterialBulkDOList.size() < param.getMaterialCount()) {
                serviceResult.setErrorCode(ErrorCode.PRODUCT_MATERIAL_NOT_MATCHING);
            }
            //待更新的换货散料表数据
            List<ChangeOrderMaterialBulkDO> changeOrderMaterialBulkDOListForUpdate = new ArrayList<>();
            //待更新的散料
            List<BulkMaterialDO> bulkMaterialDOListForUpdate = new ArrayList<>();
            for (int i = 0; i < param.getMaterialCount(); i++) {
                ChangeOrderMaterialBulkDO changeOrderMaterialBulkDO = changeOrderMaterialBulkDOList.get(i);
                changeOrderMaterialBulkDO.setDataStatus(CommonConstant.DATA_STATUS_DELETE);
                changeOrderMaterialBulkDO.setUpdateTime(now);
                changeOrderMaterialBulkDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                changeOrderMaterialBulkDOListForUpdate.add(changeOrderMaterialBulkDO);

                BulkMaterialDO bulkMaterialDO = changeOrderMaterialBulkDO.getDestBulkMaterialDO();
                bulkMaterialDO.setBulkMaterialStatus(BulkMaterialStatus.BULK_MATERIAL_STATUS_IDLE);
                bulkMaterialDOListForUpdate.add(bulkMaterialDO);
            }
            //批量更新散料状态
            bulkMaterialMapper.updateList(bulkMaterialDOListForUpdate);
        }
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        return serviceResult;
    }

    @Autowired
    private ChangeOrderMapper changeOrderMapper;
    @Autowired
    private ChangeOrderConsignInfoMapper changeOrderConsignInfoMapper;
    @Autowired
    private ChangeOrderProductMapper changeOrderProductMapper;
    @Autowired
    private ChangeOrderMaterialMapper changeOrderMaterialMapper;
    @Autowired
    private CustomerOrderSupport customerOrderSupport;
    @Autowired
    private WarehouseService warehouseService;
    @Autowired
    private WorkflowService workflowService;
    @Autowired
    private CustomerMapper customerMapper;
    @Autowired
    private ProductSkuMapper productSkuMapper;
    @Autowired
    private MaterialMapper materialMapper;
    @Autowired
    private ProductService productService;
    @Autowired
    private UserSupport userSupport;
    @Autowired
    private ProductEquipmentMapper productEquipmentMapper;
    @Autowired
    private ChangeOrderProductEquipmentMapper changeOrderProductEquipmentMapper;
    @Autowired
    private ChangeOrderMaterialBulkMapper changeOrderMaterialBulkMapper;
    @Autowired
    private BulkMaterialMapper bulkMaterialMapper;
}
