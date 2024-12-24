package project5;

// TicketSeller类实现Runnable接口，用于模拟售票窗口
class TicketSeller implements Runnable {
    // 总票数
    private static int tickets = 100;  // 假设共有100张票
    private static final Object lock = new Object();  // 用来锁定共享资源

    @Override
    public void run() {
        // 每个线程会尝试售出票，直到票卖完为止
        while (true) {
            synchronized (lock) {  // 锁定共享资源，保证线程安全
                if (tickets > 0) {
                    // 如果还有票，就出售一张
                    tickets--;
                    System.out.println(Thread.currentThread().getName() + " 售出了一张票，剩余票数: " + tickets);
                    try {
                        // 模拟售票的过程，休眠一段时间
                        Thread.sleep(100);  // 每次售票后等待100ms
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    // 如果票卖完了，退出
                    break;
                }
            }
        }
    }
}

public class TicketSale {
    public static void main(String[] args) {
        // 创建TicketSeller实例
        TicketSeller ticketSeller = new TicketSeller();

        // 创建三个线程，分别模拟三个售票窗口
        Thread window1 = new Thread(ticketSeller, "窗口1");
        Thread window2 = new Thread(ticketSeller, "窗口2");
        Thread window3 = new Thread(ticketSeller, "窗口3");

        // 启动三个窗口线程
        window1.start();
        window2.start();
        window3.start();
    }
}

