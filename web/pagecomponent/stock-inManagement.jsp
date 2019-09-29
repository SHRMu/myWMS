<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<script>

var stockin_packet = null;
var stockin_batch = null;

var stockin_customer = null;// 入库供应商编号
var stockin_goods = null;// 入库货物编号

var packetCache = new Array();//包裹信息缓存
var customerCache = new Array();// 客户信息缓存
var goodsCache = new Array();//货物信息缓存

$(function(){
	packetAutocomplete();
	batchSelectorInit();
	repositorySelectorInit();
	detilInfoToggle();
	fetchStorage();
	customerAutocomplete();
	goodsAutocomplete();
    dataValidateInit();
	stockInOption();
})

// 包裹信息自动匹配
function packetAutocomplete(){
	$('#packet_input').autocomplete({
		minLength : 0,
		delay : 200,
		source : function(request, response){
			$.ajax({
				type : 'GET',
				url : 'packetManage/getPacketList',
				dataType : 'json',
				contentType : 'application/json',
				data : {
					offset : -1,
					limit : -1,
					keyWord : request.term,
					repositoryID:$('#repository_selector').val(),
					searchType:'searchRefActive'
				},
				success : function(data){
					var autoCompleteInfo = new Array();
					$.each(data.rows, function(index,elem){
						packetCache.push(elem);
						autoCompleteInfo.push({label:elem.trace,value:elem.refid});
					});
					response(autoCompleteInfo);
				}
			});
		},
		focus : function(event, ui){
			$('#packet_input').val(ui.item.label);
			return false;
		},
		select : function(event, ui){
			$('#packet_input').val(ui.item.label);
			stockin_packet = ui.item.value;
			return false;
		}
	})
}

//当前可用的批次初始化
function batchSelectorInit() {
	$.ajax({
		type : 'GET',
		url : 'repositoryBatchManage/getBatchList',
		dataType : 'json',
		contentType : 'application/json',
		data : {
			searchType : 'searchByActive',
			keyWord : '',
			offset : -1,
			limit : -1
		},
		success : function(response){
			$.each(response.rows,function(index,elem){
				$('#batch_selector').append("<option value='" + elem.id + "'>第 " + elem.id +" 批次</option>");
			});
		},
		error : function(response){
			$('#batch_selector').append("<option value='-1'>加载失败</option>");
		}
	})
}

// 仓库下拉列表初始化
function repositorySelectorInit(){
	$.ajax({
		type : 'GET',
		url : 'repositoryManage/getRepositoryList',
		dataType : 'json',
		contentType : 'application/json',
		data : {
			searchType : 'searchAll',
			keyWord : '',
			offset : -1,
			limit : -1
		},
		success : function(response){
			$.each(response.rows,function(index,elem){
				$('#repository_selector').append("<option value='" + elem.id + "'>" + elem.id +"号仓库</option>");
			});
		},
		error : function(response){
			$('#repository_selector').append("<option value='-1'>加载失败</option>");
		}

	})
}

//客户信息自动匹配
function customerAutocomplete(){
	$('#customer_input').autocomplete({
		minLength : 0,
		delay : 200,
		source : function(request, response){
			$.ajax({
				type : 'GET',
				url : 'customerManage/getCustomerList',
				dataType : 'json',
				contentType : 'application/json',
				data : {
					offset : -1,
					limit : -1,
					keyWord : request.term,
					searchType : 'searchByName'
				},
				success : function(data){
					var autoCompleteInfo = Array();
					$.each(data.rows,function(index,elem){
						customerCache.push(elem);
						autoCompleteInfo.push({label:elem.name,value:elem.id});
					});
					response(autoCompleteInfo);
				}
			});
		},
		focus : function(event,ui){
			$('#customer_input').val(ui.item.label);
			return false;
		},
		select : function(event,ui){
			$('#customer_input').val(ui.item.label);
			stockin_customer = ui.item.value;
			customerInfoSet(stockin_customer);
			return false;
		}
	})
}

