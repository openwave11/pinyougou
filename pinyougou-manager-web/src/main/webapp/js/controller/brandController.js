app.controller("brandController", function ($scope, $controller, brandService) {

    $controller("baseController",{$scope:$scope});

    $scope.save = function () {
        var object = null;
        if ($scope.entity.id != null) {
            object = brandService.update($scope.entity);
        } else {
            object = brandService.save($scope.entity);
        }
        object.success(
            function (response) {
                if (response.success) {
                    $scope.reloadList();
                } else {
                    alert(response.message);
                }
            }
        )
    };


    $scope.findOne = function (id) {
        brandService.findOne(id).success(
            function (response) {
                $scope.entity = response;
            }
        )
    };


    $scope.deletea = function () {
        brandService.deletea($scope.ids).success(
            function (date) {
                if (date.success) {
                    $scope.reloadList();
                } else {
                    alert(date.message);
                }
            }
        )
    };
    $scope.searchEntity = {};
    $scope.searchDate = function (currentPage, itemsPerPage, searchEntity) {

        brandService.searchDate($scope.paginationConf.currentPage, $scope.paginationConf.itemsPerPage, $scope.searchEntity).success(
            function (data) {
                $scope.list = data.list;
                $scope.paginationConf.totalItems = data.total;
            }
        );
    };



});