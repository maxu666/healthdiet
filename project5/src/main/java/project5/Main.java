package project5;

// DownloadThread类继承Thread类，模拟下载任务
class DownloadThread extends Thread {
    @Override
    public void run() {
        // 无限循环，模拟一直进行下载
        while (true) {
            try {
                // 每隔一秒输出一次“下载”
                System.out.println("下载");
                // 暂停当前线程1秒钟，模拟下载过程
                Thread.sleep(1000);  // 每隔1秒输出一次
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

// MusicThread类继承Thread类，模拟听歌任务
class MusicThread extends Thread {
    @Override
    public void run() {
        // 无限循环，模拟一直进行听歌
        while (true) {
            try {
                // 每隔一秒输出一次“听歌”
                System.out.println("听歌");
                // 暂停当前线程1秒钟，模拟听歌过程
                Thread.sleep(1000);  // 每隔1秒输出一次
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

public class Main {
    public static void main(String[] args) {
        // 创建DownloadThread和MusicThread对象
        DownloadThread downloadThread = new DownloadThread();
        MusicThread musicThread = new MusicThread();

        // 启动两个线程
        downloadThread.start();
        musicThread.start();
    }
}

