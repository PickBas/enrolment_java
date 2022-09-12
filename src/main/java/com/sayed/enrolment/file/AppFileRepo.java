package com.sayed.enrolment.file;

import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Timestamp;
import java.util.List;

public interface AppFileRepo extends JpaRepository<AppFile, String> {
    List<AppFile> findAllByDateBetween(Timestamp dateStart, Timestamp dateEnd);
}
