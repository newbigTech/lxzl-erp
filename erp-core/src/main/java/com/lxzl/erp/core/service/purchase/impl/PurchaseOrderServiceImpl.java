package com.lxzl.erp.core.service.purchase.impl;

import com.alibaba.fastjson.JSON;
import com.lxzl.erp.common.constant.*;
import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.company.pojo.SubCompany;
import com.lxzl.erp.common.domain.material.pojo.Material;
import com.lxzl.erp.common.domain.product.pojo.Product;
import com.lxzl.erp.common.domain.product.pojo.ProductInStorage;
import com.lxzl.erp.common.domain.product.pojo.ProductMaterial;
import com.lxzl.erp.common.domain.product.pojo.ProductSku;
import com.lxzl.erp.common.domain.purchase.PurchaseDeliveryOrderQueryParam;
import com.lxzl.erp.common.domain.purchase.PurchaseOrderCommitParam;
import com.lxzl.erp.common.domain.purchase.PurchaseOrderQueryParam;
import com.lxzl.erp.common.domain.purchase.PurchaseReceiveOrderQueryParam;
import com.lxzl.erp.common.domain.purchase.pojo.*;
import com.lxzl.erp.common.domain.warehouse.ProductInStockParam;
import com.lxzl.erp.common.domain.warehouse.pojo.Warehouse;
import com.lxzl.erp.common.util.BigDecimalUtil;
import com.lxzl.erp.common.util.CollectionUtil;
import com.lxzl.erp.common.util.GenerateNoUtil;
import com.lxzl.erp.core.service.company.CompanyService;
import com.lxzl.erp.core.service.material.MaterialService;
import com.lxzl.erp.core.service.material.impl.support.MaterialConverter;
import com.lxzl.erp.core.service.product.ProductService;
import com.lxzl.erp.core.service.purchase.PurchaseOrderService;
import com.lxzl.erp.core.service.purchase.impl.support.PurchaseOrderConverter;
import com.lxzl.erp.core.service.purchase.impl.support.PurchaseOrderSupport;
import com.lxzl.erp.core.service.user.impl.support.UserSupport;
import com.lxzl.erp.core.service.warehouse.WarehouseService;
import com.lxzl.erp.core.service.warehouse.impl.support.WarehouseConverter;
import com.lxzl.erp.core.service.workflow.WorkflowService;
import com.lxzl.erp.dataaccess.dao.mysql.material.MaterialMapper;
import com.lxzl.erp.dataaccess.dao.mysql.product.ProductSkuMapper;
import com.lxzl.erp.dataaccess.dao.mysql.purchase.*;
import com.lxzl.erp.dataaccess.dao.mysql.supplier.SupplierMapper;
import com.lxzl.erp.dataaccess.dao.mysql.warehouse.WarehouseMapper;
import com.lxzl.erp.dataaccess.domain.material.MaterialDO;
import com.lxzl.erp.dataaccess.domain.product.ProductSkuDO;
import com.lxzl.erp.dataaccess.domain.purchase.*;
import com.lxzl.erp.dataaccess.domain.supplier.SupplierDO;
import com.lxzl.erp.dataaccess.domain.warehouse.WarehouseDO;
import com.lxzl.se.common.util.StringUtil;
import com.lxzl.se.dataaccess.mysql.config.PageQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.math.BigDecimal;
import java.util.*;


@Service
public class PurchaseOrderServiceImpl implements PurchaseOrderService {

    private static final Logger log = LoggerFactory.getLogger(PurchaseOrderServiceImpl.class);
    @Autowired
    private UserSupport userSupport;
    @Autowired
    private PurchaseOrderMapper purchaseOrderMapper;
    @Autowired
    private ProductSkuMapper productSkuMapper;
    @Autowired
    private ProductService productService;
    @Autowired
    private PurchaseOrderProductMapper purchaseOrderProductMapper;
    @Autowired
    private PurchaseDeliveryOrderMapper purchaseDeliveryOrderMapper;
    @Autowired
    private PurchaseDeliveryOrderProductMapper purchaseDeliveryOrderProductMapper;
    @Autowired
    private PurchaseReceiveOrderMapper purchaseReceiveOrderMapper;
    @Autowired
    private PurchaseReceiveOrderProductMapper purchaseReceiveOrderProductMapper;
    @Autowired
    private PurchaseOrderSupport purchaseOrderSupport;
    @Autowired
    private WarehouseService warehouseService;
    @Autowired
    private WorkflowService workflowService;
    @Autowired
    private WarehouseMapper warehouseMapper;
    @Autowired
    private SupplierMapper supplierMapper;
    @Autowired
    private CompanyService companyService;
    @Autowired
    private MaterialMapper materialMapper;
    @Autowired
    private MaterialService materialService;
    @Autowired
    private PurchaseOrderMaterialMapper purchaseOrderMaterialMapper;
    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public ServiceResult<String, String> add(PurchaseOrder purchaseOrder) {
        ServiceResult<String, String> result = new ServiceResult<>();
        Date now = new Date();
        List<PurchaseOrderProduct> purchaseOrderProductList = purchaseOrder.getPurchaseOrderProductList();
        List<PurchaseOrderMaterial> purchaseOrderMaterialList = purchaseOrder.getPurchaseOrderMaterialList();
        WarehouseDO warehouseDO = warehouseMapper.finByNo(purchaseOrder.getWarehouseNo());

        if(warehouseDO==null){
            result.setErrorCode(ErrorCode.WAREHOUSE_NOT_EXISTS);
            return result;
        }
        SupplierDO supplierDO = supplierMapper.findById(purchaseOrder.getProductSupplierId());
        if(supplierDO==null){
            result.setErrorCode(ErrorCode.SUPPLIER_NOT_EXISTS);
            return result;
        }
        //判断操作人是否可以选择该仓库
        boolean warehouseOp = userSupport.checkCurrentUserWarehouse(warehouseDO.getId());
        if(!warehouseOp){
            result.setErrorCode(ErrorCode.USER_CAN_NOT_OP_WAREHOUSE);
            return result;
        }
        //商品项列表和物料项列表至少有一个不为空
        if(CollectionUtil.isEmpty(purchaseOrderProductList)&&CollectionUtil.isEmpty(purchaseOrder.getPurchaseOrderMaterialList())){
            result.setErrorCode(ErrorCode.PURCHASE_PRODUCT_MATERIAL_CAN_NOT_ALL_NULL);
            return result;
        }
        //判断该笔采购单是总公司发起还是分公司发起，分公司表要加一个字段代表是否是总公司,这里取的用户是session中的user
        boolean isHead = userSupport.isHeadUser();

        PurchaseOrderDetail purchaseOrderDetail = new PurchaseOrderDetail(userSupport.getCurrentUserId().toString(),now);
        //校验并准备数据
        String checkErrorCode = checkDetail(purchaseOrderDetail,purchaseOrder,purchaseOrderProductList,purchaseOrderMaterialList,isHead ,now);
        if(!ErrorCode.SUCCESS.equals(checkErrorCode)){
            result.setErrorCode(checkErrorCode);
            return result;
        }

        PurchaseOrderDO purchaseOrderDO = buildPurchaseOrder(now,purchaseOrder,purchaseOrderDetail,warehouseDO);
        purchaseOrderDO.setPurchaseNo(GenerateNoUtil.generatePurchaseOrderNo(now, userSupport.getCurrentUserId()));
        purchaseOrderDO.setCreateUser(userSupport.getCurrentUserId().toString());
        purchaseOrderDO.setCreateTime(now);
        purchaseOrderDO.setWarehouseId(warehouseDO.getId());
        purchaseOrderMapper.save(purchaseOrderDO);
        //保存采购订单商品项
        for(PurchaseOrderProductDO purchaseOrderProductDO : purchaseOrderDetail.purchaseOrderProductDOList){
            purchaseOrderProductDO.setPurchaseOrderId(purchaseOrderDO.getId());
            purchaseOrderProductMapper.save(purchaseOrderProductDO);
        }
        //保存采购订单物料项
        for(PurchaseOrderMaterialDO purchaseOrderMaterialDO : purchaseOrderDetail.purchaseOrderMaterialDOList){
            purchaseOrderMaterialDO.setPurchaseOrderId(purchaseOrderDO.getId());
            purchaseOrderMaterialMapper.save(purchaseOrderMaterialDO);
        }
        result.setResult(purchaseOrderDO.getPurchaseNo());
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }
    private PurchaseOrderDO buildPurchaseOrder(Date now , PurchaseOrder purchaseOrder,PurchaseOrderDetail purchaseOrderDetail ,WarehouseDO warehouseDO){
        //保存采购单
        PurchaseOrderDO purchaseOrderDO = PurchaseOrderConverter.convertPurchaseOrder(purchaseOrder);

        //查询库房信息并保存库房快照
        purchaseOrderDO.setWarehouseSnapshot(JSON.toJSONString(WarehouseConverter.convertWarehouseDO(warehouseDO)));
        purchaseOrderDO.setPurchaseOrderAmountTotal(purchaseOrderDetail.totalAmount);
        //创建采购单时，采购单实收金额和采购单结算金额为空
        purchaseOrderDO.setPurchaseOrderAmountReal(null);
        purchaseOrderDO.setPurchaseOrderAmountStatement(null);
        purchaseOrderDO.setPurchaseOrderStatus(PurchaseOrderStatus.PURCHASE_ORDER_STATUS_WAIT_COMMIT);
        purchaseOrderDO.setDeliveryTime(null);
        purchaseOrderDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        purchaseOrderDO.setOwner(userSupport.getCurrentUserId());
        purchaseOrderDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        purchaseOrderDO.setUpdateTime(now);
        return purchaseOrderDO;
    }

