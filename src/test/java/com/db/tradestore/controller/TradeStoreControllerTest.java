package com.db.tradestore.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.db.tradestore.model.TradeInfo;
import com.db.tradestore.repository.TradeRepository;
import com.db.tradestore.service.TradeStoreService;
import com.db.tradestore.service.impl.TradeStoreServiceImpl;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(TradeStoreController.class)
@ExtendWith(SpringExtension.class)
class TradeStoreControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private TradeStoreService tradeStoreService;

  private final String ENDPOINT = "/tradestore/trade";
  private final ObjectMapper mapper = new ObjectMapper()
      .registerModule(new JavaTimeModule())
      .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
      .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
      .setSerializationInclusion(JsonInclude.Include.NON_NULL);

  private TradeInfo getDummyTradeInfo() {
    return new TradeInfo("T1",
        1, "CP-1", "B1",
        LocalDate.now().plusMonths(1), LocalDate.now(), "N");
  }

  @Test
  void saveTradeRecord() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.post(ENDPOINT)
        .contentType(MediaType.APPLICATION_JSON)
        .content(mapper.writeValueAsString(getDummyTradeInfo()))
        .characterEncoding(StandardCharsets.UTF_8))
        .andExpect(status().is(HttpStatus.CREATED.value()));
  }

  private static Stream<Arguments> badRequestParameters() {
    return Stream.of(
        Arguments.of(new TradeInfo()),
        Arguments.of(new TradeInfo("T1", -1, null, null, null, null, null)),
        Arguments.of(new TradeInfo("T1", 1, "CP1", null, null, null, null)),
        Arguments.of(new TradeInfo("T1", 1, "CP1", "B1", null, null, null)),
        Arguments.of(new TradeInfo("T1", 1, "CP1", "B1", LocalDate.now().minusDays(1), null, null))
    );
  }

  @ParameterizedTest
  @MethodSource("badRequestParameters")
  void missingValuesInBody(TradeInfo trade) throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.post(ENDPOINT)
        .contentType(MediaType.APPLICATION_JSON)
        .content(mapper.writeValueAsString(trade))
        .characterEncoding(StandardCharsets.UTF_8))
        .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
  }

  @Test
  void test1() {
    TradeInfo t1 = this.getDummyTradeInfo();
    t1.setVersion(5);

    TradeInfo t2 = this.getDummyTradeInfo();
    t2.setVersion(3);

    TradeInfo t3 = this.getDummyTradeInfo();
    t3.setVersion(1);

    List<TradeInfo> list = new ArrayList<>();
    list.add(t2);
    list.add(t3);
    list.add(t1);

    list.sort((t11, t21) ->  {
      if (t11.getVersion() < t21.getVersion()) {
        return 1;
      }
      return -1;
    });

    System.out.println(list.get(0).getVersion());
  }
}
