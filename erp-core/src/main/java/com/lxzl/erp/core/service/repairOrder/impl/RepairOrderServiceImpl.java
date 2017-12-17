package com.lxzl.erp.core.service.repairOrder.impl;

import com.lxzl.erp.common.constant.CommonConstant;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.constant.RepairOrderStatus;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.repairOrder.pojo.RepairOrder;
import com.lxzl.erp.common.domain.repairOrder.pojo.RepairOrderBulkMaterial;
import com.lxzl.erp.common.domain.repairOrder.pojo.RepairOrderEquipment;
import com.lxzl.erp.common.domain.user.pojo.User;
import com.lxzl.erp.common.util.CollectionUtil;
import com.lxzl.erp.common.util.ConverterUtil;
import com.lxzl.erp.common.util.GenerateNoUtil;
import com.lxzl.erp.common.util.ListUtil;
import com.lxzl.erp.core.service.repairOrder.RepairOrderService;
import com.lxzl.erp.core.service.user.impl.support.UserSupport;
import com.lxzl.erp.dataaccess.dao.mysql.material.BulkMaterialMapper;
import com.lxzl.erp.dataaccess.dao.mysql.order.OrderMaterialBulkMapper;
import com.lxzl.erp.dataaccess.dao.mysql.order.OrderProductEquipmentMapper;
import com.lxzl.erp.dataaccess.dao.mysql.product.ProductEquipmentMapper;
import com.lxzl.erp.dataaccess.dao.mysql.repairOrder.RepairOrderBulkMaterialMapper;
import com.lxzl.erp.dataaccess.dao.mysql.repairOrder.RepairOrderEquipmentMapper;
import com.lxzl.erp.dataaccess.dao.mysql.repairOrder.RepairOrderMapper;
import com.lxzl.erp.dataaccess.domain.material.BulkMaterialDO;
import com.lxzl.erp.dataaccess.domain.order.OrderMaterialBulkDO;
import com.lxzl.erp.dataaccess.domain.order.OrderProductDO;
import com.lxzl.erp.dataaccess.domain.order.OrderProductEquipmentDO;
import com.lxzl.erp.dataaccess.domain.product.ProductEquipmentDO;
import com.lxzl.erp.dataaccess.domain.repairOrder.RepairOrderBulkMaterialDO;
import com.lxzl.erp.dataaccess.domain.repairOrder.RepairOrderDO;
import com.lxzl.erp.dataaccess.domain.repairOrder.RepairOrderEquipmentDO;
import com.lxzl.se.common.util.StringUtil;
import org.omg.CORBA.PRIVATE_MEMBER;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: your name
 * @Description：
 * @Date: Created in 15:45 2017/12/14
 * @Modified By:
 */

@Service
public class RepairOrderServiceImpl implements RepairOrderService {

    @Autowired
    private RepairOrderMapper repairOrderMapper;

    @Autowired
    private OrderProductEquipmentMapper orderProductEquipmentMapper;

    @Autowired
    private RepairOrderEquipmentMapper repairOrderEquipmentMapper;

    @Autowired
    private OrderMaterialBulkMapper orderMaterialBulkMapper;

    @Autowired
    private RepairOrderBulkMaterialMapper repairOrderBulkMaterialMapper;

    @Autowired
    private ProductEquipmentMapper productEquipmentMapper;

    @Autowired
    private BulkMaterialMapper bulkMaterialMapper;

    @Autowired
    private UserSupport userSupport;

