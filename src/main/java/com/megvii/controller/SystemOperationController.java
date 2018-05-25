package com.megvii.controller;

import com.megvii.service.PhotoService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 系统服务操作控制器
 * @author shuli.yao
 */
@RestController
@RequestMapping("/system")
public class SystemOperationController {


    @Autowired
    PhotoService photoService;

    @PostMapping("/test")
    @ApiOperation("测试用接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "beginNumber", value = "新增数量", paramType = "query", required = true),
            @ApiImplicitParam(name = "EndNumber", value = "=结束数量", paramType = "query", required = true),
    })
    public void test(Integer beginNumber,Integer  EndNumber) throws IOException {
        File file = new File("d://ysl.jpg");

        FileInputStream fileInputStream = new FileInputStream(file);
        FileChannel fileChannel = null;
        fileChannel = fileInputStream.getChannel();
        ByteBuffer byteBuffer = ByteBuffer.allocate((int) fileChannel.size());
        while ((fileChannel.read(byteBuffer)) > 0) {
            // do nothing
            // System.out.println("reading");
        }
        byte [] bytes = byteBuffer.array();
        for (int i =beginNumber; i < EndNumber; i++) {
            String sj= String.valueOf(i);
            for (int j = 0; j < (6-String.valueOf(i).length()); j++) {
                sj+="0";
            }
            photoService.testInsert(String.valueOf(i),bytes,"372928199011"+sj);

        }
        fileInputStream.close();
        return ;
    }


//    @PostMapping("/isDeletePhotoImg")
//    @ApiOperation("增量执行时，执行完毕人像是否删除(默认删除，防止重复入库)")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "noORYes", value = "删除参数:1.删除 0.不删除", paramType = "query", required = true),
//    })
//    public String isDeletePhotoImg(String noORYes){
//        if("1".equals(noORYes)){
//            systemConfig.setJobPhotoDelete(true);
//            return "设置成功";
//        }else if("0".equals(noORYes)){
//            systemConfig.setJobPhotoDelete(false);
//            return "设置成功";
//        }
//        return "抱歉无法分辨您的传入！";
//
//    }

/*    @PostMapping("/checkImportPhoto")
    @ApiOperation("检查执行接口，仅将前n条数据入库")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "number", value = "默认入库前几条数据", paramType = "query", required = true),
    })
    public String checkImportPhoto(Integer number){
        systemConfig.setShellConfigPath("");
        systemConfig.setTimerOnOff(false);
        //全量存储文件地址
        systemConfig.setFilePath(filePath);

        photoService.checkPhotoImport(number);

        photoService.shellImprotPhoto(systemConfig.getClearShellName(),systemConfig.getShellPath(),systemConfig.getShellConfigPath());

        photoService.shellImprotPhoto(systemConfig.getImprotShellName(),systemConfig.getShellPath(),systemConfig.getShellConfigPath());
        return "入库成功，请及的清除相应记录";
    }*/
}
