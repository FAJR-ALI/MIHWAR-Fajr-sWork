package com.example.mihwar.Repository;

import com.example.mihwar.Enum.DestinationStatus;
import com.example.mihwar.Model.Destination;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DestinationRepository extends JpaRepository<Destination, Long> {

    long countByStatus(DestinationStatus status);
}
