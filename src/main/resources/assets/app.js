var module = angular.module("EthChat", ['ngRoute', 'ngMaterial', 'ngCookies', 'ngWebSocket', 'ngMessages']);

module.config(['$routeProvider', '$locationProvider', function($routeProvider, $locationProvider) {
    $routeProvider.
    when('/', {
        templateUrl: './components/home/home.html'
    }).
    when('/play', {
        templateUrl: './components/leaderboard/leaderboardContainer.html'
    }).
    when('/register', {
        templateUrl: './components/register/register.html'
    }).
    when('/settings', {
        templateUrl: './components/settings/settings.html'
    }).
    otherwise({
        redirectTo: '/',
    });

    $locationProvider.html5Mode(true);
}]);

module.run(function($rootScope) {
    $rootScope.$on('$stateChangeSuccess', function(event, toState, toParams, fromState, fromParams) {
        console.log(toState);
        console.log(fromState);
    });
});