const fs = require('fs');

// 1. Дефинираме вашата хелпър функция за разреждане
function reduceRoute(route, step = 4) {
    return route.filter((_, index) => index % step === 0);
}

try {
    // 2. Четем оригиналния GeoJSON файл
    const rawData = fs.readFileSync('Kazichene - Letishte cargo.geojson', 'utf8');
    const geojson = JSON.parse(rawData);

    // 3. Намираме къде са координатите в GeoJSON структурата
    // Обикновено маршрутите са тип "LineString" или първата функция в "FeatureCollection"
    let coordinates;
    let targetGeometry;

    if (geojson.type === "FeatureCollection") {
        targetGeometry = geojson.features[0].geometry;
    } else if (geojson.type === "Feature") {
        targetGeometry = geojson.geometry;
    } else if (geojson.type === "LineString") {
        targetGeometry = geojson;
    }

    if (targetGeometry && targetGeometry.coordinates) {
        coordinates = targetGeometry.coordinates;
        
        console.log(`Оригинален брой точки: ${coordinates.length}`);
        
        // 4. Прилагаме вашата функция
        const reducedCoordinates = reduceRoute(coordinates, 4);
        
        console.log(`Нов брой точки след разреждане: ${reducedCoordinates.length}`);
        
        // 5. Заменяме старите координати с новите разредени
        targetGeometry.coordinates = reducedCoordinates;
        
        // 6. Записваме новия файл
        fs.writeFileSync('Industrialna zona Bojurishte manevriKazichene - Letishte cargo - reduced.geojson', JSON.stringify(geojson, null, 2));
        console.log("Успех! Файлът 'Botevgradko shose - Suhata Reka - reduced.geojson' е създаден.");
        
    } else {
        console.error("Неуспешно намиране на координати. Проверете структурата на GeoJSON файла.");
    }

} catch (error) {
    console.error("Възникна грешка при обработката на файла:", error.message);
}
