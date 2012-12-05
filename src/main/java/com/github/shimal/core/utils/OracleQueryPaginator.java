
package com.github.shimal.core.utils;


public class OracleQueryPaginator implements QueryPaginator {



    //~ --- [METHODS] --------------------------------------------------------------------------------------------------

    @Override
    public String transformSql(String query, int pageStart, int pageLength) {

        String transformedQuery = "";

        transformedQuery += " SELECT OUTER.*";
        transformedQuery += " FROM (";
        transformedQuery += "   SELECT ROWNUM RN, INNER.*";
        transformedQuery += "   FROM (" + query;
        transformedQuery += "   ) INNER";
        transformedQuery += " ) OUTER";
        transformedQuery += " WHERE OUTER.RN >= " + pageStart + " AND OUTER.RN <= " + (pageStart + pageLength);

        return transformedQuery;
    }
}
