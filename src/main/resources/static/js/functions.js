const apiUrl = 'http://localhost:8080/api/v1/IBAN'
var vm = new Vue({
    el: '#app',
    data() {
        return {
            single_iban_is_valid: '',
            multi_ibans: '',
            iban: '',
            multi_ibans_response: []
        }
    },
    methods: {

        check_single_iban: function () {
            const requestOptions = {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ "ibanNumber": this.iban })
            };
            fetch(apiUrl+"/validateIban", requestOptions)
                .then(response => {
                    return response.json()
                })
                .then(res => {
                    this.single_iban_is_valid = res.ibanNumber + ": " + (res.isValid == 1 ? "VALID": "INVALID")
                })
        },

        generate_json_body: function (params) {
            var json_str = "[";
            for (let i=0; i<params.length; i++) {
                json_str += "{";
                json_str += '"ibanNumber": "' + params[i] + '"';
                json_str += "},"
            }
            return json_str.substr(0, json_str.length-1) + "]"
        },

        escapeHTML: function (str) {
            return str.replace(/&/g, "&amp;").replace(/</g, "&lt;");
        },

        check_multi_iban: function () {
            const requestOptions = {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: this.generate_json_body(this.multi_ibans.split('\n'))
            };
            fetch(apiUrl + "/validateMultiIbans", requestOptions)
                .then(response => {
                    return response.json()
                })
                .then(res => {
                    var tmp = [];
                    for (var i=0; i<res.length; i++){
                        tmp.push(res[i].ibanNumber + ": " + (res[i].isValid == 1 ? "VALID": "INVALID"))
                    }
                    this.multi_ibans_response = tmp
                })
        }
    }
});
