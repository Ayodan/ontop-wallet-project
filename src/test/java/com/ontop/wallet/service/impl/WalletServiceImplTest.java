package com.ontop.wallet.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ontop.wallet.config.Client;
import com.ontop.wallet.dao.TransactionRepository;
import com.ontop.wallet.dao.WalletRepository;
import com.ontop.wallet.dto.request.TopUpTransactionRequest;
import com.ontop.wallet.dto.request.WithdrawTransactionRequestDTO;
import com.ontop.wallet.dto.response.WalletBalanceResponseDTO;
import com.ontop.wallet.dto.response.WalletTransactionResponseDTO;
import com.ontop.wallet.exception.BadRequestException;
import com.ontop.wallet.exception.NotFoundException;
import com.ontop.wallet.mapper.RequestMapper;
import com.ontop.wallet.mapper.ResponseMapper;
import com.ontop.wallet.model.Transaction;
import com.ontop.wallet.model.Users;
import com.ontop.wallet.model.Wallet;
import com.ontop.wallet.service.UserService;
import com.ontop.wallet.transaction_enum.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Disabled;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {WalletServiceImpl.class})
@ExtendWith(SpringExtension.class)
class WalletServiceImplTest {
    @MockBean
    private Client client;

    @MockBean
    private RequestMapper requestMapper;

    @MockBean
    private ResponseMapper responseMapper;

    @MockBean
    private TransactionRepository transactionRepository;

    @MockBean
    private UserService userService;

    @MockBean
    private WalletRepository walletRepository;

    @Autowired
    private WalletServiceImpl walletServiceImpl;

    @Test
    void testGetWalletByUserId() {
        Wallet wallet = new Wallet();
        wallet.setBalance(BigDecimal.valueOf(42L));
        wallet.setId(1L);
        wallet.setUserId(1L);
        Optional<Wallet> ofResult = Optional.of(wallet);
        when(walletRepository.findByUserId(Mockito.<Long>any())).thenReturn(ofResult);
        Wallet actualWalletByUserId = walletServiceImpl.getWalletByUserId(1L);
        assertSame(wallet, actualWalletByUserId);
        assertEquals("42", actualWalletByUserId.getBalance().toString());
        verify(walletRepository).findByUserId(Mockito.<Long>any());
    }


    @Test
    void testGetWalletByUserThrowNotFoundException() {
        when(walletRepository.findByUserId(Mockito.<Long>any())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> walletServiceImpl.getWalletByUserId(1L));
        verify(walletRepository).findByUserId(Mockito.<Long>any());
    }

    @Test
    void testGetWalletByUserThrowBadRequestException() {
        when(walletRepository.findByUserId(Mockito.<Long>any()))
                .thenThrow(new BadRequestException("INVALID_USER", "An error occurred"));
        assertThrows(BadRequestException.class, () -> walletServiceImpl.getWalletByUserId(1L));
        verify(walletRepository).findByUserId(Mockito.<Long>any());
    }


    @Test
    void testPerformWithdrawFromWalletThrowBadRequestException() {
        assertThrows(BadRequestException.class,
                () -> walletServiceImpl.performWithdrawFromWallet(new WithdrawTransactionRequestDTO()));
    }


    @Test
    void testPerformWithdrawFromWalletThrowBadRequestExceptionForImsufficientBalance() {
        Wallet wallet = new Wallet();
        wallet.setBalance(BigDecimal.valueOf(42L));
        wallet.setId(1L);
        wallet.setUserId(1L);
        Optional<Wallet> ofResult = Optional.of(wallet);
        when(walletRepository.findByUserId(Mockito.<Long>any())).thenReturn(ofResult);
        when(userService.getUserById(Mockito.<Long>any()))
                .thenThrow(new BadRequestException("INSUFFICIENT_WALLET_BALANCE", "An error occurred"));
        assertThrows(BadRequestException.class, () -> walletServiceImpl.performWithdrawFromWallet(
                new WithdrawTransactionRequestDTO("INVALID_BODY", "Doe", "42", "42", "42", BigDecimal.valueOf(42L), 1L)));
        verify(userService).getUserById(Mockito.<Long>any());
    }


