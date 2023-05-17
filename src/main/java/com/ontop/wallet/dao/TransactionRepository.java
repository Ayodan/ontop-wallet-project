package com.ontop.wallet.dao;
import com.ontop.wallet.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    @Query(
            value = "SELECT * FROM transaction t WHERE t.amount =?1 and t.created_at >= ?2 and t.created_at <= ?3 ORDER BY created_at DESC LIMIT ?4 OFFSET ?5",
            nativeQuery = true)
    List<Transaction> findWalletTransactions(Long amount, String startDate, String endDate, int page, int size);
}



