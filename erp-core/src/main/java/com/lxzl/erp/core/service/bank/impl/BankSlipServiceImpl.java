package com.lxzl.erp.core.service.bank.impl;

import com.lxzl.erp.common.constant.*;
import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.bank.BankSlipDetailQueryParam;
import com.lxzl.erp.common.domain.bank.BankSlipQueryParam;
import com.lxzl.erp.common.domain.bank.ClaimParam;
import com.lxzl.erp.common.domain.bank.pojo.BankSlip;
import com.lxzl.erp.common.domain.bank.pojo.BankSlipClaim;
import com.lxzl.erp.common.domain.bank.pojo.BankSlipDetail;
import com.lxzl.erp.common.util.BigDecimalUtil;
import com.lxzl.erp.common.util.CollectionUtil;
import com.lxzl.erp.common.util.ConverterUtil;
import com.lxzl.erp.common.util.FileUtil;
import com.lxzl.erp.core.service.bank.BankSlipService;
import com.lxzl.erp.core.service.bank.impl.importSlip.*;
import com.lxzl.erp.core.service.order.impl.OrderServiceImpl;
import com.lxzl.erp.core.service.user.impl.support.UserSupport;
import com.lxzl.erp.dataaccess.dao.mysql.bank.BankSlipClaimMapper;
import com.lxzl.erp.dataaccess.dao.mysql.bank.BankSlipDetailMapper;
import com.lxzl.erp.dataaccess.dao.mysql.bank.BankSlipMapper;
import com.lxzl.erp.dataaccess.dao.mysql.customer.CustomerMapper;
import com.lxzl.erp.dataaccess.domain.bank.BankSlipClaimDO;
import com.lxzl.erp.dataaccess.domain.bank.BankSlipDO;
import com.lxzl.erp.dataaccess.domain.bank.BankSlipDetailDO;
import com.lxzl.erp.dataaccess.domain.customer.CustomerDO;
import com.lxzl.se.dataaccess.mysql.config.PageQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: pengbinjie
 * @Description：
 * @Date: Created in 19:49 2018/3/20
 * @Modified By:
 */
