package com.ontop.wallet.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RequestInfoDTO {
    private String status;
    private String error;
}
