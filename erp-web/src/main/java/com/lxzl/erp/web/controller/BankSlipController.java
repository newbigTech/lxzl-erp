package com.lxzl.erp.web.controller;

import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.bank.BankSlipDetailQueryParam;
import com.lxzl.erp.common.domain.bank.BankSlipQueryParam;
import com.lxzl.erp.common.domain.bank.pojo.BankSlip;
import com.lxzl.erp.common.domain.bank.pojo.BankSlipDetail;
import com.lxzl.erp.core.annotation.ControllerLog;
import com.lxzl.erp.core.component.ResultGenerator;
import com.lxzl.erp.core.service.bank.BankSlipDetailService;
import com.lxzl.erp.core.service.bank.BankSlipService;
import com.lxzl.se.common.domain.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @Author: your name
 * @Description：
 * @Date: Created in 19:51 2018/3/20
 * @Modified By:
 */
@RequestMapping(value = "bankSlip")
@Controller
@ControllerLog
public class BankSlipController  {

    @Autowired
    private BankSlipService bankSlipService;

    @Autowired
    private ResultGenerator resultGenerator;

    @Autowired
    private BankSlipDetailService bankSlipDetailService;

    @RequestMapping(value = "pageBankSlip", method = RequestMethod.POST)
    public Result pageBankSlip(@RequestBody BankSlipQueryParam bankSlipQueryParam, BindingResult validResult) {
        ServiceResult<String, Page<BankSlip>> serviceResult = bankSlipService.pageBankSlip(bankSlipQueryParam);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "pageBankSlipDetail", method = RequestMethod.POST)
    public Result pageBankSlipDetail(@RequestBody BankSlipDetailQueryParam bankSlipDetailQueryParam, BindingResult validResult) {
        ServiceResult<String, Page<BankSlipDetail>> serviceResult = bankSlipDetailService.pageBankSlipDetail(bankSlipDetailQueryParam);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }


}
