const express = require('express');

const app = express();

app.use(express.json());

app.get('/health', (req, res) => {
    res.json({
        service: 'mock-gps-tracker',
        status: 'UP'
    });
});

const PORT = 3001;

app.listen(PORT, () => {
    console.log(`Mock GPS Tracker running on port ${PORT}`);
});
