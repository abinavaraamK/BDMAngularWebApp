var customerApp = angular.module("customerApp", ['ngRoute','ui.bootstrap']);

customerApp.config(function($routeProvider) {
	$routeProvider
		.when('/List', {
			templateUrl: 'List.html',
			controller: 'AddListController'
		})
		.when('/add', {
			templateUrl: 'Add.html',
			controller: 'addController'
		})
		.otherwise({
			redirectTo: '/List'
		});
});

customerApp.service('shareData',function($rootScope){
	
	this. showAddButton = true;
	
	
	this.setCustomer = function(customer){
		$rootScope.customer = customer ;
	};
	
	this.getCustomer = function(){
		return $rootScope.customer;
	};
   
	this.setCustomers = function(customers){
		$rootScope.customers = customers ;
	};
   
  	this.getCustomers = function(){
  		return $rootScope.customers;
  	};
  	
  	/*this.setListPage = function(val){
		this.listPage = val;
	};
	 
	this.getListPage = function(){
		return this.listPage;
	};*/
	
	this.setShowAddButton = function(val){
		this.showAddButton = val;
	};
	
	this.getShowAddButton = function(){
		return this.showAddButton;
	};
	
	/*this.showListPage = function(){
		this.setListPage(true);
	};*/
  	
});

customerApp.controller('AddListController', ['$rootScope', '$location','$scope','listCustomers', 'deleteCustomers', 'searchCustomers','shareData', function($rootScope,$location,$scope, listCustomers, deleteCustomers, searchCustomers,shareData) {
	  $scope.searchtxt = '';
	  $scope.searchField = '--Select the Field--';
	  $scope.availableOptions = ["customerid","name", "email", "phone", "address", "orders", "action","--Select the Field--"];
	
	/*   
	  $rootScope.setCustomer = function(customer){
			$rootScope.customer = customer;
	   }
	   $rootScope.getCustomer = function(){
			return $rootScope.customer;
	   }
	    $rootScope.setCustomers = function(customers){
			$rootScope.customers = customers;
			
	   }
	   $rootScope.getCustomers = function(){
			return $rootScope.customers;
	   }*/
	   /*    
	   $rootScope.setListPage = function(val){
			$rootScope.listPage = val;
	   }
	   
	  $rootScope.setShowAddButton = function(val){
			$rootScope.showAddButton = val;
	   }
	   
	  $rootScope.showListPage = function(){
			$rootScope.setListPage(true);
	   }*/
	  
	   $scope.search = function(){
			console.log("Search Value : " + $scope.searchtxt);
			console.log("Search Field : " + $scope.searchField);
			var searchSuccess = function(response){
				shareData.setCustomers(response.data);
				$scope.figureOutCustomers();
			}
			var searchFailure = function(response){
				alert(response.data);
			}
			searchCustomers.call($scope.searchtxt, $scope.searchField, searchSuccess, searchFailure);
	   }
	    $scope.clear = function(){
			 $scope.searchtxt = '';
			$scope.searchField = '--Select the Field--';
			listCustomers.call(successList, failureList);
	   }
	   
	   
	  $scope.showAddPage = function(){
		console.log("Adding new Customer");
		shareData.setCustomer({});
/*		$scope.setListPage(false);*/
		shareData.setShowAddButton(true);
		$location.path('/add' );
	  }
	  
	  $scope.deleteCustomers = function(){
		console.log("Deleting Customer(s)");
		var CustomersToBeDeleted = [];
		var customers = shareData.getCustomers();
		console.log("customers "+customers);
		for(var i=0; i < customers.length; i++)
		{
			if(customers[i].delete){
				delete customers[i]['delete'];
				CustomersToBeDeleted.push(customers[i]);
			}
		}
		console.log(CustomersToBeDeleted);
		var success = function(response){
			alert(response.data);
			listCustomers.call(successList, failureList);
		}
		var failure = function(response){
			alert("Error" + response.data);
		}
		deleteCustomers.call(CustomersToBeDeleted, success, failure);
	  }
	  
	  
	  $scope.edit = function(editCustomer,$rootScope) {
			console.log("Edit Customer : " + editCustomer.customerid );
			shareData.setCustomer(editCustomer);
		/*	$scope.setListPage(false);*/
			shareData.setShowAddButton(false);	
			$location.path("/add");
		}
		
		/*
		* Pagination
		*/
	  $scope.pagination = {
			  currentPage :1,
			  itemsPerPage : 10,
	  };
  
	  $scope.figureOutCustomers = function() {
		var begin = (($scope.pagination.currentPage - 1) * $scope.pagination.itemsPerPage);
		var end = begin + $scope.pagination.itemsPerPage;
		$scope.filteredCustomers = shareData.getCustomers().slice(begin, end);
	  };
	   
	  $scope.pageChanged = function() {
		$scope.figureOutCustomers();
	  };
	  
	  /*
	  *Shows list of customers.
	  */
	  var successList = function successCallback(response) {
				shareData.setCustomers(response.data);
				/*$scope.setListPage(true);*/
				$scope.figureOutCustomers();
		};
		var failureList = function errorCallback(response) {
				alert("Error : " + response.data);
		};
		listCustomers.call(successList, failureList);
	  
	}]);
	

