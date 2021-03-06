<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>

<script>
    // 出入库记录查询参数
    search_type = 'searchAll';
    search_batchID = '';
    search_repositoryID = '';
    search_start_date = null;
    search_end_date = null;

    $(function(){
        batchSelectorInit();
        repositoryOptionInit();
        datePickerInit();
        storageListInit();
        searchAction();
    })

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
                    $('#search_batch_ID').append("<option value='" + elem.id + "'>第 " + elem.id +" 批次</option>");
                });
            },
            error : function(response){
                $('#search_batch_ID').append("<option value='-1'>加载失败</option>");
            }
        });
        $('#search_batch_ID').append("<option value='all'>请选择批次</option>");
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
                                field : 'recordID',
                                title : '记录ID'
                            // sortable: true
                            },
                            {
                                field : 'goodsName',
                                title : '商品名称'
                            },
                            {
                                field : 'batchCode',
                                title : '批次编号'
                            },
                            {
                                field : 'repositoryID',
                                title : '出/入库仓库ID'
                                //visible : false
                            },
                            {
                                field : 'number',
                                title : '数量'
                                //visible : false
                            },
                            {
                                field : 'time',
                                title : '日期'
                            },
                            {
                                field : 'personInCharge',
                                title : '经手人'
                            },
                            {
                                field : 'type',
                                title : '记录类型'
                            }
                             ],
                    url : 'stockRecordManage/searchStockRecord',
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

	// 分页查询参数
	function queryParams(params) {
		var temp = {
			limit : params.limit,
			offset : params.offset,
			searchType : search_type,
            batchID : search_batchID,
			repositoryID : search_repositoryID,
			startDate : search_start_date,
			endDate : search_end_date
		}
		return temp;
	}

	// 查询操作
	function searchAction(){
	    $('#search_button').click(function(){
	        search_batchID = $('#search_batch_ID').val();
	        search_repositoryID = $('#search_repository_ID').val();
	        search_type = $('#search_type').val();
	        search_start_date = $('#search_start_date').val();
	        search_end_date = $('#search_end_date').val();
	        tableRefresh();
	    })
	}

    // 表格刷新
    function tableRefresh() {
        $('#stockRecords').bootstrapTable('refresh', {
            query : {}
        });
    }

</script>

<div class="panel panel-default">
    <ol class="breadcrumb">
        <li>出入库业务流水</li>
    </ol>
    <div class="panel-body">
        <div class="row">
            <div class="col-md-3">
                <form action="" class="form-inline">
                    <div class="form-group">
                        <label class="form-label">批次ID号：</label>
                        <select class="form-control" id="search_batch_ID">
                        </select>
                    </div>
                </form>
            </div>
            <div class="col-md-3">
                <form action="" class="form-inline">
                    <div class="form-group">
                        <label class="form-label">仓库编号：</label>
                        <select class="form-control" id="search_repository_ID">
                        </select>
                    </div>
                </form>
            </div>
            <div class="col-md-3">
                <form action="" class="form-inline">
                    <label class="form-label">记录过滤：</label>
                    <select name="" id="search_type" class="form-control">
                        <option value="all">显示所有</option>
                        <option value="stockInOnly">仅显示入库记录</option>
                        <option value="stockOutOnly">仅显示出库记录</option>
                    </select>
                </form>
            </div>
            <div class="col-md-3">
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