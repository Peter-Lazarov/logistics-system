package com.sunny.times.common.routes;

import jakarta.persistence.*;

@Entity
@Table(name = "route_points")
public class RoutePointEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double lat;
    private double lon;

    @ManyToOne
    @JoinColumn(name = "route_path_id")
    private RouteEntity route;

    public RoutePointEntity() {}

    public RoutePointEntity(double lat, double lon, RouteEntity route) {
        this.lat = lat;
        this.lon = lon;
        this.route = route;
    }

    public Long getId() {
        return id;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public RouteEntity getRoute() {
        return route;
    }
}
