function generateChart(valueSelector, chartSelector) {
    const rate = parseInt(document.querySelector(valueSelector).textContent);
    const data = {
        datasets: [{
            backgroundColor: ['rgb(30, 230, 30)', 'transparent'],
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

const rateChart = generateChart('.rate-value', '.chart-rate.rate');
const criticsRateChart = generateChart('.critics-rate-value', '.chart-rate.critics-rate');



