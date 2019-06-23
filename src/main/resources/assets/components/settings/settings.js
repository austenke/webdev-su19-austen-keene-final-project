module.controller('settingsController',
    ['userFactory', '$mdToast', '$rootScope', function(userFactory, $mdToast, $rootScope) {
        let vm = this;

        vm.settings = {};

        vm.settings.currentEmail = userFactory.getUserEmail();
        vm.settings.emailVerified = userFactory.getUserEmailVerified();
        console.log("Setting current email to " + vm.settings.currentEmail);
        console.log("Setting email verified to " + vm.settings.emailVerified);

        vm.hasAccount = false;
        vm.accountId = userFactory.getUserAccountId();
        vm.waitForLink = false;
        vm.challenge = "";
        vm.botName = "";

        vm.linkAccount = function() {
            userFactory.linkAccount(vm.settings.epicName).then(function success(response) {
                console.log("Got response: " + JSON.stringify(response.data));
                vm.challenge = response.data.challenge;
                vm.botName = response.data.botName;
                vm.waitForLink = true;
            }, function error(response) {
                console.log("Got error linking account: " + response);

                $mdToast.show(
                    $mdToast.simple()
                        .textContent('Encountered error linking account. Please try again')
                        .position("bottom right")
                        .hideDelay(3000));
            });
        };
    }]);