// 货物信息自动匹配
function goodsAutocomplete(){
	$('#goods_input').autocomplete({
		minLength : 0,
		delay : 200,
		source : function(request, response){
			$.ajax({
				type : 'GET',
				url : 'goodsManage/getGoodsList',
				dataType : 'json',
				contentType : 'application/json',
				data : {
					offset : -1,
					limit : -1,
					keyWord : request.term,
					searchType : 'searchByName'
				},
				success : function(data){
					var autoCompleteInfo = new Array();
					$.each(data.rows, function(index,elem){
						goodsCache.push(elem);
						autoCompleteInfo.push({label:elem.name,value:elem.id});
					});
					response(autoCompleteInfo);
				}
			});
		},
		focus : function(event, ui){
			$('#goods_input').val(ui.item.label);
			return false;
		},
		select : function(event, ui){
			$('#goods_input').val(ui.item.label);
			stockin_goods = ui.item.value;
			goodsInfoSet(stockin_goods);
			loadStorageInfo();
			return false;
		}
	})
}

function goodsInfoSet(goodsID){
	var detailInfo;
	$.each(goodsCache,function(index,elem){
		if(elem.id == goodsID){
			detailInfo = elem;
			if(detailInfo.id==null)
				$('#info_goods_ID').text('-');
			else
				$('#info_goods_ID').text(detailInfo.id);

			if(detailInfo.name==null)
				$('#info_goods_name').text('-');
			else
				$('#info_goods_name').text(detailInfo.name);

			if(detailInfo.type==null)
				$('#info_goods_type').text('-');
			else
				$('#info_goods_type').text(detailInfo.type);

			if(detailInfo.size==null)
				$('#info_goods_size').text('-');
			else
				$('#info_goods_size').text(detailInfo.size);

			if(detailInfo.value==null)
				$('#info_goods_value').text('-');
			else
				$('#info_goods_value').text(detailInfo.value);
		}
	})
}

function customerInfoSet(customerID){
	var detailInfo;
	$.each(customerCache,function(index,elem){
		if(elem.id == customerID){
			detailInfo = elem;

			if(detailInfo.id == null)
				$('#info_customer_ID').text('-');
			else
				$('#info_customer_ID').text(detailInfo.id);

			if(detailInfo.name == null)
				$('#info_customer_name').text('-');
			else
				$('#info_customer_name').text(detailInfo.name);

			if(detailInfo.tel == null)
				$('#info_customer_tel').text('-');
			else
				$('#info_customer_tel').text(detailInfo.tel);

			if(detailInfo.address == null)
				$('#info_customer_address').text('-');
			else
				$('#info_customer_address').text(detailInfo.address);

			if(detailInfo.email == null)
				$('#info_customer_email').text('-');
			else
				$('#info_customer_email').text(detailInfo.email);

			if(detailInfo.personInCharge == null)
				$('#info_customer_person').text('-');
			else
				$('#info_customer_person').text(detailInfo.personInCharge);

		}
	})
}

//详细信息
function detilInfoToggle(){
	$('#info-show').click(function(){
		$('#detailInfo').removeClass('hide');
		$(this).addClass('hide');
		$('#info-hidden').removeClass('hide');
	});

	$('#info-hidden').click(function(){
		$('#detailInfo').removeClass('hide').addClass('hide');
		$(this).addClass('hide');
		$('#info-show').removeClass('hide');
	});
}

//获取待入库库存
function fetchStorage(){
	$('#batch_selector').change(function(){
		stockin_batch = $(this).val();
		loadStorageInfo();
	});
}

function loadStorageInfo(){
	stockin_repository = $('#repository_selector').val();
	if(stockin_packet != null && stockin_repository != null && stockin_goods != null){
		$.ajax({
			type : 'GET',
			url : 'packetStorageManage/getStorageList',
			dataType : 'json',
			contentType : 'application/json',
			data : {
				offset : -1,
				limit : -1,
				searchType : 'searchByGoodsIDPacketID',
				packetInfo : stockin_packet,
				repositoryID : stockin_repository,
				keyword : stockin_goods
			},
			success : function(response){
				if(response.total > 0){
					number = response.rows[0].number;
					storage = response.rows[0].storage;
					$('#packet_number').text(number);
					$('#packet_storage').text(storage);
				}else{
					$('#packet_number').text('0');
					$('#packet_storage').text('0');
				}
			},
			error : function(response){
			}
		})
	}
}

