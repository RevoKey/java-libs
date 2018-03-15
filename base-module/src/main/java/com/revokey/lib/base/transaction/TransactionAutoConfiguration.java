package com.revokey.lib.base.transaction;

import org.apache.commons.lang3.StringUtils;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.interceptor.TransactionInterceptor;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.List;
import java.util.Properties;

/**
 * @Name: TransactionAutoConfiguration
 * @Description: 事务配置类
 * @author RevoKey
 * @date 2018/3/16 15:44
 */
@Configuration
@EnableTransactionManagement
@ConfigurationProperties(prefix = "transaction")
@ConditionalOnExpression("${transaction.enabled:true}")
public class TransactionAutoConfiguration {
    private List<String> transactionAttributes;

    @Resource(name = "dataSource")
    private DataSource dataSource;

    @Bean
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "transactionInterceptor")
    public TransactionInterceptor transactionInterceptor(PlatformTransactionManager ptm) {
        Properties transactionProperties = new Properties();
        if (transactionAttributes != null && !transactionAttributes.isEmpty()) {
            for (String attribute : transactionAttributes) {
                if (StringUtils.isNotBlank(attribute)) {
                    String[] values = attribute.split("=", 2);
                    if (values.length == 2) {
                        transactionProperties.setProperty(values[0].trim(), values[1].trim());
                    }
                }
            }
        } else {
            transactionProperties.setProperty( "insert*", "PROPAGATION_REQUIRED,-Throwable" );
            transactionProperties.setProperty( "save*", "PROPAGATION_REQUIRED,-Throwable" );
            transactionProperties.setProperty( "update*", "PROPAGATION_REQUIRED,-Throwable" );
            transactionProperties.setProperty( "delete*", "PROPAGATION_REQUIRED,-Throwable" );
            transactionProperties.setProperty( "del*", "PROPAGATION_REQUIRED,-Throwable" );
            transactionProperties.setProperty( "select*", "readOnly" );
            transactionProperties.setProperty( "query*", "readOnly" );
            transactionProperties.setProperty( "find*", "readOnly" );
            transactionProperties.setProperty( "get*", "readOnly" );
        }
        return new TransactionInterceptor(ptm, transactionProperties);
    }

    @Bean
    public DefaultPointcutAdvisor defaultPointcutAdvisor(AspectJExpressionPointcut pointcut, TransactionInterceptor interceptor) {
        return new DefaultPointcutAdvisor(pointcut, interceptor);
    }

    @Bean
    public AspectJExpressionPointcut aspectJExpressionPointcut() {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression("execution(* *..services..*.*(..)) and !@within(com.revokey.lib.base.annotations.NoDynamicDataSource)");
        return pointcut;
    }

    public List<String> getTransactionAttributes() {
        return transactionAttributes;
    }

    public void setTransactionAttributes(List<String> transactionAttributes) {
        this.transactionAttributes = transactionAttributes;
    }
}
