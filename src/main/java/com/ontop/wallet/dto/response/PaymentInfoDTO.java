package com.ontop.wallet.dto.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentInfoDTO {
    private BigDecimal amount;
    private String id;
}
