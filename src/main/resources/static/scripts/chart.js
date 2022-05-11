function generateDoughnutChart(valueSelector, chartSelector) {
    const rate = parseInt(document.querySelector(valueSelector).textContent);
    const data = {
        datasets: [{
            backgroundColor: ['rgb(80, 220, 80)', 'transparent'],
            borderColor: ['black', 'black'],
            borderWidth: [1, 0],
            data: [rate, 10 - rate],
        }]
    };

    const config = {
        type: 'doughnut', data: data, options: {
            responsive: false, events: [], plugins: {
                legend: {
                    display: false
                }
            }
        }
    };

    return new Chart(document.querySelector(chartSelector), config);
}

function generateLineChart(chartSelector, timestamps) {
    const data = {
        labels: timestamps, datasets: [{
            label: 'Rank', data: getRanks(), fill: false, borderColor: 'rgb(80, 220, 80)'
        }]
    };
    const config = {
        type: 'line', data: data, options: {
            responsive: true, maintainAspectRatio: false, events: [], plugins: {
                legend: {
                    display: false
                }, title: {
                    display: true, text: 'Rank'
                }, datalabels: {
                    anchor: 'end', align: 'top', formatter: Math.round, font: {
                        weight: 'bold'
                    }
                }
            }, scales: {
                y: {
                    reverse: true
                }
            }
        }
    };

    return new Chart(document.querySelector(chartSelector), config);
}

function formatTo2Digits(month) {
    return (month.toString().length = 1) ? '0' + month : month
}

function getTimestamps() {
    const timestamps = [new Date(Date.parse(movie.timeOfModification))];
    movie.archivedMovies.forEach(movie => timestamps.push(new Date(Date.parse(movie.timeOfModification))));
    timestamps.forEach(ts => {
        const index = timestamps.indexOf(ts);
        timestamps[index] = ts.getDate() + '.' + formatTo2Digits(ts.getMonth()) + '.' + ts.getFullYear();
    })
    return timestamps.reverse();
}

function getRanks() {
    const ranks = [movie.position];
    movie.archivedMovies.forEach(movie => ranks.push(movie.position));
    return ranks.reverse();
}

const rateChart = generateDoughnutChart('.rate-value', '.chart-rate.rate');
const criticsRateChart = generateDoughnutChart('.critics-rate-value', '.chart-rate.critics-rate');
const historyChart = generateLineChart('.chart-history', getTimestamps(), getRanks());



