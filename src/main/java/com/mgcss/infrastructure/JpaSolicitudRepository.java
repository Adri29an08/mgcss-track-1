package com.mgcss.infrastructure;

import com.mgcss.domain.Solicitud;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaSolicitudRepository extends JpaRepository<Solicitud, Long> {
    
}