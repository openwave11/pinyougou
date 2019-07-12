app.controller('searchController', function ($scope, searchService) {

    // $controller('baseController', {$scope: $scope});//继承

    $scope.search = function () {
        searchService.search($scope.searchMap).success(
            function (response) {
                $scope.resultMap = response;
            })
    }

});