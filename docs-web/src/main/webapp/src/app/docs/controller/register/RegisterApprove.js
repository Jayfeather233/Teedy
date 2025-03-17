'use strict';

angular.module('docs').controller('RegisterApprove', function(Restangular, $scope) {

  Restangular.all('/register/reglist').getList().then(function(response) {
    $scope.approve_list = response;
  }).catch(function(error) {
    console.error("Error fetching register list:", error);
    alert("无法加载注册请求列表，请稍后再试");
  });

});