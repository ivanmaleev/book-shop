package com.example.bookshop.service;

import com.example.bookshop.dto.CommonPageData;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public interface CommonService {

    CommonPageData getCommonPageData(HttpServletRequest request, boolean usersPage);
}
