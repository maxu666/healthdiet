package project6;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class JDBCExample {
    public static void main(String[] args) {
        // 数据库连接信息
        String url = "jdbc:mysql://localhost:3306/test?useSSL=false&serverTimezone=UTC";
        String username = "root"; // 替换为您的 MySQL 用户名
        String password = "maxu20040310"; // 替换为您的 MySQL 密码

        // JDBC 相关对象
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            // 1. 加载 JDBC 驱动
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("MySQL JDBC 驱动加载成功！");

            // 2. 建立数据库连接
            connection = DriverManager.getConnection(url, username, password);
            System.out.println("成功连接到数据库！");

            // 3. 创建 Statement 对象
            statement = connection.createStatement();

            // 4. 执行 SQL 查询
            String sql = "SELECT * FROM websites";
            resultSet = statement.executeQuery(sql);

            // 5. 遍历 ResultSet 并输出数据
            System.out.println("websites 表中的数据：");
            System.out.printf("%-5s %-20s %-30s %-10s %-10s%n", "ID", "Name", "URL", "Alexa", "Country");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String urlValue = resultSet.getString("url");
                int alexa = resultSet.getInt("alexa");
                String country = resultSet.getString("country");

                System.out.printf("%-5d %-20s %-30s %-10d %-10s%n", id, name, urlValue, alexa, country);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                // 6. 关闭资源
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
                System.out.println("数据库连接已关闭。");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
