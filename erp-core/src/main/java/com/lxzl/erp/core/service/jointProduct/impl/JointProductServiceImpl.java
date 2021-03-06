package com.lxzl.erp.core.service.jointProduct.impl;

import com.lxzl.erp.common.constant.CommonConstant;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.jointProduct.JointProductQueryParam;
import com.lxzl.erp.common.domain.jointProduct.pojo.JointMaterial;
import com.lxzl.erp.common.domain.jointProduct.pojo.JointProduct;
import com.lxzl.erp.common.domain.jointProduct.pojo.JointProductProduct;
import com.lxzl.erp.common.domain.material.pojo.Material;
import com.lxzl.erp.common.domain.product.pojo.Product;
import com.lxzl.erp.common.util.CollectionUtil;
import com.lxzl.erp.common.util.ConverterUtil;
import com.lxzl.erp.common.util.ListUtil;
import com.lxzl.erp.core.service.jointProduct.JointProductService;
import com.lxzl.erp.core.service.product.ProductService;
import com.lxzl.erp.core.service.user.impl.support.UserSupport;
import com.lxzl.erp.dataaccess.dao.mysql.jointProduct.JointMaterialMapper;
import com.lxzl.erp.dataaccess.dao.mysql.jointProduct.JointProductMapper;
import com.lxzl.erp.dataaccess.dao.mysql.jointProduct.JointProductProductMapper;
import com.lxzl.erp.dataaccess.dao.mysql.material.MaterialMapper;
import com.lxzl.erp.dataaccess.dao.mysql.product.ProductMapper;
import com.lxzl.erp.dataaccess.domain.jointProduct.JointMaterialDO;
import com.lxzl.erp.dataaccess.domain.jointProduct.JointProductDO;
import com.lxzl.erp.dataaccess.domain.jointProduct.JointProductProductDO;
import com.lxzl.erp.dataaccess.domain.material.MaterialDO;
import com.lxzl.erp.dataaccess.domain.product.ProductDO;
import com.lxzl.se.dataaccess.mongo.config.PageQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.*;

@Service
public class JointProductServiceImpl implements JointProductService {

    /**
     * 添加组合商品
     */
    @Override
    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ServiceResult<String, Integer> addJointProduct(JointProduct jointProduct) {
        ServiceResult<String, Integer> serviceResult = new ServiceResult<>();
        //判断JointProductProductList 和 JointMaterialList 是否有值
        if (CollectionUtil.isEmpty(jointProduct.getJointProductProductList()) && CollectionUtil.isEmpty(jointProduct.getJointMaterialList())) {
            serviceResult.setErrorCode(ErrorCode.RECORD_NOT_EXISTS);
            return serviceResult;
        }

        //添加数据到 erp_joint_product表
        Date now = new Date();
        JointProductDO jointProductDO = new JointProductDO();
        jointProductDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        jointProductDO.setJointProductName(jointProduct.getJointProductName());
        jointProductDO.setRemark(jointProduct.getRemark());
        jointProductDO.setCreateTime(now);
        jointProductDO.setCreateUser(userSupport.getCurrentUserId().toString());
        jointProductDO.setUpdateTime(now);
        jointProductDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        //最后添加 erp_joint_product
        jointProductMapper.save(jointProductDO);

        //添加数据到 erp_joint_product_product表
        List<JointProductProduct> jointProductProductList = jointProduct.getJointProductProductList();
        if (CollectionUtil.isNotEmpty(jointProductProductList)) {
            //判断是否有重复id
            if (validDuplicateProductId(jointProductProductList)) {
                serviceResult.setErrorCode(ErrorCode.PRODUCT_CAN_NOT_REPEAT);
                return serviceResult;
            }

            for (JointProductProduct jointProductProduct : jointProductProductList) {
                ProductDO productDO = productMapper.findById(jointProductProduct.getProductId());
                if (productDO == null) {
                    //事物回滚
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                    serviceResult.setErrorCode(ErrorCode.PRODUCT_IS_NULL_OR_NOT_EXISTS);
                    return serviceResult;
                }
                JointProductProductDO jointProductProductDO = new JointProductProductDO();
                jointProductProductDO.setJointProductId(jointProductDO.getId());
                jointProductProductDO.setProductId(jointProductProduct.getProductId());
                jointProductProductDO.setProductCount(jointProductProduct.getProductCount());
                jointProductProductDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                jointProductProductDO.setUpdateTime(now);
                jointProductProductDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                jointProductProductDO.setCreateTime(now);
                jointProductProductDO.setCreateUser(userSupport.getCurrentUserId().toString());
                jointProductProductMapper.save(jointProductProductDO);
            }
        }
        //添加数据到 erp_joint_material 表
        List<JointMaterial> jointMaterialList = jointProduct.getJointMaterialList();
        if (CollectionUtil.isNotEmpty(jointMaterialList)) {
            //判断是否有重复配件编号
            if (validDuplicateMaterialNo(jointMaterialList)) {
                serviceResult.setErrorCode(ErrorCode.MATERIAL_CAN_NOT_REPEAT);
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                return serviceResult;
            }

            for (JointMaterial jointMaterial : jointMaterialList) {
                MaterialDO materialDO = materialMapper.findByNo(jointMaterial.getMaterialNo());
                if (materialDO == null) {
                    //事物回滚
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                    serviceResult.setErrorCode(ErrorCode.MATERIAL_NOT_EXISTS);
                    return serviceResult;
                }
                JointMaterialDO jointMaterialDO = new JointMaterialDO();
                jointMaterialDO.setJointProductId(jointProductDO.getId());
                jointMaterialDO.setMaterialId(materialDO.getId());
                jointMaterialDO.setMaterialNo(jointMaterial.getMaterialNo());
                jointMaterialDO.setMaterialCount(jointMaterial.getMaterialCount());
                jointMaterialDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                jointMaterialDO.setUpdateTime(now);
                jointMaterialDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                jointMaterialDO.setCreateTime(now);
                jointMaterialDO.setCreateUser(userSupport.getCurrentUserId().toString());
                jointMaterialMapper.save(jointMaterialDO);
            }
        }
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(jointProductDO.getId());
        return serviceResult;
    }

