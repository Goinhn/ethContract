pragma solidity >=0.5.5 <0.6.0;

contract Signit2{
    address public admin;

    struct Contract {
        address sender;
        address signer;
        string senderSignature;
        string signerSignature;
    }

    mapping (string => Contract) allContracts;
    uint public numOfContracts = 0;
    
    constructor() public{
        admin = msg.sender;
    }
	
    modifier onlyAdmin(){
        require(admin == msg.sender, "仅管理员可操作");
        _;
    }
	
    event NewContract(address indexed sender, address indexed signer, string contractHash);
    
    function newContract(address signer,  string memory contractHash, string memory senderSignature)public payable{
        allContracts[contractHash] = Contract(msg.sender, signer, senderSignature, "");
        numOfContracts = numOfContracts + 1;
		emit NewContract (msg.sender, signer, contractHash);
    }

    function getContractSenderSignature(string memory contractHash)public view returns (string memory){
        return allContracts[contractHash].senderSignature;
    }

    function getContractSender(string memory contractHash)public view returns (address){
        return allContracts[contractHash].sender;
    }

    function getContractSigner(string memory contractHash)public view returns (address){
        return allContracts[contractHash].signer;
    }

    function getContractSignerSignature(string memory contractHash)public view returns (string memory){
        return allContracts[contractHash].signerSignature;
    }

    function signContract(string memory contractHash, string memory signerSignature)public{
      allContracts[contractHash].signerSignature = signerSignature;
    }
	
    function getAdmin() public view returns(address){
        return admin;
    }

    function getContractCount() public view returns(uint){
        return numOfContracts;
    }
}