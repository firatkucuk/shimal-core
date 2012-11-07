
package com.github.shimal.core;


import com.github.shimal.core.annotations.FieldMap;
import com.github.shimal.query_utils.Constrainable;
import com.github.shimal.query_utils.Querable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.web.context.request.WebRequest;

import static com.github.shimal.query_utils.hql.HqlQueryUtils.like;
import static com.github.shimal.query_utils.hql.HqlQueryUtils.or;



public class ListAdapter {



    //~ --- [INSTANCE FIELDS] ------------------------------------------------------------------------------------------

    private Class                clazz;
    private Map<String, Mapping> mappings;
    private Querable             query;
    private WebRequest           request;



    //~ --- [CONSTRUCTORS] ---------------------------------------------------------------------------------------------

    public ListAdapter(Class clazz, Querable query, WebRequest request) {

        this.clazz    = clazz;
        this.query    = query;
        this.request  = request;
        this.mappings = new HashMap<String, Mapping>();

        findMappings();
    }



    //~ --- [METHODS] --------------------------------------------------------------------------------------------------

    public ListAdapter filter() {

        String term = request.getParameter("sSearch");

        if (term != null && !term.isEmpty()) {

            List<Constrainable> constraintList = new ArrayList<Constrainable>();

            for (String fieldName : mappings.keySet()) {
                Mapping mapping = mappings.get(fieldName);

                boolean isNumber = false;

                isNumber = isNumber || byte.class.equals(mapping.returnType);
                isNumber = isNumber || short.class.equals(mapping.returnType);
                isNumber = isNumber || int.class.equals(mapping.returnType);
                isNumber = isNumber || long.class.equals(mapping.returnType);
                isNumber = isNumber || float.class.equals(mapping.returnType);
                isNumber = isNumber || double.class.equals(mapping.returnType);
                isNumber = isNumber || Number.class.isAssignableFrom(mapping.returnType);

                if (isNumber) {
                    constraintList.add(like("str(" + mapping.fieldMap + ")", "'%" + term + "%'"));
                } else if (mapping.returnType.equals(String.class)) {
                    constraintList.add(like(mapping.fieldMap, "'%" + term + "%'"));
                } else if (mapping.returnType.equals(Anchor.class)) {
                    constraintList.add(like(mapping.fieldMap, "'%" + term + "%'"));
                }
            }

            Constrainable[] constraints = new Constrainable[constraintList.size()];

            constraintList.toArray(constraints);
            query.where(or(constraints));
        }

        return this;
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    public ListAdapter order() {

        String paramSortedColum = request.getParameter("iSortCol_0");

        if (paramSortedColum != null && !paramSortedColum.isEmpty()) {

            Integer sortedColumn       = 0;
            Integer sortDirection      = Querable.ORDER_ASCENDING;
            String  paramSortDirection = request.getParameter("sSortDir_0");

            try {
                sortedColumn = Integer.parseInt(paramSortedColum);
            } catch (NumberFormatException e) {
                //
            }

            if (paramSortDirection != null && !paramSortDirection.isEmpty()) {

                if (paramSortDirection.equals("desc")) {
                    sortDirection = Querable.ORDER_DESCENDING;
                }
            }

            String columnName = request.getParameter("mDataProp_" + sortedColumn);

            if (columnName != null && !columnName.isEmpty()) {
                Mapping mapping = mappings.get(columnName);

                if (mapping != null) {

                    if (sortDirection == Querable.ORDER_ASCENDING) {
                        query.asc(mapping.fieldMap);
                    } else {
                        query.desc(mapping.fieldMap);
                    }

                    if (mapping.secondOrder != null && !mapping.secondOrder.isEmpty()) {
                        query.asc(mapping.secondOrder);
                    }
                }
            }
        }

        return this;
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    private void findMappings() {

        Method[] methods = clazz.getDeclaredMethods();

        for (Method method : methods) {

            if (method.getName().startsWith("get")) {
                String   methodName = method.getName();
                Class    returnType = method.getReturnType();
                String   fieldName  = String.valueOf(methodName.charAt(3)).toLowerCase() + methodName.substring(4);
                String[] fieldMap   = getFieldMap(method, fieldName);

                if (fieldMap != null) {
                    mappings.put(fieldName, new Mapping(fieldMap[0], fieldMap[1], returnType));
                }
            }
        }
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    private String[] getFieldMap(Method method, String fieldName) {

        Annotation[] annotations = method.getAnnotations();

        for (Annotation annotation : annotations) {

            if (annotation instanceof FieldMap) {
                FieldMap fieldMap = (FieldMap) annotation;

                return fieldMap.ignore() ? null : new String[] { fieldMap.value(), fieldMap.secondOrder() };
            }
        }

        return new String[] { "s." + fieldName, "" };
    }



    //~ --- [INNER CLASSES] --------------------------------------------------------------------------------------------

    private class Mapping {



        //~ --- [INSTANCE FIELDS] --------------------------------------------------------------------------------------

        private String fieldMap;
        private Class  returnType;
        private String secondOrder;



        //~ --- [CONSTRUCTORS] -----------------------------------------------------------------------------------------

        public Mapping(String fieldMap, String secondOrder, Class returnType) {

            this.fieldMap    = fieldMap;
            this.secondOrder = secondOrder;
            this.returnType  = returnType;
        }
    }
}
