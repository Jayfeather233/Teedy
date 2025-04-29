'use strict';

angular.module('docs').controller('RegisterList', function(Restangular, $scope, $translate) {

  $scope.tmessage = {
    id: '',
    status: '',
    isShow: false,
    message: ''
  }
  $scope.filterStatus = "all";
  $scope.filtered_list = [];
  $scope.sortColumn = 'create_date';
  $scope.reverseSort = true;
  $scope.loadData = function() {
    Restangular.all('/register/all').getList().then(function(response) {
      $scope.all_list = response;
      $scope.filterByStatus($scope.filterStatus);
    }).catch(function(error) {
      console.error("Error fetching register list:", error);
      alert("无法加载注册请求列表，请稍后再试");
    });
  }
  $scope.loadData();

  $scope.sortData = function(column) {
    if ($scope.sortColumn === column) {
      $scope.reverseSort = !$scope.reverseSort;
    } else {
      $scope.sortColumn = column;
      $scope.reverseSort = false;
    }
  };

  $scope.filterByStatus = function(status) {
    $scope.filterStatus = status;
    if (status === 'all') {
      $scope.filtered_list = $scope.all_list;
    } else {
      $scope.filtered_list = $scope.all_list.filter(function(item) {
        if (status === 'passed') {
          return item.delete_date !== 'null';
        } else if (status === 'rejected') {
          return item.disable_date !== 'null';
        } else if (status === 'waiting') {
          return item.delete_date === 'null' && item.disable_date === 'null';
        }
      });
    }
  };
  // approve 函数，调用 accept API
  $scope.approve = function(item) {
    Restangular.all('/register/accept').post("", { id: item.id })
        .then(function(response) {
          $scope.tmessage.status = 'success';
          $scope.tmessage.message = $translate.instant('register.status.success');
          $scope.tmessage.isShow = true;
          $scope.tmessage.id = item.id;

          setTimeout(function () { if($scope.tmessage.id === item.id){ $scope.tmessage.isShow = false; $scope.$apply();} }, 2000);
          $scope.loadData();
        })
        .catch(function(error) {
          $scope.tmessage.status = 'failed';
          $scope.tmessage.message = $translate.instant('register.status.failed') + error;
          $scope.tmessage.isShow = true;
          $scope.tmessage.id = item.id;

          setTimeout(function () { if($scope.tmessage.id === item.id){ $scope.tmessage.isShow = false; $scope.$apply();} }, 2000);
        });
  };

  // deny 函数，调用 deny API
  $scope.deny = function(item) {
    Restangular.all('/register/deny').post("", { id: item.id })
        .then(function(response) {
          $scope.tmessage.status = 'success';
          $scope.tmessage.message = $translate.instant('register.status.success');
          $scope.tmessage.isShow = true;
          $scope.tmessage.id = item.id;

          setTimeout(function () { if($scope.tmessage.id === item.id){ $scope.tmessage.isShow = false; $scope.$apply();} }, 2000);
          $scope.loadData();
        })
        .catch(function(error) {
          $scope.tmessage.status = 'failed';
          $scope.tmessage.message = $translate.instant('register.status.failed') + error;
          $scope.tmessage.isShow = true;
          $scope.tmessage.id = item.id;

          setTimeout(function () { if($scope.tmessage.id === item.id){ $scope.tmessage.isShow = false; $scope.$apply();} }, 2000);
        });
  };

});