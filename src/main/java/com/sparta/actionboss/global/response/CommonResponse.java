package com.sparta.actionboss.global.response;

import com.sparta.actionboss.domain.post.dto.PostResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.logging.XMLFormatter;

@Data
@AllArgsConstructor
public class CommonResponse{
    private String msg;
}
