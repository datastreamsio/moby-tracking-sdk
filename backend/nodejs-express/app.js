const express = require('express')
const bodyParser = require('body-parser');
const app = express();

// Populate JSON body data.
app.use(bodyParser.json());

app.get('/', (_req, res) => res.send('Take a peek into app.js'))

app.post('/events', (req, res) => {
    console.log(JSON.stringify(req.body, null, 2))
    res.send(JSON.stringify(req.body))
})

app.listen(3000, () => console.log('NodeJS-Express test SDK backend listening on port 3000!'))