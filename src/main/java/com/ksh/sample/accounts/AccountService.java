package com.ksh.sample.accounts;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * Created by jooyoung on 2015-09-12.
 */
@Service
@Transactional
public class AccountService {

    @Autowired
    private AccountRepository repository;

    @Autowired
    private ModelMapper modelMapper;


    public Account createAccount(AccountDto.Create dto) {
        Account account = modelMapper.map(dto, Account.class);
        //TODO 유효한 사용자 이름 판단.
        String username = dto.getUserName();
        if(repository.findByUsername(username) != null){
            throw new UserDuplicatedException(username);
        }

        //TODO password hashing
        Date now = new Date();
        account.setJoined(now);
        account.setUpdated(now);

        return repository.save(account);
    }
}
