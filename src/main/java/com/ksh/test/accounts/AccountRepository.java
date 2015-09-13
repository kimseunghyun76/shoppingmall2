package com.ksh.test.accounts;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by jooyoung on 2015-09-12.
 */
public interface AccountRepository extends JpaRepository<Account,Long> {
    Account findByUsername(String username);
}
