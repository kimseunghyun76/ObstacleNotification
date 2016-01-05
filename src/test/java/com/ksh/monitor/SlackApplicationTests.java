package com.ksh.monitor;

import com.ksh.monitor.util.MDCUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.nio.charset.Charset;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SlackApplication.class)
@WebAppConfiguration
public class SlackApplicationTests {

	private static Logger logger = LoggerFactory.getLogger(SlackApplicationTests.class);

	private MockMvc mockMvc;

	private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

	private HttpMessageConverter mappingJackson2HttpMessageConverter;

	@Autowired
	private WebApplicationContext webApplicationContext;

	@Test
	public void contextLoads() {
	}

	@Before
	public void setup() throws Exception{
		this.mockMvc = webAppContextSetup(webApplicationContext).build();
	}

	@Test
	public void responseOk테스트() throws Exception{
		mockMvc.perform(get("/api/v1/test1")
				.contentType(contentType))
				.andExpect(status().isOk());
	}

	@Test
	public void responseFail테스트() throws Exception{
		mockMvc.perform(get("/api/v1/test2")
				.param("value","test")
				.contentType(contentType))
				.andExpect(status().isOk());
	}

	@Test
	public void responseFail2테스트() throws Exception{
		mockMvc.perform(post("/api/v1/test3")
				.contentType(contentType).param("value1","test"))
				.andExpect(status().isOk());
	}



	/**
	 HTTP 404 - Not Found(페이지가 아에 존재하지 않음)
	 HTTP 405 - Method not allowed(페이지는 존재하나, 그걸 못보게 막거나 리소스를 허용안함)
	 */
	@Test
	public void isMethodNotAllowed테스트() throws Exception {
		mockMvc.perform(post("/api/v1/test2")
				//.content(this.json(new Bookmark()))
				.contentType(contentType))
				.andExpect(status().isMethodNotAllowed());
	}

	@Test
	public void isNotFound테스트() throws Exception {
		mockMvc.perform(get("/api/v1/test5")
				.contentType(contentType))
				.andExpect(status().isNotFound());
	}


	protected String json(Object o) throws IOException{
		MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
		this.mappingJackson2HttpMessageConverter.write(o , MediaType.APPLICATION_JSON,mockHttpOutputMessage);

		return mockHttpOutputMessage.getBodyAsString();
	}
}
