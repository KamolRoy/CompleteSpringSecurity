  package com.comolroy.helloworld;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@ImportResource("classpath:/spring-config.xml")
//@ComponentScan
/*
 * Indicates a configuration class that declares one or more @Bean methods 
 * and also triggers auto-configuration and component scanning. This is a 
 * convenience annotation that is equivalent to declaring @Configuration, 
 * @EnableAutoConfiguration and @ComponentScan. 
 */
//@SpringBootApplication
@EnableAutoConfiguration

/*
 * Enables Spring's annotation-driven transaction management capability
 */
@EnableTransactionManagement
public class Application {

	private static final Logger logger= LoggerFactory.getLogger(Application.class);
	
	public static void main(String[] args) {
		ApplicationContext ctx= SpringApplication.run(Application.class, args);
		
		/*
		 * The following code use to list the bean created in application context.
		 */
		logger.info("Beans in application context:");
		
		String beanNames[] = ctx.getBeanDefinitionNames();
		
		Arrays.sort(beanNames);
		
		for (String beanName : beanNames) {
			logger.info(beanName);
		}
	}
}
