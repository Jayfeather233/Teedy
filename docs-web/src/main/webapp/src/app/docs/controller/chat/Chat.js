'use strict';

/**
 * Chat controller.
 */
angular.module('docs').controller('Chat', function($scope, Restangular, $timeout, $state) {
  Restangular.one('/user').get().then(function(currentUser) {
    $scope.currentUser = currentUser;
    $scope.groupUsers = [];

    const userIdSet = new Set(); // To prevent duplicates

    // Make sure currentUser.groups exists and is an array
    if (Array.isArray(currentUser.groups)) {
      currentUser.groups.forEach(function(groupId) {
        Restangular.one('/user/list?group=' + groupId).get().then(function(usersResponse) {
          (usersResponse.users || []).forEach(function(user) {
            if (!userIdSet.has(user.id)) {
              userIdSet.add(user.id);
              $scope.groupUsers.push({
                id: user.id,
                name: user.username
              });
            }
          });
        });
      });
    }
  });

  $scope.messages = {}; // A map: userId => list of messages

  Restangular.all('/chat').getList().then(function(chatMsgs) {
    chatMsgs.forEach(function(chatMsg) {
      const otherUserId = (chatMsg.from === $scope.currentUser.id) ? chatMsg.to : chatMsg.from;
      if (!$scope.messages[otherUserId]) {
        $scope.messages[otherUserId] = [];
      }
      $scope.messages[otherUserId].push(chatMsg);
    });
  });

  $scope.selectedUser = null;

// Get messages with selected user
  $scope.getMessagesWith = function (userId) {
    Restangular.one('chat', userId).get().then(function (chatMsgs) {
      // alert(chatMsgs);
      $scope.messages[userId] = chatMsgs;
    }, function (error) {
      console.error("Failed to fetch messages:", error);
      alert("Could not load messages for user " + userId + error);
      if (!$scope.messages[userId]) {
        $scope.messages[userId] = [];
      }
    });
  };

// Select user to chat with
  $scope.selectUser = function (user) {
    $scope.selectedUser = user;
    $scope.getMessagesWith(user.id);
    $state.go('chat.chatbox', { id: user.id });
  };

// Helper to get username by ID
  $scope.getUserNameById = function (id) {
    var user = $scope.groupUsers.find(function (u) {
      return u.id === id;
    });
    return user ? user.name : 'Unknown';
  };
});