// 数据校验
function dataValidateInit(){
    $('#stockin_form').bootstrapValidator({
        message : 'This is not valid',
        fields : {
            packet_input : {
                validators : {
                    notEmpty: {
                        message: '运单号不能为空'
                    }
                }
            },
            stockin_input : {
                validators : {
                    notEmpty : {
                        message : '入库数量不能为空'
                    },
                    greaterThan: {
                        value: 0,
                        message: '入库数量不能小于0'
                    }
                }
            }
        }
    })
}

// 执行货物入库操作
function stockInOption(){
	$('#submit').click(function(){
		// data validate
		$('#stockin_form').data('bootstrapValidator').validate();
		if (!$('#stockin_form').data('bootstrapValidator').isValid()) {
			return;
		}
		data = {
            packetID: stockin_packet,
			customerID: stockin_customer,
			batchID : $('#batch_selector').val(),
            repositoryID: stockin_repository,
            goodsID: stockin_goods,
            number: $('#stockin_input').val()
        }
		$.ajax({
			type : 'POST',
			url : 'stockRecordManage/stockIn',
			dataType : 'json',
			content : 'application/json',
			data : data,
			success : function(response){
				var msg;
				var type;
				
				if(response.result == "success"){
					type = 'success';
					msg = '货物入库成功';
					inputReset();
				}else{
					type = 'error';
					msg = '货物入库失败'
				}
				infoModal(type, msg);
			},
			error : function(response){
				var msg = "服务器错误";
				var type = "error";
				infoModal(type, msg);
			}
		})
	});
}

// 页面重置
function inputReset(){
	$('#packet_input').val('');
	$('#customer_input').val('');
	$('#goods_input').val('');
	$('#stockin_input').val('');
	$('#stockin_form').bootstrapValidator("resetForm",true); 
}

//操作结果提示模态框
function infoModal(type, msg) {
	$('#info_success').removeClass("hide");
	$('#info_error').removeClass("hide");
	if (type == "success") {
		$('#info_error').addClass("hide");
	} else if (type == "error") {
		$('#info_success').addClass("hide");
	}
	$('#info_content').text(msg);
	$('#info_modal').modal("show");
}

function batchStockInOption() {
	alert("请切换至-批量操作-到库库存下");
}

</script>

