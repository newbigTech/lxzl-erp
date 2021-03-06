package com.lxzl.erp.web.controller;

import com.lxzl.erp.ERPUnTransactionalTest;
import com.lxzl.erp.TestResult;
import com.lxzl.erp.common.domain.interfaceSwitch.SwitchQueryParam;
import com.lxzl.erp.common.domain.interfaceSwitch.pojo.Switch;
import com.lxzl.erp.common.util.JSONUtil;
import org.junit.Test;

/**
 * @Author : XiaoLuYu
 * @Date : Created in 2018/4/4
 * @Time : Created in 17:11
 */
public class InterfaceSwitchControllerTest extends ERPUnTransactionalTest{
    @Test
    public void add() throws Exception {

        Switch interfaceSwitch = new Switch();
        interfaceSwitch.setInterfaceUrl("   switch/page122/   ");
        interfaceSwitch.setRemark("新添加的跟新备注");
        TestResult jsonTestResult = getJsonTestResult("/interfaceSwitch/add",interfaceSwitch);
    }

    @Test
    public void update() throws Exception {
        Switch interfaceSwitch = new Switch();
        interfaceSwitch.setSwitchId(500004);
        interfaceSwitch.setInterfaceUrl("/switch/page1");
        interfaceSwitch.setIsOpen(1);
        interfaceSwitch.setRemark("跟新备注!");
        TestResult jsonTestResult = getJsonTestResult("/interfaceSwitch/update",interfaceSwitch);

    }

    @Test
    public void page() throws Exception {
        SwitchQueryParam switchQueryParam = new SwitchQueryParam();
//        switchQueryParam.setIsOpen(1);
        switchQueryParam.setInterfaceUrl("/switch");
//        switchQueryParam.setCreateEndTime(new SimpleDateFormat("yyyy-MM-dd").parse("2018-5-4"));
//        switchQueryParam.setCreateStartTime(new SimpleDateFormat("yyyy-MM-dd").parse("2018-3-5"));
        TestResult jsonTestResult = getJsonTestResult("/erp/interfaceSwitch/page",switchQueryParam);
        String beanToJSON = JSONUtil.convertBeanToJSON(jsonTestResult);
        System.out.println(beanToJSON.toString());
    }

    @Test
    public void delete() throws Exception {
        Switch interfaceSwitch = new Switch();
        interfaceSwitch.setSwitchId(500005);
        interfaceSwitch.setRemark("删除备注!");
        TestResult jsonTestResult = getJsonTestResult("/interfaceSwitch/delete",interfaceSwitch);

    }

}