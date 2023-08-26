package com.sparta.actionboss.domain.search.service;

import com.sparta.actionboss.domain.search.dto.SearchListResponseDto;
import com.sparta.actionboss.domain.search.dto.SearchResponseDto;
import com.sparta.actionboss.domain.search.entity.Address;
import com.sparta.actionboss.domain.search.repository.SearchRepository;
import com.sparta.actionboss.global.exception.SearchException;
import com.sparta.actionboss.global.exception.errorcode.ClientErrorCode;
import com.sparta.actionboss.global.response.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.sparta.actionboss.global.response.SuccessMessage.SEARCH_SUCCESS;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SearchService {

    private final SearchRepository searchRepository;

    public CommonResponse<SearchResponseDto> searchAddress(String keyword) {
        if (keyword == null) {
            throw new SearchException(ClientErrorCode.SEARCH_NULL);
        }

        Address searchAddress = searchRepository.findByAddress(keyword);

        if (searchAddress == null) {
            throw new SearchException(ClientErrorCode.SEARCH_NOT_FOUND);
        }

        return new CommonResponse<>(SEARCH_SUCCESS, new SearchResponseDto(searchAddress.getLatitude(), searchAddress.getLongitude()));
    }

    public CommonResponse<List<SearchListResponseDto>> searchAddressList(String keyword) {
        if (keyword == null) {
            throw new SearchException(ClientErrorCode.SEARCH_NULL);
        }

        List<Address> searchAddressList = searchRepository.findByAddressContaining(keyword);

        if (searchAddressList == null || searchAddressList.isEmpty()) {
            throw new SearchException(ClientErrorCode.SEARCH_NOT_FOUND);
        }

        List<SearchListResponseDto> searchListResponseDto = searchAddressList.stream()
                .map(a -> new SearchListResponseDto(
                        a.getAddress()
                ))
                .collect(Collectors.toList());

        return new CommonResponse<>(SEARCH_SUCCESS, searchListResponseDto);
    }
}