    /**
     * 更新组合商品
     */
    @Override
    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ServiceResult<String, Integer> updateJointProduct(JointProduct jointProduct) {
        ServiceResult<String, Integer> serviceResult = new ServiceResult();
        JointProductDO jointProductDO = jointProductMapper.findDetailByJointProductId(jointProduct.getJointProductId());
        if (jointProductDO == null) {
            serviceResult.setErrorCode(ErrorCode.JOINT_PRODUCT_NOT_EXISTS);
            return serviceResult;
        }

        if (CollectionUtil.isEmpty(jointProduct.getJointProductProductList()) && CollectionUtil.isEmpty(jointProduct.getJointMaterialList())) {
            serviceResult.setErrorCode(ErrorCode.RECORD_NOT_EXISTS);
            return serviceResult;
        }
        Date now = new Date();
        // 这里是跟新 erp_joint_product_product 表
        //这是传过来的值
        List<JointProductProduct> jointProductProductList = jointProduct.getJointProductProductList();
        //数据库查到的
        List<JointProductProductDO> jointProductProductDOList = jointProductDO.getJointProductProductDOList();
        //待删除列表
        Map<Integer, JointProductProductDO> productDeleteMap = ListUtil.listToMap(jointProductProductDOList, "id");

        if (CollectionUtil.isNotEmpty(jointProductProductList)) {
            //判断是否有重复id
            if (validDuplicateProductId(jointProductProductList)) {
                serviceResult.setErrorCode(ErrorCode.PRODUCT_CAN_NOT_REPEAT);
                return serviceResult;
            }

            for (JointProductProduct jointProductProduct : jointProductProductList) {
                if (jointProductProduct.getJointProductProductId() == null) {
                    //新增
                    JointProductProductDO jointProductProductDO = new JointProductProductDO();
                    jointProductProductDO.setJointProductId(jointProductDO.getId());
                    jointProductProductDO.setProductId(jointProductProduct.getProductId());
                    jointProductProductDO.setProductCount(jointProductProduct.getProductCount());
                    jointProductProductDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                    jointProductProductDO.setUpdateTime(now);
                    jointProductProductDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                    jointProductProductDO.setCreateTime(now);
                    jointProductProductDO.setCreateUser(userSupport.getCurrentUserId().toString());
                    jointProductProductMapper.save(jointProductProductDO);
                } else {
                    //更新
                    if (productDeleteMap.get(jointProductProduct.getJointProductProductId()) == null) {
                        serviceResult.setErrorCode(ErrorCode.RECORD_NOT_EXISTS);
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                        return serviceResult;
                    }
                    JointProductProductDO jointProductProductDO = productDeleteMap.get(jointProductProduct.getJointProductProductId());
                    jointProductProductDO.setId(jointProductProduct.getJointProductProductId());
                    jointProductProductDO.setJointProductId(jointProductDO.getId());
                    jointProductProductDO.setProductId(jointProductProduct.getProductId());
                    jointProductProductDO.setProductCount(jointProductProduct.getProductCount());
                    jointProductProductDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                    jointProductProductDO.setUpdateTime(now);
                    jointProductProductDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                    jointProductProductMapper.update(jointProductProductDO);
                    productDeleteMap.remove(jointProductProduct.getJointProductProductId());
                }
            }
        }

        //这是传过来的值
        List<JointMaterial> jointMaterialList = jointProduct.getJointMaterialList();
        //数据库值
        List<JointMaterialDO> jointMaterialDoList = jointProductDO.getJointMaterialDOList();
        //待删除的数据
        Map<Integer, JointMaterialDO> materialDeleteMap = ListUtil.listToMap(jointMaterialDoList, "id");
        if (CollectionUtil.isNotEmpty(jointMaterialList)) {
            //判断是否有重复配件编号
            if (validDuplicateMaterialNo(jointMaterialList)) {
                serviceResult.setErrorCode(ErrorCode.MATERIAL_CAN_NOT_REPEAT);
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                return serviceResult;
            }

            //以下是处理 erp_joint_material 的数据
            for (JointMaterial jointMaterial : jointMaterialList) {
                if (jointMaterial.getJointMaterialId() == null) {
                    //新增
                    MaterialDO materialDO = materialMapper.findByNo(jointMaterial.getMaterialNo());
                    JointMaterialDO jointMaterialDO = new JointMaterialDO();
                    jointMaterialDO.setJointProductId(jointProductDO.getId());
                    jointMaterialDO.setMaterialId(materialDO.getId());
                    jointMaterialDO.setMaterialNo(jointMaterial.getMaterialNo());
                    jointMaterialDO.setMaterialCount(jointMaterial.getMaterialCount());
                    jointMaterialDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                    jointMaterialDO.setCreateTime(now);
                    jointMaterialDO.setCreateUser(userSupport.getCurrentUserId().toString());
                    jointMaterialDO.setUpdateTime(now);
                    jointMaterialDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                    jointMaterialMapper.save(jointMaterialDO);
                } else {
                    //更新
                    if (materialDeleteMap.get(jointMaterial.getJointMaterialId()) == null) {
                        serviceResult.setErrorCode(ErrorCode.MATERIAL_NOT_EXISTS);
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                        return serviceResult;
                    }
                    JointMaterialDO jointMaterialDO = materialDeleteMap.get(jointMaterial.getJointMaterialId());
                    jointMaterialDO.setId(jointMaterial.getJointMaterialId());
                    jointMaterialDO.setMaterialNo(jointMaterial.getMaterialNo());
                    jointMaterialDO.setMaterialCount(jointMaterial.getMaterialCount());
                    jointMaterialDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                    jointMaterialDO.setUpdateTime(now);
                    jointMaterialDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                    jointMaterialMapper.update(jointMaterialDO);
                    materialDeleteMap.remove(jointMaterial.getJointMaterialId());
                }
            }
        }
        if (productDeleteMap.size() > 0) {
            //删除数据库剩下的
            for (Integer jointProductProductId : productDeleteMap.keySet()) {
                JointProductProductDO jointProductProductDODO = productDeleteMap.get(jointProductProductId);
                jointProductProductDODO.setDataStatus(CommonConstant.DATA_STATUS_DELETE);
                jointProductProductDODO.setUpdateTime(new Date());
                jointProductProductDODO.setUpdateUser(userSupport.getCurrentUserId().toString());
                jointProductProductMapper.update(jointProductProductDODO);
            }
        }
        if (materialDeleteMap.size() > 0) {
            for (Integer jointMaterialId : materialDeleteMap.keySet()) {
                JointMaterialDO jointMaterialDO = materialDeleteMap.get(jointMaterialId);
                jointMaterialDO.setDataStatus(CommonConstant.DATA_STATUS_DELETE);
                jointMaterialDO.setUpdateTime(new Date());
                jointMaterialDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                jointMaterialMapper.update(jointMaterialDO);
            }
        }


        jointProductDO.setJointProductName(jointProduct.getJointProductName());
        jointProductDO.setRemark(jointProduct.getRemark());
        jointProductDO.setUpdateTime(now);
        jointProductDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        jointProductMapper.update(jointProductDO);

        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(jointProductDO.getId());
        return serviceResult;
    }

