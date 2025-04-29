'use strict';

/**
 * Chatbox controller.
 */
angular.module('docs').controller('Chatbox', function($stateParams, Restangular, $interval, $scope) {
  // alert("chatbox loaded, id=" + $stateParams.id);
  // $stateParams.id
  $scope.newMessage = "";
  $scope.sendMessage = function () {
    if ($scope.newMessage) {
      Restangular.one('/chat').put({
        userid:  $stateParams.id,
        message: $scope.newMessage
      }).then(function (chatMsg) {
        // Message accepted by server, now update local message map
        if (!$scope.messages[$stateParams.id]) {
          $scope.messages[$stateParams.id] = [];
        }

        $scope.messages[$stateParams.id].push(chatMsg);
        $scope.newMessage = "";
      }, function (error) {
        console.error("Failed to send message:", error);
        alert("Failed to send message.");
      });
    }
  };

  $scope.getMessages = function (id) {
    const msgs = $scope.messages[id] || [];
    return msgs.slice().sort(function(a, b) {
      return a.create_time-b.create_time;
    });
  };

  const pollInterval = 2000;
  const poller = $interval(function () {
    $scope.getMessagesWith($stateParams.id);
  }, pollInterval);

  // Clean up when leaving the page
  $scope.$on('$destroy', function () {
    if (poller) {
      $interval.cancel(poller);
    }
  });
});