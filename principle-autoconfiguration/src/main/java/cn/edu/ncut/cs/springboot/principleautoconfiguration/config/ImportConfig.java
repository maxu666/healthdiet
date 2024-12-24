package cn.edu.ncut.cs.springboot.principleautoconfiguration.config;

import ch.qos.logback.core.helpers.NOPAppender;
import cn.edu.ncut.cs.springboot.principleautoconfiguration.bean.User;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @Import({User.class, NOPAppender.class})
 *    给容器中自动注册两个组件(无参构造函数)
 */
@Import({User.class, NOPAppender.class})
@Configuration
public class ImportConfig {
}
