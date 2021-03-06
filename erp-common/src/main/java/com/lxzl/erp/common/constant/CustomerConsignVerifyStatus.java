package com.lxzl.erp.common.constant;

/**
 * 客户审核地址状态
 *
 */
public class CustomerConsignVerifyStatus {

    public static final Integer VERIFY_STATUS_PENDING = 0;          // 0-待提交
    public static final Integer VERIFY_STATUS_COMMIT = 1;          // 1-已提交
    public static final Integer VERIFY_STATUS_FIRST_PASS = 2;          // 2-初审通过
    public static final Integer VERIFY_STATUS_END_PASS = 3;          // 3-终审通过
    public static final Integer VERIFY_STATUS_BACK = 4;          // 4-审核驳回


}
