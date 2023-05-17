package com.ontop.wallet.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class WalletTransactionResponseDTO {
    @JsonProperty("wallet_transaction_id")
    private Long walletTransactionId;
    private BigDecimal amount;
    @JsonProperty("user_id")
    private Long userId;
}
