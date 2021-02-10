package com.cos.book.domain.dto;

import lombok.Data;

@Data
public class CommonRespDto<T> {
	private T data;
}
