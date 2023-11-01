package kukekyakya.kukemarket.controller.sign;

import javax.validation.Valid;
import kukekyakya.kukemarket.dto.response.Response;
import kukekyakya.kukemarket.dto.sign.SignInRequest;
import kukekyakya.kukemarket.dto.sign.SignUpRequest;
import kukekyakya.kukemarket.service.sign.SignService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import static kukekyakya.kukemarket.dto.response.Response.success;

//JSON으로 응답하기 위해 RestController
@RestController
@RequiredArgsConstructor
public class SignController {
    private final SignService signService;

    @PostMapping("/api/sign-up")
    @ResponseStatus(HttpStatus.CREATED)
    //요청으로 전달받는 JSON 바디를 객체로 변환하기 위해 @RequestBody를 선언
    // Request 객체의 필드 값을 검증하기 위해 @Valid를 선언해줍니다.
    public Response signUp(@Valid @RequestBody SignUpRequest req) { // 2
        signService.signUp(req);
        return success();
    }

    @PostMapping("/api/sign-in")
    @ResponseStatus(HttpStatus.OK)
    public Response signIn(@Valid @RequestBody SignInRequest req) { // 3
        return success(signService.signIn(req));
    }


    //파라미터에 설정된 @RequestHeader는 required 옵션의 기본 설정 값이 true이기 때문에,
    // 이 헤더 값이 전달되지 않았을 때 예외가 발생하게 됩니다.
    @PostMapping("/api/refresh-token")
    @ResponseStatus(HttpStatus.OK)
    public Response refreshToken(@RequestHeader(value = "Authorization") String refreshToken){
        return success(signService.refreshToken(refreshToken));
    }
}
