app.controller("itemController", function ($scope) {

    $scope.specificationItems = {};//存储用户选择的规格

    //数量增加
    $scope.addNum = function (x) {
        $scope.num = $scope.num + x;
        if ($scope.num < 1) {
            $scope.num = 1;
        }
    }

    //用户选择规格

    $scope.selectSpecification = function (name, value) {
        $scope.specificationItems[name] = value;
        //用户选择sku之后读取信息

        searchSku();
    };

    //判断用户是否选中
    $scope.isSelected = function (name, value) {
        if ($scope.specificationItems[name] == value) {
            return true;
        } else {
            return false;
        }
    };

    //加载默认 SKU
    $scope.loadSku = function () {
        $scope.sku = skuList[0];
        $scope.specificationItems = JSON.parse(JSON.stringify($scope.sku.spec));
    }

    //选择规格更新 SKU
    matchObject=function (map1, map2) {
        //如果两个sku中元素个数不相等直接返回false
        if (map1.size!=map2.size){
            return false;
        }else {
            //如果两个sku中元素个数相等，
            //对应的值不相等。返回false
            for (var k in map1) {
                if (map1[k] != map2[k]) {
                    return false;
                }
            }
        }
        return true;
    }

    //在列表中查询当前用户选择的sku

    searchSku=function () {
        for (var i = 0; i < skuList.length; i++) {
            if (matchObject(skuList[i].spec,$scope.specificationItems)){
                $scope.sku = skuList[i];
                return;
            }
        }
        //如果没有匹配的返回如下
        $scope.sku = {id: 0, title: '-----------', price: 0};
    }

    //添加购物车预留
    
    $scope.addToCart=function () {
        alert('skuid:' + $scope.sku.id);
    }


});