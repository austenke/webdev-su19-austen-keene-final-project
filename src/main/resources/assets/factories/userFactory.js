module.service('userFactory', ['$cookies', '$q', '$rootScope', '$http',
    function ($cookies, $q, $rootScope, $http) {
        let vm = this;
        let loggedIn = false;
        let user = {};

        $http({
            method: 'GET',
            url: '/api/users/refreshJWT'
        }).then(function successCallback(response) {
            console.log("Got jwt refresh success: " + JSON.stringify(response.data));
            setLoginData(response);
            $rootScope.$broadcast('loggedIn', $rootScope.parentData);
        }, function errorCallback(response) {
            logOut();
            console.log("Got jwt refresh error");

            if (typeof response === "string") {
                console.log(response);
            } else {
                console.log(JSON.stringify(response));
            }
        });

        let setLoginData = function(response) {
            console.log("Got response data: " + response.data);
            loggedIn = true;
            user = response.data;
        };

        let userLoggedIn = function() {
            return loggedIn;
        };

        let getUserName = function() {
            return user.displayName;
        };

        let getUserAccountId = function() {
            return user.accountId;
        };

        let getUserEmail = function() {
            return user.email;
        };

        let getUserEmailVerified = function() {
            return user.emailVerified;
        };

        let getAccountId = function() {
            return user.accountId;
        };

        let register = function(email, displayName, password) {
            let deferred = $q.defer();

            if (loggedIn) {
                deferred.reject("Already logged in");
            }

            $http({
                method: 'POST',
                url: '/api/users',
                data: { "email": email, "displayName": displayName, "password": password }
            }).then(function successCallback(response) {
                console.log("Got login response: " + JSON.stringify(response));
                deferred.resolve();
            }, function errorCallback(response) {
                deferred.reject(response);
            });

            return deferred.promise;
        };

        let logOut = function() {
            loggedIn = false;
            user = {};
            $cookies.remove('jwtToken');
        };

        let logIn = function(email, password) {
            let deferred = $q.defer();

            if (loggedIn) {
                deferred.reject("Already logged in");
            }

            $http({
                method: 'POST',
                url: '/api/users/login',
                data: { email: email, password: password }
            }).then(function successCallback(response) {
                setLoginData(response);
                $rootScope.$broadcast('loggedIn', $rootScope.parentData);
                deferred.resolve(response);
            }, function errorCallback(response) {
                logOut();
                console.log("Got login error");

                if (typeof response === "string") {
                    console.log(response);
                } else {
                    console.log(JSON.stringify(response));
                }

                deferred.reject(response);
            });

            return deferred.promise;
        };

        let linkAccount = function(username) {
            let deferred = $q.defer();

            if (!loggedIn) {
                deferred.reject("Not logged in");
            }

            $http({
                method: 'GET',
                url: '/api/users/link/' + username,
            }).then(function successCallback(response) {
                deferred.resolve(response);
            }, function errorCallback(response) {
                deferred.reject(response);
            });

            return deferred.promise;
        };

        return {
            getUserAccountId: () => getUserAccountId(),
            getUserEmailVerified: () => getUserEmailVerified(),
            getUserEmail: () => getUserEmail(),
            getUserName: () => getUserName(),
            userLoggedIn: () => userLoggedIn(),
            register: (email, displayName, password) => register(email, displayName, password),
            logOut: () => logOut(),
            logIn: (email, password) => logIn(email, password),
            linkAccount: (username) => linkAccount(username)
        };
    }]);