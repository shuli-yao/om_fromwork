package com.megvii.service.impl;

import com.megvii.configuration.SystemConfig;
import com.megvii.dbsource.oracle.mapper.PhotoMapper;
import com.megvii.po.Data;
import com.megvii.po.Photo;
import com.megvii.service.PhotoService;
import com.megvii.thread.DownloadThreadPool;
import com.megvii.utlis.DateUtils;
import com.megvii.utlis.ShellUtil;
import com.megvii.utlis.TextUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

@Service
@Slf4j

public class PhotoSerivceImpl implements PhotoService {

    //执行shell脚本工具对象
    private ShellUtil shellUtil = new ShellUtil();

    //获取或写入txt脚本工具信息
    private TextUtils textUtils = new TextUtils();

    //获取系统变量对象
    @Autowired
    SystemConfig systemConfig;

    //获取数据库操作对象
    @Autowired(required = false)
    private PhotoMapper photoMapper;

    //下载连接吃池
    @Autowired
    DownloadThreadPool downloadThreadPool;

    //定义时间格式化对象
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
//    SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");





    /**
     * 全量查询人像数据(不进行Order by排序)
     * 执行流程:
     *  1.查询到数据
     *  2.直接交由入库线程入库
     * @param beginNumber
     * @param sizeNumber
     * @param date
     * @param topBeginNumber
     * @return
     */
    @Override
    public Integer XDBPhotoToLoac(Integer beginNumber, Integer sizeNumber, String date, Integer topBeginNumber) {

        long beginTime = System.currentTimeMillis();

        log.info("数据起止" + beginNumber + "," + sizeNumber.intValue());

        List<Photo> photos = findByPhotosNoOrder(beginNumber, sizeNumber.intValue(), date);

        log.info("查出数据：" + photos.size() + "条,耗时：" + (System.currentTimeMillis() -beginTime) + "ms");

        for (Photo photo : photos) {

            //将数据交由入库线程去入库
            downloadThreadPool.putImgUrl(photo);

        }
        return photos.size();
    }


    /**
     * 增量查询人像数据(进行order by排序)
     * 执行流程:
     *  1.获取上次入库节点时间
     *  2.根据时间查询数据
     *  3.去除重复数据
     *  4.交由入库线程执行入库操作
     *  5.重复以上操作直至所有增量数据入库完毕
     * @param queryMaxSize
     * @return
     * @throws ParseException
     */
    @Override
    public Integer photoToLoca(Integer queryMaxSize) throws ParseException {

        log.info("一、增量入库操作开始执行");

        //--------------------读取txt判断上次入库节点----------------
        String result = textUtils.readerOneRowText(systemConfig.getTextFilePath());

        //初始化默认时间
        String queryDate = "2010/01/01 00:00:00";

        if (result != null && !result.equals("")) {

            //txt中写入时间时按顺序的，所以读取最靠上的一条即可
            String[] results = result.split(",");
            queryDate = results[1];

            //若数据只精确到天则对时间进行填补
            if (queryDate.length() == 10) {
                queryDate = queryDate + " 00:00:00";
            }
        }

        log.info("二、获取上次入库节点时间(会已该时间做查询条件),时间=" + queryDate);

        //--------------------循环执行数据下载功能----------------
        Integer begin = 0;  //开始条数（查询数据用）
        Integer end = queryMaxSize; //结束数据条数（查询数据用）
        Integer countNumber = 0;    //此次增量查询到数据总数
        Integer addNumber = 0;      //确定为新增数据总数
        Integer repeatNumber = 0;   //已存在总数（查询时间为上次新增的时间包含该时间，所以会查出已新增数据）
        Integer queryPageNumber = 0;//执行页码 (总数/单次查询最大条数=页码)

        while (true) {

            //获取执行开始时间 （用于计算耗时）
            long beginTimer = System.currentTimeMillis();

            //每次循环执行页码加1 （仅用于统计）
            queryPageNumber++;

            //根据时间、起始条数查询增量人像数据
            List<Photo> photos = findByPhotos(begin, end, queryDate);

            //将查询到数据量加入总量变量中 (仅用于统计)
            countNumber = countNumber + photos.size();

            //循环处理查询到人像数据
            for (Photo photo : photos) {

                //按照格式格式化时间，用于比较去除重复数据
                Date queryDateTime = sdf.parse(queryDate);

                //去重两步判断，第一步，判断日期是否相同，日期若相同极有可能是上次增量的节点数据
                if (isTimeStrEquest(queryDateTime, photo.getChangeTime())) {

                    //第二步，日期相同情况下，通过日期+身份证号再进行验证，防止同一时间下有多条数据（很极限的情况一般不太可能）
                    if (!compareCardAndTime(photo.getCardId(), photo.getChangeTime())) {

                        //已存在数据+1
                        repeatNumber++;
                        continue;
                    }
                }

                addNumber++;

                //将数据交由入库线程去入库
                downloadThreadPool.putImgUrl(photo);

            }

            log.info("三、执行第" + queryPageNumber + "批数据，查询到数据数量：" + countNumber +
                    ",已存在数量:" + repeatNumber + ",新增数量:" + addNumber +
                    " ,耗时" + (System.currentTimeMillis() - beginTimer));

            //下一页操作  - -、
            begin = end;
            end = end + queryMaxSize;

            //判断若此次查询为最后一页则结束增量
            if (photos.size() < queryMaxSize) {
                break;
            }

        }

        log.info("四、执行完毕,共入库:"+addNumber+"条");
        return addNumber;
    }



