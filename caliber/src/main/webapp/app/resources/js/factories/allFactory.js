angular.module("api").factory("allFactory", function($log, $http){

    $log.debug("Booted all api factory");

    var all = {};

    /*************************** Batch ************************/
    all.createBatch = function(batchObj){
        $http({
            url: "/all/batch/create",
            method: "POST",
            data: batchObj
        }).then(function (response) {
            $log.debug(response);
            // update ui
        }, function (response) {
            $log.error("There was an error: " + response.status);
        });
    };

    all.updateBatch = function(batchObj){
        $http({
            url: "/all/batch/update",
            method: "PUT",
            data: batchObj
        }).then(function (response) {
            $log.debug(response);
            // update ui
        }, function (response) {
            $log.error("There was an error: " + response.status);
        });
    };

    all.deleteBatch = function(batchId){
        $http({
            url: "/all/batch/delete" + batchId,
            method: "DELETE"
        }).then(function (response) {
            $log.debug(response);
            // update ui
        }, function (response) {
            $log.error("There was an error: " + response.status);
        });
    };

    /*************************** Trainee ************************/
    all.createTrainee = function(traineeObj){
        $http({
            url: "/all/trainee/create",
            method: "POST",
            data: traineeObj
        }).then(function (response) {
            $log.debug(response);
            // update ui
        }, function (response) {
            $log.error("There was an error: " + response.status);
        });
    };

    all.updateTrainee = function(traineeObj){
        $http({
            url: "/all/trainee/update",
            method: "PUT",
            data: traineeObj
        }).then(function (response) {
            $log.debug(response);
            // update ui
        }, function (response) {
            $log.error("There was an error: " + response.status);
        });
    };

    all.deleteTrainee = function(traineeId){
        $http({
            url: "/all/trainee/delete/" + traineeId,
            method: "DELETE"
        }).then(function (response) {
            $log.debug(response);
            // update ui
        }, function (response) {
            $log.error("There was an error: " + response.status);
        });
    };

    /************************** Grades **************************/

    all.getGrades = function(assessmentId){
        var data = [];
        $http({
            url: "/all/grades/assessment/" + assessmentId,
            method: "GET"
        }).then(function (response) {
            $log.debug(response);
            // copy response data
            angular.copy(response.data, data);
        }, function (response) {
            $log.error("There was an error: " + response.status);
        });
        return data;
    };

    return all;
});
