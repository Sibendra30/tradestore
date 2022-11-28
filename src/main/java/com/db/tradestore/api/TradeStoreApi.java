package com.db.tradestore.api;

import com.db.tradestore.model.TradeInfo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/tradestore")
public interface TradeStoreApi {

  @PostMapping("/trade")
  ResponseEntity<TradeInfo> createTrade(@RequestBody TradeInfo requestBody);

}
