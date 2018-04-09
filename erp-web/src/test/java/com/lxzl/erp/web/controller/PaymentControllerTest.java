package com.lxzl.erp.web.controller;

import com.alibaba.fastjson.JSON;
import com.lxzl.erp.ERPUnTransactionalTest;
import com.lxzl.erp.TestResult;
import com.lxzl.erp.common.domain.customer.pojo.Customer;
import com.lxzl.erp.common.domain.erpInterface.customer.InterfaceCustomerAccountLogParam;
import com.lxzl.erp.common.domain.payment.ChargeRecordPageParam;
import com.lxzl.erp.common.domain.payment.CustomerAccountLogParam;
import com.lxzl.erp.common.domain.payment.WeixinChargeParam;
import org.junit.Test;

import java.math.BigDecimal;

/**
 * @Author : kai
 * @Date : Created in 2018/1/27
 * @Time : Created in 10:19
 */
public class PaymentControllerTest extends ERPUnTransactionalTest {

    @Test
    public void weixinPay() throws Exception {
        WeixinChargeParam param = new WeixinChargeParam();

        param.setCustomerNo("LXCC-1000-20180124-13746");
        param.setOpenId("o_ORluM1fFEVm9LMePBFvyBzbdr8");
        param.setAmount(new BigDecimal(0.01));
        TestResult testResult = getJsonTestResult("/payment/wechatCharge", param);
    }

    @Test
    public void queryChargeRecordPage() throws Exception {
        Customer customer = new Customer();
        customer.setCustomerNo("LXCC-1000-20180124-13746");
        TestResult testResult = getJsonTestResult("/payment/queryChargeRecordPage", customer);
    }

    @Test
    public void queryChargeRecordParamPage() throws Exception {


        ChargeRecordPageParam param = new ChargeRecordPageParam();
        param.setPageNo(1);
        param.setPageSize(10);
//        param.setBusinessCustomerNo("LXCC10002018010500023");
//        param.setCustomerName("腾讯");
        param.setChargeType(2);
//        param.setChargeBodyId("2");
//        param.setChargeStatus(20);
//        param.setQueryStartTime(new Date("1514908800000"));
//        param.setQueryEndTime(new Date("1517500799999"));
        TestResult testResult = getJsonTestResult("/payment/queryChargeRecordParamPage", param);
    }


    @Test
    public void queryChargeRecordParamPageJson() throws Exception {
        String json = "{\n" +
                "    \"businessAppId\":\"201712140001\",\n" +
                "    \"businessAppSecret\":\"JN2hrJcxZjn6y4XvtdzqXOdy5ouol0FeQA2ZUfuayhcj4sUlqQMezAICJEM36sdg\",\n" +
                "    \"pageNo\":1,\n" +
                "    \"pageSize\":10,\n" +
//                "    \"subCompanyId\":1545646,\n" +
                "    \"customerName\":\"rwearwe\",\n" +
                "    \"chargeType\":2\n" +
                "}";
        ChargeRecordPageParam param = JSON.parseObject(json,ChargeRecordPageParam.class);

        TestResult testResult = getJsonTestResult("/payment/queryChargeRecordParamPage", param);
    }


    @Test
    public void queryCustomerAccountLogPage() throws Exception {
        CustomerAccountLogParam param = new CustomerAccountLogParam();
        param.setBusinessCustomerNo("LXCC-1000-20180213-00128");
        param.setPageNo(1);
        param.setPageSize(15);
//        param.setCustomerAccountLogType(5);
        TestResult testResult = getJsonTestResult("/payment/queryCustomerAccountLogPage", param);
    }

    @Test
    public void weixinQueryCustomerAccountLogPage() throws Exception {
        InterfaceCustomerAccountLogParam param = new InterfaceCustomerAccountLogParam();
        param.setCustomerNo("LXCC-1000-20180213-00128");
        param.setPageNo(1);
        param.setPageSize(10);
//        param.setCustomerAccountLogType(5);
        TestResult testResult = getJsonTestResult("/payment/weixinQueryCustomerAccountLogPage", param);
    }
}
