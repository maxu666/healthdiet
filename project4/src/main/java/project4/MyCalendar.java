package project4;

import java.util.Calendar;
import java.util.Scanner;

public class MyCalendar {

    // 输出指定年份和月份的日历
    public void getCalendar(int year, int month) {
        // 使用 Calendar 类
        Calendar calendar = Calendar.getInstance();

        // 设置年份和月份，月份从0开始，所以要减去1
        calendar.set(year, month - 1, 1);

        // 获取该月的第一天是星期几
        int firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        // 获取该月的天数
        int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        // 输出日历头部：星期
        System.out.println("Sun Mon Tue Wed Thu Fri Sat");

        // 输出空格，直到该月的第一天
        for (int i = 1; i < firstDayOfWeek; i++) {
            System.out.print("    ");  // 每个星期天显示4个空格
        }

        // 输出该月的每一天
        for (int day = 1; day <= daysInMonth; day++) {
            System.out.printf("%3d ", day);
            if ((firstDayOfWeek + day - 1) % 7 == 0) {
                System.out.println(); // 每7天换行
            }
        }

        System.out.println();  // 换行
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // 提示用户输入年份和月份
        System.out.print("请输入年份：");
        int year = scanner.nextInt();

        System.out.print("请输入月份（1-12）：");
        int month = scanner.nextInt();

        // 创建 MyCalendar 对象并调用 getCalendar 方法
        MyCalendar myCalendar = new MyCalendar();
        myCalendar.getCalendar(year, month);

        scanner.close();
    }
}

