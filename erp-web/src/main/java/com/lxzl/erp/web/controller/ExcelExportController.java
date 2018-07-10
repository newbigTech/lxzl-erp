package com.lxzl.erp.web.controller;

import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.bank.BankSlipDetailQueryParam;
import com.lxzl.erp.common.domain.bank.pojo.BankSlipDetail;
import com.lxzl.erp.common.domain.dynamicSql.DynamicSqlSelectParam;
import com.lxzl.erp.common.domain.export.FinanceStatementOrderPayDetail;
import com.lxzl.erp.common.domain.statement.StatementOrderDetailQueryParam;
import com.lxzl.erp.common.domain.statement.StatementOrderMonthQueryParam;
import com.lxzl.erp.common.domain.statement.StatementOrderQueryParam;
import com.lxzl.erp.common.domain.statement.pojo.StatementOrder;
import com.lxzl.erp.common.domain.statement.pojo.StatementOrderDetail;
import com.lxzl.erp.common.domain.statistics.StatisticsSalesmanPageParam;
import com.lxzl.erp.common.domain.statistics.pojo.StatisticsSalesman;
import com.lxzl.erp.core.annotation.ControllerLog;
import com.lxzl.erp.core.component.ResultGenerator;
import com.lxzl.erp.core.service.bank.BankSlipService;
import com.lxzl.erp.core.service.dynamicSql.DynamicSqlService;
import com.lxzl.erp.core.service.export.ExportExcelCustomFormatService;
import com.lxzl.erp.core.service.export.ExcelExportConfigGroup;
import com.lxzl.erp.core.service.export.ExcelExportService;
import com.lxzl.erp.core.service.export.impl.support.ExcelExportSupport;
import com.lxzl.erp.core.service.statement.StatementService;
import com.lxzl.erp.core.service.statistics.StatisticsService;
import com.lxzl.se.common.domain.Result;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;

import static com.lxzl.erp.web.util.ResultMapUtils.toLists;

@RequestMapping("/exportExcel")
@Controller
@ControllerLog
public class ExcelExportController {

    @Autowired
    private ResultGenerator resultGenerator;
    @Autowired
    private BankSlipService bankSlipService;
    @Autowired
    private StatementService statementService;
    @Autowired
    private ExcelExportService excelExportService;
    @Autowired
    private StatisticsService statisticsService;
    @Autowired
    private DynamicSqlService dynamicSqlService;
    @Autowired
    private ExportExcelCustomFormatService exportExcelCustomFormatService;
    @RequestMapping(value = "exportPageBankSlipDetail", method = RequestMethod.POST)
    public Result exportPageBankSlip(BankSlipDetailQueryParam bankSlipDetailQueryParam, HttpServletResponse response) throws Exception {
        bankSlipDetailQueryParam.setPayerName(ExcelExportSupport.decode(bankSlipDetailQueryParam.getPayerName()));
        ServiceResult<String, Page<BankSlipDetail>> stringPageServiceResult = bankSlipService.pageBankSlipDetail(bankSlipDetailQueryParam);
        ServiceResult<String, String> serviceResult = excelExportService.export(stringPageServiceResult, ExcelExportConfigGroup.bankSlipDetailConfig, ExcelExportSupport.formatFileName("资金流水记录"), "sheet1", response);
        return resultGenerator.generate(serviceResult.getErrorCode());
    }

    @RequestMapping(value = "exportStatementOrderDetail", method = RequestMethod.POST)
    public Result exportStatementOrderDetail(StatementOrderQueryParam statementOrderQueryParam, HttpServletResponse response) throws Exception {
        ServiceResult<String, StatementOrder> serviceResult = statementService.queryStatementOrderDetail(statementOrderQueryParam.getStatementOrderNo());
        ServiceResult<String, HSSFWorkbook> result = excelExportService.getHSSFWorkbook(serviceResult, ExcelExportConfigGroup.statementOrderConfig, "sheet1");
        List<StatementOrderDetail> statementOrderDetailList = serviceResult.getResult().getStatementOrderDetailList();
        ServiceResult<String, String> serviceResult1 = excelExportService.export(statementOrderDetailList, ExcelExportConfigGroup.statementOrderDetailConfig, response, result.getResult(), ExcelExportSupport.formatFileName("结算单详情"), "sheet1", 2);
        return resultGenerator.generate(serviceResult1.getErrorCode(), serviceResult1.getResult());
    }

    @RequestMapping(value = "exportStatisticsSalesmanDetail", method = RequestMethod.POST)
    public Result exportStatisticsSalesmanDetail(StatisticsSalesmanPageParam statisticsSalesmanPageParam, HttpServletResponse response) throws Exception {

        statisticsSalesmanPageParam.setSalesmanName(ExcelExportSupport.decode(statisticsSalesmanPageParam.getSalesmanName()));
        ServiceResult<String, StatisticsSalesman> result = statisticsService.querySalesman(statisticsSalesmanPageParam);
        ServiceResult<String, String> serviceResult = excelExportService.export(result.getResult().getStatisticsSalesmanDetailPage().getItemList(), ExcelExportConfigGroup.statisticsSalesmanDetailConfig, ExcelExportSupport.formatFileName("销售统计详情"), "sheet1", response);
        return resultGenerator.generate(serviceResult.getErrorCode());
    }

    @RequestMapping(value = "exportPageStatementOrder", method = RequestMethod.POST)
    public Result exportPageStatementOrder(StatementOrderDetailQueryParam statementOrderDetailQueryParam, HttpServletResponse response) throws Exception {

        ServiceResult<String, Page<FinanceStatementOrderPayDetail>> result = statementService.queryFinanceStatementOrderPayDetail(statementOrderDetailQueryParam);
        ServiceResult<String, String> serviceResult = excelExportService.export(result.getResult().getItemList(), ExcelExportConfigGroup.statementOrderPayDetailConfig,ExcelExportSupport.formatFileName("支付明细"), "sheet1", response);
        return resultGenerator.generate(serviceResult.getErrorCode());
    }

    @RequestMapping(value = "exportDynamicSql", method = RequestMethod.POST)
    public Result exportDynamicSql(DynamicSqlSelectParam dynamicSqlSelectParam, HttpServletResponse response) throws Exception {
        String sql  = URLDecoder.decode(dynamicSqlSelectParam.getSql(),"UTF-8");
        dynamicSqlSelectParam.setSql(sql);
        dynamicSqlSelectParam.setLimit(Integer.MAX_VALUE);
        ServiceResult<String, List<Map>> result = dynamicSqlService.executeBySql(dynamicSqlSelectParam);
        ServiceResult<String, List<List<Object>>> serviceResult = new ServiceResult<>();
        serviceResult.setErrorCode(result.getErrorCode());
        if (serviceResult.getErrorCode().equals(ErrorCode.SUCCESS)) {
            List<Map> maps = result.getResult();
            List<List<Object>> results = toLists(maps);
            return resultGenerator.generate(ExcelExportSupport.export(results,ExcelExportSupport.formatFileName("动态sql"),"sheet1" , response,5000).getErrorCode());
        }
        return resultGenerator.generate(serviceResult.getErrorCode());
    }

    @RequestMapping(value = "exportStatementOrderCheck", method = RequestMethod.POST)
    public Result exportDynamicSql(StatementOrderMonthQueryParam statementOrderMonthQueryParam, HttpServletResponse response) throws Exception {
        ServiceResult<String, String> serviceResult = exportExcelCustomFormatService.queryStatementOrderCheckParam(statementOrderMonthQueryParam, response);
        return resultGenerator.generate(serviceResult.getErrorCode());
    }

}
