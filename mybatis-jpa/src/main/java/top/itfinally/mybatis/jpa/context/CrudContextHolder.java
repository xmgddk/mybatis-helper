package top.itfinally.mybatis.jpa.context;

import top.itfinally.mybatis.core.ThreadLocalDelegatedFactory;
import top.itfinally.mybatis.jpa.entity.EntityMetadata;

import java.lang.reflect.Method;

import static top.itfinally.mybatis.jpa.context.CrudContextHolder.ContextType.GENERAL;

/**
 * <pre>
 * *********************************************
 * All rights reserved.
 * Description: ${类文件描述}
 * *********************************************
 *  Version       Date          Author        Desc ( 一句话描述修改 )
 *  v1.0          2018/8/24       itfinally       首次创建
 * *********************************************
 * </pre>
 */
public class CrudContextHolder {
    private static ThreadLocal<Context> contextThreadLocal = ThreadLocalDelegatedFactory.newThreadLocal();

    private CrudContextHolder() {
    }

    public static void buildEntityAndSetContext( Class<?> entityClass, Method method ) {
        contextThreadLocal.set( new Context( GENERAL, MetadataFactory.getMetadata( entityClass ), method ) );
    }

    public static void setContext( Context context ) {
        contextThreadLocal.set( context );
    }

    public static Context getContext() {
        return contextThreadLocal.get();
    }

    public static void clear() {
        contextThreadLocal.remove();
    }

    public static class Context {
        private final ContextType contextType;
        private final EntityMetadata metadata;
        private final Method method;

        public Context( ContextType contextType, EntityMetadata metadata, Method method ) {
            this.contextType = contextType;
            this.metadata = metadata;
            this.method = method;
        }

        public ContextType getContextType() {
            return contextType;
        }

        public EntityMetadata getMetadata() {
            return metadata;
        }

        public Method getMethod() {
            return method;
        }
    }

    public static enum ContextType {
        JPA, GENERAL
    }
}
