package project4;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Scanner;

public class DateDifference {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // 提示用户输入第一天的年、月、日
        System.out.print("请输入第一天的年份: ");
        int year1 = scanner.nextInt();
        System.out.print("请输入第一天的月份: ");
        int month1 = scanner.nextInt();
        System.out.print("请输入第一天的日期: ");
        int day1 = scanner.nextInt();

        // 提示用户输入第二天的年、月、日
        System.out.print("请输入第二天的年份: ");
        int year2 = scanner.nextInt();
        System.out.print("请输入第二天的月份: ");
        int month2 = scanner.nextInt();
        System.out.print("请输入第二天的日期: ");
        int day2 = scanner.nextInt();

        // 创建两个 LocalDate 对象表示两个日期
        LocalDate date1 = LocalDate.of(year1, month1, day1);
        LocalDate date2 = LocalDate.of(year2, month2, day2);

        // 计算两个日期之间的天数差
        long daysBetween = ChronoUnit.DAYS.between(date1, date2);

        // 输出结果
        System.out.println("这两天之间的天数间隔是: " + Math.abs(daysBetween));

        // 关闭 Scanner
        scanner.close();
    }
}

