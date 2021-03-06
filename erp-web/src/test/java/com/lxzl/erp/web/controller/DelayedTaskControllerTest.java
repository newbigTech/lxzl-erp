package com.lxzl.erp.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.lxzl.erp.ERPUnTransactionalTest;
import com.lxzl.erp.TestResult;
import com.lxzl.erp.common.domain.delayedTask.DelayedTaskQueryParam;
import com.lxzl.erp.common.domain.delayedTask.pojo.DelayedTask;
import com.lxzl.erp.common.domain.replace.ReplaceOrderCommitParam;
import com.lxzl.erp.common.util.FastJsonUtil;
import com.lxzl.erp.common.util.JSONUtil;
import org.junit.Test;

import java.util.Date;

/**
 * @Author: Sunzhipeng
 * @Description:
 * @Date: Created in 2018\7\28 0028 11:05
 */
public class DelayedTaskControllerTest extends ERPUnTransactionalTest {
    @Test
    public void addDelayedTask() throws Exception{
        DelayedTask delayedTask = new DelayedTask();
        delayedTask.setTaskType(1);
        String requestString = "{\"statementOrderCustomerNo\": \"LXCC-2000-20180212-02993\", \"statementOrderStartTime\": 1533052800000,\"statementOrderEndTime\": 1535731200000}";
        delayedTask.setRequestJson(requestString);
        String json = FastJsonUtil.toJSONString(delayedTask);
        System.out.println(json);

//        for(int i = 0  ;i <= 40 ; i++){
            TestResult testResult = getJsonTestResult("/delayedTask/addDelayedTask",delayedTask);
//        }
        Thread.sleep(2000000000);
    }

    @Test
    public void pageDelayedTask() throws Exception {
        DelayedTaskQueryParam delayedTaskQueryParam = new DelayedTaskQueryParam();
        delayedTaskQueryParam.setPageNo(1);
        delayedTaskQueryParam.setPageSize(1);
        delayedTaskQueryParam.setTaskType(1);
        delayedTaskQueryParam.setTaskStatus(3);
        delayedTaskQueryParam.setCreateUserName("张婷");
        TestResult testResult = getJsonTestResult("/delayedTask/pageDelayedTask", delayedTaskQueryParam);
    }

    @Test
    public void addDelayedTaskJson() throws Exception{
        String str = "{\"taskType\":1,\"requestJson\":{\"statementOrderCustomerNo\":\"LXCC-027-20180929-00135\",\"statementOrderStartTime\":1514764800000,\"statementOrderEndTime\":1535760000000}}";

        DelayedTask delayedTask = FastJsonUtil.toBean(str, DelayedTask.class);

//        for(int i = 0  ;i <= 40 ; i++){
        TestResult testResult = getJsonTestResult("/delayedTask/addDelayedTask",delayedTask);
//        }
        Thread.sleep(2000000000);
    }
}
