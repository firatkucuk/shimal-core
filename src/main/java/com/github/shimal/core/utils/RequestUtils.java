
package com.github.shimal.core.utils;

import com.github.shimal.core.annotations.FieldMap;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import org.springframework.web.context.request.WebRequest;



public class RequestUtils {



    //~ --- [METHODS] --------------------------------------------------------------------------------------------------

    public static String generateOrderClause(WebRequest request, Class cls) {

        String sortedColumnParam = request.getParameter("iSortCol_0");

        if (sortedColumnParam != null && !sortedColumnParam.isEmpty()) {

            String  query         = "";
            Integer sortedColumn  = 0;
            String  sortDirection = request.getParameter("sSortDir_0");
            Class   clazz         = cls;

            try {
                sortedColumn = Integer.parseInt(sortedColumnParam);
            } catch (NumberFormatException e) {
                //
            }

            if (sortDirection == null || sortDirection.isEmpty()) {
                sortDirection = "desc";
            }

            String columnName = request.getParameter("mDataProp_" + sortedColumn);

            if (columnName == null || columnName.isEmpty()) {
                columnName = "";
            }

            columnName =  getColumnName(clazz, columnName);
            query      += columnName;

            if (sortDirection.equalsIgnoreCase("asc")) {
                query += " asc ";
            } else {
                query += " desc ";
            }

            if (!query.isEmpty()) {
                return " order by " + query;
            }
        }

        return "";
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    // TODO: Sistem ilk başladığında tek tarama yapılacak.
    public static String getColumnName(Class clazz, String columnName) {

        Method[] methods = clazz.getMethods();

        for (Method method : methods) {
            Annotation[] annotations = method.getAnnotations();

            String name = String.valueOf(method.getName().charAt(3)).toLowerCase() +
                    (String) method.getName().substring(4);

            for (Annotation annotation : annotations) {

                if (annotation instanceof FieldMap) {
                    FieldMap label = (FieldMap) annotation;

                    if (columnName.equals(name)) {
                        return label.value();
                    }
                }
            }
        }

        return columnName;
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    public static String getSearchTerm(WebRequest request) {

        return request.getParameter("sSearch");
    }
}
