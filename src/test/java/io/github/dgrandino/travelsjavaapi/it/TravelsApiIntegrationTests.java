package io.github.dgrandino.travelsjavaapi.it;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import io.github.dgrandino.travelsjavaapi.model.TravelTypeEnum;
import org.json.JSONException;

import org.json.JSONObject;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockitoTestExecutionListener;

import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, MockitoTestExecutionListener.class})
public class TravelsApiIntegrationTests {

    @Autowired
    private MockMvc mockMvc ;



    @Test
    @Order(1)
    public void contextLoad(){
        assertNotNull(mockMvc);
    }

    @Test
    @Order(2)
    public void shouldReturnCreateTravel() throws Exception {

        JSONObject mapToCreate = setObjectToCreate();

        this.mockMvc.perform(post("/api-travels/travels").contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapToCreate.toString())).andExpect(status().isCreated());
    }

    @Test
    @Order(3)
    public void shouldReturnUpdateTravel() throws Exception {

        JSONObject mapToUpdate = setObjectToUpdate();
        this.mockMvc.perform(put("/api-travels/travels/1").contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapToUpdate.toString())).andExpect(status().isOk());
    }

    @Test
    @Order(4)
    public void shouldReturnGetAllTravels() throws Exception {
        this.mockMvc.perform(get("/api-travels/travels")).andExpect(status().isOk());
    }

    @Test
    @Order(5)
    public void shouldReturnRemoveAllTravels() throws Exception {
        this.mockMvc.perform(delete("/api-travels/travels")).andExpect(status().isNoContent());
    }

    @SuppressWarnings("unchecked")
    private JSONObject setObjectToCreate() throws JSONException {

        String startDate = "2019-11-21T09:59:51.312Z";
        String endDate = "2019-12-01T21:08:45.202Z";

        JSONObject map = new JSONObject();
        map.put("id", 1);
        map.put("orderNumber", "220788");
        map.put("amount", "22.88");
        map.put("type", TravelTypeEnum.RETURN.getValue());
        map.put("startDate", startDate);
        map.put("endDate", endDate);

        return map;
    }

    @SuppressWarnings("unchecked")
    private JSONObject setObjectToUpdate() throws JSONException {

        String startDate = "2019-11-21T09:59:51.312Z";
        String endDate = "2019-12-01T21:08:45.202Z";

        JSONObject map = new JSONObject();
        map.put("id", 1);
        map.put("orderNumber", "220788");
        map.put("amount", "22.88");
        map.put("type", TravelTypeEnum.RETURN.getValue());
        map.put("startDate", startDate);
        map.put("endDate", endDate);

        return map;
    }
}
