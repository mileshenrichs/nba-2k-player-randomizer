class UsageLogger {

    static apiUrl = 'https://4bwufafe7h.execute-api.us-east-1.amazonaws.com/default/log2kRandomizerUse';

    static sendLog(state) {
        fetch(this.apiUrl + '?state=' + JSON.stringify(state), {
            method: 'POST'
        }).then(res => {
            console.log(res);
        }).catch(err => {
            console.log('error!!!');
            console.log(err);
        });
    }
}

export default UsageLogger;