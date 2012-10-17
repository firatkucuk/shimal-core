
package com.github.shimal.core.daos;

import java.io.Serializable;
import java.util.List;
import javax.sql.DataSource;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;



public interface GenericDAO {



    //~ --- [METHODS] --------------------------------------------------------------------------------------------------

    @Transactional
    public long count(String hql);



    //~ ----------------------------------------------------------------------------------------------------------------

    @Transactional
    public <T> long count(Class<T> c, String query);



    //~ ----------------------------------------------------------------------------------------------------------------

    @Transactional
    public <T> long count(Class<T> c, String query, Object value);



    //~ ----------------------------------------------------------------------------------------------------------------

    @Transactional
    public <T> long count(Class<T> c, String query, Object... values);



    //~ ----------------------------------------------------------------------------------------------------------------

    @Transactional
    public <T> long countAll(Class<T> c);



    //~ ----------------------------------------------------------------------------------------------------------------

    @Transactional
    public <T> void delete(T item);



    //~ ----------------------------------------------------------------------------------------------------------------

    @Transactional
    public <T> List<T> find(Class<T> c, String query);



    //~ ----------------------------------------------------------------------------------------------------------------

    @Transactional
    public <T> List<T> find(Class<T> c, String query, Object value);



    //~ ----------------------------------------------------------------------------------------------------------------

    @Transactional
    public <T> List<T> find(Class<T> c, String query, Object... values);



    //~ ----------------------------------------------------------------------------------------------------------------

    @Transactional
    public <T> List<T> findAll(Class<T> c);



    //~ ----------------------------------------------------------------------------------------------------------------

    @Transactional
    public <T> List<T> findByCriteria(DetachedCriteria criterion);



    //~ ----------------------------------------------------------------------------------------------------------------

    @Transactional
    public <T> List<T> findByCriteria(DetachedCriteria criterion, int firstResult, int maxResults);



    //~ ----------------------------------------------------------------------------------------------------------------

    @Transactional
    public <T> List<T> findByExample(T exampleEntity, int firstResult, int maxResults);



    //~ ----------------------------------------------------------------------------------------------------------------

    @Transactional
    public <T> List<T> findByNamedParam(String queryString, String[] paramNames, Object[] values);



    //~ ----------------------------------------------------------------------------------------------------------------

    @Transactional
    public <T> T first(Class<T> c);



    //~ ----------------------------------------------------------------------------------------------------------------

    @Transactional
    public <T> T first(Class<T> c, String query);



    //~ ----------------------------------------------------------------------------------------------------------------

    @Transactional
    public <T> T first(Class<T> c, String query, Object value);



    //~ ----------------------------------------------------------------------------------------------------------------

    @Transactional
    public <T> T first(Class<T> c, String query, Object... values);



    //~ ----------------------------------------------------------------------------------------------------------------

    @Transactional
    public <T> T get(Class<T> c, Serializable id);



    //~ ----------------------------------------------------------------------------------------------------------------

    @Transactional
    public <T> Criterion getCriteria(Class<T> c);



    //~ ----------------------------------------------------------------------------------------------------------------

    @Transactional
    public JdbcTemplate newJdbcTemplate();



    //~ ----------------------------------------------------------------------------------------------------------------

    @Transactional
    public <T> void persist(T item) throws DataAccessException;



    //~ ----------------------------------------------------------------------------------------------------------------

    @Transactional
    public <T> T save(T item);



    //~ ----------------------------------------------------------------------------------------------------------------

    @Transactional
    public <T> List<T> select(Class<T> c, String hql);



    //~ ----------------------------------------------------------------------------------------------------------------

    @Transactional
    public <T> List<T> select(Class<T> c, String hql, int firstResult, int maxResults);



    //~ ----------------------------------------------------------------------------------------------------------------

    public void setDataSource(DataSource dataSource);



    //~ ----------------------------------------------------------------------------------------------------------------

    @Transactional
    public <T> List<T> take(Class<T> c, int maxResultCount);



    //~ ----------------------------------------------------------------------------------------------------------------

    @Transactional
    public <T> List<T> take(Class<T> c, int maxResultCount, String query);



    //~ ----------------------------------------------------------------------------------------------------------------

    @Transactional
    public <T> List<T> take(Class<T> c, int maxResultCount, String query, Object value);



    //~ ----------------------------------------------------------------------------------------------------------------

    @Transactional
    public <T> List<T> take(Class<T> c, int maxResultCount, String query, Object... values);
}
