package com.lxzl.erp.web.test;

import com.lxzl.erp.common.util.DateUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2017-12-09 14:03
 */
public class CommonTest {
    public static void main(String[] args) {
        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(DateUtil.getNextMonthDayStartTime(20)));
    }
}
