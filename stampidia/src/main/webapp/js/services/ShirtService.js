(function() {
    'use strict';
    var ShirtService = function($resource) {
	var shirt = $resource('http://localhost:8080/stampidia/rest/shirtService/:shirtText/:shirtIdColor/:shirtIdStyle/:shirtIdSize/:shirtIdStamp/:shirtIdUser', {
	    shirtText:'@shirtText',
            shirtIdColor: '@shirtIdColor',
            shirtIdStyle: '@shirtIdStyle',
            shirtIdSize: '@shirtIdSize',
            shirtIdStamp:'@shirtIdStamp', 
            shirtIdUser: '@shirtIdUser'	
	    
	}, {
	query : {
	  method : "GET",
	  isArray : false
	}
	}),
	shirtbyId = $resource('http://localhost:8080/stampidia/rest/shirtService/:shirtId', {
	    shirtId:'@shirtId'
	}, {
	query : {
	  method : "GET",
	  isArray : false
	}
	});
	return {
	    createShirt : function(p_shirtText,p_shirtIdColor,p_shirtIdStyle,p_shirtIdSize,p_shirtIdStamp,p_shirtIdUser) {
		console.log('p_shirtText '+p_shirtText);
		return shirt.query({
		    shirtText : p_shirtText,
		    shirtIdColor: p_shirtIdColor,
		    shirtIdStyle: p_shirtIdStyle,
		    shirtIdSize: p_shirtIdSize,
		    shirtIdStamp: p_shirtIdStamp,
		    shirtIdUser: p_shirtIdUser
		});
	    },
	    getShirt : function(p_shirt) {
		console.log('getShirt Shirt '+p_shirt);
		return shirtbyId.query({
		    shirtId : p_shirt
		});
		console.log('getStamp despues ');
	    }
	}
    };
    angular.module('stampidia.services').factory('shirtService', [ '$resource', ShirtService ]);
}());