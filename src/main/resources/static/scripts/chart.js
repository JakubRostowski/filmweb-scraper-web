function generateDoughnutChart(rate, chartSelector) {
    const data = {
        datasets: [{
            backgroundColor: ['rgb(80, 220, 80)', 'transparent'],
            borderColor: ['black', 'black'],
            borderWidth: [1, 0],
            data: [rate, 10 - rate],
        }]
    };

    const config = {
        type: 'doughnut',
        data: data,
        options: {
            responsive: false,
            events: [],
            plugins: {
                legend: {
                    display: false
                }
            }
        }
    };

    return new Chart(
        document.querySelector(chartSelector),
        config
    );
}

function generateRankLineChart(chartSelector) {
    const data = {
        labels: getTimestamps(),
        datasets: [{
            label: 'Rank',
            data: getRanks(),
            fill: false,
            borderColor: 'rgb(80, 220, 80)'
        }]
    };
    const config = {
        type: 'line',
        data: data,
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: {
                    display: false
                },
                title: {
                    display: true,
                    text: 'Rank'
                }
            },
            scales: {
                y: {
                    reverse: true,
                    min: 1,
                    max: 500
                }
            }
        }
    };

    return new Chart(
        document.querySelector(chartSelector),
        config
    );
}

function generateRateLineChart(chartSelector) {
    const data = {
        labels: getTimestamps(),
        datasets: [
            {
                label: 'Rate',
                data: getRates(),
                fill: false,
                borderColor: 'yellow'
            },
            {
                label: 'Critics Rate',
                data: getCriticsRates(),
                fill: false,
                borderColor: 'orange'
            }]
    };
    const config = {
        type: 'line',
        data: data,
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                title: {
                    display: true,
                    text: 'Rating'
                }
            }
        }
    };

    return new Chart(
        document.querySelector(chartSelector),
        config
    );
}

function formatTo2Digits(month) {
    return (month.toString().length = 1) ? '0' + month : month;
}

function getTimestamps() {
    const timestamps = [];
    movie.archivedMovies.forEach(movie => timestamps.push(new Date(Date.parse(movie.timeOfModification))));
    timestamps.push(new Date(Date.parse(movie.timeOfModification)));
    timestamps.forEach(ts => {
        const index = timestamps.indexOf(ts);
        timestamps[index] = ts.getDate() + '.' + formatTo2Digits(ts.getMonth()) + '.' + ts.getFullYear();
    })
    return timestamps;
}

function getRanks() {
    const ranks = [];
    movie.archivedMovies.forEach(archivedMovie => {
        if (archivedMovie.position === -1) {
            ranks.push(null);
        } else {
            ranks.push(archivedMovie.position)
        }
    });
    ranks.push(movie.position);
    return ranks;
}

function getRates() {
    const rates = [];
    movie.archivedMovies.forEach(archivedMovie => {
        if (archivedMovie.rate === -1) {
            rates.push(null);
        } else {
            rates.push(archivedMovie.rate)
        }
    });
    rates.push(movie.rate);
    return rates;
}

function getCriticsRates() {
    const rates = [];
    movie.archivedMovies.forEach(archivedMovie => {
        if (archivedMovie.criticsRate === -1) {
            rates.push(null);
        } else {
            rates.push(archivedMovie.criticsRate)
        }
    });
    rates.push(movie.criticsRate);
    return rates;
}

const rateChart = generateDoughnutChart(movie.rate, '.chart-rate.rate');
const criticsRateChart = generateDoughnutChart(movie.criticsRate, '.chart-rate.critics-rate');
const rankHistoryChart = generateRankLineChart('.chart-history-rank', getTimestamps());
const rateHistoryChart = generateRateLineChart('.chart-history-rate', getTimestamps());



