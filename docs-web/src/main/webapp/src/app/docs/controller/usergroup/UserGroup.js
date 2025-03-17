'use strict';

/**
 * User/group controller.
 */
angular.module('docs').controller('UserGroup', function(Restangular, $scope, $state, $translate) {
  // Load users
  Restangular.one('user/list').get({
    sort_column: 1,
    asc: true
  }).then(function(data) {
    $scope.users = data.users;
  });

  // Load groups
  Restangular.one('group').get({
    sort_column: 1,
    asc: true
  }).then(function(data) {
    $scope.groups = data.groups;
  });

  // Open a user
  $scope.openUser = function(user) {
    $state.go('user.profile', { username: user.username });
  };

  // Open a group
  $scope.openGroup = function(group) {
    $state.go('group.profile', { name: group.name });
  };

  $scope.newUser = {
    username: '',
    password: '',
    newEmail: '',
    newDesc: ''
  };

  $scope.requestRegister = function (user) {
    if (!user.username || !user.password) {
      alert("请填写所有字段！");
      return;
    }
    Restangular.one('/register/create').post("", {
      username: user.username,
      password: user.password,
      email: user.newEmail,
      descript: user.newDesc
    }).then(function(response) {
      alert($translate.instant('usergroup.register.success'))
    }).catch(function(error) {
      alert("注册失败: " + (error.data?.message || "未知错误"));
    });
  }
});