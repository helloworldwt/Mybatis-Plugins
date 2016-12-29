package com.my.mybatis.plugins;

import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * @Author: wangtian
 */

@Intercepts(
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})
)
public class SlowSqlItcpt implements Interceptor{

    private static final Logger LOGGER = LogManager.getLogger(SlowSqlItcpt.class);

    /**
     * 执行时间阀值
     */
    private long executeTimeThreshold;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object returnValue = null;
        long start = System.currentTimeMillis();
        returnValue = invocation.proceed();
        long end = System.currentTimeMillis();
        long executeTime = (end - start);
        if (executeTime > executeTimeThreshold) {
            logSql(invocation, executeTime);
        }
        return returnValue;
    }

    protected void logSql(Invocation invocation, long executeTime) {
        StringBuilder str = new StringBuilder(200);
        str.append("slow query:");
        str.append(executeTime);
        str.append("ms");
        str.append(":");
        str.append(((MappedStatement) invocation.getArgs()[0]).getId()); // namespace.method
        str.append(":");
        str.append(getSql(invocation));

        LOGGER.warn(str.toString());
    }

    private static String getParameterValue(Object obj) {
        String value = null;
        if (obj instanceof Date) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            value = dateFormat.format((Date) obj);
        } else {
            if (obj != null) {
                value = obj.toString();
            } else {
                value = "";
            }
        }
        return "'" + value + "'";
    }

    public static String getSql(Invocation invocation) {
        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        Object parameter = null;
        if (invocation.getArgs().length > 1) {
            parameter = invocation.getArgs()[1];
        }
        BoundSql boundSql = mappedStatement.getBoundSql(parameter);
        Configuration configuration = mappedStatement.getConfiguration();
        Object parameterObject = boundSql.getParameterObject();
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        String sql = boundSql.getSql().replaceAll("[\\s]+", " ");
        if (parameterMappings.size() > 0 && parameterObject != null) {
            TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
            if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
                sql = sql.replaceFirst("\\?", getParameterValue(parameterObject));
            } else {
                MetaObject metaObject = configuration.newMetaObject(parameterObject);
                for (ParameterMapping parameterMapping : parameterMappings) {
                    String propertyName = parameterMapping.getProperty();
                    if (metaObject.hasGetter(propertyName)) {
                        Object obj = metaObject.getValue(propertyName);
                        sql = sql.replaceFirst("\\?", getParameterValue(obj));
                    } else if (boundSql.hasAdditionalParameter(propertyName)) {
                        Object obj = boundSql.getAdditionalParameter(propertyName);
                        sql = sql.replaceFirst("\\?", getParameterValue(obj));
                    }
                }
            }
        }
        return sql;
    }

    @Override
    public Object plugin(Object target) {
        if (target instanceof Executor) {
            return Plugin.wrap(target, this);
        } else {
            return target;
        }
    }

    @Override
    public void setProperties(Properties properties) {
        String executeTimeThreshold = properties.getProperty("executeTimeThreshold");
        this.executeTimeThreshold = Long.valueOf(executeTimeThreshold);
    }
}
