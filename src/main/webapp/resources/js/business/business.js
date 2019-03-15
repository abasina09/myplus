function loadDataTable(){
	//check if data table exist destroy it
	console.log(111)
	if (datatable!=null){
		datatable.destroy();
		datatable = null;
	}
	datatable = $("#table" + tableV).DataTable({
		"autoWidth" : true,
		"columnDefs" : [ {
			"targets" : [ 0 ],
			"visible" : true,
			"searchable" : true,
			"deferRender": true
		} ],
		"ajax" : {
			"url" : serverContext + "getUser" + getAll,
			"type" : "GET",
			"success" : function(data) {
				if(reload != tableV){
					//don't want to load ever DD for every row update on table
					var table = tableV.toLowerCase();
					loadUserCompanies(table);
					loadUserVenders(table);
					laodUserItemTypes(table);
					loadUserItemUnits(table);
					loadUserItems(table);
					reload=tableV;
				}				
				var collections = data.collection;
				console.log("getUser : "+getAll+" collections : "+collections);
				var arr = [" No Data Found "];
				if (getAll === "Company") {
					$.each(collections, function(ind, obj) {
						arr = [
								"<div id=companyId>"+obj.id+"</div>","<input type='checkbox' value="+ obj.id+ ">",
								"<div id=companyName>"+obj.name+"</div>", "<div id=companyPhone>"+obj.phone+"</div>", 
								"<div id=companyEmail>"+obj.email+"</div>","<div id=companyAddress>"+obj.address+"</div>",obj.updatedStr
							];
						datatable.row.add(arr).draw();
					});
				} else if (getAll === "Vender") {
					$.each(collections, function(ind, obj) {
						arr = [
							"<div id=venderId>"+obj.id+"</div>","<input type='checkbox' value="+ obj.id+ ">",
							"<div id=venderName>"+obj.name+"</div>", "<div id=venderCompanyDD>"+obj.companyName+"</div>",
							"<div id=venderPhone>"+obj.phone+"</div>", "<div id=venderMobile>"+obj.mobile+"</div>",
							"<div id=venderEmail>"+obj.email+"</div>","<div id=venderAddress>"+obj.address+"</div>",obj.datedStr
							];
						datatable.row.add(arr).draw();
					});
				} else if (getAll === "ItemType") {
					$.each(collections, function(ind, obj) {
						arr = [
							"<div id=itemTypeId>"+obj.id+"</div>","<input type='checkbox' value="+ obj.id+ ">",
							"<div id=itemTypeName>"+obj.name+"</div>","<div id=itemTypeDescription>"+obj.description+"</div>",obj.datedStr
							];
						datatable.row.add(arr).draw();
					});
				} else if (getAll === "ItemUnit") {
					$.each(collections, function(ind, obj) {
						arr = [
							"<div id=itemUnitId>"+obj.id+"</div>", "<input type='checkbox' value="+ obj.id+ ">",
							"<div id=itemUnitName>"+obj.name+"</div>", "<div id=itemUnitDescription>"+obj.description+"</div>",obj.datedStr
							];
						datatable.row.add(arr).draw();
					});
				} else if (getAll === "Item") {
					$.each(collections, function(ind, obj) {
						arr = [
							"<div id=itemId>"+obj.id+"</div>","<input type='checkbox' value="+ obj.id+ ">",
							"<div id=itemCode>"+obj.code+"</div>", "<div id=itemName>"+obj.name+"</div>", 
							"<div id=itemCompanyDD>"+obj.companyName+"</div>",  "<div id=itemVenderDD>"+obj.venderName+"</div>", 
							"<div id=itemPurchaseAmount>"+obj.purchaseAmount+"</div>","<div id=itemSellAmount>"+obj.sellAmount+"</div>",
							"<div id=discountTypeDD>"+obj.discountType+"</div>","<div id=itemDiscount>"+obj.discount+"</div>",/* "<div id=itemNet>"+obj.net+"</div>",*/
							"<div id=itemExpDate>"+obj.expDateStr+"</div>","<div id=itemStock>"+obj.stock+"</div>",obj.updatedStr
							];
						datatable.row.add(arr).draw();
					});
				} else if (getAll === "Purchase") {
					$.each(collections, function(ind, obj) {
						arr = [
							"<div id=purchaseId>"+obj.id+"</div>", "<input type='checkbox' value="+ obj.id+ ">",
							"<div id=purchaseItemDD>"+obj.itemName+"</div>", "<div id=purchaseQuantity>"+obj.quantity+"</div>", 
							"<div id=purchaseSellRate>"+obj.sellRate+"</div>",  "<div id=purchasePurchaseRate>"+obj.purchaseRate+"</div>", 
							/*"<div id=purchaseExpense>"+obj.purchaseExpense+"</div>","<div id=purchaseExpenseDesc>"+obj.purchaseExpenseDesc+"</div>",*/ 
							"<div id=purchaseTotalAmount>"+obj.totalAmount+"</div>","<div id=purchaseDiscount>"+obj.discount+"</div>",
							"<div id=purchaseNetAmount>"+obj.netAmount+"</div>", "<div id=purchaseStock>"+obj.stock+"</div>",obj.updatedStr
							];
						datatable.row.add(arr).draw();
					});
				} else if (getAll === "Sell") {
					$.each(collections, function(ind, obj) {
						arr = [
							"<div id=sellId>"+obj.id+"</div>", "<input type='checkbox' value="+ obj.id+ ">",
							"<div id=sellItemDD>"+obj.itemName+"</div>", "<div id=sellItems>"+obj.quantity+"</div>", 
							"<div id=sellSellRate>"+obj.sellRate+"</div>",  "<div id=sellPurchaseRate>"+obj.purchaseRate+"</div>", 
							/*"<div id=sellExpense>"+obj.sellExpense+"</div>","<div id=sellExpenseDesc>"+obj.sellExpenseDesc+"</div>",*/ 
							"<div id=sellTotalAmount>"+obj.totalAmount+"</div>","<div id=sellDiscount>"+obj.discount+"</div>",
							"<div id=sellNetAmount>"+obj.netAmount+"</div>", "<div id=sellStock>"+obj.stock+"</div>",obj.updatedStr
							];
						datatable.row.add(arr).draw();
					});
				}
			},
			 error: function(jqXHR, textStatus, errorThrown) {
//				 	window.location.href = serverContext + "login?message=" + data.responseJSON.message;
//	                alert('An error occurred... Look at the console (F12 or Ctrl+Shift+I, Console tab) for more information!');
	                console.log('jqXHR:');
	                console.log(jqXHR);
	                console.log('textStatus:');
	                console.log(textStatus);
	                console.log('errorThrown:');
	                console.log(errorThrown);
				 	window.location.href = serverContext + "login?message=" + errorThrown;
	            }
		}
	});
}

function loadUserCompanies(table) {	
	$("#"+table.toLowerCase()+"CompanyDD").empty().append("<option value = ''> Please Select </option>");
    $.get(serverContext+ "getUserCompanies",function(data){
   		$("#"+table.toLowerCase()+"CompanyDD").append(data);
    })
	.fail(function(data) {
		$("#"+table.toLowerCase()+"Company").empty().append("<option value = ''> System error  </option>");
	});
}

function loadUserVenders(table) {	
	$("#"+table.toLowerCase()+"VenderDD").empty().append("<option value = ''> Please Select </option>");
    $.get(serverContext+ "getUserVenders",function(data){
    	$("#"+table.toLowerCase()+"VenderDD").append(data);
    })
	.fail(function(data) {
		$("#"+table.toLowerCase()+"VenderDD").empty().append("<option value = ''> System error  </option>");
	});
}
function laodUserItemTypes(table) {	
	$("#"+table.toLowerCase()+"TypeDD").empty().append("<option value = ''> Please Select </option>");
    $.get(serverContext+ "getUserItemTypes",function(data){
    	$("#"+table.toLowerCase()+"TypeDD").append(data);
    })
	.fail(function(data) {
		$("#"+table.toLowerCase()+"TypeDD").empty().append("<option value = ''> System error  </option>");
	});
}
function loadUserItemUnits(table) {	
	$("#"+table.toLowerCase()+"UnitDD").empty().append("<option value = ''> Please Select </option>");
    $.get(serverContext+ "getUserItemUnits",function(data){
    	$("#"+table.toLowerCase()+"UnitDD").append(data);
    })
	.fail(function(data) {
		$("#"+table.toLowerCase()+"UnitDD").empty().append("<option value = ''> System error  </option>");
	});
}

function loadUserItems(table) {	
	$("#"+table.toLowerCase()+"ItemDD").empty().append("<option value = ''> Please Select </option>");
    $.get(serverContext+ "getUserItems",function(data){
    	$("#"+table.toLowerCase()+"ItemDD").append(data);
    })
	.fail(function(data) {
		$("#"+table.toLowerCase()+"ItemDD").empty().append("<option value = ''> System error  </option>");
	});
}

function loadUserItem(table) {	
	$("#"+table.toLowerCase()+"UnitDD").empty().append("<option value = ''> Please Select </option>");
    $.get(serverContext+ "getUserItemUnits",function(data){
    	$("#"+table.toLowerCase()+"UnitDD").append(data);
    })
	.fail(function(data) {
		$("#"+table.toLowerCase()+"UnitDD").empty().append("<option value = ''> System error  </option>");
	});
}

