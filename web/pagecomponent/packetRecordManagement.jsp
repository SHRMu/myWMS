<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>

<script>

    // 检测记录查询
    search_packetID = "";
    search_packetID = "";
    search_repositoryID = "";
    search_start_date = null;
    search_end_date = null;

    var packetCache = new Array();//包裹信息缓存

    $(function(){
        packetAutocomplete();
        repositoryOptionInit();
        datePickerInit();
        storageListInit();
        searchAction();
    })

    // 包裹信息自动匹配
    function packetAutocomplete(){
        $('#search_packet_trace').autocomplete({
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
                        repositoryID:$('#search_repository_ID').val(),
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
                $('#search_packet_trace').val(ui.item.label);
                return false;
            },
            select : function(event, ui){
                $('#search_packet_trace').val(ui.item.label);
                search_packetID = ui.item.value;
                return false;
            }
        })
    }

    // 仓库下拉框数据初始化
	function repositoryOptionInit(){
		$.ajax({
			type : 'GET',
			url : 'repositoryManage/getRepositoryList',
			dataType : 'json',
			contentType : 'application/json',
			data:{
				searchType : "searchAll",
				keyWord : "",
				offset : -1,
				limit : -1
			},
			success : function(response){
				$.each(response.rows,function(index,elem){
					$('#search_repository_ID').append("<option value='" + elem.id + "'>" + elem.id +"号仓库</option>");
				})
			},
			error : function(response){
			}
		});
	}

	// 日期选择器初始化
	function datePickerInit(){
		$('.form_date').datetimepicker({
			format:'yyyy-mm-dd',
			language : 'zh-CN',
			endDate : new Date(),
			weekStart : 1,
			todayBtn : 1,
			autoClose : 1,
			todayHighlight : 1,
			startView : 2,
			forceParse : 0,
			minView:2
		});
	}

	// 表格初始化
	function storageListInit() {
		$('#stockRecords')
				.bootstrapTable(
						{
							columns : [
									{
										field : 'id',
										title : '包裹ID',
                                        visible : false
									},
                                    {
                                        field : 'trace',
                                        title : '运单号'
                                        // sortable: true
                                    },
                                    {
                                        field : 'desc',
                                        title : '子运单'
                                        //sortable: true
                                    },
                                    {
                                        field : 'goodsID',
                                        title : '商品ID',
                                        visible : false
                                    },
									{
										field : 'goodsName',
										title : '商品名称'
									},
                                    {
                                        field : 'goodsNumber',
                                        title : '预报数量'
                                    },
                                    {
                                        field : 'goodsStorage',
                                        title : '到货数量'
                                    },
									{
										field : 'time',
										title : '发货日期'
									},
                                    {
                                        field : 'status',
                                        title : '包裹状态'
                                    },
									{
										field : 'personInCharge',
										title : '经手人',
                                        visible : false
									}
									 ],
							url : 'packetManage/searchPacketRecord',
							method : 'GET',
							queryParams : queryParams,
							sidePagination : "server",
							dataType : 'json',
							pagination : true,
							pageNumber : 1,
							pageSize : 5,
							pageList : [ 5, 10, 25, 50, 100 ],
							clickToSelect : true
						});
	}

	// 表格刷新
	function tableRefresh() {
		$('#stockRecords').bootstrapTable('refresh', {
			query : {}
		});
	}

	// 分页查询参数
	function queryParams(params) {
		var temp = {
			limit : params.limit,
			offset : params.offset,
			// searchType : search_type,
            packetID : search_packetID,
			repositoryID : search_repositoryID,
			startDate : search_start_date,
			endDate : search_end_date
		}
		return temp;
	}

	// 查询操作
	function searchAction(){
	    $('#search_button').click(function(){
	        search_packetID = search_packetID;
	        search_repositoryID = $('#search_repository_ID').val();
	        search_start_date = $('#search_start_date').val();
	        search_end_date = $('#search_end_date').val();
	        tableRefresh();
	    })
	}
</script>

<div class="panel panel-default">
    <ol class="breadcrumb">
        <li>包裹预报流水</li>
    </ol>
    <div class="panel-body">
        <div class="row">
            <div class="col-md-4">
                <form action="" class="form-inline">
                    <div class="form-group">
                        <label class="form-label">包裹运单号：</label>
                        <input class="form-control" id="search_packet_trace">
                    </div>
                </form>
            </div>
            <div class="col-md-4">
                <form action="" class="form-inline">
                    <div class="form-group">
                        <label class="form-label">仓库编号：</label>
                        <select class="form-control" id="search_repository_ID">
                        </select>
                    </div>
                </form>
            </div>
            <div class="col-md-4">
                <button class="btn btn-success" id="search_button">
                    <span class="glyphicon glyphicon-search"></span> <span>查询</span>
                </button>
            </div>
        </div>
        <div class="row" style="margin-top:20px">
            <div class="col-md-12">
                <form action="" class="form-inline">
                    <label class="form-label">日期范围：</label>
                    <input class="form_date form-control" value="" id="search_start_date" name="" placeholder="开始日期">
                    <label class="form-label">&nbsp;&nbsp;-&nbsp;&nbsp;</label>
                    <input class="form_date form-control" value="" id="search_end_date" name="" placeholder="结束日期">
                </form>
            </div>
        </div>
        <div class="row" style="margin-top:50px">
            <div class="col-md-12">
                <table id="stockRecords" class="table table-striped"></table>
            </div>
        </div>
    </div>
</div>