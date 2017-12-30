package com.lxzl.erp.core.service.amount.support;

import com.lxzl.erp.common.constant.OrderRentType;
import com.lxzl.erp.common.util.BigDecimalUtil;
import com.lxzl.se.common.util.date.DateUtil;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

@Component
public class AmountSupport {


    /**
     * @param rentStartDate 开始时间
     * @param returnEndDate 归还时间
     * @param unitAmount    单价
     * @return 总金额
     */
    public BigDecimal calculateRentAmount(Date rentStartDate, Date returnEndDate, BigDecimal unitAmount) {
        int monthSpace = com.lxzl.erp.common.util.DateUtil.getMonthSpace(rentStartDate, returnEndDate);
        Calendar returnEndDateCalendar = Calendar.getInstance();
        returnEndDateCalendar.setTime(returnEndDate);

        Calendar rentStartDateCalendar = Calendar.getInstance();
        rentStartDateCalendar.setTime(rentStartDate);

        Integer previousSurplusDays = 0, nextSurplusDays = 0;
        // 当月日期 即dd
        int returnEndDateDay = returnEndDateCalendar.get(Calendar.DAY_OF_MONTH);
        // 当月日期 即dd
        int rentStartDateDay = rentStartDateCalendar.get(Calendar.DAY_OF_MONTH);
        // 前一个月的总天数
        int previousAllDays = com.lxzl.erp.common.util.DateUtil.getActualMaximum(DateUtil.monthInterval(returnEndDate, -1));
        // 后一个月的总天数
        int nextAllDays = com.lxzl.erp.common.util.DateUtil.getActualMaximum(returnEndDate);

        if (returnEndDateDay >= rentStartDateDay) {
            nextSurplusDays = returnEndDateDay - rentStartDateDay + 1;
        } else {
            previousSurplusDays = com.lxzl.erp.common.util.DateUtil.getActualMaximum(DateUtil.monthInterval(returnEndDate, -1)) - rentStartDateDay;
            nextSurplusDays = rentStartDateDay;
        }
        BigDecimal surplusDaysAmount = BigDecimalUtil.add(BigDecimalUtil.div(BigDecimalUtil.mul(unitAmount, new BigDecimal(previousSurplusDays)), new BigDecimal(previousAllDays), BigDecimalUtil.SCALE), BigDecimalUtil.div(BigDecimalUtil.mul(unitAmount, new BigDecimal(nextSurplusDays)), new BigDecimal(nextAllDays), BigDecimalUtil.SCALE));

        // 两月相差月数总金额，加上前后两个月天数的总金额
        BigDecimal totalAmount = BigDecimalUtil.add(BigDecimalUtil.mul(unitAmount, new BigDecimal(monthSpace)), surplusDaysAmount);
        return totalAmount;
    }


    /**
     * @param value    字面量，例如设备月租金 90元 ，value=90;设备次租金10元，value=10
     * @param start    起租时间
     * @param end      归还时间
     * @param rentType 租赁类型
     * @return
     */
    public BigDecimal calculateRentCost(BigDecimal value, Date start, Date end, Integer rentType) {
        BigDecimal cost = new BigDecimal(0);
        if (OrderRentType.RENT_TYPE_DAY.equals(rentType)) {
            cost = calculateDay(value, start, end);
        } else if (OrderRentType.RENT_TYPE_MONTH.equals(rentType)) {
            cost = calculateMonth(value, start, end);
        }
        return cost;
    }

    /**
     * todo 计算按次租的租金
     *
     * @param value
     * @param start
     * @param end
     * @return
     */
    private BigDecimal calculateTime(BigDecimal value, Date start, Date end) {
        BigDecimal cost = new BigDecimal(0);
        return cost;
    }

    /**
     * todo 计算按天租的租金
     *
     * @param value
     * @param start
     * @param end
     * @return
     */
    private BigDecimal calculateDay(BigDecimal value, Date start, Date end) {
        BigDecimal cost = new BigDecimal(0);
        cost = new BigDecimal(50);
        return cost;
    }

    /**
     * todo 计算按月租的租金
     *
     * @param value
     * @param start
     * @param end
     * @return
     */
    private BigDecimal calculateMonth(BigDecimal value, Date start, Date end) {
        BigDecimal cost = new BigDecimal(0);
        cost = new BigDecimal(100);
        return cost;
    }
}
