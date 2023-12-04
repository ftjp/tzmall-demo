package com.tzmall.operations.test;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;


public class OffsetTime {

    public final static LocalTime STOP_WORK_START_TIME = LocalTime.of(22, 0);

    public final static LocalTime STOP_WORK_END_TIME = LocalTime.of(5, 0);

    public static void main(String[] args) {
//        System.out.println(getOffsetTimeResult(LocalDateTime.of(2023, 8, 21, 3, 0), 172800));//偏移48 【22:00】-【5:00】已验 ，结果 2023-08-23T19:00
//        System.out.println(getOffsetTimeResult(LocalDateTime.of(2023, 8, 21, 3, 1), 28800)); //偏移8【22:00】-【5:00】已验 ，结果 2023-08-21T13:00
//        System.out.println(getOffsetTimeResult(LocalDateTime.of(2023, 8, 21, 6, 0), 28800)); //偏移8【22:00】-【5:00】已验 ，结果 2023-08-21T14:00
//        System.out.println(getOffsetTimeResult(LocalDateTime.of(2023, 8, 21, 22, 0), 28800)); //偏移8【22:00】-【5:00】已验 ，结果 2023-08-21T13:00
//        System.out.println(getOffsetTimeResult(LocalDateTime.of(2023, 8, 21, 18, 0), 28800)); //偏移8【22:00】-【5:00】已验 ，结果 2023-08-22T09:00
//          System.out.println(getOffsetTimeResult(LocalDateTime.now(), 28800)); //偏移8【22:00】-【5:00】已验 ，结果 2023-08-22T09:00
        System.out.println(00000110 & 1);
    }

    /**
     * 计算时间差（s）
     * @param start
     * @param end
     * @return
     */
    public static long getDurationSeconds(LocalTime start, LocalTime end) {
        long result = Duration.between(start, end).getSeconds();
        //添加一天的秒数处理成正数
        if (result < 0) {
            result += 86400;
        }
        return result;
    }

    /**
     * 求偏移
     * @param currentTime 入参
     * @param offsetTime 向未来偏移的秒数(s)
     * @return
     */
    public static LocalDateTime getOffsetTimeResult(LocalDateTime currentTime, long offsetTime) {
        //结果
        LocalDateTime result;
        //拆分当前时间，得到日期部分和时间部分
        LocalDate currentLocalDate = currentTime.toLocalDate();
        LocalTime localTime = currentTime.toLocalTime();
        if (inStopWorkingHoursAtNight(localTime)) {
            /*当前时间在停工时间区间内*/
            //workDura = 除去夜间停止工作时间，每天可工作的时长
            long workDura = getDurationSeconds(STOP_WORK_END_TIME, STOP_WORK_START_TIME);
            if(offsetTime < workDura) {
                result = STOP_WORK_END_TIME.atDate(currentLocalDate).plusSeconds(offsetTime);
            } else {
                LocalDateTime templateTime = STOP_WORK_END_TIME.atDate(currentLocalDate);
                result = getOffsetTimeResult(templateTime.plusDays(offsetTime/workDura), offsetTime % workDura);
            }
        } else {
            /*当前时间不在停工时间区间内*/
            //得到【当前时间】到【夜间停止工作起始时间】的时间差
            long durationLeft = getDurationSeconds(localTime, STOP_WORK_START_TIME);
            if (offsetTime <= durationLeft) {
                result = currentTime.plusSeconds(offsetTime);
            } else {
                LocalDateTime templateTime = STOP_WORK_END_TIME.atDate(currentLocalDate);
                if (templateTime.isBefore(currentTime) || templateTime.isEqual(currentTime)) {
                    templateTime = templateTime.plusDays(1);
                }
                result = getOffsetTimeResult(templateTime, offsetTime - Math.abs(durationLeft));
            }
        }
        return result;
    }

    /**
     * 判断入参是否处于夜间停止工作时间
     * @param localTime 当前时间
     * @return
     */
    private static boolean inStopWorkingHoursAtNight(LocalTime localTime) {
        //【夜间停止工作起始时间】到【当前时间】的时间差
        long duraLeft = getDurationSeconds(STOP_WORK_START_TIME, localTime);
        //【当前时间】到【夜间停止工作结束时间】的时间差
        long duraRight = getDurationSeconds(localTime, STOP_WORK_END_TIME);
        long dura = duraLeft + duraRight;
        return Objects.equals(dura, getDurationSeconds(STOP_WORK_START_TIME, STOP_WORK_END_TIME));
    }

}
