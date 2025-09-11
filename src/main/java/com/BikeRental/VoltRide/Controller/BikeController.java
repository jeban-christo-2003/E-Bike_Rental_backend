package com.BikeRental.VoltRide.Controller;

import com.BikeRental.VoltRide.Service.BikeService;
import com.BikeRental.VoltRide.model.Model;
import com.BikeRental.VoltRide.model.Vehicle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000") // or "*" while testing
@RestController
public class BikeController {

    @Autowired
    private BikeService service;

    @GetMapping("/")
    public String index() { return "BikeRental"; }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Model modelObj) {
        try {
            String jwtToken = service.logincheck(modelObj); // Now returns String (token)
            if (jwtToken != null) {
                return ResponseEntity.ok(Map.of(
                        "success", true,
                        "message", "Login successful",
                        "token", jwtToken // Include JWT token in response
                ));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("success", false, "message", "Invalid credentials"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "message", "Server error: " + e.getMessage()));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody Model modelObj) {
        try {
            service.register(modelObj);
            return ResponseEntity.ok(Map.of("success", true, "message", "User registered successfully"));
        } catch (org.springframework.dao.DataIntegrityViolationException ex) {
            // duplicate username
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("success", false, "message", "Username already exists"));
        } catch (IllegalArgumentException iae) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("success", false, "message", iae.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "message", "Registration failed: " + e.getMessage()));
        }
    }


    @GetMapping("/vehicles")
    public List<Vehicle> vehicleList() {
        return List.of(new Vehicle(1, "E-Volt-Bike"), new Vehicle(2, "EcoRider"), new Vehicle(3, "VoltRacer"), new Vehicle(4, "VoltDrifter"), new Vehicle(5, "VoltDragster"), new Vehicle(6, "VoltTanker"), new Vehicle(7, "VoltLorry"), new Vehicle(8, "VoltMan"));
    }
}
