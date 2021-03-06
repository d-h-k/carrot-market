package com.carrot.account.controller;

import com.carrot.account.domain.Account;
import com.carrot.account.dto.AccountDto;
import com.carrot.account.dto.AccountRequestDto;
import com.carrot.account.dto.AccountResponseDto;
import com.carrot.account.service.AccountService;
import com.carrot.auth.AuthUser;
import com.carrot.auth.CurrentUser;
import com.carrot.common.apiresult.ApiResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping("api/v1/account")
@RestController
@RequiredArgsConstructor
public class AccountApiController {

    private final AccountService accountService;

    private final ModelMapper modelMapper;

    @GetMapping("/me")
    public ResponseEntity<?> getAccount(@CurrentUser Account currentUser) {

        AccountDto accountDto = modelMapper.map(currentUser, AccountDto.class);
        return ApiResult.success(accountDto, HttpStatus.OK).responseBuild();
    }

    @PostMapping
    public ResponseEntity<?> creatAccount(@RequestBody AccountRequestDto request) {

        Account account = accountService.create(request.createAccount());
        return ApiResult.success(new AccountResponseDto(account), HttpStatus.CREATED).responseBuild();
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> readAccount(@PathVariable Long id) {

        Account account = accountService.read(id);
        return ApiResult.success(new AccountResponseDto(account), HttpStatus.OK).responseBuild();
    }

    @GetMapping()
    public ResponseEntity<?> readList(Pageable pageable, @AuthUser Account account) {

        log.info("Account ?????? : {}", account.toString());
        Page<Account> accounts = accountService.readPage(pageable);
        return ApiResult.success(AccountResponseDto.pages(accounts), HttpStatus.OK).responseBuild();
    }
}
