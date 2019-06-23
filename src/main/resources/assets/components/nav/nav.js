module.controller('navController',
    ['$routeParams', '$location', '$scope', 'userFactory', '$rootScope', '$window', '$mdToast',
        function($routeParams, $location, $scope, userFactory, $rootScope, $window, $mdToast) {
            let vm = this;
            vm.userName = "";
            vm.loggedIn = false;
            vm.emailVerified = true;

            vm.currentNavItem = $location.path();
            $scope.$on('$locationChangeSuccess', () => vm.currentNavItem = $location.path());

            if (vm.userName === "") {
                console.log("Setting name to " + userFactory.getUserName());
                vm.userName = userFactory.getUserName();

                if (vm.userName !== undefined) {
                    vm.loggedIn = true;
                    vm.emailVerified = userFactory.getUserEmailVerified();
                }
            }

            vm.goToRegister = function() {
                $location.path("/register");
            };

            vm.goToSettings = function(menu, event) {
                $location.path("/settings");
            };

            vm.logIn = function() {
                console.log("Logging in");
                userFactory.logIn(vm.login.email, vm.login.password).then(function success(response) {
                    vm.loggedIn = true;
                    $location.path("/");
                    console.log(response);
                }, function error(response) {
                    console.log("Got login error");
                    console.log(response);

                    if (response.data && response.data.errors) {
                        console.log("FOUND ERRORS");
                        console.log(response.data.errors);

                        error = response.data.errors[0];

                        $mdToast.show(
                            $mdToast.simple()
                                .textContent('Encountered error logging in: ' + error)
                                .position("bottom right")
                                .hideDelay(3000));
                    } else {
                        console.log("No errors found");

                        $mdToast.show(
                            $mdToast.simple()
                                .textContent('Encountered error logging in. Please try again.')
                                .position("bottom right")
                                .hideDelay(3000));
                    }
                });
            };

            vm.logOut = function() {
                console.log("Logging out");
                userFactory.logOut();
                $location.path("/");
                $window.location.reload()
            };

            $rootScope.$on('loggedIn', function(event, data) {
                console.log("Setting name to " + userFactory.getUserName());
                vm.userName = userFactory.getUserName();
                vm.loggedIn = true;
            });
        }]);