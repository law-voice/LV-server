package com.voice.law.jpa

import com.voice.law.domain.LoginRecord
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface LoginRecordRepository extends JpaRepository<LoginRecord, Integer> {

}