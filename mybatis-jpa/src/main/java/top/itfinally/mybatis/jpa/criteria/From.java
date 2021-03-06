package top.itfinally.mybatis.jpa.criteria;

import javax.persistence.criteria.JoinType;

/**
 * <pre>
 * *********************************************
 * All rights reserved.
 * Description: ${类文件描述}
 * *********************************************
 *  Version       Date          Author        Desc ( 一句话描述修改 )
 *  v1.0          2018/9/29       itfinally       首次创建
 * *********************************************
 * </pre>
 */
public interface From<Entity> extends Path<Entity> {

    Join<Entity> join( String attributeName );

    Join<Entity> join( String attributeName, JoinType jt );
}
