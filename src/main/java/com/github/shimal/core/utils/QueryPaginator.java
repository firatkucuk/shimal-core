
package com.github.shimal.core.utils;


public interface QueryPaginator {



    //~ --- [METHODS] --------------------------------------------------------------------------------------------------

    public String transformSql(String query, int pageStart, int pageLength);
}
