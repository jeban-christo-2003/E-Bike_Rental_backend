package com.BikeRental.VoltRide.Service;

import com.BikeRental.VoltRide.JPARepository;
import com.BikeRental.VoltRide.model.Model;
import com.BikeRental.VoltRide.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BikeService {

    @Autowired
    private JPARepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired // Add this injection
    private JwtUtil jwtUtil;

    // Change return type from boolean to String (JWT token)
    public String logincheck(Model modelObj) {
        Optional<Model> user = repository.findByUsername(modelObj.getUsername());
        if (user.isPresent() && passwordEncoder.matches(modelObj.getPassword(), user.get().getPassword())) {
            // Generate and return JWT token on successful login
            return jwtUtil.generateToken(modelObj.getUsername());
        }
        return null; // Return null if authentication fails
    }

    public void register(Model modelObj) {
        if (repository.existsByUsername(modelObj.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }
        // Encode password before saving
        modelObj.setPassword(passwordEncoder.encode(modelObj.getPassword()));
        repository.save(modelObj);
    }
}