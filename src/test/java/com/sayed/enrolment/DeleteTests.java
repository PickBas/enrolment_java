package com.sayed.enrolment;

import com.sayed.enrolment.file.AppFileRepo;
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
class DeleteTests {

    @Autowired
    public MockMvc mvc;

    @Autowired
    private FolderRepo folderRepo;

    @Autowired
    private AppFileRepo fileRepo;

    @Autowired
    protected WebApplicationContext webApplicationContext;

    public void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void deleteFolder() throws Exception {
        String uriImports = "/imports";
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
        mvc.perform(MockMvcRequestBuilders.post(uriImports)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(jsonBody)).andReturn();
        String removeId = "элемент_1_1";
        String uriDelete= "/delete/" + removeId + "?date=2022-10-04T00:00:00Z";
        MvcResult result = mvc.perform(MockMvcRequestBuilders.delete(uriDelete)).andReturn();
        Assertions.assertEquals(200, result.getResponse().getStatus());
        Assertions.assertNull(folderRepo.findById(removeId).orElse(null));
        Assertions.assertNull(folderRepo.findById("элемент_1_5").orElse(null));
        Assertions.assertNull(folderRepo.findById("элемент_1_6").orElse(null));
        Assertions.assertNull(fileRepo.findById("элемент_1_4").orElse(null));
    }

    @Test
    void deleteFile() throws Exception {
        String uriImports = "/imports";
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
        mvc.perform(MockMvcRequestBuilders.post(uriImports)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(jsonBody)).andReturn();
        String removeId = "элемент_1_4";
        String uriDelete= "/delete/" + removeId + "?date=2022-10-04T00:00:00Z";
        MvcResult result = mvc.perform(MockMvcRequestBuilders.delete(uriDelete)).andReturn();
        Assertions.assertEquals(200, result.getResponse().getStatus());
        Assertions.assertNull(fileRepo.findById(removeId).orElse(null));
        Assertions.assertEquals("2022-10-04 00:00:00.0",
                folderRepo.findById("элемент_1_1").orElseThrow(
                        () -> new IllegalStateException("Could not find folder")
                ).getDate().toString());
    }

}