    private static Logger logger = LoggerFactory.getLogger(RepairOrderServiceImpl.class);


    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public ServiceResult<String, String> addRepairOrder(RepairOrder repairOrder) {
        ServiceResult<String, String> serviceResult = new ServiceResult<>();
        Date now = new Date();

        //先判断是否有设备或者散料需要维修
        if (CollectionUtil.isEmpty(repairOrder.getRepairOrderEquipmentList())
                && CollectionUtil.isEmpty(repairOrder.getRepairOrderBulkMaterialList())) {
            serviceResult.setErrorCode(ErrorCode.EQUIPMENT_AND_BULK_MATERIAL_IS_NOT_NULL);
            return serviceResult;
        }

        //然后生成维修单
        RepairOrderDO repairOrderDO = new RepairOrderDO();
        repairOrderDO.setRepairOrderNo(GenerateNoUtil.generateRepairOrderNo(now));
        repairOrderDO.setRepairReason(repairOrder.getRepairReason());
        repairOrderDO.setRemark(repairOrder.getRemark());
        repairOrderDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        repairOrderDO.setCreateTime(now);
        repairOrderDO.setUpdateTime(now);
        repairOrderDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        repairOrderDO.setCreateUser(userSupport.getCurrentUserId().toString());
        repairOrderMapper.save(repairOrderDO);

        //如果存在设备需要维修
        if (CollectionUtil.isNotEmpty(repairOrder.getRepairOrderEquipmentList())) {
            //遍历集合
            for (RepairOrderEquipment repairOrderEquipment : repairOrder.getRepairOrderEquipmentList()) {
                //获取设备维修单明细表
                RepairOrderEquipmentDO repairOrderEquipmentDO = ConverterUtil.convert(repairOrderEquipment, RepairOrderEquipmentDO.class);

                //获取订单商品设备表,判断设备是否处于租赁状态
                //获取设备
                ProductEquipmentDO productEquipmentDO = productEquipmentMapper.findByEquipmentNo(repairOrderEquipment.getEquipmentNo());
                if (productEquipmentDO == null) {
                    serviceResult.setErrorCode(ErrorCode.EQUIPMENT_NOT_EXISTS);
                    return serviceResult;
                }

                if (StringUtil.isNotEmpty(productEquipmentDO.getOrderNo())) {//设备处于租赁状态
                    OrderProductEquipmentDO orderProductEquipmentDO = orderProductEquipmentMapper.findByEquipmentNo(repairOrderEquipment.getEquipmentNo());
                    repairOrderEquipmentDO.setOrderId(orderProductEquipmentDO.getOrderId());
                    repairOrderEquipmentDO.setOrderProductId(orderProductEquipmentDO.getOrderProductId());
                }

                repairOrderEquipmentDO.setRepairOrderNo(repairOrderDO.getRepairOrderNo());
                repairOrderEquipmentDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                repairOrderEquipmentDO.setCreateTime(now);
                repairOrderEquipmentDO.setUpdateTime(now);
                repairOrderEquipmentDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                repairOrderEquipmentDO.setCreateUser(userSupport.getCurrentUserId().toString());
                repairOrderEquipmentMapper.save(repairOrderEquipmentDO);
            }
        }

        //如果存在散料需要维修
        if (CollectionUtil.isNotEmpty(repairOrder.getRepairOrderBulkMaterialList())) {
            //遍历集合
            for (RepairOrderBulkMaterial repairOrderBulkMaterial : repairOrder.getRepairOrderBulkMaterialList()) {
                //获取散料维修单明细表
                RepairOrderBulkMaterialDO repairOrderBulkMaterialDO = ConverterUtil.convert(repairOrderBulkMaterial, RepairOrderBulkMaterialDO.class);

                //获取订单物料表，判断散料是否处于租赁状态
                //获取物料表
                BulkMaterialDO bulkMaterialDO = bulkMaterialMapper.findByNo(repairOrderBulkMaterial.getBulkMaterialNo());
                if (bulkMaterialDO == null) {
                    serviceResult.setErrorCode(ErrorCode.BULK_MATERIAL_IS_NULL);
                    return serviceResult;
                }

                if (StringUtil.isNotEmpty(bulkMaterialDO.getOrderNo())) {//设备处于租赁状态
                    OrderMaterialBulkDO orderMaterialBulkDO = orderMaterialBulkMapper.findByBulkMaterialNo(repairOrderBulkMaterial.getBulkMaterialNo());
                    repairOrderBulkMaterialDO.setOrderId(orderMaterialBulkDO.getOrderId());
                    repairOrderBulkMaterialDO.setOrderMaterialId(orderMaterialBulkDO.getOrderMaterialId());
                }
                repairOrderBulkMaterialDO.setRepairOrderNo(repairOrderDO.getRepairOrderNo());
                repairOrderBulkMaterialDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                repairOrderBulkMaterialDO.setCreateTime(now);
                repairOrderBulkMaterialDO.setUpdateTime(now);
                repairOrderBulkMaterialDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                repairOrderBulkMaterialDO.setCreateUser(userSupport.getCurrentUserId().toString());
                repairOrderBulkMaterialMapper.save(repairOrderBulkMaterialDO);
            }
        }

        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(repairOrderDO.getRepairOrderNo());
        return serviceResult;
    }


