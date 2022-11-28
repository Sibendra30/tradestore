package com.db.tradestore.controller;

import com.db.tradestore.api.TradeStoreApi;
import com.db.tradestore.exception.InvalidRequestBodyException;
import com.db.tradestore.model.TradeInfo;
import com.db.tradestore.service.TradeStoreService;
import java.time.LocalDate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;

@Controller
@Slf4j
public class TradeStoreController implements TradeStoreApi {

  private final TradeStoreService tradeStoreService;

  @Autowired
  public TradeStoreController(TradeStoreService tradeStoreService) {
    this.tradeStoreService = tradeStoreService;
  }

  @Override
  public ResponseEntity<TradeInfo> createTrade(TradeInfo requestBody) {
    this.validateSaveTradeRequest(requestBody);
    TradeInfo result = this.tradeStoreService.createTrade(requestBody);
    return new ResponseEntity<>(result, HttpStatus.CREATED);
  }

  private void validateSaveTradeRequest(TradeInfo tradeInfo) {
    if (tradeInfo == null
    || StringUtils.isEmpty(tradeInfo.getTradeId())
    || StringUtils.isEmpty(tradeInfo.getBookId())
    || StringUtils.isEmpty(tradeInfo.getBookId())
    || StringUtils.isEmpty(tradeInfo.getMaturityDate())
    || LocalDate.now().compareTo(tradeInfo.getMaturityDate()) >= 0
    || tradeInfo.getVersion() == null
    || tradeInfo.getVersion() < 1) {
      log.error("Request body has missing or invalid parameters");
      throw new InvalidRequestBodyException();
    }
  }
}