    private String checkDetail(PurchaseOrderDetail purchaseOrderDetail,PurchaseOrder purchaseOrder,List<PurchaseOrderProduct> purchaseOrderProductList,List<PurchaseOrderMaterial> purchaseOrderMaterialList,boolean isHead , Date now){

        //如果是整机四大件，物料列表只能有四大件物料
        if(PurchaseType.PURCHASE_TYPE_ALL_OR_MAIN==purchaseOrder.getPurchaseType()){

            if(CollectionUtil.isNotEmpty(purchaseOrderProductList)){
                //校验采购订单商品项
                String checkErrorCode = checkPurchaseOrderProductList(purchaseOrderDetail,purchaseOrderProductList,purchaseOrder.getIsNew(),isHead);
                if(!ErrorCode.SUCCESS.equals(checkErrorCode)){
                    return checkErrorCode;
                }
            }
            if(CollectionUtil.isNotEmpty(purchaseOrderMaterialList)){
                //校验采购订单物料项
                String checkErrorCode = checkPurchaseOrderMaterialList(purchaseOrderDetail,purchaseOrderMaterialList,purchaseOrder.getPurchaseType());
                if(!ErrorCode.SUCCESS.equals(checkErrorCode)){
                    return checkErrorCode;
                }

            }
        }else{
            //如果是小配件，物料项列表一定不为空
            if(CollectionUtil.isEmpty(purchaseOrderMaterialList)){
                return ErrorCode.PURCHASE_ORDER_MATERIAL_LIST_NOT_NULL;
            }
            //校验采购订单物料项
            String checkErrorCode = checkPurchaseOrderMaterialList(purchaseOrderDetail,purchaseOrderMaterialList,purchaseOrder.getPurchaseType());
            if(!ErrorCode.SUCCESS.equals(checkErrorCode)){
                return checkErrorCode;
            }
            //小配件没发票的，100元及以上的，不允许创建采购单
            if(CommonConstant.COMMON_CONSTANT_NO.equals(purchaseOrder.getIsInvoice())&&purchaseOrderDetail.totalMaterialAmount.compareTo(new BigDecimal(100))>=0){
                return ErrorCode.PURCHASE_ORDER_MATERIAL_CAN_NOT_CREATE;
            }
        }
        return ErrorCode.SUCCESS;
    }
    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public ServiceResult<String, String> update(PurchaseOrder purchaseOrder) {
        ServiceResult<String, String> result = new ServiceResult<>();
        //校验采购单是否存在
        PurchaseOrderDO purchaseOrderDO = purchaseOrderMapper.findByPurchaseNo(purchaseOrder.getPurchaseNo());
        if(purchaseOrderDO==null){
            result.setErrorCode(ErrorCode.PURCHASE_ORDER_NOT_EXISTS);
            return result;
        }
        WarehouseDO warehouseDO = warehouseMapper.finByNo(purchaseOrder.getWarehouseNo());
        if(warehouseDO==null){
            result.setErrorCode(ErrorCode.WAREHOUSE_NOT_EXISTS);
            return result;
        }
        SupplierDO supplierDO = supplierMapper.findById(purchaseOrder.getProductSupplierId());
        if(supplierDO==null){
            result.setErrorCode(ErrorCode.SUPPLIER_NOT_EXISTS);
            return result;
        }

        //判断操作人是否可以选择该仓库
        boolean warehouseOp = userSupport.checkCurrentUserWarehouse(warehouseDO.getId());
        if(!warehouseOp){
            result.setErrorCode(ErrorCode.USER_CAN_NOT_OP_WAREHOUSE);
            return result;
        }
        //只有待提交的采购单可以修改
        if(!PurchaseOrderStatus.PURCHASE_ORDER_STATUS_WAIT_COMMIT.equals(purchaseOrderDO.getPurchaseOrderStatus())){
            result.setErrorCode(ErrorCode.PURCHASE_ORDER_COMMITTED_CAN_NOT_UPDATE);
            return result;
        }

        //判断该笔采购单是总公司发起还是分公司发起，这里取的用户是createUser
        boolean isHead = userSupport.isHeadUser(Integer.valueOf(purchaseOrderDO.getCreateUser()));
        Date now = new Date();
        List<PurchaseOrderProduct> purchaseOrderProductList = purchaseOrder.getPurchaseOrderProductList();
        PurchaseOrderDetail purchaseOrderDetail = new PurchaseOrderDetail(userSupport.getCurrentUserId().toString(),now);
        //校验采购订单商品项
        String checkErrorCode = checkPurchaseOrderProductList(purchaseOrderDetail,purchaseOrderProductList,purchaseOrder.getIsNew(),isHead);
        if(!ErrorCode.SUCCESS.equals(checkErrorCode)){
            result.setErrorCode(checkErrorCode);
            return result;
        }

        //更新采购单
        Integer purchaseOrderDOId = purchaseOrderDO.getId();
        String purchaseNo = purchaseOrderDO.getPurchaseNo();
        purchaseOrderDO = buildPurchaseOrder(now,purchaseOrder,purchaseOrderDetail,warehouseDO);
        purchaseOrderDO.setPurchaseNo(purchaseNo);
        purchaseOrderDO.setId(purchaseOrderDOId);
        purchaseOrderDO.setWarehouseId(warehouseDO.getId());
        purchaseOrderMapper.update(purchaseOrderDO);

        //查询旧采购订单项列表
        List<PurchaseOrderProductDO> purchaseOrderProductDOList =  purchaseOrderSupport.getAllPurchaseOrderProductDO(purchaseOrderDO.getId());
        //为方便比对，将旧采购订单项列表存入map

        Map<Integer,PurchaseOrderProductDO> oldMap = new HashMap<>();
        for(PurchaseOrderProductDO purchaseOrderProductDO : purchaseOrderProductDOList){
            oldMap.put(purchaseOrderProductDO.getProductSkuId(),purchaseOrderProductDO);
        }
        //为方便比对，将新采购订单项列表存入map
        Map<Integer,PurchaseOrderProductDO> newMap = new HashMap<>();
        for(PurchaseOrderProductDO purchaseOrderProductDO : purchaseOrderDetail.purchaseOrderProductDOList){
            newMap.put(purchaseOrderProductDO.getProductSkuId(),purchaseOrderProductDO);
        }

        for(Integer skuId : newMap.keySet()){
            //如果原列表有此skuId，则更新
            if(oldMap.containsKey(skuId)){
                PurchaseOrderProductDO oldDO  = oldMap.get(skuId);
                Integer oldId = oldDO.getId();
                PurchaseOrderProductDO newDO = newMap.get(skuId);
                newDO.setId(oldId);
                newDO.setUpdateTime(now);
                newDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                purchaseOrderProductMapper.update(newDO);
            }else{ //如果原列表没有新列表有，则添加
                PurchaseOrderProductDO newDO = newMap.get(skuId);
                newDO.setPurchaseOrderId(purchaseOrderDOId);
                purchaseOrderProductMapper.save(newDO);
            }
        }
        for(Integer skuId : oldMap.keySet()){
            if(!newMap.containsKey(skuId)){//如果原列表有新列表没有，则删除
                purchaseOrderProductMapper.delete(oldMap.get(skuId).getId());
            }
        }
        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(purchaseOrderDO.getPurchaseNo());
        return result;
    }
    //生成待保存的采购单项辅助类
    class PurchaseOrderDetail{
        //声明待保存的采购单项列表
        private List<PurchaseOrderProductDO> purchaseOrderProductDOList = new ArrayList<>();
        //声明待保存的采购单物料项列表
        private List<PurchaseOrderMaterialDO> purchaseOrderMaterialDOList = new ArrayList<>();
        //声明累加采购单总价
        private BigDecimal totalAmount = new BigDecimal(0);
        //声明累加采购单商品项总价
        private BigDecimal totalProductAmount = new BigDecimal(0);
        //声明累加采购单物料项总价
        private BigDecimal totalMaterialAmount = new BigDecimal(0);
        private Date now = null;
        private String userId = null;
        public PurchaseOrderDetail(String userId , Date now){
            this.userId = userId;
            this.now = now;
        }
    }

