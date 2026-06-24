package com.fr.config;

import com.fr.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTask {

    @Autowired
    LogService logService;

    @Scheduled(cron = "0 0 2 * * 1")
    public void cleanLogWeekly() {
        System.out.println("ScheduledTask: 开始每周清理日志...");
        try {
            logService.deleteAll();
            System.out.println("ScheduledTask: 日志清理完成");
        } catch (Exception e) {
            System.out.println("ScheduledTask: 日志清理失败 - " + e.getMessage());
            e.printStackTrace();
        }
    }
}