from flask import Flask, request
import pandas as pd
import py_eureka_client.eureka_client as eureka_client
import requests

rest_port = 8085
eureka_client.init(eureka_server="http://localhost:8761/eureka",
                   app_name="forecast-service",
                   instance_port=rest_port)

app = Flask(__name__)

@app.route("/forecast")
def forecast():
    owner = request.args.get('owner')
    repo = request.args.get('repo')
    username = request.args.get('username') 
    x = requests.get(f'http://performance-service/performance?owner=${owner}&repo=${repo}&username-${username}')

    json_data = response.json()
    data = json_data["performances"]
    predictions = predict_next_30_days(data)

    return predictions

if __name__ == "__main__":
    app.run(host='0.0.0.0', port = rest_port)


from sklearn.linear_model import LinearRegression
from datetime import datetime, timedelta
import numpy as np

def predict_next_30_days(data):
    metrics = [
        "issuesFixingFrequency",
        "bugFixRatio",
        "commitCount",
        "meanLeadFixTime",
        "pullRequestFrequency",
        "weeklyCommitCount",
        "openedIssuesCount",
        "allIssuesCount",
        "wontFixIssuesRatio",
        "meanPullRequestResponseTime",
        "pullRequestCount",
        "meanLeadTimeForPulls",
        "responseTimeForIssue"
    ]

    X = np.array(range(1, 31)).reshape(-1, 1)
    predicted_metrics = {}

    for metric in metrics:
        y = np.array([entry[metric] for entry in data])
        model = LinearRegression()
        model.fit(X, y)

        future_X = np.array(range(31, 61)).reshape(-1, 1)
        predictions = model.predict(future_X)

        if isinstance(data[0][metric], int):
            predictions = np.round(predictions).astype(int)

        predicted_metrics[metric] = predictions

    last_date = data[-1]["timestamp"]
    if isinstance(last_date, str):
        last_date = datetime.strptime(last_date, "%Y-%m-%d").date()

    future_results = []
    for i in range(30):
        result = {
            "issuesFixingFrequency": float(predicted_metrics["issuesFixingFrequency"][i]),
            "bugFixRatio": float(predicted_metrics["bugFixRatio"][i]),
            "commitCount": int(predicted_metrics["commitCount"][i]),
            "meanLeadFixTime": float(predicted_metrics["meanLeadFixTime"][i]),
            "pullRequestFrequency": int(predicted_metrics["pullRequestFrequency"][i]),
            "weeklyCommitCount": int(predicted_metrics["weeklyCommitCount"][i]),
            "openedIssuesCount": int(predicted_metrics["openedIssuesCount"][i]),
            "allIssuesCount": int(predicted_metrics["allIssuesCount"][i]),
            "wontFixIssuesRatio": float(predicted_metrics["wontFixIssuesRatio"][i]),
            "meanPullRequestResponseTime": int(predicted_metrics["meanPullRequestResponseTime"][i]),
            "pullRequestCount": int(predicted_metrics["pullRequestCount"][i]),
            "meanLeadTimeForPulls": float(predicted_metrics["meanLeadTimeForPulls"][i]),
            "responseTimeForIssue": float(predicted_metrics["responseTimeForIssue"][i]),
            "timestamp": last_date + timedelta(days=i + 1)
        }
        future_results.append(result)

    return future_results