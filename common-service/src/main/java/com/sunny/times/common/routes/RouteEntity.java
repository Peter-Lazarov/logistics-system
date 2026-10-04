package com.sunny.times.common.routes;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "routes")
public class RouteEntity {

    @Id
    private String pathId;

    private String origin;

    @Column(name = "destination")
    private String destination;

    private String estimatedTime;

    @OneToMany(
            mappedBy = "route",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER
    )
    private List<RoutePointEntity> points = new ArrayList<>();

    public RouteEntity() {}

    public RouteEntity(String pathId, String origin, String destination, String estimatedTime) {
        this.pathId = pathId;
        this.origin = origin;
        this.destination = destination;
        this.estimatedTime = estimatedTime;
    }

    public String getPathId() {
        return pathId;
    }

    public String getOrigin() {
        return origin;
    }

    public String getDestination() {
        return destination;
    }

    public String getEstimatedTime() {
        return estimatedTime;
    }

    public List<RoutePointEntity> getPoints() {
        return points;
    }
}
