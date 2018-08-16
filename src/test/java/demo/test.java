package demo;

import com.megvii.concurrent.pool.ThreadPool;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName test
 * @Description test
 * @Author shuliyao
 * @CreateTime 2018/7/20 下午3:48
 */
@RestController
public class Test {


    @Autowired
    ThreadPool threadPool;

    @PostMapping("/test")
    @ApiOperation("打开或者关闭定时入库开关接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "className", paramType = "query", required = true),
    })
    private String test(String className) throws ClassNotFoundException, IOException {

        File file = new File("/Users/colin/Downloads/7248.jpg_wh1200.jpg");
        InputStream inputStream = new FileInputStream(file);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] b = new byte[1024];
        int n;
        while ((n = inputStream.read(b)) != -1)
        {
            bos.write(b, 0, n);
        }
        inputStream.close();
        bos.close();
        byte [] bytes = bos.toByteArray();

        for (int i = 0; i < 11; i++) {
            Map<String,Object> map = new HashMap<>();
            map.put("url","/Users/colin/Downloads/yslTest");
            map.put("file",bytes);
            map.put("fileName",i);
            map.put("filesSuffix","jpg");
            try {
                threadPool.putAnRun(map,TestThread.class);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        }
        return "ok";
    }
}
