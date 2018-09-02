package top.itfinally.mybatis.jpa.sql;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.scripting.xmltags.XMLLanguageDriver;
import org.apache.ibatis.session.Configuration;
import top.itfinally.mybatis.jpa.entity.AttributeMetadata;
import top.itfinally.mybatis.jpa.entity.EntityMetadata;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static top.itfinally.mybatis.jpa.mapper.BasicCrudMapper.ENTITY;

/**
 * <pre>
 * *********************************************
 * All rights reserved.
 * Description: ${类文件描述}
 * *********************************************
 *  Version       Date          Author        Desc ( 一句话描述修改 )
 *  v1.0          2018/8/24       itfinally       首次创建
 * *********************************************
 *
 * 配合 BasicCrudMapper 上的注解使用
 *
 * </pre>
 */
public abstract class BasicCrudSqlCreator {
    private final XMLLanguageDriver languageDriver = new XMLLanguageDriver();

    protected final Configuration configuration;

    protected BasicCrudSqlCreator( Configuration configuration ) {
        this.configuration = configuration;
    }

    protected BoundSql buildBoundSql( Object unknownArgs, Function<XMLLanguageDriver, SqlSource> sqlBuilder ) {
        // 这里添加 mybatis 原生 sql, 即带上 #{} 标记的 sql
        // 这里已知 foreach 标签会将原来的 sql 修改, 参考 ForEachSqlNode 类
        // 针对动态 sql, mybatis 是先将字面 sql 解析为各个节点对象, 然后创建 DynamicSqlSource, 该 sql 源实例会根据实际参数生成不同的 sql
        return sqlBuilder.apply( languageDriver ).getBoundSql( unknownArgs );
    }

    protected Object getSpecifyParameter( String name, Object unknownArgs ) {
        if ( unknownArgs instanceof MapperMethod.ParamMap ) {
            Map map = ( ( MapperMethod.ParamMap ) unknownArgs );
            return map.containsKey( name ) ? map.get( name ) : Object.class;
        }

        return unknownArgs;
    }

    protected Class<?> getSpecifyParameterType( String name, Object unknownArgs ) {
        Object result = getSpecifyParameter( name, unknownArgs );
        return result != null ? result.getClass() : Object.class;
    }

    protected InsertPair createInsertFieldAndValues( EntityMetadata metadata, Object applier, boolean isNonnull ) {
        List<String> fields = new ArrayList<>();
        List<String> values = new ArrayList<>();

        for ( AttributeMetadata attr : metadata.getColumns() ) {
            if ( isNonnull && isNullValue( applier, attr ) ) {
                continue;
            }

            fields.add( attr.getJdbcName() );
            values.add( String.format( "#{%s.%s}", ENTITY, attr.getJavaName() ) );
        }

        return new InsertPair( Joiner.on( "," ).join( fields ), Joiner.on( "," ).join( values ) );
    }

    protected static class InsertPair {
        private final String fields;
        private final String values;

        public InsertPair( String fields, String values ) {
            this.fields = fields;
            this.values = values;
        }

        public String getFields() {
            return fields;
        }

        public String getValues() {
            return values;
        }
    }

    protected String createUpdateFieldAndValues( EntityMetadata metadata, Object applier, boolean isNonnull ) {
        List<String> pairs = new ArrayList<>();

        for ( AttributeMetadata attr : metadata.getColumns() ) {
            if ( attr.isPrimary() || ( isNonnull && isNullValue( applier, attr ) ) ) {
                continue;
            }

            pairs.add( String.format( "%s = #{%s.%s}", attr.getJdbcName(), ENTITY, attr.getJavaName() ) );
        }

        return Joiner.on( ", " ).join( pairs );
    }

    protected void assertInExpressionNotEmpty( @SuppressWarnings( "all" ) String argName, String methodName, Object unknownArgs ) {
        Object result = getSpecifyParameter( argName, unknownArgs );
        if ( ( result instanceof Collection && ( ( Collection ) result ).isEmpty() ) ) {
            throw new IllegalArgumentException( String.format( "The given collection is empty, method: %s parameter: %s", methodName, argName ) );
        }
    }

    private boolean isNullValue( Object applier, AttributeMetadata attr ) {
        try {
            return null == attr.getReadMethod().invoke( applier );

        } catch ( IllegalAccessException | InvocationTargetException e ) {
            throw new RuntimeException( String.format( "Can read attribute '%s' from field of entity '%s'",
                    attr.getJavaName(), attr.getField().getDeclaringClass() ), e );
        }
    }

    public abstract BoundSql queryByIdIs( EntityMetadata metadata, Object unknownArgs );

    public abstract BoundSql queryByIdIn( EntityMetadata metadata, Object unknownArgs );

    public abstract BoundSql queryAll( EntityMetadata metadata );

    public abstract BoundSql existByIdIs( EntityMetadata metadata, Object unknownArgs );

    // 针对不支持 multi values insert 操作的数据库, 可以尝试以下语句
    // insert into table( fields... ) select #{values}... union all select #{values}... union all ...
    public abstract BoundSql save( EntityMetadata metadata, Object unknownArgs );

    public abstract BoundSql saveWithNonnull( EntityMetadata metadata, Object unknownArgs );

    public abstract BoundSql saveAll( EntityMetadata metadata, Object unknownArgs );

    public abstract BoundSql updateByIdIs( EntityMetadata metadata, Object unknownArgs );

    public abstract BoundSql updateWithNonnullByIdIs( EntityMetadata metadata, Object unknownArgs );

    public abstract BoundSql deleteByIdIs( EntityMetadata metadata, Object unknownArgs );

    public abstract BoundSql deleteAllByIdIn( EntityMetadata metadata, Object unknownArgs );
}