    // 重复返回true
    private boolean validDuplicateMaterialNo(List<JointMaterial> jointMaterialList) {
        if (CollectionUtil.isNotEmpty(jointMaterialList)) {
            HashSet<String> materialNoSet = new HashSet<>();
            for (JointMaterial jointMaterial : jointMaterialList) {
                materialNoSet.add(jointMaterial.getMaterialNo());
            }

            if (jointMaterialList.size() > materialNoSet.size()) {
                return true;
            }
        }

        return false;
    }

    // 有重复返回true
    private boolean validDuplicateProductId(List<JointProductProduct> jointProductProductList) {
        if (CollectionUtil.isNotEmpty(jointProductProductList)) {
            Set<Integer> productIdSet = new HashSet<>();
            for (JointProductProduct jointProductProduct : jointProductProductList) {
                productIdSet.add(jointProductProduct.getProductId());
            }

            if (jointProductProductList.size() >productIdSet.size()) {
                return true;
            }
        }

        return false;
    }

    /**
     * 删除组合商品
     */
    @Override
    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ServiceResult<String, Integer> deleteJointProduct(JointProduct jointProduct) {
        ServiceResult<String, Integer> serviceResult = new ServiceResult<>();
        //首先查看数据库中是否有此id
        JointProductDO jointProductDO = jointProductMapper.findById(jointProduct.getJointProductId());
        if (jointProductDO == null) {
            serviceResult.setErrorCode(ErrorCode.JOINT_PRODUCT_NOT_EXISTS);
            return serviceResult;
        }

        Date now = new Date();
        jointProductDO.setDataStatus(CommonConstant.DATA_STATUS_DELETE);
        jointProductDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        jointProductDO.setUpdateTime(now);
        jointProductMapper.update(jointProductDO);

        JointProductProductDO jointProductProductDO = new JointProductProductDO();
        jointProductProductDO.setJointProductId(jointProductDO.getId());
        jointProductProductDO.setUpdateUser(jointProductDO.getUpdateUser());
        jointProductProductDO.setUpdateTime(jointProductDO.getUpdateTime());
        jointProductProductDO.setDataStatus(CommonConstant.DATA_STATUS_DELETE);
        jointProductProductMapper.deleteByJointProductId(jointProductProductDO);

        JointMaterialDO jointMaterialDO = new JointMaterialDO();
        jointMaterialDO.setJointProductId(jointProductDO.getId());
        jointMaterialDO.setUpdateUser(jointProductDO.getUpdateUser());
        jointMaterialDO.setUpdateTime(jointProductDO.getUpdateTime());
        jointMaterialDO.setDataStatus(CommonConstant.DATA_STATUS_DELETE);
        jointMaterialMapper.deleteByJointProductId(jointMaterialDO);

        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(jointProductDO.getId());
        return serviceResult;
    }

