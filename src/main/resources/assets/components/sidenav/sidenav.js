module.controller('sidenavController',
    ['$rootScope', '$timeout', '$mdSidenav', '$log', '$mdDialog', '$location', '$cookies', '$routeParams', '$mdToast', 'lobbyFactory', '$scope',
        function ($rootScope, $timeout, $mdSidenav, $log, $mdDialog, $location, $cookies, $routeParams, $mdToast, lobbyFactory, $scope) {
            let vm = this;

            $rootScope.status = "Connecting to lobby...";

            vm.addMatch = function(timestamp) {
                lobbyFactory.addMatch(timestamp).then(function (result) {
                    $mdToast.show(
                        $mdToast.simple()
                            .textContent('Added timestamp')
                            .position("bottom right")
                            .hideDelay(3000));
                }, function(err) {
                    $mdToast.show(
                        $mdToast.simple()
                            .textContent('Error adding timestamp')
                            .position("bottom right")
                            .hideDelay(3000));
                });
            };

            vm.showTimestampDialog = function() {
                // Create an empty find dialog
                let confirm = $mdDialog.prompt()
                    .title('Add match')
                    .textContent('Enter timestamp')
                    .placeholder('234587348957')
                    .ok('Add')
                    .cancel('Cancel');

                $mdDialog.show(confirm).then((result) => vm.addMatch(result));
            };

            vm.close = () => $mdSidenav('left').close();
        }]);