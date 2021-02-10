package com.cos.book.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cos.book.domain.Book;
import com.cos.book.domain.BookRepository;
import com.cos.book.domain.dto.CommonRespDto;
import com.google.gson.Gson;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class BookController {
	private final BookRepository bookRepository;
	@CrossOrigin
	@PostMapping("/book")
	public ResponseEntity<?> save(@RequestBody Book book) {
		return new ResponseEntity<>(bookRepository.save(book),HttpStatus.CREATED );		
	}
	
	@CrossOrigin
	@GetMapping("/book")
	public ResponseEntity<?> findByAll() {
		return new ResponseEntity<>(bookRepository.findAll(), HttpStatus.OK);
	}
	
	@CrossOrigin
	@GetMapping("/book/{id}")
	public ResponseEntity<?> findById(@PathVariable Integer id) {
		return new ResponseEntity<>(bookRepository.findById(id).orElseThrow(()->new IllegalArgumentException("id를 확인해주세요")), HttpStatus.OK);
	}
	
	@CrossOrigin
	@PutMapping("/book/{id}")
	public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody Book book) {
		Book bookEntity = bookRepository.findById(id)
				.orElseThrow(()->new IllegalArgumentException("id를 확인해주세요.")); // 영속화 (book 오브젝트) -> 영속성 컨텍스트 보관
		bookEntity.setTitle(book.getTitle());
		bookEntity.setRating(book.getRating());
		bookEntity.setPrice(book.getPrice());
		
		return new ResponseEntity<>(bookEntity,HttpStatus.OK);
	}
	
	@CrossOrigin
	@DeleteMapping("/book/{id}")
	public ResponseEntity<?> deleteById(@PathVariable Integer id) {
		bookRepository.deleteById(id);
		CommonRespDto<String> commonRespDto = new CommonRespDto<>();
		Gson gson = new Gson();
		commonRespDto.setData("ok");
		String respData = gson.toJson(commonRespDto);
		return new ResponseEntity<>(respData, HttpStatus.OK);
	}
}
