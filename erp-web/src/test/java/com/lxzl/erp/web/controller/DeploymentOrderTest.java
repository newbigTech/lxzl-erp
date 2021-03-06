package com.lxzl.erp.web.controller;

import com.alibaba.fastjson.JSON;
import com.lxzl.erp.ERPUnTransactionalTest;
import com.lxzl.erp.TestResult;
import com.lxzl.erp.common.constant.CommonConstant;
import com.lxzl.erp.common.constant.DeploymentType;
import com.lxzl.erp.common.domain.deploymentOrder.DeploymentOrderQueryParam;
import com.lxzl.erp.common.domain.deploymentOrder.ProcessDeploymentOrderParam;
import com.lxzl.erp.common.domain.deploymentOrder.pojo.DeploymentOrder;
import com.lxzl.erp.common.domain.deploymentOrder.pojo.DeploymentOrderProduct;
import com.lxzl.erp.common.util.JSONUtil;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
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

        TestResult testResult = getJsonTestResult("/deploymentOrder/create", deploymentOrder);
    }
    @Test
    public void testUpdateJson() throws Exception {
        String json = "{\n" +
                "\t\"deploymentOrderNo\": \"LXC010010201801120035\",\n" +
                "\t\"deploymentType\": \"2\",\n" +
                "\t\"srcWarehouseId\": \"4000001\",\n" +
                "\t\"targetWarehouseId\": \"4000002\",\n" +
                "\t\"expectReturnTime\": null,\n" +
                "\t\"deploymentOrderProductList\": [{\n" +
                "\t\t\"deploymentProductSkuId\": 178,\n" +
                "\t\t\"deploymentProductSkuCount\": 10,\n" +
                "\t\t\"deploymentProductUnitAmount\": 1000,\n" +
                "\t\t\"isNew\": 0\n" +
                "\t}, {\n" +
                "\t\t\"deploymentProductSkuId\": 211,\n" +
                "\t\t\"deploymentProductSkuCount\": \"8\",\n" +
                "\t\t\"deploymentProductUnitAmount\": \"800\",\n" +
                "\t\t\"isNew\": 0\n" +
                "\t}, {\n" +
                "\t\t\"deploymentProductSkuId\": 211,\n" +
                "\t\t\"deploymentProductSkuCount\": \"7\",\n" +
                "\t\t\"deploymentProductUnitAmount\": \"700\",\n" +
                "\t\t\"isNew\": 1\n" +
                "\t}]\n" +
                "}\n";
        DeploymentOrder deploymentOrder = JSON.parseObject(json,DeploymentOrder.class);
        TestResult testResult = getJsonTestResult("/deploymentOrder/update", deploymentOrder);
    }

    @Test
    public void testCreateOrderJson() throws Exception {
        String str = "{\n" +
                "\t\"deploymentOrderNo\": \"LXD-010-010-20180116-0025\",\n" +
                "\t\"deploymentType\": \"1\",\n" +
                "\t\"srcWarehouseId\": \"4000001\",\n" +
                "\t\"targetWarehouseId\": \"4000002\",\n" +
                "\t\"expectReturnTime\": 1516147200000,\n" +
                "\t\"deploymentOrderProductList\": [{\n" +
                "\t\t\"deploymentProductSkuId\": 211,\n" +
                "\t\t\"deploymentProductSkuCount\": 1,\n" +
                "\t\t\"deploymentProductUnitAmount\": 888,\n" +
                "\t\t\"isNew\": 0\n" +
                "\t}, {\n" +
                "\t\t\"deploymentProductSkuId\": 211,\n" +
                "\t\t\"deploymentProductSkuCount\": \"2\",\n" +
                "\t\t\"deploymentProductUnitAmount\": \"777\",\n" +
                "\t\t\"isNew\": 1\n" +
                "\t}]\n" +
                "}\n";
        DeploymentOrder deploymentOrder = JSONUtil.convertJSONToBean(str, DeploymentOrder.class);
        TestResult testResult = getJsonTestResult("/deploymentOrder/create", deploymentOrder);
    }

    @Test
    public void testCommitDeploymentOrder() throws Exception {
        DeploymentOrder deploymentOrder = new DeploymentOrder();
        deploymentOrder.setDeploymentOrderNo("DO201712011137331391559");
        deploymentOrder.setVerifyUser(500006);
        TestResult testResult = getJsonTestResult("/deploymentOrder/commit", deploymentOrder);
    }

    @Test
    public void testCancelDeploymentOrder() throws Exception {
        DeploymentOrder deploymentOrder = new DeploymentOrder();
        deploymentOrder.setDeploymentOrderNo("LXC010010201801080030");
//        deploymentOrder.setVerifyUser(1);
        TestResult testResult = getJsonTestResult("/deploymentOrder/cancel", deploymentOrder);
    }

    @Test
    public void testProcessDeploymentOrder() throws Exception {
        ProcessDeploymentOrderParam processDeploymentOrderParam = new ProcessDeploymentOrderParam();
        processDeploymentOrderParam.setDeploymentOrderNo("LXD-1000-0755-20180116-0030");
//        processDeploymentOrderParam.setEquipmentNo("LX-EQUIPMENT-4000001-2017111110114");
        processDeploymentOrderParam.setMaterialId(11);
        processDeploymentOrderParam.setMaterialCount(1);
        processDeploymentOrderParam.setIsNewMaterial(0);
        processDeploymentOrderParam.setOperationType(CommonConstant.COMMON_DATA_OPERATION_TYPE_ADD);
        TestResult testResult = getJsonTestResult("/deploymentOrder/process", processDeploymentOrderParam);
    }

    @Test
    public void testProcessJson() throws Exception{
        String str = "\n" +
                "{\n" +
                "  \"operationType\": 3,\n" +
                "  \"deploymentOrderNo\": \"DO201712221016337151726\",\n" +
                "  \"materialId\": 23,\n" +
                "  \"materialCount\": \"1\"\n" +
                "}";
        ProcessDeploymentOrderParam processDeploymentOrderParam = JSONUtil.convertJSONToBean(str, ProcessDeploymentOrderParam.class);
        TestResult testResult = getJsonTestResult("/deploymentOrder/process", processDeploymentOrderParam);
    }


    @Test
    public void testDeliveryDeploymentOrder() throws Exception {
        DeploymentOrder deploymentOrder = new DeploymentOrder();
        deploymentOrder.setDeploymentOrderNo("O201711301513586691582");
        TestResult testResult = getJsonTestResult("/deploymentOrder/delivery", deploymentOrder);
    }


    @Test
    public void testConfirmDeploymentOrder() throws Exception {
        DeploymentOrder deploymentOrder = new DeploymentOrder();
        deploymentOrder.setDeploymentOrderNo("LXC010010201801030014");
        TestResult testResult = getJsonTestResult("/deploymentOrder/confirm", deploymentOrder);
    }


    @Test
    public void testQueryPageDeploymentOrder() throws Exception {
        DeploymentOrderQueryParam param = new DeploymentOrderQueryParam();
        param.setPageNo(1);
        param.setPageSize(15);
//        param.setDeploymentOrderNo("DO201712011137331391559");
        TestResult testResult = getJsonTestResult("/deploymentOrder/queryPage", param);
    }

    @Test
    public void testQueryDetailDeploymentOrder() throws Exception {
        DeploymentOrder deploymentOrder = new DeploymentOrder();
        deploymentOrder.setDeploymentOrderNo("DO201712221016337151726");
        TestResult testResult = getJsonTestResult("/deploymentOrder/queryDetail", deploymentOrder);
    }
}
