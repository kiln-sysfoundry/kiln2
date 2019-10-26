package org.sysfoundry.kiln.formats.data;

public class Response {

    public static final ErrorResponse error(String code,String formatString,Object... inputs){
        return new ErrorResponse(code,String.format(formatString,inputs));
    }
}
