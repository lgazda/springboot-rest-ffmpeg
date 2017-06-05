package pl.net.gazda.videoprobe;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
@Ignore
public class DemoApplicationTests {
	@Autowired
	private WebApplicationContext webApplicationContext;



	@Test
	public void test() throws Exception {

		MockMultipartFile firstFile = mockVideoFile();
		MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		mockMvc.perform(videoProbePost()
				.file(firstFile)
					.contentType(MediaType.MULTIPART_FORM_DATA))
				.andExpect(status().is(200));
	}

	private MockMultipartHttpServletRequestBuilder videoProbePost() {
		return MockMvcRequestBuilders.fileUpload("/video/probe");
	}

	private MockMultipartFile mockVideoFile() {
		return new MockMultipartFile("file", "filename.mp4", "video/mp4", "fake".getBytes());
	}

}
