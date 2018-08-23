package com.megvii.po;

import lombok.Data;

import java.sql.Blob;
import java.sql.Date;
import java.sql.Timestamp;

@Data
public class Photo {

    //名称
    String name;

    //身份证号
    String cardId;

    //数据修改时间
    Timestamp changeTime;

    //性别
    String sex;

    //民族
    String ethnic;

    //户籍
    String HJ;

    //数据来源
    String dataSource;
}