    private String checkPurchaseOrderMaterialList(PurchaseOrderDetail purchaseOrderDetail , List<PurchaseOrderMaterial> purchaseOrderMaterialList ,Integer purchaseType){
        ServiceResult<String,PurchaseOrderDetail> result = new ServiceResult<>();

        //声明materialIdSet，如果一个采购单中有相同物料项，则返回错误
        Set<Integer> materialIdSet = new HashSet<>();
        //声明用于校验整机四大件列表
        List<Material> materialList = new ArrayList<>();
        // 验证采购单物料项是否合法
        for(PurchaseOrderMaterial purchaseOrderMaterial : purchaseOrderMaterialList){

            if(StringUtil.isEmpty(purchaseOrderMaterial.getMaterialNo())) {
                return ErrorCode.MATERIAL_NO_NOT_NULL;
            }
            if(purchaseOrderMaterial.getMaterialAmount()==null||  purchaseOrderMaterial.getMaterialAmount().doubleValue()<=0){
                return ErrorCode.MATERIAL_PRICE_ERROR;
            }
            if(purchaseOrderMaterial.getMaterialCount()==null||purchaseOrderMaterial.getMaterialCount()<=0){
                return ErrorCode.MATERIAL_COUNT_ERROR;
            }
            //保存采购订单物料项快照
            MaterialDO materialDO = materialMapper.findByNo(purchaseOrderMaterial.getMaterialNo());
            if(materialDO==null){
                return ErrorCode.MATERIAL_NOT_EXISTS;
            }
            materialIdSet.add(materialDO.getId());
            //累加采购单物料项总价
            purchaseOrderDetail.totalMaterialAmount = BigDecimalUtil.add(purchaseOrderDetail.totalMaterialAmount,BigDecimalUtil.mul(purchaseOrderMaterial.getMaterialAmount(),new BigDecimal(purchaseOrderMaterial.getMaterialCount())));

            PurchaseOrderMaterialDO purchaseOrderMaterialDO = new PurchaseOrderMaterialDO();


            Material material = MaterialConverter.convertMaterialDO(materialDO);
            materialList.add(material);
            purchaseOrderMaterialDO.setMaterialSnapshot(JSON.toJSONString(material));
            purchaseOrderMaterialDO.setMaterialId(materialDO.getId());
            purchaseOrderMaterialDO.setMaterialName(materialDO.getMaterialName());
            purchaseOrderMaterialDO.setMaterialCount(purchaseOrderMaterial.getMaterialCount());
            purchaseOrderMaterialDO.setMaterialAmount(purchaseOrderMaterial.getMaterialAmount());
            purchaseOrderMaterialDO.setRemark(purchaseOrderMaterial.getRemark());
            purchaseOrderMaterialDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
            purchaseOrderMaterialDO.setCreateUser(purchaseOrderDetail.userId);
            purchaseOrderMaterialDO.setUpdateUser(purchaseOrderDetail.userId);
            purchaseOrderMaterialDO.setCreateTime(purchaseOrderDetail.now);
            purchaseOrderMaterialDO.setUpdateTime(purchaseOrderDetail.now);
            purchaseOrderDetail.purchaseOrderMaterialDOList.add(purchaseOrderMaterialDO);
        }
        //采购单物料项物料ID有重复
        if(purchaseOrderMaterialList.size()!=materialIdSet.size()){
            return ErrorCode.PURCHASE_ORDER_MATERIAL_CAN_NOT_REPEAT;
        }
        //校验整机四大件类型的是否全为整机四大件
        if(PurchaseType.PURCHASE_TYPE_ALL_OR_MAIN==purchaseType&&!materialService.isAllMainMaterial(materialList)){
            return ErrorCode.PURCHASE_ORDER_MATERIAL_NOT_MAIN;
        }
        //校验小配件类型的是否全为小配件
        if(PurchaseType.PURCHASE_TYPE_GADGET==purchaseType&&!materialService.isAllGadget(materialList)){
            return ErrorCode.PURCHASE_ORDER_MATERIAL_NOT_GADGET;
        }

        return ErrorCode.SUCCESS;
    }
    private String checkPurchaseOrderProductList(PurchaseOrderDetail purchaseOrderDetail,List<PurchaseOrderProduct> purchaseOrderProductList , Integer isNew ,boolean isHead){

        // 验证采购单商品项是否合法
        for(PurchaseOrderProduct purchaseOrderProduct : purchaseOrderProductList){
            if(purchaseOrderProduct.getProductSkuId()==null){
                return ErrorCode.PRODUCT_SKU_NOT_NULL;
            }
            if(purchaseOrderProduct.getProductAmount()==null||  purchaseOrderProduct.getProductAmount().doubleValue()<=0){
                return ErrorCode.PRODUCT_SKU_PRICE_ERROR;
            }
            if(purchaseOrderProduct.getProductCount()==null||purchaseOrderProduct.getProductCount()<=0){
                return ErrorCode.PRODUCT_SKU_COUNT_ERROR;
            }
            ProductSkuDO productSkuDO = productSkuMapper.findById(purchaseOrderProduct.getProductSkuId());
            if(productSkuDO==null){
                return ErrorCode.PRODUCT_SKU_IS_NULL_OR_NOT_EXISTS;
            }
            //todo 校验此sku是否可以使用productMaterialList的物料配置
            if(purchaseOrderProduct.getProductMaterialList()==null){
                return ErrorCode.PURCHASE_ORDER_SKU_MATERIAL_ERROR;
            }
            //累加采购单总价
            purchaseOrderDetail.totalAmount = BigDecimalUtil.add(purchaseOrderDetail.totalAmount,BigDecimalUtil.mul(purchaseOrderProduct.getProductAmount(),new BigDecimal(purchaseOrderProduct.getProductCount())));

            PurchaseOrderProductDO purchaseOrderProductDO = new PurchaseOrderProductDO();
            //保存采购订单商品项快照
            ServiceResult<String,Product> productResult = productService.queryProductById(productSkuDO.getProductId());
            purchaseOrderProductDO.setProductSnapshot(JSON.toJSONString(getProductBySkuId(productResult.getResult(),productSkuDO.getId(),purchaseOrderProduct.getProductMaterialList())));
            purchaseOrderProductDO.setProductId(productSkuDO.getProductId());
            purchaseOrderProductDO.setProductName(productSkuDO.getProductName());
            purchaseOrderProductDO.setProductSkuId(productSkuDO.getId());
            purchaseOrderProductDO.setProductCount(purchaseOrderProduct.getProductCount());
            purchaseOrderProductDO.setProductAmount(purchaseOrderProduct.getProductAmount());
            purchaseOrderProductDO.setRemark(purchaseOrderProduct.getRemark());
            purchaseOrderProductDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
            purchaseOrderProductDO.setCreateUser(purchaseOrderDetail.userId);
            purchaseOrderProductDO.setUpdateUser(purchaseOrderDetail.userId);
            purchaseOrderProductDO.setCreateTime(purchaseOrderDetail.now);
            purchaseOrderProductDO.setUpdateTime(purchaseOrderDetail.now);
            purchaseOrderDetail.purchaseOrderProductDOList.add(purchaseOrderProductDO);
        }
        //发起者不是总公司 ，并且采购20000元以上全新机，则返回错误
        if(!isHead&&CommonConstant.COMMON_CONSTANT_YES.equals(isNew)&&purchaseOrderDetail.totalAmount.compareTo(new BigDecimal(20000))>=0){
            return ErrorCode.PURCHASE_ORDER_CANNOT_CREATE_BY_NEW_AND_AMOUNT;
        }
        return ErrorCode.SUCCESS;
    }
    private ServiceResult<String,List<PurchaseReceiveOrderProductDO>> checkPurchaseReceiveOrderProductListForUpdate(List<PurchaseReceiveOrderProduct> purchaseReceiveOrderProductList , String userId , Date now ){
        ServiceResult<String,List<PurchaseReceiveOrderProductDO>> result = new ServiceResult<>();
        List<PurchaseReceiveOrderProductDO> purchaseReceiveOrderProductDOList = new ArrayList<>();
        //声明skuSet，如果一个采购单中有相同sku的商品项，则返回错误
        Set<Integer> skuSet = new HashSet<>();
        // 验证采购单商品项是否合法
        for(PurchaseReceiveOrderProduct purchaseReceiveOrderProduct : purchaseReceiveOrderProductList){

            if(purchaseReceiveOrderProduct.getRealProductSkuId()==null){
                result.setErrorCode(ErrorCode.PURCHASE_RECEIVE_ORDER_PRODUCT_SKU_ID_NOT_NULL);
                return result;
            }else{
                skuSet.add(purchaseReceiveOrderProduct.getRealProductSkuId());
            }
            if(purchaseReceiveOrderProduct.getRealProductCount()==null||purchaseReceiveOrderProduct.getRealProductCount()<=0){
                result.setErrorCode(ErrorCode.PURCHASE_RECEIVE_ORDER_PRODUCT_REAL_COUNT_NOT_NULL);
                return result;
            }

            ProductSkuDO productSkuDO = productSkuMapper.findById(purchaseReceiveOrderProduct.getRealProductSkuId());
            if(productSkuDO==null){
                result.setErrorCode(ErrorCode.PRODUCT_SKU_IS_NULL_OR_NOT_EXISTS);
                return result;
            }
            //todo 校验此sku是否可以使用productMaterialList的物料配置
            if(purchaseReceiveOrderProduct.getProductMaterialList()==null){
                result.setErrorCode(ErrorCode.PURCHASE_ORDER_SKU_MATERIAL_ERROR);
                return result;
            }
            PurchaseReceiveOrderProductDO purchaseReceiveOrderProductDO = new PurchaseReceiveOrderProductDO();
            //保存采购订单商品项快照
            ServiceResult<String,Product> productResult = productService.queryProductDetailById(productSkuDO.getProductId());
            purchaseReceiveOrderProductDO.setRealProductSnapshot(JSON.toJSONString(getProductBySkuId(productResult.getResult(),productSkuDO.getId(),purchaseReceiveOrderProduct.getProductMaterialList())));
            purchaseReceiveOrderProductDO.setRealProductId(productSkuDO.getProductId());
            purchaseReceiveOrderProductDO.setRealProductName(productSkuDO.getProductName());
            purchaseReceiveOrderProductDO.setRealProductSkuId(productSkuDO.getId());
            purchaseReceiveOrderProductDO.setRealProductCount(purchaseReceiveOrderProduct.getRealProductCount());
            purchaseReceiveOrderProductDO.setRemark(purchaseReceiveOrderProduct.getRemark());
            purchaseReceiveOrderProductDO.setUpdateUser(userId);
            purchaseReceiveOrderProductDO.setUpdateTime(now);
            purchaseReceiveOrderProductDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
            purchaseReceiveOrderProductDOList.add(purchaseReceiveOrderProductDO);
        }
        //skuId有重复
        if(purchaseReceiveOrderProductList.size()!=skuSet.size()){
            result.setErrorCode(ErrorCode.PURCHASE_ORDER_PRODUCT_CAN_NOT_REPEAT);
            return result;
        }
        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(purchaseReceiveOrderProductDOList);
        return result;
    }
    private Product getProductBySkuId(Product product , Integer productSkuId, List<ProductMaterial> materialList){
        List<ProductSku> productSkuList = product.getProductSkuList();
        List<ProductSku> list = new ArrayList<>();
        for(ProductSku productSku : productSkuList){
            if(productSkuId!=null&&productSkuId.equals(productSku.getSkuId())){
                list.add(productSku);
                productSku.setProductMaterialList(materialList);
                break;
            }
        }
        product.setProductSkuList(list);
        return product;
    }
    @Override
    public ServiceResult<String, PurchaseOrder> queryPurchaseOrderByNo(String purchaseNo) {
        ServiceResult<String, PurchaseOrder> serviceResult = new ServiceResult<>();
        PurchaseOrderDO purchaseOrderDO = purchaseOrderMapper.findDetailByPurchaseNo(purchaseNo);
        if(purchaseOrderDO==null){
            serviceResult.setErrorCode(ErrorCode.PURCHASE_ORDER_NOT_EXISTS);
        }
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        PurchaseOrder purchaseOrder = PurchaseOrderConverter.convertPurchaseOrderDO(purchaseOrderDO);
        List<PurchaseDeliveryOrderDO> purchaseDeliveryOrderDOList = purchaseDeliveryOrderMapper.findListByPurchaseId(purchaseOrderDO.getId());
        List<PurchaseReceiveOrderDO> purchaseReceiveOrderDOList = purchaseReceiveOrderMapper.findListByPurchaseId(purchaseOrderDO.getId());

        List<PurchaseDeliveryOrder> purchaseDeliveryOrderList = PurchaseOrderConverter.convertPurchaseDeliveryOrderDOList(purchaseDeliveryOrderDOList);
        List<PurchaseReceiveOrder> purchaseReceiveOrderList = PurchaseOrderConverter.convertPurchaseReceiveOrderDOList(purchaseReceiveOrderDOList);
        purchaseOrder.setPurchaseDeliveryOrderList(purchaseDeliveryOrderList);
        purchaseOrder.setPurchaseReceiveOrderList(purchaseReceiveOrderList);
        serviceResult.setResult(purchaseOrder);
        return serviceResult;
    }

