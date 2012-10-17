
package com.github.shimal.app.services;

import com.github.shimal.app.daos.GenericDAO;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service
public class BaseService {



    //~ --- [INSTANCE FIELDS] ------------------------------------------------------------------------------------------

    @Autowired
    protected GenericDAO dao;



    //~ --- [METHODS] --------------------------------------------------------------------------------------------------

    @Transactional
    public <T> List<T> paginate(Class<T> cl, String query, Map<String, Object> params) {

        Integer pageStart  = (Integer) params.get("iDisplayStart");
        Integer pageLength = (Integer) params.get("iDisplayLength");

        return dao.select(cl, query, pageStart, pageLength);
    }
}
