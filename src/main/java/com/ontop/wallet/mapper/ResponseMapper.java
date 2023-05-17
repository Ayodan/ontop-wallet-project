package com.ontop.wallet.mapper;

import com.ontop.wallet.dto.request.WithdrawTransactionRequestDTO;
import com.ontop.wallet.dto.response.WalletBalanceResponseDTO;
import com.ontop.wallet.dto.response.WalletTransactionResponseDTO;
import com.ontop.wallet.model.Wallet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class ResponseMapper {

    public WalletTransactionResponseDTO mapWalletTransactionResponse(Long userId, BigDecimal amount, Long transactionId) {
        return WalletTransactionResponseDTO.builder()
                .amount(amount)
                .userId(userId)
                .walletTransactionId(transactionId)
                .build();
    }

    public WalletBalanceResponseDTO mapWalletBalanceResponse(Wallet wallet) {
        return WalletBalanceResponseDTO.builder()
                .balance(wallet.getBalance())
                .userId(wallet.getUserId())
                .build();
    }


}
