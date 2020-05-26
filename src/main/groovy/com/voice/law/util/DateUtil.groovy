package com.voice.law.util

import java.text.SimpleDateFormat

class DateUtil {

    /**
     * 加上指定年月日后的时间
     * @param date
     * @param year
     * @param month
     * @param day
     * @return
     */
    static Date getDateByAddOption(Date date, int year, int month, int day) {
        Calendar calendar = Calendar.getInstance()
        calendar.setTime(date)
        calendar.add(Calendar.YEAR, year)
        calendar.add(calendar.MONTH, month)
        calendar.add(calendar.DAY_OF_YEAR, day)
        return calendar.getTime()
    }

    public static void main(String[] args) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd")

        print simpleDateFormat.format(getDateByAddOption(new Date(), 0, 0, -7))
    }
}
