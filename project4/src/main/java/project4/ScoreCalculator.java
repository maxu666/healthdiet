package project4;

import java.util.regex.*;

public class ScoreCalculator {
    public static void main(String[] args) {
        // 使用固定的成绩字符串
        String input = "数学87分，物理76分，英语96分";

        // 解析字符串中的各科成绩
        String[] subjects = input.split("，"); // 按中文逗号分隔各科成绩
        int totalScore = 0;
        int subjectCount = 0;

        // 正则表达式匹配数字部分
        Pattern pattern = Pattern.compile("(\\d+)分");

        for (String subject : subjects) {
            Matcher matcher = pattern.matcher(subject);
            if (matcher.find()) {
                // 提取分数
                int score = Integer.parseInt(matcher.group(1));
                totalScore += score;
                subjectCount++;
            }
        }

        // 计算平均分数
        double averageScore = (subjectCount > 0) ? (double) totalScore / subjectCount : 0;

        // 输出总成绩和平均分
        System.out.println("总成绩: " + totalScore);
        System.out.println("平均分: " + String.format("%.2f", averageScore));
    }
}