    /**
     * 查询组合商品详情
     */
    @Override
    public ServiceResult<String, JointProduct> queryJointProductByJointProductId(Integer jointProductId) {
        ServiceResult<String, JointProduct> serviceResult = new ServiceResult<>();
        JointProductDO jointProductDO = jointProductMapper.findDetailByJointProductId(jointProductId);
        if (jointProductDO == null) {
            serviceResult.setErrorCode(ErrorCode.JOINT_PRODUCT_NOT_EXISTS);
            return serviceResult;
        }
        List<JointMaterialDO> jointMaterialDOList = jointProductDO.getJointMaterialDOList();
        if (CollectionUtil.isNotEmpty(jointMaterialDOList)) {
            for (JointMaterialDO jointMaterialDO : jointMaterialDOList) {
                MaterialDO materialDO = materialMapper.findById(jointMaterialDO.getMaterialId());
                jointMaterialDO.setMaterialDO(materialDO);
            }
        }
        JointProduct jointProduct = ConverterUtil.convert(jointProductDO, JointProduct.class);
        List<JointProductProduct> jointProductProductList = jointProduct.getJointProductProductList();
        if (CollectionUtil.isNotEmpty(jointProductProductList)) {
            for (JointProductProduct jointProductProduct : jointProductProductList) {
                ServiceResult<String, Product> productResult = productService.queryProductById(jointProductProduct.getProductId());
                if (!ErrorCode.SUCCESS.equals(productResult.getErrorCode())) {
                    serviceResult.setErrorCode(productResult.getErrorCode());
                    return serviceResult;
                }
                jointProductProduct.setProduct(productResult.getResult());
            }
        }
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(jointProduct);
        return serviceResult;
    }

