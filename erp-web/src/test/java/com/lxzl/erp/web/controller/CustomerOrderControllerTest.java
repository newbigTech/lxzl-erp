package com.lxzl.erp.web.controller;

import com.lxzl.erp.ERPUnTransactionalTest;
import com.lxzl.erp.TestResult;
import com.lxzl.erp.common.domain.returnOrder.RentMaterialCanProcessPageParam;
import com.lxzl.erp.common.domain.returnOrder.RentProductSkuPageParam;
import org.junit.Test;

public class CustomerOrderControllerTest extends ERPUnTransactionalTest{
    @Test
    public void pageRentProductSku() throws Exception {
        RentProductSkuPageParam rentProductSkuPageParam = new RentProductSkuPageParam();
        rentProductSkuPageParam.setCustomerNo("CC201712291147092641899");
        TestResult testResult = getJsonTestResult("/customerOrder/pageRentProduct",rentProductSkuPageParam);
    }

    @Test
    public void pageRentMaterialCanReturn() throws Exception {
        RentMaterialCanProcessPageParam rentMaterialCanProcessPageParam = new RentMaterialCanProcessPageParam();
        rentMaterialCanProcessPageParam.setCustomerNo("LXCC10002018010500022");
        TestResult testResult = getJsonTestResult("/customerOrder/pageRentMaterialCanReturn",rentMaterialCanProcessPageParam);
    }

    @Test
    public void pageRentMaterialCanChange() throws Exception {
        RentMaterialCanProcessPageParam rentMaterialCanProcessPageParam = new RentMaterialCanProcessPageParam();
        rentMaterialCanProcessPageParam.setCustomerNo("CC201711301106206721011");
        TestResult testResult = getJsonTestResult("/customerOrder/pageRentMaterialCanReturn",rentMaterialCanProcessPageParam);
    }

}