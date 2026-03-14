package com.eventmgmt.model;
import lombok.*;
import jakarta.persistence.*;
@Entity
@Table(name = "services")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OurService {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private String imageUrl;
    private double price;
    private String includes;

    // Optional parent
    @ManyToOne
    @JoinColumn(name = "parent_service_id")
    private OurService parentService;

    // Getters and Setters
}
