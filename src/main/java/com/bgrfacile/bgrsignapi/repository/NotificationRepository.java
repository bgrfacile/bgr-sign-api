package com.bgrfacile.bgrsignapi.repository;

import com.bgrfacile.bgrsignapi.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

}
