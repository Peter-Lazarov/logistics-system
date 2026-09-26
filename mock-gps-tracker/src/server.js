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

gpsTrackerService.start();

const PORT = 3001;

app.listen(PORT, () => {
    console.log(
        `Mock GPS Tracker running on port ${PORT}`
    );
});