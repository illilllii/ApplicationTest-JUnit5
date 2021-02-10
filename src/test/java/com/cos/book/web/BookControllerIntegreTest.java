package com.cos.book.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import com.cos.book.domain.Book;
import com.cos.book.domain.BookRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
public class BookControllerIntegreTest {
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private BookRepository bookRepository;

	@Autowired
	private EntityManager entityManager;
	
	@BeforeEach
	public void init() {
		entityManager.createNativeQuery("ALTER TABLE book ALTER COLUMN id RESTART WITH 1").executeUpdate();	
	}
	
	@Test
	public void save_테스트() throws Exception {
		// given (테스트를 하기 위한 준비)
		Book book = new Book(null, "제목1", 9.0, 2000.0);
		String content = new ObjectMapper().writeValueAsString(book);

		// when (테스트 실행)
		ResultActions resultAction = mockMvc.perform(post("/book")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(content)
				.accept(MediaType.APPLICATION_JSON_UTF8));
		
		// then(검증)
		resultAction
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.title").value("제목1"))
			.andDo(MockMvcResultHandlers.print());
	}
	
	@Test
	public void findAll_테스트() throws Exception {
		// given
		List<Book> books = new ArrayList<>();
		books.add(new Book(null, "제목2", 9.0, 2000.0));
		books.add(new Book(null, "제목3", 5.0, 3000.0));
		books.add(new Book(null, "제목4", 4.0, 2500.0));
		bookRepository.saveAll(books);
		
		ResultActions resultAction = mockMvc.perform(get("/book")
				.accept(MediaType.APPLICATION_JSON_UTF8));
		
		// then
		resultAction
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", Matchers.hasSize(3)))
			.andExpect(jsonPath("$.[2].title").value("제목4"))
			.andDo(MockMvcResultHandlers.print());
	}
	
	@Test
	public void findById_테스트() throws Exception {
		// given
		List<Book> books = new ArrayList<>();
		books.add(new Book(null, "제목2", 9.0, 2000.0));
		books.add(new Book(null, "제목3", 5.0, 3000.0));
		books.add(new Book(null, "제목4", 4.0, 2500.0));
		bookRepository.saveAll(books);
		
		Integer id = 2;
		
		//when
		ResultActions resultAction = mockMvc.perform(get("/book/{id}", id)
				.accept(MediaType.APPLICATION_JSON_UTF8));
		
		//then
		resultAction
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.title").value("제목3"))
			.andDo(MockMvcResultHandlers.print());
	}
	
	@Test
	public void update_테스트() throws Exception {
		// given
		List<Book> books = new ArrayList<>();
		books.add(new Book(null, "제목2", 9.0, 2000.0));
		books.add(new Book(null, "제목3", 5.0, 3000.0));
		books.add(new Book(null, "제목4", 4.0, 2500.0));
		bookRepository.saveAll(books);		
		
		Integer id = 3;
		Book book = new Book(null, "제목123", 8.0, 4000.0);
		String content = new ObjectMapper().writeValueAsString(book);
		
		//when
		ResultActions resultAction = mockMvc.perform(put("/book/{id}", id)
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(content)
				.accept(MediaType.APPLICATION_JSON_UTF8));
		
		//then
		resultAction
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id").value(3))
			.andExpect(jsonPath("$.title").value("제목123"))
			.andDo(MockMvcResultHandlers.print());
	}
	
	@Test
	public void delete_테스트() throws Exception {
		// given
		List<Book> books = new ArrayList<>();
		books.add(new Book(null, "제목2", 9.0, 2000.0));
		books.add(new Book(null, "제목3", 5.0, 3000.0));
		books.add(new Book(null, "제목4", 4.0, 2500.0));
		bookRepository.saveAll(books);		
		
		Integer id = 1;
		
		//when
		ResultActions resultAction = mockMvc.perform(delete("/book/{id}", id)
				.accept(MediaType.APPLICATION_JSON_UTF8));
		
		//then
		resultAction
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data").value("ok"))
			.andDo(MockMvcResultHandlers.print());

	}
}
