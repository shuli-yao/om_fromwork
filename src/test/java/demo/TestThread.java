package demo;

import com.megvii.bean.ResultBackObject;
import com.megvii.component.network.bean.NetworkSendObject;
import com.megvii.component.network.impl.HttpSendImpl;
import com.megvii.component.run.impl.NetworkRunImpl;
import com.megvii.concurrent.thread.ExpandThread;
import com.megvii.configuration.SystemConfig;
import com.megvii.component.run.RunService;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * @ClassName TestThread
 * @Description test
 * @Author shuliyao
 * @CreateTime 2018/7/20 下午4:58
 */
public class TestThread extends ExpandThread {
    public TestThread(LinkedBlockingQueue<Object> linkedBlockingQueue, SystemConfig systemConfig) {
        super(linkedBlockingQueue, systemConfig);
    }

    /**
     * 线程处理类
     * @param o
     * @return
     */
//    @Override
//    public RunService perform(Object o) {
//
//        Map<String,Object> map = (Map<String, Object>) o;
//        FileIoObject fileObject =new FileIoObject(map.get("fileName").toString(),
//                map.get("filesSuffix").toString()
//                ,map.get("url").toString(),map.get("file"));
//
//        return new FileIoRunImpl(fileObject,new ByteFileIo());
//    }


    public RunService perform(Object o) {
//        Map<String,Object> map = (Map<String, Object>) o;
//        FileIoObject fileObject =new FileIoObject(map.get("fileName").toString(),
//                map.get("filesSuffix").toString()
//                ,map.get("url").toString(),map.get("file"));
        return new NetworkRunImpl(new HttpSendImpl(),new NetworkSendObject());
    }

    @Override
    public void writeBack(ResultBackObject resultBackObject) {

    }


}
