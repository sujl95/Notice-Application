package me.noticeapplication.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import lombok.extern.slf4j.Slf4j;
import me.noticeapplication.common.exception.ExceptionStatus;
import me.noticeapplication.common.exception.NoticeException;
import me.noticeapplication.controller.response.exception.ErrorResponse;
import me.noticeapplication.controller.response.exception.ExceptionResponseInfo;

@Slf4j
@RestControllerAdvice
public class ExceptionHandlerController {

	private void errorLogging(Exception ex) {
		log.error("Exception = {} , message = {}", ex.getClass().getSimpleName(),
				ex.getLocalizedMessage());
	}

	@ExceptionHandler(NoticeException.class)
	private ResponseEntity<ExceptionResponseInfo> handleStatusException(NoticeException noticeException) {
		final ExceptionResponseInfo response = ExceptionResponseInfo.from(noticeException);
		HttpStatus httpStatus = noticeException.getHttpStatus();
		errorLogging(noticeException);
		return new ResponseEntity<>(response, httpStatus);
	}

	/**
	 * @Valid,@Validated 검증으로 binding 못할 때
	 */
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	private ErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
		errorLogging(exception);
		return ErrorResponse.of(ExceptionStatus.INVALID_INPUT_VALUE,
				exception.getBindingResult());
	}


	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(HttpMessageNotReadableException.class)
	private ErrorResponse httpMessageNotReadableException(HttpMessageNotReadableException exception) {
		errorLogging(exception);
		return ErrorResponse.of(ExceptionStatus.INVALID_INPUT_VALUE,
				exception.getLocalizedMessage());
	}

	/**
	 * @RequestParam enum type 불일치로 binding 못할 때
	 */
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	private ErrorResponse handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException exception) {
		errorLogging(exception);
		return ErrorResponse.of(exception);
	}

	/**
	 * @ModelAttribute 나 RequestBody 로 @Valid 로 Binding 못할 때
	 */
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(BindException.class)
	private ErrorResponse handleBindException(BindException exception) {
		errorLogging(exception);
		return ErrorResponse.of(ExceptionStatus.INVALID_TYPE_VALUE_EXCEPTION,
				exception.getBindingResult());
	}

	/**
	 * 지원하지 않는 HTTP METHOD 를 요청 했을때
	 */
	@ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	private ErrorResponse handleHttpRequestMethodNotSupportedException(
			HttpRequestMethodNotSupportedException exception) {
		return ErrorResponse.of(exception);
	}
}
