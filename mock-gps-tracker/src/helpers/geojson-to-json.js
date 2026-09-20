const fs = require('fs');

const INPUT_FILE = './data/Kazichene - Letishte cargo - reduced.geojson';
const OUTPUT_FILE = './data/Kazichene - Letishte cargo - reduced.json';
const TIME_STEP_SECONDS = 5; 

const rawData = fs.readFileSync(INPUT_FILE, 'utf8');
const geojson = JSON.parse(rawData);

const coordinates = geojson.geometry.coordinates;

// 3. Мапваме го до чист JSON с обърнати lat/lng
const pureJsonRoute = coordinates.map((coord, index) => {
    return {
        sequence: index + 1,
        lat: coord[1], // 1 - Latitude
        lng: coord[0], // 0 - Longitude
        timestamp_seconds: index * TIME_STEP_SECONDS
    };
});

fs.writeFileSync(OUTPUT_FILE, JSON.stringify(pureJsonRoute, null, 2));

console.log(`Успех! Превърнати са всички ${pureJsonRoute.length} точки и са записани в ${OUTPUT_FILE}`);
