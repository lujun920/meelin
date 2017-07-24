package com.meelin.core.pagehelper.sqlsource;
import com.meelin.core.pagehelper.Constant;
import org.apache.ibatis.builder.SqlSourceBuilder;
import org.apache.ibatis.builder.StaticSqlSource;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.scripting.xmltags.DynamicContext;
import org.apache.ibatis.scripting.xmltags.DynamicSqlSource;
import org.apache.ibatis.scripting.xmltags.SqlNode;
import org.apache.ibatis.session.Configuration;

import java.util.Map;

/**
 * @author liuzh
 */
public class PageDynamicSqlSource extends PageSqlSource implements Constant {
    private Configuration configuration;
    private SqlNode rootSqlNode;

    public PageDynamicSqlSource(DynamicSqlSource sqlSource) {
        MetaObject metaObject = SystemMetaObject.forObject(sqlSource);
        this.configuration = (Configuration) metaObject.getValue("configuration");
        this.rootSqlNode = (SqlNode) metaObject.getValue("rootSqlNode");
    }

    @Override
    protected BoundSql getDefaultBoundSql(Object parameterObject) {
        DynamicContext context = new DynamicContext(configuration, parameterObject);
        rootSqlNode.apply(context);
        SqlSourceBuilder sqlSourceParser = new SqlSourceBuilder(configuration);
        Class<?> parameterType = parameterObject == null ? Object.class : parameterObject.getClass();
        SqlSource sqlSource = sqlSourceParser.parse(context.getSql(), parameterType, context.getBindings());
        sqlSource = new OrderByStaticSqlSource((StaticSqlSource) sqlSource);
        BoundSql boundSql = sqlSource.getBoundSql(parameterObject);
        //设置条件参数
        for (Map.Entry<String, Object> entry : context.getBindings().entrySet()) {
            boundSql.setAdditionalParameter(entry.getKey(), entry.getValue());
        }
        return boundSql;
    }

    @Override
    protected BoundSql getCountBoundSql(Object parameterObject) {
        DynamicContext context = new DynamicContext(configuration, parameterObject);
        rootSqlNode.apply(context);
        SqlSourceBuilder sqlSourceParser = new SqlSourceBuilder(configuration);
        Class<?> parameterType = parameterObject == null ? Object.class : parameterObject.getClass();
        SqlSource sqlSource = sqlSourceParser.parse(context.getSql(), parameterType, context.getBindings());
        BoundSql boundSql = sqlSource.getBoundSql(parameterObject);
        sqlSource = new StaticSqlSource(configuration, localParser.get().getCountSql(boundSql.getSql()), boundSql.getParameterMappings());
        boundSql = sqlSource.getBoundSql(parameterObject);
        //设置条件参数
        for (Map.Entry<String, Object> entry : context.getBindings().entrySet()) {
            boundSql.setAdditionalParameter(entry.getKey(), entry.getValue());
        }
        return boundSql;
    }

    @SuppressWarnings("rawtypes")
    @Override
    protected BoundSql getPageBoundSql(Object parameterObject) {
        DynamicContext context;
        //由于增加分页参数后会修改parameterObject的值，因此在前面处理时备份该值
        //如果发现参数是Map并且包含该KEY，就使用备份的该值
        //解决bug#25:http://git.oschina.net/free/Mybatis_PageHelper/issues/25
        if (parameterObject != null
                && parameterObject instanceof Map
                && ((Map) parameterObject).containsKey(ORIGINAL_PARAMETER_OBJECT)) {
            context = new DynamicContext(configuration, ((Map) parameterObject).get(ORIGINAL_PARAMETER_OBJECT));
        } else {
            context = new DynamicContext(configuration, parameterObject);
        }
        rootSqlNode.apply(context);
        SqlSourceBuilder sqlSourceParser = new SqlSourceBuilder(configuration);
        Class<?> parameterType = parameterObject == null ? Object.class : parameterObject.getClass();
        SqlSource sqlSource = sqlSourceParser.parse(context.getSql(), parameterType, context.getBindings());
        sqlSource = new OrderByStaticSqlSource((StaticSqlSource) sqlSource);
        BoundSql boundSql = sqlSource.getBoundSql(parameterObject);
        sqlSource = new StaticSqlSource(configuration, localParser.get().getPageSql(boundSql.getSql()), localParser.get().getPageParameterMapping(configuration, boundSql));
        boundSql = sqlSource.getBoundSql(parameterObject);
        //设置条件参数
        for (Map.Entry<String, Object> entry : context.getBindings().entrySet()) {
            boundSql.setAdditionalParameter(entry.getKey(), entry.getValue());
        }
        return boundSql;
    }
}