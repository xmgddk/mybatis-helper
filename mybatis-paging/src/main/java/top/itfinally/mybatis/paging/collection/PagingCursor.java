package top.itfinally.mybatis.paging.collection;

import org.apache.ibatis.cursor.Cursor;
import org.springframework.jdbc.core.JdbcTemplate;
import top.itfinally.mybatis.paging.PagingItem;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

/**
 * <pre>
 * *********************************************
 * All rights reserved.
 * Description: ${类文件描述}
 * *********************************************
 *  Version       Date          Author        Desc ( 一句话描述修改 )
 *  v1.0          2018/8/17       itfinally       首次创建
 * *********************************************
 * </pre>
 */
public class PagingCursor<T> extends AbstractPaging implements Cursor<T> {
    private final Cursor<T> cursor;

    public PagingCursor( Cursor<T> cursor, PagingItem pagingItem, List<String> countingSql,
                         Object[] orderedArgs, JdbcTemplate jdbcTemplate, int indexStartingWith ) {
        super( pagingItem, countingSql, orderedArgs, jdbcTemplate, indexStartingWith );

        this.cursor = cursor;
    }

    @Override
    public boolean isOpen() {
        return cursor.isOpen();
    }

    @Override
    public boolean isConsumed() {
        return cursor.isConsumed();
    }

    @Override
    public int getCurrentIndex() {
        return cursor.getCurrentIndex();
    }

    @Override
    public void close() throws IOException {
        cursor.close();
    }

    @Override
    public @Nonnull Iterator<T> iterator() {
        return cursor.iterator();
    }
}
