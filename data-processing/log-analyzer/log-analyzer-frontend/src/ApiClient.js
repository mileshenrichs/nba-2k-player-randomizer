import axios from 'axios';

export default class ApiClient {

    static getExistingReports() {
        return axios.get('http://localhost:4000/reports');
    }

    static getReport(reportId) {
        return axios.get('http://localhost:4000/reports/' + reportId);
    }

    static generateReport(fromDate, toDate) {
        let url = 'http://localhost:4000/generate-report';
        if(fromDate && toDate) {
            url += '?from=' + fromDate + '&to=' + toDate;
        }
        
        return axios.post(url);
    }

}