<div class="panel panel-default">
	<ol class="breadcrumb">
		<li>货物入库</li>
	</ol>
	<div class="panel-body" id="stockin_div">
		<div class="row">
			<div class="col-md-6 col-sm-6">
				<div class="row">
					<div class="col-md-1 col-sm-1"></div>
					<div class="col-md-10 col-sm-11">
						<form action="" class="form-inline" >
							<div class="form-group">
								<label for="" class="form-label">包裹运单：</label>
								<input type="text" class="form-control" placeholder="请输入运单号信息" id="packet_input" name="packet_input">
							</div>
						</form>
					</div>
				</div>
			</div>
		</div>
		<div class="row" style="margin-top: 25px;">
			<div class="col-md-6 col-sm-6">
				<div class="row">
					<div class="col-md-1 col-sm-1"></div>
					<div class="col-md-10 col-sm-11">
						<form action="" class="form-inline">
							<div class="form-group">
								<label for="" class="form-label">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;客户：</label>
								<input type="text" class="form-control" id="customer_input" placeholder="请输入客户名称">
							</div>
						</form>
					</div>
				</div>
			</div>
			<div class="col-md-6 col-sm-6">
				<div class="row">
					<div class="col-md-1 col-sm-1"></div>
					<div class="col-md-10 col-sm-11">
						<form action="" class="form-inline">
							<div class="form-group">
								<label for="" class="form-label">入库货物：</label>
								<input type="text" class="form-control" id="goods_input" placeholder="请输入货物名称">
							</div>
						</form>
					</div>
				</div>
			</div>
		</div>
		<div class="row visible-md visible-lg">
			<div class="col-md-12">
				<div class='pull-right' style="cursor:pointer" id="info-show">
					<span>显示详细信息</span>
					<span class="glyphicon glyphicon-chevron-down"></span>
				</div>
				<div class='pull-right hide' style="cursor:pointer" id="info-hidden">
					<span>隐藏详细信息</span>
					<span class="glyphicon glyphicon-chevron-up"></span>
				</div>
			</div>
		</div>
		<div class="row hide" id="detailInfo" style="margin-bottom:30px">
			<div class="col-md-6  visible-md visible-lg">
				<div class="row">
					<div class="col-md-1"></div>
					<div class="col-md-10">
						<label for="" class="text-info">客户信息</label>
					</div>
				</div>
				<div class="row">
					<div class="col-md-1"></div>
					<div class="col-md-11">
						<div class="col-md-6">
							<div style="margin-top:5px">
								<div class="col-md-6">
									<span for="" class="pull-right">客户ID：</span>
								</div>
								<div class="col-md-6">
									<span id="info_customer_ID">-</span>
								</div>
							</div>
							<div style="margin-top:5px">
								<div class="col-md-6">
									<span for="" class="pull-right">负责人：</span>
								</div>
								<div class="col-md-6">
									<span id="info_customer_person">-</span>
								</div>
							</div>
							<div style="margin-top:5px">
								<div class="col-md-6">
									<span for="" class="pull-right">电子邮件：</span>
								</div>
								<div class="col-md-6">
									<span id="info_customer_email">-</span>
								</div>
							</div>
						</div>
						<div class="col-md-6">
							<div style="margin-top:5px">
								<div class="col-md-6">
									<span for="" class="pull-right">客户名：</span>
								</div>
								<div class="col-md-6">
									<span id="info_customer_name">-</span>
								</div>
							</div>
							<div style="margin-top:5px">
								<div class="col-md-6">
									<span for="" class="pull-right">联系电话：</span>
								</div>
								<div class="col-md-6">
									<span id="info_customer_tel">-</span>
								</div>
							</div>
							<div style="margin-top:5px">
								<div class="col-md-6">
									<span for="" class="pull-right">联系地址：</span>
								</div>
								<div class="col-md-6">
									<span id="info_customer_address">-</span>
								</div>
							</div>

						</div>
					</div>
				</div>
			</div>
			<div class="col-md-6 col-sm-6  visible-md visible-lg">
				<div class="row">
					<div class="col-md-1 col-sm-1"></div>
					<div class="col-md-11 col-sm-11">
						<label for="" class="text-info">货物信息</label>
					</div>
				</div>
				<div class="row">
					<div class="col-md-1"></div>
					<div class="col-md-11">
						<div class="col-md-6">
							<div style="margin-top:5px">
								<div class="col-md-6">
									<span for="" class="pull-right">货物ID：</span>
								</div>
								<div class="col-md-6">
									<span id="info_goods_ID">-</span>
								</div>
							</div>
							<div style="margin-top:5px">
								<div class="col-md-6">
									<span for="" class="pull-right">货物类型：</span>
								</div>
								<div class="col-md-6">
									<span id="info_goods_type">-</span>
								</div>
							</div>
							<div style="margin-top:5px">
								<div class="col-md-6">
									<span for="" class="pull-right">货物名：</span>
								</div>
								<div class="col-md-6">
									<span id="info_goods_name">-</span>
								</div>
							</div>
						</div>
						<div class="col-md-6">
							<div style="margin-top:5px">
								<div class="col-md-6">
									<span for="" class="pull-right">货物规格：</span>
								</div>
								<div class="col-md-6">
									<span id="info_goods_size">-</span>
								</div>
							</div>
							<div style="margin-top:5px">
								<div class="col-md-6">
									<span for="" class="pull-right">货物价值：</span>
								</div>
								<div class="col-md-6">
									<span id="info_goods_value">-</span>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="row" style="margin-top: 25px">
			<div class="col-md-6 col-sm-6">
				<div class="row">
					<div class="col-md-1 col-sm-1"></div>
					<div class="col-md-10 col-sm-11">
						<form action="" class="form-inline">
							<div class="form-group">
								<label for="" class="form-label">可用批次：</label>
								<select name="" id="batch_selector" class="form-control">
									<%--									<option value="">请选择仓库</option>--%>
								</select>
							</div>
						</form>
					</div>
				</div>
			</div>
			<div class="col-md-6 col-sm-6">
				<div class="row">
					<div class="col-md-1 col-sm-1"></div>
					<div class="col-md-10 col-sm-11">
						<form action="" class="form-inline">
							<div class="form-group">
								<label for="" class="form-label">入库仓库：</label>
								<select name="" id="repository_selector" class="form-control">
									<%--									<option value="">请选择仓库</option>--%>
								</select>
							</div>
						</form>
					</div>
				</div>
			</div>
		</div>
		<div class="row" style="margin-top: 25px">
			<div class="col-md-6 col-sm-6">
				<div class="row">
					<div class="col-md-1 col-sm-1"></div>
					<div class="col-md-10 col-sm-11">
						<form action="" class="form-inline" id="stockin_form">
							<div class="form-group">
								<label for="" class="control-label">入库数量：</label>
								<input type="text" class="form-control" placeholder="请输入数量" id="stockin_input" name="stockin_input">

								<%--								<span>)</span>--%>
							</div>
						</form>
					</div>
				</div>
			</div>
		</div>
		<div class="row" style="margin-top: 25px">
			<div class="col-md-6 col-sm-6">
				<div class="row">
					<div class="col-md-1 col-sm-1"></div>
					<div class="col-md-10 col-sm-11">
						<div class="form-group">
							<span>预报数量</span>
							<span id="packet_number"> - </span>
							<span>到货数量：</span>
							<span id="packet_storage"> - </span>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="row" style="margin-top:80px">

		</div>
	</div>
	<div class="panel-footer">
		<div class="row">
			<div class="col-md-6 col-sm-6">
				<div class="row">
					<div class="col-md-1 col-sm-1"></div>
					<div class="col-md-10 col-sm-11">
						<div style="text-align: left">
							<button class="btn btn-success" onclick="batchStockInOption()">批量操作</button>
						</div>
					</div>
				</div>
			</div>
			<div class="col-md-6 col-sm-6">
				<div class="row">
					<div class="col-md-1 col-sm-1"></div>
					<div class="col-md-10 col-sm-11">
						<div style="text-align: right">
							<button class="btn btn-success" id="submit">提交入库</button>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>

<!-- 提示消息模态框 -->
<div class="modal fade" id="info_modal" table-index="-1" role="dialog"
	aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button class="close" type="button" data-dismiss="modal"
					aria-hidden="true">&times;</button>
				<h4 class="modal-title" id="myModalLabel">信息</h4>
			</div>
			<div class="modal-body">
				<div class="row">
					<div class="col-md-4 col-sm-4"></div>
					<div class="col-md-4 col-sm-4">
						<div id="info_success" class=" hide" style="text-align: center;">
							<img src="media/icons/success-icon.png" alt=""
								style="width: 100px; height: 100px;">
						</div>
						<div id="info_error" style="text-align: center;">
							<img src="media/icons/error-icon.png" alt=""
								style="width: 100px; height: 100px;">
						</div>
					</div>
					<div class="col-md-4 col-sm-4"></div>
				</div>
				<div class="row" style="margin-top: 10px">
					<div class="col-md-4 col-sm-4"></div>
					<div class="col-md-4 col-sm-4" style="text-align: center;">
						<h4 id="info_content"></h4>
					</div>
					<div class="col-md-4 col-sm-4"></div>
				</div>
			</div>
			<div class="modal-footer">
				<button class="btn btn-default" type="button" data-dismiss="modal">
					<span>&nbsp;&nbsp;&nbsp;关闭&nbsp;&nbsp;&nbsp;</span>
				</button>
			</div>
		</div>
	</div>
</div>