@Service
public class BankSlipServiceImpl implements BankSlipService {
    private static Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);
    @Autowired
    ImportTrafficBank importTrafficBank;

    @Autowired
    ImportChinaBank importChinaBank;

    @Autowired
    ImportNanJingBank importNanJingBank;

    @Autowired
    ImportAgricultureBank importAgricultureBank;

    @Autowired
    ImportICBCBank importICBCBank;

    @Autowired
    ImportCCBBank importCCBBank;

    @Autowired
    ImportPingAnBank importPingAnBank;

    @Autowired
    ImportShangHaiPuDongDevelopmentBank importShanghaiPudongDevelopmentBank;

    @Autowired
    ImportAlipay importAlipay;

    @Autowired
    ImportCMBCBank importCMBCBank;

    @Autowired
    private BankSlipMapper bankSlipMapper;

    @Autowired
    private UserSupport userSupport;

    @Autowired
    private BankSlipDetailMapper bankSlipDetailMapper;

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private BankSlipClaimMapper bankSlipClaimMapper;

    @Override
    public ServiceResult<String, Page<BankSlip>> pageBankSlip(BankSlipQueryParam bankSlipQueryParam) {
        ServiceResult<String, Page<BankSlip>> result = new ServiceResult<>();
        PageQuery pageQuery = new PageQuery(bankSlipQueryParam.getPageNo(), bankSlipQueryParam.getPageSize());

        Integer departmentType = 0;
        if (userSupport.isFinancePerson()) {
            //财务人员类型设置为1
            departmentType = 1;
        } else if (userSupport.isBusinessAffairsPerson() || userSupport.isBusinessPerson()) {
            //商务和业务员类型设置为2
            departmentType = 2;
        }
        Map<String, Object> maps = new HashMap<>();
        maps.put("start", pageQuery.getStart());
        maps.put("pageSize", pageQuery.getPageSize());
        maps.put("bankSlipQueryParam", bankSlipQueryParam);
        maps.put("departmentType", departmentType);
        Integer totalCount = bankSlipMapper.findBankSlipCountByParams(maps);
        List<BankSlipDO> bankSlipDOList = bankSlipMapper.findBankSlipByParams(maps);

        List<BankSlip> bankSlipList = ConverterUtil.convertList(bankSlipDOList, BankSlip.class);
        Page<BankSlip> page = new Page<>(bankSlipList, totalCount, bankSlipQueryParam.getPageNo(), bankSlipQueryParam.getPageSize());

        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(page);
        return result;
    }

    @Override
    public ServiceResult<String, String> saveBankSlip(BankSlip bankSlip) throws Exception {
        ServiceResult<String, String> serviceResult = new ServiceResult<>();
        Integer bankType = bankSlip.getBankType();
        if (!BankType.BOC_BANK.equals(bankType) &&
                !BankType.TRAFFIC_BANK.equals(bankType) &&
                !BankType.NAN_JING_BANK.equals(bankType) &&
                !BankType.AGRICULTURE_BANK.equals(bankType) &&
                !BankType.ICBC_BANK.equals(bankType) &&
                !BankType.CCB_BANK.equals(bankType) &&
                !BankType.PING_AN_BANK.equals(bankType) &&
                !BankType.CMBC_BANK.equals(bankType) &&
                !BankType.SHANGHAI_PUDONG_DEVELOPMENT_BANK.equals(bankType) &&
                !BankType.ALIPAY.equals(bankType)) {
            serviceResult.setErrorCode(ErrorCode.BANK_TYPE_IS_FAIL);
            return serviceResult;
        }
        //分公司一个月不通银行只能导入一次

        BankSlipDO bankSlipDO = bankSlipMapper.findBySubCompanyIdAndMonthAndBankType(bankSlip.getSubCompanyName(), bankSlip.getSlipMonth(), bankType);
        if (bankSlipDO != null) {
            serviceResult.setErrorCode(ErrorCode.BANK_SLIP_EXISTS);
            return serviceResult;
        }


        String excelUrl = bankSlip.getExcelUrl();
        InputStream inputStream = FileUtil.getFileInputStream(excelUrl);

        if (inputStream == null) {
            serviceResult.setErrorCode(ErrorCode.EXCEL_SHEET_IS_NULL);
            return serviceResult;
        }


        if (BankType.BOC_BANK.equals(bankType)) {
            serviceResult = importChinaBank.saveChinaBank(bankSlip, inputStream);
        } else if (BankType.TRAFFIC_BANK.equals(bankType)) {
            serviceResult = importTrafficBank.saveTrafficBank(bankSlip, inputStream);
        } else if (BankType.NAN_JING_BANK.equals(bankType)) {
            serviceResult = importNanJingBank.saveNanJingBank(bankSlip, inputStream);
        } else if (BankType.AGRICULTURE_BANK.equals(bankType)) {
            serviceResult = importAgricultureBank.saveAgricultureBank(bankSlip, inputStream);
        } else if (BankType.ICBC_BANK.equals(bankType)) {
            serviceResult = importICBCBank.saveICBCBank(bankSlip, inputStream);
        } else if (BankType.CCB_BANK.equals(bankType)) {
            serviceResult = importCCBBank.saveCCBBank(bankSlip, inputStream);
        } else if (BankType.PING_AN_BANK.equals(bankType)) {
            serviceResult = importPingAnBank.savePingAnBank(bankSlip, inputStream);
        } else if (BankType.CMBC_BANK.equals(bankType)) {
            serviceResult = importCMBCBank.saveCMBCBank(bankSlip, inputStream);
        } else if (BankType.SHANGHAI_PUDONG_DEVELOPMENT_BANK.equals(bankType)) {
            serviceResult = importShanghaiPudongDevelopmentBank.saveShanghaiPudongDevelopmentBank(bankSlip, inputStream);
        } else if (BankType.ALIPAY.equals(bankType)) {
            serviceResult = importAlipay.saveAlipay(bankSlip, inputStream);
        }
        return serviceResult;
    }

    @Override
    public ServiceResult<String, Page<BankSlipDetail>> pageBankSlipDetail(BankSlipDetailQueryParam bankSlipDetailQueryParam) {
        ServiceResult<String, Page<BankSlipDetail>> result = new ServiceResult<>();
        PageQuery pageQuery = new PageQuery(bankSlipDetailQueryParam.getPageNo(), bankSlipDetailQueryParam.getPageSize());

        Map<String, Object> maps = new HashMap<>();
        maps.put("start", pageQuery.getStart());
        maps.put("pageSize", pageQuery.getPageSize());
        maps.put("bankSlipDetailQueryParam", bankSlipDetailQueryParam);

        Integer totalCount = bankSlipDetailMapper.findBankSlipDetailDOCountByParams(maps);
        List<BankSlipDetailDO> bankSlipDetailDOList = bankSlipDetailMapper.findBankSlipDetailDOByParams(maps);


        List<BankSlipDetail> bankSlipDetailList = ConverterUtil.convertList(bankSlipDetailDOList, BankSlipDetail.class);
        Page<BankSlipDetail> page = new Page<>(bankSlipDetailList, totalCount, bankSlipDetailQueryParam.getPageNo(), bankSlipDetailQueryParam.getPageSize());

        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(page);
        return result;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public ServiceResult<String, Integer> pushDownBankSlip(BankSlip bankSlip) {
        ServiceResult<String, Integer> serviceResult = new ServiceResult<>();
        Date now = new Date();
        //是否有权下推
        if (!userSupport.isFinancePerson()) {
            serviceResult.setErrorCode(ErrorCode.DATA_HAVE_NO_PERMISSION);
            return serviceResult;
        }
        //银行对公流水是否存在
        BankSlipDO bankSlipDO = bankSlipMapper.findById(bankSlip.getBankSlipId());
        if (bankSlipDO == null) {
            serviceResult.setErrorCode(ErrorCode.BANK_SLIP_NOT_EXISTS);
            return serviceResult;
        }
        //判断状态是否是初始化
        if (!SlipStatus.INITIALIZE.equals(bankSlipDO.getSlipStatus())) {
            serviceResult.setErrorCode(ErrorCode.BANK_SLIP_STATUS_NOT_INITIALIZE);
            return serviceResult;
        }
        bankSlipDO.setSlipStatus(SlipStatus.ALREADY_PUSH_DOWN);
        bankSlipDO.setUpdateTime(now);
        bankSlipDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        bankSlipMapper.update(bankSlipDO);
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(bankSlip.getBankSlipId());
        return serviceResult;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public ServiceResult<String, Integer> ignoreBankSlipDetail(BankSlipDetail bankSlipDetail) {

        Date now = new Date();
        ServiceResult<String, Integer> serviceResult = new ServiceResult<>();
        //银行对公流水项是否存在
        BankSlipDetailDO bankSlipDetailDO = bankSlipDetailMapper.findById(bankSlipDetail.getBankSlipDetailId());
        if (bankSlipDetailDO == null) {
            serviceResult.setErrorCode(ErrorCode.BANK_SLIP_DETAIL_ID_NULL);
            return serviceResult;
        }

        //状态是否为未认领
        if (!BankSlipDetailStatus.UN_CLAIMED.equals(bankSlipDetailDO.getDetailStatus())) {
            serviceResult.setErrorCode(ErrorCode.BANK_SLIP_DETAIL_STATUS_NOT_UN_CLAIMED);
            return serviceResult;
        }
        //明细状态改变为忽略
        bankSlipDetailDO.setDetailStatus(BankSlipDetailStatus.IGNORE);
        bankSlipDetailDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        bankSlipDetailDO.setUpdateTime(now);
        bankSlipDetailMapper.update(bankSlipDetailDO);

        BankSlipDO bankSlipDO = bankSlipMapper.findById(bankSlipDetailDO.getBankSlipId());
        //总表需认领数量-1
        int newNeedClaimCount = bankSlipDO.getNeedClaimCount() - 1;
        if (newNeedClaimCount == 0) {
            bankSlipDO.setSlipStatus(SlipStatus.ALL_CLAIM);
        }

        bankSlipDO.setNeedClaimCount(newNeedClaimCount);
        bankSlipMapper.update(bankSlipDO);
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(bankSlipDetail.getBankSlipDetailId());
        return serviceResult;
    }


    @Override
    public ServiceResult<String, Integer> claimBankSlipDetail(BankSlipClaim bankSlipClaim) {
        ServiceResult<String, Integer> serviceResult = new ServiceResult<>();
        Date now = new Date();

        //判断是否有银行对公流水项
        BankSlipDetailDO bankSlipDetailDO = bankSlipDetailMapper.findById(bankSlipClaim.getBankSlipClaimId());
        if (bankSlipDetailDO == null) {
            serviceResult.setErrorCode(ErrorCode.BANK_SLIP_DETAIL_IS_NULL);
            return serviceResult;
        }
        //判断银行对公流水项状态(已确认)
        if (BankSlipDetailStatus.CONFIRMED.equals(bankSlipDetailDO.getDetailStatus())) {
            serviceResult.setErrorCode(ErrorCode.BANK_SLIP_DETAIL_STATUS_IS_CONFIRMED);
            return serviceResult;
        }

        //判断客户是否存在
        BigDecimal allClaimAmount = new BigDecimal(0);
        List<ClaimParam> claimParamList = bankSlipClaim.getClaimParam();
        for (ClaimParam claimParam : claimParamList) {
            CustomerDO customerDO = customerMapper.findByNo(claimParam.getCustomerNo());
            if (customerDO == null) {
                serviceResult.setErrorCode(ErrorCode.CUSTOMER_NOT_EXISTS);
                return serviceResult;
            }
            allClaimAmount = BigDecimalUtil.add(allClaimAmount, claimParam.getClaimAmount());
        }

        //判断总共金额是否相等
        if (BigDecimalUtil.compare(allClaimAmount, bankSlipDetailDO.getTradeAmount()) != 0) {
            serviceResult.setErrorCode(ErrorCode.BANK_SLIP_DETAIL_TRADE_AMOUNT_UNEQUAL_CURRENT_AGGREGATE_AMOUNT);
            return serviceResult;
        }

        //todo 这里是保存银行对公流水认领表 数据
        ServiceResult<String, Integer> result = verifyBankSlipDetailStatus(bankSlipDetailDO, bankSlipClaim, claimParamList, now);
        if(!ErrorCode.SUCCESS.equals(result.getErrorCode())){
            return result;
        }

        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        return serviceResult;
    }


    private ServiceResult<String, Integer> verifyBankSlipDetailStatus(BankSlipDetailDO bankSlipDetailDO, BankSlipClaim bankSlipClaim, List<ClaimParam> claimParamList, Date now) {
        ServiceResult<String, Integer> serviceResult = new ServiceResult<>();
        //如果状态为未认领状态
        if (BankSlipDetailStatus.UN_CLAIMED.equals(bankSlipDetailDO.getDetailStatus())) {
            BankSlipClaimDO bankSlipClaimDO = ConverterUtil.convert(bankSlipClaim, BankSlipClaimDO.class);

            for (ClaimParam claimParam : claimParamList) {
                bankSlipClaimDO.setOtherSideAccountNo(bankSlipDetailDO.getOtherSideAccountNo());
                bankSlipClaimDO.setClaimAmount(claimParam.getClaimAmount());
                bankSlipClaimDO.setClaimSerialNo(System.currentTimeMillis());
                bankSlipClaimDO.setRechargeStatus(RechargeStatus.INITIALIZE);
                bankSlipClaimDO.setDataStatus(CommonConstant.COMMON_CONSTANT_YES);
                bankSlipClaimDO.setCreateUser(userSupport.getCurrentUserId().toString());
                bankSlipClaimDO.setCreateTime(now);
                bankSlipClaimDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                bankSlipClaimDO.setUpdateTime(now);
                bankSlipClaimMapper.save(bankSlipClaimDO);
            }
            //跟新银行对公流水项
            bankSlipDetailDO.setDetailStatus(BankSlipDetailStatus.CLAIMED);
            bankSlipClaimDO.setUpdateTime(now);
            bankSlipClaimDO.setUpdateUser(userSupport.getCurrentUserId().toString());
            bankSlipDetailMapper.update(bankSlipDetailDO);

            //跟新银行对公流水
            BankSlipDO bankSlipDO = bankSlipMapper.findById(bankSlipDetailDO.getBankSlipId());

            //跟新银行对公流水已认领笔数
            bankSlipDO.setNeedClaimCount(bankSlipDO.getNeedClaimCount() - 1);
            bankSlipDO.setClaimCount(bankSlipDO.getClaimCount() + 1);
            if (bankSlipDO.getNeedClaimCount() == 0) {
                bankSlipDO.setSlipStatus(SlipStatus.ALL_CLAIM);
            }

            bankSlipMapper.update(bankSlipDO);
            // todo 状态为已认领状态

        } else if (BankSlipDetailStatus.CLAIMED.equals(bankSlipDetailDO.getDetailStatus())) {
            //todo 以前的全部删除 添加新的
            List<BankSlipClaimDO> bankSlipClaimDOList = bankSlipClaimMapper.findByBankSlipDetailId(bankSlipDetailDO.getId());
            if (CollectionUtil.isNotEmpty(bankSlipClaimDOList)) {
                //先删除数据库原有的银行对公流水认领表
                for (BankSlipClaimDO bankSlipClaimDO : bankSlipClaimDOList) {
                    bankSlipClaimDO.setDataStatus(CommonConstant.COMMON_CONSTANT_NO);
                    bankSlipClaimDO.setUpdateTime(now);
                    bankSlipClaimDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                    bankSlipClaimMapper.update(bankSlipClaimDO);
                }

            }
            BankSlipClaimDO bankSlipClaimDO = new BankSlipClaimDO();
            for (ClaimParam claimParam : claimParamList) {
                bankSlipClaimDO.setOtherSideAccountNo(bankSlipDetailDO.getOtherSideAccountNo());
                bankSlipClaimDO.setClaimAmount(claimParam.getClaimAmount());
                bankSlipClaimDO.setClaimSerialNo(System.currentTimeMillis());
                bankSlipClaimDO.setRechargeStatus(RechargeStatus.INITIALIZE);
                bankSlipClaimDO.setDataStatus(CommonConstant.COMMON_CONSTANT_YES);
                bankSlipClaimDO.setCreateUser(userSupport.getCurrentUserId().toString());
                bankSlipClaimDO.setCreateTime(now);
                bankSlipClaimDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                bankSlipClaimDO.setUpdateTime(now);
                bankSlipClaimMapper.save(bankSlipClaimDO);
            }
            //todo 状态为忽略状态
        } else if (BankSlipDetailStatus.IGNORE.equals(bankSlipDetailDO.getDetailStatus())) {
            BankSlipClaimDO bankSlipClaimDO = ConverterUtil.convert(bankSlipClaim, BankSlipClaimDO.class);
            for (ClaimParam claimParam : claimParamList) {
                bankSlipClaimDO.setOtherSideAccountNo(bankSlipDetailDO.getOtherSideAccountNo());
                bankSlipClaimDO.setClaimAmount(claimParam.getClaimAmount());
                bankSlipClaimDO.setClaimSerialNo(System.currentTimeMillis());
                bankSlipClaimDO.setRechargeStatus(RechargeStatus.INITIALIZE);
                bankSlipClaimDO.setDataStatus(CommonConstant.COMMON_CONSTANT_YES);
                bankSlipClaimDO.setCreateUser(userSupport.getCurrentUserId().toString());
                bankSlipClaimDO.setCreateTime(now);
                bankSlipClaimDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                bankSlipClaimDO.setUpdateTime(now);
                bankSlipClaimMapper.save(bankSlipClaimDO);
            }
            //todo 跟新银行对公流水明细数据 (改变状态为已认领)
            bankSlipDetailDO.setLoanSign(BankSlipDetailStatus.CLAIMED);
            bankSlipDetailDO.setUpdateUser(userSupport.getCurrentUserId().toString());
            bankSlipDetailDO.setUpdateTime(now);
            bankSlipDetailMapper.update(bankSlipDetailDO);
            //todo 跟新银行对公流水数据(已认领笔数+1)
            BankSlipDO bankSlipDO = bankSlipMapper.findById(bankSlipDetailDO.getBankSlipId());
            //跟新银行对公流水已认领笔数
            bankSlipDO.setClaimCount(bankSlipDO.getClaimCount() + 1);
            bankSlipMapper.update(bankSlipDO);
        }
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(bankSlipDetailDO.getId());
        return serviceResult;
    }

}