package com.example.sen_scu.controller.sen_csu;

import com.example.sen_scu.service.sen_csu.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    /**
     * GET /api/dashboard/stats?year=2025
     *
     * Returns all dashboard statistics for the given year.
     * If year is omitted, defaults to the current year.
     */
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getStats(
            @RequestParam(value = "year", required = false) Integer year) {
        Map<String, Object> stats = dashboardService.getDashboardStats(year);
        return ResponseEntity.ok(stats);
    }
}
