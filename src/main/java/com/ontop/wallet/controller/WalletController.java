package com.ontop.wallet.controller;


import com.ontop.wallet.dto.request.TopUpTransactionRequest;
import com.ontop.wallet.dto.request.WithdrawTransactionRequestDTO;
import com.ontop.wallet.dto.response.WalletBalanceResponseDTO;
import com.ontop.wallet.dto.response.WalletTransactionResponseDTO;
import com.ontop.wallet.model.Transaction;
import com.ontop.wallet.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/wallets")
public class WalletController {

    @Autowired
    private WalletService walletService;

    @GetMapping("/balance")
    public ResponseEntity<WalletBalanceResponseDTO> getBalance(@RequestParam("user_id") Long userId) {
        WalletBalanceResponseDTO balance = walletService.getWalletBalance(userId);
        return new ResponseEntity<>(balance, HttpStatus.OK);
    }

    @PostMapping("/withdraw")
    public ResponseEntity<WalletTransactionResponseDTO> withdraw(@RequestBody WithdrawTransactionRequestDTO withdrawTransactionRequestDTO) {
        WalletTransactionResponseDTO response = walletService.performWithdrawFromWallet(withdrawTransactionRequestDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/top-up")
    public ResponseEntity<WalletTransactionResponseDTO> topUp(@RequestBody TopUpTransactionRequest walletTransactionRequestDTO) {
        WalletTransactionResponseDTO response = walletService.performTopUpToWallet(walletTransactionRequestDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    @GetMapping("/transactions")
    public ResponseEntity<List<Transaction>> getWalletTransactions(@RequestParam(defaultValue = "0") int page,
                                                                   @RequestParam(defaultValue = "10") int size,
                                                                   @RequestParam(required = false) Long amount,
                                                                   @RequestParam(required = false)  String startDate,
                                                                   @RequestParam(required = false)  String endDate) {
        List<Transaction> transactions = walletService.getUserWalletTransactions(amount, startDate, endDate, page, size);
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

}

