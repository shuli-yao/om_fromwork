package com.megvii.concurrent.pool;

import com.megvii.configuration.SystemConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @ClassName ThreadPool
 * @Description 程序线程池
 * @Author shuliyao
 * @CreateTime 2018/7/20 下午2:58
 */
@Component
@Slf4j
public class ThreadPool {
    @Autowired
    private SystemConfig systemConfig;


    public ExecutorService cachedThreadPool;

    public LinkedBlockingQueue<Object> downloadQueue;



    /**
     * 通过初始化方法创建线程池及下载队列对象
     */
    @PostConstruct
    public void init(){

        log.info("初始化线程池组件，数据队列大小为："+systemConfig.downloadQueueSize+",线程池大小："+systemConfig.poolSize);

        this.downloadQueue = new LinkedBlockingQueue<Object>(systemConfig.downloadQueueSize); //创建阻塞队列对象

        this.cachedThreadPool = Executors.newFixedThreadPool(systemConfig.poolSize);  //定义线程池
    }


    /**
     * 获取阻塞队列方法
     * @return
     */
    public  LinkedBlockingQueue  getArrayBlockingQueue(){
        return downloadQueue;
    }

    /**
     * 执行线程
     */
    private  void runThread(Class clazz)throws IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchMethodException {

        //获取指定的构造方法，需要该方法的参数列表
        Constructor constructor = clazz.getDeclaredConstructor(LinkedBlockingQueue.class,SystemConfig.class);
        Object b = constructor.newInstance(downloadQueue,systemConfig);
        //向线程池中添加一个执行任务
        cachedThreadPool.execute((Runnable) b);
    }

    /**
     * 向队列中添加数并执行线程
     */
    public  boolean putAnRun(Object value,Class clazz) throws InterruptedException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
            //1.向下载队列中添加数据
            downloadQueue.put(value);
            //2.检查当前唤醒线程数，是否大于当前总线程数，若不大于则唤醒一个线程。
            if(((ThreadPoolExecutor)cachedThreadPool).getActiveCount() < systemConfig.poolSize){
                runThread(clazz);
            }

        return true;
    }
}
