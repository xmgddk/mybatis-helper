package top.itfinally.mybatis.paging.collection;

import org.springframework.jdbc.core.JdbcTemplate;
import top.itfinally.mybatis.paging.PagingItem;

import javax.annotation.Nonnull;
import java.util.*;

/**
 * <pre>
 * *********************************************
 * All rights reserved.
 * Description: ${类文件描述}
 * *********************************************
 *  Version       Date          Author        Desc ( 一句话描述修改 )
 *  v1.0          2018/8/12       itfinally       首次创建
 * *********************************************
 * </pre>
 */
public class PagingMap<K, V> extends AbstractPaging implements Map<K, V> {
    private final Map<K, V> originalMap;

    public PagingMap( Map<K, V> originalMap, PagingItem pagingItem, List<String> countingSql,
                      Object[] orderedArgs, JdbcTemplate jdbcTemplate, int indexStartingWith ) {
        super( pagingItem, countingSql, orderedArgs, jdbcTemplate, indexStartingWith );

        this.originalMap = originalMap;
    }

    @Override
    public int size() {
        return originalMap.size();
    }

    @Override
    public boolean isEmpty() {
        return originalMap.isEmpty();
    }

    @Override
    public boolean containsKey( Object key ) {
        return originalMap.containsKey( key );
    }

    @Override
    public boolean containsValue( Object value ) {
        return originalMap.containsValue( value );
    }

    @Override
    public V get( Object key ) {
        return originalMap.get( key );
    }

    @Override
    public V put( K key, V value ) {
        return originalMap.put( key, value );
    }

    @Override
    public V remove( Object key ) {
        return originalMap.remove( key );
    }

    @Override
    public void putAll( @Nonnull Map<? extends K, ? extends V> m ) {
        originalMap.putAll( m );
    }

    @Override
    public void clear() {
        originalMap.clear();
    }

    @Override
    public @Nonnull Set<K> keySet() {
        return originalMap.keySet();
    }

    @Override
    public @Nonnull Collection<V> values() {
        return originalMap.values();
    }

    @Override
    public @Nonnull Set<Entry<K, V>> entrySet() {
        return originalMap.entrySet();
    }

    @Override
    public boolean equals( Object o ) {
        if ( this == o ) return true;
        if ( !( o instanceof PagingMap ) ) return false;
        PagingMap<?, ?> pagingMap = ( PagingMap<?, ?> ) o;
        return Objects.equals( originalMap, pagingMap.originalMap );
    }

    @Override
    public int hashCode() {
        return Objects.hash( originalMap );
    }

    @Override
    public String toString() {
        return originalMap.toString();
    }
}