    @Override
    public ServiceResult<String, String> commitRepairOrder(String repairOrderNo, Integer verifyUser, String commitRemark) {
        ServiceResult<String, String> serviceResult = new ServiceResult<>();

        RepairOrderDO repairOrderDO = repairOrderMapper.findByRepairOrderNo(repairOrderNo);
        if (repairOrderDO == null) {
            serviceResult.setErrorCode(ErrorCode.REPAIR_ORDER_IS_NOT_EXISTS);
            return serviceResult;
        }

        //只有初始化维修单才能进行审核
        if (!RepairOrderStatus.REPAIR_ORDER_STATUS_INIT.equals(repairOrderDO.getRepairOrderStatus())) {
            serviceResult.setErrorCode(ErrorCode.REPAIR_ORDER_STATUS_ERROR);
            return serviceResult;
        }
        //todo 审核进行事物流
        repairOrderDO.setRepairOrderStatus(RepairOrderStatus.REPAIR_ORDER_STATUS_VERIFYING);
        repairOrderDO.setUpdateTime(new Date());
        repairOrderDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        repairOrderMapper.update(repairOrderDO);

        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(repairOrderDO.getRepairOrderNo());
        return serviceResult;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean receiveVerifyResult(boolean verifyResult, String repairOrderNo) {
        try {
            RepairOrderDO repairOrderDO = repairOrderMapper.findByRepairOrderNo(repairOrderNo);
            if (repairOrderDO == null || !RepairOrderStatus.REPAIR_ORDER_STATUS_VERIFYING.equals(repairOrderDO.getRepairOrderStatus())) {
                return false;
            }
            if (verifyResult) {
                //审核通过
                repairOrderDO.setRepairOrderStatus(RepairOrderStatus.REPAIR_ORDER_STATUS_WAIT_REPAIR);
            } else {
                repairOrderDO.setRepairOrderStatus(RepairOrderStatus.REPAIR_ORDER_STATUS_INIT);
            }
            repairOrderDO.setUpdateTime(new Date());
            repairOrderDO.setUpdateUser(userSupport.getCurrentUserId().toString());
            repairOrderMapper.update(repairOrderDO);

        } catch (Exception e) {
            logger.error("审批设备维修单通知失败： {}", repairOrderNo);
            return false;
        }
        return false;
    }

    @Override
    public ServiceResult<String, String> receiveRepairOrder(String repairOrderNo) {
        ServiceResult<String, String> serviceResult = new ServiceResult<>();

        RepairOrderDO repairOrderDO = repairOrderMapper.findByRepairOrderNo(repairOrderNo);
        if (repairOrderDO == null) {
            serviceResult.setErrorCode(ErrorCode.REPAIR_ORDER_IS_NOT_EXISTS);
            return serviceResult;
        }

        //只有状态是待维修的维修单才能进行维修
        if (!RepairOrderStatus.REPAIR_ORDER_STATUS_WAIT_REPAIR.equals(repairOrderDO.getRepairOrderStatus())) {
            serviceResult.setErrorCode(ErrorCode.REPAIR_ORDER_STATUS_ERROR);
            return serviceResult;
        }

        repairOrderDO.setRepairOrderStatus(RepairOrderStatus.REPAIR_ORDER_STATUS_REPAIRING);
        repairOrderDO.setUpdateTime(new Date());
        repairOrderDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        repairOrderMapper.update(repairOrderDO);

        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(repairOrderDO.getRepairOrderNo());
        return serviceResult;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public ServiceResult<String, String> cancelRepairOrder(String repairOrderNo) {
        ServiceResult<String, String> serviceResult = new ServiceResult<>();
        RepairOrderDO repairOrderDO = repairOrderMapper.findByRepairOrderNo(repairOrderNo);
        if (repairOrderDO.getRepairOrderStatus() == null || !RepairOrderStatus.REPAIR_ORDER_STATUS_INIT.equals(repairOrderDO.getRepairOrderStatus())) {
            serviceResult.setErrorCode(ErrorCode.REPAIR_ORDER_STATUS_ERROR);
            return serviceResult;
        }
        repairOrderDO.setRepairOrderStatus(RepairOrderStatus.REPAIR_ORDER_STATUS_CANCEL);
        repairOrderDO.setUpdateTime(new Date());
        repairOrderDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        repairOrderMapper.update(repairOrderDO);

        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(repairOrderDO.getRepairOrderNo());
        return serviceResult;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public ServiceResult<String, String> updateRepairOrder(RepairOrder repairOrder) {
        ServiceResult<String, String> serviceResult = new ServiceResult<>();
        Date now = new Date();

        //先判断是否有设备或者散料需要维修
        if (CollectionUtil.isEmpty(repairOrder.getRepairOrderEquipmentList())
                && CollectionUtil.isEmpty(repairOrder.getRepairOrderBulkMaterialList())) {
            serviceResult.setErrorCode(ErrorCode.EQUIPMENT_AND_BULK_MATERIAL_IS_NOT_NULL);
            return serviceResult;
        }

        RepairOrderDO dbrepairOrderDO = repairOrderMapper.findByRepairOrderNo(repairOrder.getRepairOrderNo());
        if (dbrepairOrderDO == null) {
            serviceResult.setErrorCode(ErrorCode.REPAIR_ORDER_IS_NOT_EXISTS);
            return serviceResult;
        }

        //判断修改的订单的状态是否是初始化
        if (!RepairOrderStatus.REPAIR_ORDER_STATUS_INIT.equals(dbrepairOrderDO.getRepairOrderStatus())) {
            serviceResult.setErrorCode(ErrorCode.REPAIR_ORDER_STATUS_ERROR);
            return serviceResult;
        }

//        List<RepairOrderEquipmentDO> repairOrderEquipmentDOList = ConverterUtil.convertList(repairOrder.getRepairOrderEquipmentList(),RepairOrderEquipmentDO.class);
//        List<RepairOrderBulkMaterialDO> repairOrderBulkMaterialDOList = ConverterUtil.convertList(repairOrder.getRepairOrderBulkMaterialList(),RepairOrderBulkMaterialDO.class);

        dbrepairOrderDO = ConverterUtil.convert(repairOrder,RepairOrderDO.class);
        dbrepairOrderDO.setRepairOrderStatus(RepairOrderStatus.REPAIR_ORDER_STATUS_INIT);
        dbrepairOrderDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        dbrepairOrderDO.setUpdateTime(now);
        dbrepairOrderDO.setCreateTime(now);
        dbrepairOrderDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        dbrepairOrderDO.setCreateUser(userSupport.getCurrentUserId().toString());
        repairOrderMapper.update(dbrepairOrderDO);
        //判断设备维修单明细
        //如果传入的设备维修单明细没有值
        if (CollectionUtil.isEmpty(repairOrder.getRepairOrderEquipmentList())) {
            List<RepairOrderEquipmentDO> repairOrderEquipmentDOList = repairOrderEquipmentMapper.findByRepairOrderNo(repairOrder.getRepairOrderNo());
            //并且原设备维修单明细有值
            if (CollectionUtil.isNotEmpty(repairOrderEquipmentDOList)) {
                //清空原设备维修单明细
                repairOrderEquipmentMapper.clearDateStatus(repairOrder.getRepairOrderNo());
            }
        }else{
            //如果传入的设备维修单明细有值
            //并且原设备维修单明细没有值
            List<RepairOrderEquipmentDO> repairOrderEquipmentDOList = repairOrderEquipmentMapper.findByRepairOrderNo(repairOrder.getRepairOrderNo());
            if (CollectionUtil.isEmpty(repairOrderEquipmentDOList)) {
                for (RepairOrderEquipment  repairOrderEquipment : repairOrder.getRepairOrderEquipmentList()){
                    //获取设备维修单明细表
                    RepairOrderEquipmentDO repairOrderEquipmentDO = ConverterUtil.convert(repairOrderEquipment, RepairOrderEquipmentDO.class);

                    //获取订单商品设备表,判断设备是否处于租赁状态
                    //获取设备
                    ProductEquipmentDO productEquipmentDO = productEquipmentMapper.findByEquipmentNo(repairOrderEquipment.getEquipmentNo());
                    if (productEquipmentDO == null) {
                        serviceResult.setErrorCode(ErrorCode.EQUIPMENT_NOT_EXISTS);
                        return serviceResult;
                    }

                    if (StringUtil.isNotEmpty(productEquipmentDO.getOrderNo())) {//设备处于租赁状态
                        OrderProductEquipmentDO orderProductEquipmentDO = orderProductEquipmentMapper.findByEquipmentNo(repairOrderEquipment.getEquipmentNo());
                        repairOrderEquipmentDO.setOrderId(orderProductEquipmentDO.getOrderId());
                        repairOrderEquipmentDO.setOrderProductId(orderProductEquipmentDO.getOrderProductId());
                    }

                    repairOrderEquipmentDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                    repairOrderEquipmentDO.setCreateTime(now);
                    repairOrderEquipmentDO.setUpdateTime(now);
                    repairOrderEquipmentDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                    repairOrderEquipmentDO.setCreateUser(userSupport.getCurrentUserId().toString());
                    repairOrderEquipmentMapper.save(repairOrderEquipmentDO);
                }
            }else{
                //并且原设备维修单明细有值
                if (CollectionUtil.isNotEmpty(repairOrderEquipmentDOList)){

                }
            }

        }

        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(dbrepairOrderDO.getRepairOrderNo());
        return serviceResult;
    }





    private String saveRepairOrderEquipmentInfo(List<RepairOrderEquipmentDO> repairOrderEquipmentDOList, String repairOrderNo, User loginUser, Date currentTime){

        Map<Integer, RepairOrderEquipmentDO> saveRepairOrderEquipmentDOMap = new HashMap<>();
        Map<Integer, RepairOrderEquipmentDO> updateRepairOrderEquipmentDOMap = new HashMap<>();
        List<RepairOrderEquipmentDO> dbRepairOrderEquipmentDOList = repairOrderEquipmentMapper.findByRepairOrderNo(repairOrderNo);
        Map<String, RepairOrderEquipmentDO> dbRepairOrderEquipmentDOMap = ListUtil.listToMap(dbRepairOrderEquipmentDOList,  "equipmentId");

        for (RepairOrderEquipmentDO repairOrderEquipmentDO : repairOrderEquipmentDOList) {
            Integer key = repairOrderEquipmentDO.getEquipmentId();
            //如果原单中有现单中的数据
            if (dbRepairOrderEquipmentDOMap.get(key) != null) {
                //因为更改的是原来那一条数据，所以数据Id必须是一样的。
                repairOrderEquipmentDO.setId(dbRepairOrderEquipmentDOMap.get(key).getId());
                //将原单和现单中都存在的数据，存入此Map
                updateRepairOrderEquipmentDOMap.put(repairOrderEquipmentDO.getEquipmentId(), repairOrderEquipmentDO);
                //将原设备维修单明细中已经存入到updateRepairOrderEquipmentDOMap的数据删除
                dbRepairOrderEquipmentDOMap.remove(key);
            } else {
                //如果原单中没有现单中的数据
                saveRepairOrderEquipmentDOMap.put(repairOrderEquipmentDO.getEquipmentId(), repairOrderEquipmentDO);
            }
        }

        if (saveRepairOrderEquipmentDOMap.size() > 0){
            for (Map.Entry<Integer, RepairOrderEquipmentDO> entry : saveRepairOrderEquipmentDOMap.entrySet()){
                RepairOrderEquipmentDO repairOrderEquipmentDO = entry.getValue();
                //获取订单商品设备表,判断设备是否处于租赁状态
                //获取设备
                ProductEquipmentDO productEquipmentDO = productEquipmentMapper.findByEquipmentNo(repairOrderEquipmentDO.getEquipmentNo());
                if (productEquipmentDO == null) {
                    return (ErrorCode.EQUIPMENT_NOT_EXISTS);
                }

                if (StringUtil.isNotEmpty(productEquipmentDO.getOrderNo())) {//设备处于租赁状态
                    OrderProductEquipmentDO orderProductEquipmentDO = orderProductEquipmentMapper.findByEquipmentNo(repairOrderEquipmentDO.getEquipmentNo());
                    repairOrderEquipmentDO.setOrderId(orderProductEquipmentDO.getOrderId());
                    repairOrderEquipmentDO.setOrderProductId(orderProductEquipmentDO.getOrderProductId());
                }

                repairOrderEquipmentDO.setRepairOrderNo(repairOrderEquipmentDO.getRepairOrderNo());
                repairOrderEquipmentDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                repairOrderEquipmentDO.setCreateUser(loginUser.getUserId().toString());
                repairOrderEquipmentDO.setUpdateUser(loginUser.getUserId().toString());
                repairOrderEquipmentDO.setCreateTime(currentTime);
                repairOrderEquipmentDO.setUpdateTime(currentTime);
                repairOrderEquipmentMapper.save(repairOrderEquipmentDO);
            }
        }

        return null;
    }

}