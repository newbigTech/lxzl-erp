package com.lxzl.erp.core.service.deploymentOrder.impl;

import com.lxzl.erp.common.constant.*;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.deploymentOrder.ProcessDeploymentOrderParam;
import com.lxzl.erp.common.domain.deploymentOrder.pojo.DeploymentOrder;
import com.lxzl.erp.common.domain.deploymentOrder.pojo.DeploymentOrderMaterial;
import com.lxzl.erp.common.domain.deploymentOrder.pojo.DeploymentOrderProduct;
import com.lxzl.erp.common.domain.material.BulkMaterialQueryParam;
import com.lxzl.erp.common.domain.material.pojo.Material;
import com.lxzl.erp.common.domain.product.ProductEquipmentQueryParam;
import com.lxzl.erp.common.domain.product.pojo.Product;
import com.lxzl.erp.common.domain.user.pojo.User;
import com.lxzl.erp.common.util.*;
import com.lxzl.erp.core.service.deploymentOrder.DeploymentOrderService;
import com.lxzl.erp.core.service.deploymentOrder.impl.support.DeploymentOrderConverter;
import com.lxzl.erp.core.service.material.MaterialService;
import com.lxzl.erp.core.service.product.ProductService;
import com.lxzl.erp.core.service.workflow.WorkflowService;
import com.lxzl.erp.dataaccess.dao.mysql.deploymentOrder.DeploymentOrderMapper;
import com.lxzl.erp.dataaccess.dao.mysql.deploymentOrder.DeploymentOrderMaterialMapper;
import com.lxzl.erp.dataaccess.dao.mysql.deploymentOrder.DeploymentOrderProductMapper;
import com.lxzl.erp.dataaccess.dao.mysql.material.BulkMaterialMapper;
import com.lxzl.erp.dataaccess.dao.mysql.product.ProductEquipmentMapper;
import com.lxzl.erp.dataaccess.domain.deploymentOrder.DeploymentOrderDO;
import com.lxzl.erp.dataaccess.domain.deploymentOrder.DeploymentOrderMaterialDO;
import com.lxzl.erp.dataaccess.domain.deploymentOrder.DeploymentOrderProductDO;
import com.lxzl.erp.dataaccess.domain.product.ProductEquipmentDO;
import com.lxzl.se.common.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.*;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2017-11-27 13:58
 */
@Service("deploymentOrderService")
public class DeploymentOrderServiceImpl implements DeploymentOrderService {

    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ServiceResult<String, String> createDeploymentOrder(DeploymentOrder deploymentOrder) {
        ServiceResult<String, String> result = new ServiceResult<>();
        User loginUser = (User) session.getAttribute(CommonConstant.ERP_USER_SESSION_KEY);
        Date currentTime = new Date();
        String verifyCode = verifyDeploymentOrderInfo(deploymentOrder);
        if (!ErrorCode.SUCCESS.equals(verifyCode)) {
            result.setErrorCode(verifyCode);
            return result;
        }

        DeploymentOrderDO deploymentOrderDO = DeploymentOrderConverter.convertDeploymentOrder(deploymentOrder);
        deploymentOrderDO.setDeploymentOrderNo(GenerateNoUtil.generateDeploymentOrderNo(currentTime));
        deploymentOrderDO.setDeploymentOrderStatus(DeploymentOrderStatus.DEPLOYMENT_ORDER_STATUS_WAIT_COMMIT);
        deploymentOrderDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        deploymentOrderDO.setUpdateUser(loginUser.getUserId().toString());
        deploymentOrderDO.setCreateUser(loginUser.getUserId().toString());
        deploymentOrderDO.setUpdateTime(currentTime);
        deploymentOrderDO.setCreateTime(currentTime);
        deploymentOrderMapper.save(deploymentOrderDO);
        saveDeploymentOrderProduct(deploymentOrderDO.getDeploymentOrderNo(), DeploymentOrderConverter.convertDeploymentOrderProductList(deploymentOrder.getDeploymentOrderProductList()), loginUser, currentTime);
        saveDeploymentOrderMaterial(deploymentOrderDO.getDeploymentOrderNo(), DeploymentOrderConverter.convertDeploymentOrderMaterialList(deploymentOrder.getDeploymentOrderMaterialList()), loginUser, currentTime);

        DeploymentOrderDO newestDeploymentOrderDO = deploymentOrderMapper.findByNo(deploymentOrderDO.getDeploymentOrderNo());
        for (DeploymentOrderProductDO deploymentOrderProductDO : newestDeploymentOrderDO.getDeploymentOrderProductDOList()) {
            deploymentOrderDO.setTotalProductCount(deploymentOrderDO.getTotalProductCount() == null ? deploymentOrderProductDO.getDeploymentProductSkuCount() : (deploymentOrderDO.getTotalProductCount() + deploymentOrderProductDO.getDeploymentProductSkuCount()));
            deploymentOrderDO.setTotalProductAmount(BigDecimalUtil.add(deploymentOrderDO.getTotalProductAmount(), deploymentOrderProductDO.getDeploymentProductAmount()));
        }
        for (DeploymentOrderMaterialDO deploymentOrderMaterialDO : newestDeploymentOrderDO.getDeploymentOrderMaterialDOList()) {
            deploymentOrderDO.setTotalMaterialCount(deploymentOrderDO.getTotalMaterialCount() == null ? deploymentOrderMaterialDO.getDeploymentProductMaterialCount() : (deploymentOrderDO.getTotalMaterialCount() + deploymentOrderMaterialDO.getDeploymentProductMaterialCount()));
            deploymentOrderDO.setTotalMaterialAmount(BigDecimalUtil.add(deploymentOrderDO.getTotalMaterialAmount(), deploymentOrderMaterialDO.getDeploymentMaterialAmount()));
        }
        deploymentOrderMapper.update(deploymentOrderDO);

        result.setResult(deploymentOrderDO.getDeploymentOrderNo());
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    private void saveDeploymentOrderProduct(String deploymentOrderNo, List<DeploymentOrderProductDO> deploymentOrderProductDOList, User loginUser, Date currentTime) {

        Map<Integer, DeploymentOrderProductDO> deploymentOrderProductDOMap = ListUtil.listToMap(deploymentOrderProductDOList, "deploymentProductSkuId");
        Map<Integer, DeploymentOrderProductDO> saveDeploymentOrderProductDOMap = new HashMap<>();
        Map<Integer, DeploymentOrderProductDO> updateDeploymentOrderProductDOMap = new HashMap<>();
        List<DeploymentOrderProductDO> dbDeploymentOrderProductDOList = deploymentOrderProductMapper.findByDeploymentOrderNo(deploymentOrderNo);
        for (DeploymentOrderProductDO deploymentOrderProductDO : dbDeploymentOrderProductDOList) {
            if (deploymentOrderProductDOMap.get(deploymentOrderProductDO.getDeploymentProductSkuId()) != null) {
                updateDeploymentOrderProductDOMap.put(deploymentOrderProductDO.getDeploymentProductSkuId(), deploymentOrderProductDO);
                deploymentOrderProductDOMap.remove(deploymentOrderProductDO.getDeploymentProductSkuId());
            } else {
                saveDeploymentOrderProductDOMap.put(deploymentOrderProductDO.getDeploymentProductSkuId(), deploymentOrderProductDO);
            }
        }

        if (saveDeploymentOrderProductDOMap.size() > 0) {
            List<DeploymentOrderProductDO> saveList = new ArrayList<>();
            for (Map.Entry<Integer, DeploymentOrderProductDO> entry : saveDeploymentOrderProductDOMap.entrySet()) {
                DeploymentOrderProductDO deploymentOrderProductDO = entry.getValue();
                ServiceResult<String, Product> productServiceResult = productService.queryProductBySkuId(deploymentOrderProductDO.getDeploymentProductSkuId());
                if (!ErrorCode.SUCCESS.equals(productServiceResult.getErrorCode())) {
                    throw new BusinessException(productServiceResult.getErrorCode());
                }
                Product product = productServiceResult.getResult();
                deploymentOrderProductDO.setDeploymentProductAmount(BigDecimalUtil.mul(deploymentOrderProductDO.getDeploymentProductAmount(), new BigDecimal(deploymentOrderProductDO.getDeploymentProductSkuCount())));
                deploymentOrderProductDO.setDeploymentProductSkuSnapshot(JSONUtil.convertBeanToJSON(product));
                deploymentOrderProductDO.setDeploymentOrderNo(deploymentOrderNo);
                deploymentOrderProductDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                deploymentOrderProductDO.setUpdateUser(loginUser.getUserId().toString());
                deploymentOrderProductDO.setCreateUser(loginUser.getUserId().toString());
                deploymentOrderProductDO.setUpdateTime(currentTime);
                deploymentOrderProductDO.setCreateTime(currentTime);
                saveList.add(deploymentOrderProductDO);
            }
            deploymentOrderProductMapper.saveList(saveList);
        }

        if (updateDeploymentOrderProductDOMap.size() > 0) {
            for (Map.Entry<Integer, DeploymentOrderProductDO> entry : updateDeploymentOrderProductDOMap.entrySet()) {
                DeploymentOrderProductDO deploymentOrderProductDO = entry.getValue();
                ServiceResult<String, Product> productServiceResult = productService.queryProductBySkuId(deploymentOrderProductDO.getDeploymentProductSkuId());
                if (!ErrorCode.SUCCESS.equals(productServiceResult.getErrorCode())) {
                    throw new BusinessException(productServiceResult.getErrorCode());
                }
                Product product = productServiceResult.getResult();
                deploymentOrderProductDO.setDeploymentProductAmount(BigDecimalUtil.mul(deploymentOrderProductDO.getDeploymentProductAmount(), new BigDecimal(deploymentOrderProductDO.getDeploymentProductSkuCount())));
                deploymentOrderProductDO.setDeploymentProductSkuSnapshot(JSONUtil.convertBeanToJSON(product));
                deploymentOrderProductDO.setUpdateUser(loginUser.getUserId().toString());
                deploymentOrderProductDO.setUpdateTime(currentTime);
                deploymentOrderProductMapper.update(deploymentOrderProductDO);
            }

        }

        if (deploymentOrderProductDOMap.size() > 0) {
            for (Map.Entry<Integer, DeploymentOrderProductDO> entry : deploymentOrderProductDOMap.entrySet()) {
                DeploymentOrderProductDO deploymentOrderProductDO = entry.getValue();
                deploymentOrderProductDO.setDataStatus(CommonConstant.DATA_STATUS_DELETE);
                deploymentOrderProductDO.setUpdateUser(loginUser.getUserId().toString());
                deploymentOrderProductDO.setUpdateTime(currentTime);
                deploymentOrderProductMapper.update(deploymentOrderProductDO);
            }
        }
    }

    private void saveDeploymentOrderMaterial(String deploymentOrderNo, List<DeploymentOrderMaterialDO> deploymentOrderMaterialDOList, User loginUser, Date currentTime) {

        Map<Integer, DeploymentOrderMaterialDO> deploymentOrderMaterialDOMap = ListUtil.listToMap(deploymentOrderMaterialDOList, "deploymentMaterialId");
        Map<Integer, DeploymentOrderMaterialDO> saveDeploymentOrderMaterialDOMap = new HashMap<>();
        Map<Integer, DeploymentOrderMaterialDO> updateDeploymentOrderMaterialDOMap = new HashMap<>();
        List<DeploymentOrderMaterialDO> dbDeploymentOrderMaterialDOList = deploymentOrderMaterialMapper.findByDeploymentOrderNo(deploymentOrderNo);
        for (DeploymentOrderMaterialDO deploymentOrderMaterialDO : dbDeploymentOrderMaterialDOList) {
            if (deploymentOrderMaterialDOMap.get(deploymentOrderMaterialDO.getDeploymentMaterialId()) != null) {
                updateDeploymentOrderMaterialDOMap.put(deploymentOrderMaterialDO.getDeploymentMaterialId(), deploymentOrderMaterialDO);
                deploymentOrderMaterialDOMap.remove(deploymentOrderMaterialDO.getDeploymentMaterialId());
            } else {
                saveDeploymentOrderMaterialDOMap.put(deploymentOrderMaterialDO.getDeploymentMaterialId(), deploymentOrderMaterialDO);
            }
        }

        if (saveDeploymentOrderMaterialDOMap.size() > 0) {
            List<DeploymentOrderMaterialDO> saveList = new ArrayList<>();
            for (Map.Entry<Integer, DeploymentOrderMaterialDO> entry : saveDeploymentOrderMaterialDOMap.entrySet()) {
                DeploymentOrderMaterialDO deploymentOrderMaterialDO = entry.getValue();
                ServiceResult<String, Material> materialServiceResult = materialService.queryMaterialById(deploymentOrderMaterialDO.getDeploymentMaterialId());
                if (!ErrorCode.SUCCESS.equals(materialServiceResult.getErrorCode())) {
                    throw new BusinessException(materialServiceResult.getErrorCode());
                }
                Material material = materialServiceResult.getResult();
                deploymentOrderMaterialDO.setDeploymentMaterialAmount(BigDecimalUtil.mul(deploymentOrderMaterialDO.getDeploymentMaterialUnitAmount(), new BigDecimal(deploymentOrderMaterialDO.getDeploymentProductMaterialCount())));
                deploymentOrderMaterialDO.setDeploymentProductMaterialSnapshot(JSONUtil.convertBeanToJSON(material));
                deploymentOrderMaterialDO.setDeploymentOrderNo(deploymentOrderNo);
                deploymentOrderMaterialDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                deploymentOrderMaterialDO.setUpdateUser(loginUser.getUserId().toString());
                deploymentOrderMaterialDO.setCreateUser(loginUser.getUserId().toString());
                deploymentOrderMaterialDO.setUpdateTime(currentTime);
                deploymentOrderMaterialDO.setCreateTime(currentTime);
                saveList.add(deploymentOrderMaterialDO);
            }
            deploymentOrderMaterialMapper.saveList(saveList);
        }

        if (updateDeploymentOrderMaterialDOMap.size() > 0) {
            for (Map.Entry<Integer, DeploymentOrderMaterialDO> entry : updateDeploymentOrderMaterialDOMap.entrySet()) {
                DeploymentOrderMaterialDO deploymentOrderMaterialDO = entry.getValue();
                ServiceResult<String, Material> materialServiceResult = materialService.queryMaterialById(deploymentOrderMaterialDO.getDeploymentMaterialId());
                if (!ErrorCode.SUCCESS.equals(materialServiceResult.getErrorCode())) {
                    throw new BusinessException(materialServiceResult.getErrorCode());
                }
                Material material = materialServiceResult.getResult();
                deploymentOrderMaterialDO.setDeploymentMaterialAmount(BigDecimalUtil.mul(deploymentOrderMaterialDO.getDeploymentMaterialUnitAmount(), new BigDecimal(deploymentOrderMaterialDO.getDeploymentProductMaterialCount())));
                deploymentOrderMaterialDO.setDeploymentProductMaterialSnapshot(JSONUtil.convertBeanToJSON(material));
                deploymentOrderMaterialDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                deploymentOrderMaterialDO.setUpdateUser(loginUser.getUserId().toString());
                deploymentOrderMaterialDO.setUpdateTime(currentTime);
                deploymentOrderMaterialMapper.update(deploymentOrderMaterialDO);
            }
        }

        if (deploymentOrderMaterialDOMap.size() > 0) {
            for (Map.Entry<Integer, DeploymentOrderMaterialDO> entry : updateDeploymentOrderMaterialDOMap.entrySet()) {
                DeploymentOrderMaterialDO deploymentOrderMaterialDO = entry.getValue();
                deploymentOrderMaterialDO.setDataStatus(CommonConstant.DATA_STATUS_DELETE);
                deploymentOrderMaterialDO.setUpdateUser(loginUser.getUserId().toString());
                deploymentOrderMaterialDO.setUpdateTime(currentTime);
                deploymentOrderMaterialMapper.update(deploymentOrderMaterialDO);
            }
        }
    }

    private String verifyDeploymentOrderInfo(DeploymentOrder deploymentOrder) {
        if (deploymentOrder == null) {
            return ErrorCode.PARAM_IS_NOT_NULL;
        }
        if (!DeploymentType.DEPLOYMENT_TYPE_BORROW.equals(deploymentOrder.getDeploymentType())
                && !DeploymentType.DEPLOYMENT_TYPE_SELL.equals(deploymentOrder.getDeploymentType())) {
            return ErrorCode.PARAM_IS_ERROR;
        }
        if (CollectionUtil.isEmpty(deploymentOrder.getDeploymentOrderProductList())) {
            return ErrorCode.PARAM_IS_ERROR;
        }

        for (DeploymentOrderProduct deploymentOrderProduct : deploymentOrder.getDeploymentOrderProductList()) {
            if (deploymentOrderProduct.getDeploymentProductSkuCount() == null) {
                return ErrorCode.PARAM_IS_ERROR;
            }
            if (BigDecimalUtil.compare(deploymentOrderProduct.getDeploymentProductUnitAmount(), BigDecimal.ZERO) <= 0) {
                return ErrorCode.PARAM_IS_ERROR;
            }

            ProductEquipmentQueryParam productEquipmentQueryParam = new ProductEquipmentQueryParam();
            productEquipmentQueryParam.setSkuId(deploymentOrderProduct.getDeploymentProductSkuId());
            productEquipmentQueryParam.setCurrentWarehouseId(deploymentOrder.getSrcWarehouseId());

            Map<String, Object> maps = new HashMap<>();
            maps.put("start", 0);
            maps.put("pageSize", Integer.MAX_VALUE);
            maps.put("productEquipmentQueryParam", productEquipmentQueryParam);
            Integer productEquipmentCount = productEquipmentMapper.listCount(maps);
            // 库房商品库存不足
            if (productEquipmentCount == null || deploymentOrderProduct.getDeploymentProductSkuCount() > productEquipmentCount) {
                return ErrorCode.DEPLOYMENT_ORDER_PRODUCT_EQUIPMENT_STOCK_NOT_ENOUGH;
            }
        }

        for (DeploymentOrderMaterial deploymentOrderMaterial : deploymentOrder.getDeploymentOrderMaterialList()) {
            if (deploymentOrderMaterial.getDeploymentProductMaterialCount() == null) {
                return ErrorCode.PARAM_IS_ERROR;
            }
            if (BigDecimalUtil.compare(deploymentOrderMaterial.getDeploymentMaterialUnitAmount(), BigDecimal.ZERO) <= 0) {
                return ErrorCode.PARAM_IS_ERROR;
            }
            BulkMaterialQueryParam bulkMaterialQueryParam = new BulkMaterialQueryParam();
            bulkMaterialQueryParam.setMaterialId(deploymentOrderMaterial.getDeploymentMaterialId());
            bulkMaterialQueryParam.setCurrentWarehouseId(deploymentOrder.getSrcWarehouseId());

            Map<String, Object> maps = new HashMap<>();
            maps.put("start", 0);
            maps.put("pageSize", Integer.MAX_VALUE);
            maps.put("bulkMaterialQueryParam", bulkMaterialQueryParam);
            Integer bulkMaterialCount = bulkMaterialMapper.listCount(maps);
            // 物料库存不足
            if (bulkMaterialCount == null || deploymentOrderMaterial.getDeploymentProductMaterialCount() > bulkMaterialCount) {
                return ErrorCode.DEPLOYMENT_ORDER_BULK_MATERIAL_STOCK_NOT_ENOUGH;
            }
        }
        return ErrorCode.SUCCESS;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ServiceResult<String, String> updateDeploymentOrder(DeploymentOrder deploymentOrder) {
        ServiceResult<String, String> result = new ServiceResult<>();

        User loginUser = (User) session.getAttribute(CommonConstant.ERP_USER_SESSION_KEY);
        Date currentTime = new Date();

        DeploymentOrderDO dbDeploymentOrderDO = deploymentOrderMapper.findByNo(deploymentOrder.getDeploymentOrderNo());
        if (dbDeploymentOrderDO == null) {
            result.setErrorCode(ErrorCode.RECORD_NOT_EXISTS);
            return result;
        }
        String verifyCode = verifyDeploymentOrderInfo(deploymentOrder);
        if (!ErrorCode.SUCCESS.equals(verifyCode)) {
            result.setErrorCode(verifyCode);
            return result;
        }
        if (!DeploymentOrderStatus.DEPLOYMENT_ORDER_STATUS_WAIT_COMMIT.equals(dbDeploymentOrderDO.getDeploymentOrderStatus())) {
            result.setErrorCode(ErrorCode.DEPLOYMENT_ORDER_STATUS_ERROR);
            return result;
        }


        DeploymentOrderDO deploymentOrderDO = DeploymentOrderConverter.convertDeploymentOrder(deploymentOrder);
        deploymentOrderDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        deploymentOrderDO.setUpdateUser(loginUser.getUserId().toString());
        deploymentOrderDO.setCreateUser(loginUser.getUserId().toString());
        deploymentOrderDO.setUpdateTime(currentTime);
        deploymentOrderDO.setCreateTime(currentTime);
        deploymentOrderMapper.update(deploymentOrderDO);
        saveDeploymentOrderProduct(deploymentOrderDO.getDeploymentOrderNo(), DeploymentOrderConverter.convertDeploymentOrderProductList(deploymentOrder.getDeploymentOrderProductList()), loginUser, currentTime);
        saveDeploymentOrderMaterial(deploymentOrderDO.getDeploymentOrderNo(), DeploymentOrderConverter.convertDeploymentOrderMaterialList(deploymentOrder.getDeploymentOrderMaterialList()), loginUser, currentTime);

        DeploymentOrderDO newestDeploymentOrderDO = deploymentOrderMapper.findByNo(deploymentOrderDO.getDeploymentOrderNo());
        for (DeploymentOrderProductDO deploymentOrderProductDO : newestDeploymentOrderDO.getDeploymentOrderProductDOList()) {
            deploymentOrderDO.setTotalProductCount(deploymentOrderDO.getTotalProductCount() == null ? deploymentOrderProductDO.getDeploymentProductSkuCount() : (deploymentOrderDO.getTotalProductCount() + deploymentOrderProductDO.getDeploymentProductSkuCount()));
            deploymentOrderDO.setTotalProductAmount(BigDecimalUtil.add(deploymentOrderDO.getTotalProductAmount(), deploymentOrderProductDO.getDeploymentProductAmount()));
        }
        for (DeploymentOrderMaterialDO deploymentOrderMaterialDO : newestDeploymentOrderDO.getDeploymentOrderMaterialDOList()) {
            deploymentOrderDO.setTotalMaterialCount(deploymentOrderDO.getTotalMaterialCount() == null ? deploymentOrderMaterialDO.getDeploymentProductMaterialCount() : (deploymentOrderDO.getTotalMaterialCount() + deploymentOrderMaterialDO.getDeploymentProductMaterialCount()));
            deploymentOrderDO.setTotalMaterialAmount(BigDecimalUtil.add(deploymentOrderDO.getTotalMaterialAmount(), deploymentOrderMaterialDO.getDeploymentMaterialAmount()));
        }
        deploymentOrderMapper.update(deploymentOrderDO);

        result.setResult(deploymentOrderDO.getDeploymentOrderNo());
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    @Override
    public ServiceResult<String, String> commitDeploymentOrder(String deploymentOrderNo, Integer verifyUser) {

        ServiceResult<String, String> result = new ServiceResult<>();
        User loginUser = (User) session.getAttribute(CommonConstant.ERP_USER_SESSION_KEY);
        Date currentTime = new Date();
        DeploymentOrderDO dbDeploymentOrderDO = deploymentOrderMapper.findByNo(deploymentOrderNo);
        if (dbDeploymentOrderDO == null) {
            result.setErrorCode(ErrorCode.RECORD_NOT_EXISTS);
            return result;
        }
        if (!DeploymentOrderStatus.DEPLOYMENT_ORDER_STATUS_WAIT_COMMIT.equals(dbDeploymentOrderDO.getDeploymentOrderStatus())) {
            result.setErrorCode(ErrorCode.DEPLOYMENT_ORDER_STATUS_ERROR);
            return result;
        }

        ServiceResult<String, String> workFlowResult = workflowService.commitWorkFlow(WorkflowType.WORKFLOW_TYPE_DEPLOYMENT_ORDER_INFO, deploymentOrderNo, verifyUser);
        if (!ErrorCode.SUCCESS.equals(workFlowResult.getErrorCode())) {
            result.setErrorCode(workFlowResult.getErrorCode());
            return result;
        }

        dbDeploymentOrderDO.setDeploymentOrderStatus(DeploymentOrderStatus.DEPLOYMENT_ORDER_STATUS_VERIFYING);
        dbDeploymentOrderDO.setUpdateTime(currentTime);
        dbDeploymentOrderDO.setUpdateUser(loginUser.getUserId().toString());
        deploymentOrderMapper.update(dbDeploymentOrderDO);

        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(dbDeploymentOrderDO.getDeploymentOrderNo());
        return result;
    }

    @Override
    public ServiceResult<String, String> processDeploymentOrder(ProcessDeploymentOrderParam param) {
        ServiceResult<String, String> result = new ServiceResult<>();
        if (param == null) {
            result.setErrorCode(ErrorCode.PARAM_IS_NOT_NULL);
            return result;
        }
        DeploymentOrderDO deploymentOrderDO = deploymentOrderMapper.findByNo(param.getDeploymentOrderNo());
        if (deploymentOrderDO == null) {
            result.setErrorCode(ErrorCode.RECORD_NOT_EXISTS);
            return result;
        }

        // 处理调拨设备业务
        if (param.getEquipmentNo() != null) {
            ProductEquipmentDO productEquipmentDO = productEquipmentMapper.findByEquipmentNo(param.getEquipmentNo());
            if (productEquipmentDO == null) {
                result.setErrorCode(ErrorCode.PRODUCT_EQUIPMENT_NOT_EXISTS);
                return result;
            }
            if (!ProductEquipmentStatus.PRODUCT_EQUIPMENT_STATUS_IDLE.equals(productEquipmentDO.getEquipmentStatus())) {
                result.setErrorCode(ErrorCode.PRODUCT_EQUIPMENT_IS_NOT_IDLE);
                return result;
            }
            Map<Integer, DeploymentOrderProductDO> deploymentOrderProductDOMap = ListUtil.listToMap(deploymentOrderDO.getDeploymentOrderProductDOList(), "deploymentProductSkuId");
            if (deploymentOrderProductDOMap.get(productEquipmentDO.getSkuId()) == null) {
                result.setErrorCode(ErrorCode.DEPLOYMENT_ORDER_HAVE_NO_THIS_ITEM);
                return result;
            }
            // TODO
        }

        // 处理调拨物料业务
        if (param.getBulkMaterialNo() != null) {

        }


        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    @Override
    public ServiceResult<String, String> confirmDeploymentOrder(String deploymentOrderNo) {
        ServiceResult<String, String> result = new ServiceResult<>();
        User loginUser = (User) session.getAttribute(CommonConstant.ERP_USER_SESSION_KEY);
        Date currentTime = new Date();
        DeploymentOrderDO dbDeploymentOrderDO = deploymentOrderMapper.findByNo(deploymentOrderNo);
        if (dbDeploymentOrderDO == null) {
            result.setErrorCode(ErrorCode.RECORD_NOT_EXISTS);
            return result;
        }
        if (!DeploymentOrderStatus.DEPLOYMENT_ORDER_STATUS_PROCESSED.equals(dbDeploymentOrderDO.getDeploymentOrderStatus())) {
            result.setErrorCode(ErrorCode.DEPLOYMENT_ORDER_STATUS_ERROR);
            return result;
        }

        dbDeploymentOrderDO.setDeploymentOrderStatus(DeploymentOrderStatus.DEPLOYMENT_ORDER_STATUS_CONFIRM);
        dbDeploymentOrderDO.setUpdateTime(currentTime);
        dbDeploymentOrderDO.setUpdateUser(loginUser.getUserId().toString());
        deploymentOrderMapper.update(dbDeploymentOrderDO);

        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(dbDeploymentOrderDO.getDeploymentOrderNo());
        return result;
    }

    @Override
    public boolean receiveVerifyResult(boolean verifyResult, String businessNo) {
        User loginUser = (User) session.getAttribute(CommonConstant.ERP_USER_SESSION_KEY);
        Date currentTime = new Date();
        DeploymentOrderDO dbDeploymentOrderDO = deploymentOrderMapper.findByNo(businessNo);
        if (dbDeploymentOrderDO == null) {
            return false;
        }
        dbDeploymentOrderDO.setDeploymentOrderStatus(DeploymentOrderStatus.DEPLOYMENT_ORDER_STATUS_PROCESSING);
        dbDeploymentOrderDO.setUpdateTime(currentTime);
        dbDeploymentOrderDO.setUpdateUser(loginUser.getUserId().toString());
        deploymentOrderMapper.update(dbDeploymentOrderDO);
        return true;
    }

    @Autowired
    private DeploymentOrderMapper deploymentOrderMapper;

    @Autowired
    private DeploymentOrderProductMapper deploymentOrderProductMapper;

    @Autowired
    private DeploymentOrderMaterialMapper deploymentOrderMaterialMapper;

    @Autowired(required = false)
    private HttpSession session;

    @Autowired
    private BulkMaterialMapper bulkMaterialMapper;

    @Autowired
    private ProductEquipmentMapper productEquipmentMapper;

    @Autowired
    private ProductService productService;

    @Autowired
    private MaterialService materialService;

    @Autowired
    private WorkflowService workflowService;
}
