package com.example.bookshop.controllers;

import com.example.bookshop.entity.SmsCode;
import com.example.bookshop.security.BookstoreUserRegister;
import com.example.bookshop.security.ContactConfirmationPayload;
import com.example.bookshop.security.ContactConfirmationResponse;
import com.example.bookshop.security.RegistrationForm;
import com.example.bookshop.security.SmsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * Контроллер авторизации
 */
@Controller
@RequiredArgsConstructor
@Api(description = "Контроллер авторизации")
public class AuthUserController extends CommonController {

    private final BookstoreUserRegister userRegister;
    private final SmsService smsService;
    private final JavaMailSender javaMailSender;

    @ApiOperation("Получение страницы авторизации")
    @ApiResponse(responseCode = "200", description = "Страница авторизации")
    @GetMapping("/signin")
    public String signInPage() {
        return "signin";
    }

    @ApiOperation("Получение страницы регистрации")
    @ApiResponse(responseCode = "200", description = "Страница регистрации")
    @GetMapping("/signup")
    public String handleSignUp(Model model) {
        model.addAttribute("regForm", new RegistrationForm());
        return "signup";
    }

    @ApiOperation("Подтверждение регистрации по e-mail")
    @ApiResponse(responseCode = "200", description = "Подтверждение регистрации по e-mail")
    @PostMapping("/requestContactConfirmation")
    @ResponseBody
    public ContactConfirmationResponse handleRequestContactConfirmation(@Valid @RequestBody ContactConfirmationPayload payload) {
        ContactConfirmationResponse response = new ContactConfirmationResponse();
        response.setResult("true");
        if (payload.getContact().contains("@")) {
            return response;
        } else {
            String smsCodeString = smsService.sendSecretCodeSms(payload.getContact());
            smsService.saveNewCode(new SmsCode(smsCodeString, 60)); //expires in 1 min.
            return response;
        }
    }

    @ApiOperation("Подтверждение регистрации по e-mail")
    @ApiResponse(responseCode = "200", description = "Подтверждение регистрации по e-mail")
    @PostMapping("/requestEmailConfirmation")
    @ResponseBody
    public ContactConfirmationResponse handleRequestEmailConfirmation(@Valid ContactConfirmationPayload payload) {
        ContactConfirmationResponse response = new ContactConfirmationResponse();
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("book-shop@mail.ru");
        message.setTo(payload.getContact());
        SmsCode smsCode = new SmsCode(smsService.generateCode(), 300); //5 minutes
        smsService.saveNewCode(smsCode);
        message.setSubject("Bookstore email verification!");
        message.setText("Verification code is: " + smsCode.getCode());
        javaMailSender.send(message);
        response.setResult("true");
        return response;
    }

    @PostMapping("/approveContact")
    @ResponseBody
    public ContactConfirmationResponse handleApproveContact(@Valid ContactConfirmationPayload payload) {
        ContactConfirmationResponse response = new ContactConfirmationResponse();

        // if(smsService.verifyCode(payload.getCode())){
        response.setResult("true");
        //   }

        return response;
    }

    @PostMapping("/reg")
    public String handleUserRegistration(RegistrationForm registrationForm, Model model) {
        userRegister.registerNewUser(registrationForm);
        model.addAttribute("regOk", true);
        return "signin";
    }

    @PostMapping("/login")
    @ResponseBody
    public ContactConfirmationResponse handleLogin(@Valid @RequestBody ContactConfirmationPayload payload,
                                                   HttpServletResponse httpServletResponse) {
        ContactConfirmationResponse loginResponse = userRegister.jwtLogin(payload);
        Cookie cookie = new Cookie("token", loginResponse.getResult());
        httpServletResponse.addCookie(cookie);
        return loginResponse;
    }

    @PostMapping("/login-by-phone-number")
    @ResponseBody
    public ContactConfirmationResponse handleLoginByPhoneNumber(@Valid @RequestBody ContactConfirmationPayload payload,
                                                                HttpServletResponse httpServletResponse) {
//        if(smsService.verifyCode(payload.getCode())) {
//            ContactConfirmationResponse loginResponse = userRegister.jwtLoginByPhoneNumber(payload);
//            Cookie cookie = new Cookie("token", loginResponse.getResult());
//            httpServletResponse.addCookie(cookie);
//            return loginResponse;
//        }else {
        return null;
//        }
    }

//    @GetMapping("/profile")
//    public String handleProfile(Model model) {
//        model.addAttribute("curUsr", userRegister.getCurrentUser());
//        return "profile";
//    }

//    @GetMapping("/logout")
//    public String handleLogout(HttpServletRequest request) {
//        HttpSession session = request.getSession();
//        SecurityContextHolder.clearContext();
//        if (session != null) {
//            session.invalidate();
//        }
//
//        for (Cookie cookie : request.getCookies()) {
//            cookie.setMaxAge(0);
//        }
//
//        return "redirect:/";
//    }
}
