module.controller('registrationController',
    ['userFactory', '$mdToast', '$location', '$rootScope', function(userFactory, $mdToast, $location, $rootScope) {
        let vm = this;

        vm.user = {};
        vm.loading = false;
        vm.registrationComplete = false;
        vm.registerAnimation = "";
        vm.registerCompleteAnimation = "";

        vm.register = function() {
            vm.loading = true;
            console.log("Registering " + vm.user.displayName);

            userFactory.register(vm.user.email, vm.user.displayName, vm.user.password).then(function success(response) {
                vm.loading = false;
                console.log("Successfully registered");
                console.log(response);
                vm.registerAnimation = "animated fadeOutLeft fast";

                setTimeout(() => {
                    $rootScope.$apply(function() {
                        console.log("Registration complete");
                        vm.registrationComplete = true;
                        vm.registerCompleteAnimation = "animated fadeInRight fast";
                    });
                }, 200);
                setTimeout(() => {
                    $rootScope.$apply(function() {
                        $location.path("/");
                    });
                }, 2000);

            }, function error(response) {
                vm.loading = false;
                console.log("Got error registering: " + JSON.stringify(response));

                let error = "Encountered error registering. Please try again.";

                if (response.data && response.data.errors) {
                    console.log("FOUND ERRORS");
                    console.log(response.data.errors);

                    error = response.data.errors[0];
                } else {
                    console.log("No errors found");
                }

                $mdToast.show(
                    $mdToast.simple()
                        .textContent(error)
                        .position("bottom right")
                        .hideDelay(3000));
            });
        };

        vm.validateEmail = function(form){
            if(vm.user.email.includes(".edu")) {
                form.email.$setValidity('emailEdu', true);
            } else {
                form.email.$setValidity('emailEdu', false);
            }
        };
    }]);