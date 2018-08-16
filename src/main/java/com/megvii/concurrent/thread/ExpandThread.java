package com.megvii.concurrent.thread;

import com.megvii.bean.ResultBackObject;
import com.megvii.configuration.SystemConfig;
import com.megvii.component.run.RunService;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * @ClassName ExpandThread
 * @Description 线程执行抽象类
 * @Author shuliyao
 * @CreateTime 2018/7/20 下午3:13
 */
public abstract class ExpandThread extends  Thread{

    LinkedBlockingQueue<Object>  linkedBlockingQueue =null;

    SystemConfig systemConfig = null;

    public ExpandThread(LinkedBlockingQueue<Object> linkedBlockingQueue, SystemConfig systemConfig){
        this.linkedBlockingQueue=linkedBlockingQueue;
        this.systemConfig =systemConfig;
    }

    @Override
    public void run() {

        //1.获取队列中的数据，直到队列为空时
        while(linkedBlockingQueue.size() > 0){
            //编写回写对象
            ResultBackObject resultBackObject = new ResultBackObject();
            try {
               Object object = linkedBlockingQueue.take();

                //执行下载线程逻辑，优先执行子类逻辑
                RunService runService = perform(object);

                runService.run();

            } catch (InterruptedException e) {
                resultBackObject.setCode(200);
                resultBackObject.setSucceed(false);
                resultBackObject.setDescribe("执行异常:"+e.getMessage());

            }finally {
                writeBack(resultBackObject);
            }
        }
    }


    public abstract RunService perform(Object o);

    public abstract void writeBack(ResultBackObject resultBackObject);






}
