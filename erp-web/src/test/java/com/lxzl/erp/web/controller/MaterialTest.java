package com.lxzl.erp.web.controller;

import com.lxzl.erp.ERPTransactionalTest;
import com.lxzl.erp.ERPUnTransactionalTest;
import com.lxzl.erp.TestResult;
import com.lxzl.erp.common.constant.CommonConstant;
import com.lxzl.erp.common.domain.material.BulkMaterialQueryParam;
import com.lxzl.erp.common.domain.material.MaterialModelQueryParam;
import com.lxzl.erp.common.domain.material.MaterialQueryParam;
import com.lxzl.erp.common.domain.material.MaterialTypeQueryParam;
import com.lxzl.erp.common.domain.material.pojo.Material;
import com.lxzl.erp.common.domain.material.pojo.MaterialImg;
import com.lxzl.erp.common.domain.material.pojo.MaterialModel;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2017-11-13 15:30
 */
public class MaterialTest extends ERPUnTransactionalTest {

    @Test
    public void addMaterial() throws Exception {
        Material material = new Material();
        material.setMaterialName("固态/1T SSD");
        material.setMaterialType(8);
        material.setMaterialCapacityValue(1000.0);
        material.setIsRent(CommonConstant.YES);
        material.setMaterialPrice(new BigDecimal(5000.0));
        material.setDayRentPrice(new BigDecimal(5000.0));
        material.setMonthRentPrice(new BigDecimal(5000.0));
        material.setMaterialDesc("测试备注");
        material.setNewMaterialPrice(new BigDecimal("12.00"));
        material.setNewDayRentPrice(new BigDecimal("13.00"));
        material.setNewMonthRentPrice(new BigDecimal("14.00"));
        material.setIsReturnAnyTime(1);
        material.setK3MaterialNo("20.3333");

        List<MaterialImg> materialImgList = new ArrayList<>();
        MaterialImg materialImg = new MaterialImg();
        materialImg.setMaterialImgId(1);
        materialImgList.add(materialImg);
        material.setMaterialImgList(materialImgList);
        TestResult testResult = getJsonTestResult("/material/add", material);
    }

    @Test
    public void updateMaterial() throws Exception {
        Material material = new Material();
        material.setMaterialNo("LX-XX-20180313-00005");
        material.setMaterialDesc("M201711201356145971009");
        material.setNewMaterialPrice(new BigDecimal("22.00"));
        material.setNewDayRentPrice(new BigDecimal("23.00"));
        material.setNewMonthRentPrice(new BigDecimal("24.00"));
        material.setK3MaterialNo("20.1111");
        TestResult testResult = getJsonTestResult("/material/update", material);
    }

    @Test
    public void deleteMaterial() throws Exception {
        Material material = new Material();
        material.setMaterialNo("LXM--20180118-00002");
        TestResult testResult = getJsonTestResult("/material/delete", material);
    }

    @Test
    public void queryAllMaterial() throws Exception {
        MaterialQueryParam materialQueryParam = new MaterialQueryParam();
        materialQueryParam.setPageNo(1);
        materialQueryParam.setPageSize(15);
//        materialQueryParam.setMaterialNo("LXM--20180118-00002");
        TestResult testResult = getJsonTestResult("/material/queryAllMaterial", materialQueryParam);
    }

    @Test
    public void queryBulkMaterialByMaterialId() throws Exception {
        BulkMaterialQueryParam materialQueryParam = new BulkMaterialQueryParam();
        materialQueryParam.setPageNo(1);
        materialQueryParam.setPageSize(15);
        materialQueryParam.setMaterialId(2);
        TestResult testResult = getJsonTestResult("/material/queryBulkMaterialByMaterialId", materialQueryParam);
    }

    @Test
    public void addMaterialModel() throws Exception {
        MaterialModel materialModel = new MaterialModel();
        materialModel.setMaterialModelId(11);
        materialModel.setMaterialType(2);
        materialModel.setModelName("水冷机箱888");
        TestResult testResult = getJsonTestResult("/material/addModel", materialModel);
    }

    @Test
    public void queryMaterialByNo() throws Exception {
        MaterialQueryParam materialQueryParam = new MaterialQueryParam();
        materialQueryParam.setMaterialNo("LX--20180224-00106");
        TestResult testResult = getJsonTestResult("/material/queryMaterialByNo", materialQueryParam);
    }

    @Test
    public void updateMaterialModel() throws Exception {
        MaterialModel materialModel = new MaterialModel();
        materialModel.setMaterialModelId(11);
        materialModel.setModelName("水冷机箱1");
        TestResult testResult = getJsonTestResult("/material/updateModel", materialModel);
    }

    @Test
    public void deleteModel() throws Exception {
        MaterialModel materialModel = new MaterialModel();
        materialModel.setMaterialModelId(13);
        materialModel.setModelName("水冷机箱1");
        TestResult testResult = getJsonTestResult("/material/deleteModel", materialModel);
    }

    @Test
    public void queryModel() throws Exception {
        MaterialModelQueryParam materialModelQueryParam = new MaterialModelQueryParam();
        materialModelQueryParam.setMaterialType(2);
        TestResult testResult = getJsonTestResult("/material/queryModel", materialModelQueryParam);
    }

    @Test
    public void queryModelById() throws Exception {
        MaterialModelQueryParam materialModelQueryParam = new MaterialModelQueryParam();
        materialModelQueryParam.setMaterialModelId(2);
        TestResult testResult = getJsonTestResult("/material/queryModelById", materialModelQueryParam);
    }

    @Test
    public void queryAllBulkMaterial() throws Exception {
        BulkMaterialQueryParam bulkMaterialQueryParam = new BulkMaterialQueryParam();
        TestResult testResult = getJsonTestResult("/material/queryAllBulkMaterial", bulkMaterialQueryParam);
    }

    @Test
    public void queryType() throws Exception {
        MaterialTypeQueryParam materialTypeQueryParam = new MaterialTypeQueryParam();
        materialTypeQueryParam.setIsMainMaterial(0);
        TestResult testResult = getJsonTestResult("/material/queryType", materialTypeQueryParam);
    }
}
