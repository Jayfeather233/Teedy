'use strict';

angular.module('docs').controller('RegisterList', function(Restangular, $scope) {

  $scope.filterStatus = "all";
  $scope.filtered_list = [];
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
  $scope.approve = function(id) {
    Restangular.all('/register/accept').post("", { id: id })
        .then(function(response) {
          // 成功处理
          alert("审批通过！");
          $scope.loadData();
        })
        .catch(function(error) {
          console.error("审批失败:", error);
          alert("审批操作失败，请稍后再试");
        });
  };

  // deny 函数，调用 deny API
  $scope.deny = function(id) {
    Restangular.all('/register/deny').post("", { id: id })
        .then(function(response) {
          // 成功处理
          alert("已拒绝！");
          $scope.loadData();
        })
        .catch(function(error) {
          console.error("拒绝操作失败:", error);
          alert("拒绝操作失败，请稍后再试");
        });
  };

});