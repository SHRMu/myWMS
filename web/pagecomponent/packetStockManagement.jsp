<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<script>

var packetStockin_packet = null;// 预报的包裹ID
var packetStockin_goods = null;// 预报货物ID
var packetStockin_number = null;// 预报的数量

var packetCache = new Array();//包裹信息缓存
var goodsCache = new Array();//货物信息缓存

$(function(){
	repositorySelectorInit();

	packetAutocomplete();
	goodsAutocomplete();
    dataValidateInit();

	packetStockInOption();

})

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

// 包裹信息自动匹配
function packetAutocomplete(){
	$('#packet_trace').autocomplete({
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
					searchType:'searchByTrace'
				},
				success : function(data){
					var autoCompleteInfo = new Array();
					$.each(data.rows, function(index,elem){
						packetCache.push(elem);
						autoCompleteInfo.push({label:elem.trace,value:elem.id});
					});
					response(autoCompleteInfo);
				}
			});
		},
		focus : function(event, ui){
			$('#packet_trace').val(ui.item.label);
			return false;
		},
		select : function(event, ui){
			$('#packet_trace').val(ui.item.label);
			packetStockin_packet = ui.item.value;
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
			packetStockin_goods = ui.item.value;
			return false;
		}
	})
}

// 数据校验
function dataValidateInit(){
    $('#packetStockin_form').bootstrapValidator({
        message : 'This is not valid',
        fields : {
            packetStockin_input : {
                validators : {
                    notEmpty : {
                        message : '数量不能为空'
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

// 执行货物预报操作
function packetStockInOption(){
	$('#submit').click(function(){
		// data validate
		$('#packetStockin_form').data('bootstrapValidator').validate();
		if (!$('#packetStockin_form').data('bootstrapValidator').isValid()) {
			return;
		}
		if (packetStockin_packet == null){
			$('#addPacketWarning_modal').modal('show');
			$('#addPacket_confirm').click(function(){
				var data = {
					trace : $('#packet_trace').val(),
					desc : "",
					repositoryID : $('#repository_selector').val()
				}
				// ajax
				$.ajax({
					type : "POST",
					url : "packetManage/addPacket",
					dataType : "json",
					contentType : "application/json",
					data : JSON.stringify(data),
					success : function(response) {
						$('#addPacketWarning_modal').modal("hide");
						var msg;
						var type;
						if (response.result == "success") {
							packetStockInAction();
						} else if (response.result == "error") {
							type = "error";
							msg = "包裹添加失败";
						}
						infoModal(type, msg);
					},
					error : function(response) {

					}
				})
			})
		}else{
			packetStockInAction();
		}
	});
}

function packetStockInAction() {
	var data = {
		trace: $('#packet_trace').val(),
		repositoryID: $('#repository_selector').val(),
		goodsID: packetStockin_goods,
		number: $('#packetStockin_input').val()
	}
	$.ajax({
		type : 'POST',
		url : 'packetManage/packetStockIn',
		dataType : 'json',
		content : 'application/json',
		data : data,
		success : function(response){
			var msg;
			var type;

			if(response.result == "success"){
				type = 'success';
				msg = '货物预报成功';
				inputReset();
			}else{
				type = 'error';
				msg = '货物预报失败'
			}
			infoModal(type, msg);
		},
		error : function(response){
			var msg = "服务器错误";
			var type = "error";
			infoModal(type, msg);
		}
	})
}

// 页面重置
function inputReset(){
	$('#packet_trace').val('');
	$('#goods_input').val('');
	$('#packetStockin_input').val('');
	$('#info_storage').text('-');
	$('#packetStockin_form').bootstrapValidator("resetForm",true);
}

//todo:完善批量操作时界面切换功能
function batchPacketStockInOption() {
	alert("请进入库存管理-包裹库存管理界面")
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

</script>

<div class="panel panel-default">
	<ol class="breadcrumb">
		<li>客户预报</li>
	</ol>
	<div class="panel-body" id="packetStockin_div">
		<div class="row" style="margin-bottom: 25px">
			<div class="col-md-6 col-sm-6">
				<div class="row">
					<div class="col-md-1 col-sm-1"></div>
					<div class="col-md-10 col-sm-11">
						<form action="" class="form-inline" >
							<div class="form-group">
								<label for="" class="form-label">包裹运单：</label>
								<input type="text" class="form-control" id="packet_trace" placeholder="请输入运单号信息" >
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
						<form action="" class="form-inline">
							<div class="form-group">
								<label for="" class="form-label">收货仓库：</label>
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
						<form action="" class="form-inline">
							<div class="form-group">
								<label for="" class="form-label">预报货物：</label>
								<input type="text" class="form-control" id="goods_input" placeholder="请输入货物名称">
							</div>
						</form>
					</div>
				</div>
			</div>
			<div class="col-md-6 col-sm-6">
				<div class="row">
					<div class="col-md-1 col-sm-1"></div>
					<div class="col-md-10 col-sm-11">
						<form action="" class="form-inline" id="packetStockin_form">
							<div class="form-group">
								<label for="" class="control-label">预报数量：</label>
								<input type="text" class="form-control" placeholder="请输入数量" id="packetStockin_input" name="packetStockin_input">
							</div>
						</form>
					</div>
				</div>
			</div>
		</div>
		<div class="row" style="margin-top:80px"></div>
	</div>
	<div class="panel-footer">
		<div class="row">
			<div class="col-md-6 col-sm-6">
				<div class="row">
					<div class="col-md-1 col-sm-1"></div>
					<div class="col-md-10 col-sm-11">
						<div style="text-align: left">
							<button class="btn btn-success" onclick="batchPacketStockInOption()">批量操作</button>
						</div>
					</div>
				</div>
			</div>
			<div class="col-md-6 col-sm-6">
				<div class="row">
					<div class="col-md-1 col-sm-1"></div>
					<div class="col-md-10 col-sm-11">
						<div style="text-align: right">
							<button class="btn btn-success" id="submit">提交预报</button>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>

<!-- 添加包裹信息确认提示框 -->
<div class="modal fade" id="addPacketWarning_modal" table-index="-1"
	 role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button class="close" type="button" data-dismiss="modal"
						aria-hidden="true">&times;</button>
				<h4 class="modal-title" id="myModalLabel">警告</h4>
			</div>
			<div class="modal-body">
				<div class="row">
					<div class="col-md-3 col-sm-3" style="text-align: center;">
						<img src="media/icons/warning-icon.png" alt=""
							 style="width: 70px; height: 70px; margin-top: 20px;">
					</div>
					<div class="col-md-8 col-sm-8">
						<h3>该包裹信息不存在预报记录，是否添加该包裹信息</h3>
						<p>(注意：请确认该包裹信息为初次预报，避免重复信息)</p>
					</div>
				</div>
			</div>
			<div class="modal-footer">
				<button class="btn btn-default" type="button" data-dismiss="modal">
					<span>取消</span>
				</button>
				<button class="btn btn-danger" type="button" id="addPacket_confirm">
					<span>确认添加</span>
				</button>
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