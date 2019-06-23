module.controller('homeController',
    ['$rootScope', 'userFactory', '$mdToast', function($rootScope, userFactory, $mdToast) {
        let vm = this;

        let showVerifyEmailToast = function() {
            if (userFactory.userLoggedIn() && !userFactory.getUserEmailVerified()) {
                $mdToast.show($mdToast.simple()
                    .textContent('Please verify your email. This is required to play matches.')
                    .position("bottom right")
                    .hideDelay(0)
                    .toastClass("emailVerifyToast"));
            }
        };

        showVerifyEmailToast();

        $rootScope.$on('loggedIn', () => showVerifyEmailToast());
    }]);