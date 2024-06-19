package org.myproject.train_english_bot.service;

import lombok.Getter;
import org.myproject.train_english_bot.events.MessageEvent;
import org.myproject.train_english_bot.events.QuestionEvent;
import org.myproject.train_english_bot.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Service
public class SchedulerService {
    @Autowired
    private UserService userService;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Getter
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    @Getter
    private final Map<Integer, Long> sentMessages = new HashMap<>();

    public void putMessage(Integer messageId, Long chatId) {
        sentMessages.put(messageId, chatId);
    }

    public void removeMessage(Integer messageId) {
        sentMessages.remove(messageId);
    }

    @Scheduled(cron = "0 * * * * *")
    public void sendTrainingNotifications() {
        var now = LocalDateTime.now();
        List<User> users = userService.getUsersByTrainingNotice(now.getHour(), now.getMinute());
        for (User user : users) {
            eventPublisher.publishEvent(
                    new MessageEvent(
                            this,
                            user.getChatId(),
                            "It's time to start the training!",
                            null));
            userService.setUserTrainingNotification(user, user.getTrainingNotification().plusDays(1));
        }
    }

    @Scheduled(cron = "0 * * * * *")
    public void sendQuickNotifications() {
        var now = LocalDateTime.now();
        List<User> users = userService.getUsersByRandomNotice(now.getHour(), now.getMinute());
        for (User user : users) {
            eventPublisher.publishEvent(new QuestionEvent(this, user));
        }
    }

    public static LocalDateTime getDefaultTrainingTime() {
        LocalDateTime now = LocalDateTime.now();
        LocalDate day = now.toLocalDate();
        if (now.getHour() >= 10) {
            day = day.plusDays(1);
        }
        LocalTime time = LocalTime.of(10, 0);
        return LocalDateTime.of(day, time);
    }

    public static LocalDateTime getDateTime(int hour, int minute) {
        LocalDateTime now = LocalDateTime.now();
        LocalDate day = now.toLocalDate();
        int currentHour = now.getHour();
        if (currentHour > hour || (currentHour == hour && now.getMinute() > minute)) {
            day = day.plusDays(1);
        }
        LocalTime time = LocalTime.of(hour, minute);
        return LocalDateTime.of(day, time);
    }
}
