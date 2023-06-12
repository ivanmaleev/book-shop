package com.example.bookshop.service.impl;

import com.example.bookshop.security.BookstoreUser;
import com.example.bookshop.security.BookstoreUserRegister;
import com.example.bookshop.service.CommonService;
import com.example.bookshop.service.UsersBookService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import javax.servlet.http.HttpServletRequest;

@ActiveProfiles("test")
@SpringJUnitConfig(CommonServiceImplTest.TestConfig.class)
class CommonServiceImplTest {

    @Autowired
    private CommonService commonService;
    @MockBean
    private BookstoreUserRegister userRegister;
    @MockBean
    private UsersBookService usersBookService;
    private HttpServletRequest httpServletRequest = new MockHttpServletRequest();

    @Test
    void getCommonPageDataUsersPage() {
        Mockito.when(userRegister.getCurrentUser()).thenReturn(new BookstoreUser());
        Assertions.assertNotNull(commonService.getCommonPageData(httpServletRequest, true));
    }

    @Test
    void getCommonPageDataNotUsersPage() {
        Mockito.when(userRegister.getCurrentUser()).thenReturn(new BookstoreUser());
        Assertions.assertNotNull(commonService.getCommonPageData(httpServletRequest, false));
    }

    @TestConfiguration
    public static class TestConfig {

        @Bean
        public CommonService commonService() {
            return new CommonServiceImpl();
        }
    }
}