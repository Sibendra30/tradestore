package com.db.tradestore.repository;

import com.db.tradestore.entity.TradeRecord;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface TradeRepository extends CrudRepository<TradeRecord, String> {

  List<TradeRecord> findAllByTradeId(String tradeId);

}
