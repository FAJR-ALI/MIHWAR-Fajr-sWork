package com.example.mihwar.Controller;

import com.example.mihwar.Model.UserDestinationProgress;
import com.example.mihwar.Service.UserDestinationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user-destination-progress")
@RequiredArgsConstructor
public class UserDestinationController {


    private final UserDestinationService progressService;

    // Get all progress records
    @GetMapping
    public ResponseEntity<?> getAllProgress() {
        return ResponseEntity.status(200)
                .body(progressService.getAllProgress());
    }

    // Get progress by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getProgressById(@PathVariable Long id) {
        return ResponseEntity.status(200)
                .body(progressService.getProgressById(id));
    }

    // Add progress record
    @PostMapping
    public ResponseEntity<?> addProgress(
            @RequestBody UserDestinationProgress progress) {
        return ResponseEntity.status(201)
                .body(progressService.addProgress(progress));
    }

    // Update progress record
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProgress(
            @PathVariable Long id,
            @RequestBody UserDestinationProgress progress) {
        return ResponseEntity.status(200)
                .body(progressService.updateProgress(id, progress));
    }

    // Delete progress record
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProgress(@PathVariable Long id) {
        progressService.deleteProgress(id);
        return ResponseEntity.status(200)
                .body("Progress record deleted successfully");
    }
}
