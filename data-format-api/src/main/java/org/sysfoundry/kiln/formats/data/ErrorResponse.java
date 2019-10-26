package org.sysfoundry.kiln.formats.data;

import lombok.Value;

@Value
public class ErrorResponse {

    private String code;
    private String message;

}
