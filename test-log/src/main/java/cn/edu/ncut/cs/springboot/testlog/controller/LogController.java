package cn.edu.ncut.cs.springboot.testlog.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LogController {
    Logger logger = LoggerFactory.getLogger(LogController.class);
    @GetMapping("/test-logback")
    public void testLogback() {
        if(logger.isDebugEnabled()){
            logger.debug("Debug log...");
        }
        logger.trace("Trace log...");
        logger.debug("Debug log...");
        logger.info("Info log...");
        logger.warn("Warn log...");
        logger.error("Error log...");
    }
}