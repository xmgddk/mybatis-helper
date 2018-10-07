package top.itfinally.mybatis.jpa.criteria.query;

import com.google.common.collect.Lists;
import top.itfinally.mybatis.jpa.criteria.Expression;
import top.itfinally.mybatis.jpa.criteria.Reference;
import top.itfinally.mybatis.jpa.criteria.Root;
import top.itfinally.mybatis.jpa.criteria.path.ExpressionImpl;

import javax.persistence.criteria.Order;
import java.util.Collection;
import java.util.List;

/**
 * <pre>
 * *********************************************
 * All rights reserved.
 * Description: ${类文件描述}
 * *********************************************
 *  Version       Date          Author        Desc ( 一句话描述修改 )
 *  v1.0          2018/10/2       itfinally       首次创建
 * *********************************************
 * </pre>
 */
public class CriteriaSubQueryImpl<Entity> extends ExpressionImpl<Entity> implements SubQuery<Entity> {

    private final AbstractSubQuery parent;
    private final QueryCollector queryCollector;

    public CriteriaSubQueryImpl( CriteriaBuilder builder, Class<Entity> entityClass, AbstractSubQuery parentQuery ) {
        super( builder, null );

        this.parent = parentQuery;
        this.queryCollector = new QueryCollector( criteriaBuilder(), ( AbstractQuery<?> ) parentQuery );
    }

    @Override
    protected QueryCollector queryCollector() {
        return queryCollector;
    }

    @Override
    public SubQuery<Entity> select( Reference<?> path ) {
        queryCollector().addSelection( Lists.<Reference<?>>newArrayList( path ) );
        return this;
    }

    @Override
    public SubQuery<Entity> select( Collection<Reference<?>> path ) {
        queryCollector().addSelection( path );
        return this;
    }

    @Override
    public <X> Root<X> from( Class<X> entityClass ) {
        return queryCollector().from( entityClass );
    }

    @Override
    public SubQuery<Entity> where( Expression<Boolean> restriction ) {
        queryCollector().addCondition( Lists.newArrayList( restriction ) );
        return this;
    }

    @Override
    public SubQuery<Entity> where( List<Expression<Boolean>> restrictions ) {
        queryCollector().addCondition( restrictions );
        return this;
    }

    @Override
    public SubQuery<Entity> groupBy( Reference<?> restriction ) {
        queryCollector().addGrouping( Lists.<Reference<?>>newArrayList( restriction ) );
        return this;
    }

    @Override
    public SubQuery<Entity> having( Expression<Boolean> restriction ) {
        return this;
    }

    @Override
    public SubQuery<Entity> orderBy( Order order ) {
        return null;
    }

    @Override
    public SubQuery<Entity> orderBy( List<Order> orders ) {
        return null;
    }

    @Override
    public <T> SubQuery<T> subQuery( Class<T> entityClass ) {
        return queryCollector().subQuery( entityClass );
    }
}