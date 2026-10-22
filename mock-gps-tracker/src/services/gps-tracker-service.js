const axios = require('axios');

const truck1Route = require('../data/botevgradsko-route.json');
const truck2Route = require('../data/bojurishte-route.json');
const truck3Route = require('../data/kazichene-airport-route.json');

const TRACKING_URL = process.env.TRACKING_URL || "http://localhost:8083/gps";

const ROUTES = {
    BOTEVGRADSKO: truck1Route,
    BOJURISHTE: truck2Route,
    KAZICHENE: truck3Route
};

class GpsTrackerService {

    constructor() {
        this.activeSimulations = new Map();
    }

    async startSimulation({ shipmentId, vehicleId, route }) {
        if (!shipmentId || !vehicleId) {
            return { started: false, reason: 'missing-required-fields' };
        }

        if (this.activeSimulations.has(shipmentId)) {
            return { started: false, reason: 'already-running' };
        }

        const routeKey = (route || '').trim().toUpperCase();
        const points = ROUTES[routeKey];
        if (!points) {
            return { started: false, reason: 'unknown-route' };
        }

        const simulation = {
            shipmentId,
            vehicleId,
            points,
            index: 0,
            direction: 1,
            timer: null,
            stopped: false,
            sending: false,
            requestController: null
        };
        this.activeSimulations.set(shipmentId, simulation);

        await this.sendCurrentPoint(simulation);
        if (this.activeSimulations.get(shipmentId) !== simulation) {
            return { started: false, reason: 'stopped' };
        }
        this.advanceRoute(simulation);
        simulation.timer = setInterval(() => this.tick(simulation), 5000);
        return { started: true };
    }

    stopSimulation(shipmentId) {
        const simulation = this.activeSimulations.get(shipmentId);
        if (!simulation) {
            return { stopped: false, reason: 'not-running' };
        }

        simulation.stopped = true;
        clearInterval(simulation.timer);
        simulation.requestController?.abort();
        this.activeSimulations.delete(shipmentId);
        return { stopped: true };
    }

    async tick(simulation) {
        if (simulation.sending || this.activeSimulations.get(simulation.shipmentId) !== simulation) {
            return;
        }

        await this.sendCurrentPoint(simulation);
        if (this.activeSimulations.get(simulation.shipmentId) === simulation) {
            this.advanceRoute(simulation);
        }
    }

    async sendCurrentPoint(simulation) {
        if (simulation.stopped || this.activeSimulations.get(simulation.shipmentId) !== simulation) {
            return;
        }

        const point = simulation.points[simulation.index];
        const requestController = new AbortController();
        simulation.requestController = requestController;
        simulation.sending = true;
        try {
            await axios.post(TRACKING_URL, {
                shipmentId: simulation.shipmentId,
                vehicleId: simulation.vehicleId,
                latitude: point.lat,
                longitude: point.lng,
                timestamp: new Date().toISOString()
            }, {
                signal: requestController.signal
            });
            if (!simulation.stopped) {
                console.log(`[${simulation.shipmentId} / ${simulation.vehicleId}] ${point.lat}, ${point.lng}`);
            }
        } catch (error) {
            if (!requestController.signal.aborted) {
                console.error(`[${simulation.shipmentId} / ${simulation.vehicleId}] ${error.message}`);
            }
        } finally {
            simulation.sending = false;
            simulation.requestController = null;
        }
    }

    advanceRoute(simulation) {
        if (simulation.index === simulation.points.length - 1) {
            simulation.direction = -1;
        }

        if (simulation.index === 0) {
            simulation.direction = 1;
        }

        simulation.index += simulation.direction;
    }
}

module.exports = GpsTrackerService;
