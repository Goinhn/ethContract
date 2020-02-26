// 引入合约
var SimpleStorage = artifacts.require("SimpleStorage");

// 类似 describe, 返回账号列表
contract('SimpleStorage', function(accounts) {
    it("没有设置成功",function(){
        return SimpleStorage.deployed().then(function(instance){
            instance.set("hello","0xde00794b1a292dd388debbdaa107a9de1f155fe2");
            return instance.get("hello");
        }).then(function(value){
            assert.equal(value.valueOf(), "0xde00794b1a292dd388debbdaa107a9de1f155fe2", "没有设置成功");
        });
    });
});