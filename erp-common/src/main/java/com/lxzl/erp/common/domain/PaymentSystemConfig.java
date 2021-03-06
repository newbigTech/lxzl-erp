package com.lxzl.erp.common.domain;

/**
 * 描述: 支付系统配置信息
 *
 * @author gaochao
 * @date 2017-12-14 15:34
 */
public class PaymentSystemConfig {

    public static String paymentSystemQueryCustomerAccountURL;
    public static String paymentSystemManualChargeURL;
    public static String paymentSystemManualDeductURL;
    public static String paymentSystemBalancePayURL;
    public static String paymentSystemWeixinPayURL;
    public static String paymentSystemWeixinPayCallbackURL;
    public static String paymentSystemReturnDepositURL;
    public static String paymentSystemAppId;
    public static String paymentSystemAppSecret;

    public static String paymentSystemWeixinChargeURL;
    public static String paymentSystemQueryChargeRecordPageURL;
    public static String paymentSystemQueryPayResultURL;
    public static String paymentSystemQueryCustomerAccountLogPageURL;
    public static String paymentSystemReturnDepositExpandURL;
    public static String paymentSystemPublicTransferPlusChargeURL;
    public static String paymentSystemReturnOtherAndPayDepositURL;

    public static String paymentSystemAlipayChargeUrl;

    public void setPaymentSystemPublicTransferPlusChargeURL(String paymentSystemPublicTransferPlusChargeURL) {
        PaymentSystemConfig.paymentSystemPublicTransferPlusChargeURL = paymentSystemPublicTransferPlusChargeURL;
    }

    public void setPaymentSystemManualChargeURL(String paymentSystemManualChargeURL) {
        PaymentSystemConfig.paymentSystemManualChargeURL = paymentSystemManualChargeURL;
    }

    public void setPaymentSystemQueryCustomerAccountURL(String paymentSystemQueryCustomerAccountURL) {
        PaymentSystemConfig.paymentSystemQueryCustomerAccountURL = paymentSystemQueryCustomerAccountURL;
    }

    public void setPaymentSystemBalancePayURL(String paymentSystemBalancePayURL) {
        PaymentSystemConfig.paymentSystemBalancePayURL = paymentSystemBalancePayURL;
    }

    public void setPaymentSystemAppId(String paymentSystemAppId) {
        PaymentSystemConfig.paymentSystemAppId = paymentSystemAppId;
    }

    public void setPaymentSystemAppSecret(String paymentSystemAppSecret) {
        PaymentSystemConfig.paymentSystemAppSecret = paymentSystemAppSecret;
    }

    public void setPaymentSystemManualDeductURL(String paymentSystemManualDeductURL) {
        PaymentSystemConfig.paymentSystemManualDeductURL = paymentSystemManualDeductURL;
    }

    public void setPaymentSystemReturnDepositURL(String paymentSystemReturnDepositURL) {
        PaymentSystemConfig.paymentSystemReturnDepositURL = paymentSystemReturnDepositURL;
    }

    public void setPaymentSystemWeixinPayURL(String paymentSystemWeixinPayURL) {
        PaymentSystemConfig.paymentSystemWeixinPayURL = paymentSystemWeixinPayURL;
    }

    public void setPaymentSystemWeixinPayCallbackURL(String paymentSystemWeixinPayCallbackURL) {
        PaymentSystemConfig.paymentSystemWeixinPayCallbackURL = paymentSystemWeixinPayCallbackURL;
    }

    public void setPaymentSystemWeixinChargeURL(String paymentSystemWeixinChargeURL) {
        PaymentSystemConfig.paymentSystemWeixinChargeURL = paymentSystemWeixinChargeURL;
    }

    public void setPaymentSystemQueryChargeRecordPageURL(String paymentSystemQueryChargeRecordPageURL) {
        PaymentSystemConfig.paymentSystemQueryChargeRecordPageURL = paymentSystemQueryChargeRecordPageURL;
    }

    public void setPaymentSystemQueryPayResultURL(String paymentSystemQueryPayResultURL) {
        PaymentSystemConfig.paymentSystemQueryPayResultURL = paymentSystemQueryPayResultURL;
    }

    public void setPaymentSystemQueryCustomerAccountLogPageURL(String paymentSystemQueryCustomerAccountLogPageURL) {
        PaymentSystemConfig.paymentSystemQueryCustomerAccountLogPageURL = paymentSystemQueryCustomerAccountLogPageURL;
    }

    public void setPaymentSystemReturnDepositExpandURL(String paymentSystemReturnDepositExpandURL) {
        PaymentSystemConfig.paymentSystemReturnDepositExpandURL = paymentSystemReturnDepositExpandURL;
    }

    public static void setPaymentSystemReturnOtherAndPayDepositURL(String paymentSystemReturnOtherAndPayDepositURL) {
        PaymentSystemConfig.paymentSystemReturnOtherAndPayDepositURL = paymentSystemReturnOtherAndPayDepositURL;
    }

    public String getPaymentSystemAlipayChargeUrl() {
        return paymentSystemAlipayChargeUrl;
    }

    public void setPaymentSystemAlipayChargeUrl(String paymentSystemAlipayChargeUrl) {
        PaymentSystemConfig.paymentSystemAlipayChargeUrl = paymentSystemAlipayChargeUrl;
    }
}
