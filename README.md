# shoppingmall2

백기선 님의 아무거나 (Spring Boot + Spring Data(JPA) + )
동영상 : https://www.youtube.com/watch?v=l7Xi2p080dE
GITHub : https://github.com/keesun/amugona/

1. 파라미터에서 request / response에 직접 해당 도메인을 활용하는 것 보다, DTO를 생성하여, Inner 클래스를 활용해라. 
  그래야 보다 깔끔하고, password 같은 것에 @ignore 같은 것을 쓰지 않고도 깔끔하게 구현할수 있다. 

2. 전에 A a = new A(); 하고 a.setName(b.name); a.setAge(a.age); 이렇게 마구마구 넣는데..
   ModelMapper 를 사용해서, setter 활용해서 값을 넣어주는 것을 한번해 해결한다.
  이것과 더불어 BeanUtils.copyProperties 를 활용해도 좋더라.. 
   e.g. Account account = modelMapper.map(AccountDto.Create dto, Account.class); 이렇게 말이죠.

3. @Valid 등으로 해당 도메인 상에 객체를 검사하고, 그에 대한 결과 값은 BindingResult 로 받는다.
        if (bindlingResult.hasErrors()) {
            // 에러 응답 본문 추가하기
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

4. Controller에서는 ResponseEntity에 httpStatus.created 와 해당 생성 도메인 정보를 넣어주세요 
  new ResponseEntity<>(modelMapper.map(newAccount,AccountDto.Response.class),HttpStatus.CREATED);
  return new ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST);

5. JPA를 간단하게 적용.
  1) Table 구성과 함께 DO 생성
  2) JpaRepository<DO,TYPE>을 구현한 Repository 인터페이스 생성합니다.  
  3) 원하는 것을 생성함 . Account findByUsername(String username);  
  4) 페이징 해봐
    @RequestMapping(value="/accounts" , method= RequestMethod.GET)
    public ResponseEntity getAccounts(Pageable pageable){
        Page<Account> page = repository.findAll(pageable);
        List<AccountDto.Response> content = page.getContent().parallelStream().map(account -> modelMapper
                .map(account, AccountDto.Response.class))
                .collect(Collectors.toList());
        PageImpl<AccountDto.Response> result = new PageImpl<>(content,pageable,page.getTotalElements());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
그럼 아래와 같이 잘 나온데요.
{
"content":
[{"id":1,"userName":"sdbs2308","fullName":null,"joined":1442105711911,"updated":1442105711911}],
"totalPages":1,
"totalElements":1,
"last":true,
"number":0,
"size":20,
"numberOfElements":1,
"sort":null,
"first":true
}



6. 예외 관련한 사항들 
 - 우선 ErrorResponse 라는 클래스로 공통 에러 리턴 클래스를 하나를 만들어서 활용합니다.
 - 사용자 Exception을 만들어서 쓰는 방법  예를 들면 중복 일 경우 UserDuplicatedException을 만들어 
 - jsonPath를 활용해서 테스트 하는 방법 
  >> result.andExpect(jsonPath("$.userName" , is("sdbs2308")));
  >>  result.andExpect(jsonPath("$.code", is("duplicated.username.exception")));

7. @Transactional /  @TransactionConfiguration(defaultRollback = true) 
    에러가 나던, 안나던 RollBack 이 됩니다.(테스트 상에서는)
    만약 롤백이 안되길 원하면, @Rollback(false) --> @Commit 라고 하면 되죠 




