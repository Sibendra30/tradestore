package com.db.tradestore.service.impl;

import static org.mockito.ArgumentMatchers.any;

import com.db.tradestore.entity.TradeRecord;
import com.db.tradestore.exception.InvalidTradeVersionException;
import com.db.tradestore.model.TradeInfo;
import com.db.tradestore.repository.TradeRepository;
import com.db.tradestore.service.TradeStoreService;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class TradeStoreServiceImplTest {

  private TradeStoreService tradeStoreService;

  @MockBean
  private TradeRepository tradeRepository;

  @BeforeEach
  void initEach() {
    tradeStoreService = new TradeStoreServiceImpl(tradeRepository);
  }

  private TradeRecord getDummyTradeRecord() {
    return new TradeRecord(UUID.randomUUID().toString(), "T1",
        1, "CP-1", "B1",
        LocalDate.now().plusMonths(1), LocalDate.now(), "N");
  }

  @Test
  void testInvalidVersion() {
    TradeRecord t1 = this.getDummyTradeRecord();
    t1.setVersion(2);

    TradeRecord t2 = this.getDummyTradeRecord();
    t2.setVersion(4);

    TradeRecord t3 = this.getDummyTradeRecord();
    t3.setVersion(3);

    List<TradeRecord> list = new ArrayList<>();
    list.add(t2);
    list.add(t3);
    list.add(t1);

    TradeInfo tradeInfo = new TradeInfo("T1",
        1, "CP-1", "B1",
        LocalDate.now().plusMonths(1), LocalDate.now(), "N");

    Mockito.when(tradeRepository.findAllByTradeId(tradeInfo.getTradeId()))
        .thenReturn(list);
    Assertions.assertThrows(InvalidTradeVersionException.class,
        () -> tradeStoreService.createTrade(tradeInfo));
  }

  @Test
  void testSaveSuccess() {
    TradeInfo tradeInfo = new TradeInfo("T1",
        1, "CP-1", "B1",
        LocalDate.now().plusMonths(1), LocalDate.now(), "N");

    Mockito.when(tradeRepository.findAllByTradeId(tradeInfo.getTradeId()))
        .thenReturn(Collections.EMPTY_LIST);
    tradeStoreService.createTrade(tradeInfo);
    Mockito.verify(tradeRepository).save(any(TradeRecord.class));
  }

}
