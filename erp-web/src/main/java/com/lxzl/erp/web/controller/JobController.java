package com.lxzl.erp.web.controller;

import com.lxzl.erp.common.domain.TaskSchedulerSystemConfig;
import com.lxzl.erp.core.annotation.ControllerLog;
import com.lxzl.erp.core.service.bank.BankSlipService;
import com.lxzl.erp.core.service.user.impl.support.UserSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @Author : XiaoLuYu
 * @Date : Created in 2018/8/6
 * @Time : Created in 17:17
 */
@Controller
@ControllerLog
@RequestMapping("/job")
public class JobController {

    private static Logger logger = LoggerFactory.getLogger(JobController.class);

    @RequestMapping(value = "test",method = RequestMethod.POST)
    public void say(@RequestBody Map<String,Object> map, HttpServletRequest request){
        String appId = request.getHeader("appId");
        String appSecret = request.getHeader("appSecret");
        for (Object objectKey : map.keySet()) {
            Object objectValue = map.get(objectKey);
            System.out.println("参数名："+objectKey);
            System.out.println("参数值："+objectValue);
        }

        if(!TaskSchedulerSystemConfig.taskSchedulerSystemAppId.equals(appId) || !TaskSchedulerSystemConfig.taskSchedulerAppSecret.equals(appSecret)){
            logger.error("配对错误！");
            return;
        }
        System.out.println("appId" + appId);
        System.out.println("appSecret" + appSecret);
        System.out.println("♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥");
        System.out.println("♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥《test测试定时任务打印》♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥");
        System.out.println("♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥");
    }


}
