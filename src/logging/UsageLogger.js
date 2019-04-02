class UsageLogger {
    static apiUrl = 'https://4bwufafe7h.execute-api.us-east-1.amazonaws.com/default/log2kRandomizerUse';

    static sendLog(state) {
        fetch(this.apiUrl, {
            method: 'POST',
            mode: 'cors',
            body: JSON.stringify(state)
        });
    }
}

export default UsageLogger;