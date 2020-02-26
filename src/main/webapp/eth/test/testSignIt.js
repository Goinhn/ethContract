// 引入合约
var Signit2 = artifacts.require("Signit2");

// 类似 describe, 返回账号列表
contract('Signit2', async(accounts) => {
    it("sender设置成功", async()=>{
        let instance = await Signit2.deployed();
        await instance.newContract(accounts[0],"a","b");
        let sender = await instance.getContractSender("a");
        assert.equal(sender,accounts[0]);
    });

    it("senderSignature设置成功", async()=>{
        let instance = await Signit2.deployed();
        await instance.newContract(accounts[0],"a","b");
        let senderSignature = await instance.getContractSenderSignature("a");
        assert.equal(senderSignature,"b");
    });

    it("signer设置成功", async()=>{
        let instance = await Signit2.deployed();
        await instance.newContract(accounts[0],"a","b");
        let signer = await instance.getContractSigner("a");
        assert.equal(signer,accounts[0]);
    });

    it("signerSignature设置成功", async()=>{
        let instance = await Signit2.deployed();
        await instance.newContract(accounts[0],"a","b");
        let signerSignature = await instance.getContractSignerSignature("a");
        assert.equal(signerSignature,"");
    });

    it("signContract方法执行成功", async()=>{
        let instance = await Signit2.deployed();
        await instance.newContract(accounts[0],"a","b");
        await instance.signContract("a","c")
        let signerSignature = await instance.getContractSignerSignature("a");
        assert.equal(signerSignature,"c");
    });

    it("合同数目能正常增加", async()=>{
        let instance = await Signit2.deployed();
        var num1 = await instance.getContractCount();
        num1 = num1.toNumber();
        await instance.newContract(accounts[0],"a","b");
        var num2 = await instance.getContractCount();
        num2 = num2.toNumber();
        assert.equal(num1+1,num2);
    });
});