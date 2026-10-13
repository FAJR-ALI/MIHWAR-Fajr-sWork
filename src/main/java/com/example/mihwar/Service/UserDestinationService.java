package com.example.mihwar.Service;

import com.example.mihwar.Model.UserDestinationProgress;
import com.example.mihwar.Repository.UserDestinationProgressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDestinationService {

    private final UserDestinationProgressRepository progressRepository;

    // Get all progress records
    public List<UserDestinationProgress> getAllProgress() {
        return progressRepository.findAll();
    }

    // Get progress by ID
    public UserDestinationProgress getProgressById(Long id) {
        return progressRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Progress record not found"));
    }

    // Add progress record
    public UserDestinationProgress addProgress(
            UserDestinationProgress progress) {
        return progressRepository.save(progress);
    }

    // Update progress record
    public UserDestinationProgress updateProgress(
            Long id, UserDestinationProgress progress) {

        UserDestinationProgress oldProgress =
                progressRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException("Progress record not found"));

        oldProgress.setUserId(progress.getUserId());
        oldProgress.setDestination(progress.getDestination());
        oldProgress.setStatus(progress.getStatus());
        oldProgress.setVisitedAt(progress.getVisitedAt());

        return progressRepository.save(oldProgress);
    }

    // Delete progress record
    public void deleteProgress(Long id) {
        UserDestinationProgress progress = getProgressById(id);
        progressRepository.delete(progress);
    }
}
