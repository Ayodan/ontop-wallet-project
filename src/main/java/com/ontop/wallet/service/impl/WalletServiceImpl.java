package com.ontop.wallet.service.impl;

import com.ontop.wallet.Constant;
import com.ontop.wallet.dto.request.TopUpTransactionRequest;
import com.ontop.wallet.dto.response.WalletBalanceResponseDTO;
import com.ontop.wallet.transaction_enum.TransactionType;
import com.ontop.wallet.config.Client;
import com.ontop.wallet.dao.TransactionRepository;
import com.ontop.wallet.dao.WalletRepository;
import com.ontop.wallet.dto.request.PaymentCreationRequestDTO;
import com.ontop.wallet.dto.request.WithdrawTransactionRequestDTO;
import com.ontop.wallet.dto.response.PaymentCreationResponseDTO;
import com.ontop.wallet.dto.response.WalletTransactionResponseDTO;
import com.ontop.wallet.exception.BadRequestException;
import com.ontop.wallet.exception.InternalServerException;
import com.ontop.wallet.exception.NotFoundException;
import com.ontop.wallet.mapper.RequestMapper;
import com.ontop.wallet.mapper.ResponseMapper;
import com.ontop.wallet.model.Transaction;
import com.ontop.wallet.model.Users;
import com.ontop.wallet.model.Wallet;
import com.ontop.wallet.service.UserService;
import com.ontop.wallet.service.WalletService;
import com.ontop.wallet.util.ObjectMapperUtil;
import com.ontop.wallet.util.TransactionStatusMapperUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;
    private final RequestMapper requestMapper;
    private final ResponseMapper responseMapper;
    private final UserService userService;
    private final Client client;

    public WalletServiceImpl(WalletRepository walletRepository, TransactionRepository transactionRepository, RequestMapper requestMapper, ResponseMapper responseMapper, UserService userService, Client client) {
        this.walletRepository = walletRepository;
        this.transactionRepository = transactionRepository;
        this.requestMapper = requestMapper;
        this.responseMapper = responseMapper;
        this.userService = userService;
        this.client = client;
    }

    public Wallet getWalletByUserId(Long userId) {
        return walletRepository.findByUserId(userId).orElseThrow(() -> new NotFoundException("INVALID_USER", "user not found"));
    }


    @Transactional
    public WalletTransactionResponseDTO performWithdrawFromWallet(WithdrawTransactionRequestDTO request) {
        if (request.getName() == null || request.getAccountNumber() == null || request.getRoutingNumber() == null
                || request.getSurname() == null || request.getNationalIdentificationNumber() == null
                || request.getUserId() == null || request.getAmount() == null) {
            throw new BadRequestException("INVALID_BODY", "input all the required fields");
        }

        userService.getUserById(request.getUserId());

        Wallet wallet = getWalletByUserId(request.getUserId());
        BigDecimal transactionFee = request.getAmount().multiply(BigDecimal.valueOf(0.1));
        BigDecimal totalAmount = request.getAmount().add(transactionFee).abs();
        if (wallet.getBalance().compareTo(totalAmount) < 0) {
            throw new BadRequestException("INSUFFICIENT_WALLET_BALANCE", "not enough wallet balance");
        }

        PaymentCreationResponseDTO paymentCreationResponseDTO = null;
        PaymentCreationRequestDTO paymentCreation = requestMapper.getPaymentCreationRequest(request);
        String ObjectString = ObjectMapperUtil.objectToJson(paymentCreation);
        String responseString = null;

        try {
            responseString = client.transferServiceCall(ObjectString);
        } catch (Exception e) {
            e.printStackTrace();
            throw new InternalServerException("GENERIC_ERROR", "something bad happened");
        }
        paymentCreationResponseDTO = ObjectMapperUtil.jsonToPaymentCreationResponseDTO(responseString);

        if (paymentCreationResponseDTO == null || paymentCreationResponseDTO.getRequestInfo() == null
                || paymentCreationResponseDTO.getRequestInfo().getStatus() == null) {
            throw new InternalServerException("GENERIC_ERROR", "something bad happened");
        }

        BigDecimal newBalance = wallet.getBalance().subtract(totalAmount);
        wallet.setBalance(newBalance);
        walletRepository.save(wallet);
        Transaction transaction = createTransaction(request.getUserId(), request.getAmount(), paymentCreationResponseDTO.getRequestInfo().getStatus(), TransactionType.WITHDRAW);
        return responseMapper.mapWalletTransactionResponse(request.getUserId(), request.getAmount(), transaction.getId());
    }


    public WalletTransactionResponseDTO performTopUpToWallet(TopUpTransactionRequest request) {
        if (request.getUserId() == null || request.getAmount() == null)
            throw new BadRequestException("INVALID_BODY", "amount and user_id must not be null");

        Wallet wallet = getWalletByUserId(request.getUserId());
        if (wallet == null)
            throw new NotFoundException("NO_WALLET", "Wallet not found");
        Users users = userService.getUserById(request.getUserId());
        if (users == null)
            throw new NotFoundException("INVALID_USER", "user not found");
        BigDecimal newBalance = wallet.getBalance().add(request.getAmount());
        wallet.setBalance(newBalance);
        walletRepository.save(wallet);
        Transaction transaction = createTransaction(request.getUserId(), request.getAmount(), Constant.SUCCESS, TransactionType.TOP_UP);
        return responseMapper.mapWalletTransactionResponse(request.getUserId(), request.getAmount(), transaction.getId());

    }


    public WalletBalanceResponseDTO getWalletBalance(Long userId) {
        Optional<Wallet> wallet = walletRepository.findByUserId(userId);
        if (wallet.isEmpty())
            throw new NotFoundException("INVALID_USER", "user not found");
        return responseMapper.mapWalletBalanceResponse(wallet.get());
    }


    public List<Transaction> getUserWalletTransactions(Long amount, String startDate, String endDate, int page, int size) {
        return transactionRepository.findWalletTransactions(amount, startDate, endDate, page, size);
    }


    public Transaction createTransaction(Long userId, BigDecimal amount, String transactionStatus, TransactionType transactionType) {
        LocalDateTime now = LocalDateTime.now();
        String mappedTransactionStatus = TransactionStatusMapperUtil.mapTransactionStatus(transactionStatus).name();
        Transaction transaction = requestMapper.getTransaction(userId, amount, mappedTransactionStatus, now, transactionType.name());
        return transactionRepository.saveAndFlush(transaction);
    }

}