customerApp.controller('addController',['$scope', '$rootScope','addCustomer','$location', 'updateCustomer','shareData', function($scope,$rootScope, addCustomer,$location, updateCustomer,shareData) {
		/* $rootScope.showAddButton = true;*/
		
		console.log($scope.showAddButton );
		
		console.log(shareData.getShowAddButton());
		
		$scope.showAddButton = shareData.getShowAddButton();
		
		console.log($scope.showAddButton );
		
		$scope.list = function(){
			console.log("navigating to List Page");
			/*$scope.setListPage(true);*/
			$location.path( '/List' );
		}
		
		var addSuccess = function successCallback(response) {
				alert("Success : " + response.data);
				console.log(shareData.getCustomer());
				shareData.getCustomers().push(shareData.getCustomer());
				console.log("customers")
				console.log(shareData.getCustomers())
				$location.path( '/List' );
		};
		
		var addFailure = function errorCallback(response) {
				alert("Error : " + response.data);
		};
		
		$scope.add = function(){
			console.log("Adding new customer to DB : " + shareData.getCustomer().customerid);
			console.log("Adding new customer to DB : " + shareData.getCustomer());
			addCustomer.call(shareData.getCustomer(),addSuccess,addFailure);			
		}
		
		var updateSuccess = function successCallback(response) {
				alert("Success : " + response.data);
				shareData.setCustomer({});
				/*$scope.setListPage(true);*/
		};
		var updateFailure = function errorCallback(response) {
				alert("Error : " + response.data);
		};
		
		$scope.update = function(){
			console.log("Updating customer to DB : " + shareData.getCustomer().customerid);
			delete shareData.getCustomer()['delete'];
			updateCustomer.call(shareData.getCustomer(),updateSuccess,updateFailure);
			$location.path('/List');
		}
	
	}]);

/*
	*	Customer Value service.
	*	This holds the value of customer object;
	*/
	customerApp.value("serverIp", " https://bdmangularwebapp.mybluemix.net/");
	/*customerApp.value("serverIp", " http://localhost:8080/");*/
	
	/*
	*	Add new customer service call.
	*	This service is used to add the new customer.
	*/
	customerApp.service('addCustomer', function($http,serverIp){
		this.call = function(customer, successCallBack, failureCallBack){
			$http({
				method: 'POST',
				url: serverIp + 'rest/ws/save',
				data: customer,
			}).then(successCallBack, failureCallBack);
		};
	});
	
	/*
	*	Search Customer service call.
	*	This service is used to Search the customer with given searchData and searchField.
	*/
	customerApp.service('searchCustomers', function($http,serverIp){
		this.call = function(searchData, searchField, successCallBack, failureCallBack){
			$http({
				method: 'GET',
				url: serverIp + 'rest/ws/search?searchValue=' + searchData + '&searchColumn=' + searchField,
			}).then(successCallBack, failureCallBack);
		};
	});
	
	/*
	*	Delete Customer service call.
	*	This service is used to delete the customer.
	*/
	customerApp.service('deleteCustomers', function($http,serverIp){
		this.call = function(customers, successCallBack, failureCallBack){
			$http({
				method: 'POST',
				url: serverIp + 'rest/ws/delete',
				data: customers,
			}).then(successCallBack, failureCallBack);
		};
	});
	
	/*
	*	Update Customer service call.
	*	This service is used to Update the customer.
	*/
	customerApp.service('updateCustomer', function($http, serverIp){
		this.call = function(customer, successCallBack, failureCallBack){
			$http({
				method: 'POST',
				url: serverIp + 'rest/ws/update',
				data: customer,
			}).then(successCallBack, failureCallBack);
		};
	});
	
	/*
	*	List Customers service call.
	*	This service is used to list all the customers.
	*/
	customerApp.service('listCustomers', function($http, serverIp){
		this.call = function(successCallBack, failureCallBack){
			$http({
				method: 'GET',
				url: serverIp + 'rest/ws/list',
			}).then(successCallBack, failureCallBack);
		};
	});


	