package com.cognicx.AppointmentRemainder.ami;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AsteriskEventRepository extends JpaRepository<AsteriskEvent, Long>{

}
