module.service('lobbyFactory', ['$cookies', '$q', '$rootScope', 'userFactory', '$http', function ($cookies, $q, $rootScope, userFactory, $http) {
    let vm = this;

    let getNextMatch = function() {
        let deferred = $q.defer();

        if (!userFactory.userLoggedIn()) {
            deferred.reject("Not logged in");
        }

        $http({
            method: 'GET',
            url: '/api/lobby/nextMatch',
        }).then(function successCallback(response) {
            deferred.resolve(response);
        }, function errorCallback(response) {
            deferred.reject(response);
        });

        return deferred.promise;
    };

    let getLeaderboard = function() {
        let deferred = $q.defer();

        if (!userFactory.userLoggedIn()) {
            deferred.reject("Not logged in");
        }

        $http({
            method: 'GET',
            url: '/api/lobby/leaderboard',
        }).then(function successCallback(response) {
            deferred.resolve(response);
        }, function errorCallback(response) {
            deferred.reject(response);
        });

        return deferred.promise;
    };

    let resetLeaderboard = function() {
        let deferred = $q.defer();

        if (!userFactory.userLoggedIn()) {
            deferred.reject("Not logged in");
        }

        $http({
            method: 'GET',
            url: '/api/lobby/leaderboard/reset',
        }).then(function successCallback(response) {
            deferred.resolve(response);
        }, function errorCallback(response) {
            deferred.reject(response);
        });

        return deferred.promise;
    };

    let addMatch = function(timestamp) {
        let deferred = $q.defer();

        if (!userFactory.userLoggedIn()) {
            deferred.reject("Not logged in");
        }

        $http({
            method: 'GET',
            url: '/api/lobby/add/' + timestamp,
        }).then(function successCallback(response) {
            deferred.resolve(response);
        }, function errorCallback(response) {
            deferred.reject(response);
        });

        return deferred.promise;
    };

    return {
        getNextMatch: () => getNextMatch(),
        getLeaderboard: () => getLeaderboard(),
        resetLeaderboard: () => resetLeaderboard(),
        addMatch: (timestamp) => addMatch(timestamp)
    };
}]);