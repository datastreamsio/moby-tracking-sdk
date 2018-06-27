from flask import Flask, request, jsonify
import json

app = Flask(__name__)


@app.route('/')
def hello():
    return "Take a peek into app.py"


@app.route('/events', methods=['POST'])
def posted_events():
    print(json.dumps(request.get_json(), indent=4, sort_keys=True))
    return jsonify(request.get_json())
