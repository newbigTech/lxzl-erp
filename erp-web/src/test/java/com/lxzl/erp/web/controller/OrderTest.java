package com.lxzl.erp.web.controller;

import com.lxzl.erp.ERPUnTransactionalTest;
import com.lxzl.erp.TestResult;
import com.lxzl.erp.common.constant.CommonConstant;
import com.lxzl.erp.common.constant.OrderRentType;
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
 * 描述: 订单测试类
 *
 * @author gaochao
 * @date 2017-11-15 14:14
 */
public class OrderTest extends ERPUnTransactionalTest {
    @Test
    public void testCreateOrder() throws Exception {
        Order order = new Order();

        order.setRentType(OrderRentType.RENT_TYPE_DAY);
        order.setRentTimeLength(6);
        order.setPayMode(2);
        order.setLogisticsAmount(new BigDecimal(12));
        order.setBuyerRemark("仔细包装，别弄坏了");

        List<OrderProduct> orderProductList = new ArrayList<>();
        OrderProduct orderProduct = new OrderProduct();
        orderProduct.setProductSkuId(40);
        orderProduct.setProductCount(1);
        orderProduct.setProductUnitAmount(new BigDecimal(20.0));
        orderProduct.setInsuranceAmount(new BigDecimal(15.0));
        orderProductList.add(orderProduct);
        order.setOrderProductList(orderProductList);

        List<OrderMaterial> orderMaterialList = new ArrayList<>();

        OrderMaterial orderMaterial = new OrderMaterial();
        orderMaterial.setMaterialId(5);
        orderMaterial.setMaterialCount(1);
        orderMaterial.setMaterialUnitAmount(new BigDecimal(18.0));
        orderMaterial.setInsuranceAmount(new BigDecimal(15.0));

        orderMaterialList.add(orderMaterial);
        order.setOrderMaterialList(orderMaterialList);

        order.setBuyerCustomerNo("CC201711301106206721011");
        order.setCustomerConsignId(1);
        order.setRentStartTime(new Date());
        TestResult result = getJsonTestResult("/order/create", order);
    }

    @Test
    public void testCommitOrder() throws Exception {
        Order order = new Order();
        order.setOrderNo("O201712041441449431257");
        order.setVerifyUser(500006);
        TestResult result = getJsonTestResult("/order/commit", order);
    }

    @Test
    public void testCancelOrder() throws Exception {
        Order order = new Order();
        order.setOrderNo("O201711151901080841608");
        order.setVerifyUser(1);
        TestResult result = getJsonTestResult("/order/cancel", order);
    }

    @Test
    public void testProcessOrder() throws Exception {
        ProcessOrderParam processOrderParam = new ProcessOrderParam();
        processOrderParam.setOrderNo("O201712041441449431257");
//        processOrderParam.setEquipmentNo("LX-EQUIPMENT-4000001-2017112010034");
        processOrderParam.setBulkMaterialNo("BM2017112017070030810018");
//        processOrderParam.setBulkMaterialNo("BM2017112017070030810080");
        processOrderParam.setOperationType(CommonConstant.COMMON_DATA_OPERATION_TYPE_ADD);
        TestResult result = getJsonTestResult("/order/process", processOrderParam);
    }


    @Test
    public void testDelivery() throws Exception {
        Order order = new Order();
        order.setOrderNo("O201712041441449431257");
        TestResult result = getJsonTestResult("/order/delivery", order);
    }
}