       @Test
    void testPerformTopUpToWalletSuccess() {
        Wallet wallet = new Wallet();
        wallet.setBalance(BigDecimal.valueOf(42L));
        wallet.setId(1L);
        wallet.setUserId(1L);
        Optional<Wallet> ofResult = Optional.of(wallet);

        Wallet wallet2 = new Wallet();
        wallet2.setBalance(BigDecimal.valueOf(42L));
        wallet2.setId(1L);
        wallet2.setUserId(1L);
        when(walletRepository.save(Mockito.<Wallet>any())).thenReturn(wallet2);
        when(walletRepository.findByUserId(Mockito.<Long>any())).thenReturn(ofResult);
        Transaction transaction = mock(Transaction.class);
        when(transaction.getId()).thenReturn(1L);
        when(transactionRepository.saveAndFlush(Mockito.<Transaction>any())).thenReturn(transaction);
        when(requestMapper.getTransaction(Mockito.<Long>any(), Mockito.<BigDecimal>any(), Mockito.<String>any(),
                Mockito.<LocalDateTime>any(), Mockito.<String>any())).thenReturn(mock(Transaction.class));
        when(responseMapper.mapWalletTransactionResponse(Mockito.<Long>any(), Mockito.<BigDecimal>any(),
                Mockito.<Long>any())).thenReturn(mock(WalletTransactionResponseDTO.class));

        Users users = new Users();
        users.setFirstName("Jane");
        users.setId(1L);
        users.setSurname("Doe");
        when(userService.getUserById(Mockito.<Long>any())).thenReturn(users);
        walletServiceImpl.performTopUpToWallet(new TopUpTransactionRequest(BigDecimal.valueOf(42L), 1L));
        verify(walletRepository).save(Mockito.<Wallet>any());
        verify(walletRepository).findByUserId(Mockito.<Long>any());
        verify(transactionRepository).saveAndFlush(Mockito.<Transaction>any());
        verify(transaction).getId();
        verify(requestMapper).getTransaction(Mockito.<Long>any(), Mockito.<BigDecimal>any(), Mockito.<String>any(),
                Mockito.<LocalDateTime>any(), Mockito.<String>any());
        verify(responseMapper).mapWalletTransactionResponse(Mockito.<Long>any(), Mockito.<BigDecimal>any(),
                Mockito.<Long>any());
        verify(userService).getUserById(Mockito.<Long>any());
    }


    @Test
    void testGetWalletBalanceSuccess() {
        Wallet wallet = new Wallet();
        wallet.setBalance(BigDecimal.valueOf(42L));
        wallet.setId(1L);
        wallet.setUserId(1L);
        Optional<Wallet> ofResult = Optional.of(wallet);
        when(walletRepository.findByUserId(Mockito.<Long>any())).thenReturn(ofResult);
        when(responseMapper.mapWalletBalanceResponse(Mockito.<Wallet>any()))
                .thenReturn(mock(WalletBalanceResponseDTO.class));
        walletServiceImpl.getWalletBalance(1L);
        verify(walletRepository).findByUserId(Mockito.<Long>any());
        verify(responseMapper).mapWalletBalanceResponse(Mockito.<Wallet>any());
    }


    @Test
    void testGetWalletBalanceThrowNotFoundException() {
        when(walletRepository.findByUserId(Mockito.<Long>any())).thenReturn(Optional.empty());
        when(responseMapper.mapWalletBalanceResponse(Mockito.<Wallet>any()))
                .thenReturn(mock(WalletBalanceResponseDTO.class));
        assertThrows(NotFoundException.class, () -> walletServiceImpl.getWalletBalance(1L));
        verify(walletRepository).findByUserId(Mockito.<Long>any());
    }


    @Test
    void testGetUserWalletTransactionsSuccess() {
        ArrayList<Transaction> transactionList = new ArrayList<>();
        when(transactionRepository.findWalletTransactions(Mockito.<Long>any(), Mockito.<String>any(),
                Mockito.<String>any(), anyInt(), anyInt())).thenReturn(transactionList);
        List<Transaction> actualUserWalletTransactions = walletServiceImpl.getUserWalletTransactions(10L, "2020-03-01",
                "2020-03-01", 1, 3);
        assertSame(transactionList, actualUserWalletTransactions);
        assertTrue(actualUserWalletTransactions.isEmpty());
        verify(transactionRepository).findWalletTransactions(Mockito.<Long>any(), Mockito.<String>any(),
                Mockito.<String>any(), anyInt(), anyInt());
    }


    @Test
    void testCreateTransactionSuccess() {
        when(transactionRepository.saveAndFlush(Mockito.<Transaction>any())).thenReturn(mock(Transaction.class));
        when(requestMapper.getTransaction(Mockito.<Long>any(), Mockito.<BigDecimal>any(), Mockito.<String>any(),
                Mockito.<LocalDateTime>any(), Mockito.<String>any())).thenReturn(mock(Transaction.class));
        walletServiceImpl.createTransaction(1L, BigDecimal.valueOf(42L), "Transaction Status", TransactionType.WITHDRAW);
        verify(transactionRepository).saveAndFlush(Mockito.<Transaction>any());
        verify(requestMapper).getTransaction(Mockito.<Long>any(), Mockito.<BigDecimal>any(), Mockito.<String>any(),
                Mockito.<LocalDateTime>any(), Mockito.<String>any());
    }


    @Test
    void testCreateTransaction4() {
        when(transactionRepository.saveAndFlush(Mockito.<Transaction>any())).thenReturn(mock(Transaction.class));
        when(requestMapper.getTransaction(Mockito.<Long>any(), Mockito.<BigDecimal>any(), Mockito.<String>any(),
                Mockito.<LocalDateTime>any(), Mockito.<String>any())).thenReturn(mock(Transaction.class));
        walletServiceImpl.createTransaction(1L, BigDecimal.valueOf(42L), "Success", TransactionType.WITHDRAW);
        verify(transactionRepository).saveAndFlush(Mockito.<Transaction>any());
        verify(requestMapper).getTransaction(Mockito.<Long>any(), Mockito.<BigDecimal>any(), Mockito.<String>any(),
                Mockito.<LocalDateTime>any(), Mockito.<String>any());
    }

}

