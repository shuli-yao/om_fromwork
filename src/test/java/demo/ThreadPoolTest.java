package demo;

import org.springframework.scheduling.concurrent.DefaultManagedAwareThreadFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * @ClassName ThreadPoolTest
 * @Description 单线程池
 * @Author shuliyao
 * @CreateTime 2018/8/1 下午2:36
 */
public class ThreadPoolTest{

    public static void main(String[] args) {
        ThreadFactory threadFactory = new DefaultManagedAwareThreadFactory();

        ExecutorService service = Executors.newFixedThreadPool(100);


        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName());
            }
        });

        int i = 0;
        while (i< 10000){
            service.execute(thread);
            i++;
        }
        System.out.println("结束");
    }
}
