module.service('socketFactory', ['$cookies', '$q', '$rootScope', 'userFactory', '$http', '$websocket',
    function ($cookies, $q, $rootScope, userFactory, $http, $websocket) {
        let vm = this;

        let audio = {
            "valid": new Audio('/sound/valid.mp3'),
            "invalid": new Audio('/sound/invalid.mp3')
        };

        $rootScope.status = "";
        $rootScope.lobbyUsers = [];
        $rootScope.matchData = {};

        let joinLobby = function() {
            let deferred = $q.defer();

            if (!userFactory.userLoggedIn()) {
                deferred.reject("Not logged in");
            }

            $http({
                method: 'GET',
                url: '/api/lobby/join',
            }).then(function successCallback(response) {
                console.log("Got successful lobby join response " + JSON.stringify(response));
                deferred.resolve(response.data);
            }, function errorCallback(response) {
                deferred.reject("Got lobby join failure response: " + response);
            });

            return deferred.promise;
        };

        let handleMessage = function(message) {
            $rootScope.$apply(function() {
                console.log("Got socket message " + message.data);

                let req = JSON.parse(message.data);

                $rootScope.status = req.message;

                switch (req["type"]) {
                    case "CONNECTED":
                        console.log("Successfully connected");
                        break;
                    case "LOBBY_USERS":
                        console.log("users in socket are " + JSON.stringify(req.users));
                        $rootScope.lobbyUsers = req.users;
                        break;
                    case "MATCH_UPDATE":
                        if (req.matchId in $rootScope.matchData) {
                            $rootScope.matchData[req.matchId].users.concat(req.users);
                        } else {
                            $rootScope.matchData[req.matchId] = {"id": req.matchId, "users": req.users};
                        }
                        break;
                    case "COUNTDOWN":
                        console.log("Counting down");
                        $rootScope.$broadcast('countDown', $rootScope.parentData);
                        break;
                    case "MATCH_VALID":
                        audio["valid"].play();
                        console.log("Match valid");
                        break;
                    case "MATCH_INVALID":
                        audio["invalid"].play();
                        console.log("Match invalid");
                        break;
                    default:
                        console.log("Unknown type " + req["type"]);
                        break;
                }
            });
        };

        let openSocket = function(retries) {
            if (vm.socket) {
                console.log("SOCKET ALREADY EXISTS");
                return;
            }

            // Open a WebSocket connection
            joinLobby().then(function (result) {
                console.log("Join lobby query returned: " + JSON.stringify(result));

                vm.socket = $websocket('ws://localhost:8080/ws/socket/' + result);

                vm.socket.onMessage(function (message) {
                    handleMessage(message);
                });

                vm.socket.onClose(function () {
                    console.log("Socket closed");
                    vm.socket = null;
                    $rootScope.status = "Disconnected from lobby";

                    if (retries < 4) {
                        openSocket(retries + 1);
                    } else {
                        console.log("Retried " + retries + " times, could not connect to socket")
                    }
                });

                vm.socket.onError(function (message) {
                    console.log("Got socket error: " + message);
                });
            }, function (err) {
                console.log('Join lobby query failed: ' + err);
            });
        };

        $rootScope.$on('loggedIn', function(event, data) {
            console.log("Socket factory got logged in event");
            openSocket(0);
        });

        if (userFactory.userLoggedIn()) {
            console.log("Logged into socket factory");
            openSocket(0);
        }
    }]);