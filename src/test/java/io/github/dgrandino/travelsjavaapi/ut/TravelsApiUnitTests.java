package io.github.dgrandino.travelsjavaapi.ut;

import io.github.dgrandino.travelsjavaapi.model.Statistic;
import io.github.dgrandino.travelsjavaapi.model.Travel;
import io.github.dgrandino.travelsjavaapi.model.TravelTypeEnum;
import io.github.dgrandino.travelsjavaapi.service.StatisticService;
import io.github.dgrandino.travelsjavaapi.service.TravelService;
import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.json.JSONObject;

import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {TravelService.class, StatisticService.class})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class})
@ActiveProfiles("test")
public class TravelsApiUnitTests {

    @Autowired
    private TravelService travelService;

    @Autowired
    private StatisticService statisticService;

    @Before
    public void setUp(){
        travelService.createFactory();
        travelService.createTravelList();
    }

    @Test
    public void shouldReturnNotNullTravelService(){
        assertNotNull(travelService);
    }

    @Test
    public void shouldReturnNotNullStatisticService() throws Exception {
        assertNotNull(statisticService);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldReturnTravelCreatedWithSuccess() throws Exception {

        String startDate = "2019-11-21T09:59:51.321Z";
        String endDate = "2019-12-01T09:59:51.321Z";

        JSONObject jsonTravel = new JSONObject();
        jsonTravel.put("id",1);
        jsonTravel.put("orderNumber","123456");
        jsonTravel.put("amount","22.88");
        jsonTravel.put("type", TravelTypeEnum.RETURN.getValue());
        jsonTravel.put("startDate", startDate);
        jsonTravel.put("endDate", endDate);

        Travel travel = travelService.create(jsonTravel);

        assertNotNull(travel);
        assertEquals(travel.getId().intValue(), jsonTravel.get("id"));
        assertEquals(travel.getOrderNumber(), jsonTravel.get("orderNumber"));
        assertEquals(travel.getAmount().toString(), jsonTravel.get("amount"));
        assertEquals(travel.getType().toString(), jsonTravel.get("type"));
        assertEquals(travel.getStartDate(), ZonedDateTime.parse(startDate).toLocalDateTime());
        assertEquals(travel.getEndDate(), ZonedDateTime.parse(endDate).toLocalDateTime());

    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldReturnTravelCreatedInStartDateIsGreaterThanEnDate() throws Exception {

        JSONObject jsonTravel = new JSONObject();
        jsonTravel.put("id",2);
        jsonTravel.put("orderNumber","123456");
        jsonTravel.put("amount","22.88");
        jsonTravel.put("type", TravelTypeEnum.RETURN.getValue());
        jsonTravel.put("startDate", "2019-09-20T09:59:51.321Z");
        jsonTravel.put("endDate", "2019-09-11T09:59:51.321Z");

        Travel travel = travelService.create(jsonTravel);
        boolean travelInFuture = travelService.isStartDateGreaterThanEndDate(travel);

        assertTrue(travelInFuture);

    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldReturnTravelsStatisticsCalculated() throws Exception {

        travelService.delete();

        String startDate = "2019-11-21T09:59:51.321Z";
        String endDate = "2019-12-01T09:59:51.321Z";

        JSONObject jsonTravel123456 = new JSONObject();
        jsonTravel123456.put("id",1);
        jsonTravel123456.put("orderNumber","123456");
        jsonTravel123456.put("amount","22.88");
        jsonTravel123456.put("type", TravelTypeEnum.RETURN.getValue());
        jsonTravel123456.put("startDate", startDate);
        jsonTravel123456.put("endDate", endDate);

        Travel travel = travelService.create(jsonTravel123456);
        travelService.add(travel);

        JSONObject jsonTravel654321 = new JSONObject();
        jsonTravel654321.put("id",2);
        jsonTravel654321.put("orderNumber","654321");
        jsonTravel654321.put("amount","120.0");
        jsonTravel654321.put("type", TravelTypeEnum.RETURN.getValue());
        jsonTravel654321.put("startDate", startDate);
        jsonTravel654321.put("endDate", endDate);

        travel = travelService.create(jsonTravel654321);
        travelService.add(travel);

        Statistic statistic = statisticService.create(travelService.find());

        assertNotNull(statistic);
        assertEquals("142.88", statistic.getSum().toString());
        assertEquals("71.44", statistic.getAvg().toString());
        assertEquals("22.88", statistic.getMin().toString());
        assertEquals("120.00", statistic.getMax().toString());
        assertEquals(2, statistic.getCount());
    }

    @After
    public void tearDown(){
        travelService.clearObjects();
    }
}
