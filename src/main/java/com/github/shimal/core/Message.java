
package com.github.shimal.core;

import java.io.Serializable;



public class Message implements Serializable {



    //~ --- [STATIC FIELDS/INITIALIZERS] -------------------------------------------------------------------------------

    private static final long serialVersionUID = 1L;



    //~ --- [INSTANCE FIELDS] ------------------------------------------------------------------------------------------

    private String html;
    private String style;
    private String value;



    //~ --- [CONSTRUCTORS] ---------------------------------------------------------------------------------------------

    private Message() {

    }



    //~ --- [METHODS] --------------------------------------------------------------------------------------------------

    public static Message error(String msg) {

        Message message = new Message();
        message.value = msg;
        message.html  = "<p><strong>HATA: </strong>" + msg + "</p>";
        message.style = "nFailure";

        return message;
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    public static Message info(String msg) {

        Message message = new Message();
        message.value = msg;
        message.html  = "<p><strong>BİLGİ: </strong>" + msg + "</p>";
        message.style = "nInformation";

        return message;
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    public static Message success(String msg) {

        Message message = new Message();
        message.value = msg;
        message.html  = "<p><strong>TAMAM: </strong>" + msg + "</p>";
        message.style = "nSuccess";

        return message;
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    public static Message warn(String msg) {

        Message message = new Message();
        message.value = msg;
        message.html  = "<p><strong>UYARI: </strong>" + msg + "</p>";
        message.style = "nWarning";

        return message;
    }



    //~ --- [METHODS] --------------------------------------------------------------------------------------------------

    public String getHtml() {

        return html;
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    public String getStyle() {

        return style;
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    public String getValue() {

        return value;
    }
}
