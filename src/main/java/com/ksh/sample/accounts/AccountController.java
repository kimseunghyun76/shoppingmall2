package com.ksh.sample.accounts;

import com.ksh.sample.commons.ErrorResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by jooyoung on 2015-09-12.
 */

@RestController
public class AccountController {

    @Autowired
    private AccountService service;

    @Autowired
    private AccountRepository repository;

    @Autowired
    private ModelMapper modelMapper;

    // @RequestBody : Message Converter가 동작함
    // 일부만 쓴다면, 내장 클래스 사용해라.
    @RequestMapping(value = "/accounts", method = RequestMethod.POST)
    public ResponseEntity createAccount(
            @RequestBody @Valid AccountDto.Create create, BindingResult result) {

        if (result.hasErrors()) {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setMessage("잘못된 요청입니다.");
            errorResponse.setCode("bad.request");
            //TODO BindingResult 정보 안에 있는 값 활용
            return new ResponseEntity<>(errorResponse,HttpStatus.BAD_REQUEST);
        }

        Account newAccount = service.createAccount(create);


        // onSuccess & onFailure 의 형태로 비동기 타입형태로 처리를 하는 방식도 생긴다.
        return new ResponseEntity<>(modelMapper.map(newAccount,AccountDto.Response.class),HttpStatus.CREATED);
    }

    @ExceptionHandler(UserDuplicatedException.class)
    public ResponseEntity handleUserDuplicatedException(UserDuplicatedException  e){
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage("[" + e.getUsername() + "] 중복된 username 입니다.");
        errorResponse.setCode("duplicated.username.exception");

        return new ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value="/accounts" , method= RequestMethod.GET)
    public ResponseEntity getAccounts(Pageable pageable){
        Page<Account> page = repository.findAll(pageable);
        List<AccountDto.Response> content = page.getContent().parallelStream().map(account -> modelMapper
                .map(account, AccountDto.Response.class))
                .collect(Collectors.toList());
        PageImpl<AccountDto.Response> result = new PageImpl<>(content,pageable,page.getTotalElements());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
