const axios = require('axios');

const truck1Route = require('../data/botevgradsko-route.json');
const truck2Route = require('../data/bojurishte-route.json');
const truck3Route = require('../data/kazichene-airport-route.json');

const TRACKING_URL = "http://localhost:8082/tracking/locations";

class GpsTrackerService {

    constructor() {
        this.trucks = [
            {
                vehicleId: 'CB1234AB',
                route: truck1Route,
                index: 0,
                direction: 1
            },
            {
                vehicleId: 'CB5678CD',
                route: truck2Route,
                index: 0,
                direction: 1
            },
            {
                vehicleId: 'CB9999EF',
                route: truck3Route,
                index: 0,
                direction: 1
            }
        ];
    }

    startTruck(truck, intervalMs) {

        setInterval(async () => {

            const point = truck.route[truck.index];

            try {

                await axios.post(
                    TRACKING_URL,
                    {
                        vehicleId: truck.vehicleId,
                        lat: point.lat,
                        lng: point.lng,
                        timestamp: new Date().toISOString()
                    }
                );

                console.log(
                    `[${truck.vehicleId}] ${point.lat}, ${point.lng}`
                );

            } catch (error) {

                console.error(
                    `[${truck.vehicleId}] ${error.message}`
                );
            }

            if (truck.index === truck.route.length - 1) {
                truck.direction = -1;
            }

            if (truck.index === 0) {
                truck.direction = 1;
            }

            truck.index += truck.direction;

        }, intervalMs);
    }

    sleep(ms) {
        return new Promise(resolve =>
            setTimeout(resolve, ms)
        );
    }

    async start() {
        this.startTruck(this.trucks[0], 5000);
        
        await this.sleep(1000);
        this.startTruck(this.trucks[1], 5000);
        
        await this.sleep(1000);
        this.startTruck(this.trucks[2], 5000);
    }
}

module.exports = GpsTrackerService;