    @Override
    public ServiceResult<String, Page<PurchaseOrder>> page(PurchaseOrderQueryParam purchaseOrderQueryParam) {
        ServiceResult<String, Page<PurchaseOrder>> result = new ServiceResult<>();
        PageQuery pageQuery = new PageQuery(purchaseOrderQueryParam.getPageNo(), purchaseOrderQueryParam.getPageSize());
        purchaseOrderQueryParam.setWarehouseId(null);
        if(StringUtil.isNotEmpty(purchaseOrderQueryParam.getWarehouseNo())){
            WarehouseDO warehouseDO = warehouseMapper.finByNo(purchaseOrderQueryParam.getWarehouseNo());
            if(warehouseDO==null){
                result.setErrorCode(ErrorCode.SUCCESS);
                Page<PurchaseOrder> page = new Page<>(new ArrayList<PurchaseOrder>(), 0, purchaseOrderQueryParam.getPageNo(), purchaseOrderQueryParam.getPageSize());
                result.setResult(page);
                return result;
            }
            purchaseOrderQueryParam.setWarehouseId(warehouseDO.getId());
        }
        Map<String, Object> maps = new HashMap<>();
        maps.put("start", pageQuery.getStart());
        maps.put("pageSize", pageQuery.getPageSize());
        maps.put("queryParam", purchaseOrderQueryParam);

        Integer totalCount = purchaseOrderMapper.findPurchaseOrderCountByParams(maps);
        List<PurchaseOrderDO> purchaseOrderDOList = purchaseOrderMapper.findPurchaseOrderByParams(maps);
        List<PurchaseOrder> purchaseOrderList = PurchaseOrderConverter.convertPurchaseOrderDOList(purchaseOrderDOList);
        Page<PurchaseOrder> page = new Page<>(purchaseOrderList, totalCount, purchaseOrderQueryParam.getPageNo(), purchaseOrderQueryParam.getPageSize());

        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(page);
        return result;
    }

