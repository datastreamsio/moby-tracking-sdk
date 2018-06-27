# Backend - Python Flask
Python Flask backend for debugging the mobile tracker SDK.

This backend logs tracked events to stdout.

## Installation
### Requirements
* Python (3.6+ recommended)

### Instructions

```
$ pip install -r requirements.txt
```

## Running

### On BASH
`$ FLASK_APP=app.py flask run`

### On FISH
`$ FLASK_APP=app.py flask run`

#### Expected output
```
 * Serving Flask app "app.py"
 * Environment: production
   WARNING: Do not use the development server in a production environment.
   Use a production WSGI server instead.
 * Debug mode: off
 * Running on http://127.0.0.1:5000/ (Press CTRL+C to quit)
```
