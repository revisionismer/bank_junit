package com.bank.domain.transaction;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.springframework.data.repository.query.Param;

import lombok.RequiredArgsConstructor;

interface Dao {
	List<Transaction> findTransactionList(@Param("accountId") Long accountId, @Param("gubun") String gubun, @Param("page") Integer page);
}

// 1-1. 동적 쿼리 작성(JPQL : Jakarta Persistence Query Language)
@RequiredArgsConstructor
public class TransactionRepositoryImpl implements Dao {  // 규칙 : Impl을 꼭 붙여줘야 하고, 기존에 사용하던 jpa repository 이름이 붙어야 한다.

	private final EntityManager em;
	
	@Override
	public List<Transaction> findTransactionList(Long accountId, String gubun, Integer page) {
		// 1-2. JPQL 문법을 이용한 동적쿼리(gubun 값을 가지고 동적 쿼리 = DEPOSIT, WITHDRWA, ALL)
		String sql = "";
		sql += "select t from Transaction t ";
		
		if(gubun.equals("WITHDRAW")) {
			sql += "join fetch t.withdrawAccount wa ";
			sql += "where t.withdrawAccount.id = :withdrawAccountId";
		} else if(gubun.equals("DEPOSIT")){
			sql += "join fetch t.depositAccount da ";
			sql += "where t.depositAccount.id = :depositAccountId";
		} else {
			sql += "left join fetch t.withdrawAccount wa ";
			sql += "left join fetch t.depositAccount da ";
			sql += "where t.withdrawAccount.id = :withdrawAccountId ";
			sql += "or ";
			sql += "t.depositAccount.id = :depositAccountId";
		}
		
		TypedQuery<Transaction> query = em.createQuery(sql, Transaction.class);
		
		if(gubun.equals("WITHDRAW")) {
			query = query.setParameter("withdrawAccountId", accountId);
		} else if(gubun.equals("DEPOSIT")) {
			query = query.setParameter("depositAccountId", accountId);
		} else {
			query = query.setParameter("withdrawAccountId", accountId);
			query = query.setParameter("depositAccountId", accountId);
		}
		
		query.setFirstResult(page*5);  // 5, 10
		query.setMaxResults(5);
		
		return query.getResultList();
	} 
	
	/*
	 *  - outer join(join) -> 모두 다 가져온다.
	 *  - inner join -> null 값이 있는 컬럼은 제외하고 가져온다.
	 */
}
