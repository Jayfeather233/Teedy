'use strict';

/**
 * Chatbox controller.
 */
angular.module('docs').controller('Chatbox', function($stateParams, Restangular, $scope) {
  alert("chatbox loaded, id=" + $stateParams.id);
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
    let msgs = $scope.messages[id] || [];
    return msgs.slice().sort(function(a, b) {
      return a.create_time-b.create_time;
    });
  };
});