    /**
     * 分页查询组合商品
     */
    @Override
    public ServiceResult<String, Page<JointProduct>> pageJointProduct(JointProductQueryParam jointProductQueryParam) {

        ServiceResult<String, Page<JointProduct>> serviceResult = new ServiceResult<>();
        PageQuery pageQuery = new PageQuery(jointProductQueryParam.getPageNo(), jointProductQueryParam.getPageSize());
        HashMap<String, Object> maps = new HashMap<>();
        maps.put("start", pageQuery.getStart());
        maps.put("pageSize", pageQuery.getPageSize());
        maps.put("jointProductQueryParam", jointProductQueryParam);
        Integer jointProductCount = jointProductMapper.findJointProductCountByParam(maps);
        List<JointProductDO> jointProductDOList = jointProductMapper.findJointProductByParams(maps);
        List<JointProduct> jointProductList = ConverterUtil.convertList(jointProductDOList, JointProduct.class);
        List<JointMaterial> jointMaterialList = new ArrayList<>();
        List<JointProductProduct> jointProductProductList = new ArrayList<>();
        Set<Integer> materiaIdList = new HashSet<>();
        Set<Integer> productIdList = new HashSet<>();
        for (JointProduct jointProduct : jointProductList) {
            jointMaterialList.addAll(jointProduct.getJointMaterialList());
            for (JointMaterial jointMaterial : jointProduct.getJointMaterialList()) {
                materiaIdList.add(jointMaterial.getMaterialId());
            }

            jointProductProductList.addAll(jointProduct.getJointProductProductList());
            for (JointProductProduct jointProductProduct : jointProduct.getJointProductProductList()) {
                productIdList.add(jointProductProduct.getProductId());
            }
        }

        // 根据materiaIdList查询
        if (CollectionUtil.isNotEmpty(materiaIdList)) {
            List<MaterialDO> materialDOList = materialMapper.findByIds(materiaIdList);
            Map<Integer, MaterialDO> materialDOMap = ListUtil.listToMap(materialDOList, "id");
            for(JointMaterial jointMaterial : jointMaterialList) {
                MaterialDO materialDO = materialDOMap.get(jointMaterial.getMaterialId());
                if (materialDO != null) {
                    jointMaterial.setMaterial(ConverterUtil.convert(materialDO, Material.class));
                }
            }
        }

        // 根据productIdList查询
        if (CollectionUtil.isNotEmpty(productIdList)) {
            List<ProductDO> productDOList = productMapper.findByIds(productIdList);
            Map<Integer, ProductDO> productDOMap = ListUtil.listToMap(productDOList, "id");
            for(JointProductProduct jointProductProduct : jointProductProductList) {
                ProductDO productDO = productDOMap.get(jointProductProduct.getProductId());
                jointProductProduct.setProduct(ConverterUtil.convert(productDO, Product.class));
            }
        }

        Page<JointProduct> page = new Page<>(jointProductList, jointProductCount, jointProductQueryParam.getPageNo(), jointProductQueryParam.getPageSize());
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(page);
        return serviceResult;
    }

    @Autowired
    private UserSupport userSupport;

    @Autowired
    private JointProductMapper jointProductMapper;

    @Autowired
    private JointMaterialMapper jointMaterialMapper;

    @Autowired
    private MaterialMapper materialMapper;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private JointProductProductMapper jointProductProductMapper;

    @Autowired
    private ProductService productService;
}
