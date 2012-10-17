package com.github.shimal.core;


import java.io.Serializable;



public class Anchor implements Serializable {



    //~ --- [STATIC FIELDS/INITIALIZERS] -------------------------------------------------------------------------------

    private static final long serialVersionUID = 1L;



    //~ --- [INSTANCE FIELDS] ------------------------------------------------------------------------------------------

    private String id;
    private String text;



    //~ --- [CONSTRUCTORS] ---------------------------------------------------------------------------------------------

    public Anchor(String text) {

        this.text = text;
    }



    public Anchor(Serializable id, String text) {

        this.id   = id.toString();
        this.text = text;
    }



    //~ --- [METHODS] --------------------------------------------------------------------------------------------------

    public String getId() {

        return id;
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    public String getText() {

        return text;
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    @Override
    public String toString() {

        if (id == null) {
            return text;
        }

        return "<a href=\"#" + id + "\">" + text + "</a>";
    }
}
