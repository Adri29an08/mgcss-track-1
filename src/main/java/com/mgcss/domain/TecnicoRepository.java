package com.mgcss.domain;

import java.util.Optional;

public interface TecnicoRepository {

    Optional<Tecnico> findById(Long id);
    Tecnico save(Tecnico tecnico);
}