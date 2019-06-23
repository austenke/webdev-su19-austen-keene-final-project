module.controller('leaderboardController',
    ['$routeParams', '$timeout', '$location', '$anchorScroll', '$mdSidenav', '$log', 'userFactory', 'lobbyFactory', '$mdToast', '$rootScope', 'socketFactory',
        function($routeParams, $timeout, $location, $anchorScroll, $mdSidenav, $log, userFactory, lobbyFactory, $mdToast, $rootScope, socketFactory) {
            let vm = this;

            let audio = {
                "0": new Audio('/sound/go.mp3'),
                "1": new Audio('/sound/1.mp3'),
                "2": new Audio('/sound/2.mp3'),
                "3": new Audio('/sound/3.mp3'),
                "4": new Audio('/sound/4.mp3'),
                "5": new Audio('/sound/5.mp3'),
                "matchStarting": new Audio('/sound/matchStarting.mp3'),
                "1min": new Audio('/sound/1min.mp3'),
                "5min": new Audio('/sound/5min.mp3')
            };

            vm.showMatchData = false;
            vm.timer = "";
            vm.showTimer = false;
            vm.countdown = "";
            vm.showCountdown = false;
            vm.loading = true;
            vm.users = {};

            let showMatches = function() {
                vm.showMatchData = true;

            };

            $rootScope.$on('countDown', function(event, data) {
                vm.showMatchData = false;
                vm.showTimer = false;
                vm.showCountdown = true;
                let count = 7;

                audio["matchStarting"].play();

                let countdown = function () {
                    console.log("Count is " + count);

                    if (count === 0) {
                        vm.countdown = "Match starting: GOOO!";
                        vm.showMatchData = true;
                        $timeout(showMatches, 5000);
                        return;
                    } else if (count <= 5) {
                        audio[count.toString()].play();
                        vm.countdown = "Match starting: " + count;
                    }

                    count = count - 1;
                    $timeout(countdown, 1000);
                };

                countdown();
            });

            let updateLeaderboard = function() {
                lobbyFactory.getLeaderboard().then(function (result) {
                    console.log("Updating leaderboard with: " + JSON.stringify(result));
                    vm.users = result.data;
                }, function (err) {
                    console.log('Update leaderboard query failed');
                    console.log(JSON.stringify(err));

                    $mdToast.show(
                        $mdToast.simple()
                            .textContent('Encountered error updating leaderboard')
                            .position("bottom right")
                            .hideDelay(3000));
                });


                setTimeout(updateLeaderboard, 120000);
            };

            let updateTimer = function() {
                lobbyFactory.getNextMatch().then(function (result) {
                    let nextMatch = result.data;
                    console.log("nextMatch is " + nextMatch);

                    if (nextMatch == 0) {
                        vm.timer = "That's all, folks!";
                    } else {
                        let unixTimestamp = nextMatch / 1000;
                        let now =  moment();
                        let upcomingMatch =  moment.unix(unixTimestamp);

                        let hoursLeft = upcomingMatch.diff(now, 'h');

                        if (hoursLeft > 2) {
                            let dayHalf = "am";
                            let hours = date.getHours();

                            if (date.getHours() > 12) {
                                dayHalf = "pm";
                                hours = date.getHours() % 12;
                            }

                            vm.timer = "Next match will be on " + monthNames[date.getMonth()] + " " + date.getDate()
                                + " at " + hours + ":" + date.getMinutes() + dayHalf + " EST"
                        } else if (hoursLeft == 0) {
                            let minutesLeft = upcomingMatch.diff(now, 'm');

                            if (minutesLeft == 0) {
                                if (vm.timer !== "Match starting in 1 minute") {
                                    audio["1min"].play().then(function(result){
                                        // business logic with result
                                        console.log("Played")
                                    }).catch(function(e){
                                        //error handling logic
                                        console.log("Sound error " + e);
                                    });
                                }

                                vm.timer = "Match starting in 1 minute";
                            } else {
                                if (minutesLeft == 4 && vm.timer !== "Match starting in 5 minutes") {
                                    audio["5min"].play();
                                }

                                vm.timer = "Match starting in " + (minutesLeft + 1) + " minutes";
                            }
                        } else {
                            vm.timer = "Next match starts in " + (hoursLeft + 1) + " hours";
                        }
                    }
                }, function (err) {
                    console.log('Update timer query failed');
                    console.log(JSON.stringify(err));

                    $mdToast.show(
                        $mdToast.simple()
                            .textContent('Encountered error updating timer')
                            .position("bottom right")
                            .hideDelay(3000));
                });

                setTimeout(updateTimer, 30000);
            };

            console.log("Running leaderboard query");

            if (vm.loading && userFactory.userLoggedIn()) {
                console.log("User is logged in");
                updateLeaderboard();
                updateTimer();
                vm.showTimer = true;
                vm.loading = false;
            }
        }]);