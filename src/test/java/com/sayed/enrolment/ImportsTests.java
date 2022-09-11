package com.sayed.enrolment;

import com.sayed.enrolment.folder.Folder;
import com.sayed.enrolment.folder.FolderRepo;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(
        classes = EnrolmentApplication.class,
        properties = { "spring.jpa.hibernate.ddl-auto=create" }
)
@WebAppConfiguration
@AutoConfigureMockMvc
@RequiredArgsConstructor
class ImportsTests {

    @Autowired
    public MockMvc mvc;

    @Autowired
    private FolderRepo repository;

    @Autowired
    protected WebApplicationContext webApplicationContext;

    public void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void importsValidTest() throws Exception {
        String uri = "/imports";
        String jsonBody = """
                {
                  "items": [
                      {
                      "id": "элемент_1_1",
                      "url": null,
                      "parentId": null,
                      "size": null,
                      "type": "FOLDER"
                    },
                    {
                      "id": "элемент_1_4",
                      "url": "/file/url1",
                      "parentId": "элемент_1_1",
                      "size": 234,
                      "type": "FILE"
                    },
                    {
                      "id": "элемент_1_5",
                      "url": null,
                      "parentId": "элемент_1_1",
                      "size": null,
                      "type": "FOLDER"
                    },
                    {
                      "id": "элемент_1_6",
                      "url": null,
                      "parentId": "элемент_1_5",
                      "size": null,
                      "type": "FOLDER"
                    }
                  ],
                  "updateDate": "2022-05-28T21:12:01.000Z"
                }
                """;
        MvcResult result = mvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(jsonBody)).andReturn();
        Assertions.assertEquals(200, result.getResponse().getStatus());
        Folder folder = repository.findById("элемент_1_6").orElseThrow(
                () -> new IllegalStateException("Could not find folder")
        );
        Assertions.assertEquals("элемент_1_5", folder.getParentId());
    }

}
