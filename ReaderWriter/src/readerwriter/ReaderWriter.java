package readerwriter;
import java.util.concurrent.Semaphore;


class ReaderWriter {

    static int readerCount = 0;
    static Semaphore x = new Semaphore(1);
    static Semaphore reader = new Semaphore(1);
    static Semaphore writer = new Semaphore(1);

    static class Read implements Runnable {
        @Override
        public void run() {
            try {
                reader.acquire();
                x.acquire();
                readerCount++;
                if (readerCount == 1) writer.acquire();
                x.release();

                System.out.println("Thread "+Thread.currentThread().getName() + " is READING");
                Thread.sleep(1500);
                System.out.println("Thread "+Thread.currentThread().getName() + " has FINISHED READING");
                
                x.acquire();
                readerCount--;
                if (readerCount == 0) writer.release();
                x.release();
                reader.release();
                

            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    static class Write implements Runnable {
        @Override
        public void run() {
            try {
                reader.acquire();
                writer.acquire();
                System.out.println("Thread "+Thread.currentThread().getName() + " is WRITING");
                Thread.sleep(2500);
                System.out.println("Thread "+Thread.currentThread().getName() + " has finished WRITING");
                writer.release();
                reader.release();
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static void main(String[] args) throws Exception {
        Read read = new Read();
        Write write = new Write();
        Thread t1 = new Thread(read);
        t1.setName("thread1");
        Thread t2 = new Thread(read);
        t2.setName("thread2");
        Thread t3 = new Thread(write);
        t3.setName("thread3");
        Thread t4 = new Thread(read);
        t4.setName("thread4");
        t1.start();
        t2.start();
        t3.start();
        t4.start();
    }
}