    /**
     * 根据时间和身份证号比对上次增量数据中是否有与该数据相同的信息
     *
     * @param Card
     * @param Time
     * @return 排查到相同为false，不相同true
     */
    private boolean compareCardAndTime(String Card, Date Time) {
        List<String> readList = textUtils.readerText(systemConfig.getTextFilePath());
        for (String s : readList) {
            if (s.equals(Card + "," + DateUtils.TIMEFORMAT.format(Time))) {
                log.info("比对开始,txt内容" + s + "  人像：" + Card + "," + DateUtils.TIMEFORMAT.format(Time));
                return false;
            }
        }
        return true;
    }

    /**
     * 判断日期是否相同
     * @param q
     * @param g
     * @return
     */
    public boolean isTimeStrEquest(Date q, Date g) {
        boolean b = false;

        //直接比对两个时间
        if (q.equals(g)) {
            b = true;
        }

        //转为String比对
        if (q.toString().equals(g.toString())) {
            b = true;
        }

        //为防止不一样，统一格式化为一种格式进行比对
        if (sdf.format(q).equals(sdf.format(g))) {
            b = true;
        }
        log.info("节点日期:" + sdf.format(q) + ",增量数据日期:" + sdf.format(g) + ",结果为:" + b);
        return b;

    }

    /**
     * 根据身份证号查询照片数据
     * @param cardId
     * @return
     */
    @Override
    public byte[] findDataByCardId(String cardId) {
        byte[] data = null;
        List<Data> dataList = photoMapper.selectPhotoByCardId(cardId, systemConfig.getInputDbTalbeName());
        if (dataList != null && dataList.size() > 0) {
            if (dataList.get(0) != null && dataList.get(0).getPhotoFileData() != null) {
                data = dataList.get(0).getPhotoFileData();
            } else {
                TextUtils textUtils = new TextUtils();
                textUtils.writerText(systemConfig.getTextPaht() + File.separator + "ImgDataIsNull.txt", cardId, true);
            }
        }
        return data;
    }

    /**
     * 调起shell脚本进行洞鉴入库
     * @param fileName
     * @param shellPath
     * @param shellConfigPath
     */
    @Override
    public void shellImprotPhoto(String fileName, String shellPath, String shellConfigPath) {
        log.info("开始执行图片及信息入库功能！");

        String command = "bash " + fileName;
        if (shellConfigPath != null || !shellConfigPath.equals("")) {
            command += " " + shellConfigPath;
        }
        log.info("执行命令：" + command);
        try {
            String result = shellUtil.execCmd(command, new File(shellPath));
            log.info("执行结果:\t" + result);
        } catch (Exception e) {
            log.error("调用入库工具发生异常，请联系开发人员,异常：" + e.getMessage());
        }
    }

    /**
     * 用于创建测试数据
     * @param id
     * @param bytes
     * @param cradId
     */
    @Override
    public void testInsert(String id, byte[] bytes, String cradId) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("bytes", bytes);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        map.put("changeTiem", simpleDateFormat.format(new Date()));
        map.put("tableName", systemConfig.getInputDbTalbeName());
        map.put("cradId", cradId);
        photoMapper.testInsert(map);
    }


    @Override
    public List<Photo> findByPhotos(int begin, int end, String date) {
        return photoMapper.selectPhotoByPage(begin, end, date, systemConfig.getInputDbTalbeName());
    }

    public List<Photo> findByPhotosNoOrder(int begin, int end, String date) {
        return photoMapper.selectPhotoByPageNoOrder(begin, end, date, systemConfig.getInputDbTalbeName());
    }



}
