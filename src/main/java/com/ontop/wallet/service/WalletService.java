package com.ontop.wallet.service;

import com.ontop.wallet.dto.request.TopUpTransactionRequest;
import com.ontop.wallet.dto.request.WithdrawTransactionRequestDTO;
import com.ontop.wallet.dto.response.WalletBalanceResponseDTO;
import com.ontop.wallet.dto.response.WalletTransactionResponseDTO;
import com.ontop.wallet.model.Transaction;
import com.ontop.wallet.model.Wallet;
import java.util.List;

public interface WalletService {
    Wallet getWalletByUserId(Long userId);
    WalletTransactionResponseDTO performWithdrawFromWallet(WithdrawTransactionRequestDTO withdrawTransactionRequestDTO);
    WalletBalanceResponseDTO getWalletBalance(Long userId);
    WalletTransactionResponseDTO performTopUpToWallet(TopUpTransactionRequest topUpTransactionRequest);
    List<Transaction> getUserWalletTransactions(Long amount, String startDate, String endDate, int page, int size);
}

