package project5;

public class TurtleAndRabbitRace {

    public static void main(String[] args) {
        // 创建乌龟线程和兔子线程
        Thread turtle = new Thread(new Turtle());
        Thread rabbit = new Thread(new Rabbit());

        // 启动线程
        turtle.start();
        rabbit.start();
    }
}

// 乌龟类，实现Runnable接口
class Turtle implements Runnable {
    private int position = 0;  // 乌龟的当前位置

    @Override
    public void run() {
        while (position < 100) {  // 假设跑步100米为终点
            try {
                Thread.sleep(1000);  // 每秒更新一次位置
                position += 1;  // 乌龟每秒跑1米
                System.out.println("乌龟当前位置: " + position + " 米");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("乌龟到达终点！");
    }
}

// 兔子类，实现Runnable接口
class Rabbit implements Runnable {
    private int position = 0;  // 兔子的当前位置

    @Override
    public void run() {
        while (position < 100) {  // 假设跑步100米为终点
            try {
                Thread.sleep(1000);  // 每秒更新一次位置
                position += 3;  // 兔子每秒跑3米
                System.out.println("兔子当前位置: " + position + " 米");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("兔子到达终点！");
    }
}

