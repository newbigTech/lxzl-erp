package com.lxzl.erp.core.service.k3.converter.impl;

import com.lxzl.erp.common.domain.supplier.pojo.Supplier;
import com.lxzl.erp.core.k3WebServiceSdk.ERPServer_Models.FormSupply;
import com.lxzl.erp.core.k3WebServiceSdk.ERPServer_Models.ItemNumber;
import com.lxzl.erp.core.service.k3.converter.ConvertK3DataService;
import org.springframework.stereotype.Service;

import java.util.Arrays;


@Service
public class K3SupplierConverter implements ConvertK3DataService {

    @Override
    public Object getK3PostWebServiceData(Object data) {
        Supplier supplier = (Supplier) data;
        FormSupply formSupply = new FormSupply();
        formSupply.setNumber("999232229999");
        formSupply.setName("深圳一二三科技有限公司");
        formSupply.setAddress("地址1123123");
        formSupply.setBank("88888812232");
        formSupply.setAccount("平安银行深圳支行");
        formSupply.setContact("张三");
        formSupply.setFax("0755-12345678");
        formSupply.setMobilePhone("13999992999");
        formSupply.setPhone("0755-456789123");
        formSupply.setNumbers(new ItemNumber[]{new ItemNumber(true, "深圳一二三科技有限公司", "999232229999", "供应商")});

        return formSupply;
    }

}