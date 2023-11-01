package kukekyakya.kukemarket.advice;

import kukekyakya.kukemarket.dto.response.Response;
import kukekyakya.kukemarket.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ExceptionAdvice {
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Response exception(Exception e) {
        //구체적인 에러 보여주기
        log.info("e= {}",e.getMessage());
        return Response.failure(-1000,"오류가 발생하였습니다.");

    }
    //401 에러
    @ExceptionHandler(AuthenticationEntryPointException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Response authenticationEntryPoint(){
        return Response.failure(-1001,"인증되지 않은 사용자입니다.");
    }

    //403 에러
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public Response accessDeniedException(){
        return Response.failure(-1002,"접근이 거부되었습니다");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST) //400
    public Response methodArgumentNotValidException(MethodArgumentNotValidException e){
        return Response.failure(-1003,e.getBindingResult().getFieldError().getDefaultMessage());
    }

    @ExceptionHandler(LoginFailureException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Response loginFailureException(){
        return Response.failure(-1004,"로그인에 실패하였습니다.");
    }


    @ExceptionHandler(MemberEmailAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Response memberEmailAlreadyExistsException(MemberEmailAlreadyExistsException e) { // 4
        return Response.failure(-1005, e.getMessage() + "은 중복된 이메일 입니다.");
    }


    @ExceptionHandler (MemberNicknameAlreadyExistsException.class)
    @ResponseStatus (HttpStatus.CONFLICT)//401
    public Response memberNicknameAlreadyExistsException(MemberNicknameAlreadyExistsException e){
        return Response.failure(-1006,e.getMessage()+"은 중복된 닉네임 입니다.");
    }

    @ExceptionHandler (MemberNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)//404
    public Response memberNotFoundException() {
        return Response.failure(-1007,"요청한 회원을 찾을 수 없습니다.");
    }

    @ExceptionHandler (RoleNotFoundException.class)
    @ResponseStatus (HttpStatus.NOT_FOUND)//404
    public Response roleNotFoundException(){
        return Response.failure(-1008,"요청한 권한 등급을 찾을 수 없습니다. ");
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response missingRequestHeaderException(MissingRequestHeaderException e) {
        return Response.failure(-1009,e.getHeaderName()+"요청 헤더가 누락되었습니다.");
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response categoryNotFoundException (){
        return Response.failure(-1010,"존재하지 않는 카테고리입니다.");

    }

    @ExceptionHandler (CannotConvertNestedStructureException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Response cannotConvertNestedStructureException(CannotConvertNestedStructureException e){
        log.info("e={}",e.getMessage());
        return Response.failure(-1011,"중첩 구조 변환에 실패하였습니다");
    }
}
