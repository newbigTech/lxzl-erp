package com.lxzl.erp.web.controller;

import com.lxzl.erp.ERPUnTransactionalTest;
import com.lxzl.erp.TestResult;
import com.lxzl.erp.common.constant.CommonConstant;
import com.lxzl.erp.common.constant.DeploymentType;
import com.lxzl.erp.common.constant.OrderRentType;
import com.lxzl.erp.common.domain.deploymentOrder.ProcessDeploymentOrderParam;
import com.lxzl.erp.common.domain.deploymentOrder.pojo.DeploymentOrder;
import com.lxzl.erp.common.domain.deploymentOrder.pojo.DeploymentOrderProduct;
import com.lxzl.erp.common.domain.order.ProcessOrderParam;
import com.lxzl.erp.common.domain.order.pojo.Order;
import com.lxzl.erp.common.domain.order.pojo.OrderMaterial;
import com.lxzl.erp.common.domain.order.pojo.OrderProduct;
import com.lxzl.erp.common.domain.product.pojo.ProductSkuProperty;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 描述: 调拨单单测试类
 *
 * @author gaochao
 * @date 2017-11-15 14:14
 */
public class DeploymentOrderTest extends ERPUnTransactionalTest {
    @Test
    public void testCreateDeploymentOrder() throws Exception {
        DeploymentOrder deploymentOrder = new DeploymentOrder();
        deploymentOrder.setDeploymentType(DeploymentType.DEPLOYMENT_TYPE_BORROW);
        deploymentOrder.setSrcWarehouseId(4000002);
        deploymentOrder.setTargetWarehouseId(4000001);

        List<DeploymentOrderProduct> deploymentOrderProductList = new ArrayList<>();
        DeploymentOrderProduct deploymentOrderProduct = new DeploymentOrderProduct();
        deploymentOrderProduct.setDeploymentProductSkuId(40);
        deploymentOrderProduct.setDeploymentProductSkuCount(5);
        deploymentOrderProduct.setDeploymentProductUnitAmount(new BigDecimal(20));
        deploymentOrderProductList.add(deploymentOrderProduct);
        deploymentOrder.setDeploymentOrderProductList(deploymentOrderProductList);

        TestResult result = getJsonTestResult("/deploymentOrder/create", deploymentOrder);
    }

    @Test
    public void testCommitDeploymentOrder() throws Exception {
        Order order = new Order();
        order.setOrderNo("O201711301513586691582");
        order.setVerifyUser(500006);
        TestResult result = getJsonTestResult("/deploymentOrder/commit", order);
    }

    @Test
    public void testCancelDeploymentOrder() throws Exception {
        DeploymentOrder deploymentOrder = new DeploymentOrder();
        deploymentOrder.setDeploymentOrderNo("O201711151901080841608");
        deploymentOrder.setVerifyUser(1);
        TestResult result = getJsonTestResult("/deploymentOrder/cancel", deploymentOrder);
    }

    @Test
    public void testProcessDeploymentOrder() throws Exception {
        ProcessDeploymentOrderParam processDeploymentOrderParam = new ProcessDeploymentOrderParam();
        processDeploymentOrderParam.setDeploymentOrderNo("O201711301513586691582");
//        processDeploymentOrderParam.setEquipmentNo("LX-EQUIPMENT-4000001-2017111110114");
        processDeploymentOrderParam.setBulkMaterialNo("BM2017112017070030810053");
        processDeploymentOrderParam.setOperationType(CommonConstant.COMMON_DATA_OPERATION_TYPE_ADD);
        TestResult result = getJsonTestResult("/deploymentOrder/process", processDeploymentOrderParam);
    }


    @Test
    public void testDeliveryDeploymentOrder() throws Exception {
        DeploymentOrder deploymentOrder = new DeploymentOrder();
        deploymentOrder.setDeploymentOrderNo("O201711301513586691582");
        TestResult result = getJsonTestResult("/deploymentOrder/delivery", deploymentOrder);
    }
}