function calculateNet(val){
	//return false;
	$('#itemSellAmount').removeClass("alert-danger");
	$('#itemPurchaseAmount').removeClass("alert-danger");
	$("#itemNet").val($("#itemSellAmount").val() - $("#itemPurchaseAmount").val());// - $("#itemDiscount").val());
	
/*	if($('#discountTypeDD').val() == "Amount"){
		$("#itemNet").val($("#itemSellAmount").val() - $("#itemPurchaseAmount").val() - $("#itemDiscount").val());
	}else{
		//Discount  =  List Price × Discount Rate 
		var discount =  ($("#itemSellAmount").val() - $("#itemPurchaseAmount").val()) * ($("#itemDiscount").val()*1 / 100);
		$("#itemNet").val($("#itemSellAmount").val() - $("#itemPurchaseAmount").val() - discount);		
	}
*/	
	if(($("#itemNet").val()*1) <0){
		//$("#itemSellAmount").val(0.0);
		$('#itemSellAmount').addClass("alert-danger"); 
		$('#itemPurchaseAmount').addClass("alert-danger"); 
		var r = confirm("Please reivew your Sell and Purchase unit prices");
		if (r != true){
			$("#itemSellAmount").val(0.0);
			$("#itemPurchaseAmount").val(0.0);
		}
	}
}

var itemStock = 0;
var discountType = "%";
var discountValue = "0";
function populateData(label,value){
	$("#purchasePurchaseRate").val("");
	$("#purchaseSellRate").val("")
	$("#sellPurchaseRate").val("");
	$("#sellSellRate").val("")
	$("#sellItems").removeClass("alert-danger");
	$("pdt").html("      ");
    $.get(serverContext+ "getItem?itemId="+value,function(data){
    	console.log(data);
    	if(data){
	    	discountType = data.discountType;
	    	discountValue = data.discount;
	    	itemStock = data.stock;
    		if(value && tableV=="Purchase"){
    			$("#purchaseDiscount").val()*1>0?$("#purchaseDiscount").val():0;
		    	$("#purchasePurchaseRate").val(data.purchaseAmount);
		    	$("#purchaseSellRate").val(data.sellAmount)
		    	$("#purchaseDiscount").val(0);
		    	if($("#purchaseQuantity").val()*1<=0){
		    		$("#purchaseQuantity").val(1);
		    	}
		    	$("#pdt").html(discountType+" Discount");
		    	calculateNetPurchase();
    		}else if(value && tableV=="Sell"){
	    		if(itemStock <= 0){
	    			$("#sellItems").addClass("alert-danger");
	    			alert("No more items are available, Please purchase or select some other item to sell.");
	    			$(".form-control").val("");
	    			return false;
	    		}
	    		$("#sellPurchaseRate").val(data.purchaseAmount);
		    	$("#sellSellRate").val(data.sellAmount)
		    	$("#sellDiscount").val(discountValue);
		    	if($("#sellItems").val()*1<=0){
		    		$("#sellItems").val(1);
		    	}
		    	$("#sdt").html(discountType+" Discount");
		    	calculateNetSell();
    		}
    	}
    })
	.fail(function(data) {
		console.log(data);
	});
}

function calculateNetPurchase(){
	var p = $("#purchasePurchaseRate").val();
	var s= $("#purchaseSellRate").val();
	
	var qty= $("#purchaseQuantity").val();
	var purchaseDiscount = $("#purchaseDiscount").val()*1>0?$("#purchaseDiscount").val()*1:0;
	var purchaseTotalAmount = $($("#totalAmount").val(parseFloat(qty * p).toFixed(2))).val();
	$("#stok").val(itemStock);
	if(discountType == "%"){
		//Discount  =  List Price × Discount Rate 
		purchaseDiscount = purchaseTotalAmount * (purchaseDiscount*1 / 100);
	}

	$("#netAmount").val(parseFloat((qty * s - purchaseTotalAmount) + purchaseDiscount).toFixed(2));
	$("#purchaseTotalAmount").val(parseFloat(purchaseTotalAmount).toFixed(2));
	$("#purchaseNetAmount").val($("#netAmount").val());
}

function calculateNetSell(){
	var p = $("#sellPurchaseRate").val();
	var s= $("#sellSellRate").val();
	$("#sellItems").removeClass("alert-danger");
	
	var qty= $("#sellItems").val()*1>0?$("#sellItems").val():1;
	$("#sstok").val(itemStock);
	if(itemStock < qty){
		$("#sellItems").addClass("alert-danger");
		alert("You can not select more item than availabe in stock, Please purchase or select some other item to sell.")
		$(".form-control").val("");
		return false;
	}
	
	var sellDiscount= $("#sellDiscount").val()*1>0?$("#sellDiscount").val():0;
	sellTotalAmount = $($("#stotalAmount").val(parseFloat(qty * s).toFixed(2))).val();
	if(discountType == "%"){
		//Discount  =  List Price × Discount Rate 
		sellDiscount =  sellTotalAmount * (sellDiscount*1 / 100);
	}
	$("#snetAmount").val(sellTotalAmount- (p*qty) - sellDiscount);
	//$("#snetAmount").val($("#netAmount").val());
	
	$("#sellTotalAmount").val(sellTotalAmount);
	$("#sellNetAmount").val($("#snetAmount").val());//(totalItems * s)*1 - (1*sellDiscount - 1*sellTotalAmount));
}