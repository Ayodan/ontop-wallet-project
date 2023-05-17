package com.ontop.wallet.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WithdrawTransactionRequestDTO {
    private String name;
    private String surname;
    private String routingNumber;
    private String nationalIdentificationNumber;
    private String accountNumber;
    private BigDecimal amount;
    @JsonProperty("user_id")
    private Long userId;
}
