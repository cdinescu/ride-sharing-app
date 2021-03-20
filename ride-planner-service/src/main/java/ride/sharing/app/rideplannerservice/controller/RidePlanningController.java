package ride.sharing.app.rideplannerservice.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ride.sharing.app.rideplannerservice.dao.service.RidePlanningService;
import ride.sharing.app.rideplannerservice.domain.Ride;
import ride.sharing.app.rideplannerservice.domain.RideRequest;

@RestController
@RequestMapping("/rides")
@AllArgsConstructor
public class RidePlanningController {

    private final RidePlanningService ridePlanningService;

    @GetMapping
    public Flux<Ride> getAllRides() {
        return ridePlanningService.findAllRides();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Ride> createRide(@RequestBody RideRequest rideRequest) {
        return ridePlanningService.createRide(rideRequest);
    }

    @PutMapping("/{id}")
    public Mono<Ride> updateRide(@PathVariable("id") Long id, @RequestBody RideRequest rideRequest) {
        return ridePlanningService.updateRide(id, rideRequest);
    }
}
