package com.megvii.po;

import lombok.Data;

@Data
public class DownloadFileConfig {

    private  String filePath;

    private  String suffix;

    private  String maxSize;

    private  String maxFolder;

    private  String folderMaxFile;

}