    @Override
    public ServiceResult<String, Integer> commit(PurchaseOrderCommitParam purchaseOrderCommitParam) {
        ServiceResult<String, Integer> result = new ServiceResult<>();
        //校验采购单是否存在
        PurchaseOrderDO purchaseOrderDO = purchaseOrderMapper.findByPurchaseNo(purchaseOrderCommitParam.getPurchaseNo());
        if(purchaseOrderDO==null){
            result.setErrorCode(ErrorCode.PURCHASE_ORDER_NOT_EXISTS);
            return result;
        }else if(!PurchaseOrderStatus.PURCHASE_ORDER_STATUS_WAIT_COMMIT.equals(purchaseOrderDO.getPurchaseOrderStatus())){
            //只有待提交状态的采购单可以提交
            result.setErrorCode(ErrorCode.PURCHASE_ORDER_COMMITTED_CAN_NOT_COMMIT_AGAIN);
            return result;
        }
        //调用审核服务
        ServiceResult<String ,Integer > verifyResult = workflowService.commitWorkFlow(WorkflowType.WORKFLOW_TYPE_PURCHASE,purchaseOrderDO.getId(),purchaseOrderCommitParam.getVerifyUserId());
        //修改提交审核状态
        if(ErrorCode.SUCCESS.equals(verifyResult.getErrorCode())){
            purchaseOrderDO.setPurchaseOrderStatus(PurchaseOrderStatus.PURCHASE_ORDER_STATUS_VERIFYING);
            purchaseOrderMapper.update(purchaseOrderDO);
        }
        return verifyResult;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public String delete(PurchaseOrder purchaseOrder) {
        PurchaseOrderDO purchaseOrderDO = purchaseOrderMapper.findByPurchaseNo(purchaseOrder.getPurchaseNo());
        if(purchaseOrderDO==null){
            return ErrorCode.PURCHASE_ORDER_NOT_EXISTS;
        }
        if(!PurchaseOrderStatus.PURCHASE_ORDER_STATUS_WAIT_COMMIT.equals(purchaseOrderDO.getPurchaseOrderStatus())){
            //只有待提交状态的采购单可以删除
            return ErrorCode.PURCHASE_ORDER_COMMITTED_CAN_NOT_DELETE;
        }
        purchaseOrderMapper.deleteByPurchaseNo(purchaseOrderDO.getPurchaseNo());
        purchaseOrderProductMapper.deleteByPurchaseOrderId(purchaseOrderDO.getId());
        return ErrorCode.SUCCESS;
    }

    @Override
    public ServiceResult<String, Page<PurchaseDeliveryOrder>> pagePurchaseDelivery(PurchaseDeliveryOrderQueryParam purchaseDeliveryOrderQueryParam) {
        ServiceResult<String, Page<PurchaseDeliveryOrder>> result = new ServiceResult<>();
        PageQuery pageQuery = new PageQuery(purchaseDeliveryOrderQueryParam.getPageNo(), purchaseDeliveryOrderQueryParam.getPageSize());
        purchaseDeliveryOrderQueryParam.setPurchaseOrderId(null);
        purchaseDeliveryOrderQueryParam.setWarehouseId(null);
        if(StringUtil.isNotEmpty(purchaseDeliveryOrderQueryParam.getPurchaseNo())){
            PurchaseOrderDO purchaseOrderDO = purchaseOrderMapper.findByPurchaseNo(purchaseDeliveryOrderQueryParam.getPurchaseNo());
            if(purchaseOrderDO==null){
                result.setErrorCode(ErrorCode.SUCCESS);
                Page<PurchaseDeliveryOrder> page = new Page<>(new ArrayList<PurchaseDeliveryOrder>(), 0, purchaseDeliveryOrderQueryParam.getPageNo(), purchaseDeliveryOrderQueryParam.getPageSize());
                result.setResult(page);
                return result;
            }
            purchaseDeliveryOrderQueryParam.setPurchaseOrderId(purchaseOrderDO.getId());
        }
        if(StringUtil.isNotEmpty(purchaseDeliveryOrderQueryParam.getWarehouseNo())){
            WarehouseDO warehouseDO = warehouseMapper.finByNo(purchaseDeliveryOrderQueryParam.getWarehouseNo());
            if(warehouseDO==null){
                result.setErrorCode(ErrorCode.SUCCESS);
                Page<PurchaseDeliveryOrder> page = new Page<>(new ArrayList<PurchaseDeliveryOrder>(), 0, purchaseDeliveryOrderQueryParam.getPageNo(), purchaseDeliveryOrderQueryParam.getPageSize());
                result.setResult(page);
                return result;
            }
            purchaseDeliveryOrderQueryParam.setWarehouseId(warehouseDO.getId());
        }
        Map<String, Object> maps = new HashMap<>();
        maps.put("start", pageQuery.getStart());
        maps.put("pageSize", pageQuery.getPageSize());
        maps.put("queryParam", purchaseDeliveryOrderQueryParam);

        Integer totalCount = purchaseDeliveryOrderMapper.findPurchaseDeliveryOrderCountByParams(maps);
        List<PurchaseDeliveryOrderDO> purchaseDeliveryOrderDOList = purchaseDeliveryOrderMapper.findPurchaseDeliveryOrderByParams(maps);
        List<PurchaseDeliveryOrder> purchaseDeliveryOrderList = PurchaseOrderConverter.convertPurchaseDeliveryOrderDOList(purchaseDeliveryOrderDOList);
        Page<PurchaseDeliveryOrder> page = new Page<>(purchaseDeliveryOrderList, totalCount, purchaseDeliveryOrderQueryParam.getPageNo(), purchaseDeliveryOrderQueryParam.getPageSize());

        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(page);
        return result;
    }

    @Override
    public ServiceResult<String, PurchaseDeliveryOrder> queryPurchaseDeliveryOrderByNo(PurchaseDeliveryOrder purchaseDeliveryOrder) {
        ServiceResult<String, PurchaseDeliveryOrder> serviceResult = new ServiceResult<>();
        PurchaseDeliveryOrderDO purchaseDeliveryOrderDO =  purchaseDeliveryOrderMapper.findByNo(purchaseDeliveryOrder.getPurchaseDeliveryNo());
        if(purchaseDeliveryOrderDO==null){
            serviceResult.setErrorCode(ErrorCode.PURCHASE_DELIVERY_ORDER_NOT_EXISTS);
            return serviceResult;
        }
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(PurchaseOrderConverter.convertPurchaseDeliveryOrderDO(purchaseDeliveryOrderDO));
        return serviceResult;
    }

    /**
     * 修改采购收货单
     * 分拨情况为已分拨的（自动流转到总仓的采购收货单）不可以修改
     * 采购收货单状态为已签单的不可以修改
     * @param purchaseReceiveOrder
     * @return
     */
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    @Override
    public ServiceResult<String, String> updatePurchaseReceiveOrder(PurchaseReceiveOrder purchaseReceiveOrder) {
        ServiceResult<String, String> serviceResult = new ServiceResult<>();
        PurchaseReceiveOrderDO purchaseReceiveOrderDO = purchaseReceiveOrderMapper.findAllByNo(purchaseReceiveOrder.getPurchaseReceiveNo());
        if(purchaseReceiveOrderDO==null){
            serviceResult.setErrorCode(ErrorCode.PURCHASE_RECEIVE_ORDER_NOT_EXISTS);
            return serviceResult;
        }
        //分拨情况为已分拨的（自动流转到总仓的采购收货单）不可以修改
        if(AutoAllotStatus.AUTO_ALLOT_STATUS_YES.equals(purchaseReceiveOrderDO.getAutoAllotStatus())){
            serviceResult.setErrorCode(ErrorCode.PURCHASE_RECEIVE_ORDER_AUTO_ALLOT_YES_CAN_NOT_UPDATE);
            return serviceResult;
        }
        if(PurchaseReceiveOrderStatus.PURCHASE_RECEIVE_ORDER_STATUS_YET.equals(purchaseReceiveOrderDO.getPurchaseReceiveOrderStatus())){
            serviceResult.setErrorCode(ErrorCode.PURCHASE_RECEIVE_ORDER_STATUS_YET_CAN_NOT_UPDATE);
            return serviceResult;
        }
        Date now = new Date();
        purchaseReceiveOrderDO.setIsNew(purchaseReceiveOrder.getIsNew());
        purchaseReceiveOrderDO.setUpdateTime(now);
        purchaseReceiveOrderDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        purchaseReceiveOrderMapper.update(purchaseReceiveOrderDO);

        //这里的商品项列表，包含了所有dataStatus的数据
        List<PurchaseReceiveOrderProductDO> oldProductList = purchaseReceiveOrderDO.getPurchaseReceiveOrderProductDOList();
        //为方便比对，将旧采购订单项列表存入map
        Map<Integer,PurchaseReceiveOrderProductDO> oldMap = new HashMap<>();
        for(PurchaseReceiveOrderProductDO purchaseReceiveOrderProductDO : oldProductList){
            oldMap.put(purchaseReceiveOrderProductDO.getRealProductSkuId(),purchaseReceiveOrderProductDO);
        }
        ServiceResult<String,List<PurchaseReceiveOrderProductDO>> newProductResult = checkPurchaseReceiveOrderProductListForUpdate(purchaseReceiveOrder.getPurchaseReceiveOrderProductList(),userSupport.getCurrentUserId().toString(),now);
        if(!ErrorCode.SUCCESS.equals(newProductResult.getErrorCode())){
            serviceResult.setErrorCode(newProductResult.getErrorCode());
            return serviceResult;
        }
        //为方便比对，将新采购订单项列表存入map
        Map<Integer,PurchaseReceiveOrderProductDO> newMap = new HashMap<>();
        for(PurchaseReceiveOrderProductDO purchaseReceiveOrderProductDO : newProductResult.getResult()){
            newMap.put(purchaseReceiveOrderProductDO.getRealProductSkuId(),purchaseReceiveOrderProductDO);
        }

        for(Integer skuId : newMap.keySet()){
            //如果原列表有此skuId，则更新
            if(oldMap.containsKey(skuId)){
                PurchaseReceiveOrderProductDO oldDO  = oldMap.get(skuId);
                Integer oldId = oldDO.getId();
                PurchaseReceiveOrderProductDO newDO = newMap.get(skuId);
                newDO.setId(oldId);
                purchaseReceiveOrderProductMapper.update(newDO);
            }else{ //如果原列表没有新列表有，则添加
                PurchaseReceiveOrderProductDO newDO = newMap.get(skuId);
                //这种添加属于收货后添加的，用该字段标志
                newDO.setIsSrc(CommonConstant.COMMON_CONSTANT_NO);
                newDO.setPurchaseReceiveOrderId(purchaseReceiveOrderDO.getId());
                newDO.setCreateTime(now);
                newDO.setCreateUser(userSupport.getCurrentUserId().toString());
                purchaseReceiveOrderProductMapper.save(newDO);
            }
        }
        for(Integer skuId : oldMap.keySet()){
            if(!newMap.containsKey(skuId)){//如果原列表有新列表没有，则删除
                PurchaseReceiveOrderProductDO purchaseReceiveOrderProductDO = oldMap.get(skuId);
                purchaseReceiveOrderProductDO.setDataStatus(CommonConstant.DATA_STATUS_DELETE);
                purchaseReceiveOrderProductDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                purchaseReceiveOrderProductDO.setUpdateTime(now);
                purchaseReceiveOrderProductMapper.update(purchaseReceiveOrderProductDO);
            }
        }
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(purchaseReceiveOrderDO.getPurchaseReceiveNo());
        return serviceResult;
    }

    /**
     * 采购收货单确认签单
     * 调用入库接口，改变采购收货单状态，改变采购收货单状态
     * 如果有自动流转总仓，则总仓数据一并更新
     * @param purchaseReceiveOrder
     * @return
     */
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    @Override
    public ServiceResult<String, String> commitPurchaseReceiveOrder(PurchaseReceiveOrder purchaseReceiveOrder) {
        ServiceResult<String, String> serviceResult = new ServiceResult<>();
        PurchaseReceiveOrderDO purchaseReceiveOrderDO = purchaseReceiveOrderMapper.findAllByNo(purchaseReceiveOrder.getPurchaseReceiveNo());
        if(purchaseReceiveOrderDO==null){
            serviceResult.setErrorCode(ErrorCode.PURCHASE_RECEIVE_ORDER_NOT_EXISTS);
            return serviceResult;
        }
        //已入库的采购单不能再次入库
        if(PurchaseReceiveOrderStatus.PURCHASE_RECEIVE_ORDER_STATUS_YET.equals(purchaseReceiveOrderDO.getPurchaseReceiveOrderStatus())){
            serviceResult.setErrorCode(ErrorCode.PURCHASE_RECEIVE_ORDER_STATUS_YET_CAN_NOT_IN_AGAIN);
            return serviceResult;
        }
        //组装入库接口数据
        ProductInStockParam productInStockParam = new ProductInStockParam();
        productInStockParam.setTargetWarehouseId(purchaseReceiveOrderDO.getWarehouseId());
        productInStockParam.setCauseType(StockCauseType.STOCK_CAUSE_TYPE_IN_PURCHASE);
        productInStockParam.setReferNo(purchaseReceiveOrderDO.getPurchaseReceiveNo());
        List<ProductInStorage> productInStorageList = new ArrayList<>();
        List<PurchaseReceiveOrderProductDO> list = purchaseReceiveOrderDO.getPurchaseReceiveOrderProductDOList();
        //用来保存实际采购的sku及数量
        Map<Integer,PurchaseReceiveOrderProductDO> realSkuMap = new HashMap<>();
        //构建入库参数的同时，将数据保存到map一份，方便后面匹配采购单商品项
        for(PurchaseReceiveOrderProductDO purchaseReceiveOrderProductDO : list){
            if(CommonConstant.DATA_STATUS_ENABLE.equals(purchaseReceiveOrderProductDO.getDataStatus())){
                ProductInStorage productInStorage = new ProductInStorage();
                productInStorage.setProductId(purchaseReceiveOrderProductDO.getRealProductId());
                productInStorage.setProductSkuId(purchaseReceiveOrderProductDO.getRealProductSkuId());
                productInStorage.setProductCount(purchaseReceiveOrderProductDO.getRealProductCount());
                String realProductSnapshot = purchaseReceiveOrderProductDO.getRealProductSnapshot();
                Product product = JSON.parseObject(realProductSnapshot,Product.class);
                productInStorage.setProductMaterialList(product.getProductSkuList().get(0).getProductMaterialList());
                productInStorageList.add(productInStorage);
            }
            realSkuMap.put(purchaseReceiveOrderProductDO.getRealProductSkuId(),purchaseReceiveOrderProductDO);
        }
        productInStockParam.setProductInStorageList(productInStorageList);


        Date now  = new Date();
        //更新采购收货单状态
        purchaseReceiveOrderDO.setPurchaseReceiveOrderStatus(PurchaseReceiveOrderStatus.PURCHASE_RECEIVE_ORDER_STATUS_YET);
        purchaseReceiveOrderDO.setConfirmTime(now);
        purchaseReceiveOrderDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        purchaseReceiveOrderDO.setUpdateTime(now);
        purchaseReceiveOrderMapper.update(purchaseReceiveOrderDO);

        //如果是被总仓分拨的采购收货单，则总仓分拨单，分拨单项一并更新
        if(AutoAllotStatus.AUTO_ALLOT_STATUS_RECEIVE.equals(purchaseReceiveOrderDO.getAutoAllotStatus())){
            PurchaseReceiveOrderDO autoPurchaseReceiveOrderDO = purchaseReceiveOrderMapper.findAutoAllotReceiveOrder(purchaseReceiveOrderDO.getAutoAllotNo());
            autoPurchaseReceiveOrderDO.setPurchaseReceiveOrderStatus(PurchaseReceiveOrderStatus.PURCHASE_RECEIVE_ORDER_STATUS_YET);
            autoPurchaseReceiveOrderDO.setConfirmTime(now);
            autoPurchaseReceiveOrderDO.setUpdateUser(CommonConstant.SUPER_USER_ID.toString());
            autoPurchaseReceiveOrderDO.setUpdateTime(now);
            purchaseReceiveOrderMapper.update(purchaseReceiveOrderDO);
            List<PurchaseReceiveOrderProductDO> purchaseReceiveOrderProductDOList = autoPurchaseReceiveOrderDO.getPurchaseReceiveOrderProductDOList();
            //保存新列表的map，方便后面比对
            Map<Integer,PurchaseReceiveOrderProductDO> newSkuMap = new HashMap<>();
            for(PurchaseReceiveOrderProductDO autoPurchaseReceiveOrderProductDO : purchaseReceiveOrderProductDOList){
                newSkuMap.put(autoPurchaseReceiveOrderProductDO.getRealProductSkuId(),autoPurchaseReceiveOrderProductDO);
                PurchaseReceiveOrderProductDO purchaseReceiveOrderProductDO = realSkuMap.get(autoPurchaseReceiveOrderProductDO.getProductSkuId());
                autoPurchaseReceiveOrderProductDO.setRealProductCount(purchaseReceiveOrderProductDO.getRealProductCount());
                autoPurchaseReceiveOrderProductDO.setRealProductId(purchaseReceiveOrderProductDO.getRealProductId());
                autoPurchaseReceiveOrderProductDO.setRealProductName(purchaseReceiveOrderProductDO.getRealProductName());
                autoPurchaseReceiveOrderProductDO.setRealProductSkuId(purchaseReceiveOrderProductDO.getRealProductSkuId());
                autoPurchaseReceiveOrderProductDO.setRealProductSnapshot(purchaseReceiveOrderProductDO.getRealProductSnapshot());
                autoPurchaseReceiveOrderProductDO.setUpdateTime(now);
                autoPurchaseReceiveOrderProductDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                autoPurchaseReceiveOrderProductDO.setDataStatus(purchaseReceiveOrderProductDO.getDataStatus());
                purchaseReceiveOrderProductMapper.update(autoPurchaseReceiveOrderProductDO);
            }
            for(Integer skuId : realSkuMap.keySet()){
                if(!newSkuMap.containsKey(skuId)){
                    PurchaseReceiveOrderProductDO purchaseReceiveOrderProductDO = new PurchaseReceiveOrderProductDO();
                    BeanUtils.copyProperties(realSkuMap.get(skuId),purchaseReceiveOrderProductDO);
                    purchaseReceiveOrderProductDO.setPurchaseReceiveOrderId(autoPurchaseReceiveOrderDO.getId());
                    purchaseReceiveOrderProductDO.setCreateUser(CommonConstant.SUPER_USER_ID.toString());
                    purchaseReceiveOrderProductDO.setUpdateUser(CommonConstant.SUPER_USER_ID.toString());
                    purchaseReceiveOrderProductDO.setCreateTime(now);
                    purchaseReceiveOrderProductDO.setUpdateTime(now);
                    purchaseReceiveOrderProductDO.setId(null);
                    purchaseReceiveOrderProductMapper.save(purchaseReceiveOrderProductDO);
                }
            }
        }

        //判断采购单是否全部采购，更新采购单状态
        PurchaseOrderDO purchaseOrderDO = purchaseOrderMapper.findDetailById(purchaseReceiveOrderDO.getPurchaseOrderId());
        List<PurchaseOrderProductDO> purchaseOrderProductDOList = purchaseOrderDO.getPurchaseOrderProductDOList();
        //循环中临时记录循环的前面所有项的综合采购状态
        Integer lastPurchaseOrderStatus = PurchaseOrderStatus.PURCHASE_ORDER_STATUS_PURCHASING;
        for(PurchaseOrderProductDO purchaseOrderProductDO : purchaseOrderProductDOList){

            PurchaseReceiveOrderProductDO purchaseReceiveOrderProductDO = realSkuMap.get(purchaseOrderProductDO.getProductSkuId());
            //如果待采购商品项 在 已采购商品列表中
            if(purchaseReceiveOrderProductDO!=null){
                //如果实际采购商品数小于采购单商品项的商品数
                if(purchaseReceiveOrderProductDO.getRealProductCount()<purchaseOrderProductDO.getProductCount()){
                    lastPurchaseOrderStatus = PurchaseOrderStatus.PURCHASE_ORDER_STATUS_PART;
                }else if(PurchaseOrderStatus.PURCHASE_ORDER_STATUS_PURCHASING.equals(lastPurchaseOrderStatus)){
                    //如果实际采购商品数大于等于采购单商品项的商品数
                    //并且目前采购单状态为待采购状态，则更新采购状态为全部采购,如果是全部采购状态，不需要处理
                    lastPurchaseOrderStatus = PurchaseOrderStatus.PURCHASE_ORDER_STATUS_ALL;
                }
            }
        }
        purchaseOrderDO.setPurchaseOrderStatus(lastPurchaseOrderStatus);
        purchaseOrderDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        purchaseOrderDO.setUpdateTime(now);
        purchaseOrderMapper.update(purchaseOrderDO);
        //调用入库接口
        ServiceResult<String, Integer> inStockResult = warehouseService.productInStock(productInStockParam);
        if(!ErrorCode.SUCCESS.equals(inStockResult.getErrorCode())){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
            serviceResult.setErrorCode(inStockResult.getErrorCode());
            return serviceResult;
        }
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(purchaseReceiveOrder.getPurchaseReceiveNo());
        return serviceResult;
    }

    @Override
    public ServiceResult<String, Page<PurchaseReceiveOrder>> pagePurchaseReceive(PurchaseReceiveOrderQueryParam purchaseReceiveOrderQueryParam) {
        ServiceResult<String, Page<PurchaseReceiveOrder>> result = new ServiceResult<>();
        PageQuery pageQuery = new PageQuery(purchaseReceiveOrderQueryParam.getPageNo(), purchaseReceiveOrderQueryParam.getPageSize());
        //将3个ID的参数转为No
        purchaseReceiveOrderQueryParam.setPurchaseOrderId(null);
        purchaseReceiveOrderQueryParam.setWarehouseId(null);
        purchaseReceiveOrderQueryParam.setPurchaseDeliveryOrderId(null);
        Page<PurchaseReceiveOrder> nullPage = new Page<>(new ArrayList<PurchaseReceiveOrder>(), 0, purchaseReceiveOrderQueryParam.getPageNo(), purchaseReceiveOrderQueryParam.getPageSize());
        if(StringUtil.isNotEmpty(purchaseReceiveOrderQueryParam.getPurchaseNo())){
            PurchaseOrderDO purchaseOrderDO = purchaseOrderMapper.findByPurchaseNo(purchaseReceiveOrderQueryParam.getPurchaseNo());
            if(purchaseOrderDO==null){
                result.setErrorCode(ErrorCode.SUCCESS);
                result.setResult(nullPage);
                return result;
            }
            purchaseReceiveOrderQueryParam.setPurchaseOrderId(purchaseOrderDO.getId());
        }
        if(StringUtil.isNotEmpty(purchaseReceiveOrderQueryParam.getWarehouseNo())){
            WarehouseDO warehouseDO = warehouseMapper.finByNo(purchaseReceiveOrderQueryParam.getWarehouseNo());
            if(warehouseDO==null){
                result.setErrorCode(ErrorCode.SUCCESS);
                result.setResult(nullPage);
                return result;
            }
            purchaseReceiveOrderQueryParam.setWarehouseId(warehouseDO.getId());
        }
        if(StringUtil.isNotEmpty(purchaseReceiveOrderQueryParam.getPurchaseDeliveryNo())){
            PurchaseDeliveryOrderDO purchaseDeliveryOrderDO = purchaseDeliveryOrderMapper.findByNo(purchaseReceiveOrderQueryParam.getPurchaseDeliveryNo());
            if(purchaseDeliveryOrderDO==null){
                result.setErrorCode(ErrorCode.SUCCESS);
                result.setResult(nullPage);
                return result;
            }
            purchaseReceiveOrderQueryParam.setPurchaseDeliveryOrderId(purchaseDeliveryOrderDO.getId());
        }
        Map<String, Object> maps = new HashMap<>();
        maps.put("start", pageQuery.getStart());
        maps.put("pageSize", pageQuery.getPageSize());
        maps.put("queryParam", purchaseReceiveOrderQueryParam);

        Integer totalCount = purchaseReceiveOrderMapper.findPurchaseReceiveOrderCountByParams(maps);
        List<PurchaseReceiveOrderDO> purchaseReceiveOrderDOList = purchaseReceiveOrderMapper.findPurchaseReceiveOrderByParams(maps);
        List<PurchaseReceiveOrder> purchaseReceiveOrderList = PurchaseOrderConverter.convertPurchaseReceiveOrderDOList(purchaseReceiveOrderDOList);
        Page<PurchaseReceiveOrder> page = new Page<>(purchaseReceiveOrderList, totalCount, purchaseReceiveOrderQueryParam.getPageNo(), purchaseReceiveOrderQueryParam.getPageSize());

        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(page);
        return result;
    }

    @Override
    public ServiceResult<String, PurchaseReceiveOrder> queryPurchaseReceiveOrderByNo(PurchaseReceiveOrder purchaseReceiveOrder) {
        ServiceResult<String, PurchaseReceiveOrder> serviceResult = new ServiceResult<>();
        PurchaseReceiveOrderDO purchaseReceiveOrderDO =  purchaseReceiveOrderMapper.findByNo(purchaseReceiveOrder.getPurchaseReceiveNo());
        if(purchaseReceiveOrderDO==null){
            serviceResult.setErrorCode(ErrorCode.PURCHASE_RECEIVE_ORDER_NOT_EXISTS);
            return serviceResult;
        }
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(PurchaseOrderConverter.convertPurchaseReceiveOrderDO(purchaseReceiveOrderDO));
        return serviceResult;
    }

    /**
     * 采购单结束
     * 只有部分采购和全部采购的采购单 可以结束采购单
     * @param purchaseOrder
     * @return
     */
    @Override
    public ServiceResult<String, String> endPurchaseOrder(PurchaseOrder purchaseOrder) {
        ServiceResult<String, String> serviceResult = new ServiceResult<>();
        PurchaseOrderDO purchaseOrderDO = purchaseOrderMapper.findByPurchaseNo(purchaseOrder.getPurchaseNo());
        if(purchaseOrderDO==null){
            serviceResult.setErrorCode(ErrorCode.PURCHASE_ORDER_NOT_EXISTS);
            return serviceResult;
        }
        //只有部分采购 和 全部采购的采购单 可以结束采购单
        if(PurchaseOrderStatus.PURCHASE_ORDER_STATUS_PART.equals(purchaseOrderDO.getPurchaseOrderStatus())
                ||PurchaseOrderStatus.PURCHASE_ORDER_STATUS_ALL.equals(purchaseOrderDO.getPurchaseOrderStatus())){
            purchaseOrderDO.setPurchaseOrderStatus(PurchaseOrderStatus.PURCHASE_ORDER_STATUS_END);
            purchaseOrderDO.setUpdateTime(new Date());
            purchaseOrderDO.setUpdateUser(userSupport.getCurrentUserId().toString());
            purchaseOrderMapper.update(purchaseOrderDO);
            serviceResult.setErrorCode(ErrorCode.SUCCESS);
            serviceResult.setResult(purchaseOrderDO.getPurchaseNo());
        }else{
            serviceResult.setErrorCode(ErrorCode.PURCHASE_ORDER_STATUS_CAN_NOT_END);
        }
        return serviceResult;
    }
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    @Override
    public ServiceResult<String, String> continuePurchaseOrder(PurchaseOrder purchaseOrder) {
        ServiceResult<String, String> serviceResult = new ServiceResult<>();
        PurchaseOrderDO purchaseOrderDO = purchaseOrderMapper.findDetailByPurchaseNo(purchaseOrder.getPurchaseNo());
        if(purchaseOrderDO==null){
            serviceResult.setErrorCode(ErrorCode.PURCHASE_ORDER_NOT_EXISTS);
            return serviceResult;
        }
        //只有部分采购的采购单可以继续采购
        if(PurchaseOrderStatus.PURCHASE_ORDER_STATUS_PART.equals(purchaseOrderDO.getPurchaseOrderStatus())){
            //查询采购单的所有收货单列表
            List<PurchaseReceiveOrderDO> purchaseReceiveOrderDOList = purchaseReceiveOrderMapper.findListByPurchaseId(purchaseOrderDO.getId());
            //用来保存所有已签单的采购收货单实际已收到的sku总数
            Map<Integer,Integer> skuCountMap = new HashMap<>();
            for(PurchaseReceiveOrderDO purchaseReceiveOrderDO : purchaseReceiveOrderDOList){
                //只累加已签单的sku，并且非自动流转总仓的
                if(PurchaseReceiveOrderStatus.PURCHASE_RECEIVE_ORDER_STATUS_YET.equals(purchaseReceiveOrderDO.getPurchaseReceiveOrderStatus())
                        &&!AutoAllotStatus.AUTO_ALLOT_STATUS_YES.equals(purchaseReceiveOrderDO.getAutoAllotStatus())){
                    //找到的所有采购单项sku，累加数量
                    List<PurchaseReceiveOrderProductDO> purchaseReceiveOrderProductDOList = purchaseReceiveOrderDO.getPurchaseReceiveOrderProductDOList();
                    for(PurchaseReceiveOrderProductDO purchaseReceiveOrderProductDO : purchaseReceiveOrderProductDOList){
                        Integer countNow = skuCountMap.get(purchaseReceiveOrderProductDO.getRealProductSkuId());
                        if(countNow==null){
                            skuCountMap.put(purchaseReceiveOrderProductDO.getRealProductSkuId(),purchaseReceiveOrderProductDO.getRealProductCount());
                        }else{
                            skuCountMap.put(purchaseReceiveOrderProductDO.getRealProductSkuId(),countNow+purchaseReceiveOrderProductDO.getRealProductCount());
                        }
                    }
                }
            }
            List<PurchaseOrderProductDO> purchaseOrderProductDOList = purchaseOrderDO.getPurchaseOrderProductDOList();
            //找出没有完成的采购单项，并计算未采购完成的数量
            for(PurchaseOrderProductDO purchaseOrderProductDO : purchaseOrderProductDOList){
                Integer skuCount = skuCountMap.get(purchaseOrderProductDO.getProductSkuId());
                skuCount = skuCount == null?0:skuCount;
                //todo 保存待发货列表
                purchaseOrderProductDO.setProductCount(purchaseOrderProductDO.getProductCount()-skuCount);
            }
            //类似采购单审核之后的流程，自动生成发货通知单、收货通知单

            createPurchaseDeliveryAndReceiveOrder(purchaseOrderDO);
            serviceResult.setErrorCode(ErrorCode.SUCCESS);
            serviceResult.setResult(purchaseOrderDO.getPurchaseNo());
        }else{
            serviceResult.setErrorCode(ErrorCode.PURCHASE_ORDER_STATUS_CAN_NOT_CONTINUE);
        }
        return serviceResult;
    }

    /**
     * 接收审核结果通知，审核通过生成发货单
     * @param verifyResult
     * @param businessId
     */
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    @Override
    public boolean receiveVerifyResult(boolean verifyResult, Integer businessId) {
        try{
            PurchaseOrderDO purchaseOrderDO = purchaseOrderMapper.findDetailById(businessId);
            if(verifyResult){
                if(purchaseOrderDO==null){
                    throw new Exception();
                }
                createPurchaseDeliveryAndReceiveOrder(purchaseOrderDO);
                purchaseOrderDO.setPurchaseOrderStatus(PurchaseOrderStatus.PURCHASE_ORDER_STATUS_PURCHASING);
            }else{
                purchaseOrderDO.setPurchaseOrderStatus(PurchaseOrderStatus.PURCHASE_ORDER_STATUS_WAIT_COMMIT);
            }
            purchaseOrderMapper.update(purchaseOrderDO);
            return true;
        }catch (Exception e){
            log.error("【采购单审核后，业务处理异常，未生成发货单】",e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
            return false;
        }catch (Throwable t){
            log.error("【采购单审核后，业务处理异常，未生成发货单】",t);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
            return false;
        }
    }

    /**
     * 如果有发票：生成发货通知单及收货通知单
     * 如果没有发票；收货库房为总公司库：生成发货通知单，收料通知单
     * 如果没有发票；收货库房为分公司库：生成发货通知单，生成总公司收料通知单（real空着不存，待分公司收货通知单状态为已确认时回填）及分拨单号，生成分公司收料通知单
     * @param purchaseOrderDO
     */
    public void createPurchaseDeliveryAndReceiveOrder(PurchaseOrderDO purchaseOrderDO){
        //生成发货通知单
        PurchaseDeliveryOrderDO purchaseDeliveryOrderDO = createDeliveryDetail(purchaseOrderDO);
        if(CommonConstant.COMMON_CONSTANT_YES.equals(purchaseOrderDO.getIsInvoice())){//如果有发票
            //直接生成收料通知单
            createReceiveDetail(purchaseDeliveryOrderDO,AutoAllotStatus.AUTO_ALLOT_STATUS_NO,null);
        }else if(CommonConstant.COMMON_CONSTANT_NO.equals(purchaseOrderDO.getIsInvoice())){//如果没有发票
            //解析采购单库房快照，是否为总公司库
            Warehouse warehouse = JSON.parseObject(purchaseOrderDO.getWarehouseSnapshot(),Warehouse.class);
            boolean isHead  = SubCompanyType.SUB_COMPANY_TYPE_HEADER.equals(warehouse.getSubCompanyType());
            if(isHead){//如果是总公司仓库
                //直接生成收料通知单
                createReceiveDetail(purchaseDeliveryOrderDO,AutoAllotStatus.AUTO_ALLOT_STATUS_NO,null);
            }else{//如果是分公司仓库
                //生成总公司收料通知单
                PurchaseReceiveOrderDO purchaseReceiveOrderDO = createReceiveDetail(purchaseDeliveryOrderDO,AutoAllotStatus.AUTO_ALLOT_STATUS_YES,null);
                //生成分公司收料通知单
                createReceiveDetail(purchaseDeliveryOrderDO,AutoAllotStatus.AUTO_ALLOT_STATUS_RECEIVE,purchaseReceiveOrderDO.getAutoAllotNo());
            }
        }
    }

    /**
     * 生成发货通知单
     * @param purchaseOrderDO 采购原单
     */
    private PurchaseDeliveryOrderDO createDeliveryDetail(PurchaseOrderDO purchaseOrderDO){
        Date now = new Date();
        PurchaseDeliveryOrderDO purchaseDeliveryOrderDO = new PurchaseDeliveryOrderDO();
        purchaseDeliveryOrderDO.setPurchaseOrderId(purchaseOrderDO.getId());
        purchaseDeliveryOrderDO.setPurchaseDeliveryNo(GenerateNoUtil.generatePurchaseDeliveryOrderNo(now, purchaseOrderDO.getId()));
        purchaseDeliveryOrderDO.setWarehouseId(purchaseOrderDO.getWarehouseId());
        purchaseDeliveryOrderDO.setWarehouseSnapshot(purchaseOrderDO.getWarehouseSnapshot());
        purchaseDeliveryOrderDO.setIsInvoice(purchaseOrderDO.getIsInvoice());
        purchaseDeliveryOrderDO.setIsNew(purchaseOrderDO.getIsNew());
        purchaseDeliveryOrderDO.setPurchaseDeliveryOrderStatus(PurchaseDeliveryOrderStatus.PURCHASE_DELIVERY_ORDER_STATUS_WAIT);
        purchaseDeliveryOrderDO.setDeliveryTime(null);
        purchaseDeliveryOrderDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        purchaseDeliveryOrderDO.setOwnerSupplierId(purchaseOrderDO.getProductSupplierId());
        purchaseDeliveryOrderDO.setCreateUser(String.valueOf(CommonConstant.SUPER_USER_ID));
        purchaseDeliveryOrderDO.setUpdateUser(String.valueOf(CommonConstant.SUPER_USER_ID));
        purchaseDeliveryOrderDO.setCreateTime(now);
        purchaseDeliveryOrderDO.setUpdateTime(now);
        purchaseDeliveryOrderMapper.save(purchaseDeliveryOrderDO);
        //获取采购订单项列表
//        List<PurchaseOrderProductDO> purchaseOrderProductDOList =  purchaseOrderSupport.getAllPurchaseOrderProductDO(purchaseOrderDO.getId());
        List<PurchaseOrderProductDO> purchaseOrderProductDOList = purchaseOrderDO.getPurchaseOrderProductDOList();
        //声明发货单商品项列表，后面会将保存后的发货单商品项add到发货单中
        List<PurchaseDeliveryOrderProductDO> purchaseDeliveryOrderProductDOList = new ArrayList<>();

        //保存采购发货单商品项
        if(purchaseOrderProductDOList!=null&&purchaseOrderProductDOList.size()>0){
            for(PurchaseOrderProductDO purchaseOrderProductDO : purchaseOrderProductDOList){
                PurchaseDeliveryOrderProductDO purchaseDeliveryOrderProductDO = new PurchaseDeliveryOrderProductDO();
                purchaseDeliveryOrderProductDO.setPurchaseDeliveryOrderId(purchaseDeliveryOrderDO.getId());
                purchaseDeliveryOrderProductDO.setPurchaseOrderProductId(purchaseOrderProductDO.getId());
                purchaseDeliveryOrderProductDO.setProductId(purchaseOrderProductDO.getProductId());
                purchaseDeliveryOrderProductDO.setProductName(purchaseOrderProductDO.getProductName());
                purchaseDeliveryOrderProductDO.setProductSkuId(purchaseOrderProductDO.getProductSkuId());
                purchaseDeliveryOrderProductDO.setProductSnapshot(purchaseOrderProductDO.getProductSnapshot());
                purchaseDeliveryOrderProductDO.setProductCount(purchaseOrderProductDO.getProductCount());
                purchaseDeliveryOrderProductDO.setProductAmount(purchaseOrderProductDO.getProductAmount());
                purchaseDeliveryOrderProductDO.setRealProductId(purchaseOrderProductDO.getProductId());
                purchaseDeliveryOrderProductDO.setRealProductName(purchaseOrderProductDO.getProductName());
                purchaseDeliveryOrderProductDO.setRealProductSkuId(purchaseOrderProductDO.getProductSkuId());
                purchaseDeliveryOrderProductDO.setRealProductSnapshot(purchaseOrderProductDO.getProductSnapshot());
                purchaseDeliveryOrderProductDO.setRealProductCount(purchaseOrderProductDO.getProductCount());
                purchaseDeliveryOrderProductDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                purchaseDeliveryOrderProductDO.setCreateTime(now);
                purchaseDeliveryOrderProductDO.setUpdateTime(now);
                purchaseDeliveryOrderProductDO.setCreateUser(String.valueOf(CommonConstant.SUPER_USER_ID));
                purchaseDeliveryOrderProductDO.setUpdateUser(String.valueOf(CommonConstant.SUPER_USER_ID));
                purchaseDeliveryOrderProductMapper.save(purchaseDeliveryOrderProductDO);
                purchaseDeliveryOrderProductDOList.add(purchaseDeliveryOrderProductDO);
            }
        }
        purchaseDeliveryOrderDO.setPurchaseDeliveryOrderProductDOList(purchaseDeliveryOrderProductDOList);
        return purchaseDeliveryOrderDO;
    }

    /**
     * 生成收料通知单
     * @param purchaseDeliveryOrderDO 收料通知单原单
     * @param autoAllotStatus 分拨情况：0直接生成收货单，1：生成总公司收货单（带分拨单号）,已分拨 ，2：生成分公司收货单（保存由总公司带来的分拨号）
     */
    private PurchaseReceiveOrderDO createReceiveDetail(PurchaseDeliveryOrderDO purchaseDeliveryOrderDO, Integer autoAllotStatus,String autoAllotNo){
        Date now = new Date();
        PurchaseReceiveOrderDO purchaseReceiveOrderDO = new PurchaseReceiveOrderDO();
        purchaseReceiveOrderDO.setPurchaseDeliveryOrderId(purchaseDeliveryOrderDO.getId());
        purchaseReceiveOrderDO.setPurchaseOrderId(purchaseDeliveryOrderDO.getPurchaseOrderId());
        purchaseReceiveOrderDO.setPurchaseReceiveNo(GenerateNoUtil.generatePurchaseReceiveOrderNo(now, purchaseDeliveryOrderDO.getPurchaseOrderId()));
        purchaseReceiveOrderDO.setWarehouseId(purchaseDeliveryOrderDO.getWarehouseId());
        purchaseReceiveOrderDO.setWarehouseSnapshot(purchaseDeliveryOrderDO.getWarehouseSnapshot());
        purchaseReceiveOrderDO.setIsInvoice(purchaseDeliveryOrderDO.getIsInvoice());
        purchaseReceiveOrderDO.setIsNew(purchaseDeliveryOrderDO.getIsNew());
        purchaseReceiveOrderDO.setPurchaseReceiveOrderStatus(PurchaseReceiveOrderStatus.PURCHASE_RECEIVE_ORDER_STATUS_WAIT);
        purchaseReceiveOrderDO.setConfirmTime(null);
        purchaseReceiveOrderDO.setProductSupplierId(purchaseDeliveryOrderDO.getOwnerSupplierId());
        purchaseReceiveOrderDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        //收货单的owner不存，签单后保存
        purchaseReceiveOrderDO.setCreateUser(String.valueOf(CommonConstant.SUPER_USER_ID));
        purchaseReceiveOrderDO.setUpdateUser(String.valueOf(CommonConstant.SUPER_USER_ID));
        purchaseReceiveOrderDO.setCreateTime(now);
        purchaseReceiveOrderDO.setUpdateTime(now);
        purchaseReceiveOrderDO.setWarehouseId(purchaseDeliveryOrderDO.getWarehouseId());
        purchaseReceiveOrderDO.setWarehouseSnapshot(purchaseDeliveryOrderDO.getWarehouseSnapshot());
        if(AutoAllotStatus.AUTO_ALLOT_STATUS_YES.equals(autoAllotStatus)){
            //查询总公司仓库，保存快照，未查到总公司仓库可直接抛出异常
            SubCompany subCompany = companyService.getHeaderCompany().getResult();
            Warehouse warehouse = warehouseService.getWarehouseByCompany(subCompany.getSubCompanyId()).getResult().get(0);
            purchaseReceiveOrderDO.setWarehouseId(warehouse.getWarehouseId());
            purchaseReceiveOrderDO.setWarehouseSnapshot(JSON.toJSONString(warehouse));
            purchaseReceiveOrderDO.setAutoAllotStatus(AutoAllotStatus.AUTO_ALLOT_STATUS_YES);
            purchaseReceiveOrderDO.setAutoAllotNo(GenerateNoUtil.generateAutoAllotStatusOrderNo(now,purchaseDeliveryOrderDO.getPurchaseOrderId()));
        }else if(AutoAllotStatus.AUTO_ALLOT_STATUS_RECEIVE.equals(autoAllotStatus)){
            purchaseReceiveOrderDO.setAutoAllotStatus(AutoAllotStatus.AUTO_ALLOT_STATUS_RECEIVE);
            purchaseReceiveOrderDO.setAutoAllotNo(autoAllotNo);
        }else{
            purchaseReceiveOrderDO.setAutoAllotStatus(AutoAllotStatus.AUTO_ALLOT_STATUS_NO);
        }
        purchaseReceiveOrderMapper.save(purchaseReceiveOrderDO);
        //获取发货订单项列表
//        List<PurchaseDeliveryOrderProductDO> purchaseDeliveryOrderProductDOList =  purchaseOrderSupport.getAllPurchaseDeliveryOrderProductDO(purchaseDeliveryOrderDO.getId());
        List<PurchaseDeliveryOrderProductDO> purchaseDeliveryOrderProductDOList = purchaseDeliveryOrderDO.getPurchaseDeliveryOrderProductDOList();
        //保存采购发货单商品项
        if(purchaseDeliveryOrderProductDOList!=null&&purchaseDeliveryOrderProductDOList.size()>0){
            for(PurchaseDeliveryOrderProductDO purchaseDeliveryOrderProductDO : purchaseDeliveryOrderProductDOList){
                PurchaseReceiveOrderProductDO purchaseReceiveOrderProductDO = new PurchaseReceiveOrderProductDO();
                purchaseReceiveOrderProductDO.setPurchaseDeliveryOrderProductId(purchaseDeliveryOrderProductDO.getId());
                purchaseReceiveOrderProductDO.setPurchaseReceiveOrderId(purchaseReceiveOrderDO.getId());
                purchaseReceiveOrderProductDO.setPurchaseOrderProductId(purchaseDeliveryOrderProductDO.getPurchaseOrderProductId());
                purchaseReceiveOrderProductDO.setProductId(purchaseDeliveryOrderProductDO.getProductId());
                purchaseReceiveOrderProductDO.setProductName(purchaseDeliveryOrderProductDO.getProductName());
                purchaseReceiveOrderProductDO.setProductSkuId(purchaseDeliveryOrderProductDO.getProductSkuId());
                purchaseReceiveOrderProductDO.setProductSnapshot(purchaseDeliveryOrderProductDO.getProductSnapshot());
                purchaseReceiveOrderProductDO.setProductCount(purchaseDeliveryOrderProductDO.getProductCount());
                purchaseReceiveOrderProductDO.setRealProductId(purchaseDeliveryOrderProductDO.getProductId());
                purchaseReceiveOrderProductDO.setRealProductName(purchaseDeliveryOrderProductDO.getProductName());
                purchaseReceiveOrderProductDO.setRealProductSkuId(purchaseDeliveryOrderProductDO.getProductSkuId());
                purchaseReceiveOrderProductDO.setRealProductSnapshot(purchaseDeliveryOrderProductDO.getProductSnapshot());
                purchaseReceiveOrderProductDO.setRealProductCount(purchaseDeliveryOrderProductDO.getProductCount());
                purchaseReceiveOrderProductDO.setIsSrc(CommonConstant.YES);
                purchaseReceiveOrderProductDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                purchaseReceiveOrderProductDO.setCreateTime(now);
                purchaseReceiveOrderProductDO.setUpdateTime(now);
                purchaseReceiveOrderProductDO.setCreateUser(String.valueOf(CommonConstant.SUPER_USER_ID));
                purchaseReceiveOrderProductDO.setUpdateUser(String.valueOf(CommonConstant.SUPER_USER_ID));
                purchaseReceiveOrderProductMapper.save(purchaseReceiveOrderProductDO);
            }
        }
        return purchaseReceiveOrderDO;
    }
}
