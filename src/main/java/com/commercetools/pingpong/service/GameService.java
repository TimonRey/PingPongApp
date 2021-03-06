package com.commercetools.pingpong.service;

import com.commercetools.pingpong.model.Message;
import com.commercetools.pingpong.model.Player;
import org.springframework.stereotype.Service;

@Service
public interface GameService {

    void updateScore(Message message);

    Player getLeftPlayer();

    Player getRightPlayer();

    boolean getLeftPlayerServe();

    boolean getRightPlayerServe();

    boolean getIfItsOvertime();

    boolean getFirstServingPlayer();
}
