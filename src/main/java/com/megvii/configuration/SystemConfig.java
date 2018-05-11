package com.megvii.configuration;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
@Data
public class SystemConfig {

    Boolean TimerOnOff = false;

    @Value("${query.data.number}")
    Integer queryMaxSize;

    @Value("${shell.path}")
    String shellPath;

    @Value("${shell.config.path}")
    String shellConfigPath;

    @Value("${shell.file.name}")
    String fileName;

    String textPaht= SystemConfig.class.getResource("/").getPath()+".."+File.separator+".."+File.separator+"continuingly.txt";
}
