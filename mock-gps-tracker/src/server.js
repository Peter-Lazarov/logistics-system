const express = require('express');
const GpsTrackerService = require('./services/gps-tracker-service');

const app = express();

app.use(express.json());

app.get('/health', (req, res) => {
    res.json({
        service: 'mock-gps-tracker',
        status: 'UP'
    });
});

const gpsTrackerService = new GpsTrackerService();

app.post('/simulations/start', async (req, res) => {
    const result = await gpsTrackerService.startSimulation(req.body);
    if (result.reason === 'missing-required-fields' || result.reason === 'unknown-route') {
        return res.status(400).json({ message: 'shipmentId, vehicleId and a valid route are required.' });
    }

    return res.status(result.reason === 'already-running' ? 200 : 202).json({
        shipmentId: req.body.shipmentId,
        vehicleId: req.body.vehicleId,
        status: result.reason === 'already-running' ? 'ALREADY_RUNNING' : 'STARTED'
    });
});

app.post('/simulations/stop', (req, res) => {
    const result = gpsTrackerService.stopSimulation(req.body.shipmentId);
    return res.status(200).json({
        shipmentId: req.body.shipmentId,
        status: result.stopped ? 'STOPPED' : 'NOT_RUNNING'
    });
});

const PORT = 3001;

app.listen(PORT, () => {
    console.log(
        `Mock GPS Tracker running on port ${PORT}`
    );
});