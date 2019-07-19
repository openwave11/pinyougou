app.service('brandService', function ($http) {
    this.save = function (entity) {
        return $http.post('../brand/add.do', entity);
    };
    this.update = function (entity) {
        return $http.post('../brand/update.do', entity);
    };
    this.findOne = function (id) {
        return $http.get('../brand/findOne.do?id=' + id);
    };
    this.deletea = function (ids) {
        return $http.get('../brand/delete.do?ids=' + ids)
    };
    this.search = function (currentPage, itemsPerPage, searchEntity) {
        return $http.post('../brand/search.do?page=' + currentPage + '&rows=' + itemsPerPage, searchEntity);
    };


    this.selectOptionList = function () {
        return $http.post('../brand/selectOptionList.do');
    }

});