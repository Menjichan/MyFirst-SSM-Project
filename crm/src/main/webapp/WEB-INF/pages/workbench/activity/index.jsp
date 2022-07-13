<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
%>
<html>
<head>
    <base href="<%=basePath%>">
    <meta charset="UTF-8">

    <link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet"/>
    <link rel="stylesheet" type="text/css"
          href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css"/>
    <link rel="stylesheet" type="text/css" href="jquery/bs_pagination-master/css/jquery.bs_pagination.min.css"/>

    <script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
    <script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
    <script type="text/javascript"
            src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>
    <script type="text/javascript" src="jquery/bs_pagination-master/js/jquery.bs_pagination.min.js"></script>
    <script type="text/javascript" src="jquery/bs_pagination-master/localization/en.js"></script>

    <script type="text/javascript">

        $(function () {
            //给"创建"按钮添加单机事件
            $("#createActivityBtn").click(function () {
                //将模态窗口里的内容清空
                //重置表单
                $("#createActivityForm").get(0).reset();
                //弹出创建市场活动的模态窗口
                $("#createActivityModal").modal("show");
            });

            //给开始日期和结束日期添加日历插件
            $(".myDate").datetimepicker({
                language: 'zh-CN',//语言种类
                format: 'yyyy-mm-dd',//日期格式
                minView: 'month',//可以选择的最小视图
                initialDate: new Date(),//初始化为今天时间
                autoclose: true,//选择完即关闭
                todayBtn: true,//是否显示"今天"的按钮
                clearBtn: true,//是否显示"清空"按钮，因为插件中该按键使用的是英文，可以自己进去修改成中文
            });

            //给"保存"按钮添加单击事件
            $("#saveCreateActivityBtn").click(function () {
                //收集要发送的参数
                var owner = $("#create-marketActivityOwner").val();
                var name = $.trim($("#create-marketActivityName").val());
                var startDate = $("#create-startDate").val();
                var endDate = $("#create-endDate").val();
                var cost = $.trim($("#create-cost").val());
                var description = $.trim($("#create-description").val());
                //表单验证
                if (owner == "") {
                    alert("所有者不能为空");
                    return;
                }
                if (name == "") {
                    alert("名称不能为空");
                    return;
                }
                if (startDate != "" && endDate != "") {
                    // alert(startDate);
                    // alert(endDate);
                    //使用字符串的大小代替日期的大小
                    if (endDate < startDate) {
                        alert("结束日期不能比开始日期小");
                        return;
                    }
                }
                var regExp = /^(([1-9]\d*)|0)$/;
                if (!regExp.test(cost)) {
                    alert("成本必须为非负整数");
                    return;
                }
                //发送异步请求
                $.ajax({
                    url: 'workbench/activity/createActivity.do',
                    data: {
                        owner: owner,
                        name: name,
                        startDate: startDate,
                        endDate: endDate,
                        cost: cost,
                        description: description
                    },
                    type: 'post',
                    dataType: 'json',
                    success: function (data) {
                        if (data.code == "1") {
                            $("#createActivityModal").modal("hide");
                            //刷新市场活动列表，还未完成
                            queryActivityByConditionForPage(1, $("#demo01_div").bs_pagination('getOption', 'rowsPerPage'));
                        } else {
                            //显示错误信息
                            alert(data.message);
                            //不关闭模态窗口
                            $("#createActivityModal").modal("show");
                        }
                    }
                });

            });

            //给关闭按钮添加点击事件
            $("#closeCreateActivityBtn").click(function () {
                $("#createActivityModal").modal("hide");
            })

            //当市场活动页面加载完成，查询市场活动列表第一页以及总条数
            queryActivityByConditionForPage(1, 10);

            //给查询按钮绑定单机事件
            $("#queryActivityBtn").click(function () {
                queryActivityByConditionForPage(1, $("#demo01_div").bs_pagination('getOption', 'rowsPerPage'));
            });

            //给全选按钮添加单击事件
            $("#checkAll").click(function () {
                $("#tbody_activityList input[type='checkbox']").prop("checked", this.checked)
            });

            //给列表中的 checkbox 添加单击事件
            $("#tbody_activityList").on("click", "input[type='checkbox']", function () {
                if ($("#tbody_activityList input[type='checkbox']").size() ==
                    $("#tbody_activityList input[type='checkbox']:checked").size()) {
                    $("#checkAll").prop("checked", true);
                } else {
                    //如果列表中的 checkbox 至少有一个没被选择，全选按钮就取消
                    $("#checkAll").prop("checked", false);
                }
            });

            //给删除按钮添加单击事件
            $("#deleteActivityBtn").click(function () {
                //收集参数
                //获取所有被选中的checkbox
                //alert("aaa");
                var checkedIds = $("#tbody_activityList input[type='checkbox']:checked");
                if (checkedIds.size() == 0) {
                    alert("请选择需要删除的市场活动");
                    return;
                }
                if (window.confirm("请确认是否删除?")) {
                    var ids = "";
                    $.each(checkedIds, function () {
                        ids += "id=" + this.value + "&";
                    });
                    ids = ids.substr(0,ids.length-1);
                    //alert(ids);
                    $.ajax({
                        url:'workbench/activity/deleteActivityByIds.do',
                        data:ids,
                        type:'post',
                        dataType:'json',
                        success:function (data) {
                            if (data.code == "1") {
                                alert("删除成功");
                                queryActivityByConditionForPage(1,$("#demo01_div").bs_pagination('getOption', 'rowsPerPage'));
                            } else {
                                alert(data.message);
                            }
                        }
                    });
                }
            });

            //给"修改"按钮添加单击事件
            $("#editActivityBtn").click(function () {
                //收集参数
                //获取列表中被选中的checkbox
                var checkedIds = $("#tbody_activityList input[type='checkbox']:checked");
                if (checkedIds.size() == 0) {
                    alert("请选择需要修改的市场活动");
                    return;
                } else if (checkedIds.size() > 1) {
                    alert("每次只能修改一条市场活动");
                    return;
                } else {
                    var id = checkedIds.val();
                    //var id = checkedIds.get(0).value;
                    //var id = checkedIds[0].value;
                    //发送请求
                    $.ajax({
                        url:'workbench/activity/queryActivityById.do',
                        data:{
                            id:id
                        },
                        type:'post',
                        dataType:'json',
                        success:function (data) {
                            //回显数据
                            $("#edit-id").val(data.id);
                            $("#edit-marketActivityOwner").val(data.owner);
                            $("#edit-marketActivityName").val(data.name);
                            $("#edit-startDate").val(data.startDate);
                            $("#edit-endDate").val(data.endDate);
                            $("#edit-cost").val(data.cost);
                            $("#edit-description").val(data.description);
                            $("#editActivityModal").modal("show");
                        }
                    })
                }
            });

            //给更新按钮添加单击事件
            $("#saveEditActivityBtn").click(function () {
                //收集参数
                var id = $("#edit-id").val();
                var owner = $("#edit-marketActivityOwner").val();
                var name = $.trim($("#edit-marketActivityName").val());
                var startDate = $("#edit-startDate").val();
                var endDate = $("#edit-endDate").val();
                var cost = $.trim($("#edit-cost").val());
                var description = $.trim($("#edit-description").val());
                //表单验证
                if (owner == "") {
                    alert("所有者不能为空");
                    return;
                }
                if (name == "") {
                    alert("名称不能为空");
                    return;
                }
                if (startDate != "" && endDate != "") {
                    // alert(startDate);
                    // alert(endDate);
                    //使用字符串的大小代替日期的大小
                    if (endDate < startDate) {
                        alert("结束日期不能比开始日期小");
                        return;
                    }
                }
                var regExp = /^(([1-9]\d*)|0)$/;
                if (!regExp.test(cost)) {
                    alert("成本必须为非负整数");
                    return;
                }
                //发送请求
                $.ajax({
                    url:'workbench/activity/saveEditActivity.do',
                    data:{
                      id:id,
                      owner:owner,
                      name:name,
                      startDate:startDate,
                      endDate:endDate,
                      cost:cost,
                      description:description
                    },
                    type:"post",
                    dataType:'json',
                    success:function (data) {
                        if (data.code == "1") {
                            //关闭模态窗口
                            alert("修改成功");
                            $("#editActivityModal").modal("hide");
                            //刷新市场活动，保持页号和每页显示条数不变
                            queryActivityByConditionForPage($("#demo01_div").bs_pagination('getOption', 'currentPage'),$("#demo01_div").bs_pagination('getOption', 'rowsPerPage'));
                        } else {
                            alert(data.message);
                            //模态窗口不关闭
                            $("#editActivityModal").modal("show");
                        }
                    }
                });
            })

            //给"批量导出"绑定点击事件
            $("#exportActivityAllBtn").click(function () {
               window.location.href="workbench/activity/exportAllActivities.do";
            });

            //给"选择导出"绑定点击事件
            $("#exportActivityXzBtn").click(function () {
                //验证是否有选择需要导出的活动
                var checkedIds = $("#tbody_activityList input[type='checkbox']:checked");
                if (checkedIds.size() == 0) {
                    alert("请至少选择一条需要导出的市场活动");
                    return;
                }
                //收集参数
                var ids = "";
                $.each(checkedIds, function () {
                    ids += "id=" + this.value + "&";
                });
                ids = ids.substr(0,ids.length-1);
                //发送请求
                window.location.href="workbench/activity/exportSelectedActivities.do?" + ids;
            });

            //给"导入"按钮添加点击事件
            $("#importActivityBtn").click(function () {
                //验证文件格式
                var filename = $("#activityFile").val();
                var suffix = filename.substr(filename.lastIndexOf(".") + 1).toUpperCase();
                if (suffix != "XLS") {
                    alert("上传的文件格式必须为xls");
                    return;
                }
                //获取文件
                var activityList = $("#activityFile")[0].files[0];
                if (activityList.size > 5 * 1024 * 1024) {
                    alert("文件大小不能超过5MB");
                    return;
                }
                //发送请求
                var formData = new FormData();
                formData.append("activityList",activityList);
                $.ajax({
                    url:'workbench/activity/importActivities.do',
                    data:formData,
                    processData:false,//设置ajax向后台提交参数之前，是否把参数统一转换成字符串，可以设置true或者false，默认是true
                    contentType:false,//设置ajax向后台提交参数之前，是否把参数统一按urlencoded进行编码，默认是true
                    type:'post',
                    dataType:'json',
                    success:function (data) {
                        if (data.code == "1") {
                            alert("成功导入" + data.respData + "条记录");
                            $("#importActivityModal").modal("hide");
                            queryActivityByConditionForPage(1,$("#demo01_div").bs_pagination('getOption', 'rowsPerPage'));
                        } else {
                            alert(data.message);
                            $("#importActivityModal").modal("show");
                        }
                    }
                });
            });

            //给"导入市场活动"按钮添加单击事件，每次点击清除上一次的内容
            $("#importActivityByListBtn").click(function () {
                $("#activityFile").val("");
            });
        });

        function queryActivityByConditionForPage(pageNum, pageSize) {
            //FirstStep:收集参数
            var name = $("#query-name").val();
            var owner = $("#query-owner").val();
            var startDate = $("#query-startDate").val();
            var endDate = $("#query-endDate").val();
            //var pageNum = 1;
            //var pageSize = 10;
            //SecondStep:发送异步请求
            $.ajax({
                url: 'workbench/activity/queryActivityByConditionForPage.do',
                data: {
                    name: name,
                    owner: owner,
                    startDate: startDate,
                    endDate: endDate,
                    pageNum: pageNum,
                    pageSize: pageSize
                },
                type: 'post',
                dataType: 'json',
                success: function (data) {
                    //显示总条数
                    //$("#totalRowsB").text(data.countOfActivity);
                    //遍历市场活动列表，进行显示
                    var htmlStr = "";
                    $.each(data.activityList, function (index, obj) {
                        htmlStr += "<tr class=\"active\">";
                        htmlStr += "<td><input type=\"checkbox\" value=\"" + obj.id + "\"/></td>";
                        htmlStr += "<td><a style=\"text-decoration: none; cursor: pointer;\" onclick=\"window.location.href='workbench/activity/checkActivityDetail.do?id="+obj.id+"'\">" + obj.name + "</a></td>";
                        htmlStr += "<td>" + obj.owner + "</td>";
                        htmlStr += "<td>" + obj.startDate + "</td>";
                        htmlStr += "<td>" + obj.endDate + "</td>";
                        htmlStr += "</tr>";
                    });
                    $("#tbody_activityList").html(htmlStr);
                    //市场活动列表拼接完成后取消全选按钮
                    $("#checkAll").prop("checked", false);
                    //计算总页数
                    var totalPages = 1;
                    if (data.countOfActivity % pageSize == 0) {
                        totalPages = data.countOfActivity / pageSize;
                    } else {
                        totalPages = parseInt(data.countOfActivity / pageSize) + 1;
                    }

                    //数据查询成功后显示分页栏
                    $(function () {
                        $("#demo01_div").bs_pagination({
                            currentPage: pageNum,//当前页数
                            rowsPerPage: pageSize,//每页显示多少条记录
                            totalPages: totalPages,//总页数，该插件必填参数
                            totalRows: data.countOfActivity,//总记录数

                            visiblePageLinks: 5,//页码卡片最多显示多少个

                            showGoToPage: true,//是否显示输入页码跳转页面，默认是true
                            showRowsPerPage: true,//是否显示每页条数，默认是true
                            showRowsInfo: true,//是否显示记录信息，默认是true

                            onChangePage: function (event, pageObj) {
                                //alert(pageObj.currentPage);
                                //alert(pageObj.rowsPerPage);
                                queryActivityByConditionForPage(pageObj.currentPage, pageObj.rowsPerPage);
                            }
                        });
                    });
                }
            });
        }
    </script>
</head>
<body>

<!-- 创建市场活动的模态窗口 -->
<div class="modal fade" id="createActivityModal" role="dialog">
    <div class="modal-dialog" role="document" style="width: 85%;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">
                    <span aria-hidden="true">×</span>
                </button>
                <h4 class="modal-title" id="myModalLabel1">创建市场活动</h4>
            </div>
            <div class="modal-body">

                <form id="createActivityForm" class="form-horizontal" role="form">

                    <div class="form-group">
                        <label for="create-marketActivityOwner" class="col-sm-2 control-label">所有者<span
                                style="font-size: 15px; color: red;">*</span></label>
                        <div class="col-sm-10" style="width: 300px;">
                            <select class="form-control" id="create-marketActivityOwner">
                                <c:forEach items="${userList}" var="user">
                                    <option value="${user.id}">${user.name}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <label for="create-marketActivityName" class="col-sm-2 control-label">名称<span
                                style="font-size: 15px; color: red;">*</span></label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="create-marketActivityName">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="create-startDate" class="col-sm-2 control-label">开始日期</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control myDate" id="create-startDate" readonly>
                        </div>
                        <label for="create-endDate" class="col-sm-2 control-label">结束日期</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control myDate" id="create-endDate" readonly>
                        </div>
                    </div>
                    <div class="form-group">

                        <label for="create-cost" class="col-sm-2 control-label">成本</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="create-cost">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="create-description" class="col-sm-2 control-label">描述</label>
                        <div class="col-sm-10" style="width: 81%;">
                            <textarea class="form-control" rows="3" id="create-description"></textarea>
                        </div>
                    </div>

                </form>

            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal" id="closeCreateActivityBtn">关闭
                </button>
                <button type="button" class="btn btn-primary" id="saveCreateActivityBtn">保存</button>
            </div>
        </div>
    </div>
</div>

<!-- 修改市场活动的模态窗口 -->
<div class="modal fade" id="editActivityModal" role="dialog">
    <div class="modal-dialog" role="document" style="width: 85%;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">
                    <span aria-hidden="true">×</span>
                </button>
                <h4 class="modal-title" id="myModalLabel2">修改市场活动</h4>
            </div>
            <div class="modal-body">

                <form class="form-horizontal" role="form">
                    <input type="hidden" name="id" id="edit-id">
                    <div class="form-group">
                        <label for="edit-marketActivityOwner" class="col-sm-2 control-label">所有者<span
                                style="font-size: 15px; color: red;">*</span></label>
                        <div class="col-sm-10" style="width: 300px;">
                            <select class="form-control" id="edit-marketActivityOwner">
                                <c:forEach items="${userList}" var="user">
                                    <option value="${user.id}">${user.name}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <label for="edit-marketActivityName" class="col-sm-2 control-label">名称<span
                                style="font-size: 15px; color: red;">*</span></label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="edit-marketActivityName" value="发传单">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="edit-startDate" class="col-sm-2 control-label">开始日期</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control myDate" id="edit-startDate" value="2020-10-10" readonly>
                        </div>
                        <label for="edit-endDate" class="col-sm-2 control-label">结束日期</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control myDate" id="edit-endDate" value="2020-10-20" readonly>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="edit-cost" class="col-sm-2 control-label">成本</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="edit-cost" value="5,000">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="edit-description" class="col-sm-2 control-label">描述</label>
                        <div class="col-sm-10" style="width: 81%;">
                            <textarea class="form-control" rows="3" id="edit-description">市场活动Marketing，是指品牌主办或参与的展览会议与公关市场活动，包括自行主办的各类研讨会、客户交流会、演示会、新产品发布会、体验会、答谢会、年会和出席参加并布展或演讲的展览会、研讨会、行业交流会、颁奖典礼等</textarea>
                        </div>
                    </div>

                </form>

            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" id="saveEditActivityBtn">更新</button>
            </div>
        </div>
    </div>
</div>

<!-- 导入市场活动的模态窗口 -->
<div class="modal fade" id="importActivityModal" role="dialog">
    <div class="modal-dialog" role="document" style="width: 85%;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">
                    <span aria-hidden="true">×</span>
                </button>
                <h4 class="modal-title" id="myModalLabel">导入市场活动</h4>
            </div>
            <div class="modal-body" style="height: 350px;">
                <div style="position: relative;top: 20px; left: 50px;">
                    请选择要上传的文件：<small style="color: gray;">[仅支持.xls]</small>
                </div>
                <div style="position: relative;top: 40px; left: 50px;">
                    <input type="file" id="activityFile">
                </div>
                <div style="position: relative; width: 400px; height: 320px; left: 45% ; top: -40px;">
                    <h3>重要提示</h3>
                    <ul>
                        <li>操作仅针对Excel，仅支持后缀名为XLS的文件。</li>
                        <li>给定文件的第一行将视为字段名。</li>
                        <li>请确认您的文件大小不超过5MB。</li>
                        <li>日期值以文本形式保存，必须符合yyyy-MM-dd格式。</li>
                        <li>日期时间以文本形式保存，必须符合yyyy-MM-dd HH:mm:ss的格式。</li>
                        <li>默认情况下，字符编码是UTF-8 (统一码)，请确保您导入的文件使用的是正确的字符编码方式。</li>
                        <li>建议您在导入真实数据之前用测试文件测试文件导入功能。</li>
                    </ul>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button id="importActivityBtn" type="button" class="btn btn-primary">导入</button>
            </div>
        </div>
    </div>
</div>


<div>
    <div style="position: relative; left: 10px; top: -10px;">
        <div class="page-header">
            <h3>市场活动列表</h3>
        </div>
    </div>
</div>
<div style="position: relative; top: -20px; left: 0px; width: 100%; height: 100%;">
    <div style="width: 100%; position: absolute;top: 5px; left: 10px;">

        <div class="btn-toolbar" role="toolbar" style="height: 80px;">
            <form class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;">

                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">名称</div>
                        <input class="form-control" type="text" id="query-name">
                    </div>
                </div>

                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">所有者</div>
                        <input class="form-control" type="text" id="query-owner">
                    </div>
                </div>


                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">开始日期</div>
                        <input class="form-control" type="text" id="query-startDate"/>
                    </div>
                </div>
                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">结束日期</div>
                        <input class="form-control" type="text" id="query-endDate">
                    </div>
                </div>

                <button type="button" class="btn btn-default" id="queryActivityBtn">查询</button>

            </form>
        </div>
        <div class="btn-toolbar" role="toolbar"
             style="background-color: #F7F7F7; height: 50px; position: relative;top: 5px;">
            <div class="btn-group" style="position: relative; top: 18%;">
                <button type="button" class="btn btn-primary" id="createActivityBtn"><span
                        class="glyphicon glyphicon-plus"></span> 创建
                </button>
                <button type="button" class="btn btn-default" id="editActivityBtn"><span
                        class="glyphicon glyphicon-pencil"></span> 修改
                </button>
                <button type="button" class="btn btn-danger"><span class="glyphicon glyphicon-minus"
                                                                   id="deleteActivityBtn"></span> 删除
                </button>
            </div>
            <div class="btn-group" style="position: relative; top: 18%;">
                <button id="importActivityByListBtn" type="button" class="btn btn-default" data-toggle="modal" data-target="#importActivityModal">
                    <span class="glyphicon glyphicon-import"></span> 上传列表数据（导入）
                </button>
                <button id="exportActivityAllBtn" type="button" class="btn btn-default"><span
                        class="glyphicon glyphicon-export"></span> 下载列表数据（批量导出）
                </button>
                <button id="exportActivityXzBtn" type="button" class="btn btn-default"><span
                        class="glyphicon glyphicon-export"></span> 下载列表数据（选择导出）
                </button>
            </div>
        </div>
        <div style="position: relative;top: 10px;">
            <table class="table table-hover">
                <thead>
                <tr style="color: #B3B3B3;">
                    <td><input type="checkbox" id="checkAll"/></td>
                    <td>名称</td>
                    <td>所有者</td>
                    <td>开始日期</td>
                    <td>结束日期</td>
                </tr>
                </thead>
                <tbody id="tbody_activityList">
                <%--						<tr class="active">--%>
                <%--							<td><input type="checkbox" /></td>--%>
                <%--							<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='detail.jsp';">发传单</a></td>--%>
                <%--                            <td>zhangsan</td>--%>
                <%--							<td>2020-10-10</td>--%>
                <%--							<td>2020-10-20</td>--%>
                <%--						</tr>--%>
                <%--                        <tr class="active">--%>
                <%--                            <td><input type="checkbox" /></td>--%>
                <%--                            <td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='detail.jsp';">发传单</a></td>--%>
                <%--                            <td>zhangsan</td>--%>
                <%--                            <td>2020-10-10</td>--%>
                <%--                            <td>2020-10-20</td>--%>
                <%--                        </tr>--%>
                </tbody>
            </table>
            <div id="demo01_div"></div>
        </div>

        <%--			<div style="height: 50px; position: relative;top: 30px;">--%>
        <%--				<div>--%>
        <%--					<button type="button" class="btn btn-default" style="cursor: default;">共<b id="totalRowsB">50</b>条记录</button>--%>
        <%--				</div>--%>
        <%--				<div class="btn-group" style="position: relative;top: -34px; left: 110px;">--%>
        <%--					<button type="button" class="btn btn-default" style="cursor: default;">显示</button>--%>
        <%--					<div class="btn-group">--%>
        <%--						<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">--%>
        <%--							10--%>
        <%--							<span class="caret"></span>--%>
        <%--						</button>--%>
        <%--						<ul class="dropdown-menu" role="menu">--%>
        <%--							<li><a href="#">20</a></li>--%>
        <%--							<li><a href="#">30</a></li>--%>
        <%--						</ul>--%>
        <%--					</div>--%>
        <%--					<button type="button" class="btn btn-default" style="cursor: default;">条/页</button>--%>
        <%--				</div>--%>
        <%--				<div style="position: relative;top: -88px; left: 285px;">--%>
        <%--					<nav>--%>
        <%--						<ul class="pagination">--%>
        <%--							<li class="disabled"><a href="#">首页</a></li>--%>
        <%--							<li class="disabled"><a href="#">上一页</a></li>--%>
        <%--							<li class="active"><a href="#">1</a></li>--%>
        <%--							<li><a href="#">2</a></li>--%>
        <%--							<li><a href="#">3</a></li>--%>
        <%--							<li><a href="#">4</a></li>--%>
        <%--							<li><a href="#">5</a></li>--%>
        <%--							<li><a href="#">下一页</a></li>--%>
        <%--							<li class="disabled"><a href="#">末页</a></li>--%>
        <%--						</ul>--%>
        <%--					</nav>--%>
        <%--				</div>--%>
        <%--			</div>--%>

    </div>

</div>
</body>
</html>