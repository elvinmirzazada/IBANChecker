const apiUrl = 'http://localhost:8080/api/v1/IBAN'
var vm = new Vue({
    el: '#history',
    data() {
        return {
            history_list: [],
            filters: ["ALL", "VALID", "INVALID"],
            selectedFilter: "ALL"
        }
    },
    methods: {

        get_list_of_ibans: function () {
            const requestOptions = {
                method: 'GET'
            };
            fetch(apiUrl+"/listOfAllIbans/" + this.selectedFilter, requestOptions)
                .then(response => {
                    return response.json()
                })
                .then(res => {
                    var tmp = [];
                    for (var i=0; i<res.length; i++){
                        tmp.push(res[i].ibanNumber + ": " + (res[i].isValid == 1 ? "VALID": "INVALID"))
                    }
                    this.history_list = tmp
                })
        }

    },
    created: function() {
        this.get_list_of_ibans()
    }
});