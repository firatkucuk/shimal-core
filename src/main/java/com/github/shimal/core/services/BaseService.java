
package com.github.shimal.core.services;

import com.github.shimal.commons.dao.GenericDAO;
import com.github.shimal.core.utils.QueryPaginator;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service
public class BaseService {



    //~ --- [INSTANCE FIELDS] ------------------------------------------------------------------------------------------

    @Autowired
    protected GenericDAO dao;



    //~ --- [METHODS] --------------------------------------------------------------------------------------------------

    @Transactional
    public <T> List<T> paginate(Class<T> cl, String query, int pageStart, int pageLength) {

        return dao.select(cl, query, pageStart, pageLength);
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    @Transactional
    public <T> List<T> paginate(RowMapper<T> mapper, String query, QueryPaginator queryPaginator, int pageStart,
        int pageLength) {

        return dao.newJdbcTemplate().query(queryPaginator.transformSql(query, pageStart, pageLength), mapper);
    }
}
