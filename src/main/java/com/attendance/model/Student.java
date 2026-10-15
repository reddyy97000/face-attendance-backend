package com.attendance.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "students")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String rollNo;

    private String section;

    /** Small JPEG thumbnail of the registered face (base64 data URL) for display. */
    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String photo;

    /** Serialized facial feature vector (normalized grayscale histogram + pixels). */
    @JsonIgnore
    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String faceFeatures;

    private LocalDateTime registeredAt = LocalDateTime.now();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getRollNo() { return rollNo; }
    public void setRollNo(String rollNo) { this.rollNo = rollNo; }
    public String getSection() { return section; }
    public void setSection(String section) { this.section = section; }
    public String getPhoto() { return photo; }
    public void setPhoto(String photo) { this.photo = photo; }
    public String getFaceFeatures() { return faceFeatures; }
    public void setFaceFeatures(String faceFeatures) { this.faceFeatures = faceFeatures; }
    public LocalDateTime getRegisteredAt() { return registeredAt; }
    public void setRegisteredAt(LocalDateTime registeredAt) { this.registeredAt = registeredAt; }
}
