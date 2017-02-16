package org.kuneo.sample.test;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.SimpleTransactionStatus;

public class DummyTransactionManager implements PlatformTransactionManager {
  public TransactionStatus getTransaction(TransactionDefinition transactionDefinition)
      throws TransactionException {
    return new SimpleTransactionStatus();
  }

  public void commit(TransactionStatus transactionStatus) throws TransactionException {
  }

  public void rollback(TransactionStatus transactionStatus) throws TransactionException {

  }
}
