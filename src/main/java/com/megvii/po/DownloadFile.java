package com.megvii.po;

import lombok.Data;

@Data
public class DownloadFile {
    private String name;
    private String url;
    private String networkUrl;
    private String suffix;
    private String size;
    public void setSuffix(String suffix) {
        if(suffix.indexOf(".") < 1){
            this.suffix = "."+suffix;
        }else{
            this.suffix = suffix;
        }

    }
}
