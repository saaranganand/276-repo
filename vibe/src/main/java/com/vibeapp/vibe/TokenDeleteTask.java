package com.vibeapp.vibe;


import java.time.Instant;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vibeapp.vibe.models.PasswordResetTokenRepository;

@Service
@Transactional
public class TokenDeleteTask {
    @Autowired
    PasswordResetTokenRepository passwordResetTokenRepository;

    @Scheduled(cron = "0 0 0 * * *") // Schedules deletion every day at 12am local server time
    public void deleteExpiredToken(){
        Date current = Date.from(Instant.now());
        passwordResetTokenRepository.deleteAllExpiredSince(current);
